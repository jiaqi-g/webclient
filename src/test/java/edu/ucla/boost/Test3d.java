package edu.ucla.boost;

import java.sql.SQLException;
import java.util.List;

import edu.ucla.boost.jdbc.JdbcClient;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Set flags length 0
 */
public class Test3d extends TestCase {

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

			q = "LOAD DATA LOCAL INPATH '/home/victor/Dropbox/hive/testing/abmtest.txt' OVERWRITE INTO TABLE abmtest";
			client.executeSQL(q);

			//2d data
			q = "drop table abm2dtest";
			client.executeSQL(q);

			q = "create table abm2dtest (tid int, test_id int, id1 int, id2 int, id3 int, v1 double, v2 double, v3 double) row format delimited fields terminated by \"|\"";
			client.executeSQL(q);

			q = "LOAD DATA LOCAL INPATH '/home/victor/Dropbox/hive/testing/abm2dtest.txt' OVERWRITE INTO TABLE abm2dtest";
			client.executeSQL(q);

			//3d data
			q = "drop table abm3dtest";
			client.executeSQL(q);

			q = "create table abm3dtest (tid int, test_id int, id1 int, id2 int, id3 int, v1 double, v2 double, v3 double) row format delimited fields terminated by \"|\"";
			client.executeSQL(q);

			q = "LOAD DATA LOCAL INPATH '/home/victor/Dropbox/hive/testing/abm3dtest.txt' OVERWRITE INTO TABLE abm3dtest";
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
	public Test3d(String testName) throws Exception {
		super(testName);
		//FileSystem.createFolder(folder);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(Test3d.class);
	}

	public void testSmaller3d() throws Exception {
		//set 1 flag to false
		String q = "select srv_sum(v1), srv_count(), srv_avg(v1), cond_merge(t2.c1) " +
				"from abm3dtest t1 join (select gen_id() as id_c, " +
				"cond_join(srv_less_than(Srv, v3, ID), srv_less_than(Srv, v1, ID), srv_less_than(Srv, v2, ID)) as c1 " +
				"from abm3dtest a join " +
				"(select gen_id() as ID, srv_sum(v1) as Srv, cond_merge() from abm3dtest group by id1) b " +
				"on a.test_id = b.ID) t2 " +
				"on t1.tid = t2.id_c";

		String testResult = JdbcClient.getPrettyResult(client.executeSQL(q));
		String correctResult = FileSystem.readFileAsString(Conf.simpleTestResultFolder + "/smaller3d_result");
		assertEquals(correctResult.trim(), testResult.trim());
	}
}