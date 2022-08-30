package ca.sheridancollege.ozcelikh.controllers;



import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ca.sheridancollege.ozcelikh.beans.Event;
import ca.sheridancollege.ozcelikh.beans.User;
import ca.sheridancollege.ozcelikh.database.DatabaseAccess;


/**
 * 
 * @author Hizir Ozcelik, November 2021
 */

@Controller
public class HomeController {

	@Autowired
	@Lazy
	private DatabaseAccess da;


	@GetMapping("/")
	public String index() {
		return "index";
	}

	@GetMapping("/secure")
	public String secureIndex(Model model, Authentication authentication) {
		String email = authentication.getName();
		String name = da.findUserAccount(email).getName();
		String lastName = da.findUserAccount(email).getLastName();
		model.addAttribute("name", name);
		model.addAttribute("lastName", lastName);

		return "/secure/index";
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@GetMapping("/secure/addEvent")
	public String event(Model model, Authentication authentication) {

		String email = authentication.getName();
		System.out.println("Active user " + email);
		User currentUser = da.findUserAccount(email);
		System.out.println("Current User " + currentUser.getEmail());
		
		model.addAttribute("event", new Event());
		model.addAttribute("eventList", da.getEventList(currentUser.getUserId()));

		return "/secure/addEvent";
	}

	@PostMapping("/secure/addEvent")
	public String addEvent(Model model, Authentication authentication, Event event) {
		
		String email = authentication.getName();
		System.out.println("Active user " + email);
		User currentUser = da.findUserAccount(email);
		System.out.println("Current User " + currentUser.getEmail());
		
		da.addEvent(currentUser.getUserId(), event);
		
		model.addAttribute("eventList", da.getEventList(currentUser.getUserId()));
		model.addAttribute("event", new Event());
		
		return "/secure/addEvent";
	}
	
	@GetMapping("/permission-denied")
	public String permissionDenied() {
		return "/error/permission-denied";
	}

	@GetMapping("/register")
	public String getRegister() {
		return "register";
	}

	@GetMapping("/about")
	public String getAbout() {
		return "about";
	}

	@PostMapping("/register")
	public String postRegister(@RequestParam String name, @RequestParam String lastName, @RequestParam String username,
			@RequestParam String password) {

// Checking if user already has an account before new account creation.
		if (da.findUserAccount(username) == null) {
			da.addUser(name, lastName, username, password);

			Long userId = da.findUserAccount(username).getUserId();

			da.addRole(userId, Long.valueOf(1));

			return "accountCreated";
		} else
			return "userFound";
	}

}
