package me.kxmischesdomi.web;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.SneakyThrows;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public record RequestHandler(Gson gson) {

	public RequestHandler() {
		this(new Gson());
	}

	@SneakyThrows
	public HttpResponse executeRequest(HttpUriRequest request) {
		System.out.println("requesting... " + request);
		HttpResponse execute = HttpClientBuilder.create().build().execute(request);
		System.out.println("got response.");
		return execute;
	}

	public JsonObject readResponseAsDocument(HttpResponse response) {
		return gson.fromJson(readResponse(response), JsonObject.class);
	}

	public JsonArray readResponseAsArray(HttpResponse response) {
		return gson.fromJson(readResponse(response), JsonArray.class);
	}

	@SneakyThrows
	public String readResponse(HttpResponse response) {
		InputStream inputStream = response.getEntity().getContent();
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		StringBuilder stringBuilder = new StringBuilder();
		String bufferedStrChunk = null;
		while((bufferedStrChunk = bufferedReader.readLine()) != null){
			stringBuilder.append(bufferedStrChunk);
		}

		return stringBuilder.toString();
	}

}
