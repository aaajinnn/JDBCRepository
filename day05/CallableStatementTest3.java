package day05;

import java.sql.*;
import java.sql.Date;
import java.util.*;
import jdbc.util.*;

/*----------------------------------------------
	 * CREATE OR REPLACE PROCEDURE bbs_find 
	(mycr OUT SYS_REFCURSOR, pwriter IN bbs.writer%type)
	IS
	BEGIN
	    OPEN mycr FOR
	    SELECT no, title, writer, content, wdate FROM bbs
	    WHERE writer LIKE '%'|| pwriter ||'%'
	    ORDER BY no DESC;
	END;
/
 * ----------------------------------------------
	VARIABLE rs refcursor
	EXEC bbs_find(:rs,'k');
	
	print rs
 * 
 * */
public class CallableStatementTest3 {

	public static void main(String[] args) throws Exception {
		Scanner sc = new Scanner(System.in);
		System.out.println("검색할 작성자 : ");
		String writer = sc.nextLine();

		Connection con = DBUtil.getCon();

		String sql = "{call bbs_find(?, ?)}"; // in, out 둘다 받기
		CallableStatement cs = con.prepareCall(sql);
		cs.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR); // 아웃 파라미터 자료형 셋팅(type 설정)
		cs.setString(2, writer);// 인 파라미터로 받기
		cs.execute(); // 프로시저 실행
		ResultSet rs = (ResultSet) cs.getObject(1);

		while (rs.next()) {
			// 가르키고 있는게 있따면 하나씩 꺼내와
			int no = rs.getInt("no");
			String title = rs.getString("title");
			String writer2 = rs.getString("writer");
			String content = rs.getString("content");
			Date wdate = rs.getDate("wdate");
			System.out.printf("%d\t%s\t%s\t%s\t%s\n", no, title, writer2, wdate.toString(), content);
		}

		rs.close();
		cs.close();
		con.close();

	}

}
