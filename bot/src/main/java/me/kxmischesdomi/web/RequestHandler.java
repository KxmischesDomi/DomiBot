package me.kxmischesdomi.web;

import lombok.SneakyThrows;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.bson.BsonDocument;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class RequestHandler {

	private final HttpClient httpClient;

	public RequestHandler() {
		httpClient = HttpClientBuilder.create().build();
	}

	@SneakyThrows
	public HttpResponse executeRequest(HttpUriRequest request) {
		return httpClient.execute(request);
	}

	public BsonDocument readResponse(HttpResponse response) {
		try {
			InputStream inputStream = response.getEntity().getContent();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			StringBuilder stringBuilder = new StringBuilder();
			String bufferedStrChunk = null;
			while((bufferedStrChunk = bufferedReader.readLine()) != null){
				stringBuilder.append(bufferedStrChunk);
			}

			return BsonDocument.parse(stringBuilder.toString());
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return new BsonDocument();

	}

}
