package com.application.system.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


@Entity
public class FinalTermResult {

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
	private String acYear;
	
	
	
	
	

	public String getAcYear() {
		return acYear;
	}

	public void setAcYear(String acYear) {
		this.acYear = acYear;
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

	public float getResult() {
		return result;
	}

	public void setResult(float result) {
		this.result = result;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	

	public FinalTermResult(float math, float arabic, float islamic, float result, String grade, String acyear) {

		this.math = math;
		this.arabic = arabic;
		this.islamic = islamic;
		this.result = result;
		this.grade = grade;
		this.acYear = acyear;
	}
	
	

	public FinalTermResult(float math, float arabic, float islamic, float national, float history, float science,
			float result, String grade, String acYear) {
		
		this.math = math;
		this.arabic = arabic;
		this.islamic = islamic;
		this.national = national;
		this.history = history;
		this.science = science;
		this.result = result;
		this.grade = grade;
		this.acYear = acYear;
	}

	public FinalTermResult(float math, float arabic, float islamic, float national, float history, float science,
			float english, float computer, float result, String grade, String acYear) {
	
		this.math = math;
		this.arabic = arabic;
		this.islamic = islamic;
		this.national = national;
		this.history = history;
		this.science = science;
		this.english = english;
		this.computer = computer;
		this.result = result;
		this.grade = grade;
		this.acYear = acYear;
	}
	
	
	


	public FinalTermResult() {}
}

