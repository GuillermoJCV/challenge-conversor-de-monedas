package app;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

public class FetchCurrenciesData {
	
	public FetchCurrenciesData() {
		
	}
	
	public static String requestCurrencies(URI path) {
		HttpClient client = HttpClient.newHttpClient();
		try {
			HttpRequest req = HttpRequest.newBuilder()
						.uri(path)
						.GET()
						.build();
			HttpResponse<String> res = client.send(req, BodyHandlers.ofString());
			return res.body();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			return null;
		}
	}
}
