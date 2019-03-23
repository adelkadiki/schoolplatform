package com.application.system.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Student {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	private String name;


	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "student")
	private FirstTerm firstterm;
	
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "student")
	private FinalTerm finalterm;
	
	@ManyToOne
	private User user;
	
	
	private String academic;
	
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "student")
	private FirstClass firstclass;
	
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "student")
	private SecondClass secondclass;
	
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "student")
	private ThirdClass thirdclass;
	
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "student")
	private FourthClass fourthclass;
	
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "student")
	private FifthClass fifthclass;
	
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "student")
	private SixthClass sixthclass;
	
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "student")
	private SeventhClass seventhclass;
	
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "student")
	private EighthClass eighthclass;
	
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "student")
	private NinthClass ninthclass;
	
	
	

	public NinthClass getNinthclass() {
		return ninthclass;
	}


	public void setNinthclass(NinthClass ninthclass) {
		this.ninthclass = ninthclass;
	}


	public FirstClass getFirstclass() {
		return firstclass;
	}


	public void setFirstclass(FirstClass firstclass) {
		this.firstclass = firstclass;
	}


	public SecondClass getSecondclass() {
		return secondclass;
	}


	public void setSecondclass(SecondClass secondclass) {
		this.secondclass = secondclass;
	}


	public ThirdClass getThirdclass() {
		return thirdclass;
	}


	public void setThirdclass(ThirdClass thirdclass) {
		this.thirdclass = thirdclass;
	}


	public FourthClass getFourthclass() {
		return fourthclass;
	}


	public void setFourthclass(FourthClass fourthclass) {
		this.fourthclass = fourthclass;
	}


	public FifthClass getFifthclass() {
		return fifthclass;
	}


	public void setFifthclass(FifthClass fifthclass) {
		this.fifthclass = fifthclass;
	}


	public SixthClass getSixthclass() {
		return sixthclass;
	}


	public void setSixthclass(SixthClass sixthclass) {
		this.sixthclass = sixthclass;
	}


	public SeventhClass getSeventhclass() {
		return seventhclass;
	}


	public void setSeventhclass(SeventhClass seventhclass) {
		this.seventhclass = seventhclass;
	}


	public EighthClass getEighthclass() {
		return eighthclass;
	}


	public void setEighthclass(EighthClass eighthclass) {
		this.eighthclass = eighthclass;
	}




	public Student(String name, String academic) {
		
		this.name = name;
		this.academic = academic;
	}


	public String getAcademic() {
		return academic;
	}


	public void setAcademic(String academic) {
		this.academic = academic;
	}


	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}


	public FirstTerm getFirstterm() {
		return firstterm;
	}


	public void setFirstterm(FirstTerm firstterm) {
		this.firstterm = firstterm;
	}


	public FinalTerm getFinalterm() {
		return finalterm;
	}


	public void setFinalterm(FinalTerm finalterm) {
		this.finalterm = finalterm;
	}


	public int getId() {
		return id;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	
	public Student() {}


	
	
	
	
	
}
