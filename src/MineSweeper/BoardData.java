package MineSweeper;

public class BoardData implements java.io.Serializable {
	
	protected CellData[][] boardData;
	protected int remainBomb;
	protected boolean win;
	protected boolean lose;
	protected int remainTime;
	
	public BoardData(Board input) {
		boardData = input.extractData();
		win = false;
		lose = false;
		remainTime = 1000;
	}
	
	public int getRemainTime() {
		return remainTime;
	}
	
	public void setRemainTime(int time) {
		remainTime = time;
	}
	
	
	public boolean getLose() {
		return lose;
	}
	
	public void setLose() {
		lose = true;;
	}
	
	public boolean getWin() {
		return win;
	}
	
	public void setWin() {
		win = true;;
	}
	
	
	
	public int getRemainBomb() {
		return remainBomb;
	}
	
	public void setRemainBomb(int count) {
		remainBomb = count;
	}
	
	public String toString() {
		return "boardData";
	}
	
	

}
