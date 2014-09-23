package risiko;

import risiko.gamestate.State;

public final class ExecutorFactory {
	
	public static ActionExecutor getExecutor(State state){
		return new ActionExecutor(state);		
	};

}
