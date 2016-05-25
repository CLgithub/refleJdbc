package org.orm.core;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.orm.core.annotion.Column;
import org.orm.core.annotion.Id;
import org.orm.core.annotion.Table;
import org.orm.core.jdbc.JdbcWapper;

public class Session extends JdbcWapper{

	private Session(Connection connection) {
		super(connection);
	}

	/**
	 * save the record
	 * @param obj
	 * @return
	 * @throws Exception if the Table annotation is not assigned
	 */
	public boolean save(Object obj) throws Exception {
		StringBuilder SqlBuilder = null;
		List<Object> values = null;
		Class<?> clazz = obj.getClass();
		Table table = clazz.getAnnotation(Table.class);
		if (table != null) {
			SqlBuilder = new StringBuilder();
			SqlBuilder.append("insert into ");
			String tableName = table.tableName();
			SqlBuilder.append(tableName + "(");
			Field[] fields = clazz.getDeclaredFields();
			if (fields != null) {
				values = new ArrayList<Object>();
				for (Field field : fields) {
					field.setAccessible(true);

					Id id = field.getAnnotation(Id.class);
					// 是主键
					if (id != null) {
						if (!id.isAutoIncreament()) {
							SqlBuilder.append(id.column() + ",");
							// 添加值
							values.add(field.get(obj));
						}
					} else {
						Column column = field.getAnnotation(Column.class);
						SqlBuilder.append(column.column() + ",");
						// 添加值
						values.add(field.get(obj));
					}
				}
			}
			SqlBuilder.deleteCharAt(SqlBuilder.length() - 1).append(")");
			SqlBuilder.append(" values(");
			for (int i = 0; i < values.size(); i++) {
				SqlBuilder.append("?,");
			}
			SqlBuilder.deleteCharAt(SqlBuilder.length() - 1).append(")");
			return this.executeSql(SqlBuilder.toString(), values);
		} else {
			throw new Exception("you must assign the Table annotation");
		}
	}

	/**
	 * delete the record
	 * 
	 * @param obj
	 * @return true operation is success
	 * @throws Exception if the primary key not assign
	 */
	public boolean delete(Object obj) throws Exception {
		StringBuilder SqlBuilder = null;
		List<Object> values = null;
		Class<?> clazz = obj.getClass();
		Table table = clazz.getAnnotation(Table.class);
		if (table != null) {
			SqlBuilder = new StringBuilder();
			SqlBuilder.append("delete from ");
			String tableName = table.tableName();
			SqlBuilder.append(tableName);
			Field[] fields = clazz.getDeclaredFields();
			if (fields != null) {
				values = new ArrayList<Object>();
				for (Field field : fields) {
					field.setAccessible(true);

					// 主键
					Id id = field.getAnnotation(Id.class);
					if (id != null) {
						Object idValue = field.get(obj);
						if (idValue != null && !isInitValue(idValue)) {
							SqlBuilder.append(" where " + id.column() + "=?");
							values.add(idValue);
							break;
						} else {
							throw new Exception("you must assign a value of the primary key");
						}
					}
				}
			}
			return this.executeSql(SqlBuilder.toString(), values);
		} else {
			throw new Exception("you must assign the Table annotation");
		}
	}

	/**
	 * update record
	 * @param obj
	 * @return
	 * @throws Exception if the primary key is not assigned
	 */
	public boolean update(Object obj) throws Exception {
		StringBuilder SqlBuilder = null;
		List<Object> values = null;
		Class<?> clazz = obj.getClass();
		Table table = clazz.getAnnotation(Table.class);
		if (table != null) {
			SqlBuilder = new StringBuilder();
			SqlBuilder.append("update ");
			String tableName = table.tableName();
			SqlBuilder.append(tableName);
			Field[] fields = clazz.getDeclaredFields();
			if (fields != null) {
				SqlBuilder.append(" set ");
				values = new ArrayList<Object>();
				//设置非主键属性
				for (Field field : fields) {
					field.setAccessible(true);
					
					Column column=field.getAnnotation(Column.class);
					if(column!=null){
						Object value=field.get(obj);
						if(value!=null&&!isInitValue(value)){
							SqlBuilder.append(column.column()+"=?,");
							values.add(value);
						}
					}
				}
				//删除最后一个","号
				SqlBuilder.deleteCharAt(SqlBuilder.length()-1);
				//设置主键
				for (Field field : fields) {
					field.setAccessible(true);

					// 主键
					Id id = field.getAnnotation(Id.class);
					if (id != null) {
						Object idValue = field.get(obj);
						if (idValue != null && !isInitValue(idValue)) {
							SqlBuilder.append(" where " + id.column() + "=?");
							values.add(idValue);
							break;
						} else {
							throw new Exception("you must assign a value of the primary key");
						}
					}
				}
			}
			return this.executeSql(SqlBuilder.toString(), values);
		} else {
			throw new Exception("you must assign the Table annotation");
		}
	}
	
	public <T> T get(Object id,Class<T> clazz) throws Exception{
		if(id!=null){
			StringBuilder SqlBuilder=null;
			List<Object> values=null;
			Table table = clazz.getAnnotation(Table.class);
			if(table!=null){
				SqlBuilder=new StringBuilder();
				SqlBuilder.append("select * from ");
				SqlBuilder.append(table.tableName());
				Field[] fields = clazz.getDeclaredFields();
				if (fields != null) {
					values = new ArrayList<Object>();
					for (Field field : fields) {
						field.setAccessible(true);

						// 主键
						Id idAno = field.getAnnotation(Id.class);
						if (id != null) {
							SqlBuilder.append(" where " + idAno.column() + "=?");
							values.add(id);
							break;
						}
					}
				}
				this.resultSet=this.executeQuery(SqlBuilder.toString(), values);
				return convertValues(resultSet,clazz).get(0);
			}else{
				throw new Exception("you must assign the Table annotation");
			}
		}else{
			throw new Exception("you must assign a value of the primary key");
		}
	}
	
	//表值映射
	private <T> List<T> convertValues(ResultSet resultSet,Class<T> clazz) throws Exception{
		if(resultSet!=null){
			List<T> result=new ArrayList<T>();
			T t;
			while(resultSet.next()){
				t=clazz.newInstance();
				Field[] fields = clazz.getDeclaredFields();
				for(Field field:fields){
					field.setAccessible(true);
					Id id = field.getAnnotation(Id.class);
					if(id!=null){
						//设置id的值 
						Object value = resultSet.getObject(id.column());
						field.set(t, value);
					}else{
						Column column=field.getAnnotation(Column.class);
						//普通属性值
						if(column!=null){
							Object value = resultSet.getObject(column.column());
							field.set(t, value);
						}
					}
				}
				result.add(t);
				t=null;
			}
			return result;
		}
		return null;
	}

	// 判断是否是初值
	private boolean isInitValue(Object obj) {
		if (obj instanceof Integer) {
			Integer value = (Integer) obj;
			if (value == 0) {
				return true;
			}
		}
		if(obj instanceof Long){
			Long value=(Long)obj;
			if(value==0){
				return true;
			}
		}
		return false;
	}

}
