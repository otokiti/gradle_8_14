package com.xrea.s8.otokiti.bakusaiviewer.entity;

import java.util.Objects;

/**
 * 女の子情報.
 *
 * @author otokiti
 */
public class GirlInfo implements Comparable<GirlInfo> {

	// 女の子
	private String girl;
	// 店
	private String shop;
	// ＵＲＬ
	private String url;
	// 件数
	private String count;

	/** getter/setter */
	/**
	 * 女の子の取得.
	 *
	 * @return 女の子
	 */
	public String getGirl() {
		return girl;
	}

	/**
	 * 女の子の設定.
	 *
	 * @param girl 女の子
	 */
	public void setGirl(String girl) {
		this.girl = girl;
	}

	/**
	 * 店の取得.
	 *
	 * @return 店
	 */
	public String getShop() {
		return shop;
	}

	/**
	 * 店の設定.
	 *
	 * @param shop 店
	 */
	public void setShop(String shop) {
		this.shop = shop;
	}

	/**
	 * ＵＲＬの取得.
	 *
	 * @return ＵＲＬ
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * ＵＲＬの設定.
	 *
	 * @param url ＵＲＬ
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * 件数の取得.
	 *
	 * @return 件数
	 */
	public String getCount() {
		return count;
	}

	/**
	 * 件数の設定.
	 *
	 * @param count 件数
	 */
	public void setCount(String count) {
		this.count = count;
	}

	/**
	 * 比較
	 */
	@Override
	public int compareTo(GirlInfo o) {
		if (this.count  == null || o == null) {
			return 0;
		}
		if (o.count == null) {
			return 0;
		}
		// 比較
		float v1 = Float.parseFloat(count);
		float v2 = Float.parseFloat(o.count);

		if (v1 > v2) {
			return 1;
		} else if (v1 == v2) {
			return 0;
		} else {
			return -1;
		}
	}
}
