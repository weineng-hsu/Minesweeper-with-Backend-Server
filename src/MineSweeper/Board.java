package MineSweeper;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import java.io.Serializable;


public class Board extends JPanel implements java.io.Serializable {
	
	private final static int MAX_X = 16;
	private final static int MAX_Y = 16;
	private final static int BOMB_NUM = 40;
	
	private final static int NO_BOMB = 0;
	private final static int ONE_BOMB = 1;
	private final static int TWO_BOMB = 2;
	private final static int THREE_BOMB = 3;
	private final static int FOUR_BOMB = 4;
	private final static int FIVE_BOMB = 5;
	private final static int SIX_BOMB = 6;
	private final static int SEVEN_BOMB = 7;
	private final static int EIGHT_BOMB = 8;
	private final static int BOMB = 9;

	private int cellWidth;
	private int cellHeight;
	private int boardWidth;
	private int boardHeight;
	private Boolean gameover;
	private Boolean win;
	private int remainBomb;
	
	public int getWidth() {
		return boardWidth;
	}
	
	public int getHeight() {
		return boardHeight;
	}
	
	protected Cell[][] board = new Cell[MAX_X][MAX_Y];
	protected CellData[][] boardData = new CellData[MAX_X][MAX_Y];
	
	public int getRemainBomb() {
		return remainBomb;
	}
	
	
	public Board() {
		super();
		setBoardSize();
		generateBoard();
		setBomb();
		drawNumber();
		updateBoardData();
		drawBoard();
		addMouseListener(new CellMouseAdapter());
		//System.out.println(boardData[0][0].checkNum());
		//System.out.println(boardData[0][0].checkNum());
	}
	
	public void updateBoardData() {
		for (int y = 0; y < MAX_Y; y++) {
			for (int x = 0; x < MAX_X; x++) {
				boardData[x][y] = board[x][y].getCellData();
			}
		}
	}
	
	public void deserializeBoard(BoardData input) {
		for (int y = 0; y < MAX_Y; y++) {
			for (int x = 0; x < MAX_X; x++) {
				board[x][y] = new Cell(input.boardData[x][y]);
			}
		}
		remainBomb = input.getRemainBomb();
		win = input.getWin();
		gameover = input.getLose();
		updateBoardData();
		removeAll();
	    drawBoard();
	    validate();
		repaint();
	}
	
	public CellData[][] extractData() {
		for (int y = 0; y < MAX_Y; y++) {
			for (int x = 0; x < MAX_X; x++) {
				boardData[x][y] = board[x][y].getCellData();
			}
		}
		return boardData;
	}

	public boolean getWin() {
		return win;
	}

	public boolean getLose() {
		return gameover;
	}

	public void setBoardSize() {
		Cell setSize = new Cell("10.png", 0 , 0, false, -1);
		cellWidth = setSize.getWidth();
		cellHeight = setSize.getHeight();
		boardWidth = MAX_X * cellWidth;
		boardHeight = MAX_Y * cellHeight;
		setPreferredSize(new Dimension(boardWidth, boardHeight));
		setLayout(new GridLayout(MAX_X, MAX_Y));
		setVisible(true);
	}
	
	public void generateBoard() {
		gameover = false;
		win = false;
		remainBomb = BOMB_NUM;
		for (int y = 0; y < MAX_Y; y++) {
			for (int x = 0; x < MAX_X; x++) {
				Cell toAdd = new Cell("10.png", x, y, false, -1);
				board[x][y] = toAdd;
			}
		}
	}
	
	public void setBomb() {
		Random rand = new Random();
		for (int i = 0; i < BOMB_NUM; i++) {
			int bombX = rand.nextInt(MAX_X);
			int bombY = rand.nextInt(MAX_Y);
			if (board[bombX][bombY].checkBomb()) {
				i--;
			} else {
				board[bombX][bombY].setBomb();
			}
		}
		
	}
	
	public void drawNumber() {
		//drawInner
		for (int i = 1; i < MAX_X - 1; i++) {
			for (int j = 1; j < MAX_Y - 1; j++) {
				if (!board[i][j].checkBomb()) {
					int nearBomb = check8(i, j);
					board[i][j].setNum(nearBomb);
				}
			}
		}
		//drawCorner
		if (!board[0][MAX_Y - 1].checkBomb()) {
			int nearBomb = checkCorner(0, MAX_Y - 1);
			board[0][MAX_Y - 1].setNum(nearBomb);
		}
		if (!board[0][0].checkBomb()) {
			int nearBomb = checkCorner(0, 0);
			board[0][0].setNum(nearBomb);
		}
		if (!board[MAX_X - 1][0].checkBomb()) {
			int nearBomb = checkCorner(MAX_X - 1, 0);
			board[MAX_X - 1][0].setNum(nearBomb);
		}
		if (!board[MAX_X - 1][MAX_Y - 1].checkBomb()) {
			int nearBomb = checkCorner(MAX_X - 1, MAX_Y - 1);
			board[MAX_X - 1][MAX_Y - 1].setNum(nearBomb);
		}
		//drawEastBorder
		for (int j = 1; j < MAX_Y - 1; j++) {
			if (!board[MAX_X - 1][j].checkBomb()) {
				int nearBomb = checkEastBorder(MAX_X - 1, j);
				board[MAX_X - 1][j].setNum(nearBomb);
			}
		}
		//drawWestBorder
		for (int j = 1; j < MAX_Y - 1; j++) {
			if (!board[0][j].checkBomb()) {
				int nearBomb = checkWestBorder(0, j);
				board[0][j].setNum(nearBomb);
			}
		}
		//drawSouthBorder
		for (int i = 1; i < MAX_X - 1; i++) {
			if (!board[i][MAX_Y - 1].checkBomb()) {
				int nearBomb = checkSouthBorder(i, MAX_Y - 1);
				board[i][MAX_Y - 1].setNum(nearBomb);
			}
		}
		//drawNorthBorder
		for (int i = 1; i < MAX_X - 1; i++) {
			if (!board[i][0].checkBomb()) {
				int nearBomb = checkNorthBorder(i, 0);
				board[i][0].setNum(nearBomb);
			}
		}
	}
	
	public int check8(int x, int y) {
		int nearBomb = 0;
		if (board[x-1][y-1].checkBomb()) {
			nearBomb += 1;
		}
		if (board[x][y-1].checkBomb()) {
			nearBomb += 1;
		}
		if (board[x+1][y-1].checkBomb()) {
			nearBomb += 1;
		}
		if (board[x-1][y].checkBomb()) {
			nearBomb += 1;
		}
		if (board[x+1][y].checkBomb()) {
			nearBomb += 1;
		}
		if (board[x-1][y+1].checkBomb()) {
			nearBomb += 1;
		}
		if (board[x][y+1].checkBomb()) {
			nearBomb += 1;
		}
		if (board[x+1][y+1].checkBomb()) {
			nearBomb += 1;
		}
		return nearBomb;
	}
	
	public int checkCorner(int x, int y) {
		int nearBomb = 0;
		
		if (x == 0 && y == 0) { 				//UpperLeft
			if (board[x+1][y].checkBomb()) {
				nearBomb += 1;
			}
			if (board[x+1][y+1].checkBomb()) {
				nearBomb += 1;
			}
			if (board[x][y+1].checkBomb()) {
				nearBomb += 1;
			}
		} else if (x == 0 && y == MAX_Y - 1) { //LowerLeft
			if (board[x+1][y].checkBomb()) {
				nearBomb += 1;
			}
			if (board[x+1][y-1].checkBomb()) {
				nearBomb += 1;
			}
			if (board[x][y-1].checkBomb()) {
				nearBomb += 1;
			}
		} else if (x == MAX_X - 1 && y == 0) { //UpperRight
			if (board[x-1][y].checkBomb()) {
				nearBomb += 1;
			}
			if (board[x-1][y+1].checkBomb()) {
				nearBomb += 1;
			}
			if (board[x][y+1].checkBomb()) {
				nearBomb += 1;
			}
		} else {							  //LowerRight
			if (board[x-1][y-1].checkBomb()) {
				nearBomb += 1;
			}
			if (board[x-1][y].checkBomb()) {
				nearBomb += 1;
			}
			if (board[x][y-1].checkBomb()) {
				nearBomb += 1;
			}
		}
		return nearBomb;
	}
	
	public int checkEastBorder(int x, int y) {
		int nearBomb = 0;
		if (board[x-1][y-1].checkBomb()) {
			nearBomb += 1;
		}
		if (board[x][y-1].checkBomb()) {
			nearBomb += 1;
		}
		if (board[x-1][y].checkBomb()) {
			nearBomb += 1;
		}
		if (board[x-1][y+1].checkBomb()) {
			nearBomb += 1;
		}
		if (board[x][y+1].checkBomb()) {
			nearBomb += 1;
		}
		return nearBomb;
	}
	
	public int checkSouthBorder(int x, int y) {
		int nearBomb = 0;
		if (board[x-1][y-1].checkBomb()) {
			nearBomb += 1;
		}
		if (board[x][y-1].checkBomb()) {
			nearBomb += 1;
		}
		if (board[x+1][y-1].checkBomb()) {
			nearBomb += 1;
		}
		if (board[x-1][y].checkBomb()) {
			nearBomb += 1;
		}
		if (board[x+1][y].checkBomb()) {
			nearBomb += 1;
		}
		return nearBomb;
	}
	
	public int checkWestBorder(int x, int y) {
		int nearBomb = 0;
		if (board[x][y-1].checkBomb()) {
			nearBomb += 1;
		}
		if (board[x+1][y-1].checkBomb()) {
			nearBomb += 1;
		}
		if (board[x+1][y].checkBomb()) {
			nearBomb += 1;
		}
		if (board[x][y+1].checkBomb()) {
			nearBomb += 1;
		}
		if (board[x+1][y+1].checkBomb()) {
			nearBomb += 1;
		}
		return nearBomb;
	}
	
	public int checkNorthBorder(int x, int y) {
		int nearBomb = 0;
		if (board[x-1][y].checkBomb()) {
			nearBomb += 1;
		}
		if (board[x+1][y].checkBomb()) {
			nearBomb += 1;
		}
		if (board[x-1][y+1].checkBomb()) {
			nearBomb += 1;
		}
		if (board[x][y+1].checkBomb()) {
			nearBomb += 1;
		}
		if (board[x+1][y+1].checkBomb()) {
			nearBomb += 1;
		}
		return nearBomb;
	}
	
	public String toString() {
		//int zeroCount = 0;
		int bombCount = 0;
		String toPrint = null;
		
		for (int x = 0; x < MAX_X; x++) {
			for (int y = 0; y < MAX_Y; y++) {
				if (board[y][x].checkReveal()) {
					
					if (board[y][x].checkBomb() && !board[y][x].checkFlag()) {
						toPrint += "X";
						//System.out.print("X");
					} else if (gameover && board[y][x].checkIsFlagWrong()) {
						toPrint += "N";
					} else if (board[y][x].checkFlag()) {
						//System.out.print("!");
						toPrint += "!";
					} /*else if (gameover && board[y][x].checkIsFlagWrong()) {
						board[y][x].setImage("12.png");
						add(board[y][x]);
					}*/ else {
						switch (board[y][x].checkNum()) {
						case NO_BOMB:
							toPrint += "0";
							//System.out.print("0");
							break;
						case ONE_BOMB:
							toPrint += "1";
							//System.out.print("1");
							break;
						case TWO_BOMB:
							toPrint += "2";
							//System.out.print("2");
							break;
						case THREE_BOMB:
							toPrint += "3";
							//System.out.print("3");
							break;
						case FOUR_BOMB:
							toPrint += "4";
							//System.out.print("4");
							break;
						case FIVE_BOMB:
							toPrint += "5";
							//System.out.print("5");
							break;
						case SIX_BOMB:
							toPrint += "6";
							//System.out.print("6");
							break;
						case SEVEN_BOMB:
							toPrint += "7";
							//System.out.print("7");
							break;
						case EIGHT_BOMB:
							toPrint += "8";
							System.out.print("8");
							break;
			        	
			        	}
					}
				} else {
					toPrint += "_";
					//System.out.print("_");
				}
				
			}
			//System.out.print("\n");
			toPrint += "\n";
		}
		//System.out.println("bomb counts: " + bombCount);
		return toPrint;
	}
	
	
	public void drawBoard() {
		//int zeroCount = 0;
		int bombCount = 0;
		
		System.out.println(gameover);
		for (int x = 0; x < MAX_X; x++) {
			for (int y = 0; y < MAX_Y; y++) {
				if (board[y][x].checkReveal()) {
					//System.out.println("boarddata: " + boardData[y][x].checkReveal());
					//System.out.println("boarddata: " + board[y][x].getCellData().checkReveal());
					
					if (board[y][x].checkBomb() && !board[y][x].checkFlag()) {
						System.out.print("X");
						board[y][x].setImage("9.png");
						add(board[y][x]);
						bombCount += 1;
					} else if (gameover && board[y][x].checkIsFlagWrong()) {
						board[y][x].setImage("12.png");
						add(board[y][x]);
					} else if (board[y][x].checkFlag()) {
						System.out.print("!");
						board[y][x].setImage("11.png");
						add(board[y][x]);
					} /*else if (gameover && board[y][x].checkIsFlagWrong()) {
						board[y][x].setImage("12.png");
						add(board[y][x]);
					}*/ else {
						switch (board[y][x].checkNum()) {
						case NO_BOMB:
							board[y][x].setImage("0.png");
							//board[y][x].setSpace();
							add(board[y][x]);
							System.out.print("0");
							break;
						case ONE_BOMB:
							board[y][x].setImage("1.png");
							add(board[y][x]);
							System.out.print("1");
							break;
						case TWO_BOMB:
							board[y][x].setImage("2.png");
							add(board[y][x]);
							System.out.print("2");
							break;
						case THREE_BOMB:
							board[y][x].setImage("3.png");
							add(board[y][x]);
							System.out.print("3");
							break;
						case FOUR_BOMB:
							board[y][x].setImage("4.png");
							add(board[y][x]);
							System.out.print("4");
							break;
						case FIVE_BOMB:
							board[y][x].setImage("5.png");
							add(board[y][x]);
							System.out.print("5");
							break;
						case SIX_BOMB:
							board[y][x].setImage("6.png");
							add(board[y][x]);
							System.out.print("6");
							break;
						case SEVEN_BOMB:
							board[y][x].setImage("7.png");
							add(board[y][x]);
							System.out.print("7");
							break;
						case EIGHT_BOMB:
							board[y][x].setImage("8.png");
							add(board[y][x]);
							System.out.print("8");
							break;
			        	
			        	}
					}
				} else {
					
					board[y][x].setImage("10.png");
					add(board[y][x]);
					System.out.print("_");
				}
				
			}
			System.out.print("\n");
		}
		System.out.println("bomb counts: " + remainBomb);
	}
	
	public static void main(String[] args) {
		Board newGame = new Board();
		//newGame.drawBoard();
		JFrame test = new JFrame();
		test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		test.setSize(new Dimension(newGame.boardWidth + 10, newGame.boardHeight + 60));
		test.add(newGame);
		test.setResizable(false);
		test.setVisible(true);
	}
	
	private class CellMouseAdapter extends MouseAdapter {
		public void mousePressed(MouseEvent event) {
			
			//System.out.println("pressed");
			boolean button3 = false;
			
			if (gameover) {
				for (int i = 0; i < MAX_X; i++) {
					for (int j = 0; j < MAX_Y; j++) {
						board[i][j].removeAll();
					}
				}
			    removeAll();
				generateBoard();
				setBomb();
				drawNumber();
				drawBoard();
				updateBoardData();
				validate();
				repaint();
				return;
			}
			
			if (getComponentAt(event.getPoint()) instanceof Cell) {
				Cell panel = (Cell) getComponentAt(event.getPoint());
				if (event.getButton()== MouseEvent.BUTTON3) {
					System.out.println("BUTTON3 pressed");
					button3 = true;
					int x = panel.getBoardX();
		        	int y = panel.getBoardY();
		        	if(panel.checkFlag()) {//cancel Flag
		        		//System.out.println("cancel flag pressed: X: " + x + "Y: " + y);
		        		//System.out.println("Before cancel flag: " + board[x][y].checkFlag());
		        		remainBomb += 1;
						board[x][y].setNotReveal();
			        	board[x][y].setNotFlag();
			        	board[x][y].cancleIsFlagWrong();
			        	System.out.println("After cancel flag: " + board[x][y].checkFlag());
			        	System.out.println("After cancel flag: " + board[x][y].checkReveal());
			        } else {//Set Flag
						if (!panel.checkFlag() && !panel.checkReveal()) {
							board[x][y].setReveal();
							board[x][y].setFlag();
							remainBomb -= 1;
							if (!board[x][y].checkBomb()) {
								board[x][y].setIsFlagWrong();
								System.out.println("setFlagWong");
							}
			        	//System.out.println("setFlag");
						}
			        }
					
				}
				if (!button3) {
					if(panel.checkBomb()) {
		        		System.out.print("bomb") ;
		        		gameover = true;
		        		
		        		for (int i = 0; i < MAX_X; i++) {
		        			for (int j = 0; j < MAX_Y; j++) {
		        				if (board[i][j].checkBomb()) {
		        					board[i][j].setReveal();
		        				}
		        				if (board[i][j].checkIsFlagWrong()) {
		        					board[i][j].setReveal();
		        				}
		        			}
		        		}
			        } else {
			        	int num = panel.checkNum();
			        	int x = panel.getBoardX();
			        	int y = panel.getBoardY();
			        	System.out.println("X: " + x + " Y: " + y);
			        	switch (num) {
			    		
							case NO_BOMB:
								board[x][y].setReveal();
								revealNearSpace(x, y);
								break;
							case ONE_BOMB:
								board[x][y].setReveal();
								break;
							case TWO_BOMB:
								board[x][y].setReveal();
								break;
							case THREE_BOMB:
								board[x][y].setReveal();
								break;
							case FOUR_BOMB:
								board[x][y].setReveal();
								break;
							case FIVE_BOMB:
								board[x][y].setReveal();
								break;
							case SIX_BOMB:
								board[x][y].setReveal();
								break;
							case SEVEN_BOMB:
								board[x][y].setReveal();
								break;
							case EIGHT_BOMB:
								board[x][y].setReveal();
								break;
			        	}
			        }
				}
		    
			}
			checkWin();
			updateBoardData();
			removeAll();
		    drawBoard();
		    validate();
			repaint();
		}
	}
	
	public void checkWin() {
		for (int i = 0; i < MAX_X; i++) {
			for (int j = 0; j < MAX_Y; j++) {
				if (gameover || !board[i][j].checkReveal()) {
					return;
				}
			}
		}
		if (remainBomb == 0) {
			win = true;
		}
	}
	
	public void revealNearSpace(int x, int y) {
		if (x >= MAX_X || y >= MAX_Y || x < 0 || y < 0) {
			return;
		}
		if (board[x][y].checkNum() != 0) {
			return;
		}
		
		
		if (board[x][y].checkNum() == 0) {
			board[x][y].setReveal();
			if (x + 1 < MAX_X && !board[x+1][y].checkReveal()) {
				if (!board[x+1][y].checkBomb()) {
					board[x+1][y].setReveal();
				}
				revealNearSpace(x+1, y);
			}
			if (x - 1 >= 0 && !board[x-1][y].checkReveal()) {
				if (!board[x-1][y].checkBomb()) {
					board[x-1][y].setReveal();
				}
				revealNearSpace(x-1, y);
			}
			if (y - 1 >= 0 && !board[x][y-1].checkReveal()) {
				if (!board[x][y-1].checkBomb()) {
					board[x][y-1].setReveal();
				}
				revealNearSpace(x, y-1);
			}
			if (y + 1 < MAX_Y && !board[x][y+1].checkReveal()) {
				if (!board[x][y+1].checkBomb()) {
					board[x][y+1].setReveal();
				}
				revealNearSpace(x, y+1);
			}
				
		}
			
		
		
	}
	

}
