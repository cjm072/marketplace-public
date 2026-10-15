package com.project.marketplace.service;

import com.project.marketplace.dto.*;
import com.project.marketplace.mapper.UserMapper;
import com.project.marketplace.pojo.*;
import com.project.marketplace.repository.CustomerRepository;
import com.project.marketplace.repository.SellerRepository;
import com.project.marketplace.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.NoSuchElementException;

@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final CustomerRepository customerRepository;
    private final SellerRepository sellerRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(10);
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, CustomerRepository customerRepository, SellerRepository sellerRepository, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.customerRepository = customerRepository;
        this.sellerRepository = sellerRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Override
    public UserResponseDto getUserById(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User with ID " + userId + " not found"));
        return userMapper.toUserResponseDto(user);
    }

    @Override
    @Transactional
    public void uploadProfilePicture(MultipartFile file, Integer userId) {
        try {
            String filename = file.getOriginalFilename();
            Path filePath = Paths.get("uploads/profile_pics/", filename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            Path frontendPath = Paths.get("C:/fyp/marketplace/src/assets/", filename);
            Files.copy(file.getInputStream(), frontendPath, StandardCopyOption.REPLACE_EXISTING);

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new NoSuchElementException("User with ID " + userId + " not found"));
            user.setImage(filename);
            userRepository.save(user);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file", e);
        }
    }

    @Override
    @Transactional
    public void register(UserRequestDto dto) {
        User user = userMapper.toUser(dto);
        if (user.getRole() == UserRole.CUSTOMER) {
            Customer customer = new Customer();
            customer.setUsername(user.getUsername());
            customer.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            customer.setEmail(user.getEmail());
            customer.setPhone(user.getPhone());
            customer.setRole(user.getRole());
            customerRepository.save(customer);
        } else if (user.getRole() == UserRole.SELLER) {
            Seller seller = new Seller();
            seller.setUsername(user.getUsername());
            seller.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            seller.setEmail(user.getEmail());
            seller.setPhone(user.getPhone());
            seller.setRole(user.getRole());
            sellerRepository.save(seller);
        }
    }

    @Override
    public String login(UserLoginDto dto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.username(), dto.password()));
        User user = userRepository.findByUsername(dto.username());
         if (authentication.isAuthenticated()) {
             return jwtService.generateToken(user);
         }
         return null;
    }

    @Override
    public void changePassword(Integer userId, UserPasswordDto dto) {
        if (dto.newPassword().equals(dto.confirmPassword())) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new NoSuchElementException("User with ID " + userId + " not found"));
            user.setPassword(bCryptPasswordEncoder.encode(dto.newPassword()));
            userRepository.save(user);
        }
    }

    @Override
    public void updateProfile(Integer userId, UserEditDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User with ID " + userId + " not found"));
        user.setEmail(dto.email());
        user.setPhone(dto.phone());
        userRepository.save(user);
    }

    @Override
    public void updateAddress(Address address, Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User with ID " + userId + " not found"));
        user.setAddress(address);
        userRepository.save(user);
    }
}
