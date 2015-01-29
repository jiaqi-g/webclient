package edu.ucla.wis.common;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class ConfUtil {

	private static Field[] fields = Conf.class.getDeclaredFields();

	/**
	 * return true is conf set successfully
	 * @param key
	 * @param val
	 * @return
	 * @throws Exception
	 */
	public static boolean setConf(String key, String val) throws Exception {
		boolean success = false;
		try {
			for (Field f: fields) {
				f.setAccessible(true);
				if (f.getName().toLowerCase().equals(key.toLowerCase())) {
					Class<?> fieldClass = f.getType();
					if (fieldClass == Boolean.class) {
						f.set(null, Boolean.parseBoolean(val));
					} else if (fieldClass == Integer.class) {
						f.set(null, Integer.parseInt(val));
					} else if (fieldClass == Double.class) {
						f.set(null, Double.parseDouble(val));
					} else if (fieldClass == String.class) {
						f.set(null, val);
					}
					success = true;
					break;
				}
			}
		}
		catch (NumberFormatException e) {
		}

		return success;
	}

	public static void loadConf(Path path) {
		try {
			if (path == null) {
				Log.warn("Using config file in project path");
				path = Paths.get("config");
			}

			try (Scanner scanner =  new Scanner(path, StandardCharsets.UTF_8.name())) {
				while (scanner.hasNextLine()){
					String line = scanner.nextLine().trim();
					if (line.startsWith("#") || line.equals("")) {
						continue;
					}
					String[] strarr = line.split("=");
					String key = strarr[0].trim();
					String val = strarr[1].trim();

					setConf(key, val);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void printArgs() {
		//System.out.println("[Conf Args]");
		try {
			for (Field f: fields) {
				System.out.println(f.getName() + ": " + f.get(null));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		ConfUtil.loadConf(null);
		ConfUtil.printArgs();
	}
}