package edu.ucla.boost.cases;

import java.sql.SQLException;

import edu.ucla.boost.test.Conf;
import edu.ucla.boost.test.Srv;
import edu.ucla.wis.common.FileSystem;
import edu.ucla.wis.jdbc.JdbcClient;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Should be correct under 1 flags settings
 */
public class Test1d extends TestCase {

	static JdbcClient client;

	static {
		
		try {
			String q = "";
			client = new JdbcClient();

			//1d data
			q = "drop table abmtest";
			client.executeSQL(q);

			q = "create table abmtest (tid int, test_id int, id1 int, id2 int, id3 int, v1 double, v2 double, v3 double) row format delimited fields terminated by \"|\"";
			client.executeSQL(q);

			q = "LOAD DATA LOCAL INPATH '" + Conf.dropboxPath + "/hive/testing/abmtest.txt' OVERWRITE INTO TABLE abmtest";
			client.executeSQL(q);

			//2d data
			q = "drop table abm2dtest";
			client.executeSQL(q);

			q = "create table abm2dtest (tid int, test_id int, id1 int, id2 int, id3 int, v1 double, v2 double, v3 double) row format delimited fields terminated by \"|\"";
			client.executeSQL(q);

			q = "LOAD DATA LOCAL INPATH '" + Conf.dropboxPath + "/hive/testing/abm2dtest.txt' OVERWRITE INTO TABLE abm2dtest";
			client.executeSQL(q);

			//3d data
			q = "drop table abm3dtest";
			client.executeSQL(q);

			q = "create table abm3dtest (tid int, test_id int, id1 int, id2 int, id3 int, v1 double, v2 double, v3 double) row format delimited fields terminated by \"|\"";
			client.executeSQL(q);

			q = "LOAD DATA LOCAL INPATH '" + Conf.dropboxPath + "/hive/testing/abm3dtest.txt' OVERWRITE INTO TABLE abm3dtest";
			client.executeSQL(q);
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Create the test case
	 *
	 * @param testName name of the test case
	 */
	public Test1d(String testName) throws Exception {
		super(testName);
		//FileSystem.createFolder(folder);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(Test1d.class);
	}
	
	/**
	 * set to false in flag
	 * @throws Exception
	 */
//	public void testSmaller() throws Exception {
//		String q = FileSystem.readFileAsString("test/sql_smaller").replace(";", "");
//		String testResult = JdbcClient.getPrettyResult(client.executeSQL(q));
//		String correctResult = FileSystem.readFileAsString(Conf.simpleTestResultFolder + "/smaller_result");
//		assertEquals(correctResult.trim(), testResult.trim());
//		
//		//System.out.println(testResult);
//		
////		List<String> groups = FileSystem.readAllLines("test/group_smaller");
////		System.out.println(
////		Srv.computeSum(groups).toString());
////		System.out.println(
////		Srv.computeCount(groups).toString());
////		System.out.println(
////		Srv.computeAvg(groups).toString());
//		
//		
//		//assertEquals(s.get(0), Srv.computeSum(groups).toString());
//		//assertEquals(s.get(1), Srv.computeCount(groups).toString());
//		//assertEquals(s.get(2), Srv.computeAvg(groups).toString());
//	}
	
	
	/**
	 * set to true in flag
	 * @throws Exception
	 */
	public void testQ22() throws Exception {
		String q = "select srv_sum(v1), srv_count(), srv_avg(v1), cond_merge(t2.c) " +
				"from abmtest t1 join " +
				"(select gen_id() as id_c, srv_greater_than(Srv, v3, ID) as c " +
					"from abmtest a join ( " +
					"select gen_id() as ID, srv_sum(v1) as Srv, cond_merge() from abmtest group by id3) b) t2 " +
					"on t1.tid = t2.id_c";
		String testResult = JdbcClient.getPrettyResult(client.executeSQL(q));
		String correctResult = FileSystem.readFileAsString(Conf.autoTestResultFolder + "/Q22_result");
		assertEquals(correctResult.trim(), testResult.trim());
	}

	public void testQ26() throws Exception {
		String q = "select cond_merge(S.Cond) from (select srv_greater_than(Srv, v3, ID) as Cond from abmtest a join (select gen_id() as ID, srv_sum(v1) as Srv, cond_merge() from abmtest group by id2) b) S";
		String testResult = JdbcClient.getPrettyResult(client.executeSQL(q));
		String correctResult = FileSystem.readFileAsString(Conf.autoTestResultFolder + "/Q26_result");
		assertEquals(correctResult.trim(), testResult.trim());
	}
	public void testQ27() throws Exception {
		String q = "select cond_merge(S.Cond) from (select srv_greater_than(Srv, v2, ID) as Cond from abmtest a join (select gen_id() as ID, srv_avg(v1) as Srv, cond_merge() from abmtest group by id2) b) S";
		String testResult = JdbcClient.getPrettyResult(client.executeSQL(q));
		String correctResult = FileSystem.readFileAsString(Conf.autoTestResultFolder + "/Q27_result");
		assertEquals(correctResult.trim(), testResult.trim());
	}
	
}