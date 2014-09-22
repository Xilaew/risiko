package risiko;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class EngineTest {

	public static void main(String[] args) throws FileNotFoundException,
			IOException, RuntimeException {
		Engine engine = new Engine();
		File boardFile = new File("examples/board.board");
		engine.setBoard(new FileInputStream(boardFile));
		engine.getBoard(System.out);
	}
}
