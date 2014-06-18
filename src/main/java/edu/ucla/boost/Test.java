package edu.ucla.boost;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.util.Progressable;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * start hadoop-1.0.4 (hdfs) first before running this class
 * @author victor
 *
 */
public class Test {

	public static String hdfsURI = "hdfs://localhost:9000";
	public static String filePath = "/tmp/json_plan.txt";
	public static String hadoopHome = "/home/victor/hadoop-1.0.4/";
	//"/s2013/batch/table.html";

	public static void writeHDFS(String out) throws IOException, URISyntaxException {
		Configuration configuration = new Configuration();
		FileSystem hdfs = FileSystem.get(new URI(hdfsURI), configuration);
		Path file = new Path(hdfsURI + filePath);
		if (hdfs.exists(file)) {
			hdfs.delete(file, true);
		}

		OutputStream os = hdfs.create(file,
				new Progressable() {
			public void progress() {
				System.out.println("Writing ...");
				//out.println("...bytes written: [ "+bytesWritten+" ]");
			}
		});

		BufferedWriter br = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
		br.write(out);

		br.close();
		hdfs.close();

		System.out.println("Write finished!");
	}

	public static String readHDFS() {
		try {
			Path file = new Path(hdfsURI + filePath);
			Configuration conf = new Configuration();
			conf.addResource(new Path(hadoopHome + "conf/core-site.xml"));
			FileSystem fs = FileSystem.get(conf);

			BufferedReader br=new BufferedReader(new InputStreamReader(fs.open(file)));

			StringBuilder res = new StringBuilder();
			String line = br.readLine();
			while (line != null){
				res.append(line);
				line = br.readLine();
			}
			return res.toString();
		} catch(Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static void testJson() throws JSONException {
		JSONObject obj = new JSONObject();
		obj.put("name", "Top Level");
		obj.put("parent", "null");

		List<JSONObject> lst = new ArrayList<JSONObject>();
		JSONObject obj1 = new JSONObject();
		obj1.put("name", "Top Level");
		obj1.put("parent", "null");
		lst.add(obj1);

		JSONObject obj2 = new JSONObject();
		obj2.put("name", "Top Level");
		obj2.put("parent", "null");
		lst.add(obj2);

		obj.put("children", lst);

		System.out.println(obj);
	}

	public static void main(String[] args) throws Exception {
		//writeHDFS("a great success!");
		//System.out.println("Read out: " + readHDFS());
	}

}