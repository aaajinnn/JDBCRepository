package memberManagement;

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

		setBounds(385, 0, 400, 600);
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);

		textArea = new JTextArea();
		textArea.setBounds(12, 10, 360, 541);
		textArea.setEditable(false);
		panel.add(textArea);
	}

}
