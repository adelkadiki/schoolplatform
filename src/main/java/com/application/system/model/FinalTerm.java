package com.application.system.model;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Entity;

@Entity
public class FinalTerm {

	@Id
	@GeneratedValue
	private int id;
	
	private float math;
	private float arabic;
	private float islamic;
	private float national;
	private float history;
	private float science;
	private float english;
	private float computer;
	private float result;
	private String grade;   
	
	
	@OneToOne
	private Student student;
	
	@OneToOne
	private Graduate graduate;
	
	

	public Graduate getGraduate() {
		return graduate;
	}




	public void setGraduate(Graduate graduate) {
		this.graduate = graduate;
	}




	public Student getStudent() {
		return student;
	}




	public void setStudent(Student student) {
		this.student = student;
	}




	public String getGrade() {
		return grade;
	}




	public void setGrade(String grade) {
		this.grade = grade;
	}




	public FinalTerm() {}
	
	
	
	
	public FinalTerm(float math, float arabic, float islamic) {
	
		this.math = math;
		this.arabic = arabic;
		this.islamic = islamic;
	}


	


	public FinalTerm(float math, float arabic, float islamic, float national, float history, float science) {
		
		this.math = math;
		this.arabic = arabic;
		this.islamic = islamic;
		this.national = national;
		this.history = history;
		this.science = science;
	}


	

	public FinalTerm(float math, float arabic, float islamic, float national, float history, float science,
			float english, float computer) {
	
		this.math = math;
		this.arabic = arabic;
		this.islamic = islamic;
		this.national = national;
		this.history = history;
		this.science = science;
		this.english = english;
		this.computer = computer;
	}




	public float getResult() {
		return result;
	}



	public void setResult(float result) {
		this.result = result;
	}



	public int getId() {
		return id;
	}
	
	public float getMath() {
		return math;
	}
	public void setMath(float math) {
		this.math = math;
	}
	public float getArabic() {
		return arabic;
	}
	public void setArabic(float arabic) {
		this.arabic = arabic;
	}
	public float getIslamic() {
		return islamic;
	}
	public void setIslamic(float islamic) {
		this.islamic = islamic;
	}
	public float getNational() {
		return national;
	}
	public void setNational(float national) {
		this.national = national;
	}
	public float getHistory() {
		return history;
	}
	public void setHistory(float history) {
		this.history = history;
	}
	public float getScience() {
		return science;
	}
	public void setScience(float science) {
		this.science = science;
	}
	public float getEnglish() {
		return english;
	}
	public void setEnglish(float english) {
		this.english = english;
	}
	public float getComputer() {
		return computer;
	}
	public void setComputer(float computer) {
		this.computer = computer;
	}

	
	
}
