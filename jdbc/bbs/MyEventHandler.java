package jdbc.bbs;

// 이벤트 핸들러 => Application Layer
// UI <=== Application Luyer ===> Data Layer ===> DB
// UI와 DB를 연결해주는 다리역할
import java.awt.event.*;
import java.sql.SQLException;

public class MyEventHandler implements ActionListener {

	private MyBoardApp gui; // View
	private MemberDAO userDAO; // Model
	private BbsDAO bbsDAO; // Model

	public MyEventHandler(MyBoardApp app) {
		// handler = new MyEventHandler(this) 의 this를 전달함
		this.gui = app;
		userDAO = new MemberDAO();
		bbsDAO = new BbsDAO();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();

		if (obj == gui.btJoin) { // 회원가입
//			gui.setTitle("이벤트 연동 성공");
			joinMember();

		} else if (obj == gui.btClear) { // 지우기
			gui.clear1();

		} else if (obj == gui.btList) { // 회원목록

		} else if (obj == gui.btDel) { // 회원탈퇴

		} else if (obj == gui.bbsWrite) { // 게시판 글쓰기
			border();
		}
	}

	private void joinMember() {
		// 1.입력값 받기
		String id = gui.tfId.getText();
		String name = gui.tfName.getText();
		String pw = gui.tfPw.getText();
		String tel = gui.tfTel.getText();

		// 2.유효성 체크(id,pw,name)
		if (id == null || name == null || pw == null || id.trim().isEmpty() || name.trim().isEmpty()
				|| pw.trim().isEmpty()) { // 빈문자열 체크
			gui.showMsg("아이디, 비밀번호, 이름은 필수 입력사항입니다.");
			gui.tfId.requestFocus();
			return;

		}
		// 3. 입력값들을 MemberVO객체에 담아주기
		MemberVO user = new MemberVO(id, pw, name, tel, null); // 가입일은 sysdate로 처리할것이기 때문에 null로 처리

		// 4. userDAO의 insertMember() 호출 => DB에 넣는 작업
		try {
			int n = userDAO.insertNumber(user);

			// 5. 결과에 따른 메시지 처리
			String msg = (n > 0) ? "회원가입 완료 - 로그인으로 이동합니다." : "회원가입 실패";
			gui.showMsg(msg);

			if (n > 0) {
				gui.tabbedPane.setSelectedIndex(0); // 로그인탭 선택하여 이동
				gui.clear1();
			}
		} catch (SQLException e) {
			gui.showMsg("이미 사용중인 아이디 입니다." + e.getMessage());
		}

	}// joinMember()-------------------------

	private void border() {

		String no = String.valueOf(gui.tfNo.getText());
		String title = gui.tfTitle.getText();
		String writer = gui.tfWriter.getText();
		String content = gui.taContent.getText();

		// 유효성 체크
		if (no == null || no.equals("") || title == null || title.equals("")) {
			gui.showMsg("글번호, 글제목은 필수 입력사항 입니다.");
			gui.tfNo.requestFocus();
			return;
		}

		// 입력값들을 BbsVO객체에 담아주기
		BbsVO border = new BbsVO(Integer.parseInt(no), title, writer, content, null);

		try {
			int n = bbsDAO.insertBbs(border);

			if (n > 0) {
				gui.showMsg("글쓰기 완료");
				gui.clear1();
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
