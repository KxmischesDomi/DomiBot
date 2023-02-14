package me.kxmischesdomi.web;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
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

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public record DiscordRequestHandler(RequestHandler requestHandler,
									Config config) {

	private static final String apiEndpoint = "https://discord.com/api";

	public JsonObject requestUserInfo(String token) {
		HttpGet httpGet = new HttpGet(apiEndpoint + "/oauth2/@me");
		httpGet.setHeader("Authorization", "Bearer " + token);
		return requestHandler.readResponseAsDocument(requestHandler.executeRequest(httpGet));
	}

	public JsonArray requestUserGuilds(String token) {
		HttpGet httpGet = new HttpGet(apiEndpoint + "/users/@me/guilds");
		httpGet.setHeader("Authorization", "Bearer " + token);
		return requestHandler.readResponseAsArray(requestHandler.executeRequest(httpGet));
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

		HttpResponse httpResponse = requestHandler.executeRequest(httpPost);

		return requestHandler.readResponseAsDocument(httpResponse);
	}

}
