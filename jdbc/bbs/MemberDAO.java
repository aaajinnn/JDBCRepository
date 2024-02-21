package jdbc.bbs;

import java.sql.*;
import java.util.*;

// DAO (Data Access Object) : Database에 접근하여 CRUD로직을 수행하는 객체 
// ==> Data Layer (Persistence Layer) ==> Model에 해당
public class MemberDAO {

	private Connection con;
	private PreparedStatement ps;
	private ResultSet rs;

	/** 회원가입 처리 - C (INSERT) */
	// INSERT, DELETE, UPDATE는 반환타입을 int or boolean 사용
	public int insertNumber(MemberVO user) throws SQLException {
		// DAO가 화면단이 아니기때문에 사용자에게 무엇때문에 에러가 났는지 알려주기 위해서는
		// try~catch보단 throws하여 넘겨주는것이 좋음
		// => 이벤트핸들러가 예외를 받아 화면에 넘겨줌
		try {
			con = DBUtil.getCon();

			String sql = "INSERT INTO java_member(id, name, pw, tel, indate)";
			sql += " VALUES (?, ?, ?, ?, sysdate)";

			ps = con.prepareStatement(sql);
			ps.setString(1, user.getId()); // 사용자가 입력한 값을 받아 핸들러가 넘겨줌
			ps.setString(2, user.getName());
			ps.setString(3, user.getPw());
			ps.setString(4, user.getTel());

			int n = ps.executeUpdate();
			return n;

		} finally {
			close();
		}
	}// ----------------------------

	public void close() {
		try {
			if (rs != null)
				rs.close();
			if (ps != null)
				ps.close();
			if (con != null)
				con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}// close()---------------------

}
