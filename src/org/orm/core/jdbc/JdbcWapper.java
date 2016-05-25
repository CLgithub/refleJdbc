package org.orm.core.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public abstract class JdbcWapper {

	protected Connection connection;
	protected PreparedStatement statement;
	protected ResultSet resultSet;

	public JdbcWapper(Connection connection) {
		this.connection = connection;
	}

	public boolean executeSql(String sql, List<?> values) throws Exception {
		try {
			statement = this.connection.prepareStatement(sql);
			if (values != null && values.size() > 0) {
				for (int i = 0; i < values.size(); i++) {
					statement.setObject(i + 1, values.get(i));
				}
			}
			return statement.execute();
		} catch (Exception e) {
			throw e;
		}
	}
	
	public ResultSet executeQuery(String sql,List<?> values) throws Exception{
		try {
			statement = this.connection.prepareStatement(sql);
			if (values != null && values.size() > 0) {
				for (int i = 0; i < values.size(); i++) {
					statement.setObject(i + 1, values.get(i));
				}
			}
			return statement.executeQuery();
		} catch (Exception e) {
			throw e;
		}
	}

	public void close() throws SQLException {
		if (resultSet != null) {
			resultSet.close();
		}
		if (statement != null) {
			statement.close();
		}
		if (connection != null) {
			connection.close();
		}
	}

}
