package com.cary.marker.toolkit;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.cary.marker.field.ParserInfo;
import com.cary.marker.field.TableEntry;
import com.cary.marker.templete.DaoInterfaceMaker;
import com.cary.marker.templete.DaoMaker;
import com.cary.marker.templete.DaoModelMaker;

public class Generator {

	private static String getCellValue(HSSFRow row, int cellnum) {
		HSSFCell cell = row.getCell(cellnum);
		if (cell == null) {
			return null;
		}
		String value = cell.getStringCellValue();
		if (StringUtils.isBlank(value)) {
			return null;
		}
		return StringUtils.trimToNull(value);
	}

	private static Connection getConnection() throws IOException, ClassNotFoundException, SQLException {
		Properties properties = new Properties();
		properties.load(ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties"));
		String driverClass = properties.getProperty("oracle.jdbc.driverClass");
		String url = properties.getProperty("oracle.jdbc.url");
		Class.forName(driverClass);
		Connection connection = DriverManager.getConnection(url, properties);
		if (connection == null) {
			throw new RuntimeException("connection is null, Please check the driver settings!");
		}
		return connection;
	}

	/**
	 * 读取table信息
	 * 
	 * @author cary
	 * @return
	 * @throws IOException
	 */
	private static List<TableEntry> readTableEntries() throws IOException {
		List<TableEntry> entries = new LinkedList<TableEntry>();
		// HSSFWorkbook workbook = new HSSFWorkbook(new
		// FileInputStream("tables.xls"));
		FileInputStream in = new FileInputStream(ClassLoader.getSystemResource("tables.xls").getFile());
		HSSFWorkbook workbook = new HSSFWorkbook(in);
		HSSFSheet sheet = workbook.getSheet("tables");
		int lastRowNum = sheet.getLastRowNum();
		for (int i = 1; i <= lastRowNum; i++) {
			HSSFRow row = sheet.getRow(i);
			if (row == null) {
				break;
			}
			String tableName = getCellValue(row, 0);
			if (tableName == null) {
				break;
			}
			String sequenceName = getCellValue(row, 1);
			String packageName = getCellValue(row, 2);
			TableEntry entry = new TableEntry();
			entry.setTableName(tableName);
			entry.setSequenceName(sequenceName);
			entry.setPackageName(packageName);
			entries.add(entry);
		}
		return entries;
	}

	public static void main(String[] args) throws IOException, Exception {
		Connection connection = getConnection();
		List<TableEntry> entries = readTableEntries();
		System.out.println("There is(are) " + entries.size() + " table(s) to generate!");
		for (TableEntry entry : entries) {
			String packageName = entry.getPackageName();
			String tableName = entry.getTableName();
			String sequenceName = entry.getSequenceName();
			tableName = tableName.toUpperCase();
			if (sequenceName != null) {
				sequenceName = sequenceName.toUpperCase();
			}
			ParserInfo parserInfo = new ParserInfo(connection, packageName, tableName, sequenceName);

			DaoMaker daoMaker = new DaoMaker(parserInfo);
			daoMaker.makeDao(tableName, packageName);

			DaoInterfaceMaker daoInterfaceMaker = new DaoInterfaceMaker(parserInfo);
			daoInterfaceMaker.makeInterface();

			DaoModelMaker daoModelMaker = new DaoModelMaker(parserInfo);
			daoModelMaker.makeModel();
			System.out.println("Generate table " + tableName + " success...");
		}
		connection.close();
		System.out.println("Generator has done!");
	}
}
