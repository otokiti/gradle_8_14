package com.xrea.s8.otokiti.bakusaiviewer.entity;

import org.jsoup.nodes.Document;

public class PageInfo {

	private String url;
	private Document doc;

	public PageInfo(String url, Document doc) {
		this.url = url;
		this.doc = doc;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Document getDoc() {
		return doc;
	}

	public void setDoc(Document doc) {
		this.doc = doc;
	}
}
