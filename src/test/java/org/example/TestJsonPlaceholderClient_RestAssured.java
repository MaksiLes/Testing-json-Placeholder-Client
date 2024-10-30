package org.example;

import com.beust.ah.A;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import static io.restassured.RestAssured.given;
import static org.example.jsonPlaceholderClient.getPosts;

public class TestJsonPlaceholderClient_RestAssured {
    String BASE_URL = "https://jsonplaceholder.typicode.com";

    @BeforeTest
    public void setup() {
        RestAssured.baseURI = BASE_URL;
    }

    @Test
    public void gettingPosts_RestAssured() throws IOException, URISyntaxException, InterruptedException {
        Response response = RestAssured.given()
                .contentType("application/json")
                .when()
                .get("/posts")
                .then()
                .statusCode(200)
                .extract()
                .response();

        Assert.assertNotNull(response.body().asString(), "Response body should not be null");
        String stringResponse = response.asPrettyString();
        System.out.println(stringResponse);
        Assert.assertFalse(stringResponse.isEmpty());

        ArrayList<Post> posts = getPosts();
        Assert.assertTrue(posts.get(0).getTitle() != null && !posts.get(0).getTitle().isEmpty(), "First post should have a title");
    }

    @Test
    public void testForGettingPost_RestAssured() {
        int id = 3;
        Response response = RestAssured.given()
                .contentType("application/json")
                .when()
                .get("/posts/" + id)
                .then()
                .statusCode(200)
                .extract()
                .response();

        Assert.assertNotNull(response.body().asString(), "Response body should not be null");
        Assert.assertNotNull(response.jsonPath().getString("title"));
        Assert.assertEquals(response.jsonPath().getInt("id"), id, "Post ID should match");
    }

    @Test
    public void testForGettingPost_InvalidId_RestAssured() {
        int id = 4444;
        Response response = RestAssured.given()
                .contentType("application/json")
                .when()
                .get("/posts/" + id)
                .then()
                .statusCode(404)
                .extract()
                .response();

        Assert.assertNotNull(response.body().asString(), "Response body should not be null");
    }

    @Test
    public void testGetCommentsForExistingPost_RestAssured() {
        int postId = 3;
        Response response = RestAssured.given()
                .contentType("application/json")
                .when()
                .get("/posts/" + postId + "/comments")
                .then()
                .statusCode(200)
                .extract()
                .response();

        Assert.assertNotNull(response.body().asString(), "Response body should not be null");
        Assert.assertEquals(response.jsonPath().getInt("[0].postId"), postId, "postId should match for first comment");
        Assert.assertNotNull(response.jsonPath().getString("[0].name"), "Comment name should not be null");
        Assert.assertFalse(response.jsonPath().getString("[0].email").isEmpty());
    }

    @Test
    public void testForUpdatingPost_RestAssured() {
        int id = 3;
        int userId = 1;
        String title = "Новый title";
        String body = "Новый body";

        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(new Post(id,userId, title, body))
                .when()
                .put("/posts/" + id)
                .then()
                .statusCode(200)
                .extract()
                .response();

        Assert.assertNotNull(response.body().asString(), "Response body should not be null");
        Assert.assertEquals(response.jsonPath().getInt("id"), id, "Post ID should match");
        Assert.assertEquals(response.jsonPath().getString("title"), title, "Title should match");
        Assert.assertFalse(response.jsonPath().getString("body").isEmpty());
    }

    @Test
    public void testForCreatingPost_RestAssured() {
        int id = 101;
        int userId = 101;
        String title = "Старый title";
        String body = "Старый body";

        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(new Post(id,userId, title, body))
                .when()
                .post("/posts/")
                .then()
                .statusCode(201)
                .extract()
                .response();
        System.out.println(response.asPrettyString());
        Assert.assertNotNull(response.body().asString(), "Response body should not be null");
        Assert.assertFalse(response.body().asString().isEmpty(), "Response body should not be empty");
        Assert.assertEquals(response.jsonPath().getInt("id"), id, "Post ID should match");
        Assert.assertEquals(response.jsonPath().getString("title"), title, "Title should match");
        Assert.assertFalse(response.jsonPath().getString("body").isEmpty());
    }

    @Test
    public void testForDeletingPost_RestAssured() {
        int id = 3;
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .when()
                .delete("/posts/" + id)
                .then()
                .statusCode(200)
                .extract()
                .response();

        System.out.println(response.asPrettyString());
    }
}
