package ru.job4j.dreamjob.persistence;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ru.job4j.dreamjob.Main;
import ru.job4j.dreamjob.model.City;
import ru.job4j.dreamjob.model.Post;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class PostDBStoreTest {

    private final static BasicDataSource pool = new Main().loadPool();
    private final static PostDBStore store = new PostDBStore(pool);
    private Post post = new Post(
            0,
            "Java Job",
            "description",
            "28/04/2022",
            true,
            new City(1, "City")
    );

    @Test
    public void whenCreatePost() {
        store.add(post);
        Post postInDb = store.findById(post.getId());
        assertThat(postInDb.getName(), is(post.getName()));
    }

    @Test
    public void whenUpdatePost() {
        store.add(post);
        post.setName("new position");
        store.update(post);
        Post postInDb = store.findById(post.getId());
        assertEquals("new position", postInDb.getName());
    }

    @Test
    public void whenFindByIdPost() {
        store.add(post);
        Post postInDb = store.findById(post.getId());
        assertEquals(postInDb.getId(), post.getId());
    }

    @Test
    public void whenNotFindById() {
        assertNull(store.findById(-1));
    }

    @Test
    public void whenFindAllPosts() {
        Post post1 = new Post(
                0, "pos0", "descr0", "08.01.2022", true, new City(1, "city1")
        );
        Post post2 = new Post(
                1, "pos1", "descr1", "25.12.2021", true, new City(2, "city2")
        );
        Post post3 = new Post(
                2, "pos2", "descr2", "15.11.2021", true, new City(3, "city3")
        );
        store.add(post1);
        store.add(post2);
        store.add(post3);
        List<Post> posts = List.of(post1, post2, post3);
        List<Post> postsInDb = store.findAll();
        assertThat(postsInDb, is(posts));
    }

    @AfterEach
    public void cleanTable() throws SQLException {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("delete from post")) {
            ps.execute();
        }
    }
}
