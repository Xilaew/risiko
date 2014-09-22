package risiko;

import risiko.actions.Action;
import risiko.board.Board;
import risiko.gamestate.State;

/**
 * An executor executes Actions in a Risiko Game. It may have extension points
 * to allow adaptations of the rules or to manipulate the dices.
 * 
 * @author xilaew
 *
 */
public class Executor {

	/**
	 * Executes the given action and updates the passed state. The board is left
	 * unchanged. The passed action might also be invalidated.
	 * 
	 * @param board
	 * @param state
	 * @param action
	 */
	public void execute(final Board board, State state, Action action) {
		System.out.println("executing an Action");
		// TODO Auto-generated method stub
	}

}
