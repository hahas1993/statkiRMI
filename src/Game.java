import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Observable;
import java.util.Observer;

public class Game extends Observable implements GameService {
	 private class WrappedObserver implements Observer, Serializable {
	        private static final long serialVersionUID = 1L;
	        private RemoteObserver ro = null;

	        public WrappedObserver(RemoteObserver ro) {
	            this.ro = ro;
	        }

	        @Override
	        public void update(Observable o, Object arg) {
	            try {
	                ro.update(o.toString(), arg);
	            } catch (RemoteException e) {
	                System.out
	                        .println("Remote exception removing observer:" + this);
	                o.deleteObserver(this);
	            }
	        }
	    }

	@Override
	public void addObserver(RemoteObserver o) throws RemoteException {
		WrappedObserver mo = new WrappedObserver(o);
		addObserver(mo);
		System.out.println("Added observer to game:" + gameId);
	}
	
	public void notifyObservers(){
		setChanged();
		notifyObservers("ustawil statki");
	}
	    
	    
	private int gameId;
	private Player PlayerA;
	private Player PlayerB;
	
	public Game(int gameId){
		this.gameId = gameId;
	}
	
	public void fillBoard(Player player) throws RemoteException { //mo¿na tu jeszcze dodaæ walidacje tej tablicy statków, ale chyba starczy u klienta
		Player p = getPlayer(player.getId());
		p.setBoard(player.getBoard());
		notifyObservers();
	}
	
	public Player getPlayer(int playerId){
		if(PlayerA.getId() == playerId)
			return PlayerA;
		return PlayerB;
	}
	
	public int getGameId() {
		return gameId;
	}
	public void setGameId(int gameId) {
		this.gameId = gameId;
	}
	public Player getPlayerA() {
		return PlayerA;
	}
	public void setPlayerA(Player playerA) {
		PlayerA = playerA;
	}
	public Player getPlayerB() {
		return PlayerB;
	}
	public void setPlayerB(Player playerB) {
		PlayerB = playerB;
	}
}
