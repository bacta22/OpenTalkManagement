package com.ncc.asia.modelmapper;

import com.ncc.asia.entity.User;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    public UserDTOModelMapper convert(User user) {
        TypeMap<User,UserDTOModelMapper> propertyMapper =
                modelMapper().createTypeMap(User.class,UserDTOModelMapper.class);
        propertyMapper.addMappings(mapper ->
                mapper.map(User::getRoles
//                        .stream().map(Role::getName).collect(Collectors.toSet())
                        ,UserDTOModelMapper::setRoles));
        return modelMapper().map(user,UserDTOModelMapper.class);
    }

}
