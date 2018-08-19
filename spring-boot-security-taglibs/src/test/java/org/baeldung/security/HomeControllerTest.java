package org.baeldung.security;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class HomeControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void whenUserIsAuthenticatedThenAuthenticatedSectionsShowOnSite() throws Exception {
        String body = this.restTemplate.withBasicAuth("testUser", "password")
            .getForEntity("/", String.class)
            .getBody();

        // test <sec:authorize access="isAnonymous()">
        assertFalse(body.contains("ANONYMOUS"));

        // test <sec:authorize access="isAuthenticated()">
        assertTrue(body.contains("AUTHENTICATED Content"));

        // test <sec:authorize access="hasRole('ADMIN')">
        assertTrue(body.contains("Content for users who have the \"ADMIN\" role."));

        // test <sec:authentication property="principal.username" />
        assertTrue(body.contains("testUser"));

        // test <sec:authorize url="/adminOnlyURL">
        assertTrue(body.contains("<a href=\"/adminOnlyURL\">"));
        
        // test <sec:csrfInput />
        assertTrue(body.contains("<input type=\"hidden\" name=\"_csrf\" value=\""));

        // test <sec:csrfMetaTags />
        assertTrue(body.contains("<meta name=\"_csrf_parameter\" content=\"_csrf\" />"));
    }

    @Test
    public void whenUserIsNotAuthenticatedThenOnlyAnonymousSectionsShowOnSite() throws Exception {
        String body = this.restTemplate.getForEntity("/", String.class)
            .getBody();

        // test <sec:authorize access="isAnonymous()">
        assertTrue(body.contains("ANONYMOUS Content"));

        // test <sec:authorize access="isAuthenticated()">
        assertFalse(body.contains("AUTHENTICATED Content"));
    }
}
