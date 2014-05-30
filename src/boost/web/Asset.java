package boost.web;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class Asset {
	public static InputStream open(String uri) {
		InputStream is = null;
		try {
			is = new FileInputStream(uri);
			//is.close(); 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return is;
	}
}