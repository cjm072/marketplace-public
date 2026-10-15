package com.project.marketplace.service;

import com.project.marketplace.dto.*;
import com.project.marketplace.pojo.Address;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    UserResponseDto getUserById(Integer userId);
    void uploadProfilePicture(MultipartFile file, Integer userId);
    void register(UserRequestDto dto);
    String login(UserLoginDto dto);
    void changePassword(Integer userId, UserPasswordDto dto);
    void updateProfile(Integer userId, UserEditDto dto);
    void updateAddress(Address address, Integer userId);
}
