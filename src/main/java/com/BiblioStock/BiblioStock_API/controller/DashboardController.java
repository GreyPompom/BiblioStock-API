package com.BiblioStock.BiblioStock_API.controller;

import com.BiblioStock.BiblioStock_API.dto.dashboard.ProductCountDTO;
import com.BiblioStock.BiblioStock_API.dto.dashboard.ProductSummaryDTO;
import com.BiblioStock.BiblioStock_API.dto.dashboard.StockValueDTO;
import com.BiblioStock.BiblioStock_API.service.DashboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.BiblioStock.BiblioStock_API.dto.dashboard.DashboardOverviewDTO;
import com.BiblioStock.BiblioStock_API.dto.dashboard.MovementSummaryDTO;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }
    
    @GetMapping("/overview")
    public DashboardOverviewDTO getOverview() {
        return dashboardService.getOverview();
    }

    @GetMapping("/products/total")
    public ProductCountDTO getTotalProducts() {
        return dashboardService.getTotalProducts();
    }

    @GetMapping("/products/latest")
    public List<ProductSummaryDTO> getLast4Products() {
        return dashboardService.getLast4Products();
    }
    
    @GetMapping("/movements/summary")
    public MovementSummaryDTO getMovementSummary() {
        return dashboardService.getMovementSummary();
    }    
    @GetMapping("/stock/value")
    public StockValueDTO getTotalStockValue() {
        return dashboardService.getTotalStockValue();
    }
}
    
