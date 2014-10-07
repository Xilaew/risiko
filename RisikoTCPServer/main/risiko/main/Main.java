package risiko.main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import risiko.Engine;

public class Main {
	static Engine engine = new Engine();
	
	Main () throws IOException, RuntimeException{
		engine.setBoard(getClass().getResourceAsStream("default.board"));
	}

	public static void main(String[] args) throws IOException {
		ServerSocket ss = new ServerSocket(8021);
		Socket cs;
		while ((cs=ss.accept()) != null ){
			new Thread (new MainThread(cs,engine)).start();
		}
		ss.close();
	}

}
