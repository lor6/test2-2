package com.baeldung.hibernate.converter;

import java.io.IOException;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.baeldung.hibernate.HibernateUtil;
import com.baeldung.hibernate.pojo.Person;
import com.baeldung.hibernate.pojo.PersonName;
import com.vividsolutions.jts.util.Assert;

public class PersonNameConverterTest {

    private Session session;
    private Transaction transaction;

    @Before
    public void setUp() throws IOException {
        session = HibernateUtil.getSessionFactory()
            .openSession();
        transaction = session.beginTransaction();

        session.createNativeQuery("delete from personTable")
            .executeUpdate();

        transaction.commit();
        transaction = session.beginTransaction();
    }

    @After
    public void tearDown() {
        transaction.rollback();
        session.close();
    }

    @Test
    public void givenPersonName_WhenSaving_ThenNameAndSurnameConcat() {
        final String name = "name";
        final String surname = "surname";

        PersonName personName = new PersonName();
        personName.setName(name);
        personName.setSurname(surname);

        Person person = new Person();
        person.setPersonName(personName);

        Long id = (Long) session.save(person);

        session.flush();
        session.clear();

        String dbPersonName = (String) session.createNativeQuery("select p.personName from PersonTable p where p.id = :id")
            .setParameter("id", id)
            .getSingleResult();

        Assert.equals(surname + ", " + name, dbPersonName);

        Person dbPerson = session.createNativeQuery("select * from PersonTable p where p.id = :id", Person.class)
            .setParameter("id", id)
            .getSingleResult();

        Assert.equals(dbPerson.getPersonName()
            .getName(), name);
        Assert.equals(dbPerson.getPersonName()
            .getSurname(), surname);
    }

}
