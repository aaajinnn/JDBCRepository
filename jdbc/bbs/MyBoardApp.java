package jdbc.bbs;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
/* 디자인 패턴
 * - MVC패턴
 * 		- M(Model)데이터를 가지는 부분 DAO, VO ==> Data Layer
 * 		- V(View) : 화면(UI)를 구성하는 부분 MyBoardApp(Swing) ==> Presentation Layer
 * 		- C(Controller) : Model과 View사이에서 제어하는 부분 MyEventHandler ==> Application Layer
 * 							사용자가 입력한 값을 모델에 넘긴다.
 * 							DB에서 가져온 값을 화면쪽에 보여준다
 * 							...
 * 							제어 흐름을 담당하는 부분
 * 
 * */
import java.util.ArrayList;

// 화면 계층(Presentation Layer) => GUI를 구성
public class MyBoardApp extends JFrame {
	// 핸들러에 접근하기 위해서는 private 제거=>생략형으로(같은패키지에서는 접근가능하기때문에 생략해줌)

	// 화면
	JTextField loginId;
	JPasswordField loginPwd;
	JTextField tfId;
	JTextField tfPw;
	JTextField tfName;
	JTextField tfTel;
	CardLayout card;
	JTextField tfNo;
	JTextField tfWriter;
	JTextField tfTitle;
	JButton btLogin, btJoin, btDel, btList, btClear;
	JButton bbsWrite, bbsFind, bbsList, bbsDel;
	JTextArea taMembers, taList, taMyList, taContent;
	JTabbedPane tabbedPane;

	// 이벤트핸들러
	MyEventHandler handler; // Controller ===> 화면구성 다 한 후 생성자끝부분에서 생성 하기

	public MyBoardApp() {
		super("::MyBoardApp::");

		JPanel panel_1 = new JPanel();
		getContentPane().add(panel_1, "Center");
		panel_1.setLayout(new BorderLayout(0, 0));

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		panel_1.add(tabbedPane, BorderLayout.CENTER);

		JPanel panel_2 = new JPanel();
		panel_2.setBackground(new Color(50, 205, 50));
		tabbedPane.addTab("로그인", null, panel_2, null);
		panel_2.setLayout(null);

		JLabel lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setIcon(new ImageIcon(MyBoardApp.class.getResource("/jdbc/bbs/logo2.png")));
		lblNewLabel_1.setBounds(49, 77, 280, 114);
		panel_2.add(lblNewLabel_1);

		loginId = new JTextField();
		loginId.setBounds(49, 201, 280, 59);
		panel_2.add(loginId);
		loginId.setColumns(10);
		loginId.setBorder(new TitledBorder("::ID::"));

		loginPwd = new JPasswordField();
		loginPwd.setBounds(49, 270, 280, 57);
		panel_2.add(loginPwd);
		loginPwd.setBorder(new TitledBorder("::PASSWORD::"));

		btLogin = new JButton("로  그  인");
		btLogin.setFont(new Font("SansSerif", Font.BOLD, 14));
		btLogin.setForeground(new Color(245, 255, 250));
		btLogin.setBackground(new Color(0, 139, 139));
		btLogin.setBounds(49, 342, 280, 45);
		panel_2.add(btLogin);

		JPanel panel_3 = new JPanel();
		panel_3.setBackground(new Color(255, 245, 238));
		tabbedPane.addTab("회원가입", null, panel_3, null);
		panel_3.setLayout(null);

		JLabel lblNewLabel_2 = new JLabel("::회원 가입::");
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		lblNewLabel_2.setBounds(80, 23, 199, 46);
		panel_3.add(lblNewLabel_2);

		tfId = new JTextField();
		tfId.setBounds(85, 92, 260, 46);
		panel_3.add(tfId);
		tfId.setColumns(10);

		tfPw = new JTextField();
		tfPw.setColumns(10);
		tfPw.setBounds(85, 149, 260, 46);
		panel_3.add(tfPw);

		tfName = new JTextField();
		tfName.setColumns(10);
		tfName.setBounds(85, 205, 260, 46);
		panel_3.add(tfName);

		tfTel = new JTextField();
		tfTel.setColumns(10);
		tfTel.setBounds(85, 261, 260, 46);
		panel_3.add(tfTel);

		JLabel lblNewLabel_3 = new JLabel("아 이 디:");
		lblNewLabel_3.setBounds(12, 92, 61, 46);
		panel_3.add(lblNewLabel_3);

		JLabel lblNewLabel_3_1 = new JLabel("비밀번호:");
		lblNewLabel_3_1.setBounds(12, 149, 61, 46);
		panel_3.add(lblNewLabel_3_1);

		JLabel lblNewLabel_3_2 = new JLabel("이    름:");
		lblNewLabel_3_2.setBounds(12, 205, 61, 46);
		panel_3.add(lblNewLabel_3_2);

		JLabel lblNewLabel_3_3 = new JLabel("연 락 처:");
		lblNewLabel_3_3.setBounds(12, 261, 61, 46);
		panel_3.add(lblNewLabel_3_3);

		JPanel panel_6 = new JPanel();
		panel_6.setBounds(12, 336, 355, 46);
		panel_3.add(panel_6);
		panel_6.setLayout(new GridLayout(1, 0, 0, 0));

		btJoin = new JButton("회원가입");
		btJoin.setFont(new Font("SansSerif", Font.PLAIN, 12));
		btJoin.setBackground(Color.PINK);
		panel_6.add(btJoin);

		btDel = new JButton("회원탈퇴");
		btDel.setFont(new Font("SansSerif", Font.PLAIN, 12));
		btDel.setForeground(new Color(240, 255, 255));
		btDel.setBackground(Color.BLUE);
		panel_6.add(btDel);

		btList = new JButton("회원목록");
		btList.setFont(new Font("SansSerif", Font.PLAIN, 12));
		btList.setForeground(new Color(240, 248, 255));
		btList.setBackground(Color.MAGENTA);

		panel_6.add(btList);

		btClear = new JButton("지 우 기");
		btClear.setFont(new Font("SansSerif", Font.PLAIN, 12));
		btClear.setBackground(new Color(135, 206, 235));

		panel_6.add(btClear);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 392, 355, 230);
		panel_3.add(scrollPane);

		taMembers = new JTextArea();
		scrollPane.setViewportView(taMembers);
		taMembers.setBorder(new TitledBorder(":::회원목록:::"));

		JPanel panel = new JPanel();
		tabbedPane.addTab("게시판 글쓰기", null, panel, null);
		panel.setLayout(null);

		JPanel panel_4 = new JPanel();
		panel_4.setLayout(null);
		panel_4.setBounds(12, 20, 344, 477);
		panel.add(panel_4);

		tfNo = new JTextField();
		tfNo.setColumns(10);
		tfNo.setBounds(12, 66, 312, 45);
		panel_4.add(tfNo);
		tfNo.setBorder(new TitledBorder("글번호(NO)"));

		tfTitle = new JTextField();
		tfTitle.setColumns(10);
		tfTitle.setBounds(12, 121, 312, 53);
		panel_4.add(tfTitle);
		tfTitle.setBorder(new TitledBorder("글제목(Title)"));

		tfWriter = new JTextField();
		tfWriter.setColumns(10);
		tfWriter.setBounds(12, 182, 312, 46);
		panel_4.add(tfWriter);
		tfWriter.setBorder(new TitledBorder("작성자(Writer)"));

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(12, 238, 312, 218);
		panel_4.add(scrollPane_1);

		taContent = new JTextArea();
		scrollPane_1.setViewportView(taContent);
		taContent.setBorder(new TitledBorder("글내용(Content)"));
		// -게시판 목록--------------------------------------
		JLabel lblNewLabel = new JLabel("::나의 게시판::");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		lblNewLabel.setBounds(57, 10, 221, 46);
		panel_4.add(lblNewLabel);

		JPanel panel_6_1 = new JPanel();
		panel_6_1.setBounds(12, 528, 355, 46);
		panel.add(panel_6_1);
		panel_6_1.setLayout(new GridLayout(1, 0, 0, 0));

		bbsWrite = new JButton("글쓰기");
		bbsWrite.setActionCommand("글쓰기");
		bbsWrite.setFont(new Font("SansSerif", Font.PLAIN, 12));
		bbsWrite.setBackground(Color.PINK);
		panel_6_1.add(bbsWrite);

		bbsFind = new JButton("글검색");
		bbsFind.setActionCommand("글검색");
		bbsFind.setForeground(new Color(240, 248, 255));
		bbsFind.setFont(new Font("SansSerif", Font.PLAIN, 12));
		bbsFind.setBackground(Color.MAGENTA);
		panel_6_1.add(bbsFind);

		bbsList = new JButton("글목록");
		bbsList.setActionCommand("글목록");
		bbsList.setFont(new Font("SansSerif", Font.PLAIN, 12));
		bbsList.setBackground(new Color(135, 206, 235));
		panel_6_1.add(bbsList);

		JPanel panel_5 = new JPanel();
		tabbedPane.addTab("게시판 목록", null, panel_5, null);
		panel_5.setLayout(null);

		JLabel lblNewLabel_4 = new JLabel(":: 게시판  글목록::");
		lblNewLabel_4.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_4.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		lblNewLabel_4.setBounds(40, 22, 287, 46);
		panel_5.add(lblNewLabel_4);

		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(12, 91, 355, 531);
		panel_5.add(scrollPane_2);

		taList = new JTextArea();
		taList.setEditable(false);
		scrollPane_2.setViewportView(taList);
		taList.setBorder(new TitledBorder("글 목 록"));

		JPanel panel_7 = new JPanel();
		tabbedPane.addTab("나의 게시물", null, panel_7, null);
		panel_7.setLayout(null);

		JLabel lblNewLabel_4_1 = new JLabel(":: 나의 게시물::");
		lblNewLabel_4_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_4_1.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		lblNewLabel_4_1.setBounds(40, 22, 287, 46);
		panel_7.add(lblNewLabel_4_1);

		JScrollPane scrollPane_2_1 = new JScrollPane();
		scrollPane_2_1.setBounds(12, 91, 355, 428);
		panel_7.add(scrollPane_2_1);

		taMyList = new JTextArea();
		taMyList.setEditable(false);
		scrollPane_2_1.setViewportView(taMyList);
		taMyList.setBorder(new TitledBorder("글 목 록"));

		bbsDel = new JButton("글삭제");
		bbsDel.setForeground(new Color(240, 255, 255));
		bbsDel.setFont(new Font("SansSerif", Font.PLAIN, 12));
		bbsDel.setBackground(Color.BLUE);
		bbsDel.setActionCommand("글삭제");
		bbsDel.setBounds(146, 529, 88, 46);
		panel_7.add(bbsDel);

		// 이벤트 핸들러 생성 => 외부클래스로 구성했다면 this정보를 전달하자
		handler = new MyEventHandler(this); // this를 넘겨 핸들러가 얘를 제어하도록

		// 이벤트 소스와 연결
		// member
		btJoin.addActionListener(handler); // MyEventHandler가 ActionListener를 상속받아 얻어옴
		btList.addActionListener(handler);
		btDel.addActionListener(handler);
		btClear.addActionListener(handler);
		// bbs
		bbsWrite.addActionListener(handler);
		bbsList.addActionListener(handler);
		bbsFind.addActionListener(handler);
		bbsDel.addActionListener(handler);
		// login
		btLogin.addActionListener(handler);

		// 초기에 글쓰기, 글목록 탭은 비활성화 ==> 로그인해야 활성화
//		tabbedPane.setEnabledAt(2, false); // 글쓰기
//		tabbedPane.setEnabledAt(3, false); // 글목록

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(419, 740);

		setVisible(true);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new MyBoardApp();

	}

	// 회원가입 입력필드 지우기
	public void clear1() {
		this.tfId.setText("");
		this.tfName.setText("");
		this.tfPw.setText("");
		this.tfTel.setText("");
		this.taMembers.setText("");
		this.tfId.requestFocus();
	}

	// 게시판 입력필드 지우기
	public void clear2() {
		this.tfTitle.setText("");
		this.taContent.setText("");
	}

	// 메시지박스
	public void showMsg(String msg) {
		JOptionPane.showMessageDialog(this, msg);
	}

	// 회원목록 보기
	public void showMembers(ArrayList<MemberVO> userList) {
		if (userList == null)
			return;
		if (userList.size() == 0) {
			taMembers.setText("등록된 회원은 없습니다.");
			return;
		}
		taMembers.setText("");
		taMembers.append("===============================================================\n");
		taMembers.append("ID\tName\tTel\t\tIndate\n");
		taMembers.append("===============================================================\n");
		for (MemberVO user : userList) {
			taMembers.append(
					user.getId() + "\t" + user.getName() + "\t" + user.getTel() + "\t\t" + user.getIndate() + "\n");
		}
		taMembers.append("===============================================================\n");
	}

	// 글번호 보여주기
	public void showNum() {
		BbsDAO bbsDAO = new BbsDAO();
		try {
			int no = bbsDAO.selectNum();
			tfNo.setText(String.valueOf(no));
			tfNo.setEditable(false);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 전체 게시물 보기
	public void showBbs(ArrayList<BbsVO> bbsList) {
		if (bbsList == null)
			return;
		if (bbsList.size() == 0) {
			taList.setText("등록된 게시글이 없습니다.");
		}
		taList.setText("");
		taList.append(
				"===============================================================================================\n");
		taList.append("No\tWdate\tWriter\tTitle\t\tContent\n");
		taList.append(
				"===============================================================================================\n");
		for (BbsVO bbs : bbsList) {
			taList.append(bbs.getNo() + "\t" + bbs.getWdate() + "\t" + bbs.getWriter() + "\t" + bbs.getTitle() + "\t\t"
					+ bbs.getContent() + "\n");
		}
		taList.append(
				"===============================================================================================\n");
	}

	// 나의 게시물 보기
	public void showMyBbs(ArrayList<BbsVO> myBbsList) {
		if (myBbsList == null)
			return;
		if (myBbsList.size() == 0) {
			taList.setText("등록된 게시글이 없습니다.");
		}
		taMyList.setText("");
		taMyList.append(
				"===============================================================================================\n");
		taMyList.append("No\tWdate\tWriter\tTitle\t\tContent\n");
		taMyList.append(
				"===============================================================================================\n");
		for (BbsVO bbs : myBbsList) {
			taMyList.append(bbs.getNo() + "\t" + bbs.getWdate() + "\t" + bbs.getWriter() + "\t" + bbs.getTitle()
					+ "\t\t" + bbs.getContent() + "\n");
		}
		taMyList.append(
				"===============================================================================================\n");
	}
}
