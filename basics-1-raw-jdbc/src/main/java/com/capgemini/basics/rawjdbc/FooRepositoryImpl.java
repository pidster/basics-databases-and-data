package com.capgemini.basics.rawjdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 *
 */
public class FooRepositoryImpl implements FooRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(FooRepositoryImpl.class);

    private static final String INSERT_ONE = "INSERT INTO foo (name, created, updated) VALUES (?, ?, ?)";
    private static final String FIND_BY_NAME = "SELECT id, name, created, updated FROM foo WHERE name = ?";
    private static final String FIND_ALL = "SELECT id, name, created, updated FROM foo ORDER BY updated";

    private final String jdbcUrl;
    private final Properties properties;

    public FooRepositoryImpl(String jdbcUrl, Properties properties) {
        this.jdbcUrl = jdbcUrl;
        this.properties = properties;

        try {
            init();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void create(Foo foo) throws FooException {

        // In earlier days we didn't have try-with-resources, so the
        // code was extremely verbose; writing finally { close } statements
        // was *incredibly* repetitive...

        try (Connection connection = DriverManager.getConnection(jdbcUrl, properties);
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_ONE)) {

            preparedStatement.setString(1, foo.getName());
            preparedStatement.setDate(2, new java.sql.Date(foo.getCreated().getTime()));
            preparedStatement.setDate(3, new java.sql.Date(foo.getUpdated().getTime()));

            int updated = preparedStatement.executeUpdate();

            if (updated != 1) {
                throw new FooException("One update was expected!");
            }
        }
        catch (SQLException e) {
            throw new FooException(e);
        }
    }

    @Override
    public Foo findByName(String name) throws FooException {

        // Here, we'll adopt the typical style used prior to the
        // existence of try-with-resources clauses.

        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        Connection connection = null;

        try {

            //
            connection = DriverManager.getConnection(jdbcUrl, properties);

            // Using a PreparedStatement is an optimisation
            preparedStatement = connection.prepareStatement(FIND_BY_NAME);
            preparedStatement.setString(1, name);

            resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                throw new FooException("No results but one result was expected!");
            }

            // Retrieve the values for each column
            long id = resultSet.getLong(1);
            String fooName = resultSet.getString(2);
            Date created = resultSet.getDate(3);
            Date updated = resultSet.getDate(4);

            // now create a Foo object from each of the fields
            Foo foo = new Foo();
            foo.setId(id);
            foo.setName(fooName);
            foo.setCreated(created);
            foo.setUpdated(updated);

            return foo;

        }
        catch (SQLException e) {
            throw new FooException(e);
        }
        finally {

            // Look at this boiler plate! Often, this kind of undifferentiated code
            // was factored out into a utility class

            if (resultSet != null) {
                // don't forget to close the ResultSet!
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    LOGGER.error("ResultSet fail!", e);
                }
            }

            if (preparedStatement != null) {
                // don't forget to close the PreparedStatement!
                try {
                    preparedStatement.closeOnCompletion();
                } catch (SQLException e) {
                    LOGGER.error("PreparedStatement fail!", e);
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                }
                catch (SQLException e) {
                    LOGGER.error("Connection fail!", e);
                }
            }
        }
    }

    @Override
    public List<Foo> findAll() throws FooException {

        // Phew, finally a try-with-resources (or rather, _no_ finally...)
        try (Connection connection = DriverManager.getConnection(jdbcUrl, properties);
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            List<Foo> fooList = new ArrayList<>();

            while (resultSet.next()) {

                long id = resultSet.getLong(1);
                String name = resultSet.getString(2);
                Date created = resultSet.getDate(3);
                Date updated = resultSet.getDate(4);

                // now create a Foo object from each of the fields
                Foo foo = new Foo();
                foo.setId(id);
                foo.setName(name);
                foo.setCreated(created);
                foo.setUpdated(updated);

                fooList.add(foo);
            }

            return fooList;

        }
        catch (SQLException e) {
            throw new FooException(e);
        }
    }

    /*
      setup method - would not be part of application, normally
     */
    private final void init() throws IOException {

        try (Connection connection = DriverManager.getConnection(jdbcUrl, properties);
             Statement statement = connection.createStatement()) {

            statement.execute("CREATE TABLE foo (id IDENTITY, name VARCHAR(255), created DATE, updated DATE)");
        }
        catch (SQLException e) {
            throw new IOException(e);
        }
    }
}
