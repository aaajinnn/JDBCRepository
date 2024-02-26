package day05;

import java.sql.*;
import java.util.*;
import jdbc.util.*;

/*
 * CallableStatementTest
 * -프로시저 호출할 때 사용
 * -Connection의 prepareCall(query)을 통해 호출
 * [1] 인 파라미터가 있을 경우
 * 		{call 프로시저명(인파라미터)}
 * [2] 프로시저명만 있을 경우
 * 		{call 프로시저명}
 * ---------------------------------
 * [실습] bbs에 게시글을 등록하는 프로시저를 작성하고
 * 		jdbc통해 해당 프로시저를 호출해보자
 * ---------------------------------
 * CREATE OR REPLACE PROCEDURE bbs_add
(
    ptitle IN bbs.title%type,
    pwriter IN bbs.writer%type,
    pcontent IN bbs.content%type
)
IS 
BEGIN
    INSERT INTO bbs(no, title, writer, content, wdate)
    VALUES(bbs_no_seq.nextval, ptitle, pwriter, pcontent, sysdate);
    COMMIT;
END;
/
 * */
public class CallableStatementTest {

	public static void main(String[] args) throws Exception {
		Scanner sc = new Scanner(System.in);

		System.out.println("Title : ");
		String title = sc.nextLine();

		System.out.println("Writer : ");
		String writer = sc.nextLine();

		System.out.println("Content : ");
		String content = sc.nextLine();

		System.out.println(title + "/" + writer + "/" + content);

		Connection con = DBUtil.getCon();

		String sql = "{call bbs_add(?,?,?)}"; // in파라미터

		CallableStatement cstmt = con.prepareCall(sql);

		cstmt.setString(1, title);
		cstmt.setString(2, writer);
		cstmt.setString(3, content);

		cstmt.execute();

		cstmt.close();
		con.close();
		System.out.println("게시글 등록 완료!");

	}

}
