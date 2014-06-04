package edu.ucla.boost.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.ucla.boost.Conf;
import edu.ucla.boost.FileSystem;
import edu.ucla.boost.Log;
import edu.ucla.boost.jdbc.JdbcClient;

/**
 * Run sql script without setting up server
 * 
 * @author victor
 *
 */
public class TestGenUtil {
	
	public static final String scriptFile = "/home/victor/Dropbox/hive/testing/query.sql";
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
			extractSQLs(FileSystem.readAllLines(scriptFile));
			//FileSystem.createFile("output.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * db access
	 */
	public void genResFiles() {
		if (protect) {
			throw new RuntimeException("Protect previous running results!");
		}
		
		JdbcClient client = new JdbcClient();
		
		for (int i=0; i<sqls.size(); i++) {
			String label = "Q" + i;
			String sql = sqls.get(i).replace(";", "");
			String filename = Conf.autoTestResultFolder + "/" + label + "_result";
			
			try {
				FileSystem.createFile(filename);
				FileSystem.writeFileAsString(filename, JdbcClient.getPrettyResult(client.executeSQL(sql)));

			} catch (Exception e) {
				e.printStackTrace();
			}
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
	
	public static void main(String[] args) {
		JdbcClient.load();
		TestGenUtil test = new TestGenUtil();
		//test.genResFiles();
		test.genTestCases();
	}
	
}
