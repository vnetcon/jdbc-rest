package com.vnetcon.jdbc.rest;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Types;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;


public class RestDriver implements Driver {

	private static Driver registeredDriver;
	
	public static final String jsonPrefixInSql = "--[json";
	public static final String jsonPrefix = "json";
	public static final String restColumnName = "vnetcon_json";
	public static final String restSchema = "vnetcon";
	public static final String restTable = "json";
	public static final int restColType = Types.CLOB;
	public static final String restColTypeName = "CLOB";
	public static final String restColClassName = "java.lang.String";
	public static final String queryParamPrefix = "q_";
	public static final String tableParamPrefix = "t_";
	
	private static final String dbConf = "/etc/vnetcon/database.properties";
	private static final String urlPrefix = "jdbc:vnetcon:rest://";
//	private Properties dbProps = new Properties();
	public static Map<String, Object> dbProps = new HashMap<String, Object>();
	private Connection lastRetCon = null;
	private Connection lastRetRealCon = null;

	static {
		try {
			register();
		} catch (SQLException e) {
			throw new ExceptionInInitializerError(e);
		}
	}

	
	public static void register() throws SQLException {
		if (isRegistered()) {
			throw new IllegalStateException("Driver is already registered. It can only be registered once.");
		}
		Driver registeredDriver = new RestDriver();
		DriverManager.registerDriver(registeredDriver);
		RestDriver.registeredDriver = registeredDriver;
	}
	
	public static void deregister() throws SQLException {
		if (!isRegistered()) {
			throw new IllegalStateException(
					"Driver is not registered (or it has not been registered using Driver.register() method)");
		}
		DriverManager.deregisterDriver(registeredDriver);
		registeredDriver = null;
	}
	
	public static boolean isRegistered() {
		return registeredDriver != null;
	}

	public RestDriver() {
		
	}
	
	public static String getSafeSql(String value) {
		String safeStr = value;

		if(value == null) {
			return "''";
		}
		safeStr = safeStr.trim();
		
		if(safeStr.startsWith("'")) {
			safeStr = safeStr.substring(1);
		}
		
		if(safeStr.endsWith("'")) {
			safeStr = safeStr.substring(0, safeStr.length() - 1);
		}
		
		while(safeStr.indexOf("--") > -1) {
			safeStr = safeStr.replace("--", "-");
		}

		while(safeStr.indexOf("''") > -1) {
			safeStr = safeStr.replace("''", "'");
		}
		safeStr = safeStr.replace("'", "''");

		safeStr = "'" + safeStr + "'";
		
		return safeStr;
	}
	
	public static Map<String, String> getJsonParams(String sql) throws SQLException {
		Map<String, String> params = new LinkedHashMap<String, String>();
		try {
			if(sql.indexOf(RestDriver.jsonPrefix) > -1) {
				String tmp  = sql.substring(sql.lastIndexOf(RestDriver.jsonPrefixInSql));
				tmp = tmp.substring(tmp.indexOf('[') + 1, tmp.indexOf(']'));
	//			tmp = tmp.replaceFirst(RestDriver.jsonPrefix, "");
				if(tmp.indexOf(';') > -1) {
					String p[] = tmp.split(";");
					for(int i = 0; i < p.length; i++) {
						if(p[i].indexOf('=') > -1) {
							String pp[] = p[i].split("=");
							params.put(pp[0].trim(), pp[1].trim());
						} else {
							params.put(p[1].trim(), null);
						}
					}
				} else {
					if(tmp.indexOf('=') > -1) {
						String pp[] = tmp.split("=");
						params.put(pp[0].trim(), pp[1].trim());
					} else {
						params.put(tmp.trim(), null);
					}
				}
			}
		}catch(Exception e) {
			throw new SQLException(e);
		}
		return params;
	}
	
	private String getDatabaseConfigString(String url) throws SQLException {
		try {
			String tmp  = url;
			tmp = tmp.replaceFirst(urlPrefix, "");
			if(tmp.indexOf('?') > -1) {
				String p[] = tmp.split("\\?");
				return p[0];
			} else {
				return tmp;
			}
		}catch(Exception e) {
			throw new SQLException(e);
		}
	}
	
	private Map<String, String> getUrlParams(String url) throws SQLException {
		Map<String, String> params = new HashMap<String, String>();
		try {
			String tmp  = url;
			tmp = tmp.replaceFirst(urlPrefix, "");
			if(tmp.indexOf('?') > -1) {
				String p[] = tmp.split("\\?");
				if(p[1].indexOf('&') > -1) {
					p = p[1].split("&");
					for(int i = 0; i < p.length; i++) {
						if(p[i].indexOf('=') > -1) {
							String pp[] = p[i].split("=");
							if(pp.length > 1) {
								params.put(pp[0], pp[1]);
							} else {
								params.put(pp[0], null);
							}
						} else {
							params.put(p[i], null);
						}
					}
				} else {
					if(p[1].indexOf('=') > -1) {
						String pp[] = p[1].split("=");
						if(pp.length > 1) {
							params.put(pp[0], pp[1]);
						} else {
							params.put(pp[0], null);
						}
					} else {
						params.put(p[1], null);
					}
				}
			}
		}catch(Exception e) {
			throw new SQLException(e);
		}
		return params;
	}

	
	private void loadProperties(String url) throws SQLException {
		try {
			FileInputStream fIn = new FileInputStream(dbConf);
			Properties p = new Properties();
			p.load(fIn);
			fIn.close();
			dbProps.put(url, p);
		}catch(Exception e) {
			throw new SQLException(e);
		}
	}
	
	
	
	public synchronized Connection connect(String url, Properties info) throws SQLException {
		
		if(url != null && url.startsWith(urlPrefix)) {
			String dbConfig = this.getDatabaseConfigString(url);
			this.loadProperties(dbConfig);
			
			if(((Properties)dbProps.get(dbConfig)).getProperty(dbConfig + ".jdbc.driver") != null) {
				try {
					Class.forName(((Properties)dbProps.get(dbConfig)).getProperty(dbConfig + ".jdbc.driver"));
				} catch (ClassNotFoundException e) {
					throw new SQLException(e);
				}
			}
			System.out.println("jdbc-rest: loading database properties " + dbConfig);
			String jdbcUrl = ((Properties)dbProps.get(dbConfig)).getProperty(dbConfig + ".jdbc.url");
			String jdbcUser = ((Properties)dbProps.get(dbConfig)).getProperty(dbConfig + ".jdbc.user");
			String jdbcPass = ((Properties)dbProps.get(dbConfig)).getProperty(dbConfig + ".jdbc.pass");
			((Properties)dbProps.get(dbConfig)).setProperty("jdbc.user", jdbcUser);
			((Properties)dbProps.get(dbConfig)).setProperty("jdbc.pass", jdbcPass);
			((Properties)dbProps.get(dbConfig)).setProperty("dbConfig", dbConfig);
			dbProps.put(jdbcUrl, dbProps.get(dbConfig));
			Connection realCon = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPass); 
//			Driver d = DriverManager.getDriver(jdbcUrl);
//			Properties infor = new Properties();
//			infor.setProperty("user", jdbcUser);
//			infor.setProperty("password", jdbcPass);
//			Connection realCon = d.connect(jdbcUrl, infor);
			RestConnection con = new RestConnection(dbConfig, realCon);
			return con;
		}
		/*
		else {
			System.out.println("jdbc-rest: loading database properties " + url);
			String jdbcUser = ((Properties)dbProps.get(url)).getProperty("jdbc.user");
			String jdbcPass = ((Properties)dbProps.get(url)).getProperty("jdbc.pass");
			String dbConfig = ((Properties)dbProps.get(url)).getProperty("dbConfig");
			System.out.println("    jdbcUser " + jdbcUser);
			System.out.println("    dbConfig " + dbConfig);
			
			if(!dbProps.containsKey(url + "-conn")) {
				System.out.println("Initializeng connection");
				Connection realCon = DriverManager.getConnection(url, jdbcUser, jdbcPass); 
				RestConnection con = new RestConnection(dbConfig, realCon);
				dbProps.put(url + "-conn", realCon);
				dbProps.put(url + "-rconn", con);
			}
			
			RestConnection con = (RestConnection)dbProps.get(url + "-rconn");
			lastRetRealCon = (Connection)dbProps.get(url + "-conn");
			lastRetCon = con;
			return null;
			
			
		}
		*/
		return null;
		
	}

	public boolean acceptsURL(String url) throws SQLException {
		
		if(url != null && url.startsWith(urlPrefix)) {
			return true;
		}
		
		return false;
	}

	public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
		try {
			Class<Driver> dr = (Class<Driver>)Class.forName(this.lastRetRealCon.getMetaData().getDriverName());
			return ((Driver)dr.newInstance()).getPropertyInfo(url, info);
		}catch(Exception e) {
			throw new SQLException(e);
		}
	}

	public int getMajorVersion() {
		try {
			Class<Driver> dr = (Class<Driver>)Class.forName(this.lastRetRealCon.getMetaData().getDriverName());
			return ((Driver)dr.newInstance()).getMajorVersion();
		}catch(Exception e) {
			return 0;
		}
	}

	public int getMinorVersion() {
		try {
			Class<Driver> dr = (Class<Driver>)Class.forName(this.lastRetRealCon.getMetaData().getDriverName());
			return ((Driver)dr.newInstance()).getMinorVersion();
		}catch(Exception e) {
			return 0;
		}
	}

	public boolean jdbcCompliant() {
		try {
			Class<Driver> dr = (Class<Driver>)Class.forName(this.lastRetRealCon.getMetaData().getDriverName());
			return ((Driver)dr.newInstance()).jdbcCompliant();
		}catch(Exception e) {
			return false;
		}
	}

	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		try {
			Class<Driver> dr = (Class<Driver>)Class.forName(this.lastRetRealCon.getMetaData().getDriverName());
			return ((Driver)dr.newInstance()).getParentLogger();
		}catch(Exception e) {
			return null;
		}
	}

	
}
