package jdbc.bbs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// 게시판 관련 CRUD 수행 => data layer
public class BbsDAO {

	private Connection con;
	private PreparedStatement ps;
	private ResultSet rs;

	// 게시글 쓰기(시퀀스 - Bbs_no_seq)
	public int insertBbs(BbsVO vo) throws SQLException {
		try {
			con = DBUtil.getCon();

			String sql = "INSERT INTO bbs (no, title, writer, content, wdate)";
			sql += " VALUES(BBS_NO_SEQ.NEXTVAL, ?, ?, ?, sysdate)";

			ps = con.prepareStatement(sql);
			ps.setString(1, vo.getTitle());
			ps.setString(2, vo.getWriter());
			ps.setString(3, vo.getContent());

			int n = ps.executeUpdate();
			return n;

		} finally {
			close();
		}
	}

	// 게시판 마지막 글번호 가져오기
	public int lastNum() throws SQLException {

		try {
			con = DBUtil.getCon();
			String sql = "SELECT last_number FROM user_sequences";
			sql += " WHERE sequence_name=?";

			ps = con.prepareStatement(sql);
			ps.setString(1, "BBS_NO_SEQ");

			rs = ps.executeQuery();

			rs.next();
			int n = rs.getInt("last_number");
			return n;
		} finally {
			close();
		}

	}

	// 게시판 마지막 글번호 띄우기
	public void getNumber() {

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
