package com.xrea.s8.otokiti.bakusaiviewer.service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xrea.s8.otokiti.bakusaiviewer.App;
import com.xrea.s8.otokiti.bakusaiviewer.entity.HistoryInfo;
import com.xrea.s8.otokiti.bakusaiviewer.entity.ProxyInfo;

/**
 * プロパティファイル入出力サービス.
 *
 * @author otokiti
 */
public class PropertyService {

	/**
	 * 遅延インスタンス生成.
	 */
	private static class InstanceHolder {
		private final static PropertyService INSTANCE = new PropertyService();
	}

	/**
	 * プロパティファイル入出力サービスの取得.
	 *
	 * @return プロパティファイル入出力サービス
	 */
	public static PropertyService getInstance() {
		return InstanceHolder.INSTANCE;
	}

	/**
	 * コンストラクタ.
	 */
	private PropertyService() {
		// 処理なし
	}

	/**
	 * プロキシ情報の保存.
	 *
	 * @param proxyInfo プロキシ情報
	 */
	public ProxyInfo loadProxy() {
		try {
			File file = new File(System.getProperty("user.dir"), App.CONFIG_FILE);
			if (!file.exists()) {
				return null;
			}
			return new ObjectMapper().readerWithView(ProxyInfo.class).readValue(file, ProxyInfo.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 履歴情報の読み込み.
	 *
	 * @return 履歴情報リスト
	 */
	public Set<HistoryInfo> loadHistory() {
		try {
			File file = new File(System.getProperty("user.dir"), App.HISTORY_FILE);
			if (!file.exists()) {
				return null;
			}
			return new ObjectMapper().readValue(file, new TypeReference<Set<HistoryInfo>>() {});
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 履歴情報の読み込み.
	 *
	 * @return 履歴情報リスト
	 */
	public void saveHistory(Set<HistoryInfo> historyInfoList) {
		try {
			File file = new File(System.getProperty("user.dir"), App.HISTORY_FILE);
			new ObjectMapper().writerWithDefaultPrettyPrinter().writeValue(file, historyInfoList);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * プロキシ情報の読み込み.
	 *
	 * @return プロキシ情報
	 */
	public ProxyInfo loadConfig() {
		File propFile = new File(System.getProperty("user.dir"), App.CONFIG_FILE);
		if (!propFile.exists()) {
			return null;
		}
		Properties prop = new Properties();
		try {
			prop.load(new FileReader(propFile));

			ProxyInfo proxyInfo = new ProxyInfo();
			proxyInfo.setHost(prop.getProperty("host"));
			proxyInfo.setPort(prop.getProperty("port"));
			proxyInfo.setUser(prop.getProperty("user"));
			proxyInfo.setPass(prop.getProperty("pass"));

			if (proxyInfo.getHost() == null || proxyInfo.getHost().isEmpty()) {
				return null;
			}

			return proxyInfo;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
}
