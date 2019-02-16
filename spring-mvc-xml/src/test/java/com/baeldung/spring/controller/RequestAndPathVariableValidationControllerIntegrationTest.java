package com.baeldung.spring.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.baeldung.spring.ClientWebConfig;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ClientWebConfig.class)
@WebAppConfiguration
public class RequestAndPathVariableValidationControllerIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void getNameOfDayByNumberRequestParam_whenGetWithProperRequestParam_thenReturn200() throws Exception {
        mockMvc.perform(get("/public/api/1/name-for-day").param("dayOfWeek", Integer.toString(5))).andExpect(status().isOk());
    }

    @Test
    public void getNameOfDayByNumberRequestParam_whenGetWithInvalidRequestParam_thenReturn400() throws Exception {
        mockMvc.perform(get("/public/api/1/name-for-day").param("dayOfWeek", Integer.toString(5))).andExpect(status().isBadRequest());
    }
}
