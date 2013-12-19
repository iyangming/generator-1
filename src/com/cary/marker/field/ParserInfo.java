package com.cary.marker.field;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ParserInfo {

	// #<tableName>
	private String tableName;
	// #<tableComment>
	private String tableComment;
	// #<sequenceName>
	private String sequenceName;
	// #<daoPackage>
	private String daoPackage;
	// #<daoName>
	private String daoName;
	// #<modelPackage>
	private String modelPackage;
	// #<modelName>
	private String modelName;

	private List<Field> fields;

	public ParserInfo(Connection connection, String parentPackageName, String tableName, String sequenceName)
			throws Exception {
		fields = new ArrayList<Field>();
		daoPackage = parentPackageName + ".dao";
		//modelPackage = parentPackageName;
		modelPackage = parentPackageName + ".model";
		this.tableName = tableName;
		if (sequenceName == null) {
			sequenceName = tableName + "_S";
		}
		this.sequenceName = sequenceName;
		String javaName = getJavaName(tableName);
		daoName = javaName + "Dao";
		modelName = javaName;

		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery("SELECT * FROM " + tableName);
		ResultSetMetaData metaData = rs.getMetaData();
		DatabaseMetaData dbMetaData = connection.getMetaData();
		int columnCount = metaData.getColumnCount();
		String primaryKey = null;
		ResultSet primaryKeys = dbMetaData.getPrimaryKeys(null, null, tableName);
		if (primaryKeys.next()) {
			primaryKey = primaryKeys.getString(4);
		}
		ResultSet tables = dbMetaData.getTables(null, null, tableName, null);
		if (tables.next()) {
			tableComment = tables.getString(5);
			if (tableComment == null) {
				tableComment = "";
			}
		}
		for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
			String columnName = metaData.getColumnName(columnIndex);
			String columnTypeName = metaData.getColumnTypeName(columnIndex);
			ResultSet column = dbMetaData.getColumns(null, null, tableName, columnName);
			column.next();
			String description = column.getString(12);
			String type = columnTypeName.toUpperCase();
			int index = type.indexOf('(');
			if (index != -1) {
				type = type.substring(0, index);
			}
			int scale = metaData.getScale(columnIndex);
			int precision = metaData.getPrecision(columnIndex);
			Field field = new Field(columnName, type, getPropertyKeyByColumnName(columnName),
					columnName.equals(primaryKey), precision, scale);
			field.setFieldName(columnName);
			if ("NO".equals(column.getString(18))) {
				field.setMandatory(true);
			}
			field.setDescription(description);
			fields.add(field);
		}
		rs.close();
		statement.close();
	}

	public String getDaoName() {
		return daoName;
	}

	public String getDaoPackage() {
		return daoPackage;
	}

	public List<Field> getFields() {
		return fields;
	}

	private String getJavaName(String tableName) {
		String[] segments = tableName.split("_");
		if (segments.length == 1) {
			return getSegment(segments[0]);
		}
		StringBuilder javaName = new StringBuilder();
		for (int i = 1; i < segments.length; i++) {
			String segment = segments[i];
			javaName.append(getSegment(segment));
		}
		return javaName.toString();
	}

	public String getModelName() {
		return modelName;
	}

	public String getModelPackage() {
		return modelPackage;
	}

	private String getPropertyKeyByColumnName(String columnName) {
		String[] split = columnName.split("_");
		StringBuffer buffer = new StringBuffer();
		buffer.append(split[0].toLowerCase());
		for (int i = 1; i < split.length; i++) {
			String lowerCaseSplit = split[i].toLowerCase();
			String firstUpperChar = (lowerCaseSplit.charAt(0) + "").toUpperCase();
			buffer.append(firstUpperChar + lowerCaseSplit.substring(1));
		}
		return buffer.toString();
	}

	private String getSegment(String segment) {
		return Character.toUpperCase(segment.charAt(0)) + segment.substring(1).toLowerCase();
	}

	public String getSequenceName() {
		return sequenceName;
	}

	public String getTableComment() {
		return tableComment;
	}

	public String getTableName() {
		return tableName;
	}

	public void setDaoName(String daoName) {
		this.daoName = daoName;
	}

	public void setDaoPackage(String daoPackage) {
		this.daoPackage = daoPackage;
	}

	public void setFields(List<Field> fields) {
		this.fields = fields;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public void setModelPackage(String modelPackage) {
		this.modelPackage = modelPackage;
	}

	public void setSequenceName(String sequenceName) {
		this.sequenceName = sequenceName;
	}

	public void setTableComment(String tableComment) {
		this.tableComment = tableComment;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
}
