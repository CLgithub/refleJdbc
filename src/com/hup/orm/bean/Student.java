package com.hup.orm.bean;

import org.orm.core.annotion.Column;
import org.orm.core.annotion.Id;
import org.orm.core.annotion.Table;
import org.orm.core.jdbc.JdbcDataType;

@Table(tableName = "student")
public class Student {

	@Id(column = "stuNum", dataType = JdbcDataType.J_STRING)
	private String stuNum;

	@Column(column = "name", dataType = JdbcDataType.J_STRING)
	private String name;

	@Column(column = "age", dataType = JdbcDataType.J_STRING)
	private int age;

	public Student() {
	}

	public Student(String stuNum, String name, int age) {
		this.stuNum = stuNum;
		this.name = name;
		this.age = age;
	}

	public String getStuNum() {
		return stuNum;
	}

	public void setStuNum(String stuNum) {
		this.stuNum = stuNum;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

}
