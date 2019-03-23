package com.application.system.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;







public class Academic {

	  
	
	
	Map<Integer, String> map = new HashMap<>();
	
	
	public Academic() {}
	
	public String getYear(int y) {

		Map<Integer, String> list = new HashMap<>();
		
		list.put(1, "الصف الأول الإبتدائى");
		list.put(2, "الصف الثانى الإبتدائى");
		list.put(3, "الصف الثالث الإبتدائى");
		list.put(4, "الصف الرابع الإبتدائى");
		list.put(5, "الصف الخامس الإبتدائى");
		list.put(6, "الصف السادس الإبتدائى");
		list.put(7, "الصف السابع");
		list.put(8, "الصف الثامن");
		list.put(9, "الصف التاسع");
		list.put(10, "خريج");
		
		return list.get(y);
	}
	
	public int getClassCode(String year) {
		
		Map<String, Integer> list = new HashMap<>();
		
		list.put("الصف الأول الإبتدائى", 1);
		list.put("الصف الثانى الإبتدائى", 2);
		list.put("الصف الثالث الإبتدائى", 3);
		list.put("الصف الرابع الإبتدائى", 4);
		list.put("الصف الخامس الإبتدائى", 5);
		list.put("الصف السادس الإبتدائى", 6);
		list.put("الصف السابع", 7);
		list.put("الصف الثامن", 8);
		list.put("الصف التاسع", 9);
		list.put("خريج", 10);
		
		return list.get(year);
	}
	
	public Map<Integer, String> getIinfo(){
		
		Map<Integer, String> list = new HashMap<>();
		
		list.put(1, "الصف الأول الإبتدائى");
		list.put(2, "الصف الثانى الإبتدائى");
		list.put(3, "الصف الثالث الإبتدائى");
		list.put(4, "الصف الرابع الإبتدائى");
		list.put(5, "الصف الخامس الإبتدائى");
		list.put(6, "الصف السادس الإبتدائى");
		list.put(7, "الصف السابع");
		list.put(8, "الصف الثامن");
		list.put(9, "الصف التاسع");
		list.put(10, "خريج");
		
		return list;
		
	}
	
	public List<String> getAcademicYears(){
	
		List<String> list = new ArrayList<>();
		list.add("First Grade");
		list.add("Second Grade");
		list.add("Third Grade");
		list.add("Forth Grade");
		list.add("Fifth Grade");
		list.add("Sixth Grade");
		list.add("Seventh Grade");
		
		return list;
		
	}
	
	public List<String> getLang(){
	
		List<String> lan = new ArrayList<>();
		
		lan.add("الصف الأول الإبتدائى");
		lan.add("الصف الثانى الإبتدائى");
		lan.add("الصف الثالث الإبتدائى");
		lan.add("الصف الرابع الإبتدائى");
		lan.add("الصف الخامس الإبتدائى");
		lan.add("الصف السادس الإبتدائى");
		lan.add("الصف السابع");
		lan.add("الصف الثامن");
		lan.add("الصف التاسع");
		lan.add("خريج");
		
		return lan;
	}
	
	



	
	
}
