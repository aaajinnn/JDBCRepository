package day05;

import java.sql.*;
import jdbc.util.DBUtil;

/*참조 커서(refcursor)를 이용하여 프로시저 결과를 ResultSet으로 받아보자
 * ----------------------------------------------
 * CREATE OR REPLACE PROCEDURE bbs_list
(mycr OUT SYS_REFCURSOR)
IS
BEGIN
    OPEN mycr FOR
    SELECT no, title, writer, content, wdate FROM bbs
    ORDER BY no DESC;
END;
 * ----------------------------------------------
/
 * */
public class CallableStatementTest2 {

	public static void main(String[] args) throws Exception {
		String sql = "{call bbs_list(?)}"; // ? : 아웃 파라미터(mycr) Cursor타입
		try (Connection con = DBUtil.getCon(); CallableStatement cs = con.prepareCall(sql);) { // <=close생략

			// 아웃 파라미터 자료형 셋팅(type 설정)
			cs.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR); // CURSOR : OracleTypes가 가지고 있는 상수(int타입)
			cs.execute(); // 프로시저 실행
			ResultSet rs = (ResultSet) cs.getObject(1); // cs.getResultSet() => index를 받아올 수 없어서 사용할 수없음

			while (rs.next()) {
				// 가르키고 있는게 있따면 하나씩 꺼내와
				int no = rs.getInt("no");
				String title = rs.getString("title");
				String writer = rs.getString("writer");
				String content = rs.getString("content");
				Date wdate = rs.getDate("wdate");
				System.out.printf("%d\t%s\t%s\t%s\t%s\n", no, title, writer, wdate.toString(), content);
			}
			rs.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
