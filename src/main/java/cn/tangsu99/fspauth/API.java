package cn.tangsu99.fspauth;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.spongepowered.configurate.ConfigurationNode;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class API {
    public static String register(String name, String password, String uuid) {
        ConfigurationNode config = FSP_Auth.getInstance().getConfig();
        HttpClient client = HttpClient.newHttpClient();
        String jsonInputString = String.format("{\"username\": \"%s\", \"password\": \"%s\", \"uuid\": \"%s\"}", name, password, uuid);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(config.node("url").getString()))
                .header("Content-Type", "application/json; utf-8")
                .header("Accept", "application/json")
                .header("API-Token", config.node("api-Token").getString())
                .POST(HttpRequest.BodyPublishers.ofString(jsonInputString)) // 设置请求体
                .build();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (response.statusCode() == 200) {
            Gson gson = new GsonBuilder().create();
            JsonObject jsonObject = gson.fromJson(response.body(), JsonObject.class);
            return jsonObject.get("desc").getAsString();
        }
        return "0";
    }
}
