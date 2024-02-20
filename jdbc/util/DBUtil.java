package jdbc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
	private static String driver = "oracle.jdbc.driver.OracleDriver";
	private static String url = "jdbc:oracle:thin:@localhost:1521:XE";
	private static String user = "scott", pwd = "tiger";

	// 드라이버 로드(가장 먼저 수행되는 static 블록에)
	static {
		try {
			Class.forName(driver);
			System.out.println("Driver Loading success...");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private DBUtil() {
		// 외부에서 객체 생성 못하도록 private 접근지정자 사용

	}

	public static Connection getCon() throws SQLException {// con얻어와
		Connection con = DriverManager.getConnection(url, user, pwd);
		return con; // con반환
	}
}
