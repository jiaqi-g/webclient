package edu.ucla.boost.misc;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * start hadoop-1.0.4 (hdfs) first before running this class
 * @author victor
 *
 */
public class HDFSTest {
	
	public static String hdfsURI = "hdfs://localhost:9000";
	public static String filePath = "/tmp/json_plan.txt";
	public static String hadoopHome = "/home/victor/hadoop-1.0.4/";
	//"/s2013/batch/table.html";

	@Deprecated
	public static void writeHDFS(String out) throws IOException, URISyntaxException {
		/*
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

		System.out.println("Write finished!");*/
	}

	@Deprecated
	public static String readHDFS() {
		return null;
		/*
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
				line = br.readLine()
			}
			return res.toString();
		} catch(Exception e) {
			e.printStackTrace();
		}

		return null;*/
	}
	
	public static void main(String[] args) throws Exception {
		//writeHDFS("a great success!");
		//System.out.println("Read out: " + readHDFS());
	}
}