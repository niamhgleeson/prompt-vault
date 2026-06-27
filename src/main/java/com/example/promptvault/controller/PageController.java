package com.example.promptvault.controller;

import com.example.promptvault.model.*;
import com.example.promptvault.service.PolicyKeywordService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.promptvault.service.UserService;
import com.example.promptvault.service.PromptService;
import com.example.promptvault.service.SubmissionHistoryService;
import org.springframework.ui.Model;
import com.example.promptvault.service.PromptCategoryService;
import com.example.promptvault.model.Prompt;
import com.example.promptvault.model.PromptCategory;
import com.example.promptvault.model.User;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;

@Controller
public class PageController {

    private UserService userService;
    private PromptService promptService;
    private PromptCategoryService promptCategoryService;
    private PolicyKeywordService policyKeywordService;
    private SubmissionHistoryService submissionHistoryService;


    public PageController(
            UserService userService,
            PromptService promptService,
            PromptCategoryService promptCategoryService,
            PolicyKeywordService policyKeywordService,
            SubmissionHistoryService submissionHistoryService
    ) {

        this.userService= userService;
        this.promptService = promptService;
        this.promptCategoryService = promptCategoryService;
        this.policyKeywordService = policyKeywordService;
        this.submissionHistoryService = submissionHistoryService;

    }

    @GetMapping("/")
    public String home() {
        return "login";
    }

    @GetMapping("/login-page")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register-page")
    public String registerPage() {
        return "register";
    }

    @GetMapping("/user-dashboard")
    public String userDashboard() {
        return "user-dashboard";
    }

    @GetMapping("/admin-dashboard")
    public String adminDashboard() {
        return "admin-dashboard";
    }

    @GetMapping("/create-prompt-page")
    public String createPromptPage(
            HttpSession session,
            Model model
    ) {
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login-page";
        }

        model.addAttribute(
                "categories",
                promptCategoryService.getAll()
        );

        return "create-prompt";
    }
    @GetMapping("/user-prompts-page")
    public String userPromptsPage(
            Model model,
            HttpSession session
    ) {
        Long userId =
                (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login-page";
        }

        model.addAttribute(
                "prompts",
                promptService.getUserPrompts(userId)
        );

        return "user-prompts";
    }

    @GetMapping("/shared-prompts-page")
    public String sharedPromptsPage(Model model) {

        model.addAttribute(
                "prompts",
                promptService.getSharedPrompts()
        );

        return "shared-prompts";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam String username,
            @RequestParam String password,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {

        try {

            User user = userService.login(username, password);

            session.setAttribute("userId", user.getId());
            session.setAttribute("role", user.getRole());
            session.setAttribute("username", user.getUsername());

            if ("ADMIN".equals(user.getRole())) {
                return "redirect:/admin-dashboard";
            }

            return "redirect:/user-dashboard";

        }
        catch (RuntimeException e) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    e.getMessage()
            );

            return "redirect:/login-page";
        }
    }

    @PostMapping("/register")
    public String register(
            @RequestParam String name,
            @RequestParam String surname,
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String password
    ) {
        User user = new User();

        user.setName(name);
        user.setSurname(surname);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);

        userService.register(user);

        return "redirect:/login-page";
    }

    @PostMapping("/web/prompts")
    public String createPromptFromForm(
            @RequestParam String title,
            @RequestParam String promptText,
            @RequestParam String visibility,
            @RequestParam Long categoryId,
            HttpSession session
    ) {
        Prompt prompt = new Prompt();

        prompt.setTitle(title);
        prompt.setPromptText(promptText);
        prompt.setVisibility(visibility);

        Long ownerId =
                (Long) session.getAttribute("userId");

        if (ownerId == null) {
            return "redirect:/login-page";
        }

        User owner =
                userService.findById(ownerId);
        prompt.setOwner(owner);

        PromptCategory category = new PromptCategory();
        category.setId(categoryId);
        prompt.setCategory(category);

        promptService.createPrompt(prompt);

        return "redirect:/user-dashboard";
    }

    @PostMapping("/web/prompts/submit/{id}")
    public String submitPromptFromPage(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes
    ) {
        SubmissionHistory history =
                promptService.submitPrompt(id);

        redirectAttributes.addFlashAttribute(
                "simulatedResponse",
                history.getSimulatedResponse()
        );

        if (history.isFlagged()) {
            redirectAttributes.addFlashAttribute(
                    "warning",
                    "Warning: this prompt may contain sensitive information. Matched keyword: "
                            + history.getFlaggedKeyword()
            );
        } else {
            redirectAttributes.addFlashAttribute(
                    "message",
                    "Prompt submitted successfully."
            );
        }

        return "redirect:/user-prompts-page";
    }

    @PostMapping("/web/prompts/delete/{id}")
    public String deletePrompt(
            @PathVariable Long id,
            HttpSession session
    ) {

        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login-page";
        }

        promptService.deletePrompt(
                id,
                userId
        );

        return "redirect:/user-prompts-page";
    }

    @GetMapping("/edit-prompt-page/{id}")
    public String editPromptPage(
            @PathVariable Long id,
            HttpSession session,
            Model model
    ) {
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login-page";
        }

        Prompt prompt =
                promptService.findById(id);

        model.addAttribute("prompt", prompt);
        model.addAttribute("categories", promptCategoryService.getAll());

        return "edit-prompt";
    }

    @PostMapping("/web/prompts/edit/{id}")
    public String editPromptFromPage(
            @PathVariable Long id,
            @RequestParam String title,
            @RequestParam String promptText,
            @RequestParam String visibility,
            @RequestParam Long categoryId,
            HttpSession session
    ) {
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login-page";
        }

        Prompt updated = new Prompt();

        updated.setTitle(title);
        updated.setPromptText(promptText);
        updated.setVisibility(visibility);

        PromptCategory category = new PromptCategory();
        category.setId(categoryId);

        updated.setCategory(category);

        promptService.updatePrompt(
                id,
                userId,
                updated
        );

        return "redirect:/user-prompts-page";
    }

    @GetMapping("/admin-users-page")
    public String adminUsersPage(Model model) {

        model.addAttribute(
                "users",
                userService.getAll()
        );

        return "admin-users";
    }

    @PostMapping("/web/users/{id}/disable")
    public String disableUserFromPage(
            @PathVariable Long id
    ) {
        Long adminId = 8L;

        userService.setEnabled(
                adminId,
                id,
                false
        );

        return "redirect:/admin-users-page";
    }

    @PostMapping("/web/users/{id}/enable")
    public String enableUserFromPage(
            @PathVariable Long id
    ) {
        Long adminId = 8L;

        userService.setEnabled(
                adminId,
                id,
                true
        );
        return "redirect:/admin-users-page";
    }

    @GetMapping("/admin-categories-page")
    public String adminCategoriesPage(Model model) {

        model.addAttribute(
                "categories",
                promptCategoryService.getAll()
        );

        return "admin-categories";
    }

    @PostMapping("/web/categories")
    public String createCategoryFromPage(
            @RequestParam String name,
            @RequestParam String description
    ) {
        Long adminId = 8L;

        User admin = userService.findById(adminId);

        PromptCategory category = new PromptCategory();
        category.setName(name);
        category.setDescription(description);

        promptCategoryService.create(
                category,
                admin
        );

        return "redirect:/admin-categories-page";
    }

    @GetMapping("/admin-keywords-page")
    public String adminPolicyKeywordsPage(Model model) {

        model.addAttribute(
                "keywords",
                policyKeywordService.getAll()
        );

        return "admin-keywords";
    }

    @PostMapping("/web/keywords")
    public String createKeyword(

            @RequestParam
            String keyword

    ) {

        Long adminId = 8L;

        User admin =
                userService.findById(adminId);

        PolicyKeyword policyKeyword =
                new PolicyKeyword();

        policyKeyword.setKeyword(
                keyword
        );

        policyKeywordService.create(

                policyKeyword,
                admin

        );

        return "redirect:/admin-keywords-page";

    }

    @PostMapping("/web/keywords/{id}/delete")
    public String deleteKeyword(

            @PathVariable
            Long id

    ) {

        policyKeywordService.delete(id);

        return "redirect:/admin-keywords-page";

    }

    @GetMapping("/user-history-page")
    public String userHistoryPage(
            Model model,
            HttpSession session
    ) {
        Long userId =
                (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login-page";
        }

        model.addAttribute(
                "history",
                submissionHistoryService.getUserHistory(userId)
        );

        return "user-history";
    }

    @GetMapping("/admin-flagged-prompts-page")
    public String adminFlaggedPromptsPage(
            Model model
    ) {
        Long adminId = 8L;

        model.addAttribute(
                "flaggedPrompts",
                submissionHistoryService.getFlaggedResponses(adminId)
        );

        return "admin-flagged-prompts";
    }

    @PostMapping("/logout")
    public String logout(
            HttpSession session
    ) {
        session.invalidate();

        return "redirect:/login-page";
    }

    @GetMapping("/admin-keywords-edit-page/{id}")
    public String editKeywordPage(
            @PathVariable Long id,
            Model model
    ) {
        PolicyKeyword keyword =
                policyKeywordService.findById(id);

        model.addAttribute(
                "keyword",
                keyword
        );

        return "admin-keyword-edit";
    }

    @PostMapping("/web/keywords/{id}/edit")
    public String editKeywordFromPage(
            @PathVariable Long id,
            @RequestParam String keyword
    ) {
        Long adminId = 8L;

        User admin =
                userService.findById(adminId);

        policyKeywordService.update(
                id,
                keyword,
                admin
        );

        return "redirect:/admin-keywords-page";
    }

}