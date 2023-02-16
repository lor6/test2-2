package com.baeldung;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;

import com.baeldung.ServletResourceServerApplication.GreetingController;
import com.baeldung.ServletResourceServerApplication.MessageService;

@WebMvcTest(controllers = GreetingController.class, properties = { "server.ssl.enabled=false" })
class SpringSecurityTestGreetingControllerUnitTest {

    @MockBean
    MessageService messageService;

    @Autowired
    MockMvc api;

    /*-----------------------------------------------------------------------------*/
    /* /greet                                                                      */
    /* This end-point secured with ".anyRequest().authenticated()" in SecurityConf */
    /*-----------------------------------------------------------------------------*/

    @Test
    void givenUserIsNotAuthenticated_whenGetGreet_thenUnauthorized() throws Exception {
        // @formatter:off
        api.perform(get("/greet").with(anonymous()))
            .andExpect(status().isUnauthorized());
        // @formatter:on
    }

    @Test
    void givenUserIsAuthenticated_whenGetGreet_thenOk() throws Exception {
        final var greeting = "Whatever the service returns";
        when(messageService.greet()).thenReturn(greeting);

        // @formatter:off
        api.perform(get("/greet").with(jwt()))
            .andExpect(status().isOk())
            .andExpect(content().string(greeting));
        // @formatter:on

        verify(messageService, times(1)).greet();
    }

    /*---------------------------------------------------------------------------------------------------------------------*/
    /* /secured-route                                                                                                      */
    /* This end-point is secured with ".requestMatchers("/secured-route").hasRole("AUTHORIZED_PERSONNEL")" in SecurityConf */
    /*---------------------------------------------------------------------------------------------------------------------*/

    @Test
    void givenUserIsNotAuthenticated_whenGetSecuredRoute_thenUnauthorized() throws Exception {
        // @formatter:off
        api.perform(get("/secured-route").with(anonymous()))
            .andExpect(status().isUnauthorized());
        // @formatter:on
    }

    @Test
    void givenUserIsGrantedWithRoleAuthorizedPersonnel_whenGetSecuredRoute_thenOk() throws Exception {
        final var secret = "Secret!";
        when(messageService.getSecret()).thenReturn(secret);

        // @formatter:off
        api.perform(get("/secured-route").with(jwt().authorities(new SimpleGrantedAuthority("ROLE_AUTHORIZED_PERSONNEL"))))
            .andExpect(status().isOk())
            .andExpect(content().string(secret));
        // @formatter:on
    }

    @Test
    void givenUserIsNotGrantedWithRoleAuthorizedPersonnel_whenGetSecuredRoute_thenForbidden() throws Exception {
        // @formatter:off
        api.perform(get("/secured-route").with(jwt().authorities(new SimpleGrantedAuthority("admin"))))
            .andExpect(status().isForbidden());
        // @formatter:on
    }
    
    /*---------------------------------------------------------------------------------------------------------*/
    /* /secured-method                                                                                         */
    /* This end-point is secured with "@PreAuthorize("hasRole('AUTHORIZED_PERSONNEL')")" on @Controller method */
    /*---------------------------------------------------------------------------------------------------------*/

    @Test
    void givenUserIsNotAuthenticated_whenGetSecuredMethod_thenUnauthorized() throws Exception {
        // @formatter:off
        api.perform(get("/secured-method").with(anonymous()))
            .andExpect(status().isUnauthorized());
        // @formatter:on
    }

    @Test
    void givenUserIsGrantedWithRoleAuthorizedPersonnel_whenGetSecuredMethod_thenOk() throws Exception {
        final var secret = "Secret!";
        when(messageService.getSecret()).thenReturn(secret);

        // @formatter:off
        api.perform(get("/secured-method").with(jwt().authorities(new SimpleGrantedAuthority("ROLE_AUTHORIZED_PERSONNEL"))))
            .andExpect(status().isOk())
            .andExpect(content().string(secret));
        // @formatter:on
    }

    @Test
    void givenUserIsNotGrantedWithRoleAuthorizedPersonnel_whenGetSecuredMethod_thenForbidden() throws Exception {
        // @formatter:off
        api.perform(get("/secured-method").with(jwt().authorities(new SimpleGrantedAuthority("admin"))))
            .andExpect(status().isForbidden());
        // @formatter:on
    }

}
