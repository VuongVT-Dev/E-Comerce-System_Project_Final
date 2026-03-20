package vn.edu.fpt.comic.controller;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller

public class AdminController {

    // =====================================================
    // ADMIN LANGUAGE MANAGEMENT (/admin/*)
    // =====================================================

    @GetMapping("/admin/manage-language")
    public String adminManageLanguage(Model model, @RequestParam(required = false) String page) {
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
        Page<Language> languagePage = languageService.findByLimit(currentPage - 1, 10);
        if (invalidPage || currentPage > languagePage.getTotalPages()) {
            currentPage = 1;
            languagePage = languageService.findByLimit(0, 10);
        }

        Map<Integer, Long> bookCountMap = new HashMap<>();
        for (Language language : languagePage.getContent()) {
            bookCountMap.put(language.getId(), languageService.countBooksByLanguageId(language.getId()));
        }

        model.addAttribute("languageList", languagePage.getContent());
        model.addAttribute("totalPage", languagePage.getTotalPages());
        model.addAttribute("page", currentPage);
        model.addAttribute("bookCountMap", bookCountMap);
        return "admin/language_ad";
    }

    @PostMapping("/admin/create-language")
    public String adminCreateLanguage(HttpServletRequest request, Model model,
                                      RedirectAttributes redirectAttributes) {
        String name = request.getParameter("name");
        String code = request.getParameter("code");

        Map<String, String> errors = languageService.validateNewLanguage(name, code);

        if (!errors.isEmpty()) {
            if (errors.containsKey("name")) model.addAttribute("addNameError", errors.get("name"));
            if (errors.containsKey("code")) model.addAttribute("addCodeError", errors.get("code"));
            model.addAttribute("formName", name);
            model.addAttribute("formCode", code);
            model.addAttribute("showAddModal", true);
            return adminManageLanguage(model, request.getParameter("page"));
        }

        languageService.createLanguage(name, code, null);
        redirectAttributes.addFlashAttribute("successMessage", "Language created successfully");
        return "redirect:/admin/manage-language";
    }

    @PostMapping("/admin/edit-language")
    public String adminEditLanguage(HttpServletRequest request, Model model,
                                    RedirectAttributes redirectAttributes) {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            String newName = request.getParameter("name");

            Map<String, String> errors = languageService.validateEditLanguage(id, newName);

            if (!errors.isEmpty()) {
                if (errors.containsKey("name")) model.addAttribute("editNameError", errors.get("name"));
                model.addAttribute("editLanguageId", String.valueOf(id));
                model.addAttribute("formName", newName);
                model.addAttribute("showEditModal", true);
                return adminManageLanguage(model, request.getParameter("page"));
            }

            languageService.updateLanguage(id, newName);
            redirectAttributes.addFlashAttribute("successMessage", "Language updated successfully");
            return "redirect:/admin/manage-language";

        } catch (NumberFormatException e) {
            model.addAttribute("errorMessage", "Invalid language ID");
            return adminManageLanguage(model, request.getParameter("page"));
        }
    }


    // =====================================================
    // ADMIN STAFF MANAGEMENT (/admin/*)
    // =====================================================

    @GetMapping("/admin/manage-staff")
    public String adminManageStaff(Model model, @RequestParam(required = false) String page) {
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

        Page<Admin> adminPage = adminService.findByLimit(currentPage - 1, 10, null);
        if (invalidPage || currentPage > adminPage.getTotalPages()) {
            currentPage = 1;
            adminPage = adminService.findByLimit(0, 10, null);
        }

        Map<Integer, Integer> activeOrdersMap = new HashMap<>();
        for (Admin admin : adminPage.getContent()) {
            activeOrdersMap.put(admin.getId(), adminService.countActiveOrders(admin));
        }

        model.addAttribute("staffList", adminPage.getContent());
        model.addAttribute("totalPage", adminPage.getTotalPages());
        model.addAttribute("page", currentPage);
        model.addAttribute("activeOrdersMap", activeOrdersMap);
        return "admin/staff_ad";
    }

    @PostMapping("/admin/create-staff")
    @ResponseBody
    public Map<String, Object> adminCreateStaff(@RequestBody Map<String, String> requestData) {
        Map<String, Object> response = new HashMap<>();

        try {
            String step = requestData.get("step");

            if ("validate".equals(step)) {
                Map<String, String> errors = adminService.validateStaff(null,
                        requestData.get("username"), requestData.get("password"),
                        requestData.get("confirmPassword"), requestData.get("name"),
                        requestData.get("email"), requestData.get("phone"),
                        requestData.get("address"));

                if (!errors.isEmpty()) {
                    response.put("success", false);
                    response.put("message", errors.values().iterator().next());
                    return response;
                }

                adminService.generateAndSendOTP(requestData.get("email"));
                response.put("success", true);
                response.put("message", "OTP sent to email successfully");

            } else if ("verify".equals(step)) {
                String email = requestData.get("email");
                String otp = requestData.get("otp");

                if (!adminService.verifyOTP(email, otp)) {
                    response.put("success", false);
                    response.put("message", "Invalid or expired OTP");
                    return response;
                }

                adminService.createNewStaff(
                        requestData.get("username"),
                        requestData.get("password"),
                        requestData.get("name"),
                        email,
                        requestData.get("phone"),
                        requestData.get("address")
                );

                response.put("success", true);
                response.put("message", "Staff created successfully");
            }

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "An error occurred: " + e.getMessage());
        }

        return response;
    }

    @GetMapping("/admin/edit-staff")
    public String adminEditStaff(Model model, @RequestParam Integer id) {
        model.addAttribute("admin", adminService.findById(id));
        return "admin/staff_detail_ad";
    }

    @PostMapping("/admin/save-staff")
    public String adminSaveStaff(Model model, @ModelAttribute Admin admin,
                                 HttpServletRequest request, RedirectAttributes redirectAttributes) {
        try {
            Integer adminId = admin.getId();
            String name = request.getParameter("name");
            String phone = request.getParameter("phone");
            String address = request.getParameter("address");
            String email = request.getParameter("email");

            Map<String, String> errors = adminService.validateStaff(adminId, null, null, null, name, email, phone, address);
            if (!errors.isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", errors.values().iterator().next());
                return "redirect:/admin/edit-staff?id=" + adminId;
            }

            boolean emailChanged = !email.trim().equalsIgnoreCase(adminService.findById(adminId).getAccount().getEmail());
            adminService.updateStaff(adminId, name, phone, address, email);
            redirectAttributes.addFlashAttribute("successMessage",
                    emailChanged ? "Staff updated successfully. Email notification sent." : "Staff updated successfully");
            return "redirect:/admin/edit-staff?id=" + adminId;

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "An error occurred: " + e.getMessage());
            return "redirect:/admin/edit-staff?id=" + admin.getId();
        }
    }

    @GetMapping("/admin/delete-staff")
    public String adminDeleteStaff(@RequestParam(required = true) Integer id, RedirectAttributes redirectAttributes) {
        Admin admin = adminService.findById(id);
        if (admin == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Staff not found");
            return "redirect:/admin/manage-staff";
        }
        int activeOrders = adminService.countActiveOrders(admin);
        if (activeOrders > 0) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Cannot delete staff with " + activeOrders + " active order(s)");
            return "redirect:/admin/manage-staff";
        }
        adminService.delete(admin);
        redirectAttributes.addFlashAttribute("successMessage", "Staff deleted successfully");
        return "redirect:/admin/manage-staff";
    }



}
