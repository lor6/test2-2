package com.baeldung.hibernate;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

import com.baeldung.hibernate.entities.DeptEmployee;
import com.baeldung.hibernate.pojo.Employee;
import com.baeldung.hibernate.pojo.EntityDescription;
import com.baeldung.hibernate.pojo.Phone;
import com.baeldung.hibernate.pojo.TemporalValues;
import com.baeldung.hibernate.pojo.inheritance.Animal;
import com.baeldung.hibernate.pojo.inheritance.Bag;
import com.baeldung.hibernate.pojo.inheritance.Book;
import com.baeldung.hibernate.pojo.inheritance.Car;
import com.baeldung.hibernate.pojo.inheritance.MyEmployee;
import com.baeldung.hibernate.pojo.inheritance.MyProduct;
import com.baeldung.hibernate.pojo.inheritance.Pen;
import com.baeldung.hibernate.pojo.inheritance.Pet;
import com.baeldung.hibernate.pojo.inheritance.Vehicle;

public class HibernateUtil {
    private static String PROPERTY_FILE_NAME;

    public static SessionFactory getSessionFactory() throws IOException {
        return getSessionFactory(null);
    }

    public static SessionFactory getSessionFactory(String propertyFileName) throws IOException {
        PROPERTY_FILE_NAME = propertyFileName;
        ServiceRegistry serviceRegistry = configureServiceRegistry();
        return makeSessionFactory(serviceRegistry);
    }

    public static SessionFactory getSessionFactoryByProperties(Properties properties) throws IOException {
        ServiceRegistry serviceRegistry = configureServiceRegistry(properties);
        return makeSessionFactory(serviceRegistry);
    }

    private static SessionFactory makeSessionFactory(ServiceRegistry serviceRegistry) {
        MetadataSources metadataSources = new MetadataSources(serviceRegistry);

        metadataSources.addPackage("com.baeldung.hibernate.pojo");
        metadataSources.addAnnotatedClass(Employee.class);
        metadataSources.addAnnotatedClass(Phone.class);
        metadataSources.addAnnotatedClass(EntityDescription.class);
        metadataSources.addAnnotatedClass(TemporalValues.class);
        metadataSources.addAnnotatedClass(DeptEmployee.class);
        metadataSources.addAnnotatedClass(com.baeldung.hibernate.entities.Department.class);
        metadataSources.addAnnotatedClass(Animal.class);
        metadataSources.addAnnotatedClass(Bag.class);
        metadataSources.addAnnotatedClass(Book.class);
        metadataSources.addAnnotatedClass(Car.class);
        metadataSources.addAnnotatedClass(MyEmployee.class);
        metadataSources.addAnnotatedClass(MyProduct.class);
        metadataSources.addAnnotatedClass(Pen.class);
        metadataSources.addAnnotatedClass(Pet.class);
        metadataSources.addAnnotatedClass(Vehicle.class);
        

        Metadata metadata = metadataSources.getMetadataBuilder()
            .build();

        return metadata.getSessionFactoryBuilder()
                .build();

    }

    private static ServiceRegistry configureServiceRegistry() throws IOException {
        return configureServiceRegistry(getProperties());
    }

    private static ServiceRegistry configureServiceRegistry(Properties properties) throws IOException {
        return new StandardServiceRegistryBuilder().applySettings(properties)
                .build();
    }

    public static Properties getProperties() throws IOException {
        Properties properties = new Properties();
        URL propertiesURL = Thread.currentThread()
          .getContextClassLoader()
          .getResource(StringUtils.defaultString(PROPERTY_FILE_NAME, "hibernate.properties"));
        try (FileInputStream inputStream = new FileInputStream(propertiesURL.getFile())) {
            properties.load(inputStream);
        }
        return properties;
    }
}