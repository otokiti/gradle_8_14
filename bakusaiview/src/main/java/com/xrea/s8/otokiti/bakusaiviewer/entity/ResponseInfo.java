package com.xrea.s8.otokiti.bakusaiviewer.entity;

/**
 * レス情報.
 *
 * @author otokiti
 */
public class ResponseInfo implements Comparable<ResponseInfo> {
	// レス番号
	private String resnumb;
	// 投稿日時
	private String commentTime;
	// コメント
	private String commentText;
	// 投稿者
	private String name;

	/**
	 * コンストラクタ.
	 **/
	public ResponseInfo() {
		this.resnumb = null;
		this.commentTime = null;
		this.commentText = null;
		this.name = null;
	}

	/**
	 * コンストラクタ.
	 *
	 * @param resnumb レス番号
	 * @param commentTime 投稿日時
	 * @param commentText コメント
	 * @param name 投稿者
	 */
	public ResponseInfo(final String resnumb, final String commentTime, final String commentText, final String name) {
		this.resnumb = resnumb;
		this.commentTime = commentTime;
		this.commentText = commentText;
		this.name = name;
	}

	/** getter/setter */
	/**
	 * レス番号の取得.
	 *
	 * @return レス番号
	 */
	public String getResnumb() {
		return resnumb;
	}

	/**
	 * レス番号の設定.
	 *
	 * @param resnumb レス番号
	 */
	public void setResnumb(String resnumb) {
		this.resnumb = resnumb;
	}

	/**
	 * 投稿日時の取得.
	 *
	 * @return 投稿日時
	 */
	public String getCommentTime() {
		return commentTime;
	}

	/**
	 * 投稿日時の設定.
	 *
	 * @param commentTime 投稿日時
	 */
	public void setCommentTime(String commentTime) {
		this.commentTime = commentTime;
	}

	/**
	 * コメントの取得.
	 *
	 * @return コメント
	 */
	public String getCommentText() {
		return commentText;
	}

	/**
	 * コメントの設定.
	 *
	 * @param commentText コメント
	 */
	public void setCommentText(String commentText) {
		this.commentText = commentText;
	}

	/**
	 * 投稿者の取得.
	 *
	 * @return 投稿者
	 */
	public String getName() {
		return name;
	}

	/**
	 * 投稿者の設定.
	 *
	 * @param commentAuthor 投稿者
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 比較
	 */
	@Override
	public int compareTo(ResponseInfo o) {
		if (this.resnumb  == null || o == null) {
			return 0;
		}
		if (o.resnumb == null) {
			return 0;
		}
		// #を除去して比較
		String v1 = this.resnumb;
		String v2 = o.getResnumb();
		if (v1.indexOf('#') >= 0) {
			v1 = v1.substring(1, v1.length());
		}
		if (v2.indexOf('#') >= 0) {
			v2 = v2.substring(1, v2.length());
		}
		try {
			return Integer.parseInt(v1) - Integer.parseInt(v2);
		} catch (NumberFormatException e) {
			return v1.compareTo(v2);
		}
	}
}
