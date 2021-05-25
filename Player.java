
public class Player {
	GameColors playerColor;
	String name;
	int scores;
	boolean notHuman;
	boolean isWinner;
	
	public Player(String name,GameColors playerColor,boolean notHuman) {
		this.name = name;
		this.playerColor = playerColor;
		this.notHuman = notHuman;
		scores = 0;
		isWinner = false;
	}
	
	public GameColors getGameColor() {
		return playerColor;
	}
	public void addScores(int amount) {
		scores+=amount;
	}
	public int getScores() {
		return scores;
	}
	
	public String getName() {
		return name;
	}
	
	public void setWinner() {
		isWinner = true;
	}
	
	public boolean isWinner() {
		return isWinner;
	}

}
