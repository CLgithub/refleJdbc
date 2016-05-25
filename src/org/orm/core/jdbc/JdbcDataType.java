package org.orm.core.jdbc;

public enum JdbcDataType {

	J_INT("int"), J_BOOLEAN("bitint"),J_STRING("varchar"),J_DATETIME("datatime");

	private String value;

	private JdbcDataType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
