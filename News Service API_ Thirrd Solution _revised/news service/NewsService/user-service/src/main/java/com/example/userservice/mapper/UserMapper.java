package com.example.userservice.mapper;

import com.example.userservice.dto.UserDTO;
import com.example.userservice.model.User;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;


@Mapper(config = MapStructConfig.class)
public interface UserMapper {

    User mapToEntity(UserDTO userDTO);

    @Named(value = "dto")
    UserDTO mapToDTO(User user);

    @IterableMapping(qualifiedByName = "entity")
    List<User> mapToEntityList(List<UserDTO> userDTOS);

    @IterableMapping(qualifiedByName = "dto")
    List<UserDTO> mapToDTOList(List<User> users);

    @IterableMapping(qualifiedByName = "dto")
    Set<UserDTO> mapToDTOSet(Set<User> users);
}
