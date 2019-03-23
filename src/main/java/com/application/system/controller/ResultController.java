package com.application.system.controller;

import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.application.system.model.Academic;
import com.application.system.model.FinalTerm;
import com.application.system.model.FirstTerm;
import com.application.system.model.Student;
import com.application.system.model.User;
import com.application.system.service.InfoService;
import com.application.system.service.RoleService;
import com.application.system.service.StudentService;
import com.application.system.service.UserService;

@Controller
@RequestMapping("/student")
public class ResultController {

	@Autowired
	private UserService service;
	
	@Autowired
	private StudentService stuService;
	
	@Autowired
	private InfoService infoService;
	
	@Autowired
	private RoleService roleService;
	
	
	@GetMapping("/studentpage")
	public String studentPage(Principal p, Model model, HttpSession session) {
		
		String sid= p.getName();
		int id = Integer.parseInt(sid);
		 Student student= stuService.findStudent(id);
		 User user= student.getUser();
		 String school = user.getSchool();
		 model.addAttribute("student", student);
		 model.addAttribute("school", school);
		 session.setAttribute("user", user);
		
		return "studentpage";
	}
	
	
// this has to be removed
	@GetMapping("/firsttermresult/{id}")
	public String firstTermResult(@PathVariable("id") int id, Model model, HttpSession session) {
		Student student= stuService.findStudent(id);
		model.addAttribute("student", student);
		int useid=(Integer) session.getAttribute("userid");
		User user= service.findById(useid);
		model.addAttribute("user", user);
		FirstTerm term= student.getFirstterm();
		model.addAttribute("term", term);
		String classCode= student.getAcademic();
		Academic academic = new Academic();
		int acad= academic.getClassCode(classCode);
		if(acad==1 || acad==2|| acad==3) 
		return "firsttermresult";
		else if(acad==4)
			return "forthgraderesult";
		else if(acad==5 || acad==6 || acad==7 || acad==8 || acad==9)
			return "fifthgraderesult";
		
		return "studentpage";
	}
	//till here
	
	@GetMapping("/firstterstudentmrecord/{id}")
	public String studentsFirstTerm(@PathVariable("id") int id, Model model, HttpSession session) {
		
		
		
		Student student = stuService.findStudent(id);
		FirstTerm term = student.getFirstterm();
		Academic academic = new Academic();
		String ac= student.getAcademic();
		int year= academic.getClassCode(ac);
		User user= (User) session.getAttribute("user");
		
		String school= user.getSchool();
		model.addAttribute("student", student);
		model.addAttribute("school", school);
		if(term==null) {
			
			model.addAttribute("message", "لا توجد نتيجة  لهذا الطالب");     
			return "studentpage";
		}else {
		
			model.addAttribute("term", term);
			model.addAttribute("year", ac);
			if(year==1 || year==2 || year==3) {
				
				return "firsttermresult";
			}else if(year==5 || year==6 || year==7 || year==8 || year==9 || year==10) {
				return "fifthgraderesult";
			
		}else if(year==4)
			return "forthgraderesult";
		}
		
		
		return"studentpage";
	}
	
	@GetMapping("/finaltermstudentrecord/{id}")
	public String studentFianlTerm(@PathVariable("id") int id, Model model, HttpSession session) {
		
		Student student = stuService.findStudent(id);
		FinalTerm term = student.getFinalterm();
		String ac= student.getAcademic();
		User user = (User) session.getAttribute("user");
		String school= user.getSchool();
		
		model.addAttribute("school", school);
		
		if(term==null) {
			model.addAttribute("student", student);
			model.addAttribute("message", "لا توجد نتيجة  لهذا الطالب");     
			return "studentpage";
		} else {
		
		String grade = term.getGrade();
		Academic academic = new Academic();
		
		
		int year= academic.getClassCode(ac);
		
		model.addAttribute("student", student);
			model.addAttribute("term", term);
			
			if(year==1 || year==2 || year==3 || year==4) {
				
				if(grade.equals("راسب")) {
					String curYear= academic.getYear(year); 
					model.addAttribute("year", curYear);
				}else {
				
				year -=1;
				String curYear= academic.getYear(year);  
				model.addAttribute("year", curYear);
				}
				
				return "firsttermresult";
				
			}else if(year==6 || year==7 || year==8 || year==9 || year==10) {
				
				if(grade.equals("راسب")) {
					String curYear= academic.getYear(year); 
					model.addAttribute("year", curYear);
				}else {
				
				year -=1;
				String curYear= academic.getYear(year);  
				model.addAttribute("year", curYear);
				}
				return "fifthgraderesult";
			
		}else if(year==5) {
			
			if(grade.equals("راسب")) {
				String curYear= academic.getYear(year); 
				model.addAttribute("year", curYear);
			}else {
			
			year -=1;
			String curYear= academic.getYear(year);  
			model.addAttribute("year", curYear);
			
			}
			return "forthgraderesult";
		} 
		}
		return"studentpage";
	}
}
