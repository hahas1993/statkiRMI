import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class Client extends UnicastRemoteObject implements RemoteObserver {

	private static final long serialVersionUID = -5142389190052534862L;

	private Client() throws RemoteException {}

    public static void main(String[] args) throws RemoteException {

    	Client client = new Client();
		boolean end = false;

		Scanner sc = new Scanner(System.in);

		String host = (args.length < 1) ? null : args[0];
		try {
			Registry registry = LocateRegistry.getRegistry(host);
			Server stub = (Server) registry.lookup("Server");
			System.out.println("obserwer");
			Player p = stub.searchGame("Hahas");
			System.out.println("gra");
			stub.addObserver(client, p.getGameId());
			
			stub.fillBoard(p);
			System.out.println("plansza");

		} catch (Exception e) {
			System.err.println("Client exception: " + e.toString());
			e.printStackTrace();
		}
    }

	@Override
	public void update(Object observable, Object updateMsg) throws RemoteException {
		//InfoMsg msg = (InfoMsg) updateMsg;
		System.out.println("lala");
		//tutaj u¿ywanie tej ca³ej wiadomoœci do sprawdzania czyja kolej, zaznaczania na mapie ostatniego ruchu itp
		System.out.println(updateMsg);
	}
}