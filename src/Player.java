import java.io.Serializable;

public class Player implements Serializable {
	private static final long serialVersionUID = 1L;
	private int gameId;
	private int id;
	private int howManyFieldsWithShips;
	private String nick;
	private BoardStates[][] board;
	
	public Player(String nick, int id, int gameId){
		this.nick = nick;
		this.id = id;
		this.gameId = gameId;
		this.howManyFieldsWithShips = 20;
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getHowManyFieldsWithShips() {
		return howManyFieldsWithShips;
	}

	public void setHowManyFieldsWithShips(int howManyFieldsWithShips) {
		this.howManyFieldsWithShips = howManyFieldsWithShips;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public BoardStates[][] getBoard() {
		return board;
	}

	public void setBoard(BoardStates[][] board) {
		this.board = board;
	}
}
