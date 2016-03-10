import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GameService extends Remote {
	void addObserver(RemoteObserver o) throws RemoteException;
}
