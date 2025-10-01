package com.xrea.s8.otokiti.formatzip;

import java.awt.EventQueue;

public class App {

	public static final String TITLE = "Format Zip";
	public static final String VERSION = "1.0.0";
	public static final String COPYRIGHT = "Copyright (c) 2025 otokiti All rights reserved.";

	/**
	 * メイン処理.
	 *
	 * @param args コマンドライン引数
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new AppFrame()::display);
	}
}
