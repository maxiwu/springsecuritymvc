package umedia.test.springboot.controller;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.Operation;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	JdbcUserDetailsManager jdbcUserDetailsManager;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {

		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG,
				DateFormat.LONG, locale);

		String formattedDate = dateFormat.format(date);

		model.addAttribute("serverTime", formattedDate);

		return "login";
	}

	// redirect to login page
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login() {
		return "login";
	}

	// redirect to login page
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String loginok() {
		// is authenticated?
		if (SecurityContextHolder.getContext().getAuthentication() != null) {
			Boolean val2 = SecurityContextHolder.getContext()
					.getAuthentication().isAuthenticated();
			String name = SecurityContextHolder.getContext()
					.getAuthentication().getName();
		}
		return "home";
	}

	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String home() {
		// is authenticated?
		if (SecurityContextHolder.getContext().getAuthentication() != null) {
			Boolean val2 = SecurityContextHolder.getContext()
					.getAuthentication().isAuthenticated();
			String name = SecurityContextHolder.getContext()
					.getAuthentication().getName();
		}
		return "home";
	}

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String register() {
		return "register";
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String createUser(
			@RequestParam(value = "username", required = false) String username,
			@RequestParam(value = "password", required = false) String password,
			HttpServletRequest request) {
		// JdbcUserDetailsManager um = new JdbcUserDetailsManager();
		SaltSource saltSource = null;
		Object salt = null;

		jdbcUserDetailsManager.deleteUser("name");

		// empty grant
		Collection<GrantedAuthority> allowedOperations = new ArrayList<GrantedAuthority>();
		allowedOperations.add(new SimpleGrantedAuthority("ROLE_USER"));

		// User 'name' has no authorities and will be treated as 'not found'

		// enabled, account not expired, credential non expired, account non
		// locked, collection of authorities
		UserDetails user = new User(username, password, true, true, true, true,
				allowedOperations);
		// calculate what hashedPassword would be in this configuration
		/*
		 * String hashedPassword = passwordEncoder.encodePassword(
		 * user.getPassword(), salt);
		 */

		String hashedpassword = passwordEncoder.encode(user.getPassword());
		UserDetails puser = new User(username, hashedpassword, true, true,
				true, true, allowedOperations);

		jdbcUserDetailsManager.createUser(puser);

		return "login";
	}
}
