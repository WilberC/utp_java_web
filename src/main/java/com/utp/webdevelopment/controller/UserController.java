package com.utp.webdevelopment.controller;

import com.utp.webdevelopment.model.User;
import com.utp.webdevelopment.model.enums.UserRole;
import com.utp.webdevelopment.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping
    public String listUsers(Model model, 
                          @RequestParam(defaultValue = "0") int page,
                          @RequestParam(defaultValue = "5") int size,
                          @RequestParam(defaultValue = "id") String sortBy,
                          @RequestParam(defaultValue = "asc") String sortDir,
                          @RequestParam(required = false) String search) {
        
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<User> usersPage;
        if (search != null && !search.trim().isEmpty()) {
            // For now, we'll show all users. In a real app, you'd implement search
            usersPage = userService.findAllUsersPaginated(pageable);
        } else {
            usersPage = userService.findAllUsersPaginated(pageable);
        }
        
        model.addAttribute("users", usersPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", usersPage.getTotalPages());
        model.addAttribute("totalItems", usersPage.getTotalElements());
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("search", search);
        model.addAttribute("userRoles", UserRole.values());
        
        return "admin/users/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("userRoles", UserRole.values());
        return "admin/users/create";
    }

    @PostMapping("/create")
    public String createUser(@Valid @ModelAttribute User user, 
                           BindingResult result, 
                           Model model,
                           RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            model.addAttribute("userRoles", UserRole.values());
            return "admin/users/create";
        }
        
        try {
            if (userService.existsByEmail(user.getEmail())) {
                result.rejectValue("email", "error.user", "El email ya está registrado");
                model.addAttribute("userRoles", UserRole.values());
                return "admin/users/create";
            }
            
            userService.createUser(user);
            redirectAttributes.addFlashAttribute("success", "Usuario creado exitosamente");
            return "redirect:/admin/users";
            
        } catch (Exception e) {
            log.error("Error creating user", e);
            model.addAttribute("error", "Error al crear el usuario: " + e.getMessage());
            model.addAttribute("userRoles", UserRole.values());
            return "admin/users/create";
        }
    }

    @GetMapping("/{id}")
    public String showUser(@PathVariable Long id, Model model) {
        return userService.findUserById(id)
                .map(user -> {
                    model.addAttribute("user", user);
                    return "admin/users/show";
                })
                .orElse("redirect:/admin/users");
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        return userService.findUserById(id)
                .map(user -> {
                    model.addAttribute("user", user);
                    model.addAttribute("userRoles", UserRole.values());
                    return "admin/users/edit";
                })
                .orElse("redirect:/admin/users");
    }

    @PostMapping("/{id}/edit")
    public String updateUser(@PathVariable Long id,
                           @Valid @ModelAttribute User user,
                           BindingResult result,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            model.addAttribute("userRoles", UserRole.values());
            return "admin/users/edit";
        }
        
        try {
            // Check if email is already taken by another user
            userService.findUserByEmail(user.getEmail())
                    .ifPresent(existingUser -> {
                        if (!existingUser.getId().equals(id)) {
                            result.rejectValue("email", "error.user", "El email ya está registrado");
                        }
                    });
            
            if (result.hasErrors()) {
                model.addAttribute("userRoles", UserRole.values());
                return "admin/users/edit";
            }
            
            userService.updateUser(id, user);
            redirectAttributes.addFlashAttribute("success", "Usuario actualizado exitosamente");
            return "redirect:/admin/users";
            
        } catch (Exception e) {
            log.error("Error updating user", e);
            model.addAttribute("error", "Error al actualizar el usuario: " + e.getMessage());
            model.addAttribute("userRoles", UserRole.values());
            return "admin/users/edit";
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("success", "Usuario eliminado exitosamente");
        } catch (Exception e) {
            log.error("Error deleting user", e);
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el usuario: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }

    @GetMapping("/stats")
    public String showUserStats(Model model) {
        long totalUsers = userService.countUsers();
        long adminUsers = userService.countUsersByRole(UserRole.ADMIN);
        long customerUsers = userService.countUsersByRole(UserRole.CUSTOMER);
        long activeUsers = userService.countUsersByStatus("ACTIVE");
        long suspendedUsers = userService.countUsersByStatus("SUSPENDED");
        
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("adminUsers", adminUsers);
        model.addAttribute("customerUsers", customerUsers);
        model.addAttribute("activeUsers", activeUsers);
        model.addAttribute("suspendedUsers", suspendedUsers);
        model.addAttribute("title", "Estadísticas de Usuarios");
        
        return "admin/users/stats";
    }
} 