package me.kxmischesdomi.web;

import lombok.SneakyThrows;
import me.kxmischesdomi.config.Config;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.bson.BsonDocument;

import java.util.ArrayList;
import java.util.List;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class DiscordRequestHandler {

	private static final String tokenURL = "https://discord.com/api/oauth2/token";

	private final RequestHandler requestHandler;

	private final Config config;

	public DiscordRequestHandler(RequestHandler requestHandler, Config config) {
		this.requestHandler = requestHandler;
		this.config = config;
	}

	@SneakyThrows
	public BsonDocument requestAccessToken(String code) {
		String clientId = config.getApplicationId();
		String clientSecret = config.getApplicationSecret();

		List<NameValuePair> params = new ArrayList<>();
		params.add(new BasicNameValuePair("client_id", clientId));
		params.add(new BasicNameValuePair("client_secret", clientSecret));
		params.add(new BasicNameValuePair("grant_type", "authorization_code"));
		params.add(new BasicNameValuePair("code", code));
		params.add(new BasicNameValuePair("redirect_uri", config.getWebConfig().getAccessTokenRedirect()));
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");

		HttpPost httpPost = new HttpPost(tokenURL);
		httpPost.setEntity(entity);
		httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");

		HttpResponse httpResponse = requestHandler.executeRequest(httpPost);
		int statusCode = httpResponse.getStatusLine().getStatusCode();

		if (statusCode == 200) {
			// Authentication successfully
			return requestHandler.readResponse(httpResponse);
		} else {
			// Authentication not successfully
			return new BsonDocument();
		}


	}

}
