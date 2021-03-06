package ru.vlapin.examples.dbcp;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import lombok.val;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.*;

@Log4j2
public class ConnectToDB {

    @SneakyThrows
    public static void main(String... args) {
        Class.forName("org.h2.Driver");

        try (val connection = DriverManager.getConnection("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
             val statement = connection.createStatement()) {

            logInfo("Соединение установлено.");

            init(statement);

            statement.executeUpdate(
                    "INSERT INTO students (name, id_group) VALUES ('Баба-Яга', 123456)");

            try (val resultSet = statement.executeQuery("SELECT id, name, id_group FROM students")) {
                while (resultSet.next())
                    System.out.printf("%d %s %d%n",
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getInt("id_group"));
            }
        }
    }

    private static void init(Statement st) throws IOException, SQLException {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        ConnectToDB.class.getResourceAsStream("init.sql"),
                        StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null)
                st.executeUpdate(line);
        }
    }

    private static void logInfo(String message) {
        System.out.println(message);
        log.info(message);
    }
}
