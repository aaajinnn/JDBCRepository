package jdbc.member;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.SystemColor;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class MemberList extends JFrame {
	JTextArea textArea;

	static Connection con;
	static Statement stmt;
	static ResultSet rs;

	Container cp;

	public MemberList() {

		super(":::회원 목록:::");
		cp = this.getContentPane();

		setBounds(385, 0, 300, 250);
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 10, 260, 191);
		panel.add(scrollPane);

		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		textArea.setEditable(false);

	}
}
