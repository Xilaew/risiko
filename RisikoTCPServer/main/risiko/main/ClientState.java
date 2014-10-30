package risiko.main;

public class ClientState {
	
	private final String name;
	private String buffer = "";
	
	ClientState(String name){
		this.name=name;
	}
	
	String getName(){
		return name;
	}

	String getBuffer() {
		return buffer;
	}

	void setBuffer(String buffer) {
		if (buffer==null){
			throw new IllegalArgumentException("buffer may not be null; enter an empty String instead");
		}
		this.buffer = buffer;
	}
	
}
