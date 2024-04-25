package app;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Map;
import java.util.Scanner;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;

import com.google.gson.Gson;

public class Main {

	public static URI getRelativePath(String path) {
		String url = "https://v6.exchangerate-api.com/v6/903b80dd483e81436df2b9c0/" + path;
		try {
			return new URI(url);
		} catch (URISyntaxException e) {
			System.out.println("Incorrect URI creation");
			return null;
		}
	}
	
	/* Para convertir el json a un map de java */
	public static Map<String,String> convertJson(String json) {
		Gson gson = new Gson();
		Type string = new TypeToken<Map <String,String>>(){}.getType();
		Map<String, String> map = gson.fromJson(json, string);
		return map;
	}
	
	public static String getConversionRates(String json) {
		int divices = json.indexOf("\"conversion_rates\":") + 19;
		int end = json.length() - 2;
		
		return json.substring(divices, end);
	}
	
	public static void printCurrencies(Map<String,String> currencies) {
		int counter = 0;
		for(String key : currencies.keySet()) {
			System.out.print(key + "\t");
			counter++;
			
			if(counter >= 15) {
				System.out.println("");
				counter=0;
			}
		}
		System.out.println("");
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
	
	@SuppressWarnings("resource")
	public static double askCurrencyToUser(Map<String, String> currencies) {
		Scanner scan = new Scanner(System.in);
		String userSelection;
		userSelection = scan.nextLine();
		String currency = currencies.get(userSelection.toUpperCase());
		while(currency == null) {
			System.out.println("That's not in the selection, select another one");
			userSelection = scan.nextLine();
			currency = currencies.get(userSelection.toUpperCase());
		}
		
		return Double.parseDouble(currency);
	}
	
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		String jsonResponse = "";
		
		URI url = getRelativePath("latest/USD");
		
		jsonResponse = FetchCurrenciesData.requestCurrencies(url);
		
		String conversions = getConversionRates(jsonResponse);
		Map<String,String> currencies = convertJson(conversions);
		
		printCurrencies(currencies);

		System.out.println("Select one of these currencies as a base currency");
		double base = askCurrencyToUser(currencies);
		
		System.out.println("How much money do you want to convert?");
		double money = scan.nextDouble();
		
		System.out.println("Select one of these currencies as a target currency");
		double target = askCurrencyToUser(currencies);
		
		double conversion = (money / base) * target;
		
		System.out.println(conversion);
		
	}

}