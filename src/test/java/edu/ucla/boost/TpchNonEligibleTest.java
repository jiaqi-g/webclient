package edu.ucla.boost;

import edu.ucla.boost.common.Conf;
import edu.ucla.boost.common.ConfUtil;
import edu.ucla.boost.common.FileSystem;
import edu.ucla.boost.jdbc.JdbcClient;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * 
 * non-eligible queries test for q11, q20
 * @author victor
 *
 */
public class TpchNonEligibleTest extends TestCase {

	static JdbcClient client;
	static boolean openABM = true;

	String q = "";

	static {
		ConfUtil.loadConf(null);
		client = new JdbcClient();
	}

	public TpchNonEligibleTest(String testName) throws Exception {
		super(testName);

	}

	public static Test suite() {
		return new TestSuite(TpchNonEligibleTest.class);
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
	 * Basic tests: one level aggregation, sample on partsupp
	 * Complex functions/comparison of aggregates are not supported in analytical bootstrap mode currently
	 */
	public void testQ11() throws Exception {
		String[] queries = FileSystem.readFileAsString(Conf.tpchQueryFolder + "/q11_not.hive").split(";");

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

	public void testQ20() throws Exception {
		String[] queries = FileSystem.readFileAsString(Conf.tpchQueryFolder + "/q20_not.hive").split(";");

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