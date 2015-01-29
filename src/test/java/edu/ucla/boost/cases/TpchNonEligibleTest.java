package edu.ucla.boost.cases;

import edu.ucla.boost.test.Conf;
import edu.ucla.wis.common.FileSystem;
import edu.ucla.wis.jdbc.JdbcClient;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * 
 * non-eligible queries test
 * @author victor
 *
 */
public class TpchNonEligibleTest extends TestCase {

	static JdbcClient client;
	static boolean openABM = true;

	String q = "";

	static {
		client = new JdbcClient();
	}

	public TpchNonEligibleTest(String testName) throws Exception {
		super(testName);

	}

	public static Test suite() {
		return new TestSuite(TpchNonEligibleTest.class);
	}
	
}