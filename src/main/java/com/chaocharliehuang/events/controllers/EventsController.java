package com.chaocharliehuang.events.controllers;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.chaocharliehuang.events.models.*;
import com.chaocharliehuang.events.services.UserService;
import com.chaocharliehuang.events.validator.UserValidator;

@Controller
public class EventsController {
	
	private UserService userService;
	private UserValidator userValidator;
	
	public EventsController(UserService userService, UserValidator userValidator) {
		this.userService = userService;
		this.userValidator = userValidator;
	}
	
	@GetMapping("/")
	public String loginReg(
			@Valid @ModelAttribute("user") User user, Model model,
			@RequestParam(value="error", required=false) String error,
			@RequestParam(value="logout", required=false) String logout) {
		if (error != null) {
			model.addAttribute("errorMessage", "Invalid credentials; please try again.");
		}
		if (logout != null) {
			model.addAttribute("logoutMessage", "Logout successful!");
		}
		String[] states = {"CA", "IL", "NY", "WA"};
		model.addAttribute("states", states);
		return "loginReg.jsp";
	}
	
	@PostMapping("/registration")
	public String registration(
			@Valid @ModelAttribute("user") User user, BindingResult result, Model model) {
		String[] states = {"CA", "IL", "NY", "WA"};
		model.addAttribute("states", states);
		userValidator.validate(user, result);
		if (result.hasErrors()) {
			return "loginReg.jsp";
		} else {
			userService.createUser(user);
			return "redirect:/";
		}
	}
	
	@GetMapping("/events")
	public String allEvents(Principal principal, Model model) {
		String username = principal.getName();
		model.addAttribute("currentUser", userService.findByUsername(username));
		return "events.jsp";
	}

}
