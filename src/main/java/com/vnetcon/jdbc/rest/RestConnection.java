package com.vnetcon.jdbc.rest;

import java.io.FileInputStream;
import java.io.Reader;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.Executor;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class RestConnection implements Connection {

	private Gson gson = new GsonBuilder().setPrettyPrinting().create();
	private Connection realCon = null;
	
	private Map<String, String> queryParams = null;
	private Map<String, String> jsonParams = null;
	private boolean isParamsFromWebContaier = false;
	
	
	private String toPrettyFormat(String jsonString) {
		JsonParser parser = new JsonParser();
		JsonElement jsonElement = parser.parse(jsonString);
		String prettyJson = gson.toJson(jsonElement);
		return prettyJson;
	}
	
	public RestConnection(String config, Connection realCon) {
		this.realCon = realCon;
	}
	
	
	public boolean isParamsFromWebContaier() {
		return this.isParamsFromWebContaier;
	}
	
	public void setQueryParams(Map<String, String> queryParams) {
		this.isParamsFromWebContaier = true;
		this.queryParams = queryParams;
	}

	public Map<String, String> getQueryParams() {
		return this.queryParams;
	}
	
	public Connection getRealCon() {
		return realCon;
	}
	
	public RestConnection(Connection con) {
		this.realCon = con;
	}
	
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return realCon.unwrap(iface);
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return this.realCon.isWrapperFor(iface);
	}

	public Statement createStatement() throws SQLException {
		Statement stmt = realCon.createStatement();
		RestStatement rstmt = new RestStatement(this, stmt, jsonParams, queryParams);
		return rstmt;
	}

	public PreparedStatement prepareStatement(String sql) throws SQLException {
		/*
		if(sql != null && sql.toLowerCase().indexOf(RestDriver.jsonPrefix) > -1) {
			String psql = sql;
			jsonParams = RestDriver.getJsonParams(sql);
						
			Set<String> keys = jsonParams.keySet();
			Iterator<String> iter = keys.iterator();
			while(iter.hasNext()) {
				String key = iter.next();
				String value = jsonParams.get(key);
				String safeValue = RestDriver.getSafeSql(value);
				psql = psql.replace("'{" + key + "}'", safeValue);
				throw new SQLException(psql + ": " + key + " -> " + value);
			}
			sql = psql;
			
			PreparedStatement stmt = realCon.prepareStatement(sql);
			RestPreparedStatement rstmt = new RestPreparedStatement(this, stmt, sql, jsonParams, queryParams);
			return rstmt;
		}else {
			PreparedStatement stmt = realCon.prepareStatement(sql);
			return stmt;
		}
		*/
		PreparedStatement stmt = realCon.prepareStatement(sql);
		return stmt;
	}

	public CallableStatement prepareCall(String sql) throws SQLException {
		return realCon.prepareCall(sql);
	}

	public String nativeSQL(String sql) throws SQLException {
		return realCon.nativeSQL(sql);
	}

	public void setAutoCommit(boolean autoCommit) throws SQLException {
		realCon.setAutoCommit(autoCommit);
	}

	public boolean getAutoCommit() throws SQLException {
		return realCon.getAutoCommit();
	}

	public void commit() throws SQLException {
		realCon.commit();
	}

	public void rollback() throws SQLException {
		realCon.rollback();
	}

	public void close() throws SQLException {
		realCon.close();
	}

	public boolean isClosed() throws SQLException {
		return realCon.isClosed();
	}

	public DatabaseMetaData getMetaData() throws SQLException {
		return realCon.getMetaData();
	}

	public void setReadOnly(boolean readOnly) throws SQLException {
		realCon.setReadOnly(readOnly);
	}

	public boolean isReadOnly() throws SQLException {
		return realCon.isReadOnly();
	}

	public void setCatalog(String catalog) throws SQLException {
		realCon.setCatalog(catalog);
	}

	public String getCatalog() throws SQLException {
		return realCon.getCatalog();
	}

	public void setTransactionIsolation(int level) throws SQLException {
		realCon.setTransactionIsolation(level);
	}

	public int getTransactionIsolation() throws SQLException {
		return realCon.getTransactionIsolation();
	}

	public SQLWarning getWarnings() throws SQLException {
		return realCon.getWarnings();
	}

	public void clearWarnings() throws SQLException {
		realCon.clearWarnings();
	}

	public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
		Statement stmt = realCon.createStatement();
		RestStatement rstmt = new RestStatement(this, stmt, jsonParams, queryParams);
		return rstmt;
	}

	public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency)
			throws SQLException {
		throw new UnsupportedOperationException();
		//return realCon.prepareStatement(sql, resultSetType, resultSetConcurrency);
	}

	public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
		return realCon.prepareCall(sql, resultSetType, resultSetConcurrency);
	}

	public Map<String, Class<?>> getTypeMap() throws SQLException {
		return realCon.getTypeMap();
	}

	public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
		realCon.setTypeMap(map);
	}

	public void setHoldability(int holdability) throws SQLException {
		realCon.setHoldability(holdability);
	}

	public int getHoldability() throws SQLException {
		return realCon.getHoldability();
	}

	public Savepoint setSavepoint() throws SQLException {
		return realCon.setSavepoint();
	}

	public Savepoint setSavepoint(String name) throws SQLException {
		return realCon.setSavepoint(name);
	}

	public void rollback(Savepoint savepoint) throws SQLException {
		realCon.rollback(savepoint);
	}

	public void releaseSavepoint(Savepoint savepoint) throws SQLException {
		realCon.releaseSavepoint(savepoint);
	}

	public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		return realCon.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
	}

	public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency,
			int resultSetHoldability) throws SQLException {
		throw new UnsupportedOperationException();
		//return realCon.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
	}

	public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency,
			int resultSetHoldability) throws SQLException {
		return realCon.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
	}

	public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
		throw new UnsupportedOperationException();
		//return realCon.prepareStatement(sql, autoGeneratedKeys);
	}

	public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
		throw new UnsupportedOperationException();
		//return realCon.prepareStatement(sql, columnIndexes);
	}

	public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
		throw new UnsupportedOperationException();
		//return realCon.prepareStatement(sql, columnNames);
	}

	public Clob createClob() throws SQLException {
		return realCon.createClob();
	}

	public Blob createBlob() throws SQLException {
		return realCon.createBlob();
	}

	public NClob createNClob() throws SQLException {
		return realCon.createNClob();
	}

	public SQLXML createSQLXML() throws SQLException {
		return realCon.createSQLXML();
	}

	public boolean isValid(int timeout) throws SQLException {
		return realCon.isValid(timeout);
	}

	public void setClientInfo(String name, String value) throws SQLClientInfoException {
		realCon.setClientInfo(name, value);
	}

	public void setClientInfo(Properties properties) throws SQLClientInfoException {
		realCon.setClientInfo(properties);
	}

	public String getClientInfo(String name) throws SQLException {
		return realCon.getClientInfo(name);
	}

	public Properties getClientInfo() throws SQLException {
		return realCon.getClientInfo();
	}

	public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
		return realCon.createArrayOf(typeName, elements);
	}

	public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
		return realCon.createStruct(typeName, attributes);
	}

	public void setSchema(String schema) throws SQLException {
		realCon.setSchema(schema);
	}

	public String getSchema() throws SQLException {
		return realCon.getSchema();
	}

	public void abort(Executor executor) throws SQLException {
		realCon.abort(executor);
	}

	public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
		realCon.setNetworkTimeout(executor, milliseconds);
	}

	public int getNetworkTimeout() throws SQLException {
		return realCon.getNetworkTimeout();
	}
	
}
