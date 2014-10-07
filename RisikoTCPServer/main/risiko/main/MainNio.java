package risiko.main;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;

public class MainNio {

	public static void main(String[] args) {
		try {
			ServerSocketChannel ss = ServerSocketChannel.open();
			ss.bind(new InetSocketAddress(8021));
			System.out.println(ss.isBlocking());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
