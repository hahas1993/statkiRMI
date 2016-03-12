import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.activation.Activatable;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Map;

import javax.rmi.ssl.SslRMIClientSocketFactory;
import javax.rmi.ssl.SslRMIServerSocketFactory;
	
public class ServerImpl implements Server {

	private static final int PORT = 4242;

    @Override
    public void addObserver(RemoteObserver o, int gameId) throws RemoteException {
        Game game = games.get(gameId);
        game.addObserver(o);
    }
    
	private Map<Integer, Game> games;
	private int gameId;

    public ServerImpl() throws RemoteException {
    	games = new HashMap<Integer, Game>();
    	gameId = 0;
    }
	
    public static void main(String args[]) {
	
		try {
			setSettings();
			
			ServerImpl obj = new ServerImpl();
			SslRMIClientSocketFactory csf = new SslRMIClientSocketFactory();
			SslRMIServerSocketFactory ssf = new SslRMIServerSocketFactory();
			
			Activatable.exportObject(obj, null, PORT, csf, ssf);
			
			Registry registry = LocateRegistry.createRegistry(PORT, csf, ssf);

			try{
				registry.bind("Server", obj);
			} catch (AlreadyBoundException abex){
				registry.unbind("Server");
				registry.bind("Server", obj);
			}

			System.err.println("Server ready");
		} catch (Exception e) {
			System.err.println("Server exception: " + e.toString());
			e.printStackTrace();
		}
    }
    
	private static void setSettings() {
		String pass = "password";
		System.setProperty("javax.net.ssl.debug", "all");
		System.setProperty("javax.net.ssl.keyStore", "D:\\Users\\Pawel\\workspace\\StatkiRMI\\keys\\serverkeystore.jks");
		System.setProperty("javax.net.ssl.keyStorePassword", pass);
		System.setProperty("javax.net.ssl.trustStore", "D:\\Users\\Pawel\\workspace\\StatkiRMI\\keys\\servertruststore.jks");
		System.setProperty("javax.net.ssl.trustStorePassword", pass);
	}

	@Override
	public Player searchGame(String nick) throws RemoteException {
		for(Game game : games.values()){
			if(game.getPlayerB() == null){
				Player player = new Player(nick, 1, game.getGameId());
				game.setPlayerB(player);
				return player;
			}
		}
		Game game = makeNewGame(nick);
		games.put(game.getGameId(), game);
		return game.getPlayerA();
	}
	
	private Game makeNewGame(String nick){
		Game game = new Game(gameId++);
		Player player = new Player(nick, 0, game.getGameId());
		game.setPlayerA(player);
		return game;
	}

	@Override
	public void fillBoard(Player player) throws RemoteException { //mo¿na tu jeszcze dodaæ walidacje tej tablicy statków, ale chyba starczy u klienta
		Game game = games.get(player.getGameId());
		game.fillBoard(player);
	}

	@Override
	public void move(int gameId, int playerId, Point point) throws RemoteException {
		Game game = games.get(gameId);
		if(game == null) 
			return;
		String msg = game.move(playerId, point);
		if(msg.equals("end")){
			games.remove(gameId);
		}
	}
}