package vn.edu.fpt.comic.Controller;

import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AdmimController {








    //Phân trang , săp xep
    @GetMapping("/admin/manage-user")
    public String manageUser(Model model,
                             @RequestParam(required = false) String page,
                             @RequestParam(required = false) String sortBy) {
        int currentPage = 1;
        boolean invalidPage = false;

        if (page != null && !page.trim().isEmpty()) {
            try {
                currentPage = Integer.parseInt(page.trim());
                if (currentPage < 1) currentPage = 1;
            } catch (NumberFormatException e) {
                invalidPage = true;
                currentPage = 1;
            }
        }

        Page<User> userPage = userService.findByLimit(currentPage - 1, 10, sortBy);
        if (invalidPage || currentPage > userPage.getTotalPages()) {
            currentPage = 1;
            userPage = userService.findByLimit(0, 10, sortBy);
        }

        model.addAttribute("userList", userPage.getContent());
        model.addAttribute("totalPage", userPage.getTotalPages());
        model.addAttribute("page", currentPage);
        model.addAttribute("sortBy", sortBy);
        return "admin/user_ad";
    }
    public Map<String, String> validatePassword(String newPassword, String confirmPassword) {
        Map<String, String> errors = new HashMap<>();

        // Validate newPassword
        if (newPassword == null || newPassword.trim().isEmpty()) {
            errors.put("newPassword", "Password must not be empty");
        } else if (newPassword.length() < 6) {
            errors.put("newPassword", "Password must be at least 6 characters long");
        } else if (newPassword.length() > 20) {
            errors.put("newPassword", "Password must not exceed 20 characters");
        }

        // Validate confirmPassword
        if (confirmPassword == null || confirmPassword.trim().isEmpty()) {
            errors.put("confirmPassword", "Confirm password must not be empty");
        } else if (newPassword != null && !confirmPassword.equals(newPassword)) {
            errors.put("confirmPassword", "The verification password does not match");
        }

        return errors;
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
