package com.capgemini.basics.springjdbc;

import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class BarRepositoryImpl implements BarRepository {

    private static final String INSERT_ONE = "INSERT INTO foo (name, created, updated) VALUES (?, ?, ?)";
    private static final String FIND_BY_NAME = "SELECT id, name, created, updated FROM foo WHERE name = ?";
    private static final String FIND_ALL = "SELECT id, name, created, updated FROM foo ORDER BY updated";

    private final JdbcTemplate jdbcTemplate;

    public BarRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void create(Bar bar) {
        jdbcTemplate.update(INSERT_ONE, preparedStatement -> {
            preparedStatement.setString(1, bar.getName());
            preparedStatement.setDate(2, new java.sql.Date(bar.getCreated().getTime()));
            preparedStatement.setDate(3, new java.sql.Date(bar.getUpdated().getTime()));
        });
    }

    @Override
    public Bar findByName(String name) {
        // use a method reference for convenience
        return jdbcTemplate.query(FIND_BY_NAME, this::mapRow, name);
    }

    @Override
    public List<Bar> findAll() {
        // use a method reference for convenience
        return jdbcTemplate.query(FIND_ALL, this::mapRowIndex);
    }

    private Bar mapRow(ResultSet resultSet) throws SQLException {
        return mapRowIndex(resultSet, 0);
    }

    private Bar mapRowIndex(ResultSet resultSet, int i) throws SQLException {

        if (resultSet.isBeforeFirst()) {
            resultSet.next();
        }

        // Retrieve the values for each column
        long id = resultSet.getLong(1);
        String fooName = resultSet.getString(2);
        Date created = resultSet.getDate(3);
        Date updated = resultSet.getDate(4);

        // now create a Foo object from each of the fields
        Bar bar = new Bar();
        bar.setId(id);
        bar.setName(fooName);
        bar.setCreated(created);
        bar.setUpdated(updated);

        return bar;
    }

}
