package com.xrea.s8.otokiti.bakusaiviewer;

/**
 * サイト発生Exception.
 *
 * @author otokiti
 */
public class SiteException extends Exception {

	// UID
	private static final long serialVersionUID = 5971920277212458695L;
	// 種類
	private String type;
	// コード
	private String code;

	/**
	 * コンストラクタ.
	 **/
	public SiteException() {
		super();
		this.type = null;
		this.code = null;
	}

	/**
	 * コンストラクタ.
	 *
	 * @param type 種類
	 * @param code コード
	 */
	public SiteException(String type, String code) {
		super();
		this.type = type;
		this.code = code;
	}

	/** getter/setter */
	/**
	 * 種類の取得.
	 *
	 * @return 種類
	 */
	public String getType() {
		return type;
	}

	/**
	 * 種類の設定.
	 *
	 * @param type 種類
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * コードの取得.
	 *
	 * @return コード
	 */
	public String getCode() {
		return code;
	}

	/**
	 * コードの設定.
	 *
	 * @param code コード
	 */
	public void setCode(String code) {
		this.code = code;
	}
}
