package vn.edu.fpt.comic.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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


}
