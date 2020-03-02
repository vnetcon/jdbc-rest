package com.vnetcon.jdbc.rest;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class RestPreparedStatement implements PreparedStatement {

	private RestConnection restCon = null;
	private PreparedStatement realStmt = null;
	private String sql = null;
	private Map<String, String> jsonParams = null;
	private Map<String, String> queryParams = null;
	
	
	public RestPreparedStatement(RestConnection con, PreparedStatement stmt, String sql, Map<String, String> jsonParams, Map<String,String> queryParams) throws SQLException {
		String psql = sql;
		this.restCon = con;
		this.realStmt = stmt;
		this.sql = sql;
		this.jsonParams = jsonParams;
		this.queryParams = queryParams;

		
		if(this.jsonParams == null) {
			this.jsonParams = RestDriver.getJsonParams(sql);
		}
				
		Set<String> keys = jsonParams.keySet();
		Iterator<String> iter = keys.iterator();
		while(iter.hasNext()) {
			String key = iter.next();
			String value = jsonParams.get(key);
			
			if(key.toLowerCase().startsWith("r_") && this.isParamsFromWebContaier()) {
				String s = key;
				s = s.replaceFirst("r_", "");
				s = s.replaceFirst("R_", "");
				if(this.queryParams != null) {
					value = queryParams.get(s);
				}
			}
			
			String safeValue = RestDriver.getSafeSql(value);
			psql = psql.replace("'{" + key + "}'", safeValue);
		}
		this.sql = psql;
		
	}
	
	public boolean isParamsFromWebContaier() {
		return restCon.isParamsFromWebContaier();
	}
	
	
	public ResultSet executeQuery(String sql) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public int executeUpdate(String sql) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void close() throws SQLException {
		realStmt.close();
	}

	public int getMaxFieldSize() throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void setMaxFieldSize(int max) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public int getMaxRows() throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void setMaxRows(int max) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void setEscapeProcessing(boolean enable) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public int getQueryTimeout() throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void setQueryTimeout(int seconds) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void cancel() throws SQLException {
		throw new UnsupportedOperationException();
	}

	public SQLWarning getWarnings() throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void clearWarnings() throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void setCursorName(String name) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public boolean execute(String sql) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public ResultSet getResultSet() throws SQLException {
		throw new UnsupportedOperationException();
	}

	public int getUpdateCount() throws SQLException {
		throw new UnsupportedOperationException();
	}

	public boolean getMoreResults() throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void setFetchDirection(int direction) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public int getFetchDirection() throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void setFetchSize(int rows) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public int getFetchSize() throws SQLException {
		throw new UnsupportedOperationException();
	}

	public int getResultSetConcurrency() throws SQLException {
		throw new UnsupportedOperationException();
	}

	public int getResultSetType() throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void addBatch(String sql) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void clearBatch() throws SQLException {
		throw new UnsupportedOperationException();
	}

	public int[] executeBatch() throws SQLException {
		throw new UnsupportedOperationException();
	}

	public Connection getConnection() throws SQLException {
		throw new UnsupportedOperationException();
	}

	public boolean getMoreResults(int current) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public ResultSet getGeneratedKeys() throws SQLException {
		throw new UnsupportedOperationException();
	}

	public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public int executeUpdate(String sql, String[] columnNames) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public boolean execute(String sql, int[] columnIndexes) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public boolean execute(String sql, String[] columnNames) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public int getResultSetHoldability() throws SQLException {
		throw new UnsupportedOperationException();
	}

	public boolean isClosed() throws SQLException {
		return realStmt.isClosed();
	}

	public void setPoolable(boolean poolable) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public boolean isPoolable() throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void closeOnCompletion() throws SQLException {
		throw new UnsupportedOperationException();
	}

	public boolean isCloseOnCompletion() throws SQLException {
		throw new UnsupportedOperationException();
	}

	public <T> T unwrap(Class<T> iface) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public ResultSet executeQuery() throws SQLException {
		if(sql != null && sql.trim().toLowerCase().indexOf(RestDriver.jsonPrefix) > 0) {
			ResultSet rs = realStmt.executeQuery();
			RestResultSet rrs = new RestResultSet((RestConnection)restCon, this, rs, sql, jsonParams, queryParams);
			return rrs;
		}else {
			ResultSet rs = realStmt.executeQuery();			
			return rs;
		}
	}
	

	public int executeUpdate() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setNull(int parameterIndex, int sqlType) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setBoolean(int parameterIndex, boolean x) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setByte(int parameterIndex, byte x) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setShort(int parameterIndex, short x) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setInt(int parameterIndex, int x) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setLong(int parameterIndex, long x) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setFloat(int parameterIndex, float x) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setDouble(int parameterIndex, double x) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setString(int parameterIndex, String x) throws SQLException {
		this.realStmt.setString(parameterIndex, x);
	}

	public void setBytes(int parameterIndex, byte[] x) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setDate(int parameterIndex, Date x) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setTime(int parameterIndex, Time x) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void clearParameters() throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setObject(int parameterIndex, Object x) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public boolean execute() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public void addBatch() throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setRef(int parameterIndex, Ref x) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setBlob(int parameterIndex, Blob x) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setClob(int parameterIndex, Clob x) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setArray(int parameterIndex, Array x) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public ResultSetMetaData getMetaData() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setNull(int parameterIndex, int sqlType, String typeName) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setURL(int parameterIndex, URL x) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public ParameterMetaData getParameterMetaData() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public void setRowId(int parameterIndex, RowId x) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setNString(int parameterIndex, String value) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setNClob(int parameterIndex, NClob value) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setClob(int parameterIndex, Reader reader) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setNClob(int parameterIndex, Reader reader) throws SQLException {
		// TODO Auto-generated method stub
		
	}

}
