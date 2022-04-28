package ru.job4j.dreamjob.persistence;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Candidate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CandidateDBStore {

    private final BasicDataSource pool;

    public CandidateDBStore(BasicDataSource pool) {
        this.pool = pool;
    }

    public List<Candidate> findAll() {
        List<Candidate> candidates = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM candidate")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    candidates.add(new Candidate(
                            it.getInt("id"),
                            it.getString("name"),
                            it.getString("description"),
                            it.getString("created"),
                            it.getBoolean("visible"),
                            it.getBytes("photo"))
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return candidates;
    }

    public Candidate add(Candidate candidate) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(
                     "INSERT INTO candidate(name, description, created, visible, photo) VALUES (?, ?, ?, ?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, candidate.getName());
            ps.setString(2, candidate.getDescription());
            ps.setString(3, candidate.getCreated());
            ps.setBoolean(4, candidate.isVisible());
            ps.setBytes(5, candidate.getPhoto());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    candidate.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return candidate;
    }

    public void update(Candidate candidate) {
        try (Connection cn = pool.getConnection()) {
            PreparedStatement ps = cn.prepareStatement(
                    "UPDATE candidate SET name = ?, description = ?, visible = ?, photo = ? WHERE id = ?",
                    PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, candidate.getName());
            ps.setString(2, candidate.getDescription());
            ps.setBoolean(3, candidate.isVisible());
            ps.setBytes(4, candidate.getPhoto());
            ps.setInt(5, candidate.getId());
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Candidate findById(int id) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM candidate WHERE id = ?")
        ) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    return new Candidate(
                            it.getInt("id"),
                            it.getString("name"),
                            it.getString("description"),
                            it.getString("created"),
                            it.getBoolean("visible"),
                            it.getBytes("photo")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
