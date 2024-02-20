package day04;

//ex03
import java.util.*;

import jdbc.util.DBUtil;

import java.sql.*;
import java.sql.Date;

public class Select_bbs {

	public static void main(String[] args) throws SQLException {
		// bbs에서 게시글을 검색하되 제목에 들어간 키워드 검색하기
		Scanner sc = new Scanner(System.in);
		System.out.println("게시판 검색 키워드(title) 입력");
		String keyword = sc.nextLine();
		System.out.println(" 검색어 : [" + keyword + "]");

		String sql = "SELECT no, title, writer, content, wdate FROM bbs WHERE title LIKE ?";
		sql += " ORDER BY wdate desc";
		System.out.println(sql);
		try (Connection con = DBUtil.getCon(); PreparedStatement ps = con.prepareStatement(sql);) {
			ps.setString(1, "%" + keyword + "%");

			ResultSet rs = ps.executeQuery();
			System.out.println("======================================================================");
			System.out.println("No\tTitle\t\twriter\twdate\t\tContent\n");
			System.out.println("======================================================================");
			while (rs.next()) {
				int no = rs.getInt(1);
				String title = rs.getString(2);
				String writer = rs.getString(3);
				Date wdate = rs.getDate(5);
				String content = rs.getString(4);
				System.out.printf("%d\t%s\t%s\t%s\t%s\n", no, title, writer, wdate.toString(), content);
			}
			System.out.println("======================================================================");
			if (rs != null)
				rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
