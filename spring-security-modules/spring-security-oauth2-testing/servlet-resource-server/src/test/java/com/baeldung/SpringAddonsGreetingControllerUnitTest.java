package com.baeldung;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;

import com.baeldung.ServletResourceServerApplication.GreetingController;
import com.baeldung.ServletResourceServerApplication.MessageService;
import com.c4_soft.springaddons.security.oauth2.test.annotations.WithMockJwtAuth;

@WebMvcTest(controllers = GreetingController.class, properties = { "server.ssl.enabled=false" })
class SpringAddonsGreetingControllerUnitTest {

    @MockBean
    MessageService messageService;

    @Autowired
    MockMvc api;

    /*-----------------------------------------------------------------------------*/
    /* /greet                                                                      */
    /* This end-point secured with ".anyRequest().authenticated()" in SecurityConf */
    /*-----------------------------------------------------------------------------*/

    @Test
    @WithAnonymousUser
    void givenUserIsNotAuthenticated_whenGetGreet_thenUnauthorized() throws Exception {
        // @formatter:off
        api.perform(get("/greet"))
            .andExpect(status().isUnauthorized());
        // @formatter:on
    }

    @Test
    @WithMockJwtAuth
    void givenUserIsAuthenticated_whenGetGreet_thenOk() throws Exception {
        final var greeting = "Whatever the service returns";
        when(messageService.greet()).thenReturn(greeting);

        // @formatter:off
        api.perform(get("/greet"))
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
    @WithAnonymousUser
    void givenUserIsNotAuthenticated_whenGetSecuredRoute_thenUnauthorized() throws Exception {
        // @formatter:off
        api.perform(get("/secured-route"))
            .andExpect(status().isUnauthorized());
        // @formatter:on
    }

    @Test
    @WithMockJwtAuth({ "admin", "ROLE_AUTHORIZED_PERSONNEL" })
    void givenUserIsGrantedWithRoleAuthorizedPersonnel_whenGetSecuredRoute_thenOk() throws Exception {
        final var secret = "Secret!";
        when(messageService.getSecret()).thenReturn(secret);

        // @formatter:off
        api.perform(get("/secured-route"))
            .andExpect(status().isOk())
            .andExpect(content().string(secret));
        // @formatter:on
    }

    @Test
    @WithMockJwtAuth({ "admin" })
    void givenUserIsNotGrantedWithRoleAuthorizedPersonnel_whenGetSecuredRoute_thenForbidden() throws Exception {
        // @formatter:off
        api.perform(get("/secured-route"))
            .andExpect(status().isForbidden());
        // @formatter:on
    }
    
    /*---------------------------------------------------------------------------------------------------------*/
    /* /secured-method                                                                                         */
    /* This end-point is secured with "@PreAuthorize("hasRole('AUTHORIZED_PERSONNEL')")" on @Controller method */
    /*---------------------------------------------------------------------------------------------------------*/

    @Test
    @WithAnonymousUser
    void givenUserIsNotAuthenticated_whenGetSecuredMethod_thenUnauthorized() throws Exception {
        // @formatter:off
        api.perform(get("/secured-method"))
            .andExpect(status().isUnauthorized());
        // @formatter:on
    }

    @Test
    @WithMockJwtAuth({ "admin", "ROLE_AUTHORIZED_PERSONNEL" })
    void givenUserIsGrantedWithRoleAuthorizedPersonnel_whenGetSecuredMethod_thenOk() throws Exception {
        final var secret = "Secret!";
        when(messageService.getSecret()).thenReturn(secret);

        // @formatter:off
        api.perform(get("/secured-method"))
            .andExpect(status().isOk())
            .andExpect(content().string(secret));
        // @formatter:on
    }

    @Test
    @WithMockJwtAuth(authorities = { "admin" })
    void givenUserIsNotGrantedWithRoleAuthorizedPersonnel_whenGetSecuredMethod_thenForbidden() throws Exception {
        // @formatter:off
        api.perform(get("/secured-method"))
            .andExpect(status().isForbidden());
        // @formatter:on
    }

}
