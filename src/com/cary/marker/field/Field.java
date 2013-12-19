package com.cary.marker.field;


public class Field {

	public static String getJdbcType(String javaType) {
		if (javaType.equals("VARCHAR2")) {
			return "VARCHAR";
		}
		if (javaType.equals("LONG")) {
			return "LONGVARCHAR";
		}
		if (javaType.equals("NUMBER")) {
			return "NUMERIC";
		}
		if (javaType.equals("DATE")) {
			return "TIMESTAMP";
		}
		if (javaType.equals("TIMESTAMP")) {
			return "TIMESTAMP";
		}
		if (javaType.equals("CHAR")) {
			return "CHAR";
		}
		if (javaType.equals("CLOB")) {
			return "CLOB";
		}

		throw new RuntimeException("please implenents java type: " + javaType + "'s java type mapping!");
	}

	private String fieldName;

	private String fieldType;

	private String propertyName;

	private String description;

	private boolean isKey;

	private final int precision;

	private int scale;

	private boolean mandatory;

	public Field(String fieldName, String fieldType, String propertyName, boolean isKey, int precision, int scale) {
		this.fieldName = fieldName;
		this.fieldType = fieldType;
		this.propertyName = propertyName;
		this.isKey = isKey;
		this.precision = precision;
		this.scale = scale;
	}

	public String getDescription() {
		if (mandatory) {
			return description + " (Not Null)";
		}
		return description;
	}

	public String getFieldName() {
		return fieldName;
	}

	public String getFieldType() {
		return fieldType;
	}

	public String getJavaType() {
		if (fieldType.toUpperCase().startsWith("VARCHAR2")) {
			return "String";
		} else if (fieldType.toUpperCase().startsWith("CHAR")) {
			return "String";
		} else if (fieldType.toUpperCase().startsWith("CLOB")) {
			return "String";
		} else if (fieldType.toUpperCase().startsWith("DATE")) {
			return "Date";
		} else if (fieldType.toUpperCase().startsWith("NUMBER")) {
			if (scale > 0) {
				return "BigDecimal";
			}
			if (precision == 1) {
				return "boolean";
			}
			if (precision > 9) {
				return "long";
			}
			return "int";
		} else if (fieldType.toUpperCase().startsWith("TIMESTAMP")) {
			return "Date";
		} else if (fieldType.toUpperCase().startsWith("LONG")) {
			return "String";
		} else {
			throw new RuntimeException("please implenents jdbc type: " + fieldType + "'s java type mapping!");
		}
	}

	public String getPropertyName() {
		return propertyName;
	}

	public int getScale() {
		return scale;
	}

	public boolean isKey() {
		return isKey;
	}

	public boolean isMandatory() {
		return mandatory;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public void setKey(boolean isKey) {
		this.isKey = isKey;
	}

	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public void setScale(int scale) {
		this.scale = scale;
	}

	@Override
	public String toString() {
		return fieldName + "(Key:" + isKey + ")" + "_" + fieldType + "_" + propertyName;
	}
}
