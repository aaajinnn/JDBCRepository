package day02;
//ex03

import java.sql.*;
import java.util.*;

public class Insert_MySQL {

	public static void main(String[] args) throws Exception {
		Scanner sc = new Scanner(System.in);
		System.out.println("::회원가입::");
		System.out.println("아이디 : ");
		String id = sc.nextLine();

		System.out.println("비밀번호 : ");
		String pw = sc.nextLine();

		System.out.println("이름 : ");
		String name = sc.nextLine();

		System.out.println("연락처 : ");
		String tel = sc.nextLine();

		Class.forName("com.mysql.cj.jdbc.Driver");
		System.out.println("드라이버 로딩 성공");

		String url = "jdbc:mysql://localhost:3307/mydb?useUnicode=true&serverTimezone=Asia/Seoul";
		String user = "root", pwd = "1234";
		Connection con = DriverManager.getConnection(url, user, pwd);
		System.out.println("DB 연결 성공");

//		String sql = "INSERT INTO java_member(id, pw, name, tel, indate) ";
//		sql += " VALUES(' " + id + "',' " + pw + "','" + name + "', '" + tel + "',current_date)";

		String sql = "INSERT INTO java_member(id, pw, name, tel, indate) ";
		sql += " VALUES(' " + id + "',' " + pw + "','" + name + "', '" + tel + "',curdate())"; // now()
		System.out.println(sql);

		Statement stmt = con.createStatement();

		// DML(insert, update, delete)을 실행시킬 때
		// public int executeUpdate(String sql)를 사용하는 것이 좋다.
		// ==> sql문장에 의해 영향받은 레코드 개수를 반환한다.
		int cnt = stmt.executeUpdate(sql);
		System.out.println(cnt + "개의 레코드를 삽입했습니다.");

		stmt.close();
		con.close();
	}

}
