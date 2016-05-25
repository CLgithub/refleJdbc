package com.hup.orm.bean;

import java.util.Date;

import org.orm.core.annotion.Column;
import org.orm.core.annotion.Id;
import org.orm.core.annotion.Table;
import org.orm.core.jdbc.JdbcDataType;

@Table(tableName = "persons")
public class Person {

	@Id(column = "id", isAutoIncreament = true)
	private int id;

	@Column(column = "userName",dataType=JdbcDataType.J_STRING)
	private String name;

	@Column(column = "address",dataType=JdbcDataType.J_STRING)
	private String address;

	@Column(column = "age",dataType=JdbcDataType.J_INT)
	private int age;

	@Column(column = "birthday",dataType=JdbcDataType.J_DATETIME)
	private Date birthday;

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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

}
