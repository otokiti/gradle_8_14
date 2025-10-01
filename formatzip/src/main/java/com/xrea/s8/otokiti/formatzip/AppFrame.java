package com.xrea.s8.otokiti.formatzip;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class AppFrame extends JFrame {

	// UID
	private static final long serialVersionUID = -8867055848199659236L;

	// メッセージラベル
	private JLabel msgLbl;

	public AppFrame() {
		super(App.TITLE + " " + App.VERSION);
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// 初期画面設計
		this.initForm();

		// 初期処理
		this.initProc();
	}

	/**
	 * 初期画面設計
	 */
	private void initForm() {

		this.msgLbl = new JLabel("");

		JPanel mainPnl = new JPanel();
		mainPnl.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		mainPnl.setLayout(new BorderLayout());
		mainPnl.add(new JPanel(), BorderLayout.CENTER);
		mainPnl.add(this.msgLbl, BorderLayout.SOUTH);

		JLabel copyrightLbl = new JLabel(App.COPYRIGHT);
		copyrightLbl.setHorizontalAlignment(JLabel.RIGHT);
		copyrightLbl.setForeground(Color.GRAY);

		super.getContentPane().add(mainPnl, BorderLayout.CENTER);
		super.getContentPane().add(copyrightLbl, BorderLayout.SOUTH);
	}

	/**
	 * 初期処理.
	 */
	private void initProc() {
	}

	/**
	 * 表示処理.
	 */
	public void display() {
		super.pack();
		super.setSize(700, 500);
		super.setLocationRelativeTo(this);
		super.setVisible(true);
	}
}
