package edu.ucla.boost.test;

public class Conf {
	public static final String dropboxPath = "/home/victor/Dropbox";
	public static final String scriptFile = dropboxPath + "/hive/testing/query.sql";
	public static final String pathPrefix = dropboxPath + "/hive/TPC-H_on_Hive/tpch/eligible_queries/corrected"; //_input
	public static final String tpchQueryFolder = dropboxPath + "/hive/TPC-H_on_Hive/tpch/eligible_queries/corrected";
	
	public static final String autoTestResultFolder = "test/auto";
	public static final String simpleTestResultFolder = "test/simple";
}