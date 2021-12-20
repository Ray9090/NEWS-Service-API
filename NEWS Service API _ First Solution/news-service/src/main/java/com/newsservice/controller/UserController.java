package com.newsservice.controller;

import com.newsservice.repository.RoleRepository;
import com.newsservice.service.UserDetailsServiceImpl;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.newsservice.model.Role;
import com.newsservice.model.User;
import com.newsservice.repository.UserRepository;
import com.newsservice.service.SecurityService;
import com.newsservice.service.UserService;
import com.newsservice.web.UserValidator;

@Controller
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/registration")
    public String registration(Model model) {
        if (securityService.isAuthenticated()) {
            return "redirect:/";
        }

        model.addAttribute("roles", roleRepository.findAll());
        model.addAttribute("userForm", new User());

        return "registration";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute("userForm") User userForm, Model model, BindingResult bindingResult) {
        userValidator.validate(userForm, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "user already existing");
            model.addAttribute("roles", roleRepository.findAll());
            return "registration";
        }

        userService.save(userForm);

        //securityService.autoLogin(userForm.getUsername(), userForm.getPasswordConfirm());

        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        if (securityService.isAuthenticated()) {
            return "redirect:/api-news/news";
        }
        return "login";
    }

    @PostMapping("/authentication")
    public String login(Model model, String error, String logout, @ModelAttribute("user") User user) {
        try {
            UserDetails us = userDetailsService.loadUserByUsername(user.getUsername());
            if (us != null) {
                securityService.autoLogin(user.getUsername(), user.getPassword());
                if (us.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                    return "redirect:/admin";
                } else if (us.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_PUBLISHER"))) {
                    return "redirect:/api-news/news";
                } else if (us.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER"))) {
                    return "redirect:/api-news/news";
                }
            }
        } catch (UsernameNotFoundException e) {
            if (error != null)
                model.addAttribute("error", "Your username and password is invalid.");

            if (logout != null)
                model.addAttribute("message", "You have been logged out successfully.");
        }
        return "login";
    }

    @GetMapping({"/", "/welcome"})
    public String welcome(Model model) {
    	if (securityService.isAuthenticated()) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            UserDetails us = userDetailsService.loadUserByUsername(auth.getName());
            if (us != null) {
            	securityService.autoLogin(us.getUsername(), auth.getCredentials().toString());
                if (us.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                    return "redirect:/admin";
                } else if (us.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_PUBLISHER"))) {
                    return "redirect:/api-news/news";
                } else if (us.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER"))) {
                    return "redirect:/api-news/news";
                }
            }
    		return "redirect:/api-news/news";
    	}
        return "login";
    }

    @GetMapping("/admin")
    public String getAdmin(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "user/users";
    }
    
    @GetMapping("/admin/create")
    public String getCreateUser(Model model) {
    	List<Role> roles = roleRepository.findAll();
        model.addAttribute("allRoles", roles);
        return "user/createuser";
    }
    
    @PostMapping("/admin/create")
    public String createUser(Model model, User user, Integer roleId) {
    	Role roles = roleRepository.getById(roleId);
    	Set<Role> roleSet = new HashSet<Role>();
    	roleSet.add(roles);
    	user.setRoles(roleSet);
    	userService.save(user);
    	//User userSaved = userRepository.save(new User(null, user.getUsername(), user.getPassword(), user.getPasswordConfirm(), roleSet));
    	model.addAttribute("users", userRepository.findAll());
        return "user/users";
    }

    @GetMapping("/admin/edit/{id}")
    public String getUpdateUser(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("user", userRepository.findById(id).get());
        List<Role> roles = roleRepository.findAll();
        model.addAttribute("allRoles", roles);
        return "user/edit";
    }
    
    @PostMapping("/admin/edit/{id}")
    public String updateUser(@PathVariable("id") Integer id, Model model,User user, Integer roleId) {
    	User oldUser = userRepository.findById(id).get();
    	Optional<Role> roles =roleRepository.findById(roleId);
        //Role roles = roleRepository.getById(roleId);
    	if ( roles.isPresent()) {
    		Set<Role> roleSet = new HashSet<Role>();
        	roleSet.add(roles.get());
        	oldUser.setUsername(user.getUsername() == null ? oldUser.getUsername() : user.getUsername());
        	oldUser.setRoles(roleSet.isEmpty() ? oldUser.getRoles() : roleSet);
        	userService.update(oldUser);
            model.addAttribute("allRoles", roles);
    	}
    	 model.addAttribute("users", userRepository.findAll());
        return "user/users";
    }

    @GetMapping("/admin/delete/{id}")
    public String removeUser(@PathVariable("id") Integer id, Model model) {
        userRepository.deleteById(id);
        model.addAttribute("users", userRepository.findAll());
        return "user/users";
    }

    @RequestMapping("/403")
    public String accessDenied() {
        return "errors/403";
    }
}
