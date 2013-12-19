package com.cary.marker.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class MarkerUtil {

	public static File[] getFiles(String folder) throws UnsupportedEncodingException {
		String projectFolder = getProjectFolder(MarkerUtil.class);
		File fieldsFolder = new File(projectFolder + File.separator + folder);
		File[] fieldsFiles = fieldsFolder.listFiles();
		return fieldsFiles;
	}

	public static String getProjectFolder(Class<?> clazz) throws UnsupportedEncodingException {
		String classFolder = new File(clazz.getProtectionDomain().getCodeSource().getLocation().getPath()).getPath();
		classFolder = URLDecoder.decode(classFolder, "UTF-8");
		int index = classFolder.indexOf("target");
		return classFolder.substring(0, index);
	}

}
