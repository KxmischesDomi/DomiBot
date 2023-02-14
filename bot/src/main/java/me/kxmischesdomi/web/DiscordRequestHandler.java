package me.kxmischesdomi.web;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.SneakyThrows;
import me.kxmischesdomi.config.Config;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public record DiscordRequestHandler(RequestHandler requestHandler, Config config) {

	private static final String apiEndpoint = "https://discord.com/api/v10";

	@Getter
	private static final Cache<String, JsonArray> userGuildsCache = CacheBuilder.newBuilder()
			.expireAfterWrite(20, TimeUnit.SECONDS)
			.build();

	public JsonObject requestUserInfo(String token) {
		HttpGet httpGet = new HttpGet(apiEndpoint + "/oauth2/@me");
		httpGet.setHeader("Authorization", "Bearer " + token);
		return requestHandler.readResponseAsDocument(requestHandler.executeRequest(httpGet));
	}

	public JsonArray requestUserGuilds(String token) {
		JsonArray cache = userGuildsCache.getIfPresent(token);
		if (cache != null) return cache;
		HttpGet httpGet = new HttpGet(apiEndpoint + "/users/@me/guilds");
		httpGet.setHeader("Authorization", "Bearer " + token);
		JsonArray guilds = requestHandler.readResponseAsArray(requestHandler.executeRequest(httpGet));
		userGuildsCache.put(token, guilds);
		return guilds;
	}

	@SneakyThrows
	public JsonObject requestAccessToken(String code) {
		String clientId = config.getApplicationId();
		String clientSecret = config.getApplicationSecret();

		List<NameValuePair> params = new ArrayList<>();
		params.add(new BasicNameValuePair("client_id", clientId));
		params.add(new BasicNameValuePair("client_secret", clientSecret));
		params.add(new BasicNameValuePair("grant_type", "authorization_code"));
		params.add(new BasicNameValuePair("code", code));
		params.add(new BasicNameValuePair("redirect_uri", config.getWebConfig().getAccessTokenRedirect()));
		return createTokenRequest(params);
	}

	@SneakyThrows
	public JsonObject requestTokenRefresh(String refreshToken) {
		String clientId = config.getApplicationId();
		String clientSecret = config.getApplicationSecret();

		List<NameValuePair> params = new ArrayList<>();
		params.add(new BasicNameValuePair("client_id", clientId));
		params.add(new BasicNameValuePair("client_secret", clientSecret));
		params.add(new BasicNameValuePair("grant_type", "refresh_token"));
		params.add(new BasicNameValuePair("refresh_token", refreshToken));
		return createTokenRequest(params);

	}

	private JsonObject createTokenRequest(List<NameValuePair> params) throws UnsupportedEncodingException {
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");

		HttpPost httpPost = new HttpPost(apiEndpoint + "/oauth2/token");
		httpPost.setEntity(entity);
		httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");

		System.out.println(httpPost);
		HttpResponse httpResponse = requestHandler.executeRequest(httpPost);
		System.out.println(httpResponse);
		return requestHandler.readResponseAsDocument(httpResponse);
	}

}
