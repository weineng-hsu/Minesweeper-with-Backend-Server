package MineSweeper;

import java.io.*;
import java.net.*;
import java.sql.*;



public class MineSweeperServer {
	private ObjectOutputStream outputToClient;
	private ObjectInputStream inputFromClient;
	String host = "localhost";
	private static final String INSERT_STATEMENT = "INSERT INTO serilize_mineboard(board_object) VALUES (?)";
	private static final String SELECT_STATEMENT = "SELECT board_object FROM serilize_mineboard WHERE board_id = ?";

	/*
	 * 

        try ( Connection conn = ds.getConnection();
              Statement stmt = conn.createStatement(); ) {
            int rv = stmt.executeUpdate( query );
            System.out.println( "executeUpdate() returned " + rv );
        } catch ( SQLException e ) {
            e.printStackTrace();
            System.exit( 0 );
        }
	 */

	public static void main(String[] args) {
		new MineSweeperServer();
		/*
		try {
			//Connection conn = DriverManager.getConnection("jdbc:sqlite:mineboard.db");
			//Statement stmt = conn.createStatement(); 
	        //int rv = stmt.executeUpdate(query);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}

		System.out.println("Opened database successfully");*/
		/*
		try {
			Connection conn = DriverManager.getConnection("jdbc:sqlite:mineboard.db");
			try {
				Board load = (Board) MineSweeperServer.getBoardFromDB(conn, 1);
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
	}
	/*
	 * connection = DriverManager.getConnection
			      ("jdbc:sqlite:javabook.db");
		  out = new ObjectOutputStream(bos);   
		  out.writeObject(testSerializableObject);
		  out.flush();
		  byte[] yourBytes = bos.toByteArray();
		  String insertString = "INSERT INTO objectstore VALUES (?, ?, ?)";
			preparedStatement = connection.prepareStatement(insertString);
			preparedStatement.setBytes(2, yourBytes);
			preparedStatement.setInt(1, 1234567);
			preparedStatement.setLong(3, TestSerializableObject.serialVersionUID);
		  preparedStatement.execute();
	 */
	
	public static long storeBoardToDB(Connection conn, Object toStore) throws SQLException {

		PreparedStatement pstmt = conn.prepareStatement(INSERT_STATEMENT);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream out = null;

		// just setting the class name
		//pstmt.setString(1, "test");
		try {
			out = new ObjectOutputStream(bos);
			out.writeObject(toStore);
			out.flush();
			byte[] yourBytes = bos.toByteArray();
			pstmt.setBytes(1, yourBytes);
			pstmt.executeUpdate();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		ResultSet rs = pstmt.getGeneratedKeys();
		int serialized_id = -1;
		if (rs.next()) {
			serialized_id = rs.getInt(1);
		}
		rs.close();
		pstmt.close();
		System.out.println("Java object serialized to database. Object: ");
		return serialized_id;
	}
	
	public static Object getBoardFromDB(Connection connection, long id) throws SQLException, IOException, ClassNotFoundException {
		PreparedStatement pstmt = connection.prepareStatement(SELECT_STATEMENT);
		pstmt.setLong(1, id);
		ResultSet rs = pstmt.executeQuery();
		rs.next();

		// Object object = rs.getObject(1);

		byte[] buf = rs.getBytes(1);
		ObjectInputStream objectIn = null;
		if (buf != null)
			objectIn = new ObjectInputStream(new ByteArrayInputStream(buf));

		Object deSerializedObject = objectIn.readObject();

		rs.close();
		pstmt.close();

		System.out.println("Java object de-serialized from database. Object: ");
		return deSerializedObject;
	}


	public MineSweeperServer() {
		try {
			// Create a server socket
			ServerSocket serverSocket = new ServerSocket(8000);
			// Establish connection with the server
			//Socket senderSocket = new Socket(host, 7000);
			System.out.println("Server started ");

			// Create an object ouput stream
			//outputToFile = new ObjectOutputStream(
			//new FileOutputStream("student.dat", true));

			while (true) {
				// Listen for a new connection request
				Socket socket = serverSocket.accept();
				Connection conn = DriverManager.getConnection("jdbc:sqlite:mineboard.db");

				// Create an input stream from the socket
				inputFromClient = new ObjectInputStream(socket.getInputStream());
				

				// Read from input
				Object object = inputFromClient.readObject();

				// Write to the file
				BoardData save = (BoardData)object;
				System.out.println("got object " + object.toString());
				long serialized_id = MineSweeperServer.storeBoardToDB(conn, save);
				//outputToFile.writeObject(object);
				//outputToFile.flush();
				//System.out.println("A new student object is stored");
       
       
				//Output
				BoardData load = (BoardData) MineSweeperServer.getBoardFromDB(conn, serialized_id);
       
				// Create an output stream to the server
				ObjectOutputStream toServer = new ObjectOutputStream(socket.getOutputStream());
				toServer.writeObject(load);
			}
		} catch(ClassNotFoundException ex) {
			ex.printStackTrace();
		} catch(IOException ex) {
			ex.printStackTrace();
		} catch(SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				inputFromClient.close();
				//outputToClient.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}
