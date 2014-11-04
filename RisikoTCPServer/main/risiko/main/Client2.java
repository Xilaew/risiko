package risiko.main;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import risiko.Engine;
import risiko.GameMonitor;

public class Client2 implements Runnable{
	private Selector selector;
	SocketChannel s;
	private ByteBuffer buf = ByteBuffer.allocate(256);
	private GameMonitor game = new GameMonitor();

	public static void main(String[] args) {
		SocketAddress addr = new InetSocketAddress("localhost", 10523);
		try {
			new Client2(addr).run();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	Client2(SocketAddress address) throws IOException {
		this.selector = Selector.open();
		this.s = SocketChannel.open(address);
		ClientState cs = new ClientState("Client");
		this.s.register(this.selector, SelectionKey.OP_READ, cs);
		this.s.register(selector, SelectionKey.OP_WRITE);
	}

	private void handleRead(SelectionKey key) {
		SocketChannel ch = (SocketChannel) key.channel();
		ClientState cs = (ClientState) key.attachment();
		StringBuilder sb = new StringBuilder();

		sb.append(cs.getBuffer());

		buf.clear();
		int read = 0;
		try {
			while ((read = ch.read(buf)) > 0) {
				buf.flip();
				byte[] bytes = new byte[buf.limit()];
				buf.get(bytes);
				sb.append(new String(bytes));
				buf.clear();
			}

			String msg = sb.toString();
			int i;
			String current;
			while ((i = msg.indexOf('\0')) >= 0) {
				current = msg.substring(0, i);
				msg = msg.substring(i + 1);
				try {

					System.out.println(cs.getName() + ":" + current);

					ByteArrayOutputStream out = new ByteArrayOutputStream();
					ByteArrayInputStream in = new ByteArrayInputStream(
							current.getBytes());
					engine.executeAction(in, out);

					System.out.println(out.toString());

					out.write('\0');
					broadcast(ByteBuffer.wrap(out.toByteArray()));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (read < 0) {
				System.out.println(cs.getName() + " left the chat.\n");
				ch.close();
			}
		} catch (IOException e) {
			key.cancel();
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
