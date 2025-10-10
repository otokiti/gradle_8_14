package com.xrea.s8.otokiti.bakusaiviewer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;

import com.xrea.s8.otokiti.bakusaiviewer.entity.AreaInfo;
import com.xrea.s8.otokiti.bakusaiviewer.entity.CountryInfo;
import com.xrea.s8.otokiti.bakusaiviewer.entity.HistoryInfo;
import com.xrea.s8.otokiti.bakusaiviewer.entity.MenuInfo;
import com.xrea.s8.otokiti.bakusaiviewer.entity.ResponseInfo;
import com.xrea.s8.otokiti.bakusaiviewer.entity.ThreadInfo;
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
	// 履歴パネル
	private JPanel historyPnl;
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
		this.historyPnl = new JPanel();
		this.backBtn = new JButton("戻る");
		this.backBtn.addActionListener(e -> {
			this.targetUrl = this.urlHistory.pop();
			if (this.urlHistory.isEmpty()) {
				this.backBtn.setEnabled(false);
			}

			if (this.targetUrl.indexOf("tid=") >= 0) {
				this.reloadPage(PageMode.RESPONSE);
			} else if (this.targetUrl.indexOf("bid=") >= 0) {
				this.reloadPage(PageMode.THREAD);
			} else if (this.targetUrl.indexOf("ctgid=") >= 0) {
				this.reloadPage(PageMode.AREA);
			} else if (this.targetUrl.indexOf("acode=") >= 0) {
				this.reloadPage(PageMode.MENU);
			} else if (this.targetUrl.equals(App.BASE_URL)) {
				this.reloadPage(PageMode.COUNTRY);
			} else {
				this.reloadPage(PageMode.AREA);
			}
		});

		JSplitPane splitPnl = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPnl.setDividerSize(5);
		splitPnl.setDividerLocation(500);
		splitPnl.setLeftComponent(this.mainPnl);
		splitPnl.setRightComponent(this.historyPnl);

		JPanel basePnl = new JPanel();
		basePnl.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		basePnl.setLayout(new BorderLayout());
		basePnl.add(splitPnl, BorderLayout.CENTER);
		basePnl.add(this.backBtn, BorderLayout.WEST);
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
		this.backBtn.setEnabled(false);
		// サイトアクセスサービスの取得
		this.service = SiteAccessService.getInstance();
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
		switch (pageMode) {
			case COUNTRY:
				// 履歴一覧画面再表示
				this.reloadHistoryPage();
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
				// 掲示板一覧画面再表示
				this.reloadThreadPage();
				break;
			case RESPONSE:
				// 履歴一覧画面再表示
				this.reloadHistoryPage();
				// レス一覧画面再表示
				this.reloadResponsePage();
				break;
			default:
				break;
		}

		this.display();
	}

	/**
	 * 履歴画面再表示処理.
	 */
	private void reloadHistoryPage() {
		DefaultListModel<HistoryInfo> listModel = new DefaultListModel<>();
		JList<HistoryInfo> historyList = new JList<>(listModel);
		historyList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JScrollPane scrollPnl = new JScrollPane();
		scrollPnl.setViewportView(historyList);

		this.historyPnl.removeAll();
		this.historyPnl.setLayout(new BorderLayout());
		this.historyPnl.add(scrollPnl, BorderLayout.CENTER);

		// イベントディスパッチスレッドの作成
		SwingWorker<List<HistoryInfo>, String> worker = new SwingWorker<>() {
			@Override
			protected List<HistoryInfo> doInBackground() throws Exception {
				// 履歴一覧の取得
				return service.getHistory();
			}

			protected void process(List<String> chunks) {
			}

			@Override
			protected void done() {
				try {
					for (HistoryInfo info : get()) {
						listModel.addElement(info);
					}
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
				publish("");
			}
		};
		worker.execute();
	}

	/**
	 * 国一覧画面再表示処理.
	 */
	private void reloadCountryPage() {
		DefaultListModel<CountryInfo> listModel = new DefaultListModel<>();
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

		this.mainPnl.removeAll();
		this.mainPnl.setLayout(new BorderLayout());
		this.mainPnl.add(scrollPnl, BorderLayout.CENTER);

		// イベントディスパッチスレッドの作成
		SwingWorker<List<CountryInfo>, String> worker = new SwingWorker<>() {
			@Override
			protected List<CountryInfo> doInBackground() throws Exception {
				publish("国一覧を取得中...");
				// 国一覧の取得
				return service.getCountryList(targetUrl);
			}

			protected void process(List<String> chunks) {
				for (String msg : chunks) {
					msgLbl.setText(msg);
				}
			}

			@Override
			protected void done() {
				try {
					for (CountryInfo info : get()) {
						listModel.addElement(info);
					}
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
				publish("");
			}
		};
		worker.execute();
	}

	/**
	 * 地域一覧画面再表示処理.
	 */
	private void reloadAreaPage() {
		DefaultListModel<AreaInfo> listModel = new DefaultListModel<>();
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

		this.mainPnl.removeAll();
		this.mainPnl.setLayout(new BorderLayout());
		this.mainPnl.add(scrollPnl, BorderLayout.CENTER);

		// イベントディスパッチスレッドの作成
		SwingWorker<List<AreaInfo>, String> worker = new SwingWorker<>() {
			@Override
			protected List<AreaInfo> doInBackground() throws Exception {
				publish("地域一覧を取得中...");
				// 地域一覧の取得
				return service.getAreaList(targetUrl);
			}

			protected void process(List<String> chunks) {
				for (String msg : chunks) {
					msgLbl.setText(msg);
				}
			}

			@Override
			protected void done() {
				try {
					for (AreaInfo info : get()) {
						listModel.addElement(info);
					}
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
				publish("");
			}
		};
		worker.execute();
	}

	/**
	 * メニュー一覧画面再表示処理.
	 */
	private void reloadMenuPage() {
		DefaultListModel<MenuInfo> listModel = new DefaultListModel<>();
		JList<MenuInfo> menuList = new JList<>(listModel);
		menuList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		menuList.addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
				MenuInfo selected = menuList.getSelectedValue();
				if (selected != null) {
					this.urlHistory.push(this.targetUrl);
					this.targetUrl = selected.getUrl();
					this.backBtn.setEnabled(true);
					this.reloadPage(PageMode.THREAD);
				}
			}
		});
		JScrollPane scrollPnl = new JScrollPane();
		scrollPnl.setViewportView(menuList);

		this.mainPnl.removeAll();
		this.mainPnl.setLayout(new BorderLayout());
		this.mainPnl.add(scrollPnl, BorderLayout.CENTER);

		// イベントディスパッチスレッドの作成
		SwingWorker<List<MenuInfo>, String> worker = new SwingWorker<>() {
			@Override
			protected List<MenuInfo> doInBackground() throws Exception {
				publish("メニュー一覧を取得中...");
				// メニュー一覧の取得
				return service.getMenuList(targetUrl);
			}

			protected void process(List<String> chunks) {
				for (String msg : chunks) {
					msgLbl.setText(msg);
				}
			}

			@Override
			protected void done() {
				try {
					for (MenuInfo info : get()) {
						listModel.addElement(info);
					}
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
				publish("");
			}
		};
		worker.execute();
	}

	/**
	 * 掲示板一覧画面再表示処理.
	 */
	private void reloadThreadPage() {
		DefaultListModel<ThreadInfo> listModel = new DefaultListModel<>();
		JList<ThreadInfo> threadList = new JList<>(listModel);
		threadList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		threadList.addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
				ThreadInfo selected = threadList.getSelectedValue();
				if (selected != null) {
					HistoryInfo info = new HistoryInfo(selected.getName(), selected.getUrl());
					List<HistoryInfo> list = service.getHistory();
					list.add(info);
					service.saveHistory(list);

					this.targetUrl = selected.getUrl();
					this.backBtn.setEnabled(true);
					this.reloadPage(PageMode.RESPONSE);
				}
			}
		});
		JScrollPane scrollPnl = new JScrollPane();
		scrollPnl.setViewportView(threadList);

		this.mainPnl.removeAll();
		this.mainPnl.setLayout(new BorderLayout());
		this.mainPnl.add(scrollPnl, BorderLayout.CENTER);

		// イベントディスパッチスレッドの作成
		SwingWorker<List<ThreadInfo>, String> worker = new SwingWorker<>() {
			@Override
			protected List<ThreadInfo> doInBackground() throws Exception {
				publish("掲示板一覧を取得中...");
				// 掲示板一覧の取得
				return service.getThreadList(targetUrl);
			}

			protected void process(List<String> chunks) {
				for (String msg : chunks) {
					msgLbl.setText(msg);
				}
			}

			@Override
			protected void done() {
				try {
					for (ThreadInfo info : get()) {
						listModel.addElement(info);
					}
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
				publish("");
			}
		};
		worker.execute();
	}

	/**
	 * レス一覧画面再表示処理.
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	private void reloadResponsePage() {
		// イベントディスパッチスレッドの作成
		SwingWorker<List<ResponseInfo>, String> worker = new SwingWorker<>() {
			@Override
			protected List<ResponseInfo> doInBackground() throws Exception {
				publish("レス一覧を取得中...");
				// レス一覧の取得
				return service.getResponseList(targetUrl);
			}

			protected void process(List<String> chunks) {
				for (String msg : chunks) {
					msgLbl.setText(msg);
				}
			}

			@Override
			protected void done() {
				try {
					List<ResponseInfo> list = get();
					Collections.sort(list);
					for (ResponseInfo res : list) {
						System.out.println(res.getResnumb() + " " + res.getCommentTime());
						System.out.println(res.getCommentText());
						System.out.println(res.getName());
						System.out.println("----------------------------------------------------------");
					}
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
				publish("");
			}
		};
		worker.execute();
	}
}
