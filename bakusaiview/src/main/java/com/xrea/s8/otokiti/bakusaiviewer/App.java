package com.xrea.s8.otokiti.bakusaiviewer;

import java.awt.EventQueue;

public class App {

	public static final String TITLE = "Bakusai Viewer";
	public static final String VERSION = "1.0.0";
	public static final String COPYRIGHT = "Copyright (c) 2025 otokiti All rights reserved.";

	/** 設定ファイル */
	public static final String CONFIG_FILE = "/config.json";
	/** 履歴ファイル */
	public static final String HISTORY_FILE = "/history.json";
	/** ログファイル */
	public static final String LOG_PATH = "log";
	/** キャッシュフォルダ */
	public static final String CACHE_PATH = "cache";

	/** ベースURL */
	public static final String BASE_URL = "https://bakusai.com/overarea/";

	/** タイムアウト(秒) */
	public static final int TIMEOUT = 10;

	/**
	 * メイン処理.
	 *
	 * @param args コマンドライン引数
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new AppFrame()::display);
	}
}
