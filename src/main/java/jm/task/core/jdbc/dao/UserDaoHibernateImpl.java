package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    private Session session;
    private Transaction transaction;
    private SessionFactory sf;

    public UserDaoHibernateImpl() {
        sf = Util.getSessionFactory();
    }

    @Override
    public void createUsersTable() {
        try {
            session = sf.openSession();
            transaction = session.beginTransaction();
            session.createSQLQuery("create table if not exists user_table(id bigint auto_increment, name varchar(256), lastName varchar(256), age INT DEFAULT 0, primary key (id))").executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            System.out.println("error---createUsersTable");
        } finally {
            session.close();
        }
    }

    @Override
    public void dropUsersTable() {
        try {
            session = sf.openSession();
            transaction = session.beginTransaction();
            session.createSQLQuery("DROP TABLE if exists user_table").executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            System.out.println("error---dropUsersTable" + e);
            transaction.rollback();
        } finally {
            session.close();
        }

    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try {
            session = sf.openSession();
            transaction = session.beginTransaction();
            User user = new User(name, lastName, age);
            session.save(user);
        } catch (Exception e) {
            transaction.rollback();
            System.out.println("error---saveUser" + e);

        } finally {
            transaction.commit();
            session.close();
        }
    }

    @Override
    public void removeUserById(long id) {
        Transaction transaction = null;
        try {
            session = sf.openSession();
            transaction = session.beginTransaction();
            User user = (User) session.get(User.class, id);
            session.delete(user);
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            transaction.commit();
            session.close();
        }
    }

    @Override
    public List<User> getAllUsers() {
        session = sf.openSession();
        Transaction transaction = session.beginTransaction();
        List<User> users = session.createQuery("FROM User").list();
        transaction.commit();
        session.close();
        return users;
    }

    @Override
    public void cleanUsersTable() {
        try {
            session = sf.openSession();
            transaction = session.beginTransaction();
            session.createQuery("delete from User").executeUpdate();
        } catch (Exception e) {
            System.out.println("error---cleanUsersTable" + e);
            transaction.rollback();
        } finally {
            transaction.commit();
            session.close();
        }
    }
}
