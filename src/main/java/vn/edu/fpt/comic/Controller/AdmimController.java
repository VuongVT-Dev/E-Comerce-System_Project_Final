package vn.edu.fpt.comic.Controller;

import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
}
