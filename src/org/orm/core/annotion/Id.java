package org.orm.core.annotion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.orm.core.jdbc.JdbcDataType;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Id {

	public String column();
	
	public JdbcDataType dataType() default JdbcDataType.J_INT;
	
	public boolean isAutoIncreament() default false;
	
}
