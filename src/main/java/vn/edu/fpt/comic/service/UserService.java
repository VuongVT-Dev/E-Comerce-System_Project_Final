package vn.edu.fpt.comic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.http.HttpServletRequest;

public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    AccountService accountService;

    @Autowired
    PasswordEncoder passwordEncoder;

    public User getCurrentUser(HttpServletRequest request) {
        String username = request.getRemoteUser();
        if (username == null) {
            throw new IllegalStateException("User not authenticated");
        }

        com.thunga.web.entity.Account account = accountService.findByUsername(username);
        if (account == null) {
            throw new IllegalStateException("Account not found");
        }

        User user = userRepository.findByAccount(account);
        if (user == null || user.getId() == null) {
            throw new IllegalStateException("User not found or not persisted");
        }

        return user;
    }
}
















