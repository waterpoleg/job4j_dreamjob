package ru.job4j.dreamjob.controller;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.Model;
import ru.job4j.dreamjob.model.City;
import ru.job4j.dreamjob.model.Post;
import ru.job4j.dreamjob.model.User;
import ru.job4j.dreamjob.services.CityService;
import ru.job4j.dreamjob.services.PostService;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PostControllerTest {

    @Test
    void whenAllPosts() {
        List<Post> posts = Arrays.asList(
                new Post(1, "New post1", "desc1", "04042022", true,
                        new City(1, "city1")
                ),
                new Post(2, "New post2", "desc2", "04042022", false,
                        new City(2, "city2")
                )
        );
        Model model = mock(Model.class);
        PostService postService = mock(PostService.class);
        when(postService.findAll()).thenReturn(posts);
        CityService cityService = mock(CityService.class);
        PostController postController = new PostController(
                postService,
                cityService
        );
        HttpSession session = new MockHttpSession();
        String page = postController.posts(model, session);
        verify(model).addAttribute("posts", posts);
        assertThat(page, is("posts"));
    }

    @Test
    void whenAddPost() {
        List<City> cities = Arrays.asList(
                new City(1, "city1"),
                new City(2, "city2")
        );
        User user = new User(1, "User1", "email1", "pwd1");
        Model model = mock(Model.class);
        PostService postService = mock(PostService.class);
        CityService cityService = mock(CityService.class);
        when(cityService.getAllCities()).thenReturn(cities);
        PostController postController = new PostController(
                postService,
                cityService
        );
        HttpSession session = new MockHttpSession();
        session.setAttribute("user", user);
        String page = postController.addPost(model, session);
        verify(model).addAttribute("cities", cities);
        verify(model).addAttribute("user", user);
        assertEquals("addPost", page);
    }

    @Test
    void whenUpdatePost() {
        Post post = new Post(1, "New post1", "desc1", "04042022", true,
                new City(1, "city1")
        );
        List<City> cities = Arrays.asList(
                new City(1, "city1"),
                new City(2, "city2")
        );
        User user = new User(1, "User1", "email1", "pwd1");
        Model model = mock(Model.class);
        PostService postService = mock(PostService.class);
        CityService cityService = mock(CityService.class);
        when(postService.findById(1)).thenReturn(post);
        when(cityService.getAllCities()).thenReturn(cities);
        PostController postController = new PostController(
                postService,
                cityService
        );
        HttpSession session = new MockHttpSession();
        session.setAttribute("user", user);
        String page = postController.formUpdatePost(model, 1, session);
        verify(model).addAttribute("post", post);
        verify(model).addAttribute("cities", cities);
        verify(model).addAttribute("user", user);
        assertEquals("updatePost", page);
    }
}
