package vn.edu.fpt.comic.Service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import vn.edu.fpt.comic.Entities.Account;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AccountService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByUsername(username);
        if (account == null) {
            throw new UsernameNotFoundException("Account not found: " + username);
        }
        return org.springframework.security.core.userdetails.User
                .withUsername(account.getUsername())
                .password(account.getPassword())
                .authorities("ROLE_" + account.getRole())
                .build();
    }
    /**
     * Check if email already exists
     */
    public boolean isEmailExists(String email) {
        return accountRepository.findByEmail(email) != null;
    }

    /**
     * Verify email exists for password reset
     */
    public boolean emailExistsForReset(String email) {
        return accountRepository.findByEmail(email) != null;
    }


    /**
     * Update password for an account
     */
    public void updatePassword(String email, String newPassword) {
        Account account = accountRepository.findByEmail(email);
        if (account != null) {
            account.setPassword(passwordEncoder.encode(newPassword));
            account.setUpdated_at(new Date());
            accountRepository.save(account);
        }
    }
    /**
     * Validate account registration data
     *
     * @return Map of field errors (field name -> error message)
     */
    public Map<String, String> validateAccountRegistration(String userName, String password,
                                                           String confirmPassword, String fullName,
                                                           String email, String address) {
        Map<String, String> errors = new HashMap<>();

        // Validate userName
        if (userName == null || userName.trim().isEmpty()) {
            errors.put("userName", "Username must not be empty");
        } else if (userName.length() < 3 || userName.length() > 20) {
            errors.put("userName", "Username must be between 3 and 20 characters");
        } else if (!userName.matches("^(?=.*[A-Za-z])[A-Za-z0-9_]+$")) {
            errors.put("userName", "Username can only contain letters, numbers");
        } else {
            Account existingAccount = accountRepository.findByUsername(userName);
            if (existingAccount != null) {
                errors.put("userName", "Username is already in use");
            }
        }

        // Validate password
        if (password == null || password.trim().isEmpty()) {
            errors.put("password", "Password must not be empty");
        } else if (password.length() < 6) {
            errors.put("password", "Password must be at least 6 characters long");
        } else if (password.length() > 20) {
            errors.put("password", "Password must not exceed 20 characters");
        } else if (!password.matches("^(?=.*[A-Za-z])(?=.*\\d).+$")) {
            errors.put("password", "Password must contain at least one letter and one number");
        }

        // Validate confirmPassword
        if (confirmPassword == null || confirmPassword.trim().isEmpty()) {
            errors.put("confirmPassword", "Confirm password must not be empty");
        } else if (!confirmPassword.equals(password)) {
            errors.put("confirmPassword", "The verification password does not match");
        }

        // Validate fullName
        if (fullName == null || fullName.trim().isEmpty()) {
            errors.put("fullName", "Full name must not be empty");
        } else if (fullName.length() < 2 || fullName.length() > 50) {
            errors.put("fullName", "Full name must be between 2 and 50 characters");
        } else if (!fullName.matches("^([A-Z][a-z]+)(\\s[A-Z][a-z]+)*$")) {
            errors.put("fullName", "Full name must be capitalized each word (ex: Nguyen Van A)");
        }

        // Validate email
        if (email == null || email.trim().isEmpty()) {
            errors.put("email", "Email must not be empty");
        } else if (!emailValidator.isValid(email)) {
            errors.put("email", "Invalid email address");
        } else {
            Account existingAccount = accountRepository.findByEmail(email);
            if (existingAccount != null) {
                errors.put("email", "Email is already in use");
            }
        }

        // Validate address
        if (address == null || address.trim().isEmpty()) {
            errors.put("address", "Address must not be empty");
        } else if (address.length() < 2 || address.length() > 50) {
            errors.put("address", "Address must be between 2 and 50 characters");
        } else if (!address.matches("^(?!.*[.,#/^()'\\-]{2,})(?!-)[A-Za-z0-9\\s.,#/^()'\\-]{2,50}$")) {
            errors.put("address", "Invalid address format (ex: 123 Main St, Chicago)");
        }

        return errors;
    }
}
