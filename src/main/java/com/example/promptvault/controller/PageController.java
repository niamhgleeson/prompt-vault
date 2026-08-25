package com.example.promptvault.controller;

import com.example.promptvault.model.PolicyKeyword;
import com.example.promptvault.model.Prompt;
import com.example.promptvault.model.PromptCategory;
import com.example.promptvault.model.SubmissionHistory;
import com.example.promptvault.model.User;

import com.example.promptvault.service.PolicyKeywordService;
import com.example.promptvault.service.PromptCategoryService;
import com.example.promptvault.service.PromptService;
import com.example.promptvault.service.SubmissionHistoryService;
import com.example.promptvault.service.UserService;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PageController {

    private final UserService userService;
    private final PromptService promptService;
    private final PromptCategoryService promptCategoryService;
    private final PolicyKeywordService policyKeywordService;
    private final SubmissionHistoryService submissionHistoryService;

    public PageController(
            UserService userService,
            PromptService promptService,
            PromptCategoryService promptCategoryService,
            PolicyKeywordService policyKeywordService,
            SubmissionHistoryService submissionHistoryService
    ) {

        this.userService = userService;
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

    @GetMapping("/access-denied")
    public String accessDenied() {

        return "access-denied";
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
            Model model
    ) {

        model.addAttribute(
                "categories",
                promptCategoryService.getAll()
        );

        return "create-prompt";
    }

    @GetMapping("/user-prompts-page")
    public String userPromptsPage(
            Model model,
            Authentication authentication
    ) {

        User user =
                userService.findByUsername(
                        authentication.getName()
                );

        model.addAttribute(
                "prompts",
                promptService.getUserPrompts(
                        user.getId()
                )
        );

        return "user-prompts";
    }

    @GetMapping("/shared-prompts-page")
    public String sharedPromptsPage(
            Model model
    ) {

        model.addAttribute(
                "prompts",
                promptService.getSharedPrompts()
        );

        return "shared-prompts";
    }

    @PostMapping("/register")
    public String register(
            @RequestParam String name,
            @RequestParam String surname,
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String password,
            RedirectAttributes redirectAttributes
    ) {

        try {

            User user =
                    new User();

            user.setName(name);
            user.setSurname(surname);
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(password);

            userService.register(user);

            redirectAttributes.addFlashAttribute(
                    "message",
                    "Registration successful. You can now log in."
            );

            return "redirect:/login-page";

        } catch (RuntimeException e) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    "Registration failed. Please check your details."
            );

            return "redirect:/register-page";
        }
    }

    @PostMapping("/web/prompts")
    public String createPromptFromForm(
            @RequestParam String title,
            @RequestParam String promptText,
            @RequestParam String visibility,
            @RequestParam Long categoryId,
            Authentication authentication
    ) {

        User owner =
                userService.findByUsername(
                        authentication.getName()
                );

        Prompt prompt =
                new Prompt();

        prompt.setTitle(title);
        prompt.setPromptText(promptText);
        prompt.setVisibility(visibility);
        prompt.setOwner(owner);

        PromptCategory category =
                promptCategoryService.findById(
                        categoryId
                );

        prompt.setCategory(category);

        promptService.createPrompt(prompt);

        return "redirect:/user-prompts-page";
    }

    @PostMapping("/web/prompts/submit/{id}")
    public String submitPromptFromPage(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes,
            Authentication authentication
    ) {

        try {

            User user =
                    userService.findByUsername(
                            authentication.getName()
                    );

            SubmissionHistory history =
                    promptService.submitPrompt(
                            id,
                            user.getId()
                    );

            redirectAttributes.addFlashAttribute(
                    "simulatedResponse",
                    history.getSimulatedResponse()
            );

            if (history.isFlagged()) {

                redirectAttributes.addFlashAttribute(
                        "warning",
                        "Warning: this prompt may contain sensitive information. "
                                + "Matched keyword: "
                                + history.getFlaggedKeyword()
                );

            } else {

                redirectAttributes.addFlashAttribute(
                        "message",
                        "Prompt submitted successfully."
                );
            }

        } catch (RuntimeException e) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    "You can only submit your own prompts."
            );
        }

        return "redirect:/user-prompts-page";
    }

    @PostMapping("/web/prompts/delete/{id}")
    public String deletePrompt(
            @PathVariable Long id,
            Authentication authentication,
            RedirectAttributes redirectAttributes
    ) {

        try {

            User user =
                    userService.findByUsername(
                            authentication.getName()
                    );

            promptService.deletePrompt(
                    id,
                    user.getId()
            );

            redirectAttributes.addFlashAttribute(
                    "message",
                    "Prompt deleted successfully."
            );

        } catch (RuntimeException e) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    "You can only delete your own prompts."
            );
        }

        return "redirect:/user-prompts-page";
    }

    @GetMapping("/edit-prompt-page/{id}")
    public String editPromptPage(
            @PathVariable Long id,
            Model model,
            Authentication authentication,
            RedirectAttributes redirectAttributes
    ) {

        try {

            User user =
                    userService.findByUsername(
                            authentication.getName()
                    );

            Prompt prompt =
                    promptService.getUserPromptById(
                            id,
                            user.getId()
                    );

            model.addAttribute(
                    "prompt",
                    prompt
            );

            model.addAttribute(
                    "categories",
                    promptCategoryService.getAll()
            );

            return "edit-prompt";

        } catch (RuntimeException e) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    "You can only edit your own prompts."
            );

            return "redirect:/user-prompts-page";
        }
    }

    @PostMapping("/web/prompts/edit/{id}")
    public String editPromptFromPage(
            @PathVariable Long id,
            @RequestParam String title,
            @RequestParam String promptText,
            @RequestParam String visibility,
            @RequestParam Long categoryId,
            Authentication authentication,
            RedirectAttributes redirectAttributes
    ) {

        try {

            User user =
                    userService.findByUsername(
                            authentication.getName()
                    );

            Prompt updated =
                    new Prompt();

            updated.setTitle(title);
            updated.setPromptText(promptText);
            updated.setVisibility(visibility);

            PromptCategory category =
                    promptCategoryService.findById(
                            categoryId
                    );

            updated.setCategory(category);

            promptService.updatePrompt(
                    id,
                    user.getId(),
                    updated
            );

            redirectAttributes.addFlashAttribute(
                    "message",
                    "Prompt updated successfully."
            );

        } catch (RuntimeException e) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    "You can only edit your own prompts."
            );
        }

        return "redirect:/user-prompts-page";
    }

    @GetMapping("/admin-users-page")
    public String adminUsersPage(
            Model model
    ) {

        model.addAttribute(
                "users",
                userService.getAll()
        );

        return "admin-users";
    }

    @PostMapping("/web/users/{id}/disable")
    public String disableUserFromPage(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes
    ) {

        try {

            userService.setEnabled(
                    id,
                    false
            );

        } catch (RuntimeException e) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    e.getMessage()
            );
        }

        return "redirect:/admin-users-page";
    }

    @PostMapping("/web/users/{id}/enable")
    public String enableUserFromPage(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes
    ) {

        try {

            userService.setEnabled(
                    id,
                    true
            );

        } catch (RuntimeException e) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    e.getMessage()
            );
        }

        return "redirect:/admin-users-page";
    }

    @GetMapping("/admin-categories-page")
    public String adminCategoriesPage(
            Model model
    ) {

        model.addAttribute(
                "categories",
                promptCategoryService.getAll()
        );

        return "admin-categories";
    }

    @PostMapping("/web/categories")
    public String createCategoryFromPage(
            @RequestParam String name,
            @RequestParam String description,
            Authentication authentication
    ) {

        User admin =
                userService.findByUsername(
                        authentication.getName()
                );

        PromptCategory category =
                new PromptCategory();

        category.setName(name);
        category.setDescription(description);

        promptCategoryService.create(
                category,
                admin
        );

        return "redirect:/admin-categories-page";
    }

    @PostMapping("/web/categories/{id}/delete")
    public String deleteCategory(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes
    ) {

        try {

            promptCategoryService.delete(id);

            redirectAttributes.addFlashAttribute(
                    "message",
                    "Category deleted successfully."
            );

        } catch (RuntimeException e) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    e.getMessage()
            );
        }

        return "redirect:/admin-categories-page";
    }

    @GetMapping("/edit-category-page/{id}")
    public String editCategoryPage(
            @PathVariable Long id,
            Model model
    ) {

        PromptCategory category =
                promptCategoryService.findById(id);

        model.addAttribute(
                "category",
                category
        );

        return "edit-category";
    }

    @PostMapping("/web/categories/edit/{id}")
    public String updateCategory(
            @PathVariable Long id,
            @RequestParam String name,
            @RequestParam String description
    ) {

        PromptCategory category =
                new PromptCategory();

        category.setName(name);
        category.setDescription(description);

        promptCategoryService.update(
                id,
                category
        );

        return "redirect:/admin-categories-page";
    }

    @GetMapping("/admin-keywords-page")
    public String adminPolicyKeywordsPage(
            Model model
    ) {

        model.addAttribute(
                "keywords",
                policyKeywordService.getAll()
        );

        return "admin-keywords";
    }

    @PostMapping("/web/keywords")
    public String createKeyword(
            @RequestParam String keyword,
            Authentication authentication
    ) {

        User admin =
                userService.findByUsername(
                        authentication.getName()
                );

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
            @PathVariable Long id
    ) {

        policyKeywordService.delete(id);

        return "redirect:/admin-keywords-page";
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
            @RequestParam String keyword,
            Authentication authentication
    ) {

        User admin =
                userService.findByUsername(
                        authentication.getName()
                );

        policyKeywordService.update(
                id,
                keyword,
                admin
        );

        return "redirect:/admin-keywords-page";
    }

    @GetMapping("/user-history-page")
    public String userHistoryPage(
            Model model,
            Authentication authentication
    ) {

        User user =
                userService.findByUsername(
                        authentication.getName()
                );

        model.addAttribute(
                "history",
                submissionHistoryService.getUserHistory(
                        user.getId()
                )
        );

        return "user-history";
    }

    @GetMapping("/admin-flagged-prompts-page")
    public String adminFlaggedPromptsPage(
            Model model
    ) {

        model.addAttribute(
                "flaggedPrompts",
                submissionHistoryService.getFlaggedResponses()
        );

        return "admin-flagged-prompts";
    }
}