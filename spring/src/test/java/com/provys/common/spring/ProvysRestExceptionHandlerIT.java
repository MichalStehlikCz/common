package com.provys.common.spring;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(
    webEnvironment = WebEnvironment.MOCK,
    classes = TestApplication.class)
@AutoConfigureMockMvc
class ProvysRestExceptionHandlerIT {

  private final MockMvc mockMvc;

  @Autowired
  ProvysRestExceptionHandlerIT(MockMvc mockMvc) {
    this.mockMvc = mockMvc;
  }

  @Test
  void throwExceptionJsonTest() throws Exception {
    var result = mockMvc
        .perform(get("/exception/throw/TEST_ERROR").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is(500))
        .andExpect(content().json("{\"STATUS\":-1,\"ERROR_NM\":\"TEST_ERROR\"," +
            "\"ERRORMESSAGE\":\"TEST_ERROR: Exception thrown.\"}"));
  }

}