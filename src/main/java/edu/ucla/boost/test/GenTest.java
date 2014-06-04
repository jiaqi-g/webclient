package edu.ucla.boost.test;

public class GenTest {
	static class TestCase {
		String label;
		String query;
		
		public TestCase(String label, String query) {
			this.label = label;
			this.query = query;
		}
		
		public String getTestMethod() {
			StringBuilder sb = new StringBuilder();
			String label = this.label.toUpperCase();
			
			sb.append("public void test" + label + "() throws Exception {\n");
			sb.append("String q = \"" + query + "\";\n");
			sb.append("String testResult = JdbcClient.getPrettyResult(client.executeSQL(q));\n");		
			sb.append("String correctResult = FileSystem.readFileAsString(Conf.autoTestResultFolder + \"/" + label +"_result\");\n");
			sb.append("assertEquals(correctResult.trim(), testResult.trim());\n");
			sb.append("}");
			
			return sb.toString();
		}
	}
	
	public static void main(String[] args) {
		System.out.println(
		new TestCase("q1", "select * from test").getTestMethod()
				);
	}
}