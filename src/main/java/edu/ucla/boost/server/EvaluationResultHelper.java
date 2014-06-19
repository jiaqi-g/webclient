package edu.ucla.boost.server;

import java.util.ArrayList;
import java.util.List;

import edu.ucla.boost.common.Time;

public class EvaluationResultHelper {
	List<Object> thead;
	List<List<Object>> tbody;
	StringBuilder sb;
	Time time;
	boolean makeTable = true;
	boolean makeTime = true;
	
	public EvaluationResultHelper(List<Object> head, List<List<Object>> body, Time time) {
		this.thead = head;
		this.tbody = body;
		this.time = time;
		makeTable = true;
		makeTime = true;
	}
	
	public EvaluationResultHelper() {
		makeTable = false;
		makeTime = false;
	}
	
	public void setTable(List<Object> head, List<List<Object>> body) {
		this.thead = head;
		this.tbody = body;
		makeTable = true;
	}
	
	public void setTime(Time time) {
		this.time = time;
		makeTime = true;
	}
	
	private void makeTd(Object obj) {
		sb.append("<td>");
		sb.append(obj);
		sb.append("</td>\n");
	}
	
	private void makeTh(Object obj) {
		sb.append("<th>");
		sb.append(obj);
		sb.append("</th>\n");
	}
	
	private void makeTr(int index, List<Object> row, boolean isHead) {
		sb.append("<tr>\n");
		if (isHead) {
			makeTh("#");
			for (Object obj: row) {
				makeTh(obj);
			}
		} else {
			makeTd(index);
			for (Object obj: row) {
				makeTd(obj);
			}
		}		
		sb.append("</tr>\n");
	}
	
	private void makeHead() {
		sb.append("<thead>\n");
		makeTr(0, thead, true);
		sb.append("</thead>\n");
	}
	
	private void makeBody() {
		sb.append("<tbody>\n");
		for (int i=0; i<tbody.size(); i++) {
			makeTr(i+1, tbody.get(i), false);
		}
		sb.append("</tbody>\n");
	}
	
	private void openDiv(String divClass) {
		sb.append("<div class=\""+ divClass + "\">\n");
	}
	
	private void closeDiv() {
		sb.append("</div>\n");		
	}
	
	public String make() {
		sb = new StringBuilder();
		sb.append("<div>\n");
		
		if (makeTime) {
			openDiv("timeSecDiv");
			openDiv("abmRes");
			sb.append(time.abmTime + "\n");
			closeDiv();
			openDiv("closeRes");
			sb.append(time.closeFormTime + "\n");
			closeDiv();
			openDiv("vanillaRes");
			sb.append(time.vanillaTime + "\n");
			closeDiv();
			closeDiv();
		}
		
		if (makeTable) {
			openDiv("tableSecDiv");
			sb.append("<table class=\"table table-hover\">\n");
			makeHead();
			makeBody();
			sb.append("</table>\n");
			closeDiv();
		}
		
		sb.append("</div>");
		
		return sb.toString();
	}
	
	public static void main(String[] args) {
		List<Object> thead = new ArrayList<Object>();
		thead.add("l_partkey");
		thead.add("revenue");
		thead.add("Quantile");
		
		List<List<Object>> tbody = new ArrayList<List<Object>>();
		List<Object> line1 = new ArrayList<Object>();
		line1.add(63759);
		line1.add(1192143.00);
		line1.add(1168541.03);
		
		List<Object> line2 = new ArrayList<Object>();
		line2.add(63832);
		line2.add(1522863.89);
		line2.add(1459116.42);
		
		tbody.add(line1);
		tbody.add(line2);
		
		System.out.println(new EvaluationResultHelper(thead, tbody, new Time(10.23, 8.42, 20.10)).make() + "\n");
		//System.out.println(new EvaluationResultHelper(thead, tbody).make() + "\n");
		//System.out.println(new EvaluationResultHelper(new Time(10.23, 8.42, 20.10)).make() + "\n");
	}
}
