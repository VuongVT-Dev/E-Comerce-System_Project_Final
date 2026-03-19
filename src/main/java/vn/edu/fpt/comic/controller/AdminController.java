package vn.edu.fpt.comic.controller;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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


}
