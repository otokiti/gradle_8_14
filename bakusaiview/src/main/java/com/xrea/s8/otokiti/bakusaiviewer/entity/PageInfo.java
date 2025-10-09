package com.xrea.s8.otokiti.bakusaiviewer.entity;

import org.jsoup.nodes.Document;

/**
 * ページ情報.
 *
 * @author otokiti
 */
public class PageInfo {

	// URL
	private String url;
	// ドキュメント
	private Document doc;

	/**
	 * コンストラクタ.
	 */
	public PageInfo() {
		this.url = null;
		this.doc = null;
	}

	/**
	 * コンストラクタ.
	 *
	 * @param url URL
	 * @param doc ドキュメント
	 */
	public PageInfo(String url, Document doc) {
		this.url = url;
		this.doc = doc;
	}

	/** getter/setter */
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
	 * ドキュメントの取得.
	 *
	 * @return ドキュメント
	 */
	public Document getDoc() {
		return doc;
	}

	/**
	 * ドキュメントの設定.
	 *
	 * @param doc ドキュメント
	 */
	public void setDoc(Document doc) {
		this.doc = doc;
	}
}
