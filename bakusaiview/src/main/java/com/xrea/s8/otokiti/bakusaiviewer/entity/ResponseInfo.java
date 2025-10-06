package com.xrea.s8.otokiti.bakusaiviewer.entity;

public class ResponseInfo implements Comparable<ResponseInfo> {

	private String id;
	private String commentTime;
	private String comment;
	private String commentAuthor;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCommentTime() {
		return commentTime;
	}
	public void setCommentTime(String commentTime) {
		this.commentTime = commentTime;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getCommentAuthor() {
		return commentAuthor;
	}
	public void setCommentAuthor(String commentAuthor) {
		this.commentAuthor = commentAuthor;
	}
	@Override
	public int compareTo(ResponseInfo o) {
		if (this.id  == null || o == null) {
			return 0;
		}
		if (o.id == null) {
			return 0;
		}
		// #を除去して比較
		String v1 = this.id;
		String v2 = o.getId();
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
