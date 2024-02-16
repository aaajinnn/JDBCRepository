package day02;
// ex02

import java.sql.*;

// MySQL jdbc driver 다운로드
// D:\multicampus\Util\mysql-connector-j-8.0.33\mysql-connector-j-8.0.33
public class DBConnect_MySQL {

	public static void main(String[] args) throws Exception {
		// 1. Driver Loading
		Class.forName("com.mysql.cj.jdbc.Driver");
		System.out.println("드라이버 로딩 성공");

		// 2.DB연결
		// 포트번호 오류로 3306-> 3307로 변경
		String url = "jdbc:mysql://localhost:3307/mydb?useUnicode=true&serverTimezone=Asia/Seoul";
		String user = "root", pwd = "1234";
		Connection con = DriverManager.getConnection(url, user, pwd);
		System.out.println("MySQL DB연결 성공");

		String sql = "CREATE TABLE java_member(";
		sql += " id varchar(20) primary key,";
		sql += " pw varchar(20) not null,";
		sql += " name varchar(30) not null,";
		sql += " tel varchar(15),";
		sql += " indate date default (current_date) )";
		System.out.println(sql);

		Statement stmt = con.createStatement();
		boolean b = stmt.execute(sql);
		System.out.println("b : " + b);

		stmt.close();
		con.close();

	}

}
