package memberManagement;

import java.awt.Container;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.TextArea;
import java.awt.event.*;
import java.awt.Color;
import java.sql.*;
import java.util.Date;
import java.awt.event.*;

public class MemberJoin extends JFrame {

	Container cp;
	public String str;
	private static JPanel panel;
	private static JTextField tf_id;
	private static JPasswordField passwordField;
	private static JTextField tf_name;
	private static JTextField tf_tel;
	private static JButton btn_join, btn_delete, btn_list;
	public static JTextArea textArea;

	static Connection con;
	static Statement stmt;
	static ResultSet rs;

	MemberJoin() {
		super(":::회원가입:::");

		JDBCConnection();

		cp = this.getContentPane();

		panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);

		JLabel lblNewLabel = new JLabel("join us!");
		lblNewLabel.setFont(new Font("맑은 고딕", Font.BOLD, 40));
		lblNewLabel.setBounds(117, 21, 186, 84);
		panel.add(lblNewLabel);

		JLabel lb_id = new JLabel("ID");
		lb_id.setFont(new Font("맑은 고딕", Font.BOLD, 25));
		lb_id.setBounds(27, 128, 107, 36);
		panel.add(lb_id);

		JLabel lb_pw = new JLabel("PW");
		lb_pw.setFont(new Font("맑은 고딕", Font.BOLD, 25));
		lb_pw.setBounds(27, 183, 107, 36);
		panel.add(lb_pw);

		JLabel lb_name = new JLabel("NAME");
		lb_name.setFont(new Font("맑은 고딕", Font.BOLD, 25));
		lb_name.setBounds(27, 240, 107, 36);
		panel.add(lb_name);

		JLabel lb_tel = new JLabel("TEL");
		lb_tel.setFont(new Font("맑은 고딕", Font.BOLD, 25));
		lb_tel.setBounds(27, 296, 107, 36);
		panel.add(lb_tel);

		tf_id = new JTextField();
		tf_id.setFont(new Font("맑은 고딕", Font.PLAIN, 20));
		tf_id.setBounds(146, 128, 214, 35);
		panel.add(tf_id);
		tf_id.setColumns(10);

		passwordField = new JPasswordField();
		passwordField.setFont(new Font("맑은 고딕", Font.PLAIN, 20));
		passwordField.setColumns(10);
		passwordField.setBounds(146, 183, 214, 35);
		panel.add(passwordField);

		tf_name = new JTextField();
		tf_name.setFont(new Font("맑은 고딕", Font.PLAIN, 20));
		tf_name.setColumns(10);
		tf_name.setBounds(146, 240, 214, 35);
		panel.add(tf_name);

		tf_tel = new JTextField();
		tf_tel.setFont(new Font("맑은 고딕", Font.PLAIN, 20));
		tf_tel.setColumns(10);
		tf_tel.setBounds(146, 296, 214, 35);
		panel.add(tf_tel);

		btn_join = new JButton("회원가입 완료");
		btn_join.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				join();
			}
		});
		btn_join.setForeground(new Color(255, 255, 255));
		btn_join.setBackground(new Color(0, 128, 255));
		btn_join.setFont(new Font("맑은 고딕", Font.PLAIN, 20));
		btn_join.setBounds(30, 355, 330, 45);
		panel.add(btn_join);

		btn_delete = new JButton("회원 탈퇴");
		btn_delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				delete();
			}
		});
		btn_delete.setForeground(new Color(255, 255, 255));
		btn_delete.setBackground(new Color(255, 0, 0));
		btn_delete.setFont(new Font("맑은 고딕", Font.PLAIN, 20));
		btn_delete.setBounds(30, 418, 330, 45);
		panel.add(btn_delete);

		btn_list = new JButton("모든 회원 목록");
		btn_list.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				select();
			}
		});
		btn_list.setForeground(new Color(255, 255, 255));
		btn_list.setBackground(new Color(0, 128, 255));
		btn_list.setFont(new Font("맑은 고딕", Font.PLAIN, 20));
		btn_list.setBounds(30, 479, 330, 45);
		panel.add(btn_list);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	// JDBC 드라이버 연결
	public void JDBCConnection() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			System.out.println("드라이버 로딩 성공");

			String url = "jdbc:oracle:thin:@localhost:1521:XE";
			String user = "scott", pwd = "tiger";

			con = DriverManager.getConnection(url, user, pwd);
			System.out.println("DB연결 성공");

		} catch (ClassNotFoundException ce) {
			System.out.println("드라이버 로딩 실패");
		} catch (SQLException se) {
			System.out.println("DB연결 실패");
		}
	}

	// 연결 닫기
	public void close(Connection con, Statement stmt, ResultSet rs) {
		try {
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
			if (con != null)
				con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 메시지박스
	public void showMsg(JPanel p, String msg) {
		JOptionPane.showMessageDialog(p, msg);
	}

	// 회원가입
	public void join() {

		String id = tf_id.getText();
		char[] ch = passwordField.getPassword();
		String pw = new String(ch);
		String name = tf_name.getText();
		String tel = tf_tel.getText();

		try {
			if (id == null || id.equals("")) {
				showMsg(panel, "아이디를 입력해야 합니다.");
				tf_id.requestFocus();
				return;
			} else if (ch == null || pw.equals("")) {
				showMsg(panel, "비밀번호를 입력해야 합니다.");
				passwordField.requestFocus();
				return;
			} else if (name == null || name.equals("")) {
				showMsg(panel, "이름을 입력해야 합니다.");
				tf_name.requestFocus();
				return;
			} else {
				// 1. 회원가입처리
				String insert = "INSERT INTO java_member(id, pw, name, tel, indate) ";
				insert += " VALUES ('" + id + "','" + pw + "','" + name + "','" + tel + "',sysdate)";
				System.out.println(insert);

				stmt = con.createStatement();
				int insertcnt = stmt.executeUpdate(insert);
				System.out.println(insertcnt + "개의 레코드를 삽입했습니다.");

				showMsg(panel, "회원가입이 완료 되었습니다.");
				tf_id.setText("");
				passwordField.setText("");
				tf_name.setText("");
				tf_tel.setText("");
			}
		} catch (SQLException e) {
			System.out.println("insert시 예외 발생 : " + e.getMessage());
		} finally {
			close(con, stmt, rs);
		}

	}

	// 회원탈퇴
	public void delete() {
		String id = tf_id.getText();

		if (id == null || id.equals("")) {
			showMsg(panel, "아이디를 입력하셔야 합니다.");
			return;
		}

		try {
			// 2. 회원탈퇴처리
			String delete = "DELETE FROM java_member WHERE id = " + "'" + id + "'";
			System.out.println(delete);

			String sql = "SELECT id FROM java_member WHERE id = '" + id + "'";
			System.out.println(sql);

			stmt = con.createStatement();

			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				String deleteid = rs.getString("id");
//				boolean idCheck = (deleteid == null) ? true : false;
//
//				if (idCheck == true) {
//					System.out.println("탈퇴할 ID : " + deleteid);
//					showMsg(panel, "존재하지 않는 ID입니다.");
//
//				}
				if (id.equals(deleteid)) {
					System.out.println("탈퇴할 ID : " + deleteid);
					showMsg(panel, deleteid + "님, 정말로 탈퇴하시겠습니까?");

					int deletecnt = stmt.executeUpdate(delete);

					tf_id.setText("");
					showMsg(panel, "탈퇴처리 되었습니다.");
					System.out.println(deletecnt + "개의 레코드를 삭제하였습니다.");
				}
			}
			tf_id.requestFocus();

		} catch (SQLException e) {
			System.out.println("delete시 예외 발생 : " + e.getMessage());
		} catch (NullPointerException e) {
			System.out.println("탈퇴할 ID검색 시 예외 발생 : " + e.getMessage());
		} finally {
			close(con, stmt, rs);
		}

	}

	// 모든회원보기
	public void select() {

		String sql = "SELECT name, id, tel, indate FROM java_member";
		sql += " ORDER BY indate DESC";
		System.out.println(sql);

		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			MemberList memberList = new MemberList();

			str = "";
			while (rs.next()) {
				str = rs.getString("name") + "   ";
				str += rs.getString("id") + "   ";
				str += rs.getString("tel") + "   ";
				str += rs.getDate("indate") + "\n";
				System.out.println(str);
				memberList.textArea.append(str);
			}
			memberList.setVisible(true);
		} catch (SQLException e) {
			System.out.println("select시 예외 발생 : " + e.getMessage());
			e.printStackTrace();
		} finally {
			close(con, stmt, rs);
		}

	}

	public static void main(String[] args) {
		MemberJoin memberJoin = new MemberJoin();
		memberJoin.setSize(400, 600);
		memberJoin.setVisible(true);

	}

}
