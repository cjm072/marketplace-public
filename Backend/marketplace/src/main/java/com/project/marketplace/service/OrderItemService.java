package com.project.marketplace.service;

import com.project.marketplace.pojo.OrderItem;

import java.util.List;
import java.util.Map;

public interface OrderItemService {
    List<OrderItem> addOrderItems(List<OrderItem> orderItems);
    Map<String, Double> getCategorySalesPercentage(int year, int month);
    Map<String, Double> getCategorySalesChangeComparedToLastMonth(int year, int month);
    Map<String, Double> getProductSalesPercentageBySeller(int year, int month, int sellerId);
    Map<String, Double> getProductSalesChangeBySeller(int year, int month, int sellerId);

}
