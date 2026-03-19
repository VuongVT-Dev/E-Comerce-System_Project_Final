package vn.edu.fpt.comic.Entities.Service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import vn.edu.fpt.comic.Entities.Account;

import java.util.Date;

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
}
