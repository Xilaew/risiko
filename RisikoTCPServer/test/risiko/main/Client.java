package risiko.main;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import risiko.GameMonitor;

public class Client implements Runnable{
	private GameMonitor game = new GameMonitor();
	SocketChannel s;
	private ByteBuffer buf = ByteBuffer.allocate(256);

	public static void main(String[] args) {
		new Thread(new Client()).start();
	}

	public void run() {
		try {
			s = SocketChannel.open(new InetSocketAddress("localhost", 10523));
			s.configureBlocking(true);
			
			int read;
			StringBuilder sb = new StringBuilder();
			while ((read = s.read(buf)) > 0) {
				s.configureBlocking(false);
				buf.flip();
				byte[] bytes = new byte[buf.limit()];
				buf.get(bytes);
				sb.append(new String(bytes));
				buf.clear();
			}
			String msg;
			if (read < 0) {
				msg = "connection closed left the chat.\n";
				s.close();
			} else {
				msg = sb.toString();
			}
			System.out.println(msg);

			int i;
			String current;
			while ((i=msg.indexOf('\0'))>=0){
				current = msg.substring(0, i);
				msg = msg.substring(i+1);
				game.setBoard(new ByteArrayInputStream(current.getBytes()));
			}
			System.out.println();
			System.out.println();
			game.getBoard(System.out);
			s.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
