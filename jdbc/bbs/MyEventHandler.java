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
			String title = JOptionPane.showInputDialog(gui, "제목을 입력해주세요.", "글검색", JOptionPane.INFORMATION_MESSAGE);
			ArrayList<BbsVO> schBbs = bbsDAO.selectTitle(title);

			if (title == null) {
				gui.tfTitle.requestFocus();
				return;
			} else if (title.trim().isEmpty()) {
				gui.showMsg("검색할 제목을 입력해주세요.");
				return;
			}

			gui.showMsg("[" + title + "]을/를 검색합니다.");
			if (schBbs.size() == 0) {
				gui.showMsg("등록된 게시글이 없습니다.");
				return;
			}

			if (schBbs.size() > 0) {
				gui.showBbs(schBbs);
				gui.tabbedPane.setSelectedIndex(3);
			}

		} catch (SQLException e) {
			gui.showMsg(e.getMessage());
		}
	}// search()-----------------------

	// 글삭제
	private void removeBbs() {
		String delNum = (String) JOptionPane.showInputDialog(gui, "삭제할 글번호를 입력하세요.", "글삭제",
				JOptionPane.INFORMATION_MESSAGE);
		String id = gui.loginId.getText();
		try {
//			gui.showMsg(id + "님이" + delNum + "를 입력함");
			if (delNum == null) {
				System.out.println("cancle");
				return;
			}
			if (Integer.parseInt(delNum) > bbsDAO.selectNum()) {
				gui.showMsg("유효하지 않은 번호입니다.");
				return;
			}

			int n = bbsDAO.deleteBbs(id, Integer.parseInt(delNum));
			String msg = (n > 0) ? "삭제 완료" : "삭제 실패! 다시 시도해주세요.";
			gui.showMsg(msg);

			// 로그인한 ID의 글목록 가져오기
			ArrayList<BbsVO> listBbs = bbsDAO.selectMyBbs(id);
			gui.showMyBbs(listBbs);

			// 삭제 후 전체 글목록도 수정
			gui.bbsAll();

		} catch (NumberFormatException e) {
			gui.showMsg("정수를 입력해 주세요.");
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}// removeBbs()-----------------------

	// 전체 글 목록으로 이동
	private void listBbs() {
		gui.bbsAll();
		gui.tabbedPane.setSelectedIndex(3);
	}// listBbs()-----------------------

	// 로그인
	private void login() {
		// id, pw 값 받기
		String loginId = gui.loginId.getText();
		char[] ch = gui.loginPwd.getPassword();
		String loginPw = new String(ch);

		// 유효성 체크
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
				gui.setTitle(loginId + "님 로그인 중...");
				// 로그인, 회원가입 탭 비활성화
				gui.tabbedPane.setEnabledAt(0, false);
				gui.tabbedPane.setEnabledAt(1, false);
				gui.tabbedPane.setEnabledAt(2, true);
				gui.tabbedPane.setSelectedIndex(2);
				gui.tabbedPane.setEnabledAt(3, true);
				gui.tabbedPane.setEnabledAt(4, true);

				// 글번호 보여주기 및 비활성화
				gui.showNum();

				// 로그인한 ID 보여주기 및 비활성화
				gui.tfWriter.setText(loginId);
				gui.tfWriter.setEditable(false);

				// 게시판 전체 글목록 가져오기
				gui.bbsAll();

				// 로그인한 ID의 글목록 가져오기
				ArrayList<BbsVO> listMyBbs = bbsDAO.selectMyBbs(loginId);
				gui.showMyBbs(listMyBbs);

				// 로그인한 ID의 글목록이 없을때 삭제버튼 비활성화
				if (listMyBbs.size() == 0) {
					gui.bbsDel.setEnabled(false);
				}
			}
		} catch (SQLException e) {
			gui.showMsg(e.getMessage());
		}

	}// login()-----------------------------

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
	}// listMember()-------------------------

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
		if (id.length() > 20) {
			gui.showMsg("아이디는 20자 이내로 입력하세요.");
			gui.tfId.setText("");
			gui.tfId.requestFocus();
			return;
		} else if (pw.length() > 10) {
			gui.showMsg("비밀번호는 10자 이내로 입력하세요.");
			gui.tfPw.setText("");
			gui.tfPw.requestFocus();
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

				// 게시판 전체 글목록 가져와 글목록으로 이동
				listBbs();

				// 로그인한 ID의 글목록 가져오기
				ArrayList<BbsVO> listMyBbs = bbsDAO.selectMyBbs(writer);
				gui.showMyBbs(listMyBbs);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}// border()-------------------------------
}
