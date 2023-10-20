package manager;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private final String URL;
    private final String apiToken;
    private final HttpClient httpClient;

    public KVTaskClient(String URL) {
        this.URL = URL;
        httpClient = HttpClient.newHttpClient();
        apiToken = register();
    }

    public String load(String key) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL + "/load/" + key + "?API_TOKEN=" + apiToken))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200)
                throw new RuntimeException("Load error: " + response.statusCode());

            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Не получается получить данные с сервера");
        }
    }

    public void put(String key, String value) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL + "/save/" + key + "?API_TOKEN=" + apiToken))
                    .POST(HttpRequest.BodyPublishers.ofString(value))
                    .build();

            HttpResponse<Void> response = httpClient.send(request, HttpResponse.BodyHandlers.discarding());

            if (response.statusCode() != 200)
                throw new RuntimeException("Put error: " + response.statusCode());

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Не получается записать данные на сервер");
        }
    }

    private String register() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL + "/register"))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200)
                throw new RuntimeException("Register error: " + response.statusCode());

            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Не получается получить токен");
        }
    }
}
