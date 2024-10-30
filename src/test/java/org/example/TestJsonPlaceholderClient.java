package org.example;

import jdk.jfr.TransitionTo;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import static org.example.jsonPlaceholderClient.getPosts;

public class TestJsonPlaceholderClient {
    @Test
    public void gettingPosts() throws IOException, URISyntaxException, InterruptedException {
        ArrayList<Post> posts = getPosts();

        Assert.assertNotNull(posts);
        Assert.assertFalse(posts.isEmpty());

        Post firstPost = posts.getFirst();
        Assert.assertNotNull(firstPost);
        Assert.assertNotNull(firstPost.getTitle());
    }

    @Test
    public void testForGettingPost() throws IOException, URISyntaxException, InterruptedException {
        int id = 3;
        Post post = jsonPlaceholderClient.getPost(id);

        Assert.assertNotNull(post);
        Assert.assertNotNull(post.getTitle());
        Assert.assertNotNull(post.getBody());
    }

    @Test
    public void testForGettingPost_InvalidId() throws IOException, URISyntaxException, InterruptedException {
        int invalidPostId = 4567;
        Post post = jsonPlaceholderClient.getPost(invalidPostId);

        Assert.assertNull(post, "Ожидалось, что метод вернет null");
    }

    @Test
    public void testGetCommentsForExistingPost() throws IOException, URISyntaxException, InterruptedException {
        int postId = 3;
        ArrayList<Comment> comments = jsonPlaceholderClient.getCommentsForThePost(postId);

        Assert.assertNotNull(comments, "Ожидалось, что метод вернет не null");
        Assert.assertFalse(comments.isEmpty(), "Ожидалось, что список комментариев не будет пустым");
        Assert.assertFalse(comments.getFirst().getBody().isEmpty());
    }

    @Test
    public void testForUpdatingPost() throws IOException, URISyntaxException, InterruptedException {
        Post updatePost = jsonPlaceholderClient.updatePost(1,1,"Привет", "Обновлен");
        Assert.assertNotNull(updatePost);
        Assert.assertEquals(updatePost.getTitle(), "Привет");
    }

    @Test
    public void testForCreatingPost() throws IOException, URISyntaxException, InterruptedException {
        int id = 2;
        int userId = 2;
        String title = "Новый пост";
        String body = "Создала новый пост и добавила в него body";

        Post creatPost = jsonPlaceholderClient.createPost(id, userId, title, body);

        Assert.assertNotNull(creatPost);
        Assert.assertEquals(creatPost.getUserId(), 2);
        Assert.assertFalse(creatPost.getBody().isEmpty());
    }

    @Test
    public void testForDeletingPost() throws IOException, URISyntaxException, InterruptedException {
        int id = 2;
        boolean isDeleted = jsonPlaceholderClient.deletePost(id);

        Assert.assertTrue(isDeleted, "Ожидалось, что пост будет удален");
    }

}
