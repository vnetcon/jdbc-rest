package com.vnetcon.jdbc.rest;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;

public class RestResultSetMetaData implements ResultSetMetaData {

	public <T> T unwrap(Class<T> iface) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public int getColumnCount() throws SQLException {
		return 1;
	}

	public boolean isAutoIncrement(int column) throws SQLException {
		return false;
	}

	public boolean isCaseSensitive(int column) throws SQLException {
		return true;
	}

	public boolean isSearchable(int column) throws SQLException {
		return false;
	}

	public boolean isCurrency(int column) throws SQLException {
		return false;
	}

	public int isNullable(int column) throws SQLException {
		return columnNoNulls;
	}

	public boolean isSigned(int column) throws SQLException {
		return false;
	}

	public int getColumnDisplaySize(int column) throws SQLException {
		return 100;
	}

	public String getColumnLabel(int column) throws SQLException {
		return RestDriver.restColumnName;
	}

	public String getColumnName(int column) throws SQLException {
		return RestDriver.restColumnName;
	}

	public String getSchemaName(int column) throws SQLException {
		return RestDriver.restSchema;
	}

	public int getPrecision(int column) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public int getScale(int column) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public String getTableName(int column) throws SQLException {
		return RestDriver.restTable;
	}

	public String getCatalogName(int column) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public int getColumnType(int column) throws SQLException {
		return RestDriver.restColType;
	}

	public String getColumnTypeName(int column) throws SQLException {
		return RestDriver.restColTypeName;
	}

	public boolean isReadOnly(int column) throws SQLException {
		return true;
	}

	public boolean isWritable(int column) throws SQLException {
		return false;
	}

	public boolean isDefinitelyWritable(int column) throws SQLException {
		return false;
	}

	public String getColumnClassName(int column) throws SQLException {
		return RestDriver.restColClassName;
	}

}
