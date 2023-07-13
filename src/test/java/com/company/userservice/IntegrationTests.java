package com.company.userservice;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.Instant;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
class IntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void authFlowTest() throws Exception {
        mockMvc.perform(post("/admin/users").contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"login\": \"login-4132\",\n" +
                                "    \"password\": \"password-4\"\n" +
                                "}"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andDo(print());

        MvcResult result = mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"login\": \"login-4132\",\n" +
                                "    \"password\": \"password-4\"\n" +
                                "}"))
                .andExpect(status().isOk())
                .andDo(print()).andReturn();

        String token = result.getResponse().getContentAsString();
        String expectedTokenStart = "login-4132:password-4:";

        Assertions.assertThat(token).startsWith(expectedTokenStart);
        Assertions.assertThat(token.split(":")[2]).matches("^-?\\d{1,19}$");

        mockMvc.perform(get("/auth/token?token=login-4132:password-4:" + Instant.now().toEpochMilli()))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
