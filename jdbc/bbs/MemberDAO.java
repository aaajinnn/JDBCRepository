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

	/** 회원 탈퇴 처리 - D (DELETE) */
	public int deleteMember(String id) throws SQLException {
		try {
			con = DBUtil.getCon();
			// delete문 작성
			String sql = "DELETE FROM java_member WHERE id=?";

			// ps얻기
			ps = con.prepareStatement(sql);

			// ? setting
			ps.setString(1, id);

			// 실행 => 실행결과 반환
			int n = ps.executeUpdate();
			return n;

		} finally {
			close();
		}
	}// ----------------------------

	private ArrayList<MemberVO> makeList(ResultSet rs) throws SQLException {
		ArrayList<MemberVO> arr = new ArrayList<>();
		while (rs.next()) {
			String id = rs.getString("id");
			String pw = rs.getString("pw");
			String name = rs.getString("name");
			String tel = rs.getString("tel");
			java.sql.Date indate = rs.getDate("indate");
			// => 가져온객체를 VO에 담기
			MemberVO record = new MemberVO(id, pw, name, tel, indate);
			arr.add(record);
		}

		return arr;
	}// ----------------------------

	/** 회원 목록 가져오기 - R (SELECT) => 다중행 반환하는 경우 */
	public ArrayList<MemberVO> selectAll() throws SQLException {
		// MemberVO 하나가 행 하나, ArrayList에 차곡차곡 담아 테이블이 됨
		try {
			con = DBUtil.getCon();
			String sql = "SELECT * FROM java_member ORDER BY indate DESC";
			ps = con.prepareStatement(sql);
			// ? 가없으면 setting X
			rs = ps.executeQuery();

			// ResultSet 모듈화
			return makeList(rs);

		} finally {
			close();
		}
	}// ----------------------------

	/** PK - id로 회원정보 가져오기 - R(SELECT) => 단일행을 반환하는 경우 */
	public MemberVO findById(String id) throws SQLException {
		try {
			con = DBUtil.getCon();
			String sql = "SELECT * FROM java_member WHERE id=?";
			ps = con.prepareStatement(sql);
			ps.setString(1, id);
			rs = ps.executeQuery();

			ArrayList<MemberVO> arr = makeList(rs);
			if (arr != null && arr.size() == 1) { // 해당 id가 있다면 회원정보 반환
				MemberVO user = arr.get(0);
				return user;
			}

			return null; // 해당 id가 없는 경우
		} finally {
			close();
		}
	}// ----------------------------

	// id가 없을 경우 -1, 비밀번호가 틀릴 경우 -2, 인증받은 경우 1 반환
	public int loginCheck(String id, String pw) throws SQLException {
		MemberVO tmpUser = this.findById(id);
		// id가 없는 경우
		if (tmpUser == null)
			return -1;

		// id가 있는 경우는 비밀번호 일치여부 체크
		if (!tmpUser.getPw().equals(pw))
			return -2; // 비밀번호 불일치

		return 1; // 회원 인증 받은 경우

	}

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
