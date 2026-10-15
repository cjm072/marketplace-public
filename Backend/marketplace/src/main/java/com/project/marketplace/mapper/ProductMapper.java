package com.project.marketplace.mapper;

import com.project.marketplace.dto.ProductRequestDto;
import com.project.marketplace.dto.ProductResponseDto;
import com.project.marketplace.pojo.Product;
import com.project.marketplace.pojo.Seller;
import com.project.marketplace.repository.SellerRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProductMapper {

    private final DiscountMapper discountMapper;
    private final SellerRepository sellerRepository;

    public ProductMapper(DiscountMapper discountMapper, SellerRepository sellerRepository) {
        this.discountMapper = discountMapper;
        this.sellerRepository = sellerRepository;
    }

    public Product toProduct(ProductRequestDto dto){
        if ( dto == null ) {
            return null;
        }

        Seller seller = sellerRepository.findById(dto.sellerId())
                .orElseThrow(() -> new NoSuchElementException("Seller with ID " + dto.sellerId() + " not found"));

        Product product = new Product();
        updateProductFromDto(dto, product);
        product.setSeller(seller);

        return product;
    }

    public void updateProductFromDto(ProductRequestDto dto, Product product){
        product.setName(dto.name());
        product.setDescription((dto.description()));
        product.setPrice(dto.price());
        product.setImage(dto.image());
        product.setQuantity(dto.quantity());
        product.setCategory(dto.category());
        product.setWeight(dto.weight());
        product.setMaterials(dto.materials());
        product.setCertifications(dto.certifications());
        product.setCo2(dto.co2());
    }

    public ProductResponseDto toProductResponseDto(Product product){
        if ( product == null ) {
            return null;
        }

        return new ProductResponseDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getImage(),
                product.getQuantity(),
                product.getCategory(),
                product.getStatus(),
                product.getMaterials(),
                product.getCertifications(),
                product.getCo2(),
                discountMapper.toDiscountResponseDtoList(product.getDiscounts()),
                product.getWeight(),
                product.getSeller().getId()
        );
    }

    public List<ProductResponseDto> toProductResponseDtoList(List<Product> products){
        if ( products == null ) {
            return null;
        }
        List<ProductResponseDto> list = new ArrayList<>( products.size() );
        for ( Product product : products ) {
            list.add( toProductResponseDto( product ) );
        }

        return list;
    }
}
