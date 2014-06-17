package edu.ucla.boost.cases;

import edu.ucla.boost.test.Conf;
import edu.ucla.boost.common.FileSystem;
import edu.ucla.boost.jdbc.JdbcClient;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * standard test, notice these queries should all be eligible
 * @author victor
 *
 */
public class TpchStandardTest extends TestCase {

	static JdbcClient client;
	static boolean openABM = true;

	String q = "";

	static {
		String q = "";
		client = new JdbcClient();

		//test data
		//		q = "drop table abmtest";
		//		try {
		//			client.executeSQL(q);
		//
		//			q = "create table abmtest (tid int, test_id int, id1 int, id2 int, id3 int, v1 double, v2 double, v3 double) row format delimited fields terminated by \"|\"";
		//			client.executeSQL(q);
		//
		//			q = "LOAD DATA LOCAL INPATH '" + Conf.dropboxPath + "/hive/testing/abmtest.txt' OVERWRITE INTO TABLE abmtest";
		//			client.executeSQL(q);
		//		} catch (SQLException e) {
		//			e.printStackTrace();
		//		}
	}

	public TpchStandardTest(String testName) throws Exception {
		super(testName);
	}

	public static Test suite() {
		return new TestSuite(TpchStandardTest.class);
	}

	public void setABM() throws Exception {
		if (openABM) {
			q = "set hive.abm=true";
		}
		else {
			q = "set hive.abm=false";
		}
		client.executeSQL(q);	
	}

	/**
	 * Basic tests: one level aggregation, sample on lineitem
	 */
	public void testQ1() throws Exception {
		String[] queries = FileSystem.readFileAsString(Conf.tpchQueryFolder + "/q1.hive").split(";");

		//set abm mode
		setABM();

		//set hive.abm.sampled
		q = queries[0].trim();
		client.executeSQL(q);

		//select
		q = queries[1].trim();
		String testResult = JdbcClient.getPrettyResult(client.executeSQL(q));

		System.out.println(testResult);
	}


	public void testQ3() throws Exception {
		String[] queries = FileSystem.readFileAsString(Conf.tpchQueryFolder + "/q3.hive").split(";");

		//set abm mode
		setABM();

		//set hive.abm.sampled
		q = queries[0].trim();
		client.executeSQL(q);

		//select
		q = queries[1].trim();
		String testResult = JdbcClient.getPrettyResult(client.executeSQL(q));

		System.out.println(testResult);
	}

	public void testQ5() throws Exception {
		String[] queries = FileSystem.readFileAsString(Conf.tpchQueryFolder + "/q5.hive").split(";");

		//set abm mode
		setABM();

		//set hive.abm.sampled
		q = queries[0].trim();
		client.executeSQL(q);

		//select
		q = queries[1].trim();
		String testResult = JdbcClient.getPrettyResult(client.executeSQL(q));

		System.out.println(testResult);
	}

	public void testQ6() throws Exception {
		String[] queries = FileSystem.readFileAsString(Conf.tpchQueryFolder + "/q6.hive").split(";");

		//set abm mode
		setABM();

		//set hive.abm.sampled
		q = queries[0].trim();
		client.executeSQL(q);

		//select
		q = queries[1].trim();
		String testResult = JdbcClient.getPrettyResult(client.executeSQL(q));

		System.out.println(testResult);
	}

	public void testQ7() throws Exception {
		String[] queries = FileSystem.readFileAsString(Conf.tpchQueryFolder + "/q7.hive").split(";");

		//set abm mode
		setABM();

		//set hive.abm.sampled
		q = queries[0].trim();
		client.executeSQL(q);

		//select
		q = queries[1].trim();
		String testResult = JdbcClient.getPrettyResult(client.executeSQL(q));

		System.out.println(testResult);
	}

	public void testQ8() throws Exception {
		String[] queries = FileSystem.readFileAsString(Conf.tpchQueryFolder + "/q8.hive").split(";");

		//set abm mode
		setABM();

		//set hive.abm.sampled
		q = queries[0].trim();
		client.executeSQL(q);

		//select
		q = queries[1].trim();
		String testResult = JdbcClient.getPrettyResult(client.executeSQL(q));

		System.out.println(testResult);
	}

	public void testQ9() throws Exception {
		String[] queries = FileSystem.readFileAsString(Conf.tpchQueryFolder + "/q9.hive").split(";");

		//set abm mode
		setABM();

		//set hive.abm.sampled
		q = queries[0].trim();
		client.executeSQL(q);

		//select
		q = queries[1].trim();
		String testResult = JdbcClient.getPrettyResult(client.executeSQL(q));

		System.out.println(testResult);
	}

	public void testQ10() throws Exception {
		String[] queries = FileSystem.readFileAsString(Conf.tpchQueryFolder + "/q10.hive").split(";");

		//set abm mode
		setABM();

		//set hive.abm.sampled
		q = queries[0].trim();
		client.executeSQL(q);

		//select
		q = queries[1].trim();
		String testResult = JdbcClient.getPrettyResult(client.executeSQL(q));

		System.out.println(testResult);
	}

	public void testQ12() throws Exception {
		String[] queries = FileSystem.readFileAsString(Conf.tpchQueryFolder + "/q12.hive").split(";");

		//set abm mode
		setABM();

		//set hive.abm.sampled
		q = queries[0].trim();
		client.executeSQL(q);

		//select
		q = queries[1].trim();
		String testResult = JdbcClient.getPrettyResult(client.executeSQL(q));

		System.out.println(testResult);
	}

	public void testQ14() throws Exception {
		String[] queries = FileSystem.readFileAsString(Conf.tpchQueryFolder + "/q14.hive").split(";");

		//set abm mode
		setABM();

		//set hive.abm.sampled
		q = queries[0].trim();
		client.executeSQL(q);

		//select
		q = queries[1].trim();
		String testResult = JdbcClient.getPrettyResult(client.executeSQL(q));

		System.out.println(testResult);
	}

	/**
	 * complex one, two level group by
	 * @throws Exception
	 */
	public void testQ17() throws Exception {
		String[] queries = FileSystem.readFileAsString(Conf.tpchQueryFolder + "/q17.hive").split(";");

		//set abm mode
		setABM();

		//set hive.abm.sampled
		q = queries[0].trim();
		client.executeSQL(q);

		//select
		q = queries[1].trim();
		String testResult = JdbcClient.getPrettyResult(client.executeSQL(q));

		System.out.println(testResult);
	}

	/**
	 * complex one, two level group by
	 * @throws Exception
	 */
	public void testQ18() throws Exception {
		String[] queries = FileSystem.readFileAsString(Conf.tpchQueryFolder + "/q18.hive").split(";");

		//set abm mode
		setABM();

		//set hive.abm.sampled
		q = queries[0].trim();
		client.executeSQL(q);

		//select
		q = queries[1].trim();
		String testResult = JdbcClient.getPrettyResult(client.executeSQL(q));

		System.out.println(testResult);
	}
	
	public void testQ19() throws Exception {
		String[] queries = FileSystem.readFileAsString(Conf.tpchQueryFolder + "/q19.hive").split(";");

		//set abm mode
		setABM();

		//set hive.abm.sampled
		q = queries[0].trim();
		client.executeSQL(q);

		//select
		q = queries[1].trim();
		String testResult = JdbcClient.getPrettyResult(client.executeSQL(q));

		System.out.println(testResult);
	}
}