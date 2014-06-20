package edu.ucla.boost.server;

import java.util.ArrayList;
import java.util.List;

import edu.ucla.boost.common.Time;
import edu.ucla.boost.math.NormalDist;

public class EvaluationResultHelper {
	List<Object> thead;
	List<List<Object>> tbody;
	List<NormalDist> dists;
	
	StringBuilder sb;
	Time time;
	boolean makeTable = false;
	boolean makeTime = false;
	
	public EvaluationResultHelper() {
	}
	
	public void setTable(List<Object> head, List<List<Object>> body, List<NormalDist> dists) {
		this.thead = head;
		this.tbody = body;
		this.dists = dists;
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
	
	private void makeTr(int index, List<Object> row, boolean isHead, NormalDist dist) {
		if (isHead) {
			sb.append("<tr>\n");
			makeTh("#");
			for (Object obj: row) {
				makeTh(obj);
			}
			sb.append("</tr>\n");
		} else {
			sb.append("<tr class=\"tpchrow\" href=\"#\" title=\"" + dist.toString() + "\">\n");
			makeTd(index);
			for (Object obj: row) {
				makeTd(obj);
			}
			sb.append("</tr>\n");
		}
	}
	
	private void makeHead() {
		sb.append("<thead>\n");
		makeTr(0, thead, true, null);
		sb.append("</thead>\n");
	}
	
	private void makeBody() {
		sb.append("<tbody>\n");
		for (int i=0; i<tbody.size(); i++) {
			makeTr(i+1, tbody.get(i), false, dists.get(i));
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
		
		EvaluationResultHelper eval = new EvaluationResultHelper();
		
		List<NormalDist> dists = new ArrayList<NormalDist>();
		for (int i=0; i<tbody.size(); i++) {
			dists.add(new NormalDist(0,1));
		}
		eval.setTable(thead, tbody, dists);
		
		System.out.println(eval.make() + "\n");
		//System.out.println(new EvaluationResultHelper(thead, tbody).make() + "\n");
		//System.out.println(new EvaluationResultHelper(new Time(10.23, 8.42, 20.10)).make() + "\n");
	}
}
