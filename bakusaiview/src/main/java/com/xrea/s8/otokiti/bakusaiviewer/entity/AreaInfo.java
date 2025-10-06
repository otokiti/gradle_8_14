package com.xrea.s8.otokiti.bakusaiviewer.entity;

/**
 * 地域情報.
 *
 * @author otokiti
 */
public class AreaInfo {

	/** 地域称 */
	private String name;
	/** URL */
	private String url;

	/**
	 * コンストラクタ.
	 **/
	public AreaInfo() {
		this.name = null;
		this.url = null;
	}

	/**
	 * コンストラクタ.
	 *
	 * @param name 地域名称
	 * @param url URL
	 */
	public AreaInfo(final String name, final String url) {
		this.name = name;
		this.url = url;
	}

	/** getter/setter */
	/**
	 * 地域名称の取得.
	 *
	 * @return name 地域名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 地域名称の設定.
	 *
	 * @param name 地域名称
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
