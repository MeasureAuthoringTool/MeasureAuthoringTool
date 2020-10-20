package tools;

import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Slf4j
public class ObfuscateMatUsers {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/mat_app_blank?" +
            "serverTimezone=UTC&characterEncoding=latin1&useConfigs=maxPerformance";
    private static final String DB_USER = "root";
    private static final String DB_P = "password";

    private static final String UPDATE_SQL =
            "update USER " +
                    "set EMAIL_ADDRESS = ?, " +
                    "FIRST_NAME = ?, " +
                    "LAST_NAME = ?, " +
                    "MIDDLE_INITIAL = ?, " +
                    "LOGIN_ID = ?, " +
                    "PHONE_NO = ?, " +
                    "TITLE = ? " +
                    "where USER_ID = ?";


    private static final String FIND_USER_ID_SQL =
            "select * from USER where LOGIN_ID = ?";


    private static Set<String> uniqueLoginsNames = new HashSet<>();

    public static void main(String args[]) throws Exception {
        Connection connection = getConnection();
        List<String> userIds = getIds(connection);
        log.debug("Found {} userIds", userIds.size());

        connection.setAutoCommit(false);
        try {
            processIds(connection, userIds);
            connection.commit();
            log.debug("Committed obfuscation changes");
        } catch (Exception e) {
            connection.rollback();
            log.error("Error Changed are roll-backed");
            throw e;
        } finally {
            connection.close();
        }
    }

    private static void processIds(Connection connection, List<String> userIds) throws SQLException {
        Faker faker = new Faker(new Random());
        PreparedStatement updateStatement = connection.prepareStatement(UPDATE_SQL);
        PreparedStatement findStatement = connection.prepareStatement(FIND_USER_ID_SQL);

        for (String id : userIds) {
            updateStatement.setString(1, "fake@fakeme.out");
            updateStatement.setString(2, faker.name().firstName());
            updateStatement.setString(3, faker.name().lastName());
            updateStatement.setString(4, "");
            updateStatement.setString(5, findUniqueLoginName(faker, findStatement));
            updateStatement.setString(6, faker.phoneNumber().phoneNumber());
            updateStatement.setString(7, "");
            updateStatement.setString(8, id);
            updateStatement.executeUpdate();
        }

        updateStatement.close();
    }

    private static String findUniqueLoginName(Faker faker, PreparedStatement findStatement) throws SQLException {
        while (true) {
            String loginName = faker.name().username();

            if (uniqueLoginsNames.contains(loginName)) {
                log.warn("Found a name clash in Set with loginName: {}", loginName);
            } else {
                if (existsInDB(loginName, findStatement)) {
                    log.warn("Found a name clash in DB with loginName: {}", loginName);
                } else {
                    uniqueLoginsNames.add(loginName);
                    return loginName;
                }
            }
        }
    }

    private static boolean existsInDB(String loginName, PreparedStatement findStatement) throws SQLException {
        findStatement.setString(1, loginName);
        ResultSet resultSet = findStatement.executeQuery();
        return resultSet.next();
    }


    private static List<String> getIds(Connection connection) throws Exception {
        List<String> userIds = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select USER_ID from USER");

        while (resultSet.next()) {
            userIds.add(resultSet.getString(1));
        }

        return userIds;
    }

    private static Connection getConnection() throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        return DriverManager.getConnection(DB_URL, DB_USER, DB_P);
    }
}