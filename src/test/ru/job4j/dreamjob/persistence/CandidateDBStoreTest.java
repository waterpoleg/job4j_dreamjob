package ru.job4j.dreamjob.persistence;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ru.job4j.dreamjob.Main;
import ru.job4j.dreamjob.model.Candidate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class CandidateDBStoreTest {

    private final static BasicDataSource pool = new Main().loadPool();
    private final static CandidateDBStore store = new CandidateDBStore(pool);

    private Candidate candidate = new Candidate(
            0,
            "name",
            "description",
            "28/04/2022",
            true,
            new byte[]{0, 0}
    );

    @Test
    public void whenCreateCandidate() {
        store.add(candidate);
        Candidate candidateInDb = store.findById(candidate.getId());
        assertThat(candidateInDb.getName(), is(candidate.getName()));
    }

    @Test
    public void whenUpdatePost() {
        store.add(candidate);
        candidate.setName("new name");
        store.update(candidate);
        Candidate candidateInDb = store.findById(candidate.getId());
        assertEquals("new name", candidateInDb.getName());
    }

    @Test
    public void whenFindByIdPost() {
        store.add(candidate);
        Candidate candidateInDb = store.findById(candidate.getId());
        assertEquals(candidateInDb.getId(), candidate.getId());
    }

    @Test
    public void whenNotFindById() {
        assertNull(store.findById(-1));
    }

    @Test
    public void whenFindAllPosts() {
        Candidate candidate1 = new Candidate(
                0, "name1", "descr1", "08.01.2022", true, new byte[]{0, 0}
        );
        Candidate candidate2 = new Candidate(
                1, "name1", "descr1", "25.12.2021", true, new byte[]{0, 0}
        );
        Candidate candidate3 = new Candidate(
                2, "name1", "descr1", "15.11.2021", true, new byte[]{0, 0}
        );
        store.add(candidate1);
        store.add(candidate2);
        store.add(candidate3);
        List<Candidate> candidates = List.of(candidate1, candidate2, candidate3);
        List<Candidate> candidatesInDb = store.findAll();
        assertThat(candidatesInDb, is(candidates));
    }

    @AfterEach
    public void cleanTable() throws SQLException {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("delete from candidate")) {
            ps.execute();
        }
    }
}
