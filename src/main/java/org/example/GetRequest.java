package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class GetRequest {
    public static ArrayList<Comment> getRequest() throws URISyntaxException, IOException, InterruptedException {

        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://jsonplaceholder.typicode.com/posts/1/comments"))
                .header("Content-Type", "application/json")
                .timeout(Duration.of(10, ChronoUnit.SECONDS))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        ArrayList<Comment> list = gson.fromJson(response.body(), new TypeToken<ArrayList<Comment>>() {
        }.getType());
        return list;
    }

    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
        ArrayList<Comment> list = getRequest();

        if (list == null) {
            System.out.println("Ошибка: список комментариев равен null.");
            return;
        }

        if (list.isEmpty()) {
            System.out.println("Ошибка: список комментариев пуст.");
            return;
        }

        System.out.println("В нашем списке " + list.size() + " комментариев");

        for (Comment comment : list) {
            System.out.println("ID: " + comment.getId() + "; " + " Name: " + comment.getName() + "; " + " Email: " + comment.getEmail());
        }

        boolean commentFound = false;
        for (Comment comment : list) {
            if (comment.getName().equals("alias odio sit")) {
                System.out.println("Значение " + " '" + comment.getName() + "' " + "есть в списке.");
                commentFound = true;
                break;
            }
        }

        if (!commentFound) {
            System.out.println("Ожидаемый комментарий 'alias odio sit' не найден.");
        }
    }
}



