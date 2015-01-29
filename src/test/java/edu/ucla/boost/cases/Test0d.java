package edu.ucla.boost.cases;

import java.sql.SQLException;

import edu.ucla.boost.test.Conf;
import edu.ucla.wis.common.FileSystem;
import edu.ucla.wis.jdbc.JdbcClient;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Should be correct under any flags settings
 */
public class Test0d extends TestCase {

	static JdbcClient client;

	static {
		String q = "";
		client = new JdbcClient();

		//test data
		q = "drop table abmtest";
		try {
			client.executeSQL(q);

			q = "create table abmtest (tid int, test_id int, id1 int, id2 int, id3 int, v1 double, v2 double, v3 double) row format delimited fields terminated by \"|\"";
			client.executeSQL(q);

			q = "LOAD DATA LOCAL INPATH '" + Conf.dropboxPath + "/hive/testing/abmtest.txt' OVERWRITE INTO TABLE abmtest";
			client.executeSQL(q);
			
//			q = "set hive.abm=false";
//			client.executeSQL(q);
			
			//q = "set hive.abm.sample.size=12";
			//client.executeSQL(q);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the test case
	 *
	 * @param testName name of the test case
	 */
	public Test0d(String testName) throws Exception {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(Test0d.class);
	}

	//output object address
	//	public void testQ0() throws Exception {
	//		String q = "select lin_sum(tid), cond_print(cond_merge()) from abmtest";
	//		String testResult = JdbcClient.getPrettyResult(client.executeSQL(q));
	//		String correctResult = FileSystem.readFileAsString(Conf.autoTestResultFolder + "/Q0_result");
	//		assertEquals(correctResult.trim(), testResult.trim());
	//	}
	
	public void testQ1() throws Exception {
		String q = "select cond_print(cond_merge()) from abmtest";
		String testResult = JdbcClient.getPrettyResult(client.executeSQL(q));
		String correctResult = FileSystem.readFileAsString(Conf.autoTestResultFolder + "/Q1_result");
		assertEquals(correctResult.trim(), testResult.trim());
	}
	public void testQ2() throws Exception {
		String q = "select cond_print(cond_merge()) from emptytest";
		String testResult = JdbcClient.getPrettyResult(client.executeSQL(q));
		String correctResult = FileSystem.readFileAsString(Conf.autoTestResultFolder + "/Q2_result");
		assertEquals(correctResult.trim(), testResult.trim());
	}
	public void testQ3() throws Exception {
		String q = "select srv_print(srv_sum(v1)), cond_print(cond_merge()) from abmtest";
		String testResult = JdbcClient.getPrettyResult(client.executeSQL(q));
		String correctResult = FileSystem.readFileAsString(Conf.autoTestResultFolder + "/Q3_result");
		assertEquals(correctResult.trim(), testResult.trim());
	}
	public void testQ4() throws Exception {
		String q = "select srv_print(srv_count()), cond_print(cond_merge()) from abmtest";
		String testResult = JdbcClient.getPrettyResult(client.executeSQL(q));
		String correctResult = FileSystem.readFileAsString(Conf.autoTestResultFolder + "/Q4_result");
		assertEquals(correctResult.trim(), testResult.trim());
	}
	public void testQ5() throws Exception {
		String q = "select srv_print(srv_avg(v1)), cond_print(cond_merge()) from abmtest";
		String testResult = JdbcClient.getPrettyResult(client.executeSQL(q));
		String correctResult = FileSystem.readFileAsString(Conf.autoTestResultFolder + "/Q5_result");
		assertEquals(correctResult.trim(), testResult.trim());
	}
	public void testQ6() throws Exception {
		String q = "select srv_print(case_sum(v1)), cond_print(cond_merge()) from abmtest";
		String testResult = JdbcClient.getPrettyResult(client.executeSQL(q));
		String correctResult = FileSystem.readFileAsString(Conf.autoTestResultFolder + "/Q6_result");
		assertEquals(correctResult.trim(), testResult.trim());
	}
	public void testQ7() throws Exception {
		String q = "select srv_print(case_count()), cond_print(cond_merge()) from abmtest";
		String testResult = JdbcClient.getPrettyResult(client.executeSQL(q));
		String correctResult = FileSystem.readFileAsString(Conf.autoTestResultFolder + "/Q7_result");
		assertEquals(correctResult.trim(), testResult.trim());
	}
	public void testQ8() throws Exception {
		String q = "select srv_print(case_avg(v1)), cond_print(cond_merge()) from abmtest";
		String testResult = JdbcClient.getPrettyResult(client.executeSQL(q));
		String correctResult = FileSystem.readFileAsString(Conf.autoTestResultFolder + "/Q8_result");
		assertEquals(correctResult.trim(), testResult.trim());
	}
	public void testQ9() throws Exception {
		String q = "select srv_print(srv_sum(v1)), srv_print(srv_count()), srv_print(srv_avg(v1)), cond_print(cond_merge()) from abmtest";
		String testResult = JdbcClient.getPrettyResult(client.executeSQL(q));
		String correctResult = FileSystem.readFileAsString(Conf.autoTestResultFolder + "/Q9_result");
		assertEquals(correctResult.trim(), testResult.trim());
	}
	public void testQ10() throws Exception {
		String q = "select srv_print(srv_sum(v2)), srv_print(srv_count()), srv_print(srv_avg(v2)), " +
				"cond_print(cond_merge()) from abmtest";
		String testResult = JdbcClient.getPrettyResult(client.executeSQL(q));
		String correctResult = FileSystem.readFileAsString(Conf.autoTestResultFolder + "/Q10_result");
		assertEquals(correctResult.trim(), testResult.trim());
	}
	public void testQ11() throws Exception {
		String q = "select srv_print(srv_sum(v3)), srv_print(srv_count()), srv_print(srv_avg(v3)), " +
				"cond_print(cond_merge()) from abmtest";
		String testResult = JdbcClient.getPrettyResult(client.executeSQL(q));
		String correctResult = FileSystem.readFileAsString(Conf.autoTestResultFolder + "/Q11_result");
		assertEquals(correctResult.trim(), testResult.trim());
	}
	public void testQ12() throws Exception {
		String q = "select srv_print(case_sum(v1)), srv_print(case_count()), srv_print(case_avg(v1)), " +
				"cond_print(cond_merge()) from abmtest";
		String testResult = JdbcClient.getPrettyResult(client.executeSQL(q));
		String correctResult = FileSystem.readFileAsString(Conf.autoTestResultFolder + "/Q12_result");
		assertEquals(correctResult.trim(), testResult.trim());
	}
	public void testQ13() throws Exception {
		String q = "select srv_print(case_sum(v2)), srv_print(case_count()), srv_print(case_avg(v2)), " +
				"cond_print(cond_merge()) from abmtest";
		String testResult = JdbcClient.getPrettyResult(client.executeSQL(q));
		String correctResult = FileSystem.readFileAsString(Conf.autoTestResultFolder + "/Q13_result");
		assertEquals(correctResult.trim(), testResult.trim());
	}
	public void testQ14() throws Exception {
		String q = "select srv_print(case_sum(v3)), srv_print(case_count()), srv_print(case_avg(v3)), " +
				"cond_print(cond_merge()) from abmtest";
		String testResult = JdbcClient.getPrettyResult(client.executeSQL(q));
		String correctResult = FileSystem.readFileAsString(Conf.autoTestResultFolder + "/Q14_result");
		assertEquals(correctResult.trim(), testResult.trim());
	}
	public void testQ15() throws Exception {
		String q = "select gen_id(), srv_print(srv_sum(v1)), cond_print(cond_merge()) from abmtest";
		String testResult = JdbcClient.getPrettyResult(client.executeSQL(q));
		String correctResult = FileSystem.readFileAsString(Conf.autoTestResultFolder + "/Q15_result");
		assertEquals(correctResult.trim(), testResult.trim());
	}
	public void testQ16() throws Exception {
		String q = "select gen_id() as ID, srv_print(srv_sum(v1)) as Srv, cond_print(cond_merge()) from abmtest group by id2";
		String testResult = JdbcClient.getPrettyResult(client.executeSQL(q));
		String correctResult = FileSystem.readFileAsString(Conf.autoTestResultFolder + "/Q16_result");
		assertEquals(correctResult.trim(), testResult.trim());
	}
	public void testQ17() throws Exception {
		String q = "select srv_print(srv_greater_than_f(Srv, v1, ID)) from abmtest a join (select gen_id() as ID, srv_sum(v1) as Srv, cond_print(cond_merge()) from abmtest group by id2) b";
		String testResult = JdbcClient.getPrettyResult(client.executeSQL(q));
		String correctResult = FileSystem.readFileAsString(Conf.autoTestResultFolder + "/Q17_result");
		assertEquals(correctResult.trim(), testResult.trim());
	}
	public void testQ18() throws Exception {
		String q = "select srv_print(srv_greater_than_f(Srv, v3, ID)) from abmtest a join (select gen_id() as ID, srv_sum(v1) as Srv, cond_print(cond_merge()) from abmtest group by id2) b";
		String testResult = JdbcClient.getPrettyResult(client.executeSQL(q));
		String correctResult = FileSystem.readFileAsString(Conf.autoTestResultFolder + "/Q18_result");
		assertEquals(correctResult.trim(), testResult.trim());
	}
	public void testQ19() throws Exception {
		String q = "select srv_print(srv_greater_than(Srv, v3, ID)) from abmtest a join (select gen_id() as ID, srv_sum(v1) as Srv, cond_print(cond_merge()) from abmtest group by id2) b";
		String testResult = JdbcClient.getPrettyResult(client.executeSQL(q));
		String correctResult = FileSystem.readFileAsString(Conf.autoTestResultFolder + "/Q19_result");
		assertEquals(correctResult.trim(), testResult.trim());
	}
	public void testQ20() throws Exception {
		String q = "select srv_print(srv_less_than_f(Srv, v3, ID)) from abmtest a join (select gen_id() as ID, srv_sum(v1) as Srv, cond_print(cond_merge()) from abmtest group by id2) b";
		String testResult = JdbcClient.getPrettyResult(client.executeSQL(q));
		String correctResult = FileSystem.readFileAsString(Conf.autoTestResultFolder + "/Q20_result");
		assertEquals(correctResult.trim(), testResult.trim());
	}
	public void testQ21() throws Exception {
		String q = "select srv_print(srv_less_than(Srv, v3, ID)) from abmtest a join (select gen_id() as ID, srv_sum(v1) as Srv, cond_print(cond_merge()) from abmtest group by id2) b";
		String testResult = JdbcClient.getPrettyResult(client.executeSQL(q));
		String correctResult = FileSystem.readFileAsString(Conf.autoTestResultFolder + "/Q21_result");
		assertEquals(correctResult.trim(), testResult.trim());
	}
	public void testQ23() throws Exception {
		String q = "select srv_print(srv_less_than(Srv, v2, ID)) from abmtest a join (select gen_id() as ID, srv_sum(v1) as Srv, cond_print(cond_merge()) from abmtest group by id2) b";
		String testResult = JdbcClient.getPrettyResult(client.executeSQL(q));
		String correctResult = FileSystem.readFileAsString(Conf.autoTestResultFolder + "/Q23_result");
		assertEquals(correctResult.trim(), testResult.trim());
	}
	public void testQ24() throws Exception {
		String q = "select srv_print(srv_less_than(Srv, v2, ID)) from abmtest a join (select gen_id() as ID, srv_avg(v1) as Srv, cond_print(cond_merge()) from abmtest group by id2) b";
		String testResult = JdbcClient.getPrettyResult(client.executeSQL(q));
		String correctResult = FileSystem.readFileAsString(Conf.autoTestResultFolder + "/Q24_result");
		assertEquals(correctResult.trim(), testResult.trim());
	}
	public void testQ25() throws Exception {
		String q = "select srv_print(srv_less_than(Srv, v2, ID)) from abmtest a join (select gen_id() as ID, srv_count() as Srv, cond_print(cond_merge()) from abmtest group by id2) b";
		String testResult = JdbcClient.getPrettyResult(client.executeSQL(q));
		String correctResult = FileSystem.readFileAsString(Conf.autoTestResultFolder + "/Q25_result");
		assertEquals(correctResult.trim(), testResult.trim());
	}
}