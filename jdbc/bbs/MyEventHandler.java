package jdbc.bbs;

// 이벤트 핸들러 => Application Layer
// UI <=== Application Luyer ===> Data Layer ===> DB
// UI와 DB를 연결해주는 다리역할
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

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
			joinMember();

		} else if (obj == gui.btClear) { // 지우기
			gui.clear1();

		} else if (obj == gui.btList) { // 회원목록
			listMember();

		} else if (obj == gui.btDel) { // 회원탈퇴
			removeMember();

		} else if (obj == gui.bbsWrite) { // 게시판 글쓰기(실습)
			border();

		} else if (obj == gui.btLogin) { // 로그인처리(실습)
			login();

		} else if (obj == gui.bbsList) { // 게시판 글목록
			listBbs();

		} else if (obj == gui.bbsDel) { // 게시글 삭제
			// 로그인 한 사람이 자신이 쓴 글만 삭제
			removeBbs();

		} else if (obj == gui.bbsFind) {
			// title로 검색
			search();
		}
	}

	// title로 검색
	private void search() {
		try {
			String schTitle = JOptionPane.showInputDialog(gui, "제목으로 검색합니다.", "글검색", JOptionPane.INFORMATION_MESSAGE);
			ArrayList<BbsVO> schBbs = bbsDAO.selectTitle(schTitle);

			if (schTitle == null) {
				return;
			}
			if (schTitle.trim().equals("")) {
				gui.showMsg("검색할 제목을 입력해주세요.");
				return;
			} else if (schTitle != null && !schTitle.trim().equals("")) {
//				gui.showMsg(schTitle);
				gui.showBbs(schBbs);
				gui.tabbedPane.setSelectedIndex(3);
			}
		} catch (SQLException e) {
			gui.showMsg(e.getMessage());
		}
	}

	// 글삭제
	private void removeBbs() {
		int delNum = Integer.parseInt(gui.tfDelNum.getText());

		try {
			if (delNum > bbsDAO.selectNum()) {

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// 게시판 전체 글목록 가져오기
	private void listBbs() {
		try {
			ArrayList<BbsVO> listBbs = bbsDAO.selectAll();

			gui.showBbs(listBbs);
			// 전체 글 목록으로 이동
			gui.tabbedPane.setSelectedIndex(3);
		} catch (SQLException e) {
			gui.showMsg(e.getMessage());
		}

	}

	// 로그인
	private void login() {
		// id, pw 값 받기
		String loginId = gui.loginId.getText();
		char[] ch = gui.loginPwd.getPassword();
		String loginPw = new String(ch);

		// 유효성 체크
//		if (loginId == null || ch == null || loginId.trim().isEmpty() || loginPw.trim().isEmpty()) {
//			gui.showMsg("아이디와 비밀번호를 입력하세요.");
//			gui.loginId.requestFocus();
//			return;
//		}
//
//		// userDAO의 loginCheck(id, pw 호출)
//		try {
//			int result = userDAO.loginCheck(loginId, loginPw);
//			System.out.println("result : " + result);
//			if (result > 0) {
//				// 결과값이 1이면 로그인 성공
//				gui.showMsg(loginId + "님 환영합니다!");
//				gui.tabbedPane.setEnabledAt(2, true); // 게시판 탭 활성화
//				gui.tabbedPane.setEnabledAt(3, true);
//				gui.setTitle(loginId + "님 로그인 중...");
//				gui.tfWriter.setText(loginId); // 게시글 작성자를 로그인한 사람의 아이디로 설정
//				gui.tabbedPane.setSelectedIndex(2); // 글목록으로 탭 이동
//			} else {
//				gui.showMsg("아이디 또는 비밀번호가 일치하지 않습니다.");
//				gui.tabbedPane.setEnabledAt(2, false); // 게시판 탭 비활성화
//				gui.tabbedPane.setEnabledAt(3, false);
//			}
//		} catch (SQLException e) {
//			gui.showMsg(e.getMessage());
//		}

		if (loginId == null || loginId.trim().equals("")) {
			gui.showMsg("ID를 입력하셔야 합니다.");
			gui.loginId.requestFocus();
			return;
		} else if (loginPw == null || loginPw.trim().equals("")) {
			gui.showMsg("비밀번호를 입력하셔야 합니다.");
			gui.loginPwd.requestFocus();
			return;
		}

		try {
			if (userDAO.loginCheck(loginId, loginPw) == -1) {
				gui.showMsg("존재하지 않는 ID입니다.");
				gui.loginId.requestFocus();
				return;
			} else if (userDAO.loginCheck(loginId, loginPw) == -2) {
				gui.showMsg("비밀번호가 틀렸습니다.");
				gui.loginPwd.requestFocus();
				return;
			} else if (userDAO.loginCheck(loginId, loginPw) == 1) {
				gui.showMsg("성공적으로 로그인 되었습니다.");
				// 로그인, 회원가입 탭 비활성화
				gui.tabbedPane.setEnabledAt(0, false);
				gui.tabbedPane.setEnabledAt(1, false);
				gui.tabbedPane.setEnabledAt(2, true);
				gui.tabbedPane.setSelectedIndex(3);
				gui.tabbedPane.setEnabledAt(3, true);
				gui.tabbedPane.setEnabledAt(4, true);

				// 글번호 보여주기
				gui.showNum();

				// 로그인한 ID 보여주기
				gui.tfWriter.setText(loginId);
				gui.tfWriter.setEditable(false);

				// 게시판 전체 글목록 가져오기
				listBbs();

				// 로그인한 ID의 글목록 가져오기
				ArrayList<BbsVO> listBbs = bbsDAO.selectMyBbs(loginId);
				gui.showMyBbs(listBbs);
			}
		} catch (SQLException e) {
			gui.showMsg(e.getMessage());
		}

	}

	// 회원목록
	private void listMember() {
		try {
			// userDAO의 selectAll() 호출
			ArrayList<MemberVO> userList = userDAO.selectAll();

			// 반환 받은 ArrayList에서 회원정보를 꺼내서 taMembers에 출력
			gui.showMembers(userList);
		} catch (SQLException e) {
			gui.showMsg(e.getMessage());
		}
	}// listMember()-----------------------

	// 회원탈퇴
	private void removeMember() {
		// 1. 입력한 id값 받기
		String delId = gui.tfId.getText();

		// 2. 유효성 체크
		if (delId == null || delId.trim().equals("")) {
			gui.showMsg("ID를 입력하셔야 합니다.");
			gui.tfId.requestFocus();
			return;
		}

		// 3. userDAO의 deleteMember(id) 호출
		try {
			int n = userDAO.deleteMember(delId.trim());

			// 4. 그 결과 메시지 처리
			String msg = (n > 0) ? "회원탈퇴 완료!" : "탈퇴 실패(존재하지 않는 ID입니다.)";
			gui.showMsg(msg);

			if (n > 0) {
				// 글쓰기, 글목록 탭은 비활성화
				gui.tabbedPane.setEnabledAt(2, false);
				gui.tabbedPane.setEnabledAt(3, false);
				gui.tabbedPane.setEnabledAt(4, false);
				gui.clear1();
				gui.tabbedPane.setSelectedIndex(0); // 로그인 탭 선택
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}// removeMember()-------------------------

	// 회원가입
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

	// 게시판 글쓰기
	private void border() {
		try {
			int no = bbsDAO.selectNum();
			String title = gui.tfTitle.getText();
			String writer = gui.tfWriter.getText();
			String content = gui.taContent.getText();

			if (title == null || title.equals("")) {
				gui.showMsg("글제목은 필수 입력사항 입니다.");
				gui.tfNo.requestFocus();
				return;
			}

			// 입력값들을 BbsVO객체에 담아주기
			BbsVO border = new BbsVO(no, title, writer, content, null);
			int n = bbsDAO.insertBbs(border);

			if (n > 0) {
				gui.showMsg("글쓰기 완료");
				gui.clear2();
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}// border()-------------------------------
}
