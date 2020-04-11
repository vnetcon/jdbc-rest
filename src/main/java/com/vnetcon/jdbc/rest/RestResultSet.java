package com.vnetcon.jdbc.rest;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Calendar;
import java.util.Map;


import javax.sql.rowset.serial.SerialClob;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class RestResultSet implements ResultSet {

	private Gson gson = new GsonBuilder().setPrettyPrinting().create();
	private ResultSetUtils rsu = new ResultSetUtils();
	private ResultSet realRs = null;
	private RestConnection restCon = null;
	private RestStatement restStmt = null;
	private RestPreparedStatement restPStmt = null;
	private String sql = null;
	private boolean isJson = false;
	private Map<String, String> jsonParams = null;
	private Map<String, String> queryParams = null;
	
	public RestResultSet(RestConnection restCon, RestStatement stmt, ResultSet rs, String sql, Map<String, String> jsonParams, Map<String, String> queryParams) throws SQLException {
		this.realRs = rs;
		this.restCon = restCon;
		this.restStmt = stmt;
		this.sql = sql;
		this.jsonParams = jsonParams;
		this.queryParams = queryParams;
		
		rsu.setMainSql(this.sql);
		
		if(this.jsonParams == null) {
			this.jsonParams = RestDriver.getJsonParams(sql);
		}
		
		if(sql != null && sql.toLowerCase().indexOf(RestDriver.jsonPrefix) > -1) {
			isJson = true;
		}else {
			isJson = false;
		}
	}

	public RestResultSet(RestConnection restCon, RestPreparedStatement stmt, ResultSet rs, String sql, Map<String, String> jsonParams, Map<String, String> queryParams) {
		this.realRs = rs;
		this.restCon = restCon;
		this.restPStmt = stmt;
		this.sql = sql;
		this.jsonParams = jsonParams;
		this.queryParams = queryParams;
		
		if(sql != null && sql.toLowerCase().indexOf(RestDriver.jsonPrefix) > -1) {
			isJson = true;
		}else {
			isJson = false;
		}
	}
	
	public ResultSet getRealResultSet() {
		return realRs;
	}
	
	public <T> T unwrap(Class<T> iface) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public boolean next() throws SQLException {
		return realRs.next();
	}

	public void close() throws SQLException {
		realRs.close();
	}

	public boolean wasNull() throws SQLException {
		return realRs.wasNull();
	}

	public String getString(int columnIndex) throws SQLException {
		return realRs.getString(columnIndex);
	}

	public boolean getBoolean(int columnIndex) throws SQLException {
		return realRs.getBoolean(columnIndex);
	}

	public byte getByte(int columnIndex) throws SQLException {
		return realRs.getByte(columnIndex);
	}

	public short getShort(int columnIndex) throws SQLException {
		return realRs.getShort(columnIndex);
	}

	public int getInt(int columnIndex) throws SQLException {
		return realRs.getInt(columnIndex);
	}

	public long getLong(int columnIndex) throws SQLException {
		return realRs.getLong(columnIndex);
	}

	public float getFloat(int columnIndex) throws SQLException {
		return realRs.getFloat(columnIndex);
	}

	public double getDouble(int columnIndex) throws SQLException {
		return realRs.getDouble(columnIndex);
	}

	public BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException {
		return realRs.getBigDecimal(columnIndex, scale);
	}

	public byte[] getBytes(int columnIndex) throws SQLException {
		return realRs.getBytes(columnIndex);
	}

	public Date getDate(int columnIndex) throws SQLException {
		return realRs.getDate(columnIndex);
	}

	public Time getTime(int columnIndex) throws SQLException {
		return realRs.getTime(columnIndex);
	}

	public Timestamp getTimestamp(int columnIndex) throws SQLException {
		return realRs.getTimestamp(columnIndex);
	}

	public InputStream getAsciiStream(int columnIndex) throws SQLException {
		return realRs.getAsciiStream(columnIndex);
	}

	public InputStream getUnicodeStream(int columnIndex) throws SQLException {
		return realRs.getUnicodeStream(columnIndex);
	}

	public InputStream getBinaryStream(int columnIndex) throws SQLException {
		return realRs.getBinaryStream(columnIndex);
	}

	public String getString(String columnLabel) throws SQLException {
		return realRs.getString(columnLabel);
	}

	public boolean getBoolean(String columnLabel) throws SQLException {
		return realRs.getBoolean(columnLabel);
	}

	public byte getByte(String columnLabel) throws SQLException {
		return realRs.getByte(columnLabel);
	}

	public short getShort(String columnLabel) throws SQLException {
		return realRs.getShort(columnLabel);
	}

	public int getInt(String columnLabel) throws SQLException {
		return realRs.getInt(columnLabel);
	}

	public long getLong(String columnLabel) throws SQLException {
		return realRs.getLong(columnLabel);
	}

	public float getFloat(String columnLabel) throws SQLException {
		return realRs.getFloat(columnLabel);
	}

	public double getDouble(String columnLabel) throws SQLException {
		return realRs.getDouble(columnLabel);
	}

	public BigDecimal getBigDecimal(String columnLabel, int scale) throws SQLException {
		return realRs.getBigDecimal(columnLabel, scale);
	}

	public byte[] getBytes(String columnLabel) throws SQLException {
		return realRs.getBytes(columnLabel);
	}

	public Date getDate(String columnLabel) throws SQLException {
		return realRs.getDate(columnLabel);
	}

	public Time getTime(String columnLabel) throws SQLException {
		return realRs.getTime(columnLabel);
	}

	public Timestamp getTimestamp(String columnLabel) throws SQLException {
		return realRs.getTimestamp(columnLabel);
	}

	public InputStream getAsciiStream(String columnLabel) throws SQLException {
		return realRs.getAsciiStream(columnLabel);
	}

	public InputStream getUnicodeStream(String columnLabel) throws SQLException {
		return realRs.getUnicodeStream(columnLabel);
	}

	public InputStream getBinaryStream(String columnLabel) throws SQLException {
		return realRs.getBinaryStream(columnLabel);
	}

	public SQLWarning getWarnings() throws SQLException {
		return realRs.getWarnings();
	}

	public void clearWarnings() throws SQLException {
		realRs.clearWarnings();
	}

	public String getCursorName() throws SQLException {
		return realRs.getCursorName();
	}

	public ResultSetMetaData getMetaData() throws SQLException {
		if(isJson) {
			RestResultSetMetaData rss = new RestResultSetMetaData();
			return rss;
		} else {
			return realRs.getMetaData();
		}
	}

	
	private String toPrettyFormat(String jsonString) {
		Object json = null;
		JsonParser parser = new JsonParser();
		JsonElement jsonElement = parser.parse(jsonString);
		String prettyJson = gson.toJson(jsonElement);
		return prettyJson;
	}
	
	public Object getObject(int columnIndex) throws SQLException {
		
		if(isJson) {
			String json = null;
			try {
				
				if(columnIndex == 1) {
					json = rsu.rowToJson(restCon, realRs, jsonParams, queryParams);
					
					if(jsonParams.containsKey(RestDriver.jsonPrefix)) {
						String value = jsonParams.get(RestDriver.jsonPrefix);
						if(value != null) {
							json = "{\"" + value + "\" : " + json + "}";
						}
					}
									
					json = toPrettyFormat(json);
					return new SerialClob(json.toCharArray());
				} else {
					return new SerialClob(rsu.getRealVals().toCharArray());
				}
			} catch (Exception e) {
				throw new SQLException(json);
			}
		} else {
			return realRs.getObject(columnIndex);
		}
	}

	public Object getObject(String columnLabel) throws SQLException {
		return realRs.getObject(columnLabel);
	}

	public int findColumn(String columnLabel) throws SQLException {
		return realRs.findColumn(columnLabel);
	}

	public Reader getCharacterStream(int columnIndex) throws SQLException {
		return realRs.getCharacterStream(columnIndex);
	}

	public Reader getCharacterStream(String columnLabel) throws SQLException {
		return realRs.getCharacterStream(columnLabel);
	}

	public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
		return realRs.getBigDecimal(columnIndex);
	}

	public BigDecimal getBigDecimal(String columnLabel) throws SQLException {
		return realRs.getBigDecimal(columnLabel);
	}

	public boolean isBeforeFirst() throws SQLException {
		return realRs.isAfterLast();
	}

	public boolean isAfterLast() throws SQLException {
		return realRs.isAfterLast();
	}

	public boolean isFirst() throws SQLException {
		return realRs.isFirst();
	}

	public boolean isLast() throws SQLException {
		return realRs.isLast();
	}

	public void beforeFirst() throws SQLException {
		realRs.beforeFirst();
	}

	public void afterLast() throws SQLException {
		realRs.afterLast();
	}

	public boolean first() throws SQLException {
		return realRs.first();
	}

	public boolean last() throws SQLException {
		return realRs.last();
	}

	public int getRow() throws SQLException {
		return realRs.getRow();
	}

	public boolean absolute(int row) throws SQLException {
		return realRs.absolute(row);
	}

	public boolean relative(int rows) throws SQLException {
		return realRs.relative(rows);
	}

	public boolean previous() throws SQLException {
		return realRs.previous();
	}

	public void setFetchDirection(int direction) throws SQLException {
		realRs.setFetchDirection(direction);
	}

	public int getFetchDirection() throws SQLException {
		return realRs.getFetchDirection();
	}

	public void setFetchSize(int rows) throws SQLException {
		realRs.setFetchSize(rows);
	}

	public int getFetchSize() throws SQLException {
		return realRs.getFetchSize();
	}

	public int getType() throws SQLException {
		return realRs.getType();
	}

	public int getConcurrency() throws SQLException {
		return realRs.getConcurrency();
	}

	public boolean rowUpdated() throws SQLException {
		return false;
	}

	public boolean rowInserted() throws SQLException {
		return false;
	}

	public boolean rowDeleted() throws SQLException {
		return false;
	}

	public void updateNull(int columnIndex) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void updateBoolean(int columnIndex, boolean x) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void updateByte(int columnIndex, byte x) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void updateShort(int columnIndex, short x) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void updateInt(int columnIndex, int x) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void updateLong(int columnIndex, long x) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void updateFloat(int columnIndex, float x) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void updateDouble(int columnIndex, double x) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void updateBigDecimal(int columnIndex, BigDecimal x) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void updateString(int columnIndex, String x) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void updateBytes(int columnIndex, byte[] x) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void updateDate(int columnIndex, Date x) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void updateTime(int columnIndex, Time x) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void updateTimestamp(int columnIndex, Timestamp x) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void updateAsciiStream(int columnIndex, InputStream x, int length) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void updateBinaryStream(int columnIndex, InputStream x, int length) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void updateCharacterStream(int columnIndex, Reader x, int length) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void updateObject(int columnIndex, Object x, int scaleOrLength) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void updateObject(int columnIndex, Object x) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void updateNull(String columnLabel) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void updateBoolean(String columnLabel, boolean x) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void updateByte(String columnLabel, byte x) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void updateShort(String columnLabel, short x) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void updateInt(String columnLabel, int x) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void updateLong(String columnLabel, long x) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void updateFloat(String columnLabel, float x) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void updateDouble(String columnLabel, double x) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void updateBigDecimal(String columnLabel, BigDecimal x) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void updateString(String columnLabel, String x) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void updateBytes(String columnLabel, byte[] x) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void updateDate(String columnLabel, Date x) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void updateTime(String columnLabel, Time x) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void updateTimestamp(String columnLabel, Timestamp x) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void updateAsciiStream(String columnLabel, InputStream x, int length) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void updateBinaryStream(String columnLabel, InputStream x, int length) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void updateCharacterStream(String columnLabel, Reader reader, int length) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void updateObject(String columnLabel, Object x, int scaleOrLength) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void updateObject(String columnLabel, Object x) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void insertRow() throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void updateRow() throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void deleteRow() throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void refreshRow() throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void cancelRowUpdates() throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void moveToInsertRow() throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void moveToCurrentRow() throws SQLException {
		throw new UnsupportedOperationException();
	}

	public Statement getStatement() throws SQLException {
		return this.restStmt;
	}

	public Object getObject(int columnIndex, Map<String, Class<?>> map) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public Ref getRef(int columnIndex) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public Blob getBlob(int columnIndex) throws SQLException {
		return realRs.getBlob(columnIndex);
	}

	public Clob getClob(int columnIndex) throws SQLException {
		if(columnIndex == 1 || columnIndex == 2) {
			try {
				String json = rsu.rowToJson(restCon, realRs, jsonParams, queryParams);
				
				if(jsonParams.containsKey(RestDriver.jsonPrefix)) {
					String value = jsonParams.get(RestDriver.jsonPrefix);
					if(value != null) {
						json = "{\"" + value + "\" : " + json + "}";
					}
				}
				
				if(columnIndex == 1) {
					return new SerialClob(json.toCharArray());
				}else {
					return new SerialClob(rsu.getRealVals().toCharArray());
				}
			} catch (Exception e) {
				throw new SQLException(e);
			}
		}
		throw new SQLException("Only columnnumber 1 or 2 is available in rest driver");
	}

	public Array getArray(int columnIndex) throws SQLException {
		return realRs.getArray(columnIndex);
	}

	public Object getObject(String columnLabel, Map<String, Class<?>> map) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public Ref getRef(String columnLabel) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public Blob getBlob(String columnLabel) throws SQLException {
		return realRs.getBlob(columnLabel);
	}

	public Clob getClob(String columnLabel) throws SQLException {
		if(RestDriver.restColumnNameJson.contentEquals(columnLabel)) {
			try {
				String json = rsu.rowToJson(restCon, realRs, jsonParams, queryParams);
				return new SerialClob(json.toCharArray());
			} catch (Exception e) {
				throw new SQLException(e);
			}
		}
		throw new SQLException("Only " + RestDriver.restColumnNameJson + " column is available in rest driver");
	}

	public Array getArray(String columnLabel) throws SQLException {
		return realRs.getArray(columnLabel);
	}

	public Date getDate(int columnIndex, Calendar cal) throws SQLException {
		return realRs.getDate(columnIndex, cal);
	}

	public Date getDate(String columnLabel, Calendar cal) throws SQLException {
		return realRs.getDate(columnLabel, cal);
	}

	public Time getTime(int columnIndex, Calendar cal) throws SQLException {
		return realRs.getTime(columnIndex, cal);
	}

	public Time getTime(String columnLabel, Calendar cal) throws SQLException {
		return realRs.getTime(columnLabel, cal);
	}

	public Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException {
		return realRs.getTimestamp(columnIndex, cal);
	}

	public Timestamp getTimestamp(String columnLabel, Calendar cal) throws SQLException {
		return realRs.getTimestamp(columnLabel, cal);
	}

	public URL getURL(int columnIndex) throws SQLException {
		return realRs.getURL(columnIndex);
	}

	public URL getURL(String columnLabel) throws SQLException {
		return realRs.getURL(columnLabel);
	}

	public void updateRef(int columnIndex, Ref x) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void updateRef(String columnLabel, Ref x) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void updateBlob(int columnIndex, Blob x) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void updateBlob(String columnLabel, Blob x) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void updateClob(int columnIndex, Clob x) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void updateClob(String columnLabel, Clob x) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void updateArray(int columnIndex, Array x) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void updateArray(String columnLabel, Array x) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public RowId getRowId(int columnIndex) throws SQLException {
		return realRs.getRowId(columnIndex);
	}

	public RowId getRowId(String columnLabel) throws SQLException {
		return realRs.getRowId(columnLabel);
	}

	public void updateRowId(int columnIndex, RowId x) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void updateRowId(String columnLabel, RowId x) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public int getHoldability() throws SQLException {
		throw new UnsupportedOperationException();
	}

	public boolean isClosed() throws SQLException {
		return realRs.isClosed();
	}

	public void updateNString(int columnIndex, String nString) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void updateNString(String columnLabel, String nString) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void updateNClob(int columnIndex, NClob nClob) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void updateNClob(String columnLabel, NClob nClob) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public NClob getNClob(int columnIndex) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public NClob getNClob(String columnLabel) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public SQLXML getSQLXML(int columnIndex) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public SQLXML getSQLXML(String columnLabel) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void updateSQLXML(int columnIndex, SQLXML xmlObject) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void updateSQLXML(String columnLabel, SQLXML xmlObject) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public String getNString(int columnIndex) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public String getNString(String columnLabel) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public Reader getNCharacterStream(int columnIndex) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public Reader getNCharacterStream(String columnLabel) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void updateNCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void updateNCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void updateAsciiStream(int columnIndex, InputStream x, long length) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void updateBinaryStream(int columnIndex, InputStream x, long length) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void updateCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void updateAsciiStream(String columnLabel, InputStream x, long length) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void updateBinaryStream(String columnLabel, InputStream x, long length) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void updateCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void updateBlob(int columnIndex, InputStream inputStream, long length) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void updateBlob(String columnLabel, InputStream inputStream, long length) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void updateClob(int columnIndex, Reader reader, long length) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void updateClob(String columnLabel, Reader reader, long length) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void updateNClob(int columnIndex, Reader reader, long length) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void updateNClob(String columnLabel, Reader reader, long length) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void updateNCharacterStream(int columnIndex, Reader x) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void updateNCharacterStream(String columnLabel, Reader reader) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void updateAsciiStream(int columnIndex, InputStream x) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void updateBinaryStream(int columnIndex, InputStream x) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void updateCharacterStream(int columnIndex, Reader x) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void updateAsciiStream(String columnLabel, InputStream x) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void updateBinaryStream(String columnLabel, InputStream x) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void updateCharacterStream(String columnLabel, Reader reader) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void updateBlob(int columnIndex, InputStream inputStream) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void updateBlob(String columnLabel, InputStream inputStream) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void updateClob(int columnIndex, Reader reader) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void updateClob(String columnLabel, Reader reader) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void updateNClob(int columnIndex, Reader reader) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void updateNClob(String columnLabel, Reader reader) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public <T> T getObject(int columnIndex, Class<T> type) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public <T> T getObject(String columnLabel, Class<T> type) throws SQLException {
		throw new UnsupportedOperationException();
	}

}
