package com.baeldung.dao.repositories.user;

import com.baeldung.config.PersistenceConfiguration;
import com.baeldung.domain.user.User;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

/**
 * Created by adam.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PersistenceConfiguration.class)
@DirtiesContext
public class UserRepositoryIntegrationTest {

    private final String USER_NAME_ADAM = "Adam";
    private final String USER_NAME_PETER = "Peter";

    private final String USER_EMAIL = "email@example.com";
    private final String USER_EMAIL2 = "email2@example.com";
    private final String USER_EMAIL3 = "email3@example.com";
    private final String USER_EMAIL4 = "email4@example.com";
    private final String USER_EMAIL5 = "email5@example.com";
    private final String USER_EMAIL6 = "email6@example.com";

    private final Integer INACTIVE_STATUS = 0;
    private final Integer ACTIVE_STATUS = 1;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EntityManager entityManager;

    @Test
    @Transactional
    public void givenUsersWithSameNameInDBWhenFindAllByNameThenReturnStreamOfUsers() {
        User user1 = new User();
        user1.setName(USER_NAME_ADAM);
        user1.setEmail(USER_EMAIL);
        userRepository.save(user1);

        User user2 = new User();
        user2.setName(USER_NAME_ADAM);
        user2.setEmail(USER_EMAIL2);
        userRepository.save(user2);

        User user3 = new User();
        user3.setName(USER_NAME_ADAM);
        user3.setEmail(USER_EMAIL3);
        userRepository.save(user3);

        User user4 = new User();
        user4.setName("SAMPLE");
        user4.setEmail(USER_EMAIL4);
        userRepository.save(user4);

        try (Stream<User> foundUsersStream = userRepository.findAllByName(USER_NAME_ADAM)) {
            assertThat(foundUsersStream.count()).isEqualTo(3l);
        }
    }

    @Test
    public void givenUsersInDBWhenFindAllWithQueryAnnotationThenReturnCollectionWithActiveUsers() {
        User user1 = new User();
        user1.setName(USER_NAME_ADAM);
        user1.setEmail(USER_EMAIL);
        user1.setStatus(ACTIVE_STATUS);
        userRepository.save(user1);

        User user2 = new User();
        user2.setName(USER_NAME_ADAM);
        user2.setEmail(USER_EMAIL2);
        user2.setStatus(ACTIVE_STATUS);
        userRepository.save(user2);

        User user3 = new User();
        user3.setName(USER_NAME_ADAM);
        user3.setEmail(USER_EMAIL3);
        user3.setStatus(INACTIVE_STATUS);
        userRepository.save(user3);

        Collection<User> allActiveUsers = userRepository.findAllActiveUsers();

        assertThat(allActiveUsers.size()).isEqualTo(2);
    }

    @Test
    public void givenUsersInDBWhenFindAllWithQueryAnnotationNativeThenReturnCollectionWithActiveUsers() {
        User user1 = new User();
        user1.setName(USER_NAME_ADAM);
        user1.setEmail(USER_EMAIL);
        user1.setStatus(ACTIVE_STATUS);
        userRepository.save(user1);

        User user2 = new User();
        user2.setName(USER_NAME_ADAM);
        user2.setEmail(USER_EMAIL2);
        user2.setStatus(ACTIVE_STATUS);
        userRepository.save(user2);

        User user3 = new User();
        user3.setName(USER_NAME_ADAM);
        user3.setEmail(USER_EMAIL3);
        user3.setStatus(INACTIVE_STATUS);
        userRepository.save(user3);

        Collection<User> allActiveUsers = userRepository.findAllActiveUsersNative();

        assertThat(allActiveUsers.size()).isEqualTo(2);
    }

    @Test
    public void givenUserInDBWhenFindUserByStatusWithQueryAnnotationThenReturnActiveUser() {
        User user = new User();
        user.setName(USER_NAME_ADAM);
        user.setEmail(USER_EMAIL);
        user.setStatus(ACTIVE_STATUS);
        userRepository.save(user);

        User userByStatus = userRepository.findUserByStatus(ACTIVE_STATUS);

        assertThat(userByStatus.getName()).isEqualTo(USER_NAME_ADAM);
    }

    @Test
    public void givenUserInDBWhenFindUserByStatusWithQueryAnnotationNativeThenReturnActiveUser() {
        User user = new User();
        user.setName(USER_NAME_ADAM);
        user.setEmail(USER_EMAIL);
        user.setStatus(ACTIVE_STATUS);
        userRepository.save(user);

        User userByStatus = userRepository.findUserByStatusNative(ACTIVE_STATUS);

        assertThat(userByStatus.getName()).isEqualTo(USER_NAME_ADAM);
    }

    @Test
    public void givenUsersInDBWhenFindUserByStatusAndNameWithQueryAnnotationIndexedParamsThenReturnOneUser() {
        User user = new User();
        user.setName(USER_NAME_ADAM);
        user.setEmail(USER_EMAIL);
        user.setStatus(ACTIVE_STATUS);
        userRepository.save(user);

        User user2 = new User();
        user2.setName(USER_NAME_PETER);
        user2.setEmail(USER_EMAIL2);
        user2.setStatus(ACTIVE_STATUS);
        userRepository.save(user2);

        User userByStatus = userRepository.findUserByStatusAndName(ACTIVE_STATUS, USER_NAME_ADAM);

        assertThat(userByStatus.getName()).isEqualTo(USER_NAME_ADAM);
    }

    @Test
    public void givenUsersInDBWhenFindUserByStatusAndNameWithQueryAnnotationNamedParamsThenReturnOneUser() {
        User user = new User();
        user.setName(USER_NAME_ADAM);
        user.setEmail(USER_EMAIL);
        user.setStatus(ACTIVE_STATUS);
        userRepository.save(user);

        User user2 = new User();
        user2.setName(USER_NAME_PETER);
        user2.setEmail(USER_EMAIL2);
        user2.setStatus(ACTIVE_STATUS);
        userRepository.save(user2);

        User userByStatus = userRepository.findUserByStatusAndNameNamedParams(ACTIVE_STATUS, USER_NAME_ADAM);

        assertThat(userByStatus.getName()).isEqualTo(USER_NAME_ADAM);
    }

    @Test
    public void givenUsersInDBWhenFindUserByStatusAndNameWithQueryAnnotationNativeNamedParamsThenReturnOneUser() {
        User user = new User();
        user.setName(USER_NAME_ADAM);
        user.setEmail(USER_EMAIL);
        user.setStatus(ACTIVE_STATUS);
        userRepository.save(user);

        User user2 = new User();
        user2.setName(USER_NAME_PETER);
        user2.setEmail(USER_EMAIL2);
        user2.setStatus(ACTIVE_STATUS);
        userRepository.save(user2);

        User userByStatus = userRepository.findUserByStatusAndNameNamedParamsNative(ACTIVE_STATUS, USER_NAME_ADAM);

        assertThat(userByStatus.getName()).isEqualTo(USER_NAME_ADAM);
    }

    @Test
    public void givenUsersInDBWhenFindUserByStatusAndNameWithQueryAnnotationNamedParamsCustomNamesThenReturnOneUser() {
        User user = new User();
        user.setName(USER_NAME_ADAM);
        user.setEmail(USER_EMAIL);
        user.setStatus(ACTIVE_STATUS);
        userRepository.save(user);

        User user2 = new User();
        user2.setName(USER_NAME_PETER);
        user2.setEmail(USER_EMAIL2);
        user2.setStatus(ACTIVE_STATUS);
        userRepository.save(user2);

        User userByStatus = userRepository.findUserByUserStatusAndUserName(ACTIVE_STATUS, USER_NAME_ADAM);

        assertThat(userByStatus.getName()).isEqualTo(USER_NAME_ADAM);
    }

    @Test
    public void givenUsersInDBWhenFindUserByNameLikeWithQueryAnnotationIndexedParamsThenReturnUser() {
        User user = new User();
        user.setName(USER_NAME_ADAM);
        user.setEmail(USER_EMAIL);
        user.setStatus(ACTIVE_STATUS);
        userRepository.save(user);

        User userByStatus = userRepository.findUserByNameLike("Ad");

        assertThat(userByStatus.getName()).isEqualTo(USER_NAME_ADAM);
    }

    @Test
    public void givenUsersInDBWhenFindUserByNameLikeWithQueryAnnotationNamedParamsThenReturnUser() {
        User user = new User();
        user.setName(USER_NAME_ADAM);
        user.setEmail(USER_EMAIL);
        user.setStatus(ACTIVE_STATUS);
        userRepository.save(user);

        User userByStatus = userRepository.findUserByNameLikeNamedParam("Ad");

        assertThat(userByStatus.getName()).isEqualTo(USER_NAME_ADAM);
    }

    @Test
    public void givenUsersInDBWhenFindUserByNameLikeWithQueryAnnotationNativeThenReturnUser() {
        User user = new User();
        user.setName(USER_NAME_ADAM);
        user.setEmail(USER_EMAIL);
        user.setStatus(ACTIVE_STATUS);
        userRepository.save(user);

        User userByStatus = userRepository.findUserByNameLikeNative("Ad");

        assertThat(userByStatus.getName()).isEqualTo(USER_NAME_ADAM);
    }

    @Test
    public void givenUsersInDBWhenFindAllWithSortByNameThenReturnUsersSorted() {
        userRepository.save(new User(USER_NAME_ADAM, LocalDate.now(), USER_EMAIL, ACTIVE_STATUS));
        userRepository.save(new User(USER_NAME_PETER, LocalDate.now(), USER_EMAIL2, ACTIVE_STATUS));
        userRepository.save(new User("SAMPLE", LocalDate.now(), USER_EMAIL3, INACTIVE_STATUS));

        List<User> usersSortByName = userRepository.findAll(new Sort(Sort.Direction.ASC, "name"));

        assertThat(usersSortByName.get(0)
            .getName()).isEqualTo(USER_NAME_ADAM);
    }

    @Test(expected = PropertyReferenceException.class)
    public void givenUsersInDBWhenFindAllSortWithFunctionThenThrowException() {
        userRepository.save(new User(USER_NAME_ADAM, LocalDate.now(), USER_EMAIL, ACTIVE_STATUS));
        userRepository.save(new User(USER_NAME_PETER, LocalDate.now(), USER_EMAIL2, ACTIVE_STATUS));
        userRepository.save(new User("SAMPLE", LocalDate.now(), USER_EMAIL3, INACTIVE_STATUS));

        userRepository.findAll(new Sort(Sort.Direction.ASC, "name"));

        List<User> usersSortByNameLength = userRepository.findAll(new Sort("LENGTH(name)"));

        assertThat(usersSortByNameLength.get(0)
            .getName()).isEqualTo(USER_NAME_ADAM);
    }

    @Test
    public void givenUsersInDBWhenFindAllSortWithFunctionQueryAnnotationJPQLThenReturnUsersSorted() {
        userRepository.save(new User(USER_NAME_ADAM, LocalDate.now(), USER_EMAIL, ACTIVE_STATUS));
        userRepository.save(new User(USER_NAME_PETER, LocalDate.now(), USER_EMAIL2, ACTIVE_STATUS));
        userRepository.save(new User("SAMPLE", LocalDate.now(), USER_EMAIL3, INACTIVE_STATUS));

        userRepository.findAllUsers(new Sort("name"));

        List<User> usersSortByNameLength = userRepository.findAllUsers(JpaSort.unsafe("LENGTH(name)"));

        assertThat(usersSortByNameLength.get(0)
            .getName()).isEqualTo(USER_NAME_ADAM);
    }

    @Test
    public void givenUsersInDBWhenFindAllWithPageRequestQueryAnnotationJPQLThenReturnPageOfUsers() {
        userRepository.save(new User(USER_NAME_ADAM, LocalDate.now(), USER_EMAIL, ACTIVE_STATUS));
        userRepository.save(new User(USER_NAME_PETER, LocalDate.now(), USER_EMAIL2, ACTIVE_STATUS));
        userRepository.save(new User("SAMPLE", LocalDate.now(), USER_EMAIL3, INACTIVE_STATUS));
        userRepository.save(new User("SAMPLE1", LocalDate.now(), USER_EMAIL4, INACTIVE_STATUS));
        userRepository.save(new User("SAMPLE2", LocalDate.now(), USER_EMAIL5, INACTIVE_STATUS));
        userRepository.save(new User("SAMPLE3", LocalDate.now(), USER_EMAIL6, INACTIVE_STATUS));

        Page<User> usersPage = userRepository.findAllUsersWithPagination(new PageRequest(1, 3));

        assertThat(usersPage.getContent()
            .get(0)
            .getName()).isEqualTo("SAMPLE1");
    }

    @Test
    public void givenUsersInDBWhenFindAllWithPageRequestQueryAnnotationNativeThenReturnPageOfUsers() {
        userRepository.save(new User(USER_NAME_ADAM, LocalDate.now(), USER_EMAIL, ACTIVE_STATUS));
        userRepository.save(new User(USER_NAME_PETER, LocalDate.now(), USER_EMAIL2, ACTIVE_STATUS));
        userRepository.save(new User("SAMPLE", LocalDate.now(), USER_EMAIL3, INACTIVE_STATUS));
        userRepository.save(new User("SAMPLE1", LocalDate.now(), USER_EMAIL4, INACTIVE_STATUS));
        userRepository.save(new User("SAMPLE2", LocalDate.now(), USER_EMAIL5, INACTIVE_STATUS));
        userRepository.save(new User("SAMPLE3", LocalDate.now(), USER_EMAIL6, INACTIVE_STATUS));

        Page<User> usersSortByNameLength = userRepository.findAllUsersWithPaginationNative(new PageRequest(1, 3));

        assertThat(usersSortByNameLength.getContent()
            .get(0)
            .getName()).isEqualTo("SAMPLE1");
    }

    @Test
    @Transactional
    public void givenUsersInDBWhenUpdateStatusForNameModifyingQueryAnnotationJPQLThenModifyMatchingUsers() {
        userRepository.save(new User("SAMPLE", LocalDate.now(), USER_EMAIL, ACTIVE_STATUS));
        userRepository.save(new User("SAMPLE1", LocalDate.now(), USER_EMAIL2, ACTIVE_STATUS));
        userRepository.save(new User("SAMPLE", LocalDate.now(), USER_EMAIL3, ACTIVE_STATUS));
        userRepository.save(new User("SAMPLE3", LocalDate.now(), USER_EMAIL4, ACTIVE_STATUS));

        int updatedUsersSize = userRepository.updateUserSetStatusForName(INACTIVE_STATUS, "SAMPLE");

        assertThat(updatedUsersSize).isEqualTo(2);
    }

    @Test
    @Transactional
    public void givenUsersInDBWhenUpdateStatusForNameModifyingQueryAnnotationNativeThenModifyMatchingUsers() {
        userRepository.save(new User("SAMPLE", LocalDate.now(), USER_EMAIL, ACTIVE_STATUS));
        userRepository.save(new User("SAMPLE1", LocalDate.now(), USER_EMAIL2, ACTIVE_STATUS));
        userRepository.save(new User("SAMPLE", LocalDate.now(), USER_EMAIL3, ACTIVE_STATUS));
        userRepository.save(new User("SAMPLE3", LocalDate.now(), USER_EMAIL4, ACTIVE_STATUS));
        userRepository.flush();

        int updatedUsersSize = userRepository.updateUserSetStatusForNameNative(INACTIVE_STATUS, "SAMPLE");

        assertThat(updatedUsersSize).isEqualTo(2);
    }

    @Test
    public void givenUsersInDBWhenFindByEmailsWithDynamicQueryThenReturnCollection() {

        User user1 = new User();
        user1.setEmail(USER_EMAIL);
        userRepository.save(user1);

        User user2 = new User();
        user2.setEmail(USER_EMAIL2);
        userRepository.save(user2);

        User user3 = new User();
        user3.setEmail(USER_EMAIL3);
        userRepository.save(user3);

        Set<String> emails = new HashSet<>();
        emails.add(USER_EMAIL2);
        emails.add(USER_EMAIL3);

        Collection<User> usersWithEmails = userRepository.findUserByEmails(emails);

        assertThat(usersWithEmails.size()).isEqualTo(2);
    }

    @Test
    @Transactional
    public void givenTwoUsers_whenFindByNameUsr01_ThenUserUsr01() {
        User usr01 = new User("usr01", LocalDate.now(), "usr01@baeldung.com", 1);
        User usr02 = new User("usr02", LocalDate.now(), "usr02@baeldung.com", 1);

        userRepository.save(usr01);
        userRepository.save(usr02);

        try (Stream<User> users = userRepository.findAllByName("usr01")) {
            assertTrue(users.allMatch(usr -> usr.equals(usr01)));
        }
    }

    @Test
    @Transactional
    public void givenTwoUsers_whenFindByNameUsr00_ThenNoUsers() {
        User usr01 = new User("usr01", LocalDate.now(), "usr01@baeldung.com", 1);
        User usr02 = new User("usr02", LocalDate.now(), "usr02@baeldung.com", 1);

        userRepository.save(usr01);
        userRepository.save(usr02);

        try (Stream<User> users = userRepository.findAllByName("usr00")) {
            assertEquals(0, users.count());
        }
    }

    @Test
    public void givenTwoUsers_whenFindUsersWithGmailAddress_ThenUserUsr02() {
        User usr01 = new User("usr01", LocalDate.now(), "usr01@baeldung.com", 1);
        User usr02 = new User("usr02", LocalDate.now(), "usr02@gmail.com", 1);

        userRepository.save(usr01);
        userRepository.save(usr02);

        List<User> users = userRepository.findUsersWithGmailAddress();
        assertEquals(1, users.size());
        assertEquals(usr02, users.get(0));
    }

    @Test
    @Transactional
    public void givenTwoUsers_whenDeleteAllByCreationDateAfter_ThenOneUserRemains() {
        User usr01 = new User("usr01", LocalDate.of(2018, 1, 1), "usr01@baeldung.com", 1);
        User usr02 = new User("usr02", LocalDate.of(2018, 6, 1), "usr02@baeldung.com", 1);

        userRepository.save(usr01);
        userRepository.save(usr02);

        userRepository.deleteAllByCreationDateAfter(LocalDate.of(2018, 5, 1));

        List<User> users = userRepository.findAll();

        assertEquals(1, users.size());
        assertEquals(usr01, users.get(0));
    }

    @Test
    public void givenTwoUsers_whenFindAllUsersByPredicates_ThenUserUsr01() {
        User usr01 = new User("usr01", LocalDate.of(2018, 1, 1), "usr01@baeldung.com", 1);
        User usr02 = new User("usr02", LocalDate.of(2018, 6, 1), "usr02@baeldung.org", 1);

        userRepository.save(usr01);
        userRepository.save(usr02);

        List<Predicate<User>> predicates = new ArrayList<>();
        predicates.add(usr -> usr.getCreationDate().isAfter(LocalDate.of(2017, 12, 31)));
        predicates.add(usr -> usr.getEmail().endsWith(".com"));

        List<User> users = userRepository.findAllUsersByPredicates(predicates);

        assertEquals(1, users.size());
        assertEquals(usr01, users.get(0));
    }

    @Test
    @Transactional
    public void givenTwoUsers_whenDeactivateUsersNotLoggedInSince_ThenUserUsr02Deactivated() {
        User usr01 = new User("usr01", LocalDate.of(2018, 1, 1), "usr01@baeldung.com", 1);
        usr01.setLastLoginDate(LocalDate.now());
        User usr02 = new User("usr02", LocalDate.of(2018, 6, 1), "usr02@baeldung.org", 1);
        usr02.setLastLoginDate(LocalDate.of(2018, 7, 20));

        userRepository.save(usr01);
        userRepository.save(usr02);

        userRepository.deactivateUsersNotLoggedInSince(LocalDate.of(2018, 8, 1));

        List<User> users = userRepository.findAllUsers(Sort.by(Sort.Order.asc("name")));
        assertTrue(users.get(0).isActive());
        assertFalse(users.get(1).isActive());
    }

    @Test
    @Transactional
    public void givenTwoUsers_whenDeleteDeactivatedUsers_ThenUserUsr02Deleted() {
        User usr01 = new User("usr01", LocalDate.of(2018, 1, 1), "usr01@baeldung.com", 1);
        usr01.setLastLoginDate(LocalDate.now());
        User usr02 = new User("usr02", LocalDate.of(2018, 6, 1), "usr02@baeldung.org", 1);
        usr02.setLastLoginDate(LocalDate.of(2018, 7, 20));
        usr02.setActive(false);

        userRepository.save(usr01);
        userRepository.save(usr02);

        int deletedUsersCount = userRepository.deleteDeactivatedUsers();

        List<User> users = userRepository.findAll();
        assertEquals(1, users.size());
        assertEquals(usr01, users.get(0));
        assertEquals(1, deletedUsersCount);
    }

    @Test
    @Transactional
    public void givenTwoUsers_whenAddDeletedColumn_ThenUsersHaveDeletedColumn() {
        User usr01 = new User("usr01", LocalDate.of(2018, 1, 1), "usr01@baeldung.com", 1);
        usr01.setLastLoginDate(LocalDate.now());
        User usr02 = new User("usr02", LocalDate.of(2018, 6, 1), "usr02@baeldung.org", 1);
        usr02.setLastLoginDate(LocalDate.of(2018, 7, 20));
        usr02.setActive(false);

        userRepository.save(usr01);
        userRepository.save(usr02);

        userRepository.addDeletedColumn();

        Query nativeQuery = entityManager.createNativeQuery("select deleted from USERS.USERS where NAME = 'usr01'");
        assertEquals(0, nativeQuery.getResultList().get(0));
    }

    @After
    public void cleanUp() {
        userRepository.deleteAll();
    }

}
