package com.project.marketplace.service;

import com.project.marketplace.dto.SellerRequestDto;
import com.project.marketplace.dto.SellerResponseDto;
import com.project.marketplace.dto.UserRequestDto;
import com.project.marketplace.mapper.SellerMapper;
import com.project.marketplace.pojo.*;
import com.project.marketplace.repository.SellerRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class SellerServiceImpl implements SellerService{

    private final SellerRepository sellerRepository;
    private final SellerMapper sellerMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(10);

    public SellerServiceImpl(SellerRepository sellerRepository, SellerMapper sellerMapper) {
        this.sellerRepository = sellerRepository;
        this.sellerMapper = sellerMapper;
    }

    @Override
    public SellerResponseDto getSellerById(Integer sellerId){
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new NoSuchElementException("Seller with ID " + sellerId + " not found"));
        return sellerMapper.toSellerResponseDto(seller);
    }

    @Override
    public List<SellerResponseDto> getAllPendingSellers(){
        List<Seller> sellers = sellerRepository.findByAccountStatus(AccountStatus.PENDING);
        return sellerMapper.toSellerResponseDtoList(sellers);
    }

    @Override
    public SellerResponseDto addSeller(SellerRequestDto dto){
        Seller seller = sellerMapper.toSeller(dto);
        seller.setAccountStatus(AccountStatus.PENDING);
        Seller savedSeller = sellerRepository.save(seller);
        return sellerMapper.toSellerResponseDto(savedSeller);
    }

    @Override
    public SellerResponseDto updateSeller(SellerRequestDto dto, Integer sellerId){
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new NoSuchElementException("Seller with ID " + sellerId + " not found"));
        sellerMapper.updateSellerFromDto(dto, seller);
        Seller savedSeller = sellerRepository.save(seller);
        return sellerMapper.toSellerResponseDto(savedSeller);
    }

    @Override
    public void deleteSeller(Integer sellerId){
        sellerRepository.deleteById(sellerId);
    }

    @Override
    public SellerResponseDto approveSeller(Integer sellerId){
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new NoSuchElementException("Seller with ID " + sellerId + " not found"));
        seller.setAccountStatus(AccountStatus.APPROVED);
        Seller savedSeller = sellerRepository.save(seller);
        return sellerMapper.toSellerResponseDto(savedSeller);
    }

    @Override
    public SellerResponseDto rejectSeller(Integer sellerId){
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new NoSuchElementException("Seller with ID " + sellerId + " not found"));
        seller.setAccountStatus(AccountStatus.REJECTED);
        Seller savedSeller = sellerRepository.save(seller);
        return  sellerMapper.toSellerResponseDto(savedSeller);
    }

    @Override
    @Transactional
    public void register(SellerRequestDto dto) {
        Seller seller = sellerMapper.toSeller(dto);
        seller.setPassword(bCryptPasswordEncoder.encode(seller.getPassword()));
        seller.setAccountStatus(AccountStatus.PENDING);
        sellerRepository.save(seller);
    }

    @Override
    public boolean checkSellerStatus(Integer sellerId) {
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new NoSuchElementException("Seller with ID " + sellerId + " not found"));
        if(seller.getAccountStatus() == AccountStatus.APPROVED){
            return true;
        } else {
            return false;
        }
    }
}
