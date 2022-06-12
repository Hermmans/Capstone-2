package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.User;

import java.util.List;

public interface UserDao {

    User[] findAll();

    User findById(Long id);




// might need? not currently implemented
    User findByUsername(String username);

    boolean create(String username, String password);
}
