package edu.ucla.boost.http;

import java.util.Map;

import edu.ucla.boost.Log;
import edu.ucla.boost.Param;

public class ParamUtil {
	
	/**
	 * Return null if the format is invalid or violates some restrictions.
	 * @return
	 */
	public static String getSelectQueryString(Map<String, String> paramMap) {
		String query = paramMap.get(Param.QUERY).trim();
		query = query.replace(";", "");
		// TODO: query should contain limit in order to be executed now
		if (!query.contains("limit")) {
			Log.log("should contain limit else won't be executed");
			return null;
		}
		return query;
	}
	
	/**
	 * Return null if the format is invalid or violates some restrictions.
	 * @return
	 */
	public static String getExplainQueryString(Map<String, String> paramMap) {
		String query = paramMap.get(Param.QUERY).trim();
		query = query.replace(";", "");
		query = "explain " + query;
		if (!query.startsWith("select")) {
			Log.log("only support select query for print plan");
			return null;
		}
		return query;
	}
	
}