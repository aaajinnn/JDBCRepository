package day03;

// ex01
import java.sql.*;
import java.util.*;

/*
 PreparedStatement를 이용하여
java_member 테이블의 회원정보를 수정해 봅시다.
**/
public class PreparedStatementTest {

	public static void main(String[] args) throws Exception {
		Scanner sc = new Scanner(System.in);

		System.out.println("수정할 회원의 ID 입력 : ");
		String id = sc.nextLine();

		System.out.println("수정할 회원의 이름 입력 : ");
		String name = sc.nextLine();

		System.out.println("수정할 회원의 연락처 입력 : ");
		String tel = sc.nextLine();

		System.out.println("수정할 회원의 비밀번호 입력 : ");
		String pw = sc.nextLine();

		System.out.println(id + "/" + name + "/" + tel + "/" + pw);

		// 1. 드라이버 로드
		Class.forName("oracle.jdbc.driver.OracleDriver");

		// 2. DB연결
		String url = "jdbc:oracle:thin:@localhost:1521:XE";
		String user = "scott", pwd = "tiger";

		Connection con = DriverManager.getConnection(url, user, pwd);
		System.out.println("DB Connected...");

		// 3. sql문 작성
		// PreparedStatement이용시에는 값에 해당하는 부분을 ? (in parameter)로 기술
		String sql = "UPDATE java_member SET name = ?, tel = ?, pw = ? WHERE id = ?";

		// 4. PreparedStatement 객체 얻기
		// Connection의 PrepareStatement(쿼리문) 메서드를 이용
		// ? 를 제외한 sql문을 전처리
		PreparedStatement pstmt = con.prepareStatement(sql);

		// 5.실행시키기 전에 ? 에 해당하는 값 설정
		pstmt.setString(1, name);
		pstmt.setString(2, tel);
		pstmt.setString(3, pw);
		pstmt.setString(4, id);

		// 6. 쿼리문 실행
		int cnt = pstmt.executeUpdate();
		System.out.println(cnt + "개의 레코드 수정 완료");

		// jdbc는 auto commit

		// 7. DB자원 반납
		pstmt.close();
		con.close();

	}

}
