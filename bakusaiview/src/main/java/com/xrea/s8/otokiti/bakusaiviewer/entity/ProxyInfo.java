package com.xrea.s8.otokiti.bakusaiviewer.entity;

/**
 * プロキシ情報.
 *
 * @author otokiti
 */
public class ProxyInfo {

	// ホスト
	private String host;
	// ポート
	private String port;
	// ユーザー
	private String user;
	// パスワード
	private String pass;

	/**
	 * ホストの取得.
	 *
	 * @return ホスト
	 */
	public String getHost() {
		return host;
	}

	/**
	 * パスワードの取得.
	 *
	 * @return パスワード
	 */
	public String getPass() {
		return pass;
	}

	/**
	 * ポートの取得.
	 *
	 * @return ポート
	 */
	public String getPort() {
		return port;
	}

	/**
	 * ユーザーの取得.
	 *
	 * @return ユーザー
	 */
	public String getUser() {
		return user;
	}

	/**
	 * ホストの設定.
	 *
	 * @param host ホスト
	 */
	public void setHost(final String host) {
		this.host = host;
	}

	/**
	 * パスワードの設定.
	 *
	 * @param pass パスワード
	 */
	public void setPass(final String pass) {
		this.pass = pass;
	}

	/**
	 * ポートの設定.
	 *
	 * @param port ポート
	 */
	public void setPort(final String port) {
		this.port = port;
	}

	/**
	 * ユーザーの設定.
	 *
	 * @param user ユーザー
	 */
	public void setUser(final String user) {
		this.user = user;
	}
}
