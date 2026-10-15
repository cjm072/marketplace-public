package com.project.marketplace.service;

import com.project.marketplace.dto.ProductRequestDto;
import com.project.marketplace.dto.ProductResponseDto;
import com.project.marketplace.mapper.ProductMapper;
import com.project.marketplace.pojo.*;
import com.project.marketplace.repository.DiscountRepository;
import com.project.marketplace.repository.ProductRepository;
import com.project.marketplace.repository.SellerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final SellerRepository sellerRepository;
    private final DiscountRepository discountRepository;

    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper, SellerRepository sellerRepository, DiscountRepository discountRepository) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.sellerRepository = sellerRepository;
        this.discountRepository = discountRepository;
    }

    @Override
    public List<ProductResponseDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return productMapper.toProductResponseDtoList(products);
    }

    @Override
    public List<ProductResponseDto> getAllApprovedProducts() {
        List<Product> products = productRepository.findByStatus(ProductStatus.APPROVED);
        return productMapper.toProductResponseDtoList(products);
    }

    @Override
    public ProductResponseDto getProductById(Integer productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NoSuchElementException("Product with ID " + productId + " not found"));
        return productMapper.toProductResponseDto(product);
    }

    @Override
    public List<ProductResponseDto> getProductsByName(String name) {
        List<Product> products = productRepository.findAllByNameContainingIgnoreCase(name);
        return productMapper.toProductResponseDtoList(products);
    }

    @Override
    public List<ProductResponseDto> getAllProductsBySeller(Integer sellerId) {
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new NoSuchElementException("Seller with ID " + sellerId + " not found"));
        List<Product> products = productRepository.findAllBySeller(seller);
        return productMapper.toProductResponseDtoList(products);
    }

    @Override
    public List<ProductResponseDto> getAllPendingProducts(){
        List<Product> pendingProducts = productRepository.findByStatus(ProductStatus.PENDING);
        return productMapper.toProductResponseDtoList(pendingProducts);
    }

    @Override
    public ProductResponseDto addProduct(ProductRequestDto dto) {
        Product product = productMapper.toProduct(dto);
        product.setStatus(ProductStatus.PENDING);
        Product savedProduct =  productRepository.save(product);
        return productMapper.toProductResponseDto(savedProduct);
    }

    @Override
    public ProductResponseDto updateProduct(ProductRequestDto dto, Integer productId) {
//        Product product = productRepository.findById(id).orElseThrow(() -> {
//            throw new IllegalArgumentException("Product not found");
//        });

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NoSuchElementException("Product with ID " + productId + " not found"));

        productMapper.updateProductFromDto(dto, product);
        Product savedProduct = productRepository.save(product);
        return productMapper.toProductResponseDto(savedProduct);
    }

    @Override
    public void deleteProduct(Integer productId){
        productRepository.deleteById(productId);
    }

    @Override
    public ProductResponseDto applyDiscountToProduct(Integer productId, Integer discountId){
        Discount discount = discountRepository.findById(discountId)
                .orElseThrow(() -> new NoSuchElementException("Discount with ID " + discountId + " not found"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NoSuchElementException("Product with ID " + productId + " not found"));
        var discountList = product.getDiscounts();
        discountList.add(discount);
        product.setDiscounts(discountList);
        BigDecimal newPrice = product.getPrice();
        newPrice = newPrice.subtract(discount.getAmount());
        newPrice = newPrice.max(BigDecimal.ZERO);
        product.setPrice(newPrice);
        Product savedProduct = productRepository.save(product);
        return productMapper.toProductResponseDto(savedProduct);
    }

    @Override
    public ProductResponseDto removeDiscountFromProduct(Integer productId, Integer discountId){
        Discount discount = discountRepository.findById(discountId)
                .orElseThrow(() -> new NoSuchElementException("Discount with ID " + discountId + " not found"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NoSuchElementException("Product with ID " + productId + " not found"));
        var discountList = product.getDiscounts();
        discountList.remove(discount);
        product.setDiscounts(discountList);
        BigDecimal newPrice = product.getPrice();
        newPrice = newPrice.add(discount.getAmount());
        product.setPrice(newPrice);
        Product savedProduct = productRepository.save(product);
        return productMapper.toProductResponseDto(savedProduct);
    }

    @Override
    public ProductResponseDto approveProduct(Integer productId){
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NoSuchElementException("Product with ID " + productId + " not found"));
        product.setStatus(ProductStatus.APPROVED);
        Product savedProduct = productRepository.save(product);
        return productMapper.toProductResponseDto(savedProduct);
    }

    @Override
    public ProductResponseDto rejectProduct(Integer productId){
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NoSuchElementException("Product with ID " + productId + " not found"));
        product.setStatus(ProductStatus.REJECTED);
        Product savedProduct = productRepository.save(product);
        return productMapper.toProductResponseDto(savedProduct);
    }

    @Override
    @Transactional
    public void uploadProductPicture(MultipartFile file, Integer productId) {
        try {
            String filename = file.getOriginalFilename();
            Path filePath = Paths.get("uploads/product_pics/", filename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            Path frontendPath = Paths.get("C:/fyp/marketplace/src/assets/", filename);
            Files.copy(file.getInputStream(), frontendPath, StandardCopyOption.REPLACE_EXISTING);

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new NoSuchElementException("Product with ID " + productId + " not found"));
            product.setImage(filename);
            productRepository.save(product);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file", e);
        }
    }
}
