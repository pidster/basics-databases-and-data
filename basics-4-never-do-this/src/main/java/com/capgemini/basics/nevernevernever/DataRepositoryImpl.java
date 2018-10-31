package com.capgemini.basics.nevernevernever;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 *
 */
public class DataRepositoryImpl implements DataRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataRepositoryImpl.class);

    private final String jdbcUrl;
    private final Properties properties;

    public DataRepositoryImpl(String jdbcUrl, Properties properties) {
        this.jdbcUrl = jdbcUrl;
        this.properties = properties;

        try {
            init();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void create(ImportantData data) throws DataException {


        try (Connection connection = DriverManager.getConnection(jdbcUrl, properties);
             Statement statement = connection.createStatement()) {

            // This is terrible! Never do this!
            // Creating SQL statements by concatenating strings is bad practice and
            // exposes the application to potential SQL Injection vulnerabilities

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            String created = sdf.format(data.getCreated());
            String updated = sdf.format(data.getUpdated());

            String sql = "INSERT INTO important_data (name, created, updated) VALUES ('" + data.getName() + "', '" + created + "', '" + updated + "')";

            boolean success = statement.execute(sql);

            if (success) {
                throw new DataException("Only one update was expected!");
            }
        }
        catch (SQLException e) {
            throw new DataException(e);
        }
    }

    @Override
    public ImportantData findByName(String name) throws DataException {

        String sql = "SELECT id, name, created, updated FROM important_data WHERE name = '" + name + "'";

        ResultSet resultSet = null;
        Statement statement = null;
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(jdbcUrl, properties);
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);

            if (!resultSet.next()) {
                return null;
            }

            // Retrieve the values for each column
            long id = resultSet.getLong(1);
            String fooName = resultSet.getString(2);
            Date created = resultSet.getDate(3);
            Date updated = resultSet.getDate(4);

            // now create a ImportantData object from each of the fields
            ImportantData importantData = new ImportantData();
            importantData.setId(id);
            importantData.setName(fooName);
            importantData.setCreated(created);
            importantData.setUpdated(updated);

            return importantData;

        }
        catch (SQLException e) {
            throw new DataException(e);
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

            if (statement != null) {
                // don't forget to close the PreparedStatement!
                try {
                    statement.closeOnCompletion();
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
    public List<ImportantData> findAll() throws DataException {

        String sql = "SELECT id, name, created, updated FROM important_data ORDER BY updated";

        // Phew, finally a try-with-resources (or rather, _no_ finally...)
        try (Connection connection = DriverManager.getConnection(jdbcUrl, properties);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            List<ImportantData> importantDataList = new ArrayList<>();

            while (resultSet.next()) {

                long id = resultSet.getLong(1);
                String name = resultSet.getString(2);
                Date created = resultSet.getDate(3);
                Date updated = resultSet.getDate(4);

                // now create a ImportantData object from each of the fields
                ImportantData importantData = new ImportantData();
                importantData.setId(id);
                importantData.setName(name);
                importantData.setCreated(created);
                importantData.setUpdated(updated);

                importantDataList.add(importantData);
            }

            return importantDataList;

        }
        catch (SQLException e) {
            throw new DataException(e);
        }
    }

    /*
      setup method - would not be part of application, normally
     */
    private final void init() throws IOException {

        try (Connection connection = DriverManager.getConnection(jdbcUrl, properties);
             Statement statement = connection.createStatement()) {

            statement.execute("CREATE TABLE important_data (id IDENTITY, name VARCHAR(255), created DATE, updated DATE)");
        }
        catch (SQLException e) {
            throw new IOException(e);
        }
    }
}
