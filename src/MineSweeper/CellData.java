package MineSweeper;

import java.awt.Dimension;
import java.awt.Image;

import javax.swing.ImageIcon;

public class CellData implements java.io.Serializable {
	
	protected boolean isRevealed;
	protected boolean isBomb;
	protected boolean isSpace;
	protected boolean isFlag;
	protected boolean isFlagWrong;
	protected int number;
	protected int width;
	protected int height;
	protected int boardX;
	protected int boardY;
	
	public CellData(int x, int y, boolean reveal, int bomb) {
		
        isBomb = false;
        number = bomb;
        isSpace = false;
        isFlag = false;
        isFlagWrong = false;
        boardX = x;
        boardY = y;
        isRevealed = reveal;
        
	}
	
	public void setIsFlagWrong() {
		isFlagWrong = true;
	}
	
	public void cancleIsFlagWrong() {
		isFlagWrong = false;
	}
	
	public boolean checkIsFlagWrong() {
		return isFlagWrong;
	}
	
	public boolean checkFlag() {
		return isFlag;
	}
	
	public void setFlag() {
		isFlag = true;
	}
	
	public void setNotFlag() {
		isFlag = false;
	}
	
	public int getBoardX() {
		return boardX;
	}
	
	public int getBoardY() {
		return boardY;
	}
	
	public boolean checkBomb() {
		return isBomb;
	}
	
	public boolean checkReveal() {
		return isRevealed;
	}
	
	public void setReveal() {
		isRevealed = true;
	}
	
	public void setNotReveal() {
		isRevealed = false;
	}
	
	public void setBomb() {
		isBomb = true;
	}
	
	public boolean checkSpace() {
		return isSpace;
	}
	
	public void setSpace() {
		isSpace = true;
	}
	
	public int checkNum() {
		return number;
	}
	
	public void setNum(int num) {
		number = num;
	}

}
