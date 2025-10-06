package com.xrea.s8.otokiti.bakusaiviewer.service;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import com.xrea.s8.otokiti.bakusaiviewer.App;
import com.xrea.s8.otokiti.bakusaiviewer.entity.ProxyInfo;

import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

/**
 * Webサービス.
 *
 * @author otokiti
 */
public class WebService {

	// キャッシュ
	private Map<String, String> cache;
	// クライアント
	private OkHttpClient client;

	/**
	 * 遅延インスタンス生成.
	 */
	private static class InstanceHolder {
		private final static WebService INSTANCE = new WebService(PropertyService.getInstance().loadConfig());
	}

	/**
	 * プロパティファイル入出力サービスの取得.
	 *
	 * @return プロパティファイル入出力サービス
	 */
	public static WebService getInstance() {
		return InstanceHolder.INSTANCE;
	}

	/**
	 * コンストラクタ.
	 *
	 * @param proxyInfo プロキシ情報
	 */
	public WebService(final ProxyInfo proxyInfo) {
		this.cache = new HashMap<>();
		this.client = this.createOkHttpClient(proxyInfo);
	}

	/**
	 * クライアント設定の取得.
	 *
	 * @param proxyInfo プロキシ情報
	 * @return クライアント設定
	 */
	private OkHttpClient createOkHttpClient(final ProxyInfo proxyInfo) {
		// Cookieの設定
		CookieManager cookieManager = new CookieManager();
		cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
		// OkHttpClientの作成
		Builder builder = new OkHttpClient.Builder().connectTimeout(App.TIMEOUT, TimeUnit.SECONDS).writeTimeout(App.TIMEOUT, TimeUnit.SECONDS).readTimeout(App.TIMEOUT, TimeUnit.SECONDS).cookieJar(new JavaNetCookieJar(cookieManager));
		// プロキシ設定によって判断
		if (proxyInfo == null) {
			// プロキシ設定なし
			this.client = builder.build();

		} else if (StringUtils.isEmpty(proxyInfo.getUser())) {
			// 認証なし
			this.client = builder.proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyInfo.getHost(), Integer.valueOf(proxyInfo.getPort())))).build();

		} else {
			// 認証あり
			Authenticator proxyAuthenticator = new Authenticator() {
				@Override
				public Request authenticate(Route route, Response response) throws IOException {
					String credential = Credentials.basic(proxyInfo.getUser(), proxyInfo.getPass());
					return response.request().newBuilder().header("Proxy-Authorization", credential).build();
				}
			};
			this.client = builder.proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyInfo.getHost(), Integer.valueOf(proxyInfo.getPort())))).proxyAuthenticator(proxyAuthenticator).build();
		}

		return client;
	}

	/**
	 * HTTPリクエストの結果取得.
	 *
	 * @param address アドレス
	 * @return リクエスト結果
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public String getResponse(final String address) throws IOException, URISyntaxException {
		if (this.cache.containsKey(address)) {
			return this.cache.get(address);
		}

		Request request = new Request.Builder().url(address).build();

		try (Response response = this.client.newCall(request).execute()) {
			if (response.isSuccessful() && response.body() != null) {
				return IOUtils.toString(response.body().byteStream(), StandardCharsets.UTF_8);
			}
		}
		return null;
	}

	/**
	 * キャッシュの設定.
	 *
	 * @param address アドレス
	 * @param response レスポンス
	 */
	public void setCache(final String address, final String response) {
		if (!this.cache.containsKey(address)) {
			this.cache.put(address, response);
		}
	}
}
