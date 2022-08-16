package com.shaharia.SpringBatch1_CSV_to_MySQL.Model;

public class User {

	private int id;
	private String name;
	private String sex;
	private double salary;
	
	
	public User() {
		
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public double getSalary() {
		return salary;
	}
	public void setSalary(double salary) {
		this.salary = salary;
	}
	public User(int id, String name, String sex, double salary) {
		super();
		this.id = id;
		this.name = name;
		this.sex = sex;
		this.salary = salary;
	}
	
	 
	
}
