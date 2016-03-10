import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
	
public class ServerImpl implements Server {

    @Override
    public void addObserver(RemoteObserver o, int gameId) throws RemoteException {
        Game game = games.get(gameId);
        game.addObserver(o);
    }
	
    
	private Map<Integer, Game> games;
	private int gameId;
	
    public ServerImpl() {
    	games = new HashMap<Integer, Game>();
    	gameId = 0;
    }
	
    public static void main(String args[]) {
	
		try {
			ServerImpl obj = new ServerImpl();
			Server stub = (Server) UnicastRemoteObject.exportObject(obj, 0);

			// Bind the remote object's stub in the registry
			Registry registry = LocateRegistry.getRegistry();
			try{
				registry.bind("Server", stub);
			} catch (AlreadyBoundException abex){
				System.out.println("Server already bound");
			}

			System.err.println("Server ready");
		} catch (Exception e) {
			System.err.println("Server exception: " + e.toString());
			e.printStackTrace();
		}
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
}