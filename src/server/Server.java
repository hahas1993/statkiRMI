package server;
import java.rmi.Remote;
import java.rmi.RemoteException;

import utils.Player;
import utils.Point;
import utils.RemoteObserver;

public interface Server extends Remote {
	void addObserver(RemoteObserver o, int gameId) throws RemoteException;
	Player searchGame(String nick) throws RemoteException;
	void fillBoard(Player player) throws RemoteException;
	void move(int gameId, int playerId, Point point) throws RemoteException;
}
