package edu.ucla.boost;

import java.util.List;

import edu.ucla.boost.jdbc.JdbcClient;
import edu.ucla.boost.test.Srv;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {

	JdbcClient client;

	/**
	 * Create the test case
	 *
	 * @param testName name of the test case
	 */
	public AppTest(String testName) {
		super(testName);
		client = new JdbcClient();
		//FileSystem.createFolder(folder);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(AppTest.class);
	}

	//	public void testGreater() throws Exception {
	//		//set flag to true
	//
	//		List<String> s = FileSystem.readAllLines(Conf.testFileName);
	//		List<String> groups = FileSystem.readAllLines("test/group_greater");
	//
	//		assertEquals(s.get(0), Srv.computeSum(groups).toString());
	//		assertEquals(s.get(1), Srv.computeCount(groups).toString());
	//		assertEquals(s.get(2), Srv.computeAvg(groups).toString());
	//
	//		//boolean res = true;
	//		//assertTrue(res);
	//	}
	//
	//	public void testSmaller() throws Exception {
	//		//set flag to false
	//
	//		List<String> s = FileSystem.readAllLines(Conf.testFileName);
	//		List<String> groups = FileSystem.readAllLines("test/group_smaller");
	//
	//		assertEquals(s.get(0), Srv.computeSum(groups).toString());
	//		assertEquals(s.get(1), Srv.computeCount(groups).toString());
	//		assertEquals(s.get(2), Srv.computeAvg(groups).toString());
	//	}

//	public void testQ0() throws Exception {
//		String q = "select lin_sum(tid), cond_merge() from abmtest";
//		String testResult = JdbcClient.getPrettyResult(client.executeSQL(q));
//		String correctResult = FileSystem.readFileAsString(Conf.autoTestResultFolder + "/Q0_result");
//		assertEquals(testResult.trim(), correctResult.trim());
//	}
	
	public void testQ1() throws Exception {
		String q = "select cond_merge() from abmtest";
		String testResult = JdbcClient.getPrettyResult(client.executeSQL(q));
		String correctResult = FileSystem.readFileAsString(Conf.autoTestResultFolder + "/Q1_result");
		assertEquals(testResult.trim(), correctResult.trim());
	}
	public void testQ2() throws Exception {
		String q = "select cond_merge() from emptytest";
		String testResult = JdbcClient.getPrettyResult(client.executeSQL(q));
		String correctResult = FileSystem.readFileAsString(Conf.autoTestResultFolder + "/Q2_result");
		assertEquals(testResult.trim(), correctResult.trim());
	}
	public void testQ3() throws Exception {
		String q = "select srv_sum(v1), cond_merge() from abmtest";
		String testResult = JdbcClient.getPrettyResult(client.executeSQL(q));
		String correctResult = FileSystem.readFileAsString(Conf.autoTestResultFolder + "/Q3_result");
		assertEquals(testResult.trim(), correctResult.trim());
	}
	public void testQ4() throws Exception {
		String q = "select srv_count(), cond_merge() from abmtest";
		String testResult = JdbcClient.getPrettyResult(client.executeSQL(q));
		String correctResult = FileSystem.readFileAsString(Conf.autoTestResultFolder + "/Q4_result");
		assertEquals(testResult.trim(), correctResult.trim());
	}
	public void testQ5() throws Exception {
		String q = "select srv_avg(v1), cond_merge() from abmtest";
		String testResult = JdbcClient.getPrettyResult(client.executeSQL(q));
		String correctResult = FileSystem.readFileAsString(Conf.autoTestResultFolder + "/Q5_result");
		assertEquals(testResult.trim(), correctResult.trim());
	}
	public void testQ6() throws Exception {
		String q = "select case_sum(v1), cond_merge() from abmtest";
		String testResult = JdbcClient.getPrettyResult(client.executeSQL(q));
		String correctResult = FileSystem.readFileAsString(Conf.autoTestResultFolder + "/Q6_result");
		assertEquals(testResult.trim(), correctResult.trim());
	}
	public void testQ7() throws Exception {
		String q = "select case_count(), cond_merge() from abmtest";
		String testResult = JdbcClient.getPrettyResult(client.executeSQL(q));
		String correctResult = FileSystem.readFileAsString(Conf.autoTestResultFolder + "/Q7_result");
		assertEquals(testResult.trim(), correctResult.trim());
	}
	public void testQ8() throws Exception {
		String q = "select case_avg(v1), cond_merge() from abmtest";
		String testResult = JdbcClient.getPrettyResult(client.executeSQL(q));
		String correctResult = FileSystem.readFileAsString(Conf.autoTestResultFolder + "/Q8_result");
		assertEquals(testResult.trim(), correctResult.trim());
	}
	public void testQ9() throws Exception {
		String q = "select srv_sum(v1), srv_count(), srv_avg(v1), cond_merge() from abmtest";
		String testResult = JdbcClient.getPrettyResult(client.executeSQL(q));
		String correctResult = FileSystem.readFileAsString(Conf.autoTestResultFolder + "/Q9_result");
		assertEquals(testResult.trim(), correctResult.trim());
	}
	public void testQ10() throws Exception {
		String q = "select srv_sum(v2), srv_count(), srv_avg(v2), cond_merge() from abmtest";
		String testResult = JdbcClient.getPrettyResult(client.executeSQL(q));
		String correctResult = FileSystem.readFileAsString(Conf.autoTestResultFolder + "/Q10_result");
		assertEquals(testResult.trim(), correctResult.trim());
	}
	public void testQ11() throws Exception {
		String q = "select srv_sum(v3), srv_count(), srv_avg(v3), cond_merge() from abmtest";
		String testResult = JdbcClient.getPrettyResult(client.executeSQL(q));
		String correctResult = FileSystem.readFileAsString(Conf.autoTestResultFolder + "/Q11_result");
		assertEquals(testResult.trim(), correctResult.trim());
	}
	public void testQ12() throws Exception {
		String q = "select case_sum(v1), case_count(), case_avg(v1), cond_merge() from abmtest";
		String testResult = JdbcClient.getPrettyResult(client.executeSQL(q));
		String correctResult = FileSystem.readFileAsString(Conf.autoTestResultFolder + "/Q12_result");
		assertEquals(testResult.trim(), correctResult.trim());
	}
	public void testQ13() throws Exception {
		String q = "select case_sum(v2), case_count(), case_avg(v2), cond_merge() from abmtest";
		String testResult = JdbcClient.getPrettyResult(client.executeSQL(q));
		String correctResult = FileSystem.readFileAsString(Conf.autoTestResultFolder + "/Q13_result");
		assertEquals(testResult.trim(), correctResult.trim());
	}
	public void testQ14() throws Exception {
		String q = "select case_sum(v3), case_count(), case_avg(v3), cond_merge() from abmtest";
		String testResult = JdbcClient.getPrettyResult(client.executeSQL(q));
		String correctResult = FileSystem.readFileAsString(Conf.autoTestResultFolder + "/Q14_result");
		assertEquals(testResult.trim(), correctResult.trim());
	}
	public void testQ15() throws Exception {
		String q = "select gen_id(), srv_sum(v1), cond_merge() from abmtest";
		String testResult = JdbcClient.getPrettyResult(client.executeSQL(q));
		String correctResult = FileSystem.readFileAsString(Conf.autoTestResultFolder + "/Q15_result");
		assertEquals(testResult.trim(), correctResult.trim());
	}
	public void testQ16() throws Exception {
		String q = "select gen_id() as ID, srv_sum(v1) as Srv, cond_merge() from abmtest group by id2";
		String testResult = JdbcClient.getPrettyResult(client.executeSQL(q));
		String correctResult = FileSystem.readFileAsString(Conf.autoTestResultFolder + "/Q16_result");
		assertEquals(testResult.trim(), correctResult.trim());
	}
	public void testQ17() throws Exception {
		String q = "select srv_greater_than_f(Srv, v1, ID) from abmtest a join (select gen_id() as ID, srv_sum(v1) as Srv, cond_merge() from abmtest group by id2) b";
		String testResult = JdbcClient.getPrettyResult(client.executeSQL(q));
		String correctResult = FileSystem.readFileAsString(Conf.autoTestResultFolder + "/Q17_result");
		assertEquals(testResult.trim(), correctResult.trim());
	}
	public void testQ18() throws Exception {
		String q = "select srv_greater_than_f(Srv, v3, ID) from abmtest a join (select gen_id() as ID, srv_sum(v1) as Srv, cond_merge() from abmtest group by id2) b";
		String testResult = JdbcClient.getPrettyResult(client.executeSQL(q));
		String correctResult = FileSystem.readFileAsString(Conf.autoTestResultFolder + "/Q18_result");
		assertEquals(testResult.trim(), correctResult.trim());
	}
	public void testQ19() throws Exception {
		String q = "select srv_greater_than(Srv, v3, ID) from abmtest a join (select gen_id() as ID, srv_sum(v1) as Srv, cond_merge() from abmtest group by id2) b";
		String testResult = JdbcClient.getPrettyResult(client.executeSQL(q));
		String correctResult = FileSystem.readFileAsString(Conf.autoTestResultFolder + "/Q19_result");
		assertEquals(testResult.trim(), correctResult.trim());
	}
	public void testQ20() throws Exception {
		String q = "select srv_less_than_f(Srv, v3, ID) from abmtest a join (select gen_id() as ID, srv_sum(v1) as Srv, cond_merge() from abmtest group by id2) b";
		String testResult = JdbcClient.getPrettyResult(client.executeSQL(q));
		String correctResult = FileSystem.readFileAsString(Conf.autoTestResultFolder + "/Q20_result");
		assertEquals(testResult.trim(), correctResult.trim());
	}
	public void testQ21() throws Exception {
		String q = "select srv_less_than(Srv, v3, ID) from abmtest a join (select gen_id() as ID, srv_sum(v1) as Srv, cond_merge() from abmtest group by id2) b";
		String testResult = JdbcClient.getPrettyResult(client.executeSQL(q));
		String correctResult = FileSystem.readFileAsString(Conf.autoTestResultFolder + "/Q21_result");
		assertEquals(testResult.trim(), correctResult.trim());
	}
	public void testQ22() throws Exception {
		String q = "select srv_sum(v1), srv_count(), srv_avg(v1), cond_merge(t2.c) from abmtest t1 join (select gen_id() as id_c, srv_greater_than(Srv, v3, ID) as c from abmtest a join ( select gen_id() as ID, srv_sum(v1) as Srv, cond_merge() from abmtest group by id3) b) t2 on t1.tid = t2.id_c";
		String testResult = JdbcClient.getPrettyResult(client.executeSQL(q));
		String correctResult = FileSystem.readFileAsString(Conf.autoTestResultFolder + "/Q22_result");
		assertEquals(testResult.trim(), correctResult.trim());
	}
	public void testQ23() throws Exception {
		String q = "select srv_less_than(Srv, v2, ID) from abmtest a join (select gen_id() as ID, srv_sum(v1) as Srv, cond_merge() from abmtest group by id2) b";
		String testResult = JdbcClient.getPrettyResult(client.executeSQL(q));
		String correctResult = FileSystem.readFileAsString(Conf.autoTestResultFolder + "/Q23_result");
		assertEquals(testResult.trim(), correctResult.trim());
	}
	public void testQ24() throws Exception {
		String q = "select srv_less_than(Srv, v2, ID) from abmtest a join (select gen_id() as ID, srv_avg(v1) as Srv, cond_merge() from abmtest group by id2) b";
		String testResult = JdbcClient.getPrettyResult(client.executeSQL(q));
		String correctResult = FileSystem.readFileAsString(Conf.autoTestResultFolder + "/Q24_result");
		assertEquals(testResult.trim(), correctResult.trim());
	}
	public void testQ25() throws Exception {
		String q = "select srv_less_than(Srv, v2, ID) from abmtest a join (select gen_id() as ID, srv_count() as Srv, cond_merge() from abmtest group by id2) b";
		String testResult = JdbcClient.getPrettyResult(client.executeSQL(q));
		String correctResult = FileSystem.readFileAsString(Conf.autoTestResultFolder + "/Q25_result");
		assertEquals(testResult.trim(), correctResult.trim());
	}
	public void testQ26() throws Exception {
		String q = "select cond_merge(S.Cond) from (select srv_greater_than(Srv, v3, ID) as Cond from abmtest a join (select gen_id() as ID, srv_sum(v1) as Srv, cond_merge() from abmtest group by id2) b) S";
		String testResult = JdbcClient.getPrettyResult(client.executeSQL(q));
		String correctResult = FileSystem.readFileAsString(Conf.autoTestResultFolder + "/Q26_result");
		assertEquals(testResult.trim(), correctResult.trim());
	}
	public void testQ27() throws Exception {
		String q = "select cond_merge(S.Cond) from (select srv_greater_than(Srv, v2, ID) as Cond from abmtest a join (select gen_id() as ID, srv_avg(v1) as Srv, cond_merge() from abmtest group by id2) b) S";
		String testResult = JdbcClient.getPrettyResult(client.executeSQL(q));
		String correctResult = FileSystem.readFileAsString(Conf.autoTestResultFolder + "/Q27_result");
		assertEquals(testResult.trim(), correctResult.trim());
	}
}