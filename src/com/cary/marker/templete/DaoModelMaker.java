package com.cary.marker.templete;

import java.io.IOException;
import java.util.List;

import com.cary.marker.field.Field;
import com.cary.marker.field.ParserInfo;

public class DaoModelMaker {

	private ParserInfo parserInfo;

	public DaoModelMaker(ParserInfo parserInfo) {
		this.parserInfo = parserInfo;
	}

	private String generateDaoModelContent() throws IOException {
		String templateFileContent = DaoMaker.getTemplateFileContent("ModelTemplate.xml");
		String daoModelContent = templateFileContent.replaceAll("#<modelPackage>", parserInfo.getModelPackage());
		daoModelContent = daoModelContent.replaceAll("#<tableComment>", parserInfo.getTableComment());
		daoModelContent = daoModelContent.replaceAll("#<tableName>", parserInfo.getTableName());
		daoModelContent = daoModelContent.replaceAll("#<modelName>", parserInfo.getModelName());
		daoModelContent = daoModelContent.replaceAll("#<serialVersion>", "1");
		daoModelContent = daoModelContent.replaceAll("#<fieldsContent>", getFieldsContent());
		daoModelContent = daoModelContent.replaceAll("#<getterSetterContent>", getGetterSetterContent());
		return daoModelContent;
	}

	private String getFieldsContent() {
		List<Field> FIELDS = parserInfo.getFields();
		StringBuffer buffer = new StringBuffer();
		String template = "\t/** #<propertyDescription> */\n\tprivate #<javaType> #<propertyName>;\n";
		for (Field field : FIELDS) {
			String property = template.replaceAll("#<javaType>", field.getJavaType());
			property = property.replaceAll("#<propertyName>", field.getPropertyName());
			String description = field.getDescription();
			if (description != null && !description.trim().equals("")) {
				property = property.replaceAll("#<propertyDescription>", description);
			} else {
				property = property.replaceAll("#<propertyDescription>", "");
			}
			buffer.append(property);
		}
		return buffer.toString();
	}

	private String getGetterContent(Field field) {
		String fieldType = field.getFieldType();
		String templateGetter = "\tpublic #<javaType> get#<metholdPostName>() {\n\t\treturn #<propertyName>;\n\t}\n\n";
		if ("BIT".equals(fieldType)) {
			templateGetter = "\tpublic #<javaType> is#<metholdPostName>() {\n\t\treturn #<propertyName>;\n\t}\n\n";
		}
		return getMetholdContent(templateGetter, field);
	}

	private String getGetterSetterContent() {
		StringBuffer buffer = new StringBuffer();
		List<Field> FIELDS = parserInfo.getFields();
		for (Field field : FIELDS) {
			buffer.append(getGetterContent(field));
			buffer.append(getSetterContent(field));
		}
		return buffer.toString();
	}

	private String getMetholdContent(String template, Field field) {
		StringBuffer buffer = new StringBuffer();
		String methold = template.replaceAll("#<javaType>", field.getJavaType());
		String propertyName = field.getPropertyName();
		methold = methold.replaceAll("#<metholdPostName>",
				propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1));
		methold = methold.replaceAll("#<propertyName>", field.getPropertyName());
		buffer.append(methold);
		return buffer.toString();
	}

	private String getSetterContent(Field field) {
		String templateSetter = "\tpublic void set#<metholdPostName>(#<javaType> #<propertyName>) {\n\t\tthis.#<propertyName> = #<propertyName>;\n\t}\n\n";
		return getMetholdContent(templateSetter, field);
	}

	public void makeModel() throws IOException, Exception {
		String generateDaoModelContent = generateDaoModelContent();
		DaoMaker.writeContentToFile(
				DaoMaker.createFile(parserInfo.getModelPackage(), parserInfo.getModelName(), ".java"),
				generateDaoModelContent);
	}
}
