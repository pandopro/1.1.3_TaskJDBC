package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    private Connection connection;

    public UserDaoJDBCImpl() {
        connection = Util.getMysqlConnection();

    }

    public void createUsersTable() {
        try {
            Statement stmt = connection.createStatement();
            stmt.execute("create table if not exists user_table (id bigint auto_increment, name varchar(256), lastName varchar(256), age INT DEFAULT 0, primary key (id))");
            stmt.close();
        } catch (SQLException throwables) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            throwables.printStackTrace();
        }

    }

    public void dropUsersTable() {
        try {
            Statement stmt = connection.createStatement();
            stmt.execute("DROP TABLE if exists user_table");
            stmt.close();
        } catch (SQLException throwables) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            throwables.printStackTrace();
        }

    }

    public void saveUser(String name, String lastName, byte age) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT into user_table(name, lastName, age) values(?,?,?)");
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setLong(3, age);
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            throwables.printStackTrace();
        }

    }

    public void removeUserById(long id) {
        try {
            PreparedStatement ps = connection.prepareStatement("delete from user_table where id = ?");
            ps.setLong(1, id);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException throwables) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            throwables.printStackTrace();
        }
    }

    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement("select * from user_table");
            ps.executeQuery();
            ResultSet result = ps.getResultSet();
            while (result.next()) {
                if (!result.wasNull()) {
                    list.add(new User(result.getString("name"), result.getString("lastName"), Byte.parseByte(result.getString("age"))));
                }
            }
            result.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void cleanUsersTable() {
        try {
            Statement stmt = connection.createStatement();
            stmt.execute("TRUNCATE TABLE user_table");
            stmt.close();
        } catch (SQLException throwables) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            throwables.printStackTrace();
        }


    }
}
