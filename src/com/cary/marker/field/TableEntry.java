package com.cary.marker.field;

public class TableEntry {

	private String tableName;
	private String sequenceName;
	private String packageName;

	public String getPackageName() {
		return packageName;
	}

	public String getSequenceName() {
		return sequenceName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public void setSequenceName(String sequenceName) {
		this.sequenceName = sequenceName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
}
