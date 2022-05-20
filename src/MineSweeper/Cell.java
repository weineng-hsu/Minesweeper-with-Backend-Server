package MineSweeper;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import java.io.Serializable;

public class Cell extends JPanel implements java.io.Serializable  {
	
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
	
	protected CellData data;
	
	private Image img;
	
	public Cell(String type, int x, int y, boolean reveal, int bomb) {
		this(new ImageIcon(type).getImage(), x, y, reveal, bomb);
	}
	
	public Cell(CellData data) {
		Dimension size = new Dimension(15, 15);
        isBomb = data.checkBomb();
        number = data.checkNum();
        isSpace = data.checkSpace();
        isFlag = data.checkFlag();
        isFlagWrong = data.checkIsFlagWrong();
        boardX = data.boardX;
        boardY = data.boardY;
        isRevealed = data.checkReveal();
        this.data = data;
	}
	
	
	public Cell(Image img, int x, int y, boolean reveal, int bomb) {
		
		this.img = img;
        Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
        isBomb = false;
        number = bomb;
        isSpace = false;
        isFlag = false;
        isFlagWrong = false;
        boardX = x;
        boardY = y;
        isRevealed = reveal;
        data = new CellData(x, y, reveal, bomb);
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        setSize(size);
        setLayout(null);
        setVisible(true);
	}
	
	public CellData getCellData() {
		return data;
	}
	
	public void setIsFlagWrong() {
		isFlagWrong = true;
		data.setIsFlagWrong();
	}
	
	public void cancleIsFlagWrong() {
		isFlagWrong = false;
		data.cancleIsFlagWrong();
	}
	
	public boolean checkIsFlagWrong() {
		return isFlagWrong;
	}
	
	public boolean checkFlag() {
		return isFlag;
	}
	
	public void setFlag() {
		isFlag = true;
		data.setFlag();
	}
	
	public void setNotFlag() {
		isFlag = false;
		data.setNotFlag();
	}
	
	public int getBoardX() {
		return boardX;
	}
	
	public int getBoardY() {
		return boardY;
	}
	/*
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}*/
	
	
	public boolean checkBomb() {
		return isBomb;
	}
	
	public boolean checkReveal() {
		return isRevealed;
	}
	
	public void setReveal() {
		isRevealed = true;
		data.setReveal();
	}
	
	public void setNotReveal() {
		isRevealed = false;
		data.setNotReveal();
	}
	
	public void setImage(String type) {
		img = new ImageIcon(type).getImage();
	}
	
	public void setBomb() {
		isBomb = true;
		data.setBomb();
	}
	
	public boolean checkSpace() {
		return isSpace;
	}
	
	public void setSpace() {
		isSpace = true;
		data.setSpace();
	}
	
	public int checkNum() {
		return number;
	}
	
	public void setNum(int num) {
		number = num;
		data.setNum(num);
	}
	
	@Override
    public void paintComponent(Graphics g) {
		super.paintComponent(g);
        g.drawImage(img, 0, 0, null);
    }
    
	
}
