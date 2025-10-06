package com.xrea.s8.otokiti.bakusaiviewer.service;

import javax.swing.SwingWorker;
import com.xrea.s8.otokiti.bakusaiviewer.service.DownloadTaskWorker.EndType;


public class DownloadTaskWorker extends SwingWorker<EndType, EndType> {

	/** 終了判定 */
	public enum EndType {
		Successful("ダウンロード完了"),
		FailureService("ダウンロードサービスが起動できませんでした。"),
		FailureError("ダウンロードが失敗しました。");

		// メッセージ
		private String msg;

		/**
		 * コンストラクタ.
		 *
		 * @param msg メッセージ
		 */
		private EndType(String msg) {
			this.msg = msg;
		}

		/**
		 * メッセージの取得.
		 *
		 * @return メッセージ
		 */
		public String getValue() {
			return this.msg;
		}

		/**
		 * @inheritDoc
		 */
		@Override
		public String toString() {
			return this.getValue();
		}
	}

	public DownloadTaskWorker() {
		super();
	}

	@Override
	protected EndType doInBackground() throws Exception {
		return EndType.Successful;

	}

	@Override
	protected void done() {

	}
}
