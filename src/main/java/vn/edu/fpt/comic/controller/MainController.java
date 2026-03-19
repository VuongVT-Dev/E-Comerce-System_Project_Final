package vn.edu.fpt.comic.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class MainController {
    // ==================== LOGIN ENDPOINTS ====================

    @GetMapping("/login")
    public String login(HttpServletRequest request, Model model) {
        return "login";
    }

    // ==================== FORGOT PASSWORD ENDPOINTS ====================

    @GetMapping("/forgot-password")
    public String forgotPassword(Model model) {
        return "forgot_password";
    }

    @PostMapping("/forgot-password-otp")
    @ResponseBody
    public Map<String, Object> forgotPasswordOTP(@RequestBody Map<String, String> requestData) {
        Map<String, Object> response = new HashMap<>();

        try {
            String action = requestData.get("action");

            if ("send".equals(action)) {
                String email = requestData.get("email");

                if (email == null || email.trim().isEmpty()) {
                    response.put("success", false);
                    response.put("message", "Email must not be empty");
                    return response;
                }

                email = email.trim().toLowerCase();

                if (!accountService.emailExistsForReset(email)) {
                    response.put("success", false);
                    response.put("message", "Email not found in system");
                    return response;
                }

                accountService.generateAndSendOTP(email, "password_reset");

                response.put("success", true);
                response.put("message", "OTP sent to email successfully");
                response.put("expiryMinutes", 5);
                response.put("email", email);

            } else if ("verify".equals(action)) {
                String email = requestData.get("email");
                String otp = requestData.get("otp");

                if (email == null || email.trim().isEmpty()) {
                    response.put("success", false);
                    response.put("message", "Email must not be empty");
                    return response;
                }

                if (otp == null || otp.trim().isEmpty()) {
                    response.put("success", false);
                    response.put("message", "OTP must not be empty");
                    return response;
                }

                email = email.trim().toLowerCase();
                otp = otp.trim();

                if (!otp.matches("^\\d{6}$")) {
                    response.put("success", false);
                    response.put("message", "OTP must be exactly 6 digits");
                    return response;
                }

                if (!accountService.verifyOTP(email, otp, "password_reset")) {
                    response.put("success", false);
                    response.put("message", "Invalid or expired OTP");
                    return response;
                }

                String resetToken = accountService.generateResetToken(email);

                response.put("success", true);
                response.put("message", "OTP verified successfully");
                response.put("resetToken", resetToken);
                response.put("email", email);

            } else if ("reset".equals(action)) {
                String email = requestData.get("email");
                String resetToken = requestData.get("resetToken");
                String newPassword = requestData.get("newPassword");
                String confirmPassword = requestData.get("confirmPassword");

                if (email == null || email.trim().isEmpty()) {
                    response.put("success", false);
                    response.put("message", "Email must not be empty");
                    return response;
                }

                if (resetToken == null || resetToken.trim().isEmpty()) {
                    response.put("success", false);
                    response.put("message", "Invalid session. Please start the password reset process again");
                    return response;
                }

                email = email.trim().toLowerCase();

                if (!accountService.verifyResetToken(email, resetToken)) {
                    response.put("success", false);
                    response.put("message", "Invalid or expired reset token");
                    return response;
                }

                Map<String, String> passwordErrors = accountService.validatePassword(newPassword, confirmPassword);
                if (!passwordErrors.isEmpty()) {
                    response.put("success", false);
                    response.put("errors", passwordErrors);
                    response.put("message", passwordErrors.values().iterator().next());
                    return response;
                }

                accountService.updatePassword(email, newPassword);
                accountService.clearResetToken(email);

                response.put("success", true);
                response.put("message", "Password reset successfully");
                response.put("redirect", "/login");

            } else if ("resend".equals(action)) {
                String email = requestData.get("email");

                if (email == null || email.trim().isEmpty()) {
                    response.put("success", false);
                    response.put("message", "Email must not be empty");
                    return response;
                }

                email = email.trim().toLowerCase();

                if (!accountService.emailExistsForReset(email)) {
                    response.put("success", false);
                    response.put("message", "Email not found in system");
                    return response;
                }

                accountService.resendOTP(email, "password_reset");

                response.put("success", true);
                response.put("message", "OTP resent successfully");
                response.put("expiryMinutes", 5);
                response.put("email", email);

            } else {
                response.put("success", false);
                response.put("message", "Invalid action");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "An error occurred: " + e.getMessage());
        }

        return response;
    }
}
