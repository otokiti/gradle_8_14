package com.xrea.s8.otokiti.bakusaiviewer.entity;

/**
 * 履歴情報.
 *
 * @author otokiti
 */
public class HistoryInfo {

	// 名称
	private String name;
	// URL
	private String url;

	/**
	 * コンストラクタ.
	 **/
	public HistoryInfo() {
		this.name = null;
		this.url = null;
	}

	/**
	 * コンストラクタ.
	 *
	 * @param name 名称
	 * @param url URL
	 */
	public HistoryInfo(final String name, final String url) {
		this.name = name;
		this.url = url;
	}

	/** getter/setter */
	/**
	 * 名称の取得.
	 *
	 * @return 名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 名称の設定.
	 *
	 * @param name 名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * URLの取得.
	 *
	 * @return URL
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * URLの設定.
	 *
	 * @param url URL
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * 文字列変換.
	 */
	public String toString() {
		return this.name;
	}
}
