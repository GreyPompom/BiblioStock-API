package com.BiblioStock.BiblioStock_API.service;

import com.BiblioStock.BiblioStock_API.model.*;
import com.BiblioStock.BiblioStock_API.repository.*;
import com.BiblioStock.BiblioStock_API.dto.PriceAdjustmentRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.util.List;


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
    public void applyAdjustment(PriceAdjustmentRequestDTO dto) {
        String sql = "SELECT fn_apply_price_adjustment(?, ?, ?, ?, ?)";
        jdbcTemplate.queryForObject(sql, 
            Void.class,
            dto.getScopeType(),
            dto.getPercent(),
            dto.getAppliedBy(),
            dto.getCategoryId(),
            dto.getNote()
        );
    }

    public List<PriceAdjustment> listHistory() {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "appliedAt"));
    }
}
