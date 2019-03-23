package com.application.system.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class SixthClass {

	@Id
	@GeneratedValue
	private int id;
	
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	private FirstTermResult firsterm;
	
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	private FinalTermResult finalterm;
	
	@OneToOne
	private Student student;

	private final String academic = "الصف السادس الإبتدائي";
	
	@OneToOne
	private Graduate graduate;
	
	
	
	
	public Graduate getGraduate() {
		return graduate;
	}

	public void setGraduate(Graduate graduate) {
		this.graduate = graduate;
	}

	public String getAcademic() {
		return academic;
	}

	public FirstTermResult getFirsterm() {
		return firsterm;
	}

	public void setFirsterm(FirstTermResult firsterm) {
		this.firsterm = firsterm;
	}

	public FinalTermResult getFinalterm() {
		return finalterm;
	}

	public void setFinalterm(FinalTermResult finalterm) {
		this.finalterm = finalterm;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public SixthClass(FirstTermResult firsterm, FinalTermResult finalterm) {
	
		this.firsterm = firsterm;
		this.finalterm = finalterm;
	}
	
	public SixthClass() {}
}
