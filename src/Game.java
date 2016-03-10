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
	    
	private int gameId;
	private Player playerA;
	private Player playerB;
	
	public Game(int gameId){
		this.gameId = gameId;
	}
	
	public void fillBoard(Player player) throws RemoteException { //mo¿na tu jeszcze dodaæ walidacje tej tablicy statków, ale chyba starczy u klienta
		Player p = getPlayer(player.getId());
		p.setBoard(player.getBoard());
		if(playerA.getBoard() != null && playerB.getBoard() != null){
			InfoMsg msg = new InfoMsg();
			msg.setMsg("start");
			msg.setTurn((player.getId() + 1) % 2);
			setChanged();
			notifyObservers(msg);
		}
	}
	
	public String move(int playerId, Point point){
		InfoMsg msg = new InfoMsg();
		msg.setPoint(point);
		msg.setPlayerId(playerId);
		Player pa = playerB; // pod pa ten player który wykona³ ruch
		Player pb = playerA;
		if(playerA.getId() == playerId){
			pa = playerA;
			pb = playerB;
		}
		
		if(pb.getBoard()[point.getX()][point.getY()].equals(BoardStates.SHIP)){
			pb.getBoard()[point.getX()][point.getY()] = BoardStates.HIT;
			msg.setHit(true);
			msg.setTurn(pa.getId());
			pb.setHowManyFieldsWithShips(pb.getHowManyFieldsWithShips()-1);
			if(pb.getHowManyFieldsWithShips() == 0){ //koniec gry
				msg.setMsg("end");
			}
			else{
				msg.setMsg("move");
			}
		}
		else{
			pb.getBoard()[point.getX()][point.getY()] = BoardStates.MISHIT;
			msg.setHit(false);
			msg.setTurn(pb.getId());
		}
		
		setChanged();
		notifyObservers(msg);
		return msg.getMsg();
	}
	
	private Player getPlayer(int playerId){
		if(playerA.getId() == playerId)
			return playerA;
		return playerB;
	}
	
	public int getGameId() {
		return gameId;
	}
	public void setGameId(int gameId) {
		this.gameId = gameId;
	}
	public Player getPlayerA() {
		return playerA;
	}
	public void setPlayerA(Player playerA) {
		this.playerA = playerA;
	}
	public Player getPlayerB() {
		return playerB;
	}
	public void setPlayerB(Player playerB) {
		this.playerB = playerB;
	}
}
