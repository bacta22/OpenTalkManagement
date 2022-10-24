package com.ncc.asia.mapper;

import com.ncc.asia.dto.user.UserDTOModify;
import com.ncc.asia.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ModifyUserMapper {

    ModifyUserMapper INSTANCE = Mappers.getMapper(ModifyUserMapper.class);

    // for create or update
    default User getUserForModify (UserDTOModify modifyUserDTO) {
        User user = new User();
        user.setId(modifyUserDTO.getId());
        user.setUsername(modifyUserDTO.getUsername());
        user.setPassword(modifyUserDTO.getPassword());
        user.setFullName(modifyUserDTO.getFullName());
        user.setPhoneNumber(modifyUserDTO.getPhoneNumber());
        user.setBirthDay(modifyUserDTO.getBirthDay()); // invalid date => JSON parse exception
        user.setEnabled(modifyUserDTO.isEnabled());
        return user;
    }
}

