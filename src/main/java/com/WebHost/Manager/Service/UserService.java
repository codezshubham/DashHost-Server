package com.WebHost.Manager.Service;

import com.WebHost.Manager.Model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User saveUser(User user);
    Optional<User> getUserById(Long id);
    Optional<User> getUserByEmail(String email);
    List<User> getAllUsers();
    void deleteUser(Long id);
    Optional<User> getUserByEmailWithClients(String email);
}

