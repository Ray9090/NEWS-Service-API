package com.example.userservice.service;

import com.example.userservice.dto.UserDTO;
import com.example.userservice.dto.handler.AuthenticateHandlerRequest;
import com.example.userservice.exception.*;
import com.example.userservice.mapper.UserMapper;
import com.example.userservice.model.ProfileRole;
import com.example.userservice.model.User;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

import static com.example.userservice.constants.ColumnName.EMAIL;
import static com.example.userservice.model.ProfileRole.*;

@Service
public class UserService {
    @Autowired
    protected PasswordEncoder passwordEncoder;
    @Autowired
    protected AuthenticationManager authenticationManager;
    @Autowired
    protected JwtTokenProvider jwtTokenProvider;
    @Autowired
    protected UserRepository userRepository;
    @Lazy
    @Autowired
    private UserMapper userMapper;




    @Transactional
    public UserDTO findByEmail(String email) {
        if (email == null) {
            throw new NullValueException(EMAIL);
        }
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(email));
        return userMapper.mapToDTO(user);
    }

    @Transactional
    public String checkEmailNotExist(String email) {
        if (email == null) {
            throw new NullValueException(EMAIL);
        }

        boolean isPresent = userRepository.findByEmail(email).isPresent();

        if (isPresent) {
            throw new EmailAlreadyExisteException();
        }

        return "Email not exist in db";
    }


    @Transactional
    public UserDTO findDTOById(Long id) {
        if (id == null) {
            throw new NullValueException("id");
        }
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(User.class, id));
        return userMapper.mapToDTO(user);
    }


    @Transactional
    public User findById(Long id) {
        if (id == null) {
            throw new NullValueException("id");
        }
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(User.class, id));
    }

    @Transactional
    public void createUser(UserDTO userDTO) {

        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new CredentialAlreadyExistsException("Email");
        }

        ProfileRole profileRoles = ProfileRole.NONE_ROLE;

        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userDTO.setEmail(userDTO.getEmail().toLowerCase());
        userDTO.setRole(profileRoles);


        userRepository.save(userMapper.mapToEntity(userDTO));
    }



    @Transactional
    public UserDTO authenticate(AuthenticateHandlerRequest user) {
       // log.debug("authenticate:: email: "+user.getEmail());

        if (user == null) {
            throw new NullValueException("user");
        }
        if (user.getEmail() == null) {
            throw new NullValueException(EMAIL);
        }
        if (user.getPassword() == null) {
            throw new NullValueException("password");
        }
        String username = user.getEmail().toLowerCase();
        UserDTO userDto = findByEmail(username);

        if (!passwordEncoder.matches(user.getPassword(), userDto.getPassword())) {
            throw new WrongPasswordException(User.class);
        }

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, user.getPassword()));

        userDto.setToken(jwtTokenProvider.createToken(username, findByEmail(username).getRole()));

        return userDto;
    }

    @Transactional
    public String deleteUser(Long id) {
        if (id == null) {
            throw new NullValueException("id");
        }
        User userToDelete = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(User.class, id));
        userRepository.delete(userToDelete);

        return "User is deleted";
    }


    @Transactional
    public UserDTO update(UserDTO userDto) {

        User user = userMapper.mapToEntity(findDTOById(userDto.getId()));

        try {

            user.setFirstName(userDto.getFirstName());
            user.setLastName(userDto.getLastName());
            user.setEmail(userDto.getEmail());
            user.setRole(userDto.getRole());

            return userMapper.mapToDTO(userRepository.save(user));

        } catch (Exception e) {
            throw new NotFoundException("Error while updating user : " + e);
        }

    }

    /**
     * Get authenticated user email
     * @return email
     */
    public String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null ? authentication.getName() : null;
    }


    /**
     * Change user Role
     * @param userId
     * @param role
     * @return
     */
    public String changeUserRole(Long userId, String role) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            user.get().setRole(getRole(role));
            userRepository.save(user.get());

            return "User "+ user.get().getUsername()+ " Role updated to " +role;

        }else {
            throw new NotFoundException("User");
        }
    }

    private ProfileRole getRole(String role){
        switch (role){
            case "user":
                return USER_ROLE;
            case "publisher":
                return PUBLISHER_ROLE;
            case "admin":
                return ADMIN_ROLE;
            default:
                throw new NotFoundException("Role");
        }
    }
}
