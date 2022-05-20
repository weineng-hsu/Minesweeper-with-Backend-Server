package MineSweeper;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.Timer;



public class MineSweeper extends JFrame{
	
	JPanel topCountDownPanel;
	JLabel countDown;
	JPanel bottomMassagePanel;
	JPanel boardPanel;
	JLabel massage;
	Board game;
	String host = "localhost";
	Socket mineSocket;
	Timer gameTimer;
	int remainTime;
	
	private int remainBomb;
	private boolean gameStart;
	private int FRAME_WIDTH;
	private int FRAME_HEIGHT;
	private final int COUNTDOWN_MASSAGE_HEIGHT = 10;
	ObjectInputStream inputFromServer;
	
	public MineSweeper() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// set the size correctly
		
		game = new Board();
		gameStart = false;
		remainTime = 1000;
		remainBomb = game.getRemainBomb();
		FRAME_WIDTH = game.getWidth();
		FRAME_HEIGHT = game.getHeight() + 2 * COUNTDOWN_MASSAGE_HEIGHT + 80;
		this.setSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
		this.setResizable(false);
		

		//panel at the top
		createTopCountDownPanel();
		createBottomMassagePanel();
		
		
		// there should be objects in the top panel
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
	    menuBar.add(createFileMenu());
	    
	    gameTimer = new Timer(1000, new TimeClass());
	    gameTimer.start();

		// and of course you will want them to be properly aligned in the frame
	    add(topCountDownPanel, BorderLayout.NORTH);
	    add(game, BorderLayout.CENTER);
	    add(bottomMassagePanel, BorderLayout.SOUTH);
	    game.addMouseListener(new MassageMouseAdapter());
	    
	}
	
	private class TimeClass implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if (game.getLose()) {
				gameTimer.stop();
				remainTime = 1001;
			} 
			
			if (gameStart) {
				gameTimer.start();
				remainTime -= 1;
			} else {
				remainTime = 1000;
			}
			
			if (remainTime >= 1) {
				countDown.setText("Time Remaining: " + remainTime);
			} else {
				countDown.setText("Game lost");
			}
		}
	}
	
	private class MassageMouseAdapter extends MouseAdapter {
		public void mousePressed(MouseEvent event) {
			remainBomb = game.getRemainBomb();
			gameStart = true;
			gameTimer.start();
			System.out.println(remainBomb);
			getContentPane().remove(bottomMassagePanel);
			createBottomMassagePanel();
			add(bottomMassagePanel, BorderLayout.SOUTH);
		    validate();
		    repaint();
		}
	}
	
	public void createTopCountDownPanel() {
		topCountDownPanel = new JPanel();
		topCountDownPanel.setSize(new Dimension(FRAME_WIDTH, COUNTDOWN_MASSAGE_HEIGHT));
		countDown = new JLabel("Time Remaining: 1000");
		topCountDownPanel.add(countDown);
	}
	
	public void createBottomMassagePanel() {
		bottomMassagePanel = new JPanel();
		bottomMassagePanel.setLayout(new GridLayout());
		bottomMassagePanel.setSize(new Dimension(FRAME_WIDTH, COUNTDOWN_MASSAGE_HEIGHT));
		massage = new JLabel();
		if (game.getWin()) {
			massage.setText("Game won");
		} else if (game.getLose()) {
			massage.setText("Game lost");
		} else {
			massage.setText(Integer.toString(remainBomb));
		}
		bottomMassagePanel.add(massage);
	}
	
	public void updateBottomMassage() {
		remainBomb = game.getRemainBomb();
		System.out.println(remainBomb);
		getContentPane().remove(bottomMassagePanel);
		createBottomMassagePanel();
		add(bottomMassagePanel, BorderLayout.SOUTH);
	    validate();
	    repaint();
	}
	
	public JMenu createFileMenu() {
		JMenu menu = new JMenu("File");
		menu.add(createFileNewItem());
		menu.add(createFileOpenItem());
		menu.add(createFileSaveItem());
		menu.add(createFileExitItem());
		return menu;
	}
	
	public JMenuItem createFileExitItem() {
		JMenuItem item = new JMenuItem("Exit");
		class MenuItemListener implements ActionListener {
			public void actionPerformed(ActionEvent event) {
				System.exit(0);
			}
		}      
	    ActionListener listener = new MenuItemListener();
	    item.addActionListener(listener);
	    return item;
	}
	
	public JMenuItem createFileSaveItem() {
		JMenuItem item = new JMenuItem("Save");
		class MenuItemListener implements ActionListener {
			public void actionPerformed(ActionEvent event) {
				//ToDo
				 try {
				        // Establish connection with the server
				        mineSocket = new Socket(host, 8000);

				        // Create an output stream to the server
				        ObjectOutputStream toServer = new ObjectOutputStream(mineSocket.getOutputStream());

				        //String test = "connection test";
				        BoardData transfer = new BoardData(game);
				        transfer.setRemainBomb(game.getRemainBomb());
				        transfer.setRemainTime(remainTime);
				        if (game.getWin()) {
				        	transfer.setWin();
				        }
				        if (game.getLose()) {
				        	transfer.setLose();
				        }
				        toServer.writeObject(transfer);
				      }
				      catch (IOException ex) {
				        ex.printStackTrace();
				      }
			}
		}      
	    ActionListener listener = new MenuItemListener();
	    item.addActionListener(listener);
	    return item;
	}
	
	public JMenuItem createFileNewItem() {
		JMenuItem item = new JMenuItem("New");   
		class MenuItemListener implements ActionListener {
			public void actionPerformed(ActionEvent event) {
				getContentPane().remove(game);
				gameTimer.stop();
				game = new Board();
				game.validate();
			    game.repaint();
			    getContentPane().add(game, BorderLayout.CENTER);
			    game.addMouseListener(new MassageMouseAdapter());
			    updateBottomMassage();
			    gameStart = false;
			    validate();
			    repaint();
			}
		}
		ActionListener listener = new MenuItemListener();
	    item.addActionListener(listener);
	    return item;
	}
	
	
	public JMenuItem createFileOpenItem() {
		JMenuItem item = new JMenuItem("Open");   
		class MenuItemListener implements ActionListener {
			public void actionPerformed(ActionEvent event) {
				System.out.println("Open");
				try {
				     // Create a server socket
				     //ServerSocket serverSocket = new ServerSocket(7000);
				     System.out.println("Load started ");
				     //InputStream input = socket.getInputStream();

				     // Create an object ouput stream
				     //outputToFile = new ObjectOutputStream(
				       //new FileOutputStream("student.dat", true));

				     
				       // Listen for a new connection request
				       //socket = socket.accept();

				       // Create an input stream from the socket
				       inputFromServer = new ObjectInputStream(mineSocket.getInputStream());

				       // Read from input
				       Object object = inputFromServer.readObject();

				       // Write to the file
				       BoardData s = (BoardData)object;
				       System.out.println("got object " + object.toString());
				       //System.out.println(s.boardData[0][0].number);
				       //System.out.println(s.boardData[0][0].isRevealed);
				       //outputToFile.writeObject(object);
				       //outputToFile.flush();
				       
				       game.deserializeBoard(s);
				       remainTime = s.getRemainTime();
				       updateBottomMassage();
				       getContentPane().remove(game);
					   getContentPane().add(game, BorderLayout.CENTER);
					   validate();
					   repaint();
				     
				   }
				   catch(ClassNotFoundException ex) {
				     ex.printStackTrace();
				   }
				   catch(IOException ex) {
				     ex.printStackTrace();
				   }
				   finally {
				     try {
				       inputFromServer.close();
				       //outputToFile.close();
				     }
				     catch (Exception ex) {
				       ex.printStackTrace();
				     }
				   }
				 }
			}
		ActionListener listener = new MenuItemListener();
	    item.addActionListener(listener);
	    return item;
	}

	public static void main(String[] args) {
		MineSweeper ms = new MineSweeper();
		ms.setVisible(true);
		ms.repaint();
	}

}
