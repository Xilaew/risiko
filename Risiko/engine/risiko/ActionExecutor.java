package risiko;

import risiko.actions.Action;
import risiko.actions.AddPlayer;
import risiko.actions.Atack;
import risiko.actions.CoinCards;
import risiko.actions.MoveTroops;
import risiko.actions.RemovePlayer;
import risiko.actions.SetTroops;
import risiko.actions.StartGame;
import risiko.actions.util.actionSwitch;
import risiko.gamestate.GameState;
import risiko.gamestate.State;

/**
 * An executor executes Actions in a Risiko Game. It may have extension points
 * to allow adaptations of the rules or to manipulate the dices.
 * 
 * @author xilaew
 *
 */
public class ActionExecutor extends actionSwitch<Object> {

	private final State state;

	public ActionExecutor(State state) {
		this.state = state;
	}

	/**
	 * Executes the given action and updates the passed state. The board is left
	 * unchanged. The passed action might also be invalidated.
	 * 
	 * @param action
	 */
	public void execute(Action action) {
		System.out.println("executing an Action");
		this.doSwitch(action);
	}

	@Override
	public Object caseAtack(Atack object) {
		// TODO Auto-generated method stub
		return super.caseAtack(object);
	}

	@Override
	public Object caseSetTroops(SetTroops object) {
		// TODO Auto-generated method stub
		return super.caseSetTroops(object);
	}

	@Override
	public Object caseCoinCards(CoinCards object) {
		// TODO Auto-generated method stub
		return super.caseCoinCards(object);
	}

	@Override
	public Object caseMoveTroops(MoveTroops object) {
		// TODO Auto-generated method stub
		return super.caseMoveTroops(object);
	}

	@Override
	public Object caseAddPlayer(AddPlayer object) {
		if (state.getState() == GameState.ACCEPTING_PLAYERS) {
			state.getPlayers().addAll(object.getPlayers());
			return state;
		}
		return null;
	}

	@Override
	public Object caseStartGame(StartGame object) {
		// TODO Auto-generated method stub
		return super.caseStartGame(object);
	}

	@Override
	public Object caseRemovePlayer(RemovePlayer object) {
		// TODO Auto-generated method stub
		return super.caseRemovePlayer(object);
	}
}
