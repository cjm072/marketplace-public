package com.project.marketplace.controller;

import com.project.marketplace.dto.*;
import com.project.marketplace.payload.ApiResponse;
import com.project.marketplace.pojo.Address;
import com.project.marketplace.service.SellerService;
import com.project.marketplace.service.UserService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/account")
public class AccountController {

    private final UserService userService;
    private final SellerService sellerService;

    public AccountController(UserService userService, SellerService sellerService) {
        this.userService = userService;
        this.sellerService = sellerService;
    }

    @GetMapping("{userId}")
    public ApiResponse<UserResponseDto> getAdmin(@PathVariable("userId") Integer userId) {
        UserResponseDto dto = userService.getUserById(userId);
        return ApiResponse.success(dto);
    }

    @PutMapping("/uploadProfilePicture")
    public ApiResponse<Void> uploadProfilePicture(@RequestParam("image") MultipartFile file,  @RequestParam("userId") Integer userId) {
        userService.uploadProfilePicture(file, userId);
        return ApiResponse.success();
    }

    @PostMapping("/login")
    public ApiResponse<String> login(@RequestBody UserLoginDto dto) {
        String token = userService.login(dto);
        return ApiResponse.success(token);
    }

    @PatchMapping("/profileInfo/{userId}")
    public ApiResponse<Void> updateProfile(@RequestBody UserEditDto dto, @PathVariable("userId") Integer userId) {
        userService.updateProfile(userId, dto);
        return ApiResponse.success();
    }

    @PatchMapping("/updatePassword/{userId}")
    public ApiResponse<Void> updatePassword(@RequestBody UserPasswordDto dto, @PathVariable("userId") Integer userId) {
        userService.changePassword(userId, dto);
        return ApiResponse.success();
    }

    @PatchMapping("/updateAddress/{userId}")
    public ApiResponse<Void> updateAddress(@RequestBody Address address, @PathVariable("userId") Integer userId) {
        userService.updateAddress(address, userId);
        return ApiResponse.success();
    }

    @GetMapping("/checkSellerStatus/{sellerId}")
    public ApiResponse<Boolean> checkSellerStatus(@PathVariable("sellerId") Integer sellerId) {
        boolean sellerStatus = sellerService.checkSellerStatus(sellerId);
        return ApiResponse.success(sellerStatus);
    }
}
