package com.application.system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.application.system.model.Academic;
import com.application.system.model.FirstTerm;
import com.application.system.model.Info;
import com.application.system.model.Role;
import com.application.system.model.Student;
import com.application.system.model.User;
import com.application.system.repository.UserRepository;
import com.application.system.service.InfoService;
import com.application.system.service.RoleService;
import com.application.system.service.StudentService;
import com.application.system.service.UserService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Controller
public class UserController {

	
	@Autowired
	private UserService service;
	
	@Autowired
	private StudentService stuService;
	
	@Autowired
	private InfoService infoService;
	
	@Autowired
	private RoleService roleService;

	
	
	
	// login page which is main page
			@GetMapping("/")
			public String login() {
				
				return "home";
			}
			
			//userlogin page for the systemSecurity
			@GetMapping("/userlogin")
			public String userLogin() {
				
				return "userloginform";
			}
			
			
			@GetMapping("/logout")
			public String logout() {
				
				return "home";
			}
		
	
	// adding new user
	@PostMapping("/add")
	public String add(@RequestParam("name") String name, @RequestParam("school") String school, Model model) {
	

		
		User user = new User(name,school);
		service.saveUser(user);
		model.addAttribute("user", user);
		
		return "adduserconfirm";
	}
	
	// submit adding a user
	@PostMapping("/sumbitinfo")
	public String submitUserInfo(@RequestParam("id") int id, @RequestParam("password") String password) {
		
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String hashed  = passwordEncoder.encode(password);
		Info info = new Info(id, hashed, 1);
		Role role = new Role("USER");
		Set<Role> roles = new HashSet<Role>();
		roles.add(role);
		info.setRole(roles); 
		roleService.saveRole(role);
		infoService.saveInfo(info);
	
		return "add";
		
		
	}
	
	@GetMapping("/newuser")
	public String newUser() {
		return "add";
	}
	
	//launching the view of user login
	
	@GetMapping("/user/find")
	public String find() {
		return "find";
	}
	
	@GetMapping("/accessdenied")
	public String accessDenied() {
		return "accessdenied";
	}
	
	//submitting the name and password of the user for authentication 
/*	@PostMapping("/finduser")
	public String findUser(@RequestParam("name") String name, @RequestParam("password") String userPassword, HttpSession session) {
		
		
		String password = null;
		int id=0;
			
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		User user= service.findByName(name);
		if(user==null) { 
			System.out.println("not found");
		}else
		password = user.getPassword();
		if(passwordEncoder.matches(userPassword, password)) {								
			id = user.getId();
			session.setAttribute("userid", id);
			
			return "welcome";
		}else {
			System.out.println("password not matching");
			
		}
		
		return "find";
	}
	
	*/
	
	
	/*
	@GetMapping("/result")
	public String studentResultRequest() {
		
		return "studentlogin";
	}
*/
	/*
	@PostMapping("/studentresult")
	public String studentResult(@RequestParam("username") int stu_id, @RequestParam("password") int user_id, Model model, HttpSession session) {
		
		User user= service.findById(user_id);
		if(user !=null) {
			Student student =stuService.findStudent(stu_id);
				
			if(student !=null && student.getUser().equals(user)) {
				model.addAttribute("student", student);
				session.setAttribute("userid", user_id);
				return "studentpage";
			
			}else System.out.println("wrong information");
		}else System.out.println("wrong information");
		return "studentlogin";
	}
	*/
	
	
	
	@GetMapping("/finaltermresutl/{id}")
	public String finalTermResult(@PathVariable("id") int id) {
		System.out.println(id);
		return "studentpage";
	}
	
	
}
