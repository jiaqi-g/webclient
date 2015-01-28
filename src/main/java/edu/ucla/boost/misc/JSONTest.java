package edu.ucla.boost.misc;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONTest {	
	public static String filePath = "/tmp/json_plan.txt";

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
	}
}