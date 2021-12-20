package com.newsservice.service;


import com.newsservice.model.User;

import java.util.List;

public interface UserService {

	void save(User user);

    User findByUsername(String username);

    List<User> getAll();

    User update(User user);

    void remove(Integer id);
}