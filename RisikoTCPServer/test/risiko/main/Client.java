package risiko.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

	public static void main(String[] args) throws UnknownHostException,
			IOException {
		Socket s = new Socket("localhost", 8021);
		BufferedReader br = new BufferedReader(new InputStreamReader(
				s.getInputStream()));
		String line;
		while ((line = br.readLine()) != null) {
			System.out.println(line);
		}
		s.close();
	}

}
