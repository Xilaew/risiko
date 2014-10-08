package risiko.main;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import risiko.Engine;

public class MainNio implements Runnable {
	private final int port;
	private ServerSocketChannel ssc;
	private Selector selector;
	private Engine engine;
	private ByteBuffer buf = ByteBuffer.allocate(256);

	MainNio(int port) throws IOException {
		this.port = port;
		this.ssc = ServerSocketChannel.open();
		this.ssc.socket().bind(new InetSocketAddress(port));
		this.ssc.configureBlocking(false);
		this.selector = Selector.open();
		engine = new Engine();
		engine.setBoard(MainNio.class.getResourceAsStream("default.board"));

		this.ssc.register(selector, SelectionKey.OP_ACCEPT);
	}

	@Override
	public void run() {
		try {
			System.out.println("Server starting on port " + this.port);

			Iterator<SelectionKey> iter;
			SelectionKey key;
			while (this.ssc.isOpen()) {
				selector.select();
				iter = this.selector.selectedKeys().iterator();
				while (iter.hasNext()) {
					key = iter.next();
					iter.remove();

					if (key.isAcceptable())
						this.handleAccept(key);
					if (key.isReadable())
						this.handleRead(key);
				}
			}
		} catch (IOException e) {
			System.out.println("IOException, server of port " + this.port
					+ " terminating. Stack trace:");
			e.printStackTrace();
		}
	}

	private void handleAccept(SelectionKey key) throws IOException {
		SocketChannel sc = ((ServerSocketChannel) key.channel()).accept();
		String address = (new StringBuilder(sc.socket().getInetAddress()
				.toString())).append(":").append(sc.socket().getPort())
				.toString();
		sc.configureBlocking(false);
		sc.register(selector, SelectionKey.OP_READ, address);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		engine.getBoard(out);
		out.write("\0".getBytes());
		engine.getState(out);
		out.write("\0".getBytes());
		sc.write(ByteBuffer.wrap(out.toByteArray()));
		// engine.getState(sc.socket().getOutputStream());
		System.out.println("accepted connection from: " + address);
	}

	private void handleRead(SelectionKey key) {
		SocketChannel ch = (SocketChannel) key.channel();
		StringBuilder sb = new StringBuilder();

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
			String info;
			if (read < 0) {
				info = key.attachment() + " left the chat.\n";
				ch.close();
			} else {
				info = key.attachment() + ": " + sb.toString();
			}

			System.out.println(info);
			String msg = sb.toString();
			if (msg.length() > 1) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				ByteArrayInputStream in = new ByteArrayInputStream(sb
						.toString().getBytes());
				engine.executeAction(in, out);
				broadcast(ByteBuffer.wrap(out.toByteArray()));
			}
		} catch (IOException e) {
			key.cancel();
			e.printStackTrace();
		}
	}

	private void broadcast(ByteBuffer msgBuf) throws IOException {
		for (SelectionKey key : selector.keys()) {
			if (key.isValid() && key.channel() instanceof SocketChannel) {
				SocketChannel sch = (SocketChannel) key.channel();
				sch.write(msgBuf);
				msgBuf.rewind();
			}
		}
	}

	public static void main(String[] args) throws IOException {
		MainNio server = new MainNio(10523);
		(new Thread(server)).start();
	}
}