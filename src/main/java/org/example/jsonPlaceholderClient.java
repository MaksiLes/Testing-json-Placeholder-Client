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

public class jsonPlaceholderClient {
    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";

    public static ArrayList<Post> getPosts() throws IOException, InterruptedException, URISyntaxException {
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(BASE_URL + "/posts"))
                .header("Content-Type", "application/json")
                .timeout(Duration.of(10, ChronoUnit.SECONDS))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        ArrayList<Post> listPosts = gson.fromJson(response.body(), new TypeToken<ArrayList<Post>>() {}.getType());
        return listPosts;
    }

    public static Post getPost(int id) throws IOException, InterruptedException, URISyntaxException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(BASE_URL + "/posts/" + id))
                .header("Content-Type", "application/json")
                .timeout(Duration.of(10, ChronoUnit.SECONDS))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());

        if(response.statusCode() == 404) {
            return null;
        }

        Post getPost = gson.fromJson(response.body(), Post.class);
        return getPost;
    }

    public static ArrayList<Comment> getCommentsForThePost(int postId) throws IOException, InterruptedException, URISyntaxException {
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(BASE_URL + "/posts/" +postId + "/comments"))
                .header("Content-Type", "application/json")
                .timeout(Duration.of(10, ChronoUnit.SECONDS))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        ArrayList<Comment> listCommentsForThePost = gson.fromJson(response.body(), new TypeToken<ArrayList<Comment>>() {}.getType());
        return listCommentsForThePost;
    }

    public static ArrayList<Comment> getCommentsForSpecificPostID(int postId) throws IOException, InterruptedException, URISyntaxException {
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(BASE_URL + "/comments?postId=" + postId))
                .header("Content-Type", "application/json")
                .timeout(Duration.of(10, ChronoUnit.SECONDS))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        ArrayList<Comment> listCommentsForTheSpecificPostID = gson.fromJson(response.body(), new TypeToken<ArrayList<Comment>>() {}.getType());
        return listCommentsForTheSpecificPostID;
    }

    public static Post createPost(int id, int userId, String title, String body) throws IOException, InterruptedException, URISyntaxException {
        Post post = new Post(id, userId, title, body);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(post);

        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(BASE_URL + "/posts"))
                .header("Content-Type", "application/json")
                .timeout(Duration.of(10, ChronoUnit.SECONDS))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());

        Post createdPost = gson.fromJson(response.body(), Post.class);
        return createdPost;

    }

    public static Post updatePost(int id, int userId, String title, String body) throws IOException, InterruptedException, URISyntaxException {
        Post post = new Post(id, userId, title, body);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(post);
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(BASE_URL + "/posts/" + id))
                .header("Content-Type", "application/json")
                .timeout(Duration.of(10, ChronoUnit.SECONDS))
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            System.out.println("Пост обновлен: " + response.body());
        } else {
            System.out.println("Ошибка обновления поста: " + response.statusCode() + " " + response.body());
        }

        Post updatedPost = gson.fromJson(response.body(), Post.class);
        return updatedPost;
    }

    public static Post partialUpdate(int id, int userId, String title, String body) throws IOException, InterruptedException, URISyntaxException {
        Post post = new Post(id, userId, title, body);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(post);

        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(BASE_URL + "/posts/" + id))
                .header("Content-Type", "application/json")
                .timeout(Duration.of(10, ChronoUnit.SECONDS))
                .method("PATCH", HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            System.out.println("Пост обновлен: " + response.body());
        } else {
            System.out.println("Ошибка обновления поста: " + response.statusCode() + " " + response.body());
        }

        Post partialUpdatePost = gson.fromJson(response.body(), Post.class);
        return partialUpdatePost;
    }

    public static boolean deletePost(int id) throws IOException, InterruptedException, URISyntaxException {
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(BASE_URL + "/posts/" + id))
                .header("Content-Type", "application/json")
                .timeout(Duration.of(10, ChronoUnit.SECONDS))
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            System.out.println("Пост c ID 1 успешно удалён.");
            return true;
        } else {
            System.out.println("Ошибка удаления поста: " + response.statusCode() + " " + response.body());
        }

        return false;
    }

    public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException {
        getCommentsForThePost(2);
    }

}
