package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

@ThreadSafe
@Repository
@AllArgsConstructor
public class SimpleCrudRepository implements CrudRepository {

    private final SessionFactory sf;

    private static final Logger LOG = LoggerFactory.getLogger(SimpleCrudRepository.class.getName());
    private static final String LOG_MESSAGE = "Exception in SimpleCrudRepository";

    @Override
    public boolean run(Consumer<Session> command) {
        boolean rslt = false;
        try {
            tx(session -> {
                        command.accept(session);
                        return null;
                    }
            );
            rslt = true;
        } catch (Exception e) {
            LOG.error(LOG_MESSAGE, e);
        }
        return rslt;
    }

    @Override
    public void run(String query, Map<String, Object> args) {
        Consumer<Session> command = session -> {
            var sq = session
                    .createQuery(query);
            for (Map.Entry<String, Object> arg : args.entrySet()) {
                sq.setParameter(arg.getKey(), arg.getValue());
            }
            sq.executeUpdate();
        };
        run(command);
    }

    @Override
    public <T> Optional<T> optional(String query, Class<T> cl, Map<String, Object> args) {
        Function<Session, Optional<T>> command = session -> {
            var sq = session
                    .createQuery(query, cl);
            for (Map.Entry<String, Object> arg : args.entrySet()) {
                sq.setParameter(arg.getKey(), arg.getValue());
            }
            return Optional.ofNullable(sq.getSingleResult());
        };
        try {
            return tx(command);
        } catch (Exception e) {
            LOG.error(LOG_MESSAGE, e);
        }
        return Optional.empty();
    }

    @Override
    public <T> List<T> query(String query, Class<T> cl) {
        Function<Session, List<T>> command = session -> session
                .createQuery(query, cl)
                .list();
        try {
            return tx(command);
        } catch (Exception e) {
            LOG.error(LOG_MESSAGE, e);
        }
        return new ArrayList<>();
    }

    @Override
    public <T> List<T> query(String query, Class<T> cl, Map<String, Object> args) {
        Function<Session, List<T>> command = session -> {
            var sq = session
                    .createQuery(query, cl);
            for (Map.Entry<String, Object> arg : args.entrySet()) {
                sq.setParameter(arg.getKey(), arg.getValue());
            }
            return sq.list();
        };
        try {
            return tx(command);
        } catch (Exception e) {
            LOG.error(LOG_MESSAGE, e);
        }
        return new ArrayList<>();
    }

    @Override
    public boolean query(String query, Map<String, Object> args) {
        Function<Session, Boolean> command = session -> {
            var sq = session.createQuery(query);
            args.forEach(sq::setParameter);
            return sq.executeUpdate() > 0;
        };
        try {
            return tx(command);
        } catch (Exception e) {
            LOG.error(LOG_MESSAGE, e);
        }
        return false;
    }

    @Override
    public <T> T tx(Function<Session, T> command) throws Exception {
        var session = sf.openSession();
        try (session) {
            var tx = session.beginTransaction();
            T rsl = command.apply(session);
            tx.commit();
            return rsl;
        } catch (Exception e) {
            var tx = session.getTransaction();
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new Exception(e);
        }
    }
}
