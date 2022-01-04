package com.newsservice.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import com.newsservice.model.News;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.newsservice.model.User;
import com.newsservice.repository.RoleRepository;
import com.newsservice.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void save(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles(user.getRoles());
        userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User update(User user) {
        Optional<User> _auction = userRepository.findById(user.getId());

        if (_auction.isPresent()) {
            User updateEntity = _auction.get();
            updateEntity.setUsername(user.getUsername());
            updateEntity.setPassword(user.getPassword());
            updateEntity.setRoles(user.getRoles());
            return userRepository.save(updateEntity);
        }

        return user;
    }

    @Override
    public void remove(Integer id) {
        userRepository.deleteById(id);
    }
}
