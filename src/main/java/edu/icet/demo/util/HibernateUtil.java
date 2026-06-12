package edu.icet.demo.util;

import edu.icet.demo.entity.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Builds a single Hibernate SessionFactory configured for PostgreSQL (Supabase).
 * Connection settings are read from src/main/resources/application.properties
 * so the shop owner can plug in their own Supabase credentials without
 * touching the code.
 */
public final class HibernateUtil {

    private static SessionFactory sessionFactory;

    private HibernateUtil() {
    }

    private static synchronized SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            Properties config = loadConfig();

            Map<String, Object> settings = new HashMap<>();
            settings.put("hibernate.connection.driver_class", "org.postgresql.Driver");
            settings.put("hibernate.connection.url", config.getProperty("db.url"));
            settings.put("hibernate.connection.username", config.getProperty("db.username"));
            settings.put("hibernate.connection.password", config.getProperty("db.password"));
            settings.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
            settings.put("hibernate.hbm2ddl.auto", config.getProperty("hibernate.hbm2ddl.auto", "update"));
            settings.put("hibernate.show_sql", config.getProperty("hibernate.show_sql", "false"));

            StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                    .applySettings(settings)
                    .build();

            try {
                Metadata metadata = new MetadataSources(registry)
                        .addAnnotatedClass(UserEntity.class)
                        .addAnnotatedClass(CustomerEntity.class)
                        .addAnnotatedClass(SupplierEntity.class)
                        .addAnnotatedClass(ProductEntity.class)
                        .addAnnotatedClass(OrderEntity.class)
                        .addAnnotatedClass(OrderDetailEntity.class)
                        .buildMetadata();
                sessionFactory = metadata.getSessionFactoryBuilder().build();
            } catch (RuntimeException e) {
                StandardServiceRegistryBuilder.destroy(registry);
                e.printStackTrace();
                throw new IllegalStateException(
                        "Could not connect to the database. Check the Supabase connection "
                                + "settings in application.properties.\n\nCause: " + rootCauseMessage(e), e);
            }
        }
        return sessionFactory;
    }

    /** Walks the cause chain to the most specific (root) error message. */
    private static String rootCauseMessage(Throwable e) {
        Throwable cause = e;
        while (cause.getCause() != null && cause.getCause() != cause) {
            cause = cause.getCause();
        }
        return cause.getMessage() == null ? cause.getClass().getSimpleName() : cause.getMessage();
    }

    private static Properties loadConfig() {
        Properties properties = new Properties();
        try (InputStream in = HibernateUtil.class.getResourceAsStream("/application.properties")) {
            if (in == null) {
                throw new IllegalStateException("application.properties not found on the classpath.");
            }
            properties.load(in);
        } catch (IOException e) {
            throw new IllegalStateException("Could not read application.properties.", e);
        }
        return properties;
    }

    public static Session getSession() {
        return getSessionFactory().openSession();
    }
}
