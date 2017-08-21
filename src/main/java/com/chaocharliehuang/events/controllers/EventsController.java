package com.chaocharliehuang.events.controllers;

import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.chaocharliehuang.events.models.*;
import com.chaocharliehuang.events.services.*;
import com.chaocharliehuang.events.validator.*;

@Controller
public class EventsController {
	
	private UserService userService;
	private EventService eventService;
	private UserValidator userValidator;
	private EventValidator eventValidator;
	
	public EventsController(
			UserService userService, EventService eventService, 
			UserValidator userValidator, EventValidator eventValidator) {
		this.userService = userService;
		this.eventService = eventService;
		this.userValidator = userValidator;
		this.eventValidator = eventValidator;
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
	public String registration(@Valid @ModelAttribute("user") User user, BindingResult result, Model model) {
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
	public String allEvents(
			Principal principal, Model model,
			@Valid @ModelAttribute("event") Event event) {
		User currentUser = userService.findByUsername(principal.getName());
		model.addAttribute("currentUser", currentUser);
		
		model.addAttribute("eventsInState", eventService.findEventsByState(currentUser.getState()));
		model.addAttribute("eventsOutOfState", eventService.findEventsNotInState(currentUser.getState()));
		
		String[] states = {"CA", "IL", "NY", "WA"};
		model.addAttribute("states", states);
		return "events.jsp";
	}
	
	@PostMapping("/events")
	public String createEvent(
			@Valid @ModelAttribute("event") Event event, BindingResult result,
			Principal principal, Model model) {
		User currentUser = userService.findByUsername(principal.getName());
		
		model.addAttribute("eventsInState", eventService.findEventsByState(currentUser.getState()));
		model.addAttribute("eventsOutOfState", eventService.findEventsNotInState(currentUser.getState()));
		
		String[] states = {"CA", "IL", "NY", "WA"};
		model.addAttribute("states", states);
		
		eventValidator.validate(event, result);
		if (result.hasErrors()) {
			return "events.jsp";
		} else {
			event.setHost(currentUser);
			eventService.createEvent(event);
			return "redirect:/events";
		}
	}
	
	@GetMapping("/events/{id}")
	public String eventPage(@PathVariable("id") Long id, Model model) {
		model.addAttribute("event", eventService.findEventById(id));
		return "eventPage.jsp";
	}
	
	@GetMapping("/events/{id}/join")
	public String joinEvent(@PathVariable("id") Long id, Principal principal) {
		User currentUser = userService.findByUsername(principal.getName());
		Event event = eventService.findEventById(id);
		if (event.getHost() != currentUser) {
			List<User> attendees = event.getAttendees();
			attendees.add(currentUser);
			event.setAttendees(attendees);
			eventService.createEvent(event);
		}
		return "redirect:/events";
	}
	
	@GetMapping("/events/{id}/cancel")
	public String unjoinEvent(@PathVariable("id") Long id, Principal principal) {
		User currentUser = userService.findByUsername(principal.getName());
		Event event = eventService.findEventById(id);
		if (event.getHost() != currentUser) {
			List<User> attendees = event.getAttendees();
			attendees.remove(currentUser);
			event.setAttendees(attendees);
			eventService.createEvent(event);
		}
		return "redirect:/events";
	}
	
	@GetMapping("/events/{id}/edit")
	public String editEvent(
			@PathVariable("id") Long id, Principal principal, Model model) {
		User currentUser = userService.findByUsername(principal.getName());
		Event event = eventService.findEventById(id);
		if (event.getHost() != currentUser || event == null) {
			return "redirect:/events";
		} else {
			String[] states = {"CA", "IL", "NY", "WA"};
			model.addAttribute("states", states);
			model.addAttribute("event", event);
			return "editEvent.jsp";
		}
	}
	
	@PostMapping("/events/{id}/edit")
	public String updateEvent(
			@PathVariable("id") Long id, Model model,
			@Valid @ModelAttribute("event") Event event, BindingResult result) {
		String[] states = {"CA", "IL", "NY", "WA"};
		model.addAttribute("states", states);
		eventValidator.validate(event, result);
		if (result.hasErrors()) {
			return "editEvent.jsp";
		} else {
			Event currentEvent = eventService.findEventById(id);
			currentEvent.setName(event.getName());
			currentEvent.setDate(event.getDate());
			currentEvent.setCity(event.getCity());
			currentEvent.setState(event.getState());
			eventService.createEvent(currentEvent);
			return "redirect:/events/" + id;
		}
	}
	
	@GetMapping("/events/{id}/delete")
	public String deleteEvent(@PathVariable("id") Long id, Principal principal, Model model) {
		User currentUser = userService.findByUsername(principal.getName());
		Event event = eventService.findEventById(id);
		if (event.getHost() == currentUser && event != null) {
			eventService.deleteEvent(id);
		}
		return "redirect:/events";
	}
}
