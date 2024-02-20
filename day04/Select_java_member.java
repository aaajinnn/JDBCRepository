package day04;

//ex01
import java.util.*;
import jdbc.util.DBUtil;
import java.sql.*;
import java.sql.*;

public class Select_java_member {

	public static void main(String[] args) throws Exception {
		Scanner sc = new Scanner(System.in);
		System.out.println("검색할 회원명 : ");
		String name = sc.nextLine();

		String sql = "SELECT id, name, tel, indate FROM java_member WHERE name=?";

		// -----------------------------------
//		DBUtil util = new DBUtil(); // [x]

		// try with resource ==> try(자원){}catch(예외){}
		// ==> 자원들을 자동으로 close()
		try (Connection con = DBUtil.getCon(); // static인 getCon()을 얻어옴
				PreparedStatement ps = con.prepareStatement(sql);) {

			ps.setString(1, name);

			// 실행 => executeQuery(select문)
			ResultSet rs = ps.executeQuery();
			while (rs.next()) { // next() 커서 이동, 있으면 true를 반환하여 반복문돌려 데이터를 하나씩 꺼냄
				String id = rs.getString(1); // getString(컬럼명); 또는 getString(컬럼 index);
				String name2 = rs.getString(2);
				String tel = rs.getString(3);
				java.sql.Date indate = rs.getDate(4); // String = getString(4);도 가능
				System.out.printf("%s\t%s\t%s\t%s\n", id, name2, tel, indate.toString());
			} // 모두 출력 후 다시 next실행, 레코드 없을 시 false반환해 반복문 벗어남
			if (rs != null)
				rs.close(); // 출력이 끝났다면 rs반환해줌

			System.out.println("DB연결 성공!");
		} catch (Exception e) {
			e.printStackTrace();
		}
//		ps.close();  // 자동으로 close()된다
//		con.close(); // 자동으로 close()된다
		// ------------------------------------
	}

}
