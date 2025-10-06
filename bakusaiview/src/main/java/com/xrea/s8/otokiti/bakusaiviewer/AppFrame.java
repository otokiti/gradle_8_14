package com.xrea.s8.otokiti.bakusaiviewer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;

import com.xrea.s8.otokiti.bakusaiviewer.entity.AreaInfo;
import com.xrea.s8.otokiti.bakusaiviewer.entity.CountryInfo;
import com.xrea.s8.otokiti.bakusaiviewer.entity.MenuInfo;
import com.xrea.s8.otokiti.bakusaiviewer.service.SiteAccessService;

public class AppFrame extends JFrame {

	// UID
	private static final long serialVersionUID = 1009887179671234287L;

	/** ページモード */
	public static enum PageMode {
		/* 国一覧 */
		COUNTRY
		/** 地域一覧 */
		, AREA
		/** メニュー一覧 */
		, MENU
		/** 掲示板一覧 */
		, THREAD
		/** レス一覧 */
		, RESPONSE
	}

	// ターゲットURL
	private String targetUrl;
	// URL履歴
	private Deque<String> urlHistory;
	// サービス
	private SiteAccessService service;
	// メインパネル
	private JPanel mainPnl;
	// メッセージラベル
	private JLabel msgLbl;
	// 戻るボタン
	private JButton backBtn;

	public AppFrame() {
		super(App.TITLE + " " + App.VERSION);
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// 初期画面設計
		this.initForm();

		// 初期処理
		this.initProc();

		// 初期画面表示
		this.reloadPage(PageMode.COUNTRY);
	}

	/**
	 * 初期画面設計
	 */
	private void initForm() {

		this.msgLbl = new JLabel("");
		this.mainPnl = new JPanel();
		this.backBtn = new JButton("戻る");
		this.backBtn.addActionListener(e -> {
			this.targetUrl = this.urlHistory.pop();
			if (this.targetUrl.equals(App.BASE_URL)) {
				this.reloadPage(PageMode.COUNTRY);
			} else if (this.targetUrl.indexOf("acode=") >= 0) {
				this.reloadPage(PageMode.MENU);
			} else if (this.targetUrl.indexOf("ctgid=") >= 0) {
				this.reloadPage(PageMode.AREA);
			} else {
				this.reloadPage(PageMode.AREA);
			}
		});

		JPanel basePnl = new JPanel();
		basePnl.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		basePnl.setLayout(new BorderLayout());
		basePnl.add(this.mainPnl, BorderLayout.CENTER);
		basePnl.add(this.backBtn, BorderLayout.EAST);
		basePnl.add(this.msgLbl, BorderLayout.SOUTH);

		JLabel copyrightLbl = new JLabel(App.COPYRIGHT);
		copyrightLbl.setHorizontalAlignment(JLabel.RIGHT);
		copyrightLbl.setForeground(Color.GRAY);

		super.getContentPane().add(basePnl, BorderLayout.CENTER);
		super.getContentPane().add(copyrightLbl, BorderLayout.SOUTH);
	}

	/**
	 * 初期処理.
	 */
	private void initProc() {
		// 初期URL設定
		this.targetUrl = App.BASE_URL;
		this.urlHistory = new ArrayDeque<>();
		// サイトアクセスサービスの取得
		this.service = SiteAccessService.getInstance();

//		try {
//			SiteAccessService service = SiteAccessService.getInstance();
//
//			ThreadInfo threadInfo = new ThreadInfo(null, "https://bakusai.com/thr_res/acode=5/ctgid=103/bid=340/tid=12691711/");
//
//			service.getResponseInfoList(threadInfo);
//
//		} catch (IOException | URISyntaxException e) {
//			// TODO 自動生成された catch ブロック
//			e.printStackTrace();
//		}
	}

	/**
	 * 表示処理.
	 */
	public void display() {
		super.pack();
		super.setSize(700, 500);
		super.setLocationRelativeTo(this);
		super.setVisible(true);
	}

	/**
	 * 画面再表示処理.
	 *
	 * @param pageMode ページモード
	 */
	private void reloadPage(PageMode pageMode) {
		this.mainPnl.removeAll();
		switch (pageMode) {
			case COUNTRY:
				// 国一覧画面再表示
				this.reloadCountryPage();
				break;
			case AREA:
				// 地域一覧画面再表示
				this.reloadAreaPage();
				break;
			case MENU:
				// メニュー一覧画面再表示
				this.reloadMenuPage();
				break;
			case THREAD:
				break;
			default:
				break;
		}
		this.display();
	}

	/**
	 * 国一覧画面再表示処理.
	 */
	private void reloadCountryPage() {
		this.backBtn.setEnabled(false);

		// 国一覧の取得
		List<CountryInfo> list = this.service.getCountryList(this.targetUrl);

		ListModel<CountryInfo> listModel = new DefaultListModel<>();
		for (CountryInfo info : list) {
			((DefaultListModel<CountryInfo>) listModel).addElement(info);
		}

		JList<CountryInfo> countryList = new JList<>(listModel);
		countryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		countryList.addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
				CountryInfo selected = countryList.getSelectedValue();
				if (selected != null) {
					this.urlHistory.push(this.targetUrl);
					this.targetUrl = selected.getUrl();
					this.backBtn.setEnabled(true);
					if (this.targetUrl.indexOf("acode=") >= 0) {
						this.reloadPage(PageMode.MENU);
					} else {
						this.reloadPage(PageMode.AREA);
					}
				}
			}
		});
		JScrollPane scrollPnl = new JScrollPane();
		scrollPnl.setViewportView(countryList);

		this.mainPnl.setLayout(new BorderLayout());
		this.mainPnl.add(scrollPnl, BorderLayout.CENTER);
	}

	/**
	 * 地域一覧画面再表示処理.
	 */
	private void reloadAreaPage() {
		List<AreaInfo> list = this.service.getAreaList(this.targetUrl);

		ListModel<AreaInfo> listModel = new DefaultListModel<>();
		for (AreaInfo info : list) {
			((DefaultListModel<AreaInfo>) listModel).addElement(info);
		}

		JList<AreaInfo> areaList = new JList<>(listModel);
		areaList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		areaList.addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
				AreaInfo selected = areaList.getSelectedValue();
				if (selected != null) {
					this.urlHistory.push(this.targetUrl);
					this.targetUrl = selected.getUrl();
					this.backBtn.setEnabled(true);
					if (this.targetUrl.indexOf("acode=") >= 0) {
						this.reloadPage(PageMode.MENU);
					} else {
						this.reloadPage(PageMode.COUNTRY);
					}
				}
			}
		});
		JScrollPane scrollPnl = new JScrollPane();
		scrollPnl.setViewportView(areaList);

		this.mainPnl.setLayout(new BorderLayout());
		this.mainPnl.add(scrollPnl, BorderLayout.CENTER);
	}

	/**
	 * メニュー一覧画面再表示処理.
	 */
	private void reloadMenuPage() {
		List<MenuInfo> list = this.service.getMenuList(this.targetUrl);

		ListModel<MenuInfo> listModel = new DefaultListModel<>();
		for (MenuInfo info : list) {
			((DefaultListModel<MenuInfo>) listModel).addElement(info);
		}

		JList<MenuInfo> areaList = new JList<>(listModel);
		areaList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		areaList.addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
				MenuInfo selected = areaList.getSelectedValue();
				if (selected != null) {
					this.urlHistory.push(this.targetUrl);
					this.targetUrl = selected.getUrl();
					this.backBtn.setEnabled(true);
					this.reloadPage(PageMode.THREAD);
				}
			}
		});
		JScrollPane scrollPnl = new JScrollPane();
		scrollPnl.setViewportView(areaList);

		this.mainPnl.setLayout(new BorderLayout());
		this.mainPnl.add(scrollPnl, BorderLayout.CENTER);
	}
}
