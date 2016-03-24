package utils;
import java.io.Serializable;

public class InfoMsg implements Serializable {
	private static final long serialVersionUID = 1L;
	private int playerId;
	private int turn; //kolejka gracza którego id == turn
	private String msg; //jak == "start" to poczatek gry, jak =="move" to player o id == playerId ruszy³ siê na pole point, jak =="end" to wygra³ gracz o id==playerId
	private Point point;
	private boolean hit; //true jak w danym ruchu trafi³ statek
	
	public int getPlayerId() {
		return playerId;
	}
	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}
	public int getTurn() {
		return turn;
	}
	public void setTurn(int turn) {
		this.turn = turn;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Point getPoint() {
		return point;
	}
	public void setPoint(Point point) {
		this.point = point;
	}
	public boolean isHit() {
		return hit;
	}
	public void setHit(boolean hit) {
		this.hit = hit;
	}
	@Override
	public String toString() {
		return "InfoMsg [playerId=" + playerId + ", turn=" + turn + ", msg=" + msg + ", point=" + point + ", hit=" + hit
				+ "]";
	}
}
