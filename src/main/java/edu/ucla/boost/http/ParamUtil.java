package edu.ucla.boost.http;

import java.util.Map;

import edu.ucla.boost.Confidence;
import edu.ucla.boost.Log;
import edu.ucla.boost.Param;
import edu.ucla.boost.Quantile;

public class ParamUtil {
	
	private Map<String, String> paramMap;
	
	public ParamUtil(Map<String, String> paramMap) {
		this.paramMap = paramMap;
	}
	
	public boolean doVariance() {
		return Boolean.parseBoolean(paramMap.get(Param.VARIANCE));
	}
	
	public boolean doConfidence() {
		return Boolean.parseBoolean(paramMap.get(Param.CONFIDENCE));
	}
	
	public boolean doExist() {
		return Boolean.parseBoolean(paramMap.get(Param.EXIST));
	}
	
	public boolean doQuantile() {
		return Boolean.parseBoolean(paramMap.get(Param.QUANTILE));
	}
	
	public Quantile getQuantile() {
		return new Quantile(paramMap.get(Param.QUANTILE_VALUE));
	}
	
	public Confidence getConfidence() {
		return new Confidence(paramMap.get(Param.CONFIDENCE_FROM), paramMap.get(Param.CONFIDENCE_TO));
	}
	
	/**
	 * Return null if the format is invalid or violates some restrictions.
	 * @return
	 */
	public String getSelectQueryString() {
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
	public String getExplainQueryString() {
		String query = paramMap.get(Param.QUERY).trim();
		query = query.replace(";", "");
		if (!query.startsWith("select")) {
			Log.log("only support select query for print plan");
			return null;
		}
		query = "explain " + query;
		return query;
	}
	
}