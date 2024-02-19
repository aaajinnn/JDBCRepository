package day03;

//ex02
import java.sql.*;
import java.util.*;

// 실습 : DEPT테이블에 부서정보를 입력받아 insert하는 문장을 실행시키기
// 부서번호, 부서명, 근무지 ==>
public class PreparedStatementTest2 {

	public static void main(String[] args) throws Exception {
		Scanner sc = new Scanner(System.in);

		System.out.println("삽입할 부서번호 입력 : ");
		String no = sc.nextLine();

		System.out.println("삽입할 부서명 입력 : ");
		String dname = sc.nextLine();

		System.out.println("삽입할 근무지 입력 : ");
		String loc = sc.nextLine();

		Class.forName("oracle.jdbc.driver.OracleDriver");

		String url = "jdbc:oracle:thin:@localhost:1521:XE";
		String user = "scott", pwd = "tiger";

		Connection con = DriverManager.getConnection(url, user, pwd);

		String sql = "INSERT INTO dept VALUES(?, ?, ?)";

		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setString(1, no);
		pstmt.setString(2, dname);
		pstmt.setString(3, loc);

		int cnt = pstmt.executeUpdate();
		System.out.println(cnt + "개의 레코드 삽입 완료");

		pstmt.close();
		con.close();
	}

}
