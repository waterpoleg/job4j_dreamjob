package ru.job4j.dreamjob.services;

import ru.job4j.dreamjob.model.Post;
import ru.job4j.dreamjob.persistence.PostStore;

import java.util.Collection;

public class PostService {

    private static final PostService INST = new PostService();
    private final PostStore store = PostStore.instOf();

    public static PostService instOf() {
        return INST;
    }

    public void add(Post post) {
        store.add(post);
    }

    public Collection<Post> findAll() {
        return store.findAll();
    }

    public Post findById(int id) {
        return store.findById(id);
    }

    public void update(Post post) {
        store.update(post);
    }
}
