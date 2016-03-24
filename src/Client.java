import java.awt.Color;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import javax.rmi.ssl.SslRMIClientSocketFactory;
import javax.swing.JButton;

public class Client extends UnicastRemoteObject implements RemoteObserver {

    private static final long serialVersionUID = -5142389190052534862L;
    private static final int PORT = 4242;
    private static int count=0;
    private static GUI g;
    private static Player p;
    private static Server server;
    
    private Client() throws RemoteException, InterruptedException {
        g=new GUI();
    }

    public static void main(String[] args) throws RemoteException, InterruptedException {

        Client client = new Client();
        try {
            setSettings();
            Registry registry = LocateRegistry.getRegistry(null, PORT, new SslRMIClientSocketFactory());
            server = (Server) registry.lookup("Server");
          //  g=new GUI();     
            while (g.isFlag() == false){
                  Thread.sleep(1000);
                //Czy podano nick
            }
            p = server.searchGame(g.getPlayerName());
            System.out.println(p.getNick());
          //  System.out.println(p.getGameId());
            server.addObserver(client, p.getGameId());
            BoardStates[][] boardStates = new BoardStates[10][10];
            g.msgAreaSetText("Ustaw po kolei statki:\n1 x 4-masztowiec,\n2 x 3-masztowiec,\n3 x 2-masztowiec, \n4 x 1-masztowiec.\n");
            while (!g.isSaveClicked()) {  
              //  System.out.println(g.isSaveClicked());
                Thread.sleep(100);
            }
            g.msgAreaSetText("Statki rozstawione.\n");
            JButton[][] playerBoard = g.getPlayerGrid();
            while (g.isShipsSet() == false){
                Thread.sleep(1000);
                setShips(boardStates,playerBoard,g);
            } 
            p.setBoard(boardStates);
            //Jeżeli statki dobrze ustawione
            server.fillBoard(p);
            if(count<1){
                g.msgAreaSetText("Oczekiwanie na rozstawienie statków przez przeciwnika....\n");
            }
           // System.out.println("plansza");

        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }

    private static void setSettings() {
        String pass = "password";
        System.setProperty("javax.net.ssl.debug", "all");
        //	System.setProperty("javax.net.ssl.keyStore", "D:\\Users\\Pawel\\workspace\\StatkiRMI\\keys\\clientkeystore.jks");
        System.setProperty("javax.net.ssl.keyStore", "C:\\Users\\Klis\\Desktop\\Aneta - Studia Magisterskie\\Semestr I\\RSI\\PS\\PS2\\Statki\\statkiRMI\\keys\\clientkeystore.jks");
        System.setProperty("javax.net.ssl.keyStorePassword", pass);
        System.setProperty("javax.net.ssl.trustStore", "C:\\Users\\Klis\\Desktop\\Aneta - Studia Magisterskie\\Semestr I\\RSI\\PS\\PS2\\Statki\\statkiRMI\\keys\\clienttruststore.jks");
        //	System.setProperty("javax.net.ssl.trustStore", "D:\\Users\\Pawel\\workspace\\StatkiRMI\\keys\\clienttruststore.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", pass);
    }
    
    //Sprawdzanie czy dobrze rozstawione statki
    private static void setShips(BoardStates[][] bs, JButton[][] playerBoard, GUI g){
        for(int i=0; i<10; i++){
                for(int j=0; j<10; j++){
                    if (playerBoard[i][j].getBackground().equals(Color.GREEN)){
                             bs[i][j] = BoardStates.SHIP;
                    if (playerBoard[i][j].getBackground().equals(Color.WHITE)){
                             bs[i][j] = BoardStates.EMPTY;
                    }
                }
            }        
        }
       g.setShipsSet(true);
    }
    @Override
    public void update(Object observable, Object updateMsg) throws RemoteException {
        count++;
        if(count==1){
            g.msgAreaSetText("Gra rozpoczęta.\n");
        }
        InfoMsg msg = (InfoMsg) updateMsg;
        if(msg.getTurn() == msg.getPlayerId() && p.getId()==msg.getPlayerId()){
            g.setPlayerTurn();
        }
        else{
            g.setComTurn();
        }
     //   System.out.println(msg.toString());
        //tutaj uzywanie tej calej wiadomosci do sprawdzania czyja kolej, zaznaczania na mapie ostatniego ruchu itp
        System.out.println(updateMsg);
    }
    
    public static void hitField(int x, int y) throws RemoteException{
        Point point = new Point();
        point.setX(x);
        point.setY(y);
        System.out.println(x+","+y);
        server.move(p.getGameId(),p.getId(),point);
    }

}
