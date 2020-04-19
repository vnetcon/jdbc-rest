package com.vnetcon.jdbc.rest;

import java.io.Reader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

public class ResultSetUtils {

	
	class RealValues {
		public String mainSQL;
		public Map<String, String> params;
		public Map<String, String> subqueries = new HashMap<String, String>();
	}
	
	private Map<String, List<String>> rsCols = new HashMap<String, List<String>>();
	private Gson gson = new Gson();
	RealValues rv = new RealValues();

	/*
	private Map<String, String> jsonParams;
	private Map<String, String> queryParams;
	*/

	
	public void setMainSql(String mainSql) {
		rv.mainSQL = mainSql;
	}
	
	public String getRealVals() {
		return gson.toJson(rv);
	}
	
	public String subQueryToJson(Connection con, String subqueryCol, String sql, ResultSet rsParent, Map<String, String> jsonParams, Map<String, String> queryParams, List<String> colNames) throws Exception {
//		this.queryParams = queryParams;
		// we need to override these - these are not same than in main query
		jsonParams = RestDriver.getJsonParams(sql); 
		
		StringBuilder json = new StringBuilder();
		Statement stmt = con.createStatement();
		
		for(int i = 0; i < colNames.size(); i++) {
			String value = rsParent.getString(i+1);
			String safeValue = RestDriver.getSafeSql(value);
			sql = sql.replace("'{t_" + colNames.get(i) + "}'", safeValue);
		}

		rv.subqueries.put(subqueryCol, sql);
		ResultSet rs = stmt.executeQuery(sql);
		json.append("[");
		String delim = "";
		while (rs.next()) {

			json.append(delim);
			char[] buf = new char[1024];
			int iRead = 0;
			StringBuilder sb = new StringBuilder();

			Reader r = rs.getClob(1).getCharacterStream();
			while ((iRead = r.read(buf)) > -1) {
				sb.append(buf, 0, iRead);
			}
			json.append(sb.toString());
			delim = ",";
		}
		stmt.close();

		json.append("]");

		String sRet = json.toString();
		sRet = fixJson(sRet);
		return sRet;

	}
	
	public String rowToJson(Connection con, ResultSet rs, Map<String, String> jsonParams, Map<String, String> queryParams) throws Exception {
//		this.jsonParams = jsonParams;
//		this.queryParams = queryParams;
		
		rv.params = queryParams;
		StringBuilder asJson = new StringBuilder();
		List<String> colNames = null;
		int iColCount = rs.getMetaData().getColumnCount();
		Map<String, Object> json = new HashMap<String, Object>();

		if(!rsCols.containsKey(rs)) {
			colNames = new ArrayList<String>();
			
			for(int i = 0; i < iColCount; i++) {
				colNames.add(rs.getMetaData().getColumnName(i+1));
			}
			
			rsCols.put(rs.toString(), colNames);
		}else {
			colNames = rsCols.get(rs.toString());
		}
		
		for(int i = 0; i < iColCount; i++) {
			String col = colNames.get(i);
			String value = rs.getString(i+1);
			if(col.toLowerCase().indexOf("hidden_") == -1 && col.toLowerCase().indexOf("subquery_") == -1) {
				json.put(col, value);
			}
			if(col.toLowerCase().indexOf("subquery_") > -1) {
				String cleanCol = col.replace("subquery_", "");
				cleanCol = cleanCol.replace("SUBQUERY_", "");
				value = subQueryToJson(con, col, value, rs, jsonParams, queryParams, colNames);
				json.put(cleanCol, value);
			}
		}

		asJson.append(gson.toJson(json));
		String sRet = asJson.toString();
		sRet = fixJson(sRet);
		
		return sRet;
	}
	
	private String fixJson(String s) {
		String sRet = s;
		sRet = sRet.replace("}\"}", "}}");
		sRet = sRet.replace("\"{", "{");
		sRet = sRet.replace("\\", "");

		sRet = sRet.replace("\"}\",\"", "\"},\"");
		sRet = sRet.replace("\"}]\",\"", "\"}],\"");
		
		sRet = sRet.replace("]\"}", "]}");
		sRet = sRet.replace("]\",", "],");
		sRet = sRet.replace("\"[", "[");
		return sRet;
	}
	
}
