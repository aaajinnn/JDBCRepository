package jdbc.member;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
	private static String driver = "oracle.jdbc.driver.OracleDriver";
	private static String url = "jdbc:oracle:thin:@localhost:1521:XE";
	private static String user = "scott", pwd = "tiger";

	static {
		try {
			Class.forName(driver);
			System.out.println("드라이버 로딩 성공");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public DBConnection() {

	}

	public static Connection getCon() throws SQLException {
		Connection con = DriverManager.getConnection(url, user, pwd);
		return con;
	}
}
