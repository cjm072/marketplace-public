package com.project.marketplace.service;

import com.project.marketplace.dto.OrderItemRequestDto;
import com.project.marketplace.dto.OrderItemResponseDto;
import com.project.marketplace.mapper.OrderItemMapper;
import com.project.marketplace.pojo.OrderItem;
import com.project.marketplace.repository.OrderItemRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;

    public OrderItemServiceImpl(OrderItemRepository orderItemRepository, OrderItemMapper orderItemMapper) {
        this.orderItemRepository = orderItemRepository;
        this.orderItemMapper = orderItemMapper;
    }

    @Override
    public List<OrderItem> addOrderItems(List<OrderItem> orderItems) {
        return orderItemRepository.saveAll(orderItems);
    }

    @Override
    public Map<String, Double> getCategorySalesPercentage(int year, int month) {
        List<Object[]> rawData = orderItemRepository.getTotalSalesPerCategoryByMonth(year, month);

        BigDecimal grandTotal = rawData.stream()
                .map(row -> (BigDecimal) row[1])
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, Double> percentageMap = new HashMap<>();
        for (Object[] row : rawData) {
            String category = row[0].toString();
            BigDecimal categoryTotal = (BigDecimal) row[1];
            double percentage = grandTotal.compareTo(BigDecimal.ZERO) == 0
                    ? 0.0
                    : categoryTotal.divide(grandTotal, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    .doubleValue();
            percentageMap.put(category, percentage);
        }

        return percentageMap;
    }

    @Override
    public Map<String, Double> getCategorySalesChangeComparedToLastMonth(int year, int month) {
        int prevYear = (month == 1) ? year - 1 : year;
        int prevMonth = (month == 1) ? 12 : month - 1;

        List<Object[]> currentMonthRaw = orderItemRepository.getTotalSalesPerCategoryByMonth(year, month);
        List<Object[]> lastMonthRaw = orderItemRepository.getTotalSalesPerCategoryByMonth(prevYear, prevMonth);

        Map<String, BigDecimal> currentMonthMap = new HashMap<>();
        for (Object[] row : currentMonthRaw) {
            currentMonthMap.put(row[0].toString(), (BigDecimal) row[1]);
        }

        Map<String, BigDecimal> lastMonthMap = new HashMap<>();
        for (Object[] row : lastMonthRaw) {
            lastMonthMap.put(row[0].toString(), (BigDecimal) row[1]);
        }

        Map<String, Double> result = new HashMap<>();

        Set<String> allCategories = new HashSet<>();
        allCategories.addAll(currentMonthMap.keySet());
        allCategories.addAll(lastMonthMap.keySet());

        for (String category : allCategories) {
            BigDecimal current = currentMonthMap.getOrDefault(category, BigDecimal.ZERO);
            BigDecimal last = lastMonthMap.getOrDefault(category, BigDecimal.ZERO);

            double change;

            if (last.compareTo(BigDecimal.ZERO) == 0) {
                if (current.compareTo(BigDecimal.ZERO) == 0) {
                    change = 0.0;
                } else {
                    change = 100.0;
                }
            } else {
                change = current.subtract(last)
                        .divide(last, 4, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100))
                        .doubleValue();
            }

            result.put(category, change);
        }

        return result;
    }

    @Override
    public Map<String, Double> getProductSalesPercentageBySeller(int year, int month, int sellerId) {
        List<Object[]> rawData = orderItemRepository.getTotalSalesPerProductBySellerAndMonth(year, month, sellerId);

        BigDecimal grandTotal = rawData.stream()
                .map(row -> (BigDecimal) row[1])
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, Double> percentageMap = new HashMap<>();
        for (Object[] row : rawData) {
            String productName = row[0].toString();
            BigDecimal productTotal = (BigDecimal) row[1];
            double percentage = grandTotal.compareTo(BigDecimal.ZERO) == 0
                    ? 0.0
                    : productTotal.divide(grandTotal, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    .doubleValue();
            percentageMap.put(productName, percentage);
        }

        return percentageMap;
    }

    @Override
    public Map<String, Double> getProductSalesChangeBySeller(int year, int month, int sellerId) {
        int prevYear = (month == 1) ? year - 1 : year;
        int prevMonth = (month == 1) ? 12 : month - 1;

        List<Object[]> currentMonthRaw = orderItemRepository.getTotalSalesPerProductBySellerAndMonth(year, month, sellerId);
        List<Object[]> lastMonthRaw = orderItemRepository.getTotalSalesPerProductBySellerAndMonth(prevYear, prevMonth, sellerId);

        Map<String, BigDecimal> currentMap = new HashMap<>();
        for (Object[] row : currentMonthRaw) {
            currentMap.put(row[0].toString(), (BigDecimal) row[1]);
        }

        Map<String, BigDecimal> lastMap = new HashMap<>();
        for (Object[] row : lastMonthRaw) {
            lastMap.put(row[0].toString(), (BigDecimal) row[1]);
        }

        Map<String, Double> result = new HashMap<>();
        Set<String> allProducts = new HashSet<>();
        allProducts.addAll(currentMap.keySet());
        allProducts.addAll(lastMap.keySet());

        for (String product : allProducts) {
            BigDecimal current = currentMap.getOrDefault(product, BigDecimal.ZERO);
            BigDecimal last = lastMap.getOrDefault(product, BigDecimal.ZERO);

            double change;
            if (last.compareTo(BigDecimal.ZERO) == 0) {
                change = current.compareTo(BigDecimal.ZERO) == 0 ? 0.0 : 100.0;
            } else {
                change = current.subtract(last)
                        .divide(last, 4, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100))
                        .doubleValue();
            }

            result.put(product, change);
        }

        return result;
    }

}

