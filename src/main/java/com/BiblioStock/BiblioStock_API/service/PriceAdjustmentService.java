package com.BiblioStock.BiblioStock_API.service;

import java.math.BigDecimal;
import java.sql.Types;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.BiblioStock.BiblioStock_API.dto.PriceAdjustmentRequestDTO;
import com.BiblioStock.BiblioStock_API.dto.PriceAdjustmentResponseDTO;
import com.BiblioStock.BiblioStock_API.exception.BusinessException;
import com.BiblioStock.BiblioStock_API.exception.ResourceNotFoundException;
import com.BiblioStock.BiblioStock_API.model.PriceAdjustment;
import com.BiblioStock.BiblioStock_API.repository.PriceAdjustmentRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PriceAdjustmentService {

    private final PriceAdjustmentRepository repository;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PriceAdjustmentService(PriceAdjustmentRepository repository, JdbcTemplate jdbcTemplate) {
        this.repository = repository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public String applyAdjustment(PriceAdjustmentRequestDTO dto) {
        Long appliedBy = 7L; // ID fixo para teste

        try {
            //arquitetura hexagonal 
            // Validações antes de chamar o banco
            validateAdjustmentRequest(dto);

            String sql = "SELECT public.fn_apply_price_adjustment(?, ?, ?, ?, ?)";

            String result = jdbcTemplate.queryForObject(
                    sql,
                    new Object[]{
                        dto.scopeType(),
                        dto.percent(),
                        appliedBy,
                        dto.categoryId(),
                        dto.note()
                    },
                    new int[]{
                        Types.VARCHAR,
                        Types.NUMERIC,
                        Types.BIGINT,
                        Types.BIGINT,
                        Types.VARCHAR
                    },
                    String.class
            );

            log.info("Ajuste de preço aplicado: {}", result);
            return result;

// {
//   "message": "Ajuste CATEGORIA aplicado com sucesso",
//   "products_updated": 15,
//   "product_ids": [1, 2, 3, 4, 5],
//   "scope_type": "CATEGORIA",
//   "percent_applied": 0.05
// }
        } catch (DataAccessException ex) {
            log.error("Erro ao aplicar ajuste de preço: {}", ex.getMessage());
            throw new BusinessException("Erro ao aplicar reajuste de preços: " + ex.getMostSpecificCause().getMessage());
        } catch (Exception ex) {
            log.error("Erro inesperado: {}", ex.getMessage());
            throw new BusinessException("Erro inesperado ao aplicar reajuste: " + ex.getMessage());
        }
    }

    public List<PriceAdjustment> listHistory() {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "appliedAt"));
    }

    @Transactional
    public void applyGlobalAdjustment(BigDecimal percent, String note) {
        PriceAdjustmentRequestDTO dto = new PriceAdjustmentRequestDTO(
                "GLOBAL",
                percent,
                null,
                note
        );
        applyAdjustment(dto);
    }

    /**
     * Helper simplificado para aplicar ajuste em categoria
     */
    @Transactional
    public void applyCategoryAdjustment(BigDecimal percent, Long categoryId, String note) {
        PriceAdjustmentRequestDTO dto = new PriceAdjustmentRequestDTO(
                "CATEGORIA",
                percent,
                categoryId,
                note
        );
        applyAdjustment(dto);
    }

    public BigDecimal getLatestGlobalAdjustment() {
        return repository
                .findFirstByScopeTypeOrderByAppliedAtDesc("GLOBAL")
                .map(PriceAdjustment::getPercent)
                .orElse(BigDecimal.ZERO);
    }

    private void validateAdjustmentRequest(PriceAdjustmentRequestDTO dto) {
        if (dto.scopeType() == null || (!dto.scopeType().equals("GLOBAL") && !dto.scopeType().equals("CATEGORIA"))) {
            throw new BusinessException("scopeType deve ser 'GLOBAL' ou 'CATEGORIA'");
        }

        if (dto.percent() == null || dto.percent().compareTo(new BigDecimal("-1")) < 0 || dto.percent().compareTo(new BigDecimal("10")) > 0) {
            throw new BusinessException("percent deve estar entre -1 e 10");
        }

        if (dto.scopeType().equals("CATEGORIA") && dto.categoryId() == null) {
            throw new BusinessException("categoryId é obrigatório para ajuste por CATEGORIA");
        }
    }

    public List<PriceAdjustmentResponseDTO> getAllCategoriesPercent() {
        return repository.findByScopeType("CATEGORIA")
                .stream()
                .map(pa -> new PriceAdjustmentResponseDTO(
                pa.getScopeType(),
                pa.getPercent(),
                pa.getCategory() != null ? pa.getCategory().getId() : null,
                pa.getCategory() != null ? pa.getCategory().getName() : null, 
                pa.getNote()
        ))
                .toList();
    }

    public PriceAdjustmentResponseDTO getCategoryPercent(Long categoryId) {
        PriceAdjustment entity = repository.findByScopeTypeAndCategoryId("CATEGORIA", categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Percentual não encontrado para a categoria " + categoryId));

        return PriceAdjustmentResponseDTO.fromEntity(entity);
    }
}
