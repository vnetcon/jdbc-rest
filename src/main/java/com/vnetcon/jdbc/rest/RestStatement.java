package com.vnetcon.jdbc.rest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public class RestStatement implements Statement {

	private Statement realStmt = null;
	private RestConnection restCon = null;
	private String sql = null;
	private Map<String, String> jsonParams;
	private Map<String, String> queryParams;

	
	public RestStatement(RestConnection restCon, Statement stmt, Map<String, String> jsonParams, Map<String, String> queryParams) {
		this.realStmt = stmt;
		this.restCon = restCon;
		this.jsonParams = jsonParams;
		this.queryParams = queryParams;
	}
	
	public boolean isParamsFromWebContaier() {
		return restCon.isParamsFromWebContaier();
	}
	
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return realStmt.unwrap(iface);
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return realStmt.isWrapperFor(iface);
	}

	public ResultSet executeQuery(String sql) throws SQLException {
		/*
		String psql = sql;
		this.sql = sql;
		if(sql != null && sql.trim().toLowerCase().indexOf(RestDriver.jsonPrefix) > -1) {
			
			if(jsonParams == null) {
				jsonParams = RestDriver.getJsonParams(sql);
			}

			Set<String> keys = jsonParams.keySet();
			Iterator<String> iter = keys.iterator();
			while(iter.hasNext()) {
				String key = iter.next();
				String value = jsonParams.get(key);
				String safeValue = RestDriver.getSafeSql(value);
				psql = psql.replace("'{" + key + "}'", safeValue);
			}
			sql = psql;
			
			   
			ResultSet rs = realStmt.executeQuery(sql);
			RestResultSet rrs = new RestResultSet(restCon, this, rs, sql, jsonParams, queryParams);
			return rrs;
		}else {
			ResultSet rs = realStmt.executeQuery(sql);			
			return rs;
		}
		*/
		if(this.execute(sql)) {
			return this.getResultSet();
		}
		//throw new SQLException("SQL execute return false for some reason.");
		return null;
	}

	public int executeUpdate(String sql) throws SQLException {
		return realStmt.executeUpdate(sql);
	}

	public void close() throws SQLException {
		realStmt.close();
	}

	public int getMaxFieldSize() throws SQLException {
		return realStmt.getMaxFieldSize();
	}

	public void setMaxFieldSize(int max) throws SQLException {
		realStmt.setMaxFieldSize(max);
	}

	public int getMaxRows() throws SQLException {
		return realStmt.getMaxRows();
	}

	public void setMaxRows(int max) throws SQLException {
		realStmt.setMaxRows(max);
	}

	public void setEscapeProcessing(boolean enable) throws SQLException {
		realStmt.setEscapeProcessing(enable);
	}

	public int getQueryTimeout() throws SQLException {
		return realStmt.getQueryTimeout();
	}

	public void setQueryTimeout(int seconds) throws SQLException {
		realStmt.setQueryTimeout(seconds);
	}

	public void cancel() throws SQLException {
		realStmt.cancel();
	}

	public SQLWarning getWarnings() throws SQLException {
		return realStmt.getWarnings();
	}

	public void clearWarnings() throws SQLException {
		realStmt.clearWarnings();
	}

	public void setCursorName(String name) throws SQLException {
		realStmt.setCursorName(name);
	}

	public boolean execute(String sql) throws SQLException {
		String psql= sql;
		this.sql = sql;
		
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

		if(queryParams != null) {
			keys = queryParams.keySet();
			iter = keys.iterator();
			while(iter.hasNext()) {
				String key = iter.next();
				String value = queryParams.get(key);
				String safeValue = RestDriver.getSafeSql(value);
				psql = psql.replace("'{r_" + key + "}'", safeValue);
			}
		}
		
		this.sql = psql;
		
		return realStmt.execute(this.sql);
	}

	public ResultSet getResultSet() throws SQLException {
		ResultSet rs = realStmt.getResultSet();
		RestResultSet restRs = new RestResultSet(restCon, this, rs, sql, jsonParams, queryParams);
		return restRs;
	}
	

	public int getUpdateCount() throws SQLException {
		return realStmt.getUpdateCount();
	}

	public boolean getMoreResults() throws SQLException {
		return realStmt.getMoreResults();
	}

	public void setFetchDirection(int direction) throws SQLException {
		realStmt.setFetchDirection(direction);
	}

	public int getFetchDirection() throws SQLException {
		return realStmt.getFetchDirection();
	}

	public void setFetchSize(int rows) throws SQLException {
		realStmt.setFetchSize(rows);
	}

	public int getFetchSize() throws SQLException {
		return realStmt.getFetchSize();
	}

	public int getResultSetConcurrency() throws SQLException {
		return realStmt.getResultSetConcurrency();
	}

	public int getResultSetType() throws SQLException {
		return realStmt.getResultSetType();
	}

	public void addBatch(String sql) throws SQLException {
		realStmt.addBatch(sql);
	}

	public void clearBatch() throws SQLException {
		realStmt.clearBatch();
	}

	public int[] executeBatch() throws SQLException {
		return realStmt.executeBatch();
	}

	public Connection getConnection() throws SQLException {
		return restCon;
	}

	public boolean getMoreResults(int current) throws SQLException {
		return realStmt.getMoreResults(current);
	}

	public ResultSet getGeneratedKeys() throws SQLException {
		return realStmt.getGeneratedKeys();
	}

	public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
		return realStmt.executeUpdate(sql, autoGeneratedKeys);
	}

	public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
		return realStmt.executeUpdate(sql, columnIndexes);
	}

	public int executeUpdate(String sql, String[] columnNames) throws SQLException {
		return realStmt.executeUpdate(sql, columnNames);
	}

	public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
		return realStmt.execute(sql, autoGeneratedKeys);
	}

	public boolean execute(String sql, int[] columnIndexes) throws SQLException {
		return realStmt.execute(sql, columnIndexes);
	}

	public boolean execute(String sql, String[] columnNames) throws SQLException {
		return realStmt.execute(sql, columnNames);
	}

	public int getResultSetHoldability() throws SQLException {
		return realStmt.getResultSetHoldability();
	}

	public boolean isClosed() throws SQLException {
		return realStmt.isClosed();
	}

	public void setPoolable(boolean poolable) throws SQLException {
		realStmt.setPoolable(poolable);
	}

	public boolean isPoolable() throws SQLException {
		return realStmt.isPoolable();
	}

	public void closeOnCompletion() throws SQLException {
		realStmt.closeOnCompletion();
	}

	public boolean isCloseOnCompletion() throws SQLException {
		return realStmt.isCloseOnCompletion();
	}

}
