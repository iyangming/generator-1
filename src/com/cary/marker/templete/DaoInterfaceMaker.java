package com.cary.marker.templete;

import java.io.IOException;

import com.cary.marker.field.ParserInfo;


public class DaoInterfaceMaker {

	private ParserInfo parserInfo;

	public DaoInterfaceMaker(ParserInfo parserInfo) {
		this.parserInfo = parserInfo;
	}

	private String generateDaoInterfaceContent() throws IOException {
		String templateFileContent = DaoMaker.getTemplateFileContent("JavaTemplate.xml");
		String daoInterfaceContent = templateFileContent.replaceAll("#<modelPackage>", parserInfo.getModelPackage());
		daoInterfaceContent = daoInterfaceContent.replaceAll("#<tableComment>", parserInfo.getTableComment());
		daoInterfaceContent = daoInterfaceContent.replaceAll("#<tableName>", parserInfo.getTableName());
		daoInterfaceContent = daoInterfaceContent.replaceAll("#<daoPackage>", parserInfo.getDaoPackage());
		daoInterfaceContent = daoInterfaceContent.replaceAll("#<daoName>", parserInfo.getDaoName());
		daoInterfaceContent = daoInterfaceContent.replaceAll("#<modelName>", parserInfo.getModelName());
		return daoInterfaceContent.trim();
	}

	public void makeInterface() throws Exception, IOException {
		DaoMaker.writeContentToFile(DaoMaker.createFile(parserInfo.getDaoPackage(), parserInfo.getDaoName(), ".java"), generateDaoInterfaceContent());
	}

}
