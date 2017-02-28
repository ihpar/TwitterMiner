import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLMgr {
	private final String url = "jdbc:mysql://127.0.0.1:3306/twitterdb";
	private final String user = "root";
	private final String password = "";
	private Connection conn;

	public MySQLMgr() {
		conn = null;
	}

	public Connection connectToDB() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		conn = DriverManager.getConnection(url, user, password);
		return conn;
	}

	public void closeConnection() {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
}
