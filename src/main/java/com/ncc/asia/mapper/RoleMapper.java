package com.ncc.asia.mapper;


import com.ncc.asia.dto.role.RoleDTO;
import com.ncc.asia.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);

    RoleDTO roleToRoleDTO (Role role);

}
