package org.example.user_management.service.interfaces;

import org.example.user_management.entity.User;

public interface IUserService {
    User register(User user);

    User authenticate(String username, String password);
}
