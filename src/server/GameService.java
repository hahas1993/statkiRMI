package server;
import java.rmi.Remote;
import java.rmi.RemoteException;

import utils.RemoteObserver;

public interface GameService extends Remote {
	void addObserver(RemoteObserver o) throws RemoteException;
}
