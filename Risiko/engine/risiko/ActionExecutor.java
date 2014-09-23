package risiko;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.util.EList;

import risiko.actions.Action;
import risiko.actions.AddPlayer;
import risiko.actions.Atack;
import risiko.actions.CoinCards;
import risiko.actions.MoveTroops;
import risiko.actions.RemovePlayer;
import risiko.actions.SetTroops;
import risiko.actions.StartGame;
import risiko.actions.util.actionSwitch;
import risiko.board.Board;
import risiko.board.Country;
import risiko.board.boardFactory;
import risiko.board.boardPackage;
import risiko.gamestate.CountryState;
import risiko.gamestate.GameState;
import risiko.gamestate.Player;
import risiko.gamestate.State;
import risiko.gamestate.stateFactory;

/**
 * An executor executes Actions in a Risiko Game. It may have extension points
 * to allow adaptations of the rules or to manipulate the dices.
 * 
 * @author xilaew
 *
 */
public class ActionExecutor extends actionSwitch<Object> {

	private final State state;
	private final Board board;

	public ActionExecutor(Board board, State state) {
		this.state = state;
		this.board = board;
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
		if (state.getState() == GameState.ACCEPTING_PLAYERS
				&& state.getPlayers().size() >= 2) {
			state.setState(GameState.INITIAL_TROOP_DISTRIBUTION);
			state.setTroopsToSet(1);
			state.setTurn(state.getPlayers().get(0));
			distributeCountriesAmongPlayers();
			return state;
		}
		return super.caseStartGame(object);
	}

	private void distributeCountriesAmongPlayers() {
		List<Country> toDistribute = new LinkedList<Country>();
		toDistribute.addAll(board.getCountries());
		while (toDistribute.size() / state.getPlayers().size()
				* state.getPlayers().size() != toDistribute.size()) {
			toDistribute.add(null);
		}
		Collections.shuffle(toDistribute);
		Iterator<Player> playerIt = state.getPlayers().listIterator(
				toDistribute.size());
		for (Country c : toDistribute) {
			CountryState countryState = stateFactory.eINSTANCE
					.createCountryState();
			countryState.setTroops(1);
			countryState.setCountry(c);
			countryState.setPlayer(playerIt.next());
			state.getCountryState().put(c, countryState);
		}
	}

	@Override
	public Object caseRemovePlayer(RemovePlayer object) {
		if (state.getState() == GameState.ACCEPTING_PLAYERS) {
			state.getPlayers().removeAll(object.getPlayers());
			return state;
		}
		return super.caseRemovePlayer(object);
	}
}
