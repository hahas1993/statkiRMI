import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import javax.rmi.ssl.SslRMIClientSocketFactory;

public class Client extends UnicastRemoteObject implements RemoteObserver {

	private static final long serialVersionUID = -5142389190052534862L;
	private static final int PORT = 4242;

	private Client() throws RemoteException {}

    public static void main(String[] args) throws RemoteException {
    	
    	Client client = new Client();

		try {
			setSettings();
			
			Registry registry = LocateRegistry.getRegistry(null, PORT, new SslRMIClientSocketFactory());
			Server server = (Server) registry.lookup("Server");
			Player p = server.searchGame("Hahas");
			System.out.println("gra");
			server.addObserver(client, p.getGameId());
			BoardStates[][] x = new BoardStates[1][1];
			p.setBoard(x);
			server.fillBoard(p);
			System.out.println("plansza");

		} catch (Exception e) {
			System.err.println("Client exception: " + e.toString());
			e.printStackTrace();
		}
    }
    
	private static void setSettings() {
		String pass = "password";
		System.setProperty("javax.net.ssl.debug", "all");
		System.setProperty("javax.net.ssl.keyStore", "D:\\Users\\Pawel\\workspace\\StatkiRMI\\keys\\clientkeystore.jks");
		System.setProperty("javax.net.ssl.keyStorePassword", pass);
		System.setProperty("javax.net.ssl.trustStore", "D:\\Users\\Pawel\\workspace\\StatkiRMI\\keys\\clienttruststore.jks");
		System.setProperty("javax.net.ssl.trustStorePassword", pass);
	}

	@Override
	public void update(Object observable, Object updateMsg) throws RemoteException {
		//InfoMsg msg = (InfoMsg) updateMsg;
		//tutaj u¿ywanie tej ca³ej wiadomoœci do sprawdzania czyja kolej, zaznaczania na mapie ostatniego ruchu itp
		System.out.println(updateMsg);
	}
}