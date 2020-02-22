package com.provys.common.spring;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;
import static org.springframework.web.servlet.function.RequestPredicates.contentType;

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

  private static final String THROW_URL = "/exception/throw/TEST_ERROR";
  private final MockMvc mockMvc;

  @Autowired
  ProvysRestExceptionHandlerIT(MockMvc mockMvc) {
    this.mockMvc = mockMvc;
  }

  @Test
  void throwExceptionJsonTest() throws Exception {
    mockMvc
        .perform(get(THROW_URL).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().is(500))
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(content().json("{\"STATUS\":-1,\"ERROR_NM\":\"TEST_ERROR\","
            + "\"ERRORMESSAGE\":\"TEST_ERROR: Exception thrown.\"}"));
  }

  @Test
  void throwExceptionXmlTest() throws Exception {
    mockMvc
        .perform(get(THROW_URL).accept(MediaType.APPLICATION_XML))
        .andExpect(status().is(500))
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_XML))
        .andExpect(xpath("ERRORSTATUS/STATUS").number(-1d))
        .andExpect(xpath("ERRORSTATUS/ERROR_NM").string("TEST_ERROR"))
        .andExpect(xpath("ERRORSTATUS/ERRORMESSAGE").string("TEST_ERROR: Exception thrown."));
  }

  @Test
  void throwExceptionTextTest() throws Exception {
    mockMvc
        .perform(get(THROW_URL).accept(MediaType.TEXT_PLAIN))
        .andExpect(status().is(500))
        .andExpect(content().string("TEST_ERROR: Exception thrown."));
  }
}