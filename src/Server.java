import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Server extends Remote {
	void addObserver(RemoteObserver o, int gameId) throws RemoteException;
	Player searchGame(String nick) throws RemoteException;
	void fillBoard(Player player) throws RemoteException;
	void move(int gameId, int playerId, Point point) throws RemoteException;
}
