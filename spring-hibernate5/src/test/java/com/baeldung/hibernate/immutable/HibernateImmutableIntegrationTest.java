package com.baeldung.hibernate.immutable;

import com.baeldung.hibernate.immutable.entities.Event;
import com.baeldung.hibernate.immutable.util.HibernateUtil;
import com.google.common.collect.Sets;
import org.hibernate.CacheMode;
import org.hibernate.Session;
import org.junit.*;
import org.junit.rules.ExpectedException;

import javax.persistence.PersistenceException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class HibernateImmutableIntegrationTest {

    private static Session session;

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Before
    public void before() {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        createEvent();
        session.setCacheMode(CacheMode.REFRESH);
    }

    @BeforeClass
    public static void setup() {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
    }

    @AfterClass
    public static void teardown() {
        HibernateUtil.getSessionFactory().close();
    }

    @Test
    public void addEvent() {
        Event event = new Event();
        event.setId(2L);
        event.setTitle("Public Event");
        session.save(event);
        session.getTransaction().commit();
    }

    @Test
    public void updateEvent() {
        Event event = (Event) session.createQuery("FROM Event WHERE title='New Event'").list().get(0);
        event.setTitle("Private Event");
        session.update(event);
        session.flush();
        session.refresh(event);

        assertThat(event.getTitle(), equalTo("New Event"));
        assertThat(event.getId(), equalTo(5L));
    }

    @Test
    public void deleteEvent() {
        Event event = (Event) session.createQuery("FROM Event WHERE title='New Event'").list().get(0);
        session.delete(event);
        session.getTransaction().commit();
    }

    @Test
    public void addGuest() {
        Event event = (Event) session.createQuery("FROM Event WHERE title='New Event'").list().get(0);
        String newGuest = "Sara";
        event.getGuestList().add(newGuest);

        exception.expect(PersistenceException.class);
        session.save(event);
        session.getTransaction().commit();
    }

    @Test
    public void deleteCascade() {
        Event event = (Event) session.createQuery("FROM Event WHERE title='New Event'").list().get(0);
        String guest = event.getGuestList().iterator().next();
        event.getGuestList().remove(guest);

        exception.expect(PersistenceException.class);
        session.saveOrUpdate(event);
        session.getTransaction().commit();
    }

    private static void createEvent() {
        Event event = new Event(5L, "New Event", Sets.newHashSet("guest"));
        session.save(event);
    }
}
