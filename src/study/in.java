package study;

import java.io.FileInputStream;
import java.io.IOException;

public class in {

	public static void main(String args[]) throws IOException {
		FileInputStream fis = new FileInputStream("C:\\Users\\86176\\Desktop\\encryted.txt");
		int b;
		while ((b = fis.read()) != -1) {
			System.out.print((char) b);
		}
		fis.close();
	}
}
