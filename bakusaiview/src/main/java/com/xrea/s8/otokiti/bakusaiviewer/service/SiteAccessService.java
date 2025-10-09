package com.xrea.s8.otokiti.bakusaiviewer.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Nodes;

import com.xrea.s8.otokiti.bakusaiviewer.entity.AreaInfo;
import com.xrea.s8.otokiti.bakusaiviewer.entity.CountryInfo;
import com.xrea.s8.otokiti.bakusaiviewer.entity.HistoryInfo;
import com.xrea.s8.otokiti.bakusaiviewer.entity.MenuInfo;
import com.xrea.s8.otokiti.bakusaiviewer.entity.PageInfo;
import com.xrea.s8.otokiti.bakusaiviewer.entity.ResponseInfo;
import com.xrea.s8.otokiti.bakusaiviewer.entity.ThreadInfo;

public class SiteAccessService {

	// ウェブサービス
	private WebService webService;

	/**
	 * 遅延インスタンス生成.
	 */
	private static class InstanceHolder {
		private final static SiteAccessService INSTANCE = new SiteAccessService(WebService.getInstance());
	}

	/**
	 * サイトアクセスサービスの取得.
	 *
	 * @return サイトアクセスサービス
	 */
	public static SiteAccessService getInstance() {
		return InstanceHolder.INSTANCE;
	}

	/**
	 * コンストラクタ.
	 *
	 * @param appService 汎用サービス
	 * @param propertyService プロパティファイル入出力サービス
	 */
	public SiteAccessService(WebService webService) {
		this.webService = webService;
	}

	/**
	 * レスポンスの取得.
	 *
	 * @param address アドレス
	 * @return レスポンス
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	public String getHttpResponse(final String address) throws URISyntaxException, IOException {
		return this.webService.getResponse(address);
	}

	/**
	 * ドキュメントの取得.
	 *
	 * @param html HTML
	 * @param address アドレス
	 * @return ドキュメント
	 */
	private Document getDocument(final String html, final String address) {
		return Jsoup.parse(html, address);
	}

	/**
	 * 国一覧の取得.
	 *
	 * @param address アドレス
	 * @return 国一覧
	 */
	public List<CountryInfo> getCountryList(String address) {
		List<CountryInfo> list = new ArrayList<>();
		try {
			Document doc = this.getDocument(this.getHttpResponse(address), address);

			Nodes<Node> nodes = doc.selectNodes("#chooseCountry div a");
			for (Node node : nodes) {
				Element element = (Element) node;
				CountryInfo info = new CountryInfo();
				info.setName(element.getElementsByTag("span").first().text());
				info.setUrl(element.attr("abs:href"));
				list.add(info);
			}
		} catch (URISyntaxException | IOException e) {
			e.printStackTrace();
		}

		return list;
	}

	/**
	 * 地域一覧の取得.
	 *
	 * @param address アドレス
	 * @return 地域一覧
	 */
	public List<AreaInfo> getAreaList(String address) {
		List<AreaInfo> list = new ArrayList<>();
		try {
			Document doc = this.getDocument(this.getHttpResponse(address), address);
			Nodes<Node> nodes = doc.selectNodes("#areaMap #bakusaiMap_02 .index_area_wrapper a");
			for (Node node : nodes) {
				Element element = (Element) node;
				AreaInfo info = new AreaInfo();
				info.setName(element.getElementsByClass("area_name").first().text());
				info.setUrl(element.attr("abs:href"));
				list.add(info);
			}
		} catch (URISyntaxException | IOException e) {
			e.printStackTrace();
		}

		return list;
	}

	/**
	 * メニュー一覧の取得.
	 *
	 * @param address アドレス
	 * @return メニュー一覧
	 */
	public List<MenuInfo> getMenuList(String address) {
		List<MenuInfo> list = new ArrayList<>();
		try {
			Document doc = this.getDocument(this.getHttpResponse(address), address);
			Element el = doc.selectFirst("frameset");
			if (el != null) {
				// フレームあり
				String link = doc.selectFirst("frameset frameset frame").attr("abs:src");
				return this.getMainMenuList(link);
			}
			Nodes<Node> nodes = doc.selectNodes("#ctgmenu .border_new .ctg_menu_list_link");
			for (Node node : nodes) {
				Element element = (Element) node;
				MenuInfo info = new MenuInfo();
				info.setName(element.getElementsByClass("ctg_menu_list_title").first().text());
				info.setUrl(element.attr("abs:href"));
				list.add(info);
			}
		} catch (URISyntaxException | IOException e) {
			e.printStackTrace();
		}

		return list;
	}

	/**
	 * メインメニュー一覧の取得.
	 *
	 * @param address アドレス
	 * @return メインメニュー一覧
	 */
	public List<MenuInfo> getMainMenuList(String address) {
		List<MenuInfo> list = new ArrayList<>();
		try {
			Document doc = this.getDocument(this.getHttpResponse(address), address);
			Nodes<Node> nodes = doc.selectNodes("#ctgmenu .border_new .ctg_menu_list_link");
			for (Node node : nodes) {
				Element element = (Element) node;
				MenuInfo info = new MenuInfo();
				info.setName(element.attr("title"));
				info.setUrl(element.attr("abs:href"));
				list.add(info);
			}
		} catch (URISyntaxException | IOException e) {
			e.printStackTrace();
		}

		return list;
	}

	/**
	 * 掲示板一覧の取得.
	 *
	 * @param address アドレス
	 * @return 掲示板一覧
	 */
	public List<ThreadInfo> getThreadList(String address) {
		// ページ一覧の取得
		List<PageInfo> pageList = getThreadPageList(address);

		List<ThreadInfo> list = new ArrayList<>();
		for (PageInfo page : pageList) {
			Nodes<Node> nodes = page.getDoc().selectNodes("#inner_container #columnWrap #thrListWrap #thrListInner a");
			for (Node node : nodes) {
				Element element = (Element) node;
				ThreadInfo info = new ThreadInfo();
				info.setName(element.attr("title"));
				info.setUrl(element.attr("abs:href"));
				list.add(info);
			}
		}

		return list;
	}


	/**
	 * 掲示板ページ一覧の取得.
	 *
	 * @param address アドレス
	 * @return ページ一覧
	 */
	private List<PageInfo> getThreadPageList(String address) {
		List<PageInfo> list = new ArrayList<>();
		try {
			Document doc = this.getDocument(this.getHttpResponse(address), address);

			Set<String> set = new HashSet<>();

			// 現在開いているページを登録
			list.add(new PageInfo(address, doc));

			List<Nodes<Node>> nodesList = new ArrayList<>();
			// 前ページを取得
			Nodes<Node> nodes = doc.selectNodes("#inner_container #columnWrap .paging .paging_prevlink a");
			if (nodes != null && !nodes.isEmpty()) {
				nodesList.add(nodes);
			}
			// 番号指定のページを取得
			nodes = doc.selectNodes("#inner_container #columnWrap .paging .paging_numberlink a");
			if (nodes != null && !nodes.isEmpty()) {
				nodesList.add(nodes);
			}
			// 次ページを取得
			nodes = doc.selectNodes("#inner_container #columnWrap .paging .paging_nextlink a");
			if (nodes != null && !nodes.isEmpty()) {
				nodesList.add(nodes);
			}

			for (Nodes<Node> nodez : nodesList) {
				for (Node node : nodez) {
					Element e = node.parentElement().firstElementChild();
					if (e != null) {
						String link = e.attr("abs:href");
						if (!link.isEmpty()) {
							if (!set.contains(link)) {
								set.add(link);
								list.add(new PageInfo(link, getDocument(this.getHttpResponse(link), link)));
							}
						}
					}
				}
			}
		} catch (URISyntaxException | IOException e) {
			e.printStackTrace();
		}

		return list;
	}

	/**
	 * レス一覧の取得.
	 *
	 * @param address アドレス
	 * @return レス一覧
	 */
	public List<ResponseInfo> getResponseList(String address) {
		// ページ一覧の取得
		List<PageInfo> pageList = getResponsePageList(address);

		List<ResponseInfo> list = new ArrayList<>();
		for (PageInfo page : pageList) {
			Nodes<Node> nodes = page.getDoc().selectNodes("#inner_container #res_list .res_list_article");
			for (Node node : nodes) {
				ResponseInfo res = new ResponseInfo();
				Element element = (Element) node;
				// ID
				res.setResnumb(element.selectFirst(".resnumb a").text());
				// 投稿日時
				Element commentTime = element.selectFirst(".res_meta_wrap span[itemprop = commentTime]");
				if (commentTime != null) {
					res.setCommentTime(commentTime.text());
				} else {
					res.setCommentTime("");
				}
				// コメント
				Element comment = element.selectFirst(".body div[itemprop = commentText]");
				if (comment != null) {
					res.setCommentText(comment.wholeText().replaceAll("\r\n", ""));
				} else {
					res.setCommentText(element.selectFirst(".resbody").text());
				}
				// 投稿者
				Element commentAuthor = element.selectFirst(".name_goodbad_box .name-wapper .name");
				if (commentAuthor != null) {
					res.setName(commentAuthor.text());
				} else {
					res.setName("");
				}

				list.add(res);
			}
		}

		return list;
	}

	/**
	 * レスページ一覧の取得.
	 *
	 * @param address アドレス
	 * @return ページ一覧
	 */
	private List<PageInfo> getResponsePageList(String address) {
		List<PageInfo> list = new ArrayList<>();
		try {
			Document doc = this.getDocument(this.getHttpResponse(address), address);

			Set<String> set = new HashSet<>();

			// 現在開いているページを登録
			list.add(new PageInfo(address, doc));

			List<Nodes<Node>> nodesList = new ArrayList<>();
			// 前ページを取得
			Nodes<Node> nodes = doc.selectNodes("#inner_container .reslist_td .paging .paging_prevlink a");
			if (nodes != null && !nodes.isEmpty()) {
				nodesList.add(nodes);
			}
			// 番号指定のページを取得
			nodes = doc.selectNodes("#inner_container .reslist_td .paging .paging_numberlink a");
			if (nodes != null && !nodes.isEmpty()) {
				nodesList.add(nodes);
			}
			// 次ページを取得
			nodes = doc.selectNodes("#inner_container .reslist_td .paging .paging_nextlink a");
			if (nodes != null && !nodes.isEmpty()) {
				nodesList.add(nodes);
			}

			for (Nodes<Node> nodez : nodesList) {
				for (Node node : nodez) {
					Element e = node.parentElement().firstElementChild();
					if (e != null) {
						String link = e.attr("abs:href");
						if (!link.isEmpty()) {
							if (!set.contains(link)) {
								set.add(link);
								list.add(new PageInfo(link, getDocument(this.getHttpResponse(link), link)));
							}
						}
					}
				}
			}
		} catch (URISyntaxException | IOException e) {
			e.printStackTrace();
		}

		return list;
	}

	/**
	 * 履歴情報リストの取得.
	 *
	 * @return 履歴情報リスト
	 */
	public List<HistoryInfo> getHistory() {
		return this.webService.getHistory();
	}

	/**
	 * 履歴情報リストの保存.
	 *
	 * @param history 履歴情報リスト
	 */
	public void saveHistory(List<HistoryInfo> history) {
		this.webService.saveHistory(history);
	}
}
