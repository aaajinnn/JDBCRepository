package day01;
//Ex01

import java.sql.*; // 인터페이스이기 때문에 new X

// oracle의 jdbc driver => ojdbc6.jar ==> OracleDriver
public class DBConnect_Oracle {

	public static void main(String[] args) {
		try {
			// 1. Driver 로딩
			Class.forName("oracle.jdbc.driver.OracleDriver"); // 통역사 역할
			System.out.println("Driver Loading Success...");

			// 2. DB연결 => DriverManager.getConnection()을 이용
			String url = "jdbc:oracle:thin:@localhost:1521:XE"; // DB접속 url정보.
			// 프로토콜:DBMS 유형:드라이브타입:@host IP주소:port:전역데이터베이스
			String user = "scott", pwd = "tiger";

			Connection con = DriverManager.getConnection(url, user, pwd);
			System.out.println("DB Connected...");

			// 3. SQL문 작성 => java_member테이블을 생성하는 문장
			String sql = "CREATE TABLE java_member(";
			sql += " id varchar2(20) primary key,";
			sql += " pw varchar2(10) not null,";
			sql += " name varchar2(30) not null,";
			sql += " tel varchar2(15),";
			sql += " indate date default sysdate)"; // 세미콜론(;) 없이!!
			System.out.println(sql);

			// 위 SQL문을 번역해서 실행시켜보자
			// 4. Statement객체 얻어오기 = con.createStatement()
			Statement stmt = con.createStatement();

			// 5. statement의 executeXXX()메서드를 이용해서 쿼리문 실행
			boolean b = stmt.execute(sql); // db를 포맷에맞춰 컴파일해 결과 출력
			System.out.println(" b : " + b);
			// sql문이 select문이면 true를 반환
			// select문이 아니면 false를 반환 (=> false를 반환했다고 해서 실패한 것이 아님)

			// 6. DB 연결자원 반납
			if (stmt != null)
				stmt.close(); // 먼저 받은 Connection보다 먼저 닫아줌

			if (con != null)
				con.close(); // 꼭 닫아주어야함!!

		} catch (ClassNotFoundException e) { // Driver 로딩 error
			System.out.println("Driver Loading Fail...");
			e.printStackTrace();
		} catch (SQLException e) {// DB연결 error
			System.out.println("SQL Error...");
			e.printStackTrace();
		}
	}

}
