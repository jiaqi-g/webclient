package edu.ucla.boost.test;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import edu.ucla.boost.test.Conf;
import edu.ucla.wis.common.FileSystem;
import edu.ucla.wis.common.Log;
import edu.ucla.wis.jdbc.JdbcClient;

/**
 * Run sql script without setting up server
 *
 */
public class TestGenUtil {
	
	public List<String> sqls = new ArrayList<String>();
	boolean protect = true;
	
	private void extractSQLs(List<String> lst) {
		StringBuilder sb = new StringBuilder();
		for (String l: lst) {
			if (l.startsWith("#")) {
				continue;
			}
			
			sb.append(l + " ");

			if (l.endsWith(";")) {
				String query = sb.toString().trim();
				Log.log(query);
				sqls.add(query);
				sb = new StringBuilder();
			}
		}
	}
	
	public TestGenUtil() {
		try {
			extractSQLs(FileSystem.readAllLines(Conf.scriptFile));
			//FileSystem.createFile("output.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * db access
	 */
//	public void genResFiles() {
//		if (protect) {
//			throw new RuntimeException("Protect previous running results!");
//		}
//		
//		JdbcClient client = new JdbcClient();
//		
//		for (int i=0; i<sqls.size(); i++) {
//			String label = "Q" + i;
//			String sql = sqls.get(i).replace(";", "");
//			String filename = Conf.autoTestResultFolder + "/" + label + "_result";
//			
//			try {
//				FileSystem.createFile(filename);
//				FileSystem.writeFileAsString(filename, JdbcClient.getPrettyResult(client.executeSQL(sql)));
//
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//	}
	
	public static void genTPCHFinalRes() throws Exception {
		JdbcClient client = new JdbcClient();
		
		//int[] labels = new int[]{1, 3, 5, 6, 7, 8, 9, 10, 11, 12, 14, 19};
		int[] labels = new int[]{1}; //, 3, 5, 6, 7, 8, 9, 10, 11, 12, 14, 19};
		
		for (int i=0; i<labels.length; i++) {
			int index = labels[i];
			String query = FileSystem.readFileAsString(Conf.pathPrefix + "/q" + index + ".hive").replace("\n", " ").split(";")[1];

			String out = "boost_output";
			FileSystem.writeFileAsString(out, 
			JdbcClient.getPrettyResult(
			client.executeSQL(query)));
		}
	}
	
	/**
	 * no db access
	 */
	public void genTestCases() {
		for (int i=0; i<sqls.size(); i++) {
			String label = "Q" + i;
			String sql = sqls.get(i).replace(";", "");
			
			System.out.println(
			new GenTest.TestCase(label, sql).getTestMethod());
		}
	}
	
	public static void main(String[] args) throws Exception {
		JdbcClient.load();
		genTPCHFinalRes();
//		TestGenUtil test = new TestGenUtil();
//		//test.genResFiles();
//		test.genTestCases();
	}
	
}
