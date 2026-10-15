package com.project.marketplace.controller.admin;

import com.project.marketplace.payload.ApiResponse;
import com.project.marketplace.service.OrderItemService;
import com.project.marketplace.service.PaymentService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/admin/payment")
public class AdminAnalyticsController {
    private final PaymentService paymentService;
    private final OrderItemService orderItemService;

    public AdminAnalyticsController(PaymentService paymentService, OrderItemService orderItemService) {
        this.paymentService = paymentService;
        this.orderItemService = orderItemService;
    }

    @GetMapping("/countByMonth")
    public ApiResponse<Map<Integer, Long>> getPaymentCountPerMonth(@RequestParam int year) {
        Map<Integer, Long> paymentPerMonth = paymentService.getPaymentCountPerMonth(year);
        return ApiResponse.success(paymentPerMonth);
    }

    @GetMapping("/amountByMonth")
    public ApiResponse<Map<Integer, BigDecimal>> getPaymentAmountPerMonth(@RequestParam int year) {
        Map<Integer, BigDecimal> amountPerMonth = paymentService.getPaymentAmountPerMonth(year);
        return ApiResponse.success(amountPerMonth);
    }

    @GetMapping("/averageByMonth")
    public ApiResponse<Map<Integer, BigDecimal>> getAvgPaymentPerOrderPerMonth(@RequestParam int year) {
        Map<Integer, BigDecimal> averageAmountPerMonth = paymentService.getAveragePaymentPerMonth(year);
        return ApiResponse.success(averageAmountPerMonth);
    }

    @GetMapping("/categorySalesPercentage")
    public ApiResponse<Map<String, Double>> getCategorySalesPercentage(
            @RequestParam int year,
            @RequestParam int month) {
        Map<String, Double> categoryPercentages = orderItemService.getCategorySalesPercentage(year, month);
        return ApiResponse.success(categoryPercentages);
    }

    @GetMapping("/categorySalesChange")
    public ApiResponse<Map<String, Double>> getCategorySalesChange(
            @RequestParam int year,
            @RequestParam int month) {
        Map<String, Double> percentageChanges = orderItemService.getCategorySalesChangeComparedToLastMonth(year,month);
        return ApiResponse.success(percentageChanges);
    }
}
