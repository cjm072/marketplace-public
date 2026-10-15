package com.project.marketplace.controller.seller;

import com.project.marketplace.payload.ApiResponse;
import com.project.marketplace.service.OrderItemService;
import com.project.marketplace.service.PaymentService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/seller/payment")
public class SellerAnalyticsController {
    private final PaymentService paymentService;
    private final OrderItemService orderItemService;

    public SellerAnalyticsController(PaymentService paymentService, OrderItemService orderItemService) {
        this.paymentService = paymentService;
        this.orderItemService = orderItemService;
    }

    @GetMapping("/countByMonth")
    public ApiResponse<Map<Integer, Long>> getPaymentCountPerMonthBySeller(@RequestParam int year, @RequestParam Integer sellerId) {
        Map<Integer, Long> data = paymentService.getPaymentCountPerMonthBySeller(year, sellerId);
        return ApiResponse.success(data);
    }

    @GetMapping("/amountByMonth")
    public ApiResponse<Map<Integer, BigDecimal>> getPaymentAmountPerMonthBySeller(@RequestParam int year, @RequestParam Integer sellerId) {
        Map<Integer, BigDecimal> data = paymentService.getPaymentAmountPerMonthBySeller(year, sellerId);
        return ApiResponse.success(data);
    }

    @GetMapping("/averageByMonth")
    public ApiResponse<Map<Integer, BigDecimal>> getAveragePaymentPerMonthBySeller(@RequestParam int year, @RequestParam Integer sellerId) {
        Map<Integer, BigDecimal> data = paymentService.getAveragePaymentPerMonthBySeller(year, sellerId);
        return ApiResponse.success(data);
    }

    @GetMapping("/productSalesPercentage")
    public ApiResponse<Map<String, Double>> getProductSalesPercentageBySeller(
            @RequestParam int year,
            @RequestParam int month,
            @RequestParam int sellerId
    ) {
        Map<String, Double> data = orderItemService.getProductSalesPercentageBySeller(year, month, sellerId);
        return ApiResponse.success(data);
    }

    @GetMapping("/productSalesChange")
    public ApiResponse<Map<String, Double>> getProductSalesChangeBySeller(
            @RequestParam int year,
            @RequestParam int month,
            @RequestParam int sellerId
    ) {
        Map<String, Double> data = orderItemService.getProductSalesChangeBySeller(year, month, sellerId);
        return ApiResponse.success(data);
    }

}
