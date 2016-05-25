package com.hup.orm.test;

import org.orm.core.Session;
import org.orm.core.SessionFactory;

import com.hup.orm.bean.Student;

public class SessionTest {

	public static void main(String[] args) {
		try {
			Session session = SessionFactory.openSession();
			Student student = new Student("1002", "ÀîËÄ", 10);
			session.save(student);
			/*Student student=new Student();
			student.setStuNum("1002");
			session.delete(student);*/
			/*Student student = session.get("1002", Student.class);
			System.out.println(student.getName());*/
//			Person person = session.get(2, Person.class);
//			System.out.println(person);
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
