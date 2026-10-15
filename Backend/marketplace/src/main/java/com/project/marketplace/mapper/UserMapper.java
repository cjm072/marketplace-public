package com.project.marketplace.mapper;

import com.project.marketplace.dto.UserRequestDto;
import com.project.marketplace.dto.UserResponseDto;
import com.project.marketplace.pojo.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserMapper {
    public User toUser(UserRequestDto dto){
        if ( dto == null ) {
            return null;
        }
        User user = new User();
        updateUserFromDto(dto, user);
        return user;
    }

    public void updateUserFromDto(UserRequestDto dto, User user){
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setRole(dto.getRole());
    }

    public UserResponseDto toUserResponseDto(User user){
        if ( user == null ) {
            return null;
        }

        return new UserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPhone(),
                user.getImage(),
                user.getAddress()
        );
    }

    public List<UserResponseDto> toUserResponseDtoList(List<User> users){
        if ( users == null ) {
            return null;
        }
        List<UserResponseDto> list = new ArrayList<>( users.size() );
        for ( User user : users ) {
            list.add( toUserResponseDto(user) );
        }

        return list;
    }
}
