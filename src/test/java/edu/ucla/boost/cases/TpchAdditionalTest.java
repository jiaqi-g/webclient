package edu.ucla.boost.cases;

import edu.ucla.boost.test.Conf;
import edu.ucla.boost.common.FileSystem;
import edu.ucla.boost.jdbc.JdbcClient;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * additional test for q17, q18 inner group by
 * @author victor
 *
 */
public class TpchAdditionalTest extends TestCase {

	static JdbcClient client;
	static boolean openABM = true;

	String q = "";

	static {
		client = new JdbcClient();
	}

	public TpchAdditionalTest(String testName) throws Exception {
		super(testName);
	}

	public static Test suite() {
		return new TestSuite(TpchAdditionalTest.class);
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

	public void testQ17_inner() throws Exception {
		String[] queries = FileSystem.readFileAsString(Conf.tpchQueryFolder + "/q17_inner.hive").split(";");

		//set abm mode
		q = "set hive.abm=false";
		client.executeSQL(q);

		//set hive.abm.sampled
		q = queries[0].trim();
		client.executeSQL(q);

		//select
		q = queries[1].trim();
		String testResult = JdbcClient.getPrettyResult(client.executeSQL(q));

		System.out.println(testResult);
	}

	public void testQ18_inner() throws Exception {
		String[] queries = FileSystem.readFileAsString(Conf.tpchQueryFolder + "/q18_inner.hive").split(";");

		//set abm mode
		q = "set hive.abm=false";
		client.executeSQL(q);

		//set hive.abm.sampled
		q = queries[0].trim();
		client.executeSQL(q);

		//select
		q = queries[1].trim();
		String testResult = JdbcClient.getPrettyResult(client.executeSQL(q));

		System.out.println(testResult);
	}
}