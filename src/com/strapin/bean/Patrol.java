package com.strapin.bean;

public class Patrol {
	public String patrolerId;
	public String patrolerFirstName;
	public String patrolerLastName;
	
	public Patrol(String patrolerid,String firstname,String lastName){
		this.patrolerId  = patrolerid;
		this.patrolerFirstName = firstname;
		this.patrolerLastName = lastName;
	}

	public String getPatrolerId() {
		return patrolerId;
	}

	public void setPatrolerId(String patrolerId) {
		this.patrolerId = patrolerId;
	}

	public String getPatrolerFirstName() {
		return patrolerFirstName;
	}

	public void setPatrolerFirstName(String patrolerFirstName) {
		this.patrolerFirstName = patrolerFirstName;
	}

	public String getPatrolerLastName() {
		return patrolerLastName;
	}

	public void setPatrolerLastName(String patrolerLastName) {
		this.patrolerLastName = patrolerLastName;
	}
	
	
}
