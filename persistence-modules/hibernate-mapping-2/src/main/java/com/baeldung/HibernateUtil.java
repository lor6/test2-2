package com.baeldung;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// import com.baeldung.associations.unidirectional.Department;
import com.baeldung.manytomany.model.Employee;
import com.baeldung.manytomany.model.Project;
import com.baeldung.uuids.WebSiteUser;
import com.baeldung.uuids.Element;
import com.baeldung.uuids.Reservation;
import com.baeldung.uuids.Sale;

public class HibernateUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(HibernateUtil.class);
    private static SessionFactory sessionFactory;

    private static SessionFactory buildSessionFactory() {
        try {
            // Create the SessionFactory from hibernate-annotation.cfg.xml
            Configuration configuration = new Configuration();
            configuration.addAnnotatedClass(Employee.class);
            configuration.addAnnotatedClass(Project.class);
            configuration.addAnnotatedClass(WebSiteUser.class);
            configuration.addAnnotatedClass(Element.class);
            configuration.addAnnotatedClass(Reservation.class);
            configuration.addAnnotatedClass(Sale.class);
            // configuration.addAnnotatedClass(Department.class);
            configuration.configure("manytomany.cfg.xml");
            LOGGER.debug("Hibernate Annotation Configuration loaded");

            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties())
                    .build();
            LOGGER.debug("Hibernate Annotation serviceRegistry created");

            SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);

            return sessionFactory;
        } catch (Throwable ex) {
            LOGGER.error("Initial SessionFactory creation failed.", ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            sessionFactory = buildSessionFactory();
        }
        return sessionFactory;
    }
}
