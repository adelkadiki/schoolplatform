package com.application.system.controller;


import java.security.Principal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.ui.Model;


import com.application.system.model.Academic;
import com.application.system.model.EighthClass;
import com.application.system.model.FifthClass;
import com.application.system.model.FinalTerm;
import com.application.system.model.FinalTermResult;
import com.application.system.model.FirstClass;
import com.application.system.model.FirstTerm;
import com.application.system.model.FirstTermResult;
import com.application.system.model.FourthClass;
import com.application.system.model.Graduate;
import com.application.system.model.Info;
import com.application.system.model.NinthClass;
import com.application.system.model.Role;
import com.application.system.model.SecondClass;
import com.application.system.model.SeventhClass;
import com.application.system.model.SixthClass;
import com.application.system.model.Student;
import com.application.system.model.ThirdClass;
import com.application.system.model.User;
import com.application.system.service.EighthClassService;
import com.application.system.service.FifthClassService;
import com.application.system.service.FinalTermResultService;
import com.application.system.service.FinalTermService;
import com.application.system.service.FirstClassService;
import com.application.system.service.FirstTermResultService;
import com.application.system.service.FirstTermService;
import com.application.system.service.FourthClassService;
import com.application.system.service.GraduateService;
import com.application.system.service.InfoService;
import com.application.system.service.NinthClassService;
import com.application.system.service.RoleService;
import com.application.system.service.SecondClassService;
import com.application.system.service.SeventhClassService;
import com.application.system.service.SixthClassService;
import com.application.system.service.StudentService;
import com.application.system.service.ThirdClassService;
import com.application.system.service.UserService;

@Controller
@RequestMapping("/user")
public class StudentController {

	@Autowired
	private StudentService service;
	
	@Autowired
	private UserService userService;
		
	@Autowired
	private InfoService infoService;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private GraduateService graduateService;  
	
	@Autowired
	private FirstTermService firstTermService;
	
	@Autowired
	private FinalTermService finalTermService;
	
	@Autowired
	private FirstClassService firstClassService;
	
	@Autowired
	private SecondClassService secondClassService;
	
	@Autowired
	private ThirdClassService thirdClassService;
	
	@Autowired
	private FourthClassService fourthClassService;
	
	@Autowired
	private FifthClassService fifthClassService;
	
	@Autowired
	private SixthClassService sixthClassService;
	
	@Autowired
	private SeventhClassService seventhClassService;
	
	@Autowired
	private EighthClassService eighthClassService;
	
	@Autowired
	private NinthClassService ninthClassService;
	
	@Autowired
	private FirstTermResultService firstTermResultService;
	
	@Autowired
	private FinalTermResultService finalTermResultService;
	
	@GetMapping("/home")
	public String home(Principal p, HttpSession session) {
		String sid= p.getName();
		int id = Integer.parseInt(sid);
		session.setAttribute("userid", id);
		return "welcome";
	}
	
	// launching the view of adding student form 
				@GetMapping("/addstudent")
				public String addstudent(HttpSession session) {
					int id= (Integer) session.getAttribute("userid");
					session.setAttribute("userid", id);	
					return "addstudent";
				}
		
				
				// return the password setting template for new student
				@PostMapping("/submitstudentpassword")
				public String passwordSetting(@RequestParam("id") int id, HttpSession session) {
					
					int userid= (Integer) session.getAttribute("userid");
				    String password=  Integer.toString(userid);
					BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
					String hashed  = passwordEncoder.encode(password);
					Info info = new Info(id, hashed, 1);
					Role role = new Role("STUDENT");
					Set<Role> roles = new HashSet<Role>();
					roles.add(role);
					info.setRole(roles); 
					roleService.saveRole(role);
					infoService.saveInfo(info);
					
					return "addstudent";
				}
				
		// submitting the form of adding a new student associated to a user
		@GetMapping("/submitstudent")
		public String submitStudent(@RequestParam("name") String name, @RequestParam("year") int year, HttpSession session, Model model) {
			
			
			int id = (Integer) session.getAttribute("userid");
		
			
			User user = userService.findById(id);
			Academic academic = new Academic();
			String student_class = academic.getYear(year);
			
			Student student = new Student(name, student_class);
			
			user.getStudents().add(student);
		
			
			service.saveStudent(student);
			model.addAttribute("student", student);
			
			return "passwordset";
			
		}
		
		// cancel new added student
		@GetMapping("/deleteadding/{id}")
		public String deleteAdding(@PathVariable("id") int id, Model model, HttpSession session) {                   
			
			service.deleteStudent(id);
			return "addstudent";
		}
		
		@GetMapping("/studentsearchbyid")
		public String studentSearchById(@RequestParam("id") int sid, Model model, HttpSession session) {
			
			
			  
			// List<Student>	students= service.findStudentById(sid);
				
			int id= (Integer) session.getAttribute("userid");
			
		/*	User user = userService.findById(id);
			
			List<Student> result = new ArrayList<>();
			
			for(Student s : students) {
				if(s.getUser()==user)
					result.add(s);
			}
			
			if(result.isEmpty()) {
				model.addAttribute("message", "لا يوجد طالب بهذا الرقم");
			}
			
			model.addAttribute("students", result);
			
			*/
			Student student = service.findStudentByStudentIdAndUserId(sid, id);
			if(student!=null)
				model.addAttribute("students", student);
			
			else 
				model.addAttribute("message", "لا يوجد طالب يحمل هذا الرقم");
			
			return "search";
			
		}
		
		@GetMapping("/findstudentsearchbyid")
		public String findStudentSearchById(@RequestParam("id") int sid, Model model, HttpSession session) {
			
			
			  
			// List<Student>	students= service.findStudentById(sid);
				
			int id= (Integer) session.getAttribute("userid");
			
		
			Student student = service.findStudentByStudentIdAndUserId(sid, id);
			if(student!=null)
				model.addAttribute("students", student);
			
			else 
				model.addAttribute("message", "لا يوجد طالب يحمل هذا الرقم");
			
			return "listall";
			
		}
		
		//listing the students list
		@GetMapping("/studentslist")
		public String studentsList(HttpSession session, Model model) {
			
			int id = (Integer) session.getAttribute("userid");
			List<Student> students = userService.findStudents(id);
			List<Student> list = new ArrayList<Student>();
			for(Student s : students) {
				if(!s.getAcademic().equals("خريج"))
					list.add(s);
			}
			model.addAttribute("students", list);
			session.setAttribute("students", students);
			
			return "studentslist";
		}
		
		// list all students
		@GetMapping("/listall")
		public String listAll(HttpSession session, Model model) {
		//	int id = (Integer) session.getAttribute("userid");
			
		//	List<Student> students = userService.findStudents(id);
		//	model.addAttribute("students", students);
			return "listall";
		}
		
		
		// search by requested from listall page
		@GetMapping("/studentsearchbyname")
		public String seachByName(HttpSession session, @RequestParam("name") String name, Model model) {
			
			int id = (Integer) session.getAttribute("userid");
			User user = userService.findById(id);
			int userid = user.getId();
			
		//	List<Student> list= students.stream().filter(s -> service.findByNameLike(name).equals(s.getName())).collect(Collectors.toList());
			
			List<Student> list = service.findByStudentNameAndUserId(name, userid);
			
			if(list.isEmpty())
				model.addAttribute("message", "no student was found");
			else 
				
				model.addAttribute("students", list);
			
			return "listall";
			
		}
		
		
		
		
		// list all graduates
				@GetMapping("/graduate")
				public String graduate(HttpSession session) {
					int id = (Integer) session.getAttribute("userid");
					session.setAttribute("userid", id);
					
					return "graduate";
				}
				
				// graduate by name controller
				@GetMapping("/graduatesearchbyname")
				public String graduateSearchByName(HttpSession session, @RequestParam("name") String name, Model model) {
					
					int id = (Integer) session.getAttribute("userid");
					/* User user = userService.findById(id);
					
					List<Graduate> students = graduateService.findGraduates(name);
					List<Graduate> result = new ArrayList<>();
					
					for(Graduate s : students) {
						if(s.getUser()==user)
							result.add(s);
					}
					
					
					model.addAttribute("students", result);
					*/
					List<Graduate> students = graduateService.findGraduateByNameLikeAndUserId(name, id);
					if(students.isEmpty()) 
						model.addAttribute("message", "لا يوجد طالب بهذا الاسم");
					else 
						model.addAttribute("students", students);
					
					return "graduate";
					
				}
				
				// getting the student id to search in archive view
				@GetMapping("/graduatesearchbyid")
				public String graduateSearchById(HttpSession session, @RequestParam("id") int id, Model model) {
					
					int userid = (Integer) session.getAttribute("userid");
				/*
					User user = userService.findById(userid);
					Graduate student = graduateService.findById(id);
					if(student!=null) {
						
					if(student.getUser()==user)
						model.addAttribute("students", student);
					else 
						model.addAttribute("message", "لا يوجد طالب بهذا الرقم");
					}else
						model.addAttribute("message", "لا يوجد طالب بهذا الرقم");
					*/
					
					Graduate graduate = graduateService.findGraduateByIdAndUserId(id, userid);
					if(graduate!=null)
						model.addAttribute("students", graduate);
					else 
						model.addAttribute("message", "لا يوجد خريج يحمل هذا الرقم");
					return"graduate";
				}

		// launching the search page with the user's id 
		@GetMapping("/search")
		public String search(HttpSession session) {
			int id= (Integer) session.getAttribute("userid");
			session.setAttribute("userid", id);
			return "search";
		}
		
		@GetMapping("/searchbyid")
		public String searchById() {
			return "findstudentbyid";
		}
		
		
		//link to archive view
		@GetMapping("/archive")
		public String archive(HttpSession session) {
			int id= (Integer) session.getAttribute("userid");
			session.setAttribute("userid", id);
			return "archive";
		}
		
		
		// getting student's name for archiving search
		@GetMapping("/archivesearchbyname")
		public String archiveSearchByName(HttpSession session, @RequestParam("name") String name, Model model) {
			
			int id = (Integer) session.getAttribute("userid");
			User user = userService.findById(id);
			
			List<Student> students = service.findByNameLike(name);
			List<Student> result = new ArrayList<>();
			
			for(Student s : students) {
				if(s.getUser()==user)
					result.add(s);
			}
			
			
			model.addAttribute("students", result);
			
			if(students.isEmpty()) 
				model.addAttribute("message", "لا يوجد طالب بهذا الاسم");
			
			return "archive";
			
		}
		
		// applying back button to edit page 
		@GetMapping("/searchback/{id}")
		public String searchBack(@PathVariable("id") int id, Model model) {
			
			Student student = service.findStudent(id);
			model.addAttribute("students", student);
			
			return "search";
		}
		
		// getting the student id to search in archive view
		@GetMapping("/archivesearchbyid")
		public String archiveSearchById(HttpSession session, @RequestParam("id") int id, Model model) {
			
			int userid = (Integer) session.getAttribute("userid");
		
			User user = userService.findById(userid);
			Student student = service.findStudent(id);
			if(student!=null) {
				
			if(student.getUser()==user)
				model.addAttribute("students", student);
			else 
				model.addAttribute("message", "لا يوجد طالب بهذا الرقم");
			}else
				model.addAttribute("message", "لا يوجد طالب بهذا الرقم");
			
			return"archive";
		}
		
		
		// getting the student recored request
		@GetMapping("/studentrecored/{id}")
		public String studentRecored(@PathVariable("id") int id, Model model) {
			
			Student student  = service.findStudent(id);
			model.addAttribute("student", student);
			
			return "studentrecored";
		}
		
		
		// getting the graduate recored request
				@GetMapping("/graduaterecored/{id}")
				public String graduateRecored(@PathVariable("id") int id, Model model) {
					
					
					Graduate graduate  = graduateService.findById(id);
					model.addAttribute("student", graduate);
					
					return "graduaterecord";
				}
		
		// getting student's information from archive page
		@GetMapping("/firstclassfirstterm/{id}")
		public String studentRecord(HttpSession session, @PathVariable("id") int id, Model model) {
			
			Student student = service.findStudent(id);
			FirstClass firstclass= student.getFirstclass();
			
			if(firstclass==null) {
				model.addAttribute("message", "لا توجد بيانات الإمتحان الجزئي للصف الأول للطالب");
				model.addAttribute("student", student);
				return "studentrecored";
			}
			FirstTermResult firstterm= firstclass.getFirsterm();
			
			if(firstterm==null) {
				model.addAttribute("message", "لا توجد بيانات الإمتحان الجزئي للصف الأول للطالب");
				model.addAttribute("student", student);
				return "studentrecored";
			}
			
			String academic= firstclass.getAcademic();
			int userid= (Integer) session.getAttribute("userid");
			User user= userService.findById(userid);
			String school= user.getSchool();
			model.addAttribute("school", school);
			
				model.addAttribute("firstterm", firstterm);
				model.addAttribute("student", student);
				model.addAttribute("academic", academic);
				
				return "firstclassfisttermarchive";
			
			
		}

		// getting student's information from archive page
		@GetMapping("/firstclassfirsttermgraduate/{id}")
		public String graduateRecord(HttpSession session, @PathVariable("id") int id, Model model) {
			
			Graduate student = graduateService.findById(id);
			
			FirstClass firstclass= student.getFirstclass();
			
			if(firstclass==null) {
				model.addAttribute("message", "لا توجد بيانات الإمتحان الجزئي للصف الأول للطالب");
				model.addAttribute("student", student);
				return "graduaterecord";
			}
			FirstTermResult firstterm= firstclass.getFirsterm();
			
			if(firstterm==null) {
				model.addAttribute("message", "لا توجد بيانات الإمتحان الجزئي للصف الأول للطالب");
				model.addAttribute("student", student);
				return "graduaterecord";
			}
			
			String academic= firstclass.getAcademic();
			int userid= (Integer) session.getAttribute("userid");
			User user= userService.findById(userid);
			String school= user.getSchool();
			model.addAttribute("school", school);
			
				model.addAttribute("firstterm", firstterm);
				model.addAttribute("student", student);
				model.addAttribute("academic", academic);
				
				return "firstclassfirsttermgraduate";
			
			
		}

		
		// getting student's information from archive page to process final result
		
				@GetMapping("/firstclassfinal/{id}")
				public String studentRecordFinal(HttpSession session, @PathVariable("id") int id, Model model) {
					
					Student student = service.findStudent(id);
					FirstClass firstclass= student.getFirstclass();
					
					if(firstclass==null) {
						model.addAttribute("message", "لا توجد بيانات الإمتحان النهائي للصف الأول للطالب");
						model.addAttribute("student", student);
						return "studentrecored";
					}
					FinalTermResult finalterm= firstclass.getFinalterm();
					
					if(finalterm==null) {
						model.addAttribute("message", "لا توجد بيانات الإمتحان النهائي للصف الأول للطالب");
						model.addAttribute("student", student);
						return "studentrecored";
					}
					
					String academic= firstclass.getAcademic();
					int userid= (Integer) session.getAttribute("userid");
					User user= userService.findById(userid);
					String school= user.getSchool();
					model.addAttribute("school", school);
					
						model.addAttribute("firstterm", finalterm);
						model.addAttribute("student", student);
						model.addAttribute("academic", academic);
						
						return "firstclassfisttermarchive";
					
					
				}
				
				
				// getting student's information from graduate page to process final result
				@GetMapping("/firstclassfinalgraduate/{id}")
				public String graduateRecordFinal(HttpSession session, @PathVariable("id") int id, Model model) {
					
					Graduate student = graduateService.findById(id);
					FirstClass firstclass= student.getFirstclass();
					
					if(firstclass==null) {
						model.addAttribute("message", "لا توجد بيانات الإمتحان النهائي للصف الأول للطالب");
						model.addAttribute("student", student);
						return "graduaterecord";
					}
					FinalTermResult finalterm= firstclass.getFinalterm();
					
					if(finalterm==null) {
						model.addAttribute("message", "لا توجد بيانات الإمتحان النهائي للصف الأول للطالب");
						model.addAttribute("student", student);
						return "graduaterecord";
					}
					
					String academic= firstclass.getAcademic();
					int userid= (Integer) session.getAttribute("userid");
					User user= userService.findById(userid);
					String school= user.getSchool();
					model.addAttribute("school", school);
					
						model.addAttribute("firstterm", finalterm);
						model.addAttribute("student", student);
						model.addAttribute("academic", academic);
						
						return "firstclassfirsttermgraduate";
					
					
				}
				
				// getting student's id to issue second class first term result
				@GetMapping("/secondclassfirstterm/{id}")
				public String secondClassFirtTerm(HttpSession session, @PathVariable("id") int id, Model model) {
					Student student = service.findStudent(id);
					SecondClass secondclass= student.getSecondclass();
					
					if(secondclass==null) {
						model.addAttribute("message", "لا توجد بيانات الإمتحان الجزئي للصف الثانى للطالب");
						model.addAttribute("student", student);
						return "studentrecored";
					}
					FirstTermResult firstterm= secondclass.getFirsterm();
					
					if(firstterm==null) {
						model.addAttribute("message", "لا توجد بيانات الإمتحان الجزئي للصف الثانى للطالب");
						model.addAttribute("student", student);
						return "studentrecored";
					}
					
					String academic= secondclass.getAcademic();
					int userid= (Integer) session.getAttribute("userid");
					User user= userService.findById(userid);
					String school= user.getSchool();
					model.addAttribute("school", school);
					
						model.addAttribute("firstterm", firstterm);
						model.addAttribute("student", student);
						model.addAttribute("academic", academic);
						
						return "firstclassfisttermarchive";
		}
		
				// getting graduat's id to issue second class first term result
				@GetMapping("/secondclassfirsttermgraduate/{id}")
				public String secondClassFirtTermGraduate(HttpSession session, @PathVariable("id") int id, Model model) {
					
					Graduate student = graduateService.findById(id);
					SecondClass secondclass= student.getSecondclass();
					
					if(secondclass==null) {
						model.addAttribute("message", "لا توجد بيانات الإمتحان الجزئي للصف الثانى للطالب");
						model.addAttribute("student", student);
						return "graduaterecord";
					}
					FirstTermResult firstterm= secondclass.getFirsterm();
					
					if(firstterm==null) {
						model.addAttribute("message", "لا توجد بيانات الإمتحان الجزئي للصف الثانى للطالب");
						model.addAttribute("student", student);
						return "graduaterecord";
					}
					
					String academic= secondclass.getAcademic();
					int userid= (Integer) session.getAttribute("userid");
					User user= userService.findById(userid);
					String school= user.getSchool();
					model.addAttribute("school", school);
					
						model.addAttribute("firstterm", firstterm);
						model.addAttribute("student", student);
						model.addAttribute("academic", academic);
						
						return "firstclassfirsttermgraduate";
		}
				
				
				// getting student's if to issute second class final result
				@GetMapping("/secondclassfinal/{id}")
				public String seconedClassFinal(HttpSession session, @PathVariable("id") int id, Model model) {
					
					Student student = service.findStudent(id);
					SecondClass secondclass= student.getSecondclass();
					
					if(secondclass==null) {
						model.addAttribute("message", "لا توجد بيانات الإمتحان النهائي للصف الثانى للطالب");
						model.addAttribute("student", student);
						return "studentrecored";
					}
					FinalTermResult finalterm= secondclass.getFinalterm();
					
					if(finalterm==null) {
						model.addAttribute("message", "لا توجد بيانات الإمتحان النهائي للصف الثانى للطالب");
						model.addAttribute("student", student);
						return "studentrecored";
					}
					
					String academic= secondclass.getAcademic();
					int userid= (Integer) session.getAttribute("userid");
					User user= userService.findById(userid);
					String school= user.getSchool();
					model.addAttribute("school", school);
					
						model.addAttribute("firstterm", finalterm);
						model.addAttribute("student", student);
						model.addAttribute("academic", academic);
						
						return "firstclassfisttermarchive";
				}
				
				
				// getting student's if to issute second class final result
				@GetMapping("/secondclassfinalgraduate/{id}")
				public String seconedClassFinalGraduate(HttpSession session, @PathVariable("id") int id, Model model) {
					
					Graduate student = graduateService.findById(id);
					SecondClass secondclass= student.getSecondclass();
					
					if(secondclass==null) {
						model.addAttribute("message", "لا توجد بيانات الإمتحان النهائي للصف الثانى للطالب");
						model.addAttribute("student", student);
						return "graduaterecord";
					}
					FinalTermResult finalterm= secondclass.getFinalterm();
					
					if(finalterm==null) {
						model.addAttribute("message", "لا توجد بيانات الإمتحان النهائي للصف الثانى للطالب");
						model.addAttribute("student", student);
						return "graduaterecord";
					}
					
					String academic= secondclass.getAcademic();
					int userid= (Integer) session.getAttribute("userid");
					User user= userService.findById(userid);
					String school= user.getSchool();
					model.addAttribute("school", school);
					
						model.addAttribute("firstterm", finalterm);
						model.addAttribute("student", student);
						model.addAttribute("academic", academic);
						
						return "firstclassfirsttermgraduate";
				}
				
				
				// getting student's id to issue third class first term result
				@GetMapping("/thirdclassfirstterm/{id}")
				public String thirdClassFirtTerm(HttpSession session, @PathVariable("id") int id, Model model) {
					Student student = service.findStudent(id);
					ThirdClass thirdclass= student.getThirdclass();
					
					if(thirdclass==null) {
						model.addAttribute("message", "لا توجد بيانات الإمتحان الجزئي للصف الثالث للطالب");
						model.addAttribute("student", student);
						return "studentrecored";
					}
					FirstTermResult firstterm= thirdclass.getFirsterm();
					
					if(firstterm==null) {
						model.addAttribute("message", "لا توجد بيانات الإمتحان الجزئي للصف الثالث للطالب");
						model.addAttribute("student", student);
						return "studentrecored";
					}
					
					String academic= thirdclass.getAcademic();
					int userid= (Integer) session.getAttribute("userid");
					User user= userService.findById(userid);
					String school= user.getSchool();
					model.addAttribute("school", school);
					
						model.addAttribute("firstterm", firstterm);
						model.addAttribute("student", student);
						model.addAttribute("academic", academic);
						
						return "firstclassfisttermarchive";
		}
				
				// getting student's id to issue third class first term result
				@GetMapping("/thirdclassfirsttermgraduate/{id}")
				public String thirdClassFirtTermGraduate(HttpSession session, @PathVariable("id") int id, Model model) {
					
					
					Graduate student = graduateService.findById(id);
					ThirdClass thirdclass= student.getThirdclass();
					
					if(thirdclass==null) {
						model.addAttribute("message", "لا توجد بيانات الإمتحان الجزئي للصف الثالث للطالب");
						model.addAttribute("student", student);
						return "graduaterecord";
					}
					FirstTermResult firstterm= thirdclass.getFirsterm();
					
					if(firstterm==null) {
						model.addAttribute("message", "لا توجد بيانات الإمتحان الجزئي للصف الثالث للطالب");
						model.addAttribute("student", student);
						return "graduaterecord";
					}
					
					String academic= thirdclass.getAcademic();
					int userid= (Integer) session.getAttribute("userid");
					User user= userService.findById(userid);
					String school= user.getSchool();
					model.addAttribute("school", school);
					
						model.addAttribute("firstterm", firstterm);
						model.addAttribute("student", student);
						model.addAttribute("academic", academic);
						
						return "firstclassfirsttermgraduate";
		}
				
				// getting student's id to issue third class final result
				@GetMapping("/thirdclassfinal/{id}")
				public String thirdClassFinal(HttpSession session, @PathVariable("id") int id, Model model) {
					
					Student student = service.findStudent(id);
					ThirdClass thirdclass= student.getThirdclass();
					
					if(thirdclass==null) {
						model.addAttribute("message", "لا توجد بيانات الإمتحان النهائي للصف الثالث للطالب");
						model.addAttribute("student", student);
						return "studentrecored";
					}
					FinalTermResult finalterm= thirdclass.getFinalterm();
					
					if(finalterm==null) {
						model.addAttribute("message", "لا توجد بيانات الإمتحان النهائي للصف الثالث للطالب");
						model.addAttribute("student", student);
						return "studentrecored";
					}
					
					String academic= thirdclass.getAcademic();
					int userid= (Integer) session.getAttribute("userid");
					User user= userService.findById(userid);
					String school= user.getSchool();
					model.addAttribute("school", school);
					
						model.addAttribute("firstterm", finalterm);
						model.addAttribute("student", student);
						model.addAttribute("academic", academic);
						
						return "firstclassfisttermarchive";
				}
				
				
				
				// getting student's id to issue third class final result
				@GetMapping("/thirdclassfinalgraduate/{id}")
				public String thirdClassFinalGraduate(HttpSession session, @PathVariable("id") int id, Model model) {
					
					Graduate student = graduateService.findById(id);
					ThirdClass thirdclass= student.getThirdclass();
					
					if(thirdclass==null) {
						model.addAttribute("message", "لا توجد بيانات الإمتحان النهائي للصف الثالث للطالب");
						model.addAttribute("student", student);
						return "graduaterecord";
					}
					FinalTermResult finalterm= thirdclass.getFinalterm();
					
					if(finalterm==null) {
						model.addAttribute("message", "لا توجد بيانات الإمتحان النهائي للصف الثالث للطالب");
						model.addAttribute("student", student);
						return "graduaterecord";
					}
					
					String academic= thirdclass.getAcademic();
					int userid= (Integer) session.getAttribute("userid");
					User user= userService.findById(userid);
					String school= user.getSchool();
					model.addAttribute("school", school);
					
						model.addAttribute("firstterm", finalterm);
						model.addAttribute("student", student);
						model.addAttribute("academic", academic);
						
						return "firstclassfirsttermgraduate";
				}
				
				
				// getting student's id to issue fourth class first term result
				@GetMapping("/fourthclassfirstterm/{id}")
				public String fourthClassFirtTerm(HttpSession session, @PathVariable("id") int id, Model model) {
					Student student = service.findStudent(id);
					FourthClass fourthclass= student.getFourthclass();
					
					if(fourthclass==null) {
						model.addAttribute("message", "لا توجد بيانات الإمتحان الجزئي للصف الرابع للطالب");
						model.addAttribute("student", student);
						return "studentrecored";
					}
					FirstTermResult firstterm= fourthclass.getFirsterm();
					
					if(firstterm==null) {
						model.addAttribute("message", "لا توجد بيانات الإمتحان الجزئي للصف الرابع للطالب");
						model.addAttribute("student", student);
						return "studentrecored";
					}
					
					String academic= fourthclass.getAcademic();
					int userid= (Integer) session.getAttribute("userid");
					User user= userService.findById(userid);
					String school= user.getSchool();
					model.addAttribute("school", school);
					
						model.addAttribute("firstterm", firstterm);
						model.addAttribute("student", student);
						model.addAttribute("academic", academic);
						
						return "fourthclassfisttermarchive";
		}
				
				
				// getting student's id to issue fourth class first term result
				@GetMapping("/fourthclassfirsttermgraduate/{id}")
				public String fourthClassFirtTermGraduate(HttpSession session, @PathVariable("id") int id, Model model) {
					
					
					Graduate student = graduateService.findById(id);
					FourthClass fourthclass= student.getFourthclass();
					
					if(fourthclass==null) {
						model.addAttribute("message", "لا توجد بيانات الإمتحان الجزئي للصف الرابع للطالب");
						model.addAttribute("student", student);
						return "graduaterecord";
					}
					FirstTermResult firstterm= fourthclass.getFirsterm();
					
					if(firstterm==null) {
						model.addAttribute("message", "لا توجد بيانات الإمتحان الجزئي للصف الرابع للطالب");
						model.addAttribute("student", student);
						return "graduaterecord";
					}
					
					String academic= fourthclass.getAcademic();
					int userid= (Integer) session.getAttribute("userid");
					User user= userService.findById(userid);
					String school= user.getSchool();
					model.addAttribute("school", school);
					
						model.addAttribute("firstterm", firstterm);
						model.addAttribute("student", student);
						model.addAttribute("academic", academic);
						
						return "fourthclassfirsttermgraduate";
		}
				
				// getting student's id to issue fourth class final result
				@GetMapping("/fourthclassfinal/{id}")
				public String fourthClassFinal(HttpSession session, @PathVariable("id") int id, Model model) {
					
					Student student = service.findStudent(id);
					FourthClass fourthclass= student.getFourthclass();
					
					if(fourthclass==null) {
						model.addAttribute("message", "لا توجد بيانات الإمتحان النهائي للصف الرابع للطالب");
						model.addAttribute("student", student);
						return "studentrecored";
					}
					FinalTermResult finalterm= fourthclass.getFinalterm();
					
					if(finalterm==null) {
						model.addAttribute("message", "لا توجد بيانات الإمتحان النهائي للصف الرابع للطالب");
						model.addAttribute("student", student);
						return "studentrecored";
					}
					
					String academic= fourthclass.getAcademic();
					int userid= (Integer) session.getAttribute("userid");
					User user= userService.findById(userid);
					String school= user.getSchool();
					model.addAttribute("school", school);
					
						model.addAttribute("firstterm", finalterm);
						model.addAttribute("student", student);
						model.addAttribute("academic", academic);
						
						return "fourthclassfisttermarchive";
				}
				
				
				// getting student's id to issue fourth class final result
				@GetMapping("/fourthclassfinalgraduate/{id}")
				public String fourthClassFinalGraduate(HttpSession session, @PathVariable("id") int id, Model model) {
					
					Graduate student = graduateService.findById(id);
					FourthClass fourthclass= student.getFourthclass();
					
					if(fourthclass==null) {
						model.addAttribute("message", "لا توجد بيانات الإمتحان النهائي للصف الرابع للطالب");
						model.addAttribute("student", student);
						return "graduaterecord";
					}
					FinalTermResult finalterm= fourthclass.getFinalterm();
					
					if(finalterm==null) {
						model.addAttribute("message", "لا توجد بيانات الإمتحان النهائي للصف الرابع للطالب");
						model.addAttribute("student", student);
						return "graduaterecord";
					}
					
					String academic= fourthclass.getAcademic();
					int userid= (Integer) session.getAttribute("userid");
					User user= userService.findById(userid);
					String school= user.getSchool();
					model.addAttribute("school", school);
					
						model.addAttribute("firstterm", finalterm);
						model.addAttribute("student", student);
						model.addAttribute("academic", academic);
						
						return "fourthclassfirsttermgraduate";
				}
				
				// getting student's id to issue fifth class first term result
				@GetMapping("/fifthclassfirstterm/{id}")
				public String fifthClassFirtTerm(HttpSession session, @PathVariable("id") int id, Model model) {
					Student student = service.findStudent(id);
					FifthClass fifthclass= student.getFifthclass();
					
					if(fifthclass==null) {
						model.addAttribute("message", "لا توجد بيانات الإمتحان الجزئي للصف الخامس للطالب");
						model.addAttribute("student", student);
						return "studentrecored";
					}
					FirstTermResult firstterm= fifthclass.getFirsterm();
					
					if(firstterm==null) {
						model.addAttribute("message", "لا توجد بيانات الإمتحان الجزئي للصف الخامس للطالب");
						model.addAttribute("student", student);
						return "studentrecored";
					}
					
					String academic= fifthclass.getAcademic();
					int userid= (Integer) session.getAttribute("userid");
					User user= userService.findById(userid);
					String school= user.getSchool();
					model.addAttribute("school", school);
					
						model.addAttribute("firstterm", firstterm);
						model.addAttribute("student", student);
						model.addAttribute("academic", academic);
						
						return "fifthclassfisttermarchive";
		}
				
				
				// getting graduate's id to issue fifth class first term result
				@GetMapping("/fifthclassfirsttermgraduate/{id}")
				public String fifthClassFirtTermGraduate(HttpSession session, @PathVariable("id") int id, Model model) {
					
					Graduate student = graduateService.findById(id);
					FifthClass fifthclass= student.getFifthclass();
					
					if(fifthclass==null) {
						model.addAttribute("message", "لا توجد بيانات الإمتحان الجزئي للصف الخامس للطالب");
						model.addAttribute("student", student);
						return "graduaterecord";
					}
					FirstTermResult firstterm= fifthclass.getFirsterm();
					
					if(firstterm==null) {
						model.addAttribute("message", "لا توجد بيانات الإمتحان الجزئي للصف الخامس للطالب");
						model.addAttribute("student", student);
						return "graduaterecord";
					}
					
					String academic= fifthclass.getAcademic();
					int userid= (Integer) session.getAttribute("userid");
					User user= userService.findById(userid);
					String school= user.getSchool();
					model.addAttribute("school", school);
					
						model.addAttribute("firstterm", firstterm);
						model.addAttribute("student", student);
						model.addAttribute("academic", academic);
						
						return "fifthclassfirsttermgraduate";
		}
			
				
		
				// getting student's id to issue fifth class final result
				@GetMapping("/fifthclassfinal/{id}")
				public String fifthClassFinal(HttpSession session, @PathVariable("id") int id, Model model) {
					
					Student student = service.findStudent(id);
					FifthClass fifthclass= student.getFifthclass();
					
					if(fifthclass==null) {
						model.addAttribute("message", "لا توجد بيانات الإمتحان النهائي للصف الخامس للطالب");
						model.addAttribute("student", student);
						return "studentrecored";
					}
					FinalTermResult finalterm= fifthclass.getFinalterm();
					
					if(finalterm==null) {
						model.addAttribute("message", "لا توجد بيانات الإمتحان النهائي للصف الخامس للطالب");
						model.addAttribute("student", student);
						return "studentrecored";
					}
					
					String academic= fifthclass.getAcademic();
					int userid= (Integer) session.getAttribute("userid");
					User user= userService.findById(userid);
					String school= user.getSchool();
					model.addAttribute("school", school);
					
						model.addAttribute("firstterm", finalterm);
						model.addAttribute("student", student);
						model.addAttribute("academic", academic);
						
						return "fifthclassfisttermarchive";
				}
				
				
				// getting graduate's id to issue fifth class final result
				@GetMapping("/fifthclassfinalgraduate/{id}")
				public String fifthClassFinalGraduate(HttpSession session, @PathVariable("id") int id, Model model) {
					
					Graduate student = graduateService.findById(id);
					FifthClass fifthclass= student.getFifthclass();
					
					if(fifthclass==null) {
						model.addAttribute("message", "لا توجد بيانات الإمتحان النهائي للصف الخامس للطالب");
						model.addAttribute("student", student);
						return "graduaterecord";
					}
					FinalTermResult finalterm= fifthclass.getFinalterm();
					
					if(finalterm==null) {
						model.addAttribute("message", "لا توجد بيانات الإمتحان النهائي للصف الخامس للطالب");
						model.addAttribute("student", student);
						return "graduaterecord";
					}
					
					String academic= fifthclass.getAcademic();
					int userid= (Integer) session.getAttribute("userid");
					User user= userService.findById(userid);
					String school= user.getSchool();
					model.addAttribute("school", school);
					
						model.addAttribute("firstterm", finalterm);
						model.addAttribute("student", student);
						model.addAttribute("academic", academic);
						
						return "fifthclassfirsttermgraduate";
				}
				
				// getting student's id to issue sixth class first term result
				@GetMapping("/sixthclassfirstterm/{id}")
				public String sixthClassFirtTerm(HttpSession session, @PathVariable("id") int id, Model model) {
					Student student = service.findStudent(id);
					SixthClass sixthclass= student.getSixthclass();
					
					if(sixthclass==null) {
						model.addAttribute("message", "لا توجد بيانات الإمتحان الجزئي للصف السادس للطالب");
						model.addAttribute("student", student);
						return "studentrecored";
					}
					FirstTermResult firstterm= sixthclass.getFirsterm();
					
					if(firstterm==null) {
						model.addAttribute("message", "لا توجد بيانات الإمتحان الجزئي للصف السادس للطالب");
						model.addAttribute("student", student);
						return "studentrecored";
					}
					
					String academic= sixthclass.getAcademic();
					int userid= (Integer) session.getAttribute("userid");
					User user= userService.findById(userid);
					String school= user.getSchool();
					model.addAttribute("school", school);
					
						model.addAttribute("firstterm", firstterm);
						model.addAttribute("student", student);
						model.addAttribute("academic", academic);
						
						return "fifthclassfisttermarchive";
		}
				
				
				// getting graduate's id to issue sixth class first term result
				@GetMapping("/sixthclassfirsttermgraduate/{id}")
				public String sixthClassFirtTermGraduate(HttpSession session, @PathVariable("id") int id, Model model) {
					
					
					Graduate student = graduateService.findById(id);
					SixthClass sixthclass= student.getSixthclass();
					
					if(sixthclass==null) {
						model.addAttribute("message", "لا توجد بيانات الإمتحان الجزئي للصف السادس للطالب");
						model.addAttribute("student", student);
						return "graduaterecord";
					}
					FirstTermResult firstterm= sixthclass.getFirsterm();
					
					if(firstterm==null) {
						model.addAttribute("message", "لا توجد بيانات الإمتحان الجزئي للصف السادس للطالب");
						model.addAttribute("student", student);
						return "graduaterecord";
					}
					
					String academic= sixthclass.getAcademic();
					int userid= (Integer) session.getAttribute("userid");
					User user= userService.findById(userid);
					String school= user.getSchool();
					model.addAttribute("school", school);
					
						model.addAttribute("firstterm", firstterm);
						model.addAttribute("student", student);
						model.addAttribute("academic", academic);
						
						return "fifthclassfirsttermgraduate";
		}
				
				
				// getting student's id to issue sixth class final result
				@GetMapping("/sixthclassfinal/{id}")
				public String sixthClassFinal(HttpSession session, @PathVariable("id") int id, Model model) {
					
					Student student = service.findStudent(id);
					SixthClass sixthclass= student.getSixthclass();
					
					if(sixthclass==null) {
						model.addAttribute("message", "لا توجد بيانات الإمتحان النهائي للصف السادس للطالب");
						model.addAttribute("student", student);
						return "studentrecored";
					}
					FinalTermResult finalterm= sixthclass.getFinalterm();
					
					if(finalterm==null) {
						model.addAttribute("message", "لا توجد بيانات الإمتحان النهائي للصف السادس للطالب");
						model.addAttribute("student", student);
						return "studentrecored";
					}
					
					String academic= sixthclass.getAcademic();
					int userid= (Integer) session.getAttribute("userid");
					User user= userService.findById(userid);
					String school= user.getSchool();
					model.addAttribute("school", school);
					
						model.addAttribute("firstterm", finalterm);
						model.addAttribute("student", student);
						model.addAttribute("academic", academic);
						
						return "fifthclassfisttermarchive";
				}
				
				
				// getting graduate's id to issue sixth class final result
				@GetMapping("/sixthclassfinalgraduate/{id}")
				public String sixthClassFinalGraduate(HttpSession session, @PathVariable("id") int id, Model model) {
					
					Graduate student = graduateService.findById(id);
					SixthClass sixthclass= student.getSixthclass();
					
					if(sixthclass==null) {
						model.addAttribute("message", "لا توجد بيانات الإمتحان النهائي للصف السادس للطالب");
						model.addAttribute("student", student);
						return "graduaterecord";
					}
					FinalTermResult finalterm= sixthclass.getFinalterm();
					
					if(finalterm==null) {
						model.addAttribute("message", "لا توجد بيانات الإمتحان النهائي للصف السادس للطالب");
						model.addAttribute("student", student);
						return "graduaterecord";
					}
					
					String academic= sixthclass.getAcademic();
					int userid= (Integer) session.getAttribute("userid");
					User user= userService.findById(userid);
					String school= user.getSchool();
					model.addAttribute("school", school);
					
						model.addAttribute("firstterm", finalterm);
						model.addAttribute("student", student);
						model.addAttribute("academic", academic);
						
						return "fifthclassfirsttermgraduate";
				}
				
				// getting student's id to issue seventh class first term result
				@GetMapping("/seventhclassfirstterm/{id}")
				public String seventhClassFirtTerm(HttpSession session, @PathVariable("id") int id, Model model) {
					Student student = service.findStudent(id);
					SeventhClass seventhclass= student.getSeventhclass();
					
					if(seventhclass==null) {
						model.addAttribute("message", "لا توجد بيانات الإمتحان الجزئى للصف السابع للطالب");
						model.addAttribute("student", student);
						return "studentrecored";
					}
					FirstTermResult firstterm= seventhclass.getFirsterm();
					
					if(firstterm==null) {
						model.addAttribute("message", "لا توجد بيانات الإمتحان الجزئى للصف السابع للطالب");
						model.addAttribute("student", student);
						return "studentrecored";
					}
					
					String academic= seventhclass.getAcademic();
					int userid= (Integer) session.getAttribute("userid");
					User user= userService.findById(userid);
					String school= user.getSchool();
					model.addAttribute("school", school);
					
						model.addAttribute("firstterm", firstterm);
						model.addAttribute("student", student);
						model.addAttribute("academic", academic);
						
						return "fifthclassfisttermarchive";
		}
				
				
				// getting graduate's id to issue seventh class first term result
				@GetMapping("/seventhclassfirsttermgraduate/{id}")
				public String seventhClassFirtTermGraduate(HttpSession session, @PathVariable("id") int id, Model model) {
					
					
					Graduate student = graduateService.findById(id);
					SeventhClass seventhclass= student.getSeventhclass();
					
					if(seventhclass==null) {
						model.addAttribute("message", "لا توجد بيانات الإمتحان الجزئى للصف السابع للطالب");
						model.addAttribute("student", student);
						return "graduaterecord";
					}
					FirstTermResult firstterm= seventhclass.getFirsterm();
					
					if(firstterm==null) {
						model.addAttribute("message", "لا توجد بيانات الإمتحان الجزئى للصف السابع للطالب");
						model.addAttribute("student", student);
						return "graduaterecord";
					}
					
					String academic= seventhclass.getAcademic();
					int userid= (Integer) session.getAttribute("userid");
					User user= userService.findById(userid);
					String school= user.getSchool();
					model.addAttribute("school", school);
					
						model.addAttribute("firstterm", firstterm);
						model.addAttribute("student", student);
						model.addAttribute("academic", academic);
						
						return "fifthclassfirsttermgraduate";
		}
				
				
				// getting student's id to issue seventh class final result
				@GetMapping("/seventhclassfinal/{id}")
				public String seventhClassFinal(HttpSession session, @PathVariable("id") int id, Model model) {
					
					Student student = service.findStudent(id);
					SeventhClass seventhclass= student.getSeventhclass();
					
					if(seventhclass==null) {
						model.addAttribute("message", "لا توجد بيانات الإمتحان النهائي للصف السابع للطالب");
						model.addAttribute("student", student);
						return "studentrecored";
					}
					FinalTermResult finalterm= seventhclass.getFinalterm();
					
					if(finalterm==null) {
						model.addAttribute("message", "لا توجد بيانات الإمتحان النهائي للصف السابع للطالب");
						model.addAttribute("student", student);
						return "studentrecored";
					}
					
					String academic= seventhclass.getAcademic();
					int userid= (Integer) session.getAttribute("userid");
					User user= userService.findById(userid);
					String school= user.getSchool();
					model.addAttribute("school", school);
					
						model.addAttribute("firstterm", finalterm);
						model.addAttribute("student", student);
						model.addAttribute("academic", academic);
						
						return "fifthclassfisttermarchive";
				}
				
				
				// getting graduate's id to issue seventh class final result
				@GetMapping("/seventhclassfinalgraduate/{id}")
				public String seventhClassFinalGraduate(HttpSession session, @PathVariable("id") int id, Model model) {
					
					Graduate student = graduateService.findById(id);
					SeventhClass seventhclass= student.getSeventhclass();
					
					if(seventhclass==null) {
						model.addAttribute("message", "لا توجد بيانات الإمتحان النهائي للصف السابع للطالب");
						model.addAttribute("student", student);
						return "graduaterecord";
					}
					FinalTermResult finalterm= seventhclass.getFinalterm();
					
					if(finalterm==null) {
						model.addAttribute("message", "لا توجد بيانات الإمتحان النهائي للصف السابع للطالب");
						model.addAttribute("student", student);
						return "graduaterecord";
					}
					
					String academic= seventhclass.getAcademic();
					int userid= (Integer) session.getAttribute("userid");
					User user= userService.findById(userid);
					String school= user.getSchool();
					model.addAttribute("school", school);
					
						model.addAttribute("firstterm", finalterm);
						model.addAttribute("student", student);
						model.addAttribute("academic", academic);
						
						return "fifthclassfirsttermgraduate";
				}
				
				
				// getting student's id to issue eighth class first term result
				@GetMapping("/eighthclassfirstterm/{id}")
				public String eighthClassFirtTerm(HttpSession session, @PathVariable("id") int id, Model model) {
					Student student = service.findStudent(id);
					EighthClass eighthclass= student.getEighthclass();
					
					if(eighthclass==null) {
						model.addAttribute("message", "لا توجد بيانات الإمتحان النهائي للصف التاسع للطالب");
						model.addAttribute("student", student);
						return "studentrecored";
					}
					FirstTermResult firstterm= eighthclass.getFirsterm();
					
					if(firstterm==null) {
						model.addAttribute("message", "لا توجد بيانات الإمتحان النهائي للصف التاسع للطالب");
						model.addAttribute("student", student);
						return "studentrecored";
					}
					
					String academic= eighthclass.getAcademic();
					int userid= (Integer) session.getAttribute("userid");
					User user= userService.findById(userid);
					String school= user.getSchool();
					model.addAttribute("school", school);
					
						model.addAttribute("firstterm", firstterm);
						model.addAttribute("student", student);
						model.addAttribute("academic", academic);
						
						return "fifthclassfisttermarchive";
		}
				
				
				// getting student's id to issue eighth class first term result
				@GetMapping("/eighthclassfirsttermgraduate/{id}")
				public String eighthClassFirtTermGraduate(HttpSession session, @PathVariable("id") int id, Model model) {
					
					Graduate student = graduateService.findById(id);
					EighthClass eighthclass= student.getEighthclass();
					
					if(eighthclass==null) {
						model.addAttribute("message", "لا توجد بيانات الإمتحان النهائي للصف التاسع للطالب");
						model.addAttribute("student", student);
						return "graduaterecord";
					}
					FirstTermResult firstterm= eighthclass.getFirsterm();
					
					if(firstterm==null) {
						model.addAttribute("message", "لا توجد بيانات الإمتحان النهائي للصف التاسع للطالب");
						model.addAttribute("student", student);
						return "graduaterecord";
					}
					
					String academic= eighthclass.getAcademic();
					int userid= (Integer) session.getAttribute("userid");
					User user= userService.findById(userid);
					String school= user.getSchool();
					model.addAttribute("school", school);
					
						model.addAttribute("firstterm", firstterm);
						model.addAttribute("student", student);
						model.addAttribute("academic", academic);
						
						return "fifthclassfirsttermgraduate";
		}
				
				
				// getting student's id to issue eighth class final result
				@GetMapping("/eighthclassfinal/{id}")
				public String eighthClassFinal(HttpSession session, @PathVariable("id") int id, Model model) {
					
					Student student = service.findStudent(id);
					EighthClass eighthclass= student.getEighthclass();
					
					if(eighthclass==null) {
						model.addAttribute("message", "لا توجد بيانات الإمتحان النهائي للصف الثامن للطالب");
						model.addAttribute("student", student);
						return "studentrecored";
					}
					FinalTermResult finalterm= eighthclass.getFinalterm();
					
					if(finalterm==null) {
						model.addAttribute("message", "لا توجد بيانات الإمتحان النهائي للصف الثامن للطالب");
						model.addAttribute("student", student);
						return "studentrecored";
					}
					
					String academic= eighthclass.getAcademic();
					int userid= (Integer) session.getAttribute("userid");
					User user= userService.findById(userid);
					String school= user.getSchool();
					model.addAttribute("school", school);
					
						model.addAttribute("firstterm", finalterm);
						model.addAttribute("student", student);
						model.addAttribute("academic", academic);
						
						return "fifthclassfisttermarchive";
				}
				
				
				// getting graduate's id to issue eighth class final result
				@GetMapping("/eighthclassfinalgraduate/{id}")
				public String eighthClassFinalGraduate(HttpSession session, @PathVariable("id") int id, Model model) {
					
					Graduate student = graduateService.findById(id);
					EighthClass eighthclass= student.getEighthclass();
					
					if(eighthclass==null) {
						model.addAttribute("message", "لا توجد بيانات الإمتحان النهائي للصف الثامن للطالب");
						model.addAttribute("student", student);
						return "graduaterecord";
					}
					FinalTermResult finalterm= eighthclass.getFinalterm();
					
					if(finalterm==null) {
						model.addAttribute("message", "لا توجد بيانات الإمتحان النهائي للصف الثامن للطالب");
						model.addAttribute("student", student);
						return "graduaterecord";
					}
					
					String academic= eighthclass.getAcademic();
					int userid= (Integer) session.getAttribute("userid");
					User user= userService.findById(userid);
					String school= user.getSchool();
					model.addAttribute("school", school);
					
						model.addAttribute("firstterm", finalterm);
						model.addAttribute("student", student);
						model.addAttribute("academic", academic);
						
						return "fifthclassfirsttermgraduate";
				}
				

				// getting student's id to issue ninth class first term result
				@GetMapping("/ninthclassfirstterm/{id}")
				public String ninthClassFirtTerm(HttpSession session, @PathVariable("id") int id, Model model) {
					Student student = service.findStudent(id);
					NinthClass ninthclass= student.getNinthclass();
					
					if(ninthclass==null) {
						model.addAttribute("message", "لا توجد بيانات الإمتحان الجزئي للصف التاسع للطالب");
						model.addAttribute("student", student);
						return "studentrecored";
					}
					FirstTermResult firstterm= ninthclass.getFirsterm();
					
					if(firstterm==null) {
						model.addAttribute("message", "لا توجد بيانات الإمتحان الجزئي للصف التاسع للطالب");
						model.addAttribute("student", student);
						return "studentrecored";
					}
					
					String academic= ninthclass.getAcademic();
					int userid= (Integer) session.getAttribute("userid");
					User user= userService.findById(userid);
					String school= user.getSchool();
					model.addAttribute("school", school);
					
						model.addAttribute("firstterm", firstterm);
						model.addAttribute("student", student);
						model.addAttribute("academic", academic);
						
						return "fifthclassfisttermarchive";
		}
				
				
				// getting student's id to issue ninth class first term result
				@GetMapping("/ninthclassfirsttermgraduate/{id}")
				public String ninthClassFirtTermGraduate(HttpSession session, @PathVariable("id") int id, Model model) {
					
					Graduate student = graduateService.findById(id);
					NinthClass ninthclass= student.getNinthclass();
					
					if(ninthclass==null) {
						model.addAttribute("message", "لا توجد بيانات الإمتحان الجزئي للصف التاسع للطالب");
						model.addAttribute("student", student);
						return "graduaterecord";
					}
					FirstTermResult firstterm= ninthclass.getFirsterm();
					
					if(firstterm==null) {
						model.addAttribute("message", "لا توجد بيانات الإمتحان الجزئي للصف التاسع للطالب");
						model.addAttribute("student", student);
						return "graduaterecord";
					}
					
					String academic= ninthclass.getAcademic();
					int userid= (Integer) session.getAttribute("userid");
					User user= userService.findById(userid);
					String school= user.getSchool();
					model.addAttribute("school", school);
					
						model.addAttribute("firstterm", firstterm);
						model.addAttribute("student", student);
						model.addAttribute("academic", academic);
						
						return "fifthclassfirsttermgraduate";
		}
				
				
				// getting student's id to issue ninth class final result
				@GetMapping("/ninthclassfinal/{id}")
				public String ninthClassFinal(HttpSession session, @PathVariable("id") int id, Model model) {
					
					Student student = service.findStudent(id);
					NinthClass ninthclass= student.getNinthclass();
					
					if(ninthclass==null) {
						model.addAttribute("message", "لا توجد بيانات الإمتحان النهائي للصف التاسع للطالب");
						model.addAttribute("student", student);
						return "studentrecored";
					}
					FinalTermResult finalterm= ninthclass.getFinalterm();
					
					if(finalterm==null) {
						model.addAttribute("message", "لا توجد بيانات الإمتحان النهائي للصف التاسع للطالب");
						model.addAttribute("student", student);
						return "studentrecored";
					}
					
					String academic= ninthclass.getAcademic();
					int userid= (Integer) session.getAttribute("userid");
					User user= userService.findById(userid);
					String school= user.getSchool();
					model.addAttribute("school", school);
					
						model.addAttribute("firstterm", finalterm);
						model.addAttribute("student", student);
						model.addAttribute("academic", academic);
						
						return "fifthclassfisttermarchive";
				}
				
				
				// getting student's id to issue ninth class final result
				@GetMapping("/ninthclassfinalgraduate/{id}")
				public String ninthClassFinalGraduate(HttpSession session, @PathVariable("id") int id, Model model) {
					
					Graduate student = graduateService.findById(id);
					NinthClass ninthclass= student.getNinthclass();
					
					if(ninthclass==null) {
						model.addAttribute("message", "لا توجد بيانات الإمتحان النهائي للصف التاسع للطالب");
						model.addAttribute("student", student);
						return "graduaterecord";
					}
					FinalTermResult finalterm= ninthclass.getFinalterm();
					
					if(finalterm==null) {
						model.addAttribute("message", "لا توجد بيانات الإمتحان النهائي للصف التاسع للطالب");
						model.addAttribute("student", student);
						return "graduaterecord";
					}
					
					String academic= ninthclass.getAcademic();
					int userid= (Integer) session.getAttribute("userid");
					User user= userService.findById(userid);
					String school= user.getSchool();
					model.addAttribute("school", school);
					
						model.addAttribute("firstterm", finalterm);
						model.addAttribute("student", student);
						model.addAttribute("academic", academic);
						
						return "fifthclassfirsttermgraduate";
				}
				
				
		// submitting the search form of the seach.html page (search by name)
		@GetMapping("/studentsearch")
		public String studentSearch(@RequestParam("name") String name, Model model, HttpSession session) {
			
			int id= (Integer) session.getAttribute("userid");
		/*	User user = userService.findById(id);
			
			List<Student> students = service.findByNameLike(name);
			
			List<Student> result = new ArrayList<>();
			
			for(Student s : students) {
				if(s.getUser()==user)
					result.add(s);
			}
			*/
			List<Student> result= service.findByStudentNameAndUserId(name, id);
			
			
			if(result.isEmpty())
				model.addAttribute("message", "لا يوجد طالب بهذا الاسم");
			else
				model.addAttribute("students", result);
			return "search";
		}
		
		// getting the id of the student from UPDATE link on the students' list and populate it in a form in studentifo.html 
		@GetMapping("/update/{id}")
		public String studentUpdate(@PathVariable("id") int id, Model model) {
			
			Student student = service.findStudent(id);
			model.addAttribute("student", student);
			
			Academic academic = new Academic();
			List<String> list = academic.getLang();
			
			model.addAttribute("years", list);
			return "studentinfo";
		}
		
		// getting information of the student who will repeat the year
		@GetMapping("/repeat/{id}")
		public String repeat(@PathVariable("id") int id, Model model) {
			
			Student student = service.findStudent(id);
			model.addAttribute("student", student);
			
			Academic academic = new Academic();
			List<String> list = academic.getLang();
			
			model.addAttribute("years", list);
			return "studentrepeat";
			
		}
		
		// getting student's id from DELETE link on studentslist.html page
		@GetMapping("/delete/{id}")
		public String deleteStudent(@PathVariable("id") int id, Model model, HttpSession session) {
			
			service.deleteStudent(id);		
			infoService.deleteInfo(id);
	//		int userid = (Integer) session.getAttribute("userid");
	//		List<Student> list= userService.findStudents(userid);
			model.addAttribute("message", "تم حذف الطالب");
			return "search";
			
			
		}
		
		// getting student's updated information and republish them in studentlist.html page 
		@GetMapping("/submitupdatedstudent")
		public String submitUpdatedInfo(@RequestParam("name") String name, @RequestParam("id") int id, @RequestParam("academic") String year, HttpSession session, Model model) {
		
		//	int userid= (Integer) session.getAttribute("userid");
			Student student= service.findStudent(id);
			student.setName(name);
			student.setAcademic(year);
			service.saveStudent(student);
		//	List<Student> students = userService.findStudents(userid);
			model.addAttribute("students", student);
		
			return "search";  
		}
		
		//submit the year of repeated student
		
		@GetMapping("/submitrepeatedstudent")
		public String submitRepeated(@RequestParam("name") String name, @RequestParam("id") int id, @RequestParam("academic") String year, HttpSession session, Model model) {
			
			int userid= (Integer) session.getAttribute("userid");
			Student student= service.findStudent(id);
			student.setAcademic(year);
			FinalTerm term = student.getFinalterm();
			
			student.setFinalterm(null);
			service.saveStudent(student);
			finalTermService.deleteFinalTerm(term);										
			
			List<Student> students = userService.findStudents(userid);
			model.addAttribute("students", students);
		
			return "studentslist";  
		}
		
		//show students transferring view with the user's id session
		
		@GetMapping("/transfer")
		public String transfer(HttpSession session) {
			int id = (Integer)session.getAttribute("userid");
			session.setAttribute("userid", id);
			return "transfer";
		}
		
		
		
		// getting student's id from FirstTerm link on studenstlist.html
		@GetMapping("/firstterm/{id}")
		public String firstTerm(@PathVariable("id") int id, HttpSession session, Model model) {
			Student student = service.findStudent(id);
			session.setAttribute("student_id", id);
			List<Student> students = (List<Student>) session.getAttribute("students");
			session.setAttribute("students", students);
			model.addAttribute("student", student);
            FirstTerm term = student.getFirstterm();
			Academic academic = new Academic();
            int stuAcademic= academic.getClassCode(student.getAcademic());
           
			if(stuAcademic==1 || stuAcademic==2 || stuAcademic==3) {
				if(term != null) {
					model.addAttribute("term", term);
				
					return "getfirstgraderesulttoedit";
				
				} else if(term==null) {
					model.addAttribute("students", student);
					model.addAttribute("message", "لم ترصد درجات الإمتحان الجزئي للطالب يرجى العودة للقائمة الرئيسية و الضغط على رصد الدرجات لإتمام إجراء رصد الدرجات");
					return "search";
					
					//return "firstgradefromeditpage";
			}
             
             
			
			}else if(stuAcademic==4) {
					if(term !=null) {
					model.addAttribute("term", term);
					
					return "getforthgradetoedit"; 
					}else if(term==null) {
					
						model.addAttribute("students", student);
						model.addAttribute("message", "لم ترصد درجات الإمتحان الجزئي للطالب يرجى العودة للقائمة الرئيسية و الضغط على رصد الدرجات لإتمام إجراء رصد الدرجات");
						return "search";
					//return "forthgradetoedit";    
			}
			}else if(stuAcademic==5 || stuAcademic==6 || stuAcademic==7 || stuAcademic==8 || stuAcademic==9) {
					if(term !=null) {
					model.addAttribute("term", term);
						return "getfifthgradetoedit";
				}else if(term==null) {
						
					model.addAttribute("students", student);
					model.addAttribute("message", "لم ترصد درجات الإمتحان الجزئي للطالب يرجى العودة للقائمة الرئيسية و الضغط على رصد الدرجات لإتمام إجراء رصد الدرجات");
					return "search";
					
						//return "fifthgradetoedit";
				}
			}
			
			
			return "redirect:/studentsacademiclist"; 
		}
		
		
		
		// getting student's id from submitupdatefifthgradefinal link on studenstlist.html
		@GetMapping("/finalterm/{id}")
		public String finalTerm(@PathVariable("id") int id, HttpSession session, Model model) {
			
			Student student = service.findStudent(id);
			session.setAttribute("student_id", id);
            FinalTerm term = student.getFinalterm();
			Academic academic = new Academic();
			model.addAttribute("student", student);
           int stuAcademic = academic.getClassCode(student.getAcademic());
            
            if(stuAcademic==1 || stuAcademic==2 || stuAcademic==3 || stuAcademic==4) {
				if(term != null) {
					model.addAttribute("term", term);
				
					return "getfinalgraderesult";
				
				} else if(term==null) {
					
					model.addAttribute("students", student);
					model.addAttribute("message", "لم ترصد درجات الإمتحان النهائي للطالب يرجى العودة للقائمة الرئيسية و الضغط على رصد الدرجات لإتمام إجراء رصد الدرجات");
					return "search";
					//return "firstgradefinaltoedit";
			}
            }else if(stuAcademic==5) {
				if(term !=null) {
				model.addAttribute("term", term);
				
				return "getforthgradefinalresult";   
				}else if(term==null) {
					model.addAttribute("students", student);
					model.addAttribute("message", "لم ترصد درجات الإمتحان النهائي للطالب يرجى العودة للقائمة الرئيسية و الضغط على رصد الدرجات لإتمام إجراء رصد الدرجات");
					return "search";
				//return "forthgradefinalresult";
		}
		}else if(stuAcademic==6 || stuAcademic==7 || stuAcademic==8 || stuAcademic==9) {
				if(term !=null) {
				model.addAttribute("term", term);
					return "getfifthgradefinal";
			}else if(term==null) {
				
				model.addAttribute("students", student);
				model.addAttribute("message", "لم ترصد درجات الإمتحان النهائي للطالب يرجى العودة للقائمة الرئيسية و الضغط على رصد الدرجات لإتمام إجراء رصد الدرجات");
				return "search";
				//	return "fifthgradefinaledit";
			}
		}
		
		
		return "redirect:/studentsacademiclist"; 
            
		}
		
		
		// getting the grades (submit) from firstgrade.html from
		@GetMapping("/submitfirstgrade")
		public String submitFirstGrade(@RequestParam("id") int id, @RequestParam("math") float math, @RequestParam("arabic") float arabic, @RequestParam("islamic") float islamic, HttpSession session, Model model) {
			Student student = service.findStudent(id);
			
			// new code begins
			
			FirstTerm firstterm = student.getFirstterm();
			if(firstterm==null) {
						
			FirstTerm term = new FirstTerm(math, arabic, islamic);
			student.setFirstterm(term);
			
			float result = Math.round((math+islamic+arabic)/3);
			
			if(result >=50 && result < 65)
				term.setGrade("مقبول");
			else if(result >=65 && result <75 )
				term.setGrade("جيد");
			else if(result >=75 && result <85)
				term.setGrade("جيد جدا");
			else if(result >=85)
				term.setGrade("ممتاز");
			else
				term.setGrade("راسب");
			
			if(term.getMath()<50 || term.getIslamic()<50 || term.getArabic()<50)
				term.setGrade("راسب");
			
			term.setResult(result);
			term.setStudent(student);
			firstTermService.saveFirstTerm(term);
			
			
			}else {
				firstterm.setMath(math);
				firstterm.setIslamic(islamic);
				firstterm.setArabic(arabic);
				student.setFirstterm(firstterm);
				float result = Math.round((math+islamic+arabic)/3);
				
				if(result >=50 && result < 65)
					firstterm.setGrade("مقبول");
				else if(result >=65 && result <75 )
					firstterm.setGrade("جيد");
				else if(result >=75 && result <85)
					firstterm.setGrade("جيد جدا");
				else if(result >=85)
					firstterm.setGrade("ممتاز");
				else
					firstterm.setGrade("راسب");
				
				if(firstterm.getMath()<50 || firstterm.getIslamic()<50 || firstterm.getArabic()<50)
					firstterm.setGrade("راسب");
				
				firstterm.setResult(result);
				firstterm.setStudent(student);
				firstTermService.saveFirstTerm(firstterm);
			}
			// new code ends
			
			
			List<Student> list= (List<Student>) session.getAttribute("list");
			model.addAttribute("students", list);
			
			FinalTerm finalterm = student.getFinalterm();
			if(finalterm!=null) {
				
				int year = Calendar.getInstance().get(Calendar.YEAR);
				
				String acyear = (year-2)+"/"+(year-1);
				Academic acad = new Academic();
				String ac = student.getAcademic();
				int academic =acad.getClassCode(ac);
				FinalTermResult rs = new FinalTermResult(finalterm.getMath(), finalterm.getArabic(), finalterm.getIslamic(), finalterm.getResult(), finalterm.getGrade(), acyear);
				
				// switch place
				saveFinalTermInfo(academic, student, rs);
			
				student.setFinalterm(null);
				service.saveStudent(student);
				finalTermService.deleteFinalTerm(finalterm);
				
			}
			
			return "studentsacademiclist";  
		}

		/*
		// getting the grades (submit) from firstgrade.html from
				@GetMapping("/submitfirstgradefromeditpage")
				public String submitFirstGradeFromEditPage(@RequestParam("id") int id, @RequestParam("math") float math, @RequestParam("arabic") float arabic, @RequestParam("islamic") float islamic, HttpSession session, Model model) {
					Student student = service.findStudent(id);
					FirstTerm term = new FirstTerm(math, arabic, islamic);
					student.setFirstterm(term);
					float result = Math.round((math+islamic+arabic)/3);
					
			//		List<Student> list= (List<Student>) session.getAttribute("students");
					
					if(result >=50 && result < 65)
						term.setGrade("مقبول");
					else if(result >=65 && result <75 )
						term.setGrade("جيد");
					else if(result >=75 && result <85)
						term.setGrade("جيد جدا");
					else if(result >=85)
						term.setGrade("ممتاز");                      // new deletion for edit page
					else
						term.setGrade("راسب");
					
					if(term.getArabic()<50 || term.getIslamic()<50 || term.getMath()<50)
						term.setGrade("راسب");
					
					term.setResult(result);
					term.setStudent(student);
					firstTermService.saveFirstTerm(term);
					model.addAttribute("students", student);
					
					return "search";  
				}
				*/
				
			/*	@GetMapping("/submitfirstgradefinalfromedit")
				public String submitFirstTermFinalresultFromEdit(@RequestParam("id") int id, @RequestParam("math") float math, @RequestParam("arabic") float arabic, @RequestParam("islamic") float islamic, HttpSession session, Model model) {
					Student student = service.findStudent(id);
					FinalTerm term = new FinalTerm(math, arabic, islamic);
					student.setFinalterm(term);
					float result = Math.round((math+islamic+arabic)/3);
					
					List<Student> list= (List<Student>) session.getAttribute("students");
					
					if(result >=50 && result < 65)
						term.setGrade("مقبول");
					else if(result >=65 && result <75 )
						term.setGrade("جيد");
					else if(result >=75 && result <85)
						term.setGrade("جيد جدا");
					else if(result >=85)                       new deletion for edit page
						term.setGrade("ممتاز");
					else
						term.setGrade("راسب");
					
					if(term.getMath()<50 || term.getIslamic()<50 || term.getIslamic()<50)
						term.setGrade("راسب");
					
					if(term.getResult()>=50 && term.getMath()>=50 && term.getArabic()>=50 && term.getIslamic()>=50) {
						
						String year= student.getAcademic();
						Academic ac = new Academic();
						int next= ac.getClassCode(year);
						next +=1;
						String nextYear= ac.getYear(next);
						student.setAcademic(nextYear);
						service.saveStudent(student);
						
						
						
					}else {
						term.setGrade("راسب");
					}
					
					
					student.setFinalterm(term);

					term.setResult(result);
					term.setStudent(student);
					finalTermService.saveFianlTerm(term);
					model.addAttribute("students", student);
					
					return "search";
				}  */
				
				
				// saving first term result method
				public  void saveFirstTermInfo(int acyear, Student student, FirstTermResult firsttermresult) {
					
					
					switch(acyear) {
					
					case 1 : FirstClass fclass = student.getFirstclass();
								if(fclass==null) {
					
					FirstClass firstclass = new FirstClass();
					
					firstclass.setFirsterm(firsttermresult);
					firstclass.setStudent(student);
					firstClassService.saveFirstClass(firstclass);
					student.setFirstclass(firstclass);
				    service.saveStudent(student);
					}else {
						
						fclass.setFirsterm(firsttermresult);
						
						student.setFirstclass(fclass);
						fclass.setStudent(student);
						firstClassService.saveFirstClass(fclass);
						service.saveStudent(student);
					}
								break;
								
					case 2 : SecondClass sclass = student.getSecondclass();
								if(sclass==null) {
									SecondClass secondclass = new SecondClass();
									
									secondclass.setFirsterm(firsttermresult);
									
									secondclass.setFirsterm(firsttermresult);
									student.setSecondclass(secondclass);
									secondclass.setStudent(student);
									secondClassService.saveSecondClass(secondclass);
						            service.saveStudent(student);
									
								}else {
									
									sclass.setFirsterm(firsttermresult);
									
									sclass.setFirsterm(firsttermresult);
									student.setSecondclass(sclass);
									sclass.setStudent(student);
									secondClassService.saveSecondClass(sclass);
						            service.saveStudent(student);
								}
					
								break;
					case 3 : ThirdClass tclass = student.getThirdclass();
								if(tclass==null) {
									ThirdClass thirdclass = new ThirdClass();
									
									thirdclass.setFirsterm(firsttermresult);
									
									thirdclass.setFirsterm(firsttermresult);
									student.setThirdclass(thirdclass);
									thirdclass.setStudent(student);
									thirdClassService.saveThirdClass(thirdclass);
						            service.saveStudent(student);
								}else {
									
									tclass.setFirsterm(firsttermresult);
									
									tclass.setFirsterm(firsttermresult);
									student.setThirdclass(tclass);
									tclass.setStudent(student);
									thirdClassService.saveThirdClass(tclass);
									service.saveStudent(student);
								}
								break;
							
					case 4 : FourthClass frtclass = student.getFourthclass();
								if(frtclass==null) {
									
									FourthClass fourthclass = new FourthClass();
									
									fourthclass.setFirsterm(firsttermresult);
									
									fourthclass.setFirsterm(firsttermresult);
									student.setFourthclass(fourthclass);
									fourthclass.setStudent(student);
									fourthClassService.saveFourthClass(fourthclass);
						            service.saveStudent(student);
									
								}else {
									
									frtclass.setFirsterm(firsttermresult);
									
									frtclass.setFirsterm(firsttermresult);
									student.setFourthclass(frtclass);
									frtclass.setStudent(student);
									fourthClassService.saveFourthClass(frtclass);
									service.saveStudent(student);
								}
								break;
								
					case 5 :	FifthClass fifclass = student.getFifthclass();
								if(fifclass==null) {
									FifthClass fifthclass = new FifthClass();
									
									fifthclass.setFirsterm(firsttermresult);
									
									fifthclass.setFirsterm(firsttermresult);
									student.setFifthclass(fifthclass);
									fifthclass.setStudent(student);								
									fifthClassService.saveFifthClass(fifthclass);
						            service.saveStudent(student);
								}else {
									
									fifclass.setFirsterm(firsttermresult);
									
									fifclass.setFirsterm(firsttermresult);
									student.setFifthclass(fifclass);
									fifclass.setStudent(student);
									fifthClassService.saveFifthClass(fifclass);
									service.saveStudent(student);
								}
								break;
								
					case 6 :  SixthClass sxthclass = student.getSixthclass();
								if(sxthclass==null) {
									SixthClass sixthclass = new SixthClass();
									
									sixthclass.setFirsterm(firsttermresult);
									
									sixthclass.setFirsterm(firsttermresult);
									student.setSixthclass(sixthclass);
									sixthclass.setStudent(student);																
									sixthClassService.saveSixthClass(sixthclass);
						            service.saveStudent(student);
								}else {
									
									sxthclass.setFirsterm(firsttermresult);
									
									sxthclass.setFirsterm(firsttermresult);
									student.setSixthclass(sxthclass);
									sxthclass.setStudent(student);
									sixthClassService.saveSixthClass(sxthclass);
									service.saveStudent(student);
									
								}
								break;
								
					case 7 :	SeventhClass sevclass = student.getSeventhclass();
									if(sevclass==null) {
										SeventhClass seventhclass = new SeventhClass();
										
										seventhclass.setFirsterm(firsttermresult);
										
										seventhclass.setFirsterm(firsttermresult);									
										student.setSeventhclass(seventhclass);									
										seventhclass.setStudent(student);									
										seventhClassService.saveSeventhClass(seventhclass);
							            service.saveStudent(student);
									}else {
										
										sevclass.setFirsterm(firsttermresult);
										
										sevclass.setFirsterm(firsttermresult);									
										student.setSeventhclass(sevclass);
										sevclass.setStudent(student);									
										seventhClassService.saveSeventhClass(sevclass);
										service.saveStudent(student);
									}
									break;
									
					case 8 :	EighthClass eigclass = student.getEighthclass();
									if(eigclass==null) {
										EighthClass eclass = new EighthClass();
										
										eclass.setFirsterm(firsttermresult);
										
										eclass.setFirsterm(firsttermresult);									
										student.setEighthclass(eclass);
										eclass.setStudent(student);									
										eighthClassService.saveEighthClass(eclass);
										service.saveStudent(student);
									}else {
										
										eigclass.setFirsterm(firsttermresult);
										
										eigclass.setFirsterm(firsttermresult);									
										student.setEighthclass(eigclass);
										eigclass.setStudent(student);					
										eighthClassService.saveEighthClass(eigclass);
										service.saveStudent(student);
									}
									
									break;
									
					case 9 :   NinthClass ninclass = student.getNinthclass();
								if(ninclass==null) {
									NinthClass ninthclass = new NinthClass();
									
									ninthclass.setFirsterm(firsttermresult);
									
									ninthclass.setFirsterm(firsttermresult);																	
									student.setNinthclass(ninthclass);
									ninthclass.setStudent(student);																	
									ninthClassService.saveNinthClass(ninthclass);
									service.saveStudent(student);
								}else {
									
									ninclass.setFirsterm(firsttermresult);
									
									ninclass.setFirsterm(firsttermresult);																	
									student.setNinthclass(ninclass);								
									ninclass.setStudent(student);								
									ninthClassService.saveNinthClass(ninclass);
									service.saveStudent(student);
								}
								break;
					}
				}
				
				
				// method of saving final term result 
				
				public  void saveFinalTermInfo(int academic, Student student, FinalTermResult rs) {
					switch(academic) {
					
				/*	case 1 : 
						FirstClass fclass = student.getFirstclass();
						if(fclass==null) {
						FirstClass firstclass = new FirstClass();
						firstclass.setFinalterm(rs);
						finalTermResultService.saveFinalTermResult(rs);  
						firstclass.setStudent(student);
						firstClassService.saveFirstClass(firstclass);
						student.setFirstclass(firstclass);
						service.saveStudent(student);
						} else {
							
							fclass.setFinalterm(rs);
							finalTermResultService.saveFinalTermResult(rs);
							fclass.setStudent(student);
							firstClassService.saveFirstClass(fclass);
							student.setFirstclass(fclass);
							service.saveStudent(student);
						}
						break;  */
						
					case 2 : 
						FirstClass frsclass = student.getFirstclass();
						if(frsclass==null) {
						FirstClass firstclass = new FirstClass();
						firstclass.setFinalterm(rs);
						finalTermResultService.saveFinalTermResult(rs);  
						firstclass.setStudent(student);
						firstClassService.saveFirstClass(firstclass);
						student.setFirstclass(firstclass);
						service.saveStudent(student);
						} else {
							
							frsclass.setFinalterm(rs);
							finalTermResultService.saveFinalTermResult(rs);
							frsclass.setStudent(student);
							firstClassService.saveFirstClass(frsclass);
							student.setFirstclass(frsclass);
							service.saveStudent(student);
						}
						break;
						
					case 3 :
						SecondClass sclass = student.getSecondclass();
						if(sclass==null) {
						SecondClass secondclass = new SecondClass();
						secondclass.setFinalterm(rs);
						finalTermResultService.saveFinalTermResult(rs);
						secondclass.setStudent(student);
						secondClassService.saveSecondClass(secondclass);						
						student.setSecondclass(secondclass);
						service.saveStudent(student);
						}else {
							sclass.setFinalterm(rs);
							sclass.setStudent(student);
							secondClassService.saveSecondClass(sclass);							
							student.setSecondclass(sclass);
							service.saveStudent(student);
						}
						break;
						
					case 4 :
						ThirdClass trdclass = student.getThirdclass();
						if(trdclass==null) {
						ThirdClass thirdclass = new ThirdClass();
						thirdclass.setFinalterm(rs);
						finalTermResultService.saveFinalTermResult(rs);
						thirdclass.setStudent(student);						
						thirdClassService.saveThirdClass(thirdclass);						
						student.setThirdclass(thirdclass);
						service.saveStudent(student);
						}else {
							trdclass.setFinalterm(rs);
							finalTermResultService.saveFinalTermResult(rs);  
							trdclass.setStudent(student);							
							thirdClassService.saveThirdClass(trdclass);
							student.setThirdclass(trdclass);
							service.saveStudent(student);
						}
						break;
						
					case 5 :  FourthClass frclass = student.getFourthclass();
								if(frclass==null) {
									FourthClass fourthclass = new FourthClass();
									fourthclass.setFinalterm(rs);
									finalTermResultService.saveFinalTermResult(rs);
									fourthclass.setStudent(student);
									fourthClassService.saveFourthClass(fourthclass);
									student.setFourthclass(fourthclass);
									service.saveStudent(student);
								}else {
									frclass.setFinalterm(rs);
									finalTermResultService.saveFinalTermResult(rs);  
									frclass.setStudent(student);
									fourthClassService.saveFourthClass(frclass);
									service.saveStudent(student);
								}
									break;
						
					case 6 :	FifthClass fifclass = student.getFifthclass();
									if(fifclass==null) {
										FifthClass fifthclass = new FifthClass();
										fifthclass.setFinalterm(rs);
										finalTermResultService.saveFinalTermResult(rs);  
										fifthclass.setStudent(student);
										fifthClassService.saveFifthClass(fifthclass);
										student.setFifthclass(fifthclass);
										service.saveStudent(student);
										}else {
											fifclass.setFinalterm(rs);
											finalTermResultService.saveFinalTermResult(rs);  
											fifclass.setStudent(student);
											fifthClassService.saveFifthClass(fifclass);
											student.setFifthclass(fifclass);
											service.saveStudent(student);
					}
									break;
						
					case 7 : 	SixthClass sixclass = student.getSixthclass();
								if(sixclass==null) {
									SixthClass sixthclass = new SixthClass();
									sixthclass.setFinalterm(rs);
									finalTermResultService.saveFinalTermResult(rs);  
									sixthclass.setStudent(student);
									sixthClassService.saveSixthClass(sixthclass);
									student.setSixthclass(sixthclass);
									service.saveStudent(student);
									}else {
										sixclass.setFinalterm(rs);
										finalTermResultService.saveFinalTermResult(rs);  
										sixclass.setStudent(student);
										sixthClassService.saveSixthClass(sixclass);
										student.setSixthclass(sixclass);
										service.saveStudent(student);
					}
									break;
						
						
					case 8 :    SeventhClass sevclass = student.getSeventhclass();
								if(sevclass==null) {
									SeventhClass seventhclass = new SeventhClass();
									seventhclass.setFinalterm(rs);
									finalTermResultService.saveFinalTermResult(rs);  
									seventhclass.setStudent(student);
									seventhClassService.saveSeventhClass(seventhclass);
									student.setSeventhclass(seventhclass);
									service.saveStudent(student);
									}else {
										sevclass.setFinalterm(rs);
										finalTermResultService.saveFinalTermResult(rs);  
										sevclass.setStudent(student);
										seventhClassService.saveSeventhClass(sevclass);
										student.setSeventhclass(sevclass);
										service.saveStudent(student);
					}
								break;	
						
						
						
					case 9 :   EighthClass eclass = student.getEighthclass();
								if(eclass==null) {
									EighthClass eihgthclass= new EighthClass();
									eihgthclass.setFinalterm(rs);
									finalTermResultService.saveFinalTermResult(rs);  
									eihgthclass.setStudent(student);
									eighthClassService.saveEighthClass(eihgthclass);
									student.setEighthclass(eihgthclass);
									service.saveStudent(student);
								}else {
									eclass.setFinalterm(rs);
									finalTermResultService.saveFinalTermResult(rs);  
									eclass.setStudent(student);
									eighthClassService.saveEighthClass(eclass);
									student.setEighthclass(eclass);
									service.saveStudent(student);
								}
								
								NinthClass ninclass = student.getNinthclass();
								if(ninclass==null) {
								NinthClass ninthclass = new NinthClass();
								ninthclass.setFinalterm(rs);
								finalTermResultService.saveFinalTermResult(rs);  
								ninthclass.setStudent(student);
								ninthClassService.saveNinthClass(ninthclass);
								student.setNinthclass(ninthclass);
								service.saveStudent(student);
								}else {
									ninclass.setFinalterm(rs);
									finalTermResultService.saveFinalTermResult(rs);  
									ninclass.setStudent(student);
									ninthClassService.saveNinthClass(ninclass);
									student.setNinthclass(ninclass);
									service.saveStudent(student);
					}
								
					break;
						
								
						
					}
				}
		
		@GetMapping("/submitfinalfirstgrade")
		public String submitFinalFirstGrade(Model model, HttpSession session, @RequestParam("id") int id, @RequestParam("math") float math, @RequestParam("arabic") float arabic, @RequestParam("islamic") float islamic) {
			
			
			Student student = service.findStudent(id);
			
			//new section begins
			String aca = student.getAcademic();
			Academic academic = new Academic();
			int acyear= academic.getClassCode(aca);
			FirstTerm firsterm = student.getFirstterm(); 
			int year = Calendar.getInstance().get(Calendar.YEAR);
			
			String acdyear = (year-2)+"/"+(year-1);
			
			if(firsterm!=null) {
				
		
				FirstTermResult firsttermresult= new FirstTermResult(firsterm.getMath(), firsterm.getArabic(), firsterm.getIslamic(), firsterm.getResult(), firsterm.getGrade(), acdyear);
				
				firstTermResultService.saveFirstTermResult(firsttermresult);
			
				saveFirstTermInfo(acyear, student,firsttermresult);
				
			
				
				student.setFirstterm(null);
				firstTermService.delateFirstTerm(firsterm);
				
				
			}
			
			//new section ends
			
			FinalTerm finalterm = student.getFinalterm();
			if(finalterm==null) {
			
			FinalTerm term = new FinalTerm(math, arabic, islamic);
			float result = Math.round((arabic+islamic+math)/3);
			term.setResult(result);
			if(result >=50 && result < 65)
				term.setGrade("مقبول");
			else if(result >=65 && result <75 )
				term.setGrade("جيد");
			else if(result >=75 && result <85)
				term.setGrade("جيد جدا");
			else if(result >=85)
				term.setGrade("ممتاز");
			else
				term.setGrade("راسب");
									
			
			
			if(term.getResult()>=50 && term.getMath()>=50 && term.getArabic()>=50 && term.getIslamic()>=50) {
				
				String curyear= student.getAcademic();
				Academic ac = new Academic();
				int next= ac.getClassCode(curyear);
				next +=1;
				String nextYear= ac.getYear(next);
				student.setAcademic(nextYear);
				service.saveStudent(student);
				
				
				
			}else {
				term.setGrade("راسب");
				
			}
			
			
			student.setFinalterm(term);
			term.setStudent(student);
			finalTermService.saveFianlTerm(term);
			}else {
				
				finalterm.getArabic();
				finalterm.getIslamic();
				finalterm.getMath();
				
				float result = Math.round((arabic+islamic+math)/3);
				finalterm.setResult(result);
				if(result >=50 && result < 65)
					finalterm.setGrade("مقبول");
				else if(result >=65 && result <75 )
					finalterm.setGrade("جيد");
				else if(result >=75 && result <85)
					finalterm.setGrade("جيد جدا");
				else if(result >=85)
					finalterm.setGrade("ممتاز");
				else
					finalterm.setGrade("راسب");
										
				
				
				if(finalterm.getResult()>=50 && finalterm.getMath()>=50 && finalterm.getArabic()>=50 && finalterm.getIslamic()>=50) {
					
					String curyear= student.getAcademic();
					Academic ac = new Academic();
					int next= ac.getClassCode(curyear);
					next +=1;
					String nextYear= ac.getYear(next);
					student.setAcademic(nextYear);
					service.saveStudent(student);
					
					
					
				}else {
					finalterm.setGrade("راسب");
					
				}
				
				
				student.setFinalterm(finalterm);
				finalterm.setStudent(student);
				finalTermService.saveFianlTerm(finalterm);
			}
			
			List<Student> list = (List<Student>) session.getAttribute("list");
			model.addAttribute("students", list);
			
		/*	if(result>=50 && term.getArabic()>=50 && term.getIslamic()>=50 && term.getMath()>=50) {
			String year= student.getAcademic();
			Academic ac = new Academic();
			int next= ac.getClassCode(year);
			next +=1;
			String nextYear= ac.getYear(next);
			student.setAcademic(nextYear);
			service.saveStudent(student);
			}else {
				term.setRepeated(true);
			}
		*/		
			
			
			return "studentsacademiclist";  
		}
		
		//getting the information of submitting the form of getfristgraderesult.html 
		@GetMapping("/updatestudentresult")
		public String updateStduentResult(@RequestParam("math") float math, @RequestParam("arabic") float arabic, @RequestParam("islamic") float islamic, HttpSession session, Model model) {
			
			
			int id= (Integer) session.getAttribute("student_id");
			List<Student> list = (List<Student>) session.getAttribute("list");
			Student student = service.findStudent(id);
			FirstTerm term= student.getFirstterm();
			float result = Math.round((arabic+islamic+math)/3);
			term.setArabic(arabic);
			term.setIslamic(islamic);
			term.setMath(math);
			term.setResult(result);
				
			if(result >=50 && result < 65)
				term.setGrade("مقبول");
			else if(result >=65 && result <75 )
				term.setGrade("جيد");
			else if(result >=75 && result <85)
				term.setGrade("جيد جدا");
			else if(result >=85)
				term.setGrade("ممتاز");
			else
				term.setGrade("راسب");
			
			if(term.getMath()<50 || term.getIslamic()<50 || term.getArabic()<50)
				term.setGrade("راسب");
			
			model.addAttribute("students", list);
			firstTermService.saveFirstTerm(term); 
			
			// new section 
			FinalTerm finalterm = student.getFinalterm();
			if(finalterm!=null) {
				
				
				Academic acad = new Academic();
				String ac = student.getAcademic();
				int academic =acad.getClassCode(ac);
				int year = Calendar.getInstance().get(Calendar.YEAR);
				
				String acdyear = (year-2)+"/"+(year-1);
				FinalTermResult rs = new FinalTermResult(finalterm.getMath(), finalterm.getArabic(), finalterm.getIslamic(), finalterm.getResult(), finalterm.getGrade(), acdyear);
				finalTermResultService.saveFinalTermResult(rs);
				// switch place
				saveFinalTermInfo(academic, student, rs);
				
				student.setFinalterm(null);
				service.saveStudent(student);
				finalTermService.deleteFinalTerm(finalterm);
				
			}
			
			// new section 
			
			return "studentsacademiclist";  
		}

		// editing first grader student result
		@GetMapping("/editresult")
		public String editResult(@RequestParam("math") float math, @RequestParam("arabic") float arabic, @RequestParam("islamic") float islamic, HttpSession session, Model model) {
			
			
			int id= (Integer) session.getAttribute("student_id");
			List<Student> list = (List<Student>) session.getAttribute("students");
			
			Student student = service.findStudent(id);
			FirstTerm term= student.getFirstterm();
			float result = Math.round((arabic+islamic+math)/3);
			term.setArabic(arabic);
			term.setIslamic(islamic);
			term.setMath(math);
			term.setResult(result);
				
			if(result >=50 && result < 65)
				term.setGrade("مقبول");
			else if(result >=65 && result <75 )
				term.setGrade("جيد");
			else if(result >=75 && result <85)
				term.setGrade("جيد جدا");
			else if(result >=85)
				term.setGrade("ممتاز");
			else
				term.setGrade("راسب");
			
			if(term.getArabic()<50 || term.getIslamic()<50 || term.getMath()<50)
				term.setGrade("راسب");
			
			model.addAttribute("students", list);
			firstTermService.saveFirstTerm(term); 
			
			Academic academic = new Academic();
			Map<Integer, String> lang = academic.getIinfo();  
			model.addAttribute("lang", lang);
			model.addAttribute("students", student);
			return "search";  
		}
		
		
		
		//getting the information of submitting the form of getfristgraderesult.html 
				@GetMapping("/updatefirstgradefinalresult")
				public String updateStduentFinalResult(@RequestParam("math") float math, @RequestParam("arabic") float arabic, @RequestParam("islamic") float islamic, HttpSession session, Model model) {
					
					
					int id= (Integer) session.getAttribute("student_id");
					Student student = service.findStudent(id);
					List<Student> list = (List<Student>) session.getAttribute("students");
					FinalTerm term= student.getFinalterm();
					term.setArabic(arabic);
					term.setIslamic(islamic);
					term.setMath(math);
					float result = Math.round((arabic+islamic+math)/3);      
					term.setResult(result);
					
					if(result >=50 && result < 65)
						term.setGrade("مقبول");
					else if(result >=65 && result <75 )
						term.setGrade("جيد");
					else if(result >=75 && result <85)
						term.setGrade("جيد جدا");
					else if(result >=85)
						term.setGrade("ممتاز");
					else
						term.setGrade("راسب");												
					
					if(term.getArabic()<50 || term.getIslamic()<50 || term.getMath()<50)
						term.setGrade("راسب");
					
					if(term.getResult()>=50 && term.getMath()>=50 && term.getArabic()>=50 && term.getIslamic()>=50) {
						
						String year= student.getAcademic();
						Academic ac = new Academic();
						int next= ac.getClassCode(year);
						next +=1;
						String nextYear= ac.getYear(next);
						student.setAcademic(nextYear);
						service.saveStudent(student);
						
						
						
					}else {
						term.setGrade("راسب");
					}
					
					
					student.setFinalterm(term);
					term.setStudent(student);
					finalTermService.saveFianlTerm(term);
					model.addAttribute("students", student);
							
					
					return "search";  
					
					
				}
		
		
		// getting the information of submitting the form in forthgrade.html    
		@GetMapping("/submitforthgrade")
		public String submitForthGrade(HttpSession session, Model model, @RequestParam("math") float math, @RequestParam("arabic") float arabic, @RequestParam("islamic") float islamic, @RequestParam("national") float national, @RequestParam("history") float history, @RequestParam("science") float science) {
			
			int id= (Integer) session.getAttribute("student_id");
			
			Student student = service.findStudent(id);
			
			FirstTerm firstterm = student.getFirstterm();
			if(firstterm==null) {
					
			FirstTerm term = new FirstTerm(math, arabic, islamic, national, history, science);
			
			float result = Math.round((math+islamic+arabic+national+history+science)/6);
			
			if(result >=50 && result < 65)
				term.setGrade("مقبول");
			else if(result >=65 && result <75 )
				term.setGrade("جيد");
			else if(result >=75 && result <85)
				term.setGrade("جيد جدا");
			else if(result >=85)
				term.setGrade("ممتاز");
			else
				term.setGrade("راسب");
			
			if(term.getMath()<50 || term.getIslamic()<50 || term.getArabic()<50 || term.getNational()<50 || term.getHistory()<50 || term.getScience()<50)
				term.setGrade("راسب");
			
			term.setResult(result);
			student.setFirstterm(term);
			term.setStudent(student);
			firstTermService.saveFirstTerm(term);
			} else {
				
				firstterm.setMath(math);
				firstterm.setIslamic(islamic);
				firstterm.setArabic(arabic);
				firstterm.setNational(national);
				firstterm.setHistory(history);
				firstterm.setScience(science);
				
				float result = Math.round((math+islamic+arabic+national+history+science)/6);
				
				if(result >=50 && result < 65)
					firstterm.setGrade("مقبول");
				else if(result >=65 && result <75 )
					firstterm.setGrade("جيد");
				else if(result >=75 && result <85)
					firstterm.setGrade("جيد جدا");
				else if(result >=85)
					firstterm.setGrade("ممتاز");
				else
					firstterm.setGrade("راسب");
				
				if(firstterm.getMath()<50 || firstterm.getIslamic()<50 || firstterm.getArabic()<50 || firstterm.getNational()<50 || firstterm.getHistory()<50 || firstterm.getScience()<50)
					firstterm.setGrade("راسب");
				
				firstterm.setResult(result);
				student.setFirstterm(firstterm);
				firstterm.setStudent(student);
				firstTermService.saveFirstTerm(firstterm);
				
			}
			
			// new section 
			
			FinalTerm finalterm = student.getFinalterm();
			if(finalterm!=null) {
				
				
				Academic acad = new Academic();
				String ac = student.getAcademic();
				int academic =acad.getClassCode(ac);
				int year = Calendar.getInstance().get(Calendar.YEAR);
				
				String acdyear = (year-2)+"/"+(year-1);
				FinalTermResult rs = new FinalTermResult(finalterm.getMath(), finalterm.getArabic(), finalterm.getIslamic(), finalterm.getNational(), finalterm.getHistory(), finalterm.getScience(), finalterm.getResult(), finalterm.getGrade(), acdyear);
				
				// switch place
				saveFinalTermInfo(academic, student, rs);
				
				student.setFinalterm(null);
				service.saveStudent(student);
				finalTermService.deleteFinalTerm(finalterm);
				
			}
			
			// new section
			List<Student> list = (List<Student>) session.getAttribute("list");
			model.addAttribute("students", list);
			
			return "studentsacademiclist"; 
		}
		
	/*	// submitting forth grader student's information from edit page
		@GetMapping("/submitforthgradefromedit")
		public String submitForthGradeFromEdit(HttpSession session, Model model, @RequestParam("math") float math, @RequestParam("arabic") float arabic, @RequestParam("islamic") float islamic, @RequestParam("national") float national, @RequestParam("history") float history, @RequestParam("science") float science) {
			
			int id= (Integer) session.getAttribute("student_id");
			List<Student> list = (List<Student>) session.getAttribute("students");
			Student student = service.findStudent(id);
			FirstTerm term = new FirstTerm(math, arabic, islamic, national, history, science);
			float result = Math.round((math+arabic+islamic+national+history+science)/6);
			term.setResult(result);
			
			if(result >=50 && result < 65)
				term.setGrade("مقبول");
			else if(result >=65 && result <75 )
				term.setGrade("جيد");
			else if(result >=75 && result <85)         new deletion for edit page
				term.setGrade("جيد جدا");
			else if(result >=85)
				term.setGrade("ممتاز");
			else
				term.setGrade("راسب");
			
			term.setStudent(student);
			student.setFirstterm(term);
			firstTermService.saveFirstTerm(term);
																 		
		//	model.addAttribute("students", list);
			model.addAttribute("students", student);
															
			return "search";                              
		 }  */
		
		
		
		// submit forth grade student from edit page 
		@GetMapping("/submitforthgradefromeditfinal")
		public String submitForthGradeFromEditFinal(HttpSession session, Model model, @RequestParam("math") float math, @RequestParam("arabic") float arabic, @RequestParam("islamic") float islamic, @RequestParam("national") float national, @RequestParam("history") float history, @RequestParam("science") float science) {
			
			int id= (Integer) session.getAttribute("student_id");
			List<Student> list = (List<Student>) session.getAttribute("students");
			Student student = service.findStudent(id);
			FinalTerm term = new FinalTerm(math, arabic, islamic, national, history, science);
			float result = Math.round((math+arabic+islamic+national+history+science)/6);
			term.setResult(result);
			
			if(result >=50 && result < 65)
				term.setGrade("مقبول");
			else if(result >=65 && result <75 )
				term.setGrade("جيد");
			else if(result >=75 && result <85)
				term.setGrade("جيد جدا");
			else if(result >=85)
				term.setGrade("ممتاز");
			else
				term.setGrade("راسب");
			
			student.setFinalterm(term);									
			finalTermService.saveFianlTerm(term);
			model.addAttribute("students", list);
																
															
			return "studentslist";    
		}
		
		// getting submitted information form forthgradefinal veiw
		@GetMapping("/submitforthgradefinal")
		public String submitForthGradeFinal(Model model,@RequestParam("id") int id, HttpSession session, @RequestParam("math") float math, @RequestParam("arabic") float arabic, @RequestParam("islamic") float islamic, @RequestParam("national") float national, @RequestParam("history") float history, @RequestParam("science") float science) {
			
			
			Student student = service.findStudent(id);
			
			// new section
			
			String aca = student.getAcademic();
			Academic academic = new Academic();
			int acyear= academic.getClassCode(aca);
			FirstTerm firsterm = student.getFirstterm(); 
			int year = Calendar.getInstance().get(Calendar.YEAR);
			
			String acdyear = (year-2)+"/"+(year-1);
			
			if(firsterm!=null) {
				
				FirstTermResult firsttermresult= new FirstTermResult(firsterm.getMath(), firsterm.getArabic(), firsterm.getIslamic(), firsterm.getNational(), firsterm.getHistory(), firsterm.getScience(), firsterm.getResult(), firsterm.getGrade(), acdyear);
				firstTermResultService.saveFirstTermResult(firsttermresult);
				
				saveFirstTermInfo(acyear, student, firsttermresult);
				
				
				
				student.setFirstterm(null);
				firstTermService.delateFirstTerm(firsterm);
				
				
			}
			
			// new section
			
			FinalTerm finalterm = student.getFinalterm();
			if(finalterm==null) {
			
			FinalTerm term = new FinalTerm(math, arabic, islamic, national, history, science);
			float result = Math.round((math+arabic+islamic+national+history+science)/6);
			term.setResult(result);
			if(result >=50 && result < 65)
				term.setGrade("مقبول");
			else if(result >=65 && result <75 )
				term.setGrade("جيد");
			else if(result >=75 && result <85)
				term.setGrade("جيد جدا");                        
			else if(result >=85)
				term.setGrade("ممتاز");
			else
				term.setGrade("راسب");
			
			if(term.getArabic()>=50 && term.getMath()>=50 && term.getNational()>=50 && term.getHistory()>=50 && term.getScience()>=50)
			{
				
				String curyear= student.getAcademic();
				Academic ac = new Academic();
				int next= ac.getClassCode(curyear);
				next +=1;
				String nextYear= ac.getYear(next);
				student.setAcademic(nextYear);
				service.saveStudent(student);
				
			}else {
				term.setGrade("راسب");
			}
			student.setFinalterm(term);
			term.setStudent(student);
			finalTermService.saveFianlTerm(term);
			}else {
				
				finalterm.setArabic(arabic);
				finalterm.setIslamic(islamic);
				finalterm.setMath(math);
				finalterm.setNational(national);
				finalterm.setHistory(history);
				finalterm.setScience(science);
				
				float result = Math.round((math+arabic+islamic+national+history+science)/6);
				finalterm.setResult(result);
				if(result >=50 && result < 65)
					finalterm.setGrade("مقبول");
				else if(result >=65 && result <75 )
					finalterm.setGrade("جيد");
				else if(result >=75 && result <85)
					finalterm.setGrade("جيد جدا");                        
				else if(result >=85)
					finalterm.setGrade("ممتاز");
				else
					finalterm.setGrade("راسب");
				
				if(finalterm.getArabic()>=50 && finalterm.getMath()>=50 && finalterm.getNational()>=50 && finalterm.getHistory()>=50 && finalterm.getScience()>=50)
				{
					
					String curyear= student.getAcademic();
					Academic ac = new Academic();
					int next= ac.getClassCode(curyear);
					next +=1;
					String nextYear= ac.getYear(next);
					student.setAcademic(nextYear);
					service.saveStudent(student);
					
				}else {
					finalterm.setGrade("راسب");
				}
				student.setFinalterm(finalterm);
				finalterm.setStudent(student);
				finalTermService.saveFianlTerm(finalterm);
				
			}
			
			List<Student> list = (List<Student>) session.getAttribute("list");
			model.addAttribute("students", list);
			
			
		/*	String year= student.getAcademic();
			Academic ac = new Academic();
			int next= ac.getClassCode(year);
			
			if(term.getResult()>=50 && term.getArabic()>=50 && term.getMath()>=50 && term.getIslamic()>=50 && term.getHistory()>=50 && term.getNational()>=50 || term.getScience()>=50) {
			next +=1;
			String nextYear= ac.getYear(next);
			student.setAcademic(nextYear);
			service.saveStudent(student);
			}else {
				term.setRepeated(true);
			}
		*/	
			
			return "studentsacademiclist";  
		}
		
		// getting the submitted information form getforthgrade.html
		@GetMapping("/submitupdatedforthgrade")
		public String submitUpdatedForthGrade(HttpSession session, Model model, @RequestParam("math") float math, @RequestParam("arabic") float arabic, @RequestParam("islamic") float islamic, @RequestParam("national") float national, @RequestParam("history") float history, @RequestParam("science") float science) {
			int id= (Integer) session.getAttribute("student_id");
			List<Student> list = (List<Student>) session.getAttribute("list");
			Student student= service.findStudent(id);
			
			FirstTerm term = student.getFirstterm();
			float result = Math.round((arabic+islamic+math+history+science+national)/6);
			term.setArabic(arabic);
			term.setIslamic(islamic);
			term.setMath(math);
			term.setHistory(history);
			term.setScience(science);
			term.setNational(national);
			
			
			if(result >=50 && result < 65)
				term.setGrade("مقبول");
			else if(result >=65 && result <75 )
				term.setGrade("جيد");
			else if(result >=75 && result <85)
				term.setGrade("جيد جدا");
			else if(result >=85)
				term.setGrade("ممتاز");
			else
				term.setGrade("راسب");
			
			if(term.getMath()<50 || term.getIslamic()<50 || term.getArabic()<50 ||term.getNational()<50 || term.getHistory()<50 || term.getScience()<50 )
				term.setGrade("راسب");
			
			term.setResult(result);
			term.setStudent(student);
			firstTermService.saveFirstTerm(term);
						   														
			model.addAttribute("students", list);
			
			// new section 
			
			FinalTerm finalterm = student.getFinalterm();
			if(finalterm!=null) {
				
				
				Academic acad = new Academic();
				String ac = student.getAcademic();
				int academic =acad.getClassCode(ac);
				int year = Calendar.getInstance().get(Calendar.YEAR);
				
				String acdyear = (year-2)+"/"+(year-1);
				FinalTermResult rs = new FinalTermResult(finalterm.getMath(), finalterm.getArabic(), finalterm.getIslamic(), finalterm.getNational(), finalterm.getHistory(), finalterm.getScience(), finalterm.getResult(), finalterm.getGrade(), acdyear);
				
				// switch place
				saveFinalTermInfo(academic, student, rs);
				
				student.setFinalterm(null);
				service.saveStudent(student);
				finalTermService.deleteFinalTerm(finalterm);
				
			}
			
			// new section 
			
			return "studentsacademiclist"; 
			
		}
		
		
		@GetMapping("/submitupdatedforthgradefromedit")
		public String submitUpdatedForthGradeFromEdit(HttpSession session, Model model, @RequestParam("math") float math, @RequestParam("arabic") float arabic, @RequestParam("islamic") float islamic, @RequestParam("national") float national, @RequestParam("history") float history, @RequestParam("science") float science) {
			int id= (Integer) session.getAttribute("student_id");
			List<Student> list = (List<Student>) session.getAttribute("students");
			Student student= service.findStudent(id);
			FirstTerm term = student.getFirstterm();
			float result = Math.round((arabic+islamic+math+history+science+national)/6);
			term.setArabic(arabic);
			term.setIslamic(islamic);
			term.setMath(math);
			term.setHistory(history);
			term.setScience(science);
			term.setNational(national);
			term.setResult(result);
			
			if(result >=50 && result < 65)
				term.setGrade("مقبول");
			else if(result >=65 && result <75 )
				term.setGrade("جيد");
			else if(result >=75 && result <85)
				term.setGrade("جيد جدا");
			else if(result >=85)
				term.setGrade("ممتاز");
			else
				term.setGrade("راسب");
			
			if(term.getArabic()<50 || term.getIslamic()<50 || term.getMath()<50 || term.getHistory()<50 || term.getScience()<50 || term.getNational()<50)
				term.setGrade("راسب");
			
			firstTermService.saveFirstTerm(term);
		//	model.addAttribute("students", list);
	
			Academic academic = new Academic();
			Map<Integer, String> lang = academic.getIinfo();   
			model.addAttribute("lang", lang);     
			
			model.addAttribute("students", student);
			return "search"; 
			
		}
		
		
		
		@GetMapping("/submitupdatedforthgradefinal")
		public String submitUpdatedForthGradeFinal(Model model, HttpSession session, @RequestParam("math") float math, @RequestParam("arabic") float arabic, @RequestParam("islamic") float islamic, @RequestParam("national") float national, @RequestParam("history") float history, @RequestParam("science") float science) {
			
			int id= (Integer) session.getAttribute("student_id");
			Student student= service.findStudent(id);
			FinalTerm term = student.getFinalterm();
			term.setArabic(arabic);
			term.setIslamic(islamic);
			term.setMath(math);
			term.setHistory(history);
			term.setScience(science);
			term.setNational(national);
			float result = Math.round((arabic+islamic+math+history+science+national)/6);
			term.setResult(result);
			if(result >=50 && result <65)
				term.setGrade("مقبول");
			else if(result >=65 && result <75)
				term.setGrade("جيد");
			else if(result >=75 && result <85)
				term.setGrade("جيد جدا");
			else if(result >=85)
				term.setGrade("ممتاز");
			else
				term.setGrade("راسب");
			
			if(term.getArabic()<50 || term.getIslamic()<50 || term.getMath()<50 || term.getHistory()<50 || term.getScience()<50 || term.getNational()<50)
				term.setGrade("راسب");
			
			term.setStudent(student);
			finalTermService.saveFianlTerm(term);
			
			List<Student> list = (List<Student>) session.getAttribute("students");
			model.addAttribute("students", student);
			
		/*	Academic academic = new Academic();
			Map<Integer, String> lang = academic.getIinfo();  
			model.addAttribute("lang", lang);
			*/
			return "search";  
		}
		
		
	/*	@GetMapping("/submitforthgradefinaltoedit")
		public String fifthGradefinalToEdit(HttpSession session, Model model, @RequestParam("math") float math, @RequestParam("arabic") float arabic, @RequestParam("islamic") float islamic, @RequestParam("national") float national, @RequestParam("history") float history, @RequestParam("science") float science) {             	
			
			int id= (Integer) session.getAttribute("student_id");
			List<Student> list = (List<Student>) session.getAttribute("students");
			Student student = service.findStudent(id);
			FinalTerm term = new FinalTerm(math, arabic, islamic, national, history, science);
			float result = Math.round((math+arabic+islamic+national+history+science)/6);
			term.setResult(result);
			
			if(result >=50 && result < 65)
				term.setGrade("مقبول");
			else if(result >=65 && result <75 )
				term.setGrade("جيد");
			else if(result >=75 && result <85)
				term.setGrade("جيد جدا");
			else if(result >=85)                       new deletion for edit page
				term.setGrade("ممتاز");
			else
				term.setGrade("راسب");
			
			if(term.getArabic()<50 || term.getIslamic()<50 || term.getMath()<50 || term.getHistory()<50 || term.getScience()<50 || term.getNational()<50)
				term.setGrade("راسب");
			
			term.setStudent(student);
			student.setFinalterm(term);
			finalTermService.saveFianlTerm(term);
																 		
			model.addAttribute("students", student);
			
			return "search";
		}   */
		
		// getting the student's information submitted from fifthgrade.html form 
		@GetMapping("/fifthgrade")
		public String fifthGrade(HttpSession session, Model model, @RequestParam("math") float math, @RequestParam("arabic") float arabic, @RequestParam("islamic") float islamic, @RequestParam("national") float national, @RequestParam("history") float history, @RequestParam("science") float science, @RequestParam("computer") float computer, @RequestParam("english") float english) {
			int id=(Integer) session.getAttribute("student_id");
			
			Student student= service.findStudent(id);
			
			FirstTerm firstterm = student.getFirstterm();
			if(firstterm==null) {
			
			FirstTerm term = new FirstTerm(math, arabic, islamic, national, history, science, computer, english);
			float result = Math.round((math+arabic+islamic+national+history+science+computer+english)/8);
			term.setResult(result);
			if(result >=50 && result <65)
				term.setGrade("مقبول");
			else if(result >=65 && result <75)
				term.setGrade("جيد");
			else if(result >=75 && result <85)
				term.setGrade("جيد جدا");
			else if(result >=85)
				term.setGrade("ممتاز");
			else
				term.setGrade("راسب");
			
			if(term.getMath()<50 || term.getIslamic()<50 || term.getArabic()<50 || term.getNational()<50 || term.getHistory()<50 || term.getScience()<50 || term.getComputer()<50 || term.getEnglish()<50)
				term.setGrade("راسب");
			
			student.setFirstterm(term);
			term.setStudent(student);
			firstTermService.saveFirstTerm(term);
			} else {
				
				firstterm.setMath(math);
				firstterm.setArabic(arabic);
				firstterm.setIslamic(islamic);
				firstterm.setNational(national);
				firstterm.setHistory(history);
				firstterm.setScience(science);
				firstterm.setComputer(computer);
				firstterm.setComputer(english);
				float result = Math.round((math+arabic+islamic+national+history+science+computer+english)/8);
				firstterm.setResult(result);
				if(result >=50 && result <65)
					firstterm.setGrade("مقبول");
				else if(result >=65 && result <75)
					firstterm.setGrade("جيد");
				else if(result >=75 && result <85)
					firstterm.setGrade("جيد جدا");
				else if(result >=85)
					firstterm.setGrade("ممتاز");
				else
					firstterm.setGrade("راسب");
				
				if(firstterm.getMath()<50 || firstterm.getIslamic()<50 || firstterm.getArabic()<50 || firstterm.getNational()<50 || firstterm.getHistory()<50 || firstterm.getScience()<50 || firstterm.getComputer()<50 || firstterm.getEnglish()<50)
					firstterm.setGrade("راسب");
				
				student.setFirstterm(firstterm);
				firstterm.setStudent(student);
				firstTermService.saveFirstTerm(firstterm);
				
			}
			
			// new section 
			
			FinalTerm finalterm = student.getFinalterm();
			if(finalterm!=null) {
				
				
				Academic acad = new Academic();
				String ac = student.getAcademic();
				int academic =acad.getClassCode(ac);
				int year = Calendar.getInstance().get(Calendar.YEAR);
				
				String acdyear = (year-2)+"/"+(year-1);
				FinalTermResult rs = new FinalTermResult(finalterm.getMath(), finalterm.getArabic(), finalterm.getIslamic(), finalterm.getNational(), finalterm.getHistory(), finalterm.getScience(), finalterm.getEnglish(), finalterm.getComputer(),  finalterm.getResult(), finalterm.getGrade(), acdyear);
				
				// switch place
				saveFinalTermInfo(academic, student, rs);
				
				student.setFinalterm(null);
				service.saveStudent(student);
				finalTermService.deleteFinalTerm(finalterm);
				
			}
			
			// new section 
			List<Student> list = (List<Student>) session.getAttribute("list");
			model.addAttribute("students", list);
			return "studentsacademiclist";  
			
		}
		
		
		// getting student's result from fifthgradefinalresult view
		@GetMapping("/fifthgradefinalresult")
		public String fifthGradeFinalResult(HttpSession session, Model model, @RequestParam("math") float math, @RequestParam("arabic") float arabic, @RequestParam("islamic") float islamic, @RequestParam("national") float national, @RequestParam("history") float history, @RequestParam("science") float science, @RequestParam("computer") float computer, @RequestParam("english") float english) {
			int id=(Integer) session.getAttribute("student_id");
			
			Student student= service.findStudent(id);
			
			// new section
			
			String aca = student.getAcademic();
			Academic academic = new Academic();
			int acyear= academic.getClassCode(aca);
			FirstTerm firsterm = student.getFirstterm(); 
			
			if(firsterm!=null) {
				
				int year = Calendar.getInstance().get(Calendar.YEAR);
				
				String acdyear = (year-2)+"/"+(year-1);
				FirstTermResult firsttermresult= new FirstTermResult(firsterm.getMath(), firsterm.getArabic(), firsterm.getIslamic(), firsterm.getNational(), firsterm.getHistory(), firsterm.getScience(), firsterm.getEnglish(), firsterm.getComputer(), firsterm.getResult(), firsterm.getGrade(), acdyear);
				firstTermResultService.saveFirstTermResult(firsttermresult);

				saveFirstTermInfo(acyear, student, firsttermresult);
				
				student.setFirstterm(null);
				firstTermService.delateFirstTerm(firsterm);

			}
			
			// new section
			FinalTerm finalterm = student.getFinalterm();
			if(finalterm==null) {
			
			FinalTerm term = new FinalTerm(math, arabic, islamic, national, history, science, computer, english);
			float result = Math.round((math+arabic+islamic+national+history+science+computer+english)/8);
			term.setResult(result);
			if(result >=50 && result <65)
				term.setGrade("مقبول");
			else if(result >=65 && result <75)
				term.setGrade("جيد");
			else if(result >=75 && result <85)
				term.setGrade("جيد جدا");
			else if(result >=85)
				term.setGrade("ممتاز");
			else
				term.setGrade("راسب");
			
			if(term.getMath()>=50 && term.getArabic()>=50 && term.getIslamic()>=50 && term.getNational()>=50 && term.getHistory()>=50 && term.getScience()>=50 && term.getComputer()>=50 && term.getEnglish()>=50) 
				
			{
				String year= student.getAcademic();
				Academic ac = new Academic();
				int next= ac.getClassCode(year);
				next +=1;
				String nextYear= ac.getYear(next);
				student.setAcademic(nextYear);
				service.saveStudent(student);
					
				
			}else {
				term.setGrade("راسب");
			}
			
			
			student.setFinalterm(term);
			term.setStudent(student);
			finalTermService.saveFianlTerm(term);
			
			
			int acadyear = academic.getClassCode(student.getAcademic());
			
				if(acadyear==10) {
					acadyear -= 1;
					int year = Calendar.getInstance().get(Calendar.YEAR);
					String acdyear = (year-1)+"/"+(year);
			
					FinalTermResult finaltermresult = new FinalTermResult(term.getMath(), term.getArabic(), term.getIslamic(), term.getNational(), term.getHistory(), term.getScience(), term.getResult(), term.getGrade(), acdyear);
					finalTermResultService.saveFinalTermResult(finaltermresult);
					saveFinalTermInfo(acadyear, student, finaltermresult);
										

					Graduate graduate = new Graduate(student.getId(), student.getName(), student.getFirstterm(), student.getFinalterm(), student.getUser(), student.getAcademic(), student.getFirstclass(), student.getSecondclass(), student.getThirdclass(), student.getFourthclass(), student.getFifthclass(), student.getSixthclass(), student.getSeventhclass(), student.getEighthclass(), student.getNinthclass());
					graduateService.saveGraduate(graduate);
					//classes code begins 
					FirstClass fclass = student.getFirstclass();
					if(fclass!=null) {
						fclass.setStudent(null);
						fclass.setGraduate(graduate);
						firstClassService.saveFirstClass(fclass);
					}
					SecondClass sclass = student.getSecondclass();
					if(sclass!=null) {
						sclass.setStudent(null);
						sclass.setGraduate(graduate);
						secondClassService.saveSecondClass(sclass);
					}
					ThirdClass thclass = student.getThirdclass();
					if(thclass!=null) {
						thclass.setStudent(null);
						thclass.setGraduate(graduate);
						thirdClassService.saveThirdClass(thclass);
					}
					FourthClass frclass = student.getFourthclass();
					if(frclass!=null) {
						frclass.setStudent(null);
						frclass.setGraduate(graduate);
						fourthClassService.saveFourthClass(frclass);
					}
					FifthClass fifclass = student.getFifthclass();
					if(fifclass!=null) {
						fifclass.setStudent(null);
						fifclass.setGraduate(graduate);
						fifthClassService.saveFifthClass(fifclass);
					}
					SixthClass sxthclass = student.getSixthclass();
					if(sxthclass!=null) {
						sxthclass.setStudent(null);
						sxthclass.setGraduate(graduate);
						sixthClassService.saveSixthClass(sxthclass);
					}
					SeventhClass sevclass = student.getSeventhclass();
					if(sevclass!=null) {
						sevclass.setStudent(null);
						sevclass.setGraduate(graduate);
						seventhClassService.saveSeventhClass(sevclass);
					}
					EighthClass egclass = student.getEighthclass();
					if(egclass!=null) {
						egclass.setStudent(null);
						egclass.setGraduate(graduate);
						eighthClassService.saveEighthClass(egclass);
					}
					NinthClass ninclass = student.getNinthclass();
					if(ninclass!=null) {
						ninclass.setStudent(null);
						ninclass.setGraduate(graduate);
						ninthClassService.saveNinthClass(ninclass);
					}
						
					//classes code ends
						
						
						service.deleteStudent(id);
					
				}
			}else {
				
				finalterm.setMath(math);
				finalterm.setArabic(arabic);
				finalterm.setIslamic(islamic);
				finalterm.setNational(national);
				finalterm.setHistory(history);
				finalterm.setScience(science);
				finalterm.setComputer(computer);
				finalterm.setEnglish(english);
				
				float result = Math.round((math+arabic+islamic+national+history+science+computer+english)/8);
				finalterm.setResult(result);
				if(result >=50 && result <65)
					finalterm.setGrade("مقبول");
				else if(result >=65 && result <75)
					finalterm.setGrade("جيد");
				else if(result >=75 && result <85)
					finalterm.setGrade("جيد جدا");
				else if(result >=85)
					finalterm.setGrade("ممتاز");
				else
					finalterm.setGrade("راسب");
				
				if(finalterm.getMath()>=50 && finalterm.getArabic()>=50 && finalterm.getIslamic()>=50 && finalterm.getNational()>=50 && finalterm.getHistory()>=50 && finalterm.getScience()>=50 && finalterm.getComputer()>=50 && finalterm.getEnglish()>=50) 
					
				{
					String year= student.getAcademic();
					Academic ac = new Academic();
					int next= ac.getClassCode(year);
					next +=1;
					String nextYear= ac.getYear(next);
					student.setAcademic(nextYear);
					service.saveStudent(student);
						
					
				}else {
					finalterm.setGrade("راسب");
				}
				
				
				student.setFinalterm(finalterm);
				finalterm.setStudent(student);
				finalTermService.saveFianlTerm(finalterm);
				
				
				int acadyear = academic.getClassCode(student.getAcademic());
				
					if(acadyear==10) {
						acadyear -= 1;
						int year = Calendar.getInstance().get(Calendar.YEAR);
						String acdyear = (year-1)+"/"+(year);
				
						FinalTermResult finaltermresult = new FinalTermResult(finalterm.getMath(), finalterm.getArabic(), finalterm.getIslamic(), finalterm.getNational(), finalterm.getHistory(), finalterm.getScience(), finalterm.getResult(), finalterm.getGrade(), acdyear);
						finalTermResultService.saveFinalTermResult(finaltermresult);
						saveFinalTermInfo(acadyear, student, finaltermresult);
						Graduate graduate = new Graduate(student.getId(), student.getName(), student.getFirstterm(), student.getFinalterm(), student.getUser(), student.getAcademic(), student.getFirstclass(), student.getSecondclass(), student.getThirdclass(), student.getFourthclass(), student.getFifthclass(), student.getSixthclass(), student.getSeventhclass(), student.getEighthclass(), student.getNinthclass());
						graduateService.saveGraduate(graduate);
						service.deleteStudent(id);
						
			}
			}
			List<Student> list = (List<Student>) session.getAttribute("list");
			model.addAttribute("students", list);
				
			return "studentsacademiclist";
		}
		
		
		
	/*	// submitting information of fifth grader from edit page
		@GetMapping("/fifthgradefromedit")
		public String fifthGradeFromEditPage(HttpSession session, Model model, @RequestParam("math") float math, @RequestParam("arabic") float arabic, @RequestParam("islamic") float islamic, @RequestParam("national") float national, @RequestParam("history") float history, @RequestParam("science") float science, @RequestParam("computer") float computer, @RequestParam("english") float english) {
			int id=(Integer) session.getAttribute("student_id");
			List<Student> list = (List<Student>) session.getAttribute("students");
			Student student= service.findStudent(id);
			FirstTerm term = new FirstTerm(math, arabic, islamic, national, history, science, computer, english);
			float result = Math.round((math+arabic+islamic+national+history+science+computer+english)/8);
			term.setResult(result);
			if(result >=50 && result <65)
				term.setGrade("مقبول");
			else if(result >=65 && result <75)
				term.setGrade("جيد");
			else if(result >=75 && result <85)
				term.setGrade("جيد جدا");
			else if(result >=85)                          new deletion for edit page
				term.setGrade("ممتاز");
			else
				term.setGrade("راسب");
			
			if(term.getMath()<50 || term.getArabic()<50 || term.getIslamic()<50 || term.getNational()<50 || term.getHistory()<50 || term.getScience()<50 || term.getComputer()<50 || term.getEnglish()<50 )
				term.setGrade("راسب");
				
			student.setFirstterm(term);
			term.setStudent(student);
			firstTermService.saveFirstTerm(term);
			model.addAttribute("students", student);
		//	model.addAttribute("students", list);	
			
			return "search";  
			
		}  */
		
		
	/*	@GetMapping("/submitfifthgradefinalfromedit")
		public String submitFifthGradeFinalFromEdit(HttpSession session, Model model, @RequestParam("math") float math, @RequestParam("arabic") float arabic, @RequestParam("islamic") float islamic, @RequestParam("national") float national, @RequestParam("history") float history, @RequestParam("science") float science, @RequestParam("computer") float computer, @RequestParam("english") float english) {
			int id=(Integer) session.getAttribute("student_id");
			List<Student> list = (List<Student>) session.getAttribute("students");
			Student student= service.findStudent(id);
			FinalTerm term = new FinalTerm(math, arabic, islamic, national, history, science, computer, english);
			float result = Math.round((math+arabic+islamic+national+history+science+computer+english)/8);
			term.setResult(result);
			if(result >=50 && result <65)
				term.setGrade("مقبول");
			else if(result >=65 && result <75)
				term.setGrade("جيد");
			else if(result >=75 && result <85)
				term.setGrade("جيد جدا");
			else if(result >=85)
				term.setGrade("ممتاز");
			else                                        new deletion for edit page
				term.setGrade("راسب");
			
			if(term.getMath()<50 || term.getArabic()<50 || term.getIslamic()<50 || term.getNational()<50 || term.getEnglish()<50 || term.getHistory()<50 || term.getScience()<50 || term.getComputer()< 50)
				term.setGrade("راسب");
				
			student.setFinalterm(term);	
			term.setStudent(student);
			finalTermService.saveFianlTerm(term);
			model.addAttribute("students", student);
			return "search";
		} */
		
		// submitting information from fifthgradefinal.html page
		@GetMapping("/fifthgradefinal")  
		public String fifthGradeFinal(HttpSession session, Model model,@RequestParam("id") int id, @RequestParam("math") float math, @RequestParam("arabic") float arabic, @RequestParam("islamic") float islamic, @RequestParam("national") float national, @RequestParam("history") float history, @RequestParam("science") float science, @RequestParam("computer") float computer, @RequestParam("english") float english) {
				
			List<String> list = (List<String>) session.getAttribute("students");
			Student student= service.findStudent(id);
			FinalTerm term = new FinalTerm(math, arabic, islamic, national, history, science, computer, english);
			float result = Math.round((math+arabic+islamic+national+history+science+computer+english)/8);
			term.setResult(result);
			
			if(result >=50 && result <65)
				term.setGrade("مقبول");
			else if(result >=65 && result <75)
				term.setGrade("جيد");
			else if(result >=75 && result <85)
				term.setGrade("جيد جدا");
			else if(result >=85)					
				term.setGrade("ممتاز");
			else
				term.setGrade("راسب");
			
			
			student.setFinalterm(term);
			finalTermService.saveFianlTerm(term);
			model.addAttribute("students", list);
			Academic academic = new Academic();
			Map<Integer, String> lang = academic.getIinfo();  
			model.addAttribute("lang", lang);
			
			model.addAttribute("students", list);
			return "studentsacademiclist";  
		}
		
		
		// submitting edited fifth grader student's information
		@GetMapping("/submitupdatefifthgrade")
		public String submitUpdateFifthGrade(HttpSession session,Model model ,@RequestParam("math") float math, @RequestParam("arabic") float arabic, @RequestParam("islamic") float islamic, @RequestParam("national") float national, @RequestParam("history") float history, @RequestParam("science") float science, @RequestParam("computer") float computer, @RequestParam("english") float english) {
			int id=(Integer) session.getAttribute("student_id");
			List<Student> list = (List<Student>) session.getAttribute("list");
			Student student= service.findStudent(id);
			FirstTerm term= student.getFirstterm();
			float result = Math.round((arabic+computer+english+history+islamic+math+national+science)/8);
			term.setArabic(arabic);
			term.setComputer(computer);
			term.setEnglish(english);
			term.setHistory(history);
			term.setIslamic(islamic);
			term.setMath(math);
			term.setNational(national);
			term.setScience(science);
			term.setResult(result);
			
			
			if(result >=50 && result < 65)
				term.setGrade("مقبول");
			else if(result >=65 && result <75 )
				term.setGrade("جيد");
			else if(result >=75 && result <85)
				term.setGrade("جيد جدا");
			else if(result >=85)
				term.setGrade("ممتاز");
			else
				term.setGrade("راسب");
			
			if(term.getMath()<50 || term.getIslamic()<50 || term.getArabic()<50 || term.getComputer()<50 || term.getEnglish()<50 || term.getHistory()<50 || term.getNational()<50 || term.getScience()<50)
				term.setGrade("راسب");
			
			firstTermService.saveFirstTerm(term); 
			model.addAttribute("students", list);
			
			// new section 
			
			FinalTerm finalterm = student.getFinalterm();
			if(finalterm!=null) {
				
				
				Academic acad = new Academic();
				String ac = student.getAcademic();
				int academic =acad.getClassCode(ac);
				int year = Calendar.getInstance().get(Calendar.YEAR);
				
				String acdyear = (year-2)+"/"+(year-1);
				FinalTermResult rs = new FinalTermResult(finalterm.getMath(), finalterm.getArabic(), finalterm.getIslamic(), finalterm.getNational(), finalterm.getHistory(), finalterm.getScience(), finalterm.getEnglish(), finalterm.getComputer(), finalterm.getResult(), finalterm.getGrade(), acdyear);
				
				// switch place
				saveFinalTermInfo(academic, student, rs);
				
				student.setFinalterm(null);
				service.saveStudent(student);
				finalTermService.deleteFinalTerm(finalterm);
				
			}
			
			// new section 
			
			return "studentsacademiclist";  
			
		}
		
		
		@GetMapping("/submitupdatefifthgradefromedit") 
		public String submitUpdateFifthGradeFromEdit(HttpSession session,Model model ,@RequestParam("math") float math, @RequestParam("arabic") float arabic, @RequestParam("islamic") float islamic, @RequestParam("national") float national, @RequestParam("history") float history, @RequestParam("science") float science, @RequestParam("computer") float computer, @RequestParam("english") float english) {
			int id=(Integer) session.getAttribute("student_id");
			List<Student> list = (List<Student>) session.getAttribute("students");
			Student student= service.findStudent(id);
			FirstTerm term= student.getFirstterm();
			float result = Math.round((arabic+computer+english+history+islamic+math+national+science)/8);
			term.setArabic(arabic);
			term.setComputer(computer);
			term.setEnglish(english);
			term.setHistory(history);
			term.setIslamic(islamic);
			term.setMath(math);
			term.setNational(national);
			term.setScience(science);
			term.setResult(result);
			
			
			if(result >=50 && result < 65)
				term.setGrade("مقبول");
			else if(result >=65 && result <75 )
				term.setGrade("جيد");
			else if(result >=75 && result <85)
				term.setGrade("جيد جدا");
			else if(result >=85)
				term.setGrade("ممتاز");
			else
				term.setGrade("راسب");
			
			if(term.getArabic()<50 || term.getIslamic()<50 || term.getMath()<50 || term.getComputer()<50 || term.getEnglish()<50 || term.getHistory()<50 || term.getNational()<50 || term.getScience()<50 )
			term.setGrade("راسب");
			
			term.setStudent(student);
			firstTermService.saveFirstTerm(term); 
			model.addAttribute("students", student);
			
		/*	model.addAttribute("students", list);
			Academic academic = new Academic();
			Map<Integer, String> lang = academic.getIinfo();  
			model.addAttribute("lang", lang);
		*/	
			
			
			return "search";  
			
		}
		
		@GetMapping("/submitupdatefifthgradefinal")
		public String submitUpdateFifthGradeFinal(Model model, HttpSession session, @RequestParam("math") float math, @RequestParam("arabic") float arabic, @RequestParam("islamic") float islamic, @RequestParam("national") float national, @RequestParam("history") float history, @RequestParam("science") float science, @RequestParam("computer") float computer, @RequestParam("english") float english) {
			
			int id=(Integer) session.getAttribute("student_id");
			Student student= service.findStudent(id);
			FinalTerm term= student.getFinalterm();
			term.setArabic(arabic);
			term.setComputer(computer);
			term.setEnglish(english);
			term.setHistory(history);
			term.setIslamic(islamic);
			term.setMath(math);
			term.setNational(national);
			term.setScience(science);
			float result = Math.round((arabic+computer+english+history+islamic+math+national+science)/8);
			term.setResult(result);
			
			if(result >=50 && result < 65)
				term.setGrade("مقبول");
			else if(result >=65 && result <75 )
				term.setGrade("جيد");
			else if(result >=75 && result <85)
				term.setGrade("جيد جدا");
			else if(result >=85)
				term.setGrade("ممتاز");
			else
				term.setGrade("راسب");
			
			if(term.getArabic()<50 || term.getIslamic()<50 || term.getComputer()<50 || term.getEnglish()<50 || term.getHistory()<50 || term.getMath()<50 || term.getNational()<50 || term.getNational()<50)
				term.setGrade("راسب");
			term.setStudent(student);
		
			finalTermService.saveFianlTerm(term);
			List<Student> list = (List<Student>) session.getAttribute("students");
			model.addAttribute("students", student);
		/*	Academic academic = new Academic();
			Map<Integer, String> lang = academic.getIinfo();  
			model.addAttribute("lang", lang);
			*/
			return "search";  
		}
		
		//launch students classified by classes view with user's id transferred
		@GetMapping("/studentsbyacademic")
		public String studentsByAcademic(HttpSession session) {
			int id = (Integer) session.getAttribute("userid");
			session.setAttribute("userid", id); 
			return "studentsAcademicClasses";
		}
		
		// getting information from studentsAcademicClasses.html 
		@GetMapping("/listbyacademic/{ac}")
		public String studentsByClasses(@PathVariable("ac") String year ,HttpSession session, Model model) {
		
			
			int id = (Integer) session.getAttribute("userid");
			List<Student> students = userService.findStudents(id);
			List<Student> list = new ArrayList<>();
			
			for(Student s : students) {
				
				if(s.getAcademic().equals(year))
					list.add(s);
				
			}
			
			model.addAttribute("students", list);
			model.addAttribute("year", year);
			session.setAttribute("list", list);
			
			
			return "studentsacademiclist";
		}
		
		//getting order from welcome page to display student record
		@GetMapping("/studentrecord")
		public String studentRecord(HttpSession session) {
			int id= (Integer) session.getAttribute("userid");
			
			session.setAttribute("userid", id);
			return "studentsrecord";
		}
		
		
		@GetMapping("/studentsrecord/{ac}")
		public String studentRecByAcademic(@PathVariable("ac") String academic, HttpSession session, Model model) {
			
			int id = (Integer) session.getAttribute("userid");
			List<Student> students = userService.findStudents(id);
			List<Student> list = new ArrayList<Student>();
			for(Student s : students) {
				if(s.getAcademic().equals(academic))
					list.add(s);
			}
			
			model.addAttribute("students", list);
			session.setAttribute("students", list);      
			model.addAttribute("year", academic);
			return "studentsrecordbyacademic";
			
		}
		
		// getting student's id from students record section for first term process
		@GetMapping("/firsttermrecord/{id}")
		public String studentsFirstTerm(@PathVariable("id") int id, Model model, HttpSession session) {
			
			
			Student student = service.findStudent(id);
			FirstTerm term = student.getFirstterm();
			Academic academic = new Academic();
			String ac= student.getAcademic();
			int year= academic.getClassCode(ac);
			int userid= (Integer) session.getAttribute("userid");
			User user= userService.findById(userid);
			String school= user.getSchool();
			model.addAttribute("student", student);
			model.addAttribute("school", school);
			if(term==null) {
				List<Student> list=(List<Student>) session.getAttribute("students");
				model.addAttribute("students", list);
				model.addAttribute("year", ac);
				model.addAttribute("message", " لا توجد نتيجة للطالب يمكن البحث عنها فى الأرشيف");     
				return "studentsrecordbyacademic";
			}else {
			
				model.addAttribute("term", term);
				model.addAttribute("year", ac);
				if(year==1 || year==2 || year==3) {
					
					return "firstgradefirsttermrecord";
				}else if(year==5 || year==6 || year==7 || year==8 || year==9 || year==10) {
					return "fifthgradefirsttermrecord";
				
			}else if(year==4)
				return "forthgradefirsttermresult";
			}
			return"studentsrecordbyacademic";
		}
		
		// getting student's id from students record section for final term process
		@GetMapping("/finaltermrecord/{id}")
		public String studentFianlTerm(@PathVariable("id") int id, Model model, HttpSession session) {
			
			Student student = service.findStudent(id);
			FinalTerm term = student.getFinalterm();
			String ac= student.getAcademic();
			if(term==null) {
				List<Student> list=(List<Student>) session.getAttribute("students");
				model.addAttribute("students", list);
				model.addAttribute("year", ac);
				model.addAttribute("message", "لا توجد نتيجة  لهذا الطالب");     
				return "studentsrecordbyacademic";
			} else {
			
			String grade = term.getGrade();
			Academic academic = new Academic();
			
			
			int year= academic.getClassCode(ac);
			int userid= (Integer) session.getAttribute("userid");
			User user= userService.findById(userid);
			String school= user.getSchool();
			model.addAttribute("student", student);
			model.addAttribute("school", school);
			
			
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
					
					return "firstgradefirsttermrecord";
					
				}else if(year==6 || year==7 || year==8 || year==9 || year==10) {
					
					if(grade.equals("راسب")) {
						String curYear= academic.getYear(year); 
						model.addAttribute("year", curYear);
					}else {
					
					year -=1;
					String curYear= academic.getYear(year);  
					model.addAttribute("year", curYear);
					}
					return "fifthgradefirsttermrecord";
				
			}else if(year==5) {
				
				if(grade.equals("راسب")) {
					String curYear= academic.getYear(year); 
					model.addAttribute("year", curYear);
				}else {
				
				year -=1;
				String curYear= academic.getYear(year);  
				model.addAttribute("year", curYear);
				
				}
				return "forthgradefirsttermresult";
			} 
			}
			return"studentsrecordbyacademic";
		}
		
		
		// setting the first term result
		@GetMapping("/firsttermbyacademic/{id}")
		public String firstTermByAcademic(@PathVariable("id") int id, Model model, HttpSession session) {
			
			Student student= service.findStudent(id);
			session.setAttribute("student_id", id);
			Academic stuAcademic = new Academic();
			int academic = stuAcademic.getClassCode(student.getAcademic());
			model.addAttribute("student", student);
			
			FirstTerm term= student.getFirstterm();
			if(academic==1 || academic==2 || academic==3) {
			/*	if(term!=null) {
				
					return "getfirstgraderesult";
				
				}else if(term ==null) {
					
					model.addAttribute("student", student);
					return "firstgrade";
				} */
				
				model.addAttribute("student", student);
				return "firstgrade";
	  
				
			} else if(academic==5 || academic==6 || academic==7 || academic==8 || academic==9) {
				
				/*
				if(term !=null) {
					model.addAttribute("term", term);
						return "getfifthgrade";
				}else if(term==null) {
						return "fifthgrade";
				}
				*/
				model.addAttribute("student", student);
				return "fifthgrade";
				
			} else if(academic == 4) {
				
			/*	if(term !=null) {
					model.addAttribute("term", term);
					
					return "getforthgrade"; 
				} else if(term == null) {
					return "forthgrade";
				}*/
				
				model.addAttribute("student", student);
				return "forthgrade";
				
			}
				
			return "studentsAcademicClasses";
					
		}
		
		@GetMapping("/finaltermbyacademic/{id}")
		public String finalTermByAcademic(@PathVariable("id") int id, Model model, HttpSession session) {
			
			Student student= service.findStudent(id);
			session.setAttribute("student_id", id);
			Academic stuAcademic = new Academic();
			int academic = stuAcademic.getClassCode(student.getAcademic());
			FinalTerm term = student.getFinalterm();
			model.addAttribute("student", student);
			
			if(academic==1 || academic==2 || academic==3) {
				
				 /*if(term==null)
				return "finalgrade";
							
				else if(term!=null)
					return "getfinalgrade";		
				*/
				model.addAttribute("student", student);
				return "finalgrade";
				
			}else if(academic==4) {
			/*	if(term==null)
				return "forthgradefinal";
				else if(term!=null) {
					
						return "getforthgradefinal";					
				}
				*/
				model.addAttribute("student", student);
				return "forthgradefinal";
			
			}else if(academic==5 || academic==6 || academic==7 || academic==8 || academic==9) {
				
			/*	if(term==null)
				return "fifthgradefinalresult";
				else if(term!=null)
					return "getfifthgradefinalform";     
				*/
				model.addAttribute("student", student);
				return "fifthgradefinalresult";
			}
			
			
			return "studentsAcademicClasses";
		}
		
		
		@GetMapping("/submitupdatedfinalfirstgrade")
		public String submitUpdatedFirstGrade(@RequestParam("id") int id,@RequestParam("math") float math, @RequestParam("arabic") float arabic, @RequestParam("islamic") float islamic, HttpSession session, Model model) {                     
			
			
			
			Student student = service.findStudent(id);
			
			// new section
			
			String aca = student.getAcademic();
			Academic academic = new Academic();
			int acyear= academic.getClassCode(aca);
			FirstTerm firsterm = student.getFirstterm(); 
			int year = Calendar.getInstance().get(Calendar.YEAR);
			
			String acdyear = (year-2)+"/"+(year-1);
			
			if(firsterm!=null) {
				
				FirstTermResult firsttermresult= new FirstTermResult(firsterm.getMath(), firsterm.getArabic(), firsterm.getIslamic(), firsterm.getResult(), firsterm.getGrade(), acdyear);
				firstTermResultService.saveFirstTermResult(firsttermresult);
								
				saveFirstTermInfo(acyear, student, firsttermresult);
				
				student.setFirstterm(null);
				firstTermService.delateFirstTerm(firsterm);
				
				
			}
			
			// new section
			
			FinalTerm term = student.getFinalterm();
			term.setArabic(arabic);
			term.setMath(math);
			term.setIslamic(islamic);
			float result = Math.round((arabic+islamic+math)/3);
			term.setResult(result);
			
			if(result >=50 && result < 65)
				term.setGrade("مقبول");
			else if(result >=65 && result <75 )
				term.setGrade("جيد");
			else if(result >=75 && result <85)
				term.setGrade("جيد جدا");
			else if(result >=85)
				term.setGrade("ممتاز");
			else
				term.setGrade("راسب");
														
			if(term.getArabic()>=50 && term.getIslamic()>=50 && term.getMath()>=50)
			{
				String curyear= student.getAcademic();
				Academic ac = new Academic();
				int next= ac.getClassCode(curyear);
				next +=1;
				String nextYear= ac.getYear(next);
				student.setAcademic(nextYear);
				service.saveStudent(student);	
				
			}else {
				term.setGrade("راسب");
			}
			
			finalTermService.saveFianlTerm(term);
			
			List<Student> list = (List<Student>) session.getAttribute("list");
			model.addAttribute("students", list);
			
			
			return "studentsacademiclist";
												
		}
		
		@GetMapping("/submitfifthgradefinal")
		public String submitFifthGradeFinal(Model model, HttpSession session, @RequestParam("id") int id, @RequestParam("math") float math, @RequestParam("arabic") float arabic, @RequestParam("islamic") float islamic, @RequestParam("national") float national, @RequestParam("history") float history, @RequestParam("science") float science, @RequestParam("computer") float computer, @RequestParam("english") float english) {
			
			Student student = service.findStudent(id);
			
			// new section
			
			String aca = student.getAcademic();
			Academic academic = new Academic();
			int acyear= academic.getClassCode(aca);
			FirstTerm firsterm = student.getFirstterm(); 
			
			if(firsterm!=null) {
				
				int year = Calendar.getInstance().get(Calendar.YEAR);
				
				String acdyear = (year-2)+"/"+(year-1);
				FirstTermResult firsttermresult= new FirstTermResult(firsterm.getMath(), firsterm.getArabic(), firsterm.getIslamic(), firsterm.getNational(), firsterm.getHistory(), firsterm.getScience(), firsterm.getEnglish(), firsterm.getComputer(),  firsterm.getResult(), firsterm.getGrade(), acdyear);
				firstTermResultService.saveFirstTermResult(firsttermresult);

				saveFirstTermInfo(acyear, student, firsttermresult);
				
				student.setFirstterm(null);
				firstTermService.delateFirstTerm(firsterm);

			}
			
			// new section
			
			
			FinalTerm term = student.getFinalterm();
			term.setArabic(arabic);
			term.setComputer(computer);
			term.setEnglish(english);
			term.setHistory(history);
			term.setIslamic(islamic);
			term.setMath(math);
			term.setNational(national);
			term.setScience(science);
			
			float result = Math.round((arabic+computer+english+history+islamic+math+national+science)/8);
			term.setResult(result);
			
			if(result >=50 && result < 65)
				term.setGrade("مقبول");
			else if(result >=65 && result <75 )
				term.setGrade("جيد");
			else if(result >=75 && result <85)
				term.setGrade("جيد جدا");
			else if(result >=85)
				term.setGrade("ممتاز");
			else
				term.setGrade("راسب");
			
			if(term.getComputer()>=50 && term.getEnglish()>=50 && term.getHistory()>=50 && term.getIslamic()>=50 && term.getMath()>=50 && term.getNational()>=50 && term.getNational()>=50 && term.getScience()>=50)
			{
			String year= student.getAcademic();
			Academic ac = new Academic();
			int next= ac.getClassCode(year);
			next +=1;
			String nextYear= ac.getYear(next);
			student.setAcademic(nextYear);
			service.saveStudent(student);
			}else {
				term.setGrade("راسب");
			}
			
			finalTermService.saveFianlTerm(term);
			List<Student> list = (List<Student>) session.getAttribute("list");
			model.addAttribute("students", list);
			
			int acadyear = academic.getClassCode(student.getAcademic());
			if(acadyear==10) {
				acadyear -= 1;
				int year = Calendar.getInstance().get(Calendar.YEAR);
				String acdyear = (year-1)+"/"+(year);			
				FinalTermResult finaltermresult = new FinalTermResult(term.getMath(), term.getArabic(), term.getIslamic(), term.getNational(), term.getHistory(), term.getScience(), term.getEnglish(), term.getComputer(), term.getResult(), term.getGrade(), acdyear);
				finalTermResultService.saveFinalTermResult(finaltermresult);
				saveFinalTermInfo(acadyear, student, finaltermresult);
			}
			
			
			
			return "studentsacademiclist";
			
		}
	
		
		@GetMapping("/submitforthgradefinalupdated")
		public String submitForthGradeFinalUpadted(Model model, HttpSession session, @RequestParam("id") int id, @RequestParam("math") float math, @RequestParam("arabic") float arabic, @RequestParam("islamic") float islamic, @RequestParam("national") float national, @RequestParam("history") float history, @RequestParam("science") float science) {
			
			Student student = service.findStudent(id);
			
			// new section
			
			String aca = student.getAcademic();
			Academic academic = new Academic();
			int acyear= academic.getClassCode(aca);
			FirstTerm firsterm = student.getFirstterm(); 
			
			if(firsterm!=null) {
				int year = Calendar.getInstance().get(Calendar.YEAR);
				
				String acdyear = (year-2)+"/"+(year-1);
				FirstTermResult firsttermresult= new FirstTermResult(firsterm.getMath(), firsterm.getArabic(), firsterm.getIslamic(), firsterm.getNational(), firsterm.getHistory(), firsterm.getScience(), firsterm.getResult(), firsterm.getGrade(), acdyear);
				firstTermResultService.saveFirstTermResult(firsttermresult);
				saveFirstTermInfo(acyear, student, firsttermresult);
				
				student.setFirstterm(null);
				firstTermService.delateFirstTerm(firsterm);
							
			}
			
			// new section
			
			FinalTerm term = student.getFinalterm();
			term.setArabic(arabic);
			term.setHistory(history);
			term.setIslamic(islamic);
			term.setMath(math);
			term.setNational(national);
			term.setScience(science);
			
			float result = Math.round((arabic+history+islamic+math+national+science)/6);
			term.setResult(result);
			
			if(result >=50 && result < 65)
				term.setGrade("مقبول");
			else if(result >=65 && result <75 )
				term.setGrade("جيد");
			else if(result >=75 && result <85)
				term.setGrade("جيد جدا");
			else if(result >=85)
				term.setGrade("ممتاز");
			else
				term.setGrade("راسب");

			if(term.getArabic()>=50 && term.getHistory()>=50 && term.getIslamic()>=50 && term.getMath()>=50 && term.getNational()>=50 && term.getScience()>50)
			{
				String year= student.getAcademic();
				Academic ac = new Academic();
				int next= ac.getClassCode(year);
				next +=1;
				String nextYear= ac.getYear(next);
				student.setAcademic(nextYear);
				service.saveStudent(student);		
				
			}else {
				term.setGrade("راسب");
			}
			
			
			finalTermService.saveFianlTerm(term);
			List<Student> list = (List<Student>) session.getAttribute("list");
			model.addAttribute("students", list);
			
			
		/*	model.addAttribute("students", list);
			if(term.getResult()>=50 && term.getArabic()>=50 && term.getHistory()>=50 && term.getIslamic()>=50 && term.getMath()>=50 && term.getNational()>=50 && term.getScience()>=50)
			{	
			String year= student.getAcademic();
			Academic ac = new Academic();
			int next= ac.getClassCode(year);
			next +=1;
			String nextYear= ac.getYear(next);
			student.setAcademic(nextYear);
			service.saveStudent(student);
			}else {
				term.setRepeated(true);
			}
			*/
			return "studentsacademiclist";
			
		}
}
