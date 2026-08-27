package com.example.promptvault.controller;

import com.example.promptvault.dto.RegistrationRequest;
import com.example.promptvault.exception.RateLimitExceededException;

import com.example.promptvault.model.PolicyKeyword;
import com.example.promptvault.model.Prompt;
import com.example.promptvault.model.PromptCategory;
import com.example.promptvault.model.SubmissionHistory;
import com.example.promptvault.model.User;

import com.example.promptvault.service.PolicyKeywordService;
import com.example.promptvault.service.PromptCategoryService;
import com.example.promptvault.service.PromptService;
import com.example.promptvault.service.SecurityAuditService;
import com.example.promptvault.service.SubmissionHistoryService;
import com.example.promptvault.service.UserService;

import jakarta.validation.Valid;

import org.springframework.security.core.Authentication;

import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;

import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
    private final SecurityAuditService securityAuditService;

    public PageController(
            UserService userService,
            PromptService promptService,
            PromptCategoryService promptCategoryService,
            PolicyKeywordService policyKeywordService,
            SubmissionHistoryService submissionHistoryService,
            SecurityAuditService securityAuditService
    ) {

        this.userService = userService;
        this.promptService = promptService;
        this.promptCategoryService = promptCategoryService;
        this.policyKeywordService = policyKeywordService;
        this.submissionHistoryService = submissionHistoryService;
        this.securityAuditService = securityAuditService;
    }


    // --------------------------------------------------
    // HOME / LOGIN / REGISTER
    // --------------------------------------------------

    @GetMapping("/")
    public String home() {

        return "login";
    }


    @GetMapping("/login-page")
    public String loginPage() {

        return "login";
    }


    @GetMapping("/register-page")
    public String registerPage(
            Model model
    ) {

        model.addAttribute(
                "user",
                new RegistrationRequest()
        );

        return "register";
    }


    @PostMapping("/register")
    public String register(
            @Valid
            @ModelAttribute("user")
            RegistrationRequest request,

            BindingResult errors,

            RedirectAttributes redirectAttributes
    ) {

        if (errors.hasErrors()) {

            return "register";
        }

        try {

            User user =
                    new User();

            user.setName(
                    request.getName()
            );

            user.setSurname(
                    request.getSurname()
            );

            user.setUsername(
                    request.getUsername()
            );

            user.setEmail(
                    request.getEmail()
            );

            user.setPassword(
                    request.getPassword()
            );

            userService.register(
                    user
            );

            redirectAttributes
                    .addFlashAttribute(
                            "message",
                            "Registration successful. "
                                    + "You can now log in."
                    );

            return "redirect:/login-page";

        } catch (RuntimeException e) {

            /*
             * Do not expose database or framework
             * exception details to the user.
             */
            redirectAttributes
                    .addFlashAttribute(
                            "error",
                            "Registration failed. "
                                    + "Please check your details."
                    );

            return "redirect:/register-page";
        }
    }


    @GetMapping("/access-denied")
    public String accessDenied() {

        return "access-denied";
    }


    // --------------------------------------------------
    // DASHBOARDS
    // --------------------------------------------------

    @GetMapping("/user-dashboard")
    public String userDashboard() {

        return "user-dashboard";
    }


    @GetMapping("/admin-dashboard")
    public String adminDashboard() {

        return "admin-dashboard";
    }


    // --------------------------------------------------
    // USER PROMPTS
    // --------------------------------------------------

    @GetMapping("/create-prompt-page")
    public String createPromptPage(
            Model model
    ) {

        model.addAttribute(
                "categories",
                promptCategoryService
                        .getAll()
        );

        return "create-prompt";
    }


    @GetMapping("/user-prompts-page")
    public String userPromptsPage(
            Model model,
            Authentication authentication
    ) {

        User user =
                userService
                        .findByUsername(
                                authentication
                                        .getName()
                        );

        model.addAttribute(
                "prompts",
                promptService
                        .getUserPrompts(
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
                promptService
                        .getSharedPrompts()
        );

        return "shared-prompts";
    }


    @PostMapping("/web/prompts")
    public String createPromptFromForm(
            @RequestParam String title,
            @RequestParam String promptText,
            @RequestParam String visibility,
            @RequestParam Long categoryId,
            Authentication authentication,
            RedirectAttributes redirectAttributes
    ) {

        try {

            User owner =
                    userService
                            .findByUsername(
                                    authentication
                                            .getName()
                            );

            PromptCategory category =
                    promptCategoryService
                            .findById(
                                    categoryId
                            );

            Prompt prompt =
                    new Prompt();

            prompt.setTitle(
                    title
            );

            prompt.setPromptText(
                    promptText
            );

            prompt.setVisibility(
                    visibility
            );

            prompt.setOwner(
                    owner
            );

            prompt.setCategory(
                    category
            );

            promptService.createPrompt(
                    prompt
            );

            redirectAttributes
                    .addFlashAttribute(
                            "message",
                            "Prompt created successfully."
                    );

        } catch (RuntimeException e) {

            redirectAttributes
                    .addFlashAttribute(
                            "error",
                            "Unable to create the prompt."
                    );
        }

        return "redirect:/user-prompts-page";
    }


    @PostMapping(
            "/web/prompts/submit/{id}"
    )
    public String submitPromptFromPage(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes,
            Authentication authentication
    ) {

        try {

            User user =
                    userService
                            .findByUsername(
                                    authentication
                                            .getName()
                            );

            SubmissionHistory history =
                    promptService
                            .submitPrompt(
                                    id,
                                    user.getId()
                            );

            redirectAttributes
                    .addFlashAttribute(
                            "simulatedResponse",
                            history
                                    .getSimulatedResponse()
                    );

            if (history.isFlagged()) {

                redirectAttributes
                        .addFlashAttribute(
                                "warning",
                                "Warning: this prompt "
                                        + "may contain sensitive "
                                        + "information. "
                                        + "Matched keyword: "
                                        + history
                                        .getFlaggedKeyword()
                        );

            } else {

                redirectAttributes
                        .addFlashAttribute(
                                "message",
                                "Prompt submitted successfully."
                        );
            }

        } catch (
                RateLimitExceededException e
        ) {

            /*
             * This message is safe because it is
             * created by our own application.
             */
            redirectAttributes
                    .addFlashAttribute(
                            "error",
                            e.getMessage()
                    );

        } catch (
                RuntimeException e
        ) {

            /*
             * Never send raw exception details
             * to the browser.
             */
            redirectAttributes
                    .addFlashAttribute(
                            "error",
                            "Unable to submit the prompt."
                    );
        }

        return "redirect:/user-prompts-page";
    }


    @PostMapping(
            "/web/prompts/delete/{id}"
    )
    public String deletePrompt(
            @PathVariable Long id,
            Authentication authentication,
            RedirectAttributes redirectAttributes
    ) {

        try {

            User user =
                    userService
                            .findByUsername(
                                    authentication
                                            .getName()
                            );

            promptService
                    .deletePrompt(
                            id,
                            user.getId()
                    );

            redirectAttributes
                    .addFlashAttribute(
                            "message",
                            "Prompt deleted successfully."
                    );

        } catch (
                RuntimeException e
        ) {

            redirectAttributes
                    .addFlashAttribute(
                            "error",
                            "Unable to delete the prompt."
                    );
        }

        return "redirect:/user-prompts-page";
    }


    @GetMapping(
            "/edit-prompt-page/{id}"
    )
    public String editPromptPage(
            @PathVariable Long id,
            Model model,
            Authentication authentication,
            RedirectAttributes redirectAttributes
    ) {

        try {

            User user =
                    userService
                            .findByUsername(
                                    authentication
                                            .getName()
                            );

            Prompt prompt =
                    promptService
                            .getUserPromptById(
                                    id,
                                    user.getId()
                            );

            model.addAttribute(
                    "prompt",
                    prompt
            );

            model.addAttribute(
                    "categories",
                    promptCategoryService
                            .getAll()
            );

            return "edit-prompt";

        } catch (
                RuntimeException e
        ) {

            /*
             * Safe generic response also covers
             * ownership violations without leaking
             * whether a particular prompt exists.
             */
            redirectAttributes
                    .addFlashAttribute(
                            "error",
                            "Unable to access that prompt."
                    );

            return "redirect:/user-prompts-page";
        }
    }


    @PostMapping(
            "/web/prompts/edit/{id}"
    )
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
                    userService
                            .findByUsername(
                                    authentication
                                            .getName()
                            );

            PromptCategory category =
                    promptCategoryService
                            .findById(
                                    categoryId
                            );

            Prompt updated =
                    new Prompt();

            updated.setTitle(
                    title
            );

            updated.setPromptText(
                    promptText
            );

            updated.setVisibility(
                    visibility
            );

            updated.setCategory(
                    category
            );

            promptService
                    .updatePrompt(
                            id,
                            user.getId(),
                            updated
                    );

            redirectAttributes
                    .addFlashAttribute(
                            "message",
                            "Prompt updated successfully."
                    );

        } catch (
                RuntimeException e
        ) {

            redirectAttributes
                    .addFlashAttribute(
                            "error",
                            "Unable to update the prompt."
                    );
        }

        return "redirect:/user-prompts-page";
    }


    // --------------------------------------------------
    // ADMIN - USERS
    // --------------------------------------------------

    @GetMapping("/admin-users-page")
    public String adminUsersPage(
            Model model
    ) {

        model.addAttribute(
                "users",
                userService
                        .getAll()
        );

        return "admin-users";
    }


    @PostMapping(
            "/web/users/{id}/disable"
    )
    public String disableUserFromPage(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes,
            Authentication authentication
    ) {

        try {

            userService.setEnabled(
                    id,
                    false
            );

            securityAuditService
                    .userStatusChanged(
                            authentication
                                    .getName(),
                            id,
                            false
                    );

            redirectAttributes
                    .addFlashAttribute(
                            "message",
                            "User disabled successfully."
                    );

        } catch (
                RuntimeException e
        ) {

            redirectAttributes
                    .addFlashAttribute(
                            "error",
                            "Unable to update the user account."
                    );
        }

        return "redirect:/admin-users-page";
    }


    @PostMapping(
            "/web/users/{id}/enable"
    )
    public String enableUserFromPage(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes,
            Authentication authentication
    ) {

        try {

            userService.setEnabled(
                    id,
                    true
            );

            securityAuditService
                    .userStatusChanged(
                            authentication
                                    .getName(),
                            id,
                            true
                    );

            redirectAttributes
                    .addFlashAttribute(
                            "message",
                            "User enabled successfully."
                    );

        } catch (
                RuntimeException e
        ) {

            redirectAttributes
                    .addFlashAttribute(
                            "error",
                            "Unable to update the user account."
                    );
        }

        return "redirect:/admin-users-page";
    }


    // --------------------------------------------------
    // ADMIN - CATEGORIES
    // --------------------------------------------------

    @GetMapping(
            "/admin-categories-page"
    )
    public String adminCategoriesPage(
            Model model
    ) {

        model.addAttribute(
                "categories",
                promptCategoryService
                        .getAll()
        );

        return "admin-categories";
    }


    @PostMapping("/web/categories")
    public String createCategoryFromPage(
            @RequestParam String name,
            @RequestParam String description,
            Authentication authentication,
            RedirectAttributes redirectAttributes
    ) {

        try {

            User admin =
                    userService
                            .findByUsername(
                                    authentication
                                            .getName()
                            );

            PromptCategory category =
                    new PromptCategory();

            category.setName(
                    name
            );

            category.setDescription(
                    description
            );

            promptCategoryService
                    .create(
                            category,
                            admin
                    );

            securityAuditService
                    .categoryChanged(
                            authentication
                                    .getName(),
                            category.getId(),
                            "CREATE"
                    );

            redirectAttributes
                    .addFlashAttribute(
                            "message",
                            "Category created successfully."
                    );

        } catch (
                RuntimeException e
        ) {

            redirectAttributes
                    .addFlashAttribute(
                            "error",
                            "Unable to create the category."
                    );
        }

        return "redirect:/admin-categories-page";
    }


    @PostMapping(
            "/web/categories/{id}/delete"
    )
    public String deleteCategory(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes,
            Authentication authentication
    ) {

        try {

            promptCategoryService
                    .delete(
                            id
                    );

            securityAuditService
                    .categoryChanged(
                            authentication
                                    .getName(),
                            id,
                            "DELETE"
                    );

            redirectAttributes
                    .addFlashAttribute(
                            "message",
                            "Category deleted successfully."
                    );

        } catch (
                RuntimeException e
        ) {

            /*
             * Do not reveal foreign-key names,
             * SQL or database table names.
             */
            redirectAttributes
                    .addFlashAttribute(
                            "error",
                            "Unable to delete the category. "
                                    + "It may still be in use."
                    );
        }

        return "redirect:/admin-categories-page";
    }


    @GetMapping(
            "/edit-category-page/{id}"
    )
    public String editCategoryPage(
            @PathVariable Long id,
            Model model,
            RedirectAttributes redirectAttributes
    ) {

        try {

            PromptCategory category =
                    promptCategoryService
                            .findById(
                                    id
                            );

            model.addAttribute(
                    "category",
                    category
            );

            return "edit-category";

        } catch (
                RuntimeException e
        ) {

            redirectAttributes
                    .addFlashAttribute(
                            "error",
                            "Unable to access that category."
                    );

            return "redirect:/admin-categories-page";
        }
    }


    @PostMapping(
            "/web/categories/edit/{id}"
    )
    public String updateCategory(
            @PathVariable Long id,
            @RequestParam String name,
            @RequestParam String description,
            Authentication authentication,
            RedirectAttributes redirectAttributes
    ) {

        try {

            PromptCategory category =
                    new PromptCategory();

            category.setName(
                    name
            );

            category.setDescription(
                    description
            );

            promptCategoryService
                    .update(
                            id,
                            category
                    );

            securityAuditService
                    .categoryChanged(
                            authentication
                                    .getName(),
                            id,
                            "UPDATE"
                    );

            redirectAttributes
                    .addFlashAttribute(
                            "message",
                            "Category updated successfully."
                    );

        } catch (
                RuntimeException e
        ) {

            redirectAttributes
                    .addFlashAttribute(
                            "error",
                            "Unable to update the category."
                    );
        }

        return "redirect:/admin-categories-page";
    }


    // --------------------------------------------------
    // ADMIN - POLICY KEYWORDS
    // --------------------------------------------------

    @GetMapping(
            "/admin-keywords-page"
    )
    public String adminPolicyKeywordsPage(
            Model model
    ) {

        model.addAttribute(
                "keywords",
                policyKeywordService
                        .getAll()
        );

        return "admin-keywords";
    }


    @PostMapping("/web/keywords")
    public String createKeyword(
            @RequestParam String keyword,
            Authentication authentication,
            RedirectAttributes redirectAttributes
    ) {

        try {

            User admin =
                    userService
                            .findByUsername(
                                    authentication
                                            .getName()
                            );

            PolicyKeyword policyKeyword =
                    new PolicyKeyword();

            policyKeyword.setKeyword(
                    keyword
            );

            policyKeywordService
                    .create(
                            policyKeyword,
                            admin
                    );

            securityAuditService
                    .keywordChanged(
                            authentication
                                    .getName(),
                            policyKeyword.getId(),
                            "CREATE"
                    );

            redirectAttributes
                    .addFlashAttribute(
                            "message",
                            "Keyword created successfully."
                    );

        } catch (
                RuntimeException e
        ) {

            redirectAttributes
                    .addFlashAttribute(
                            "error",
                            "Unable to create the keyword."
                    );
        }

        return "redirect:/admin-keywords-page";
    }


    @PostMapping(
            "/web/keywords/{id}/delete"
    )
    public String deleteKeyword(
            @PathVariable Long id,
            Authentication authentication,
            RedirectAttributes redirectAttributes
    ) {

        try {

            policyKeywordService
                    .delete(
                            id
                    );

            securityAuditService
                    .keywordChanged(
                            authentication
                                    .getName(),
                            id,
                            "DELETE"
                    );

            redirectAttributes
                    .addFlashAttribute(
                            "message",
                            "Keyword deleted successfully."
                    );

        } catch (
                RuntimeException e
        ) {

            redirectAttributes
                    .addFlashAttribute(
                            "error",
                            "Unable to delete the keyword."
                    );
        }

        return "redirect:/admin-keywords-page";
    }


    @GetMapping(
            "/admin-keywords-edit-page/{id}"
    )
    public String editKeywordPage(
            @PathVariable Long id,
            Model model,
            RedirectAttributes redirectAttributes
    ) {

        try {

            PolicyKeyword keyword =
                    policyKeywordService
                            .findById(
                                    id
                            );

            model.addAttribute(
                    "keyword",
                    keyword
            );

            return "admin-keyword-edit";

        } catch (
                RuntimeException e
        ) {

            redirectAttributes
                    .addFlashAttribute(
                            "error",
                            "Unable to access that keyword."
                    );

            return "redirect:/admin-keywords-page";
        }
    }


    @PostMapping(
            "/web/keywords/{id}/edit"
    )
    public String editKeywordFromPage(
            @PathVariable Long id,
            @RequestParam String keyword,
            Authentication authentication,
            RedirectAttributes redirectAttributes
    ) {

        try {

            User admin =
                    userService
                            .findByUsername(
                                    authentication
                                            .getName()
                            );

            policyKeywordService
                    .update(
                            id,
                            keyword,
                            admin
                    );

            securityAuditService
                    .keywordChanged(
                            authentication
                                    .getName(),
                            id,
                            "UPDATE"
                    );

            redirectAttributes
                    .addFlashAttribute(
                            "message",
                            "Keyword updated successfully."
                    );

        } catch (
                RuntimeException e
        ) {

            redirectAttributes
                    .addFlashAttribute(
                            "error",
                            "Unable to update the keyword."
                    );
        }

        return "redirect:/admin-keywords-page";
    }


    // --------------------------------------------------
    // HISTORY
    // --------------------------------------------------

    @GetMapping("/user-history-page")
    public String userHistoryPage(
            Model model,
            Authentication authentication
    ) {

        User user =
                userService
                        .findByUsername(
                                authentication
                                        .getName()
                        );

        model.addAttribute(
                "history",
                submissionHistoryService
                        .getUserHistory(
                                user.getId()
                        )
        );

        return "user-history";
    }


    @GetMapping(
            "/admin-flagged-prompts-page"
    )
    public String adminFlaggedPromptsPage(
            Model model
    ) {

        model.addAttribute(
                "flaggedPrompts",
                submissionHistoryService
                        .getFlaggedResponses()
        );

        return "admin-flagged-prompts";
    }
}