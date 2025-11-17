package com.BiblioStock.BiblioStock_API.service;

import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.BiblioStock.BiblioStock_API.exception.BusinessException;

@Service
public class SettingsService {

    private final PriceAdjustmentService priceAdjustmentService;

    @Autowired
    public SettingsService(PriceAdjustmentService priceAdjustmentService) {
        this.priceAdjustmentService = priceAdjustmentService;
    }

    
    public BigDecimal getGlobalAdjustment() {
        return priceAdjustmentService.getLatestGlobalAdjustment();
    }

    /**
     * Define o percentual global e aplica o ajuste via função no banco
     *
     * @param percent percentual a ser aplicado (ex: 0.10 = 10%)
     * @param note observação opcional
     */
    @Transactional
    public void setGlobalAdjustment(BigDecimal percent, String note) {
        try {
            priceAdjustmentService.applyGlobalAdjustment(percent, note);
        } catch (DataAccessException ex) {
            throw new BusinessException("Erro ao definir percentual global: " + ex.getMessage());
        }
    }
}
