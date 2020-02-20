package com.provys.common.spring;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.provys.common.jackson.JacksonMappers;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.*;

class WsErrorTest {

  private static final WsError SAMPLE1_VALUE;

  static {
    SAMPLE1_VALUE = new WsError(-1, null, null, null);
  }

  private static final String SAMPLE1_JSON = "{\"STATUS\":-1}";
  private static final String SAMPLE1_XML =
      "<?xml version=\"1.0\" encoding=\"UTF-8\"?><ERRORSTATUS>" +
          "<STATUS>-1</STATUS><ERROR_NM/><ERRORMESSAGE/><ERRORSTACK/></ERRORSTATUS>";
  private static final WsError SAMPLE2_VALUE;

  static {
    SAMPLE2_VALUE = new WsError(-1, "TEST_ERROR", "Test error instance",
        "Stack\nLine2\nLine3");
  }

  private static final String SAMPLE2_JSON = "{\"STATUS\":-1,\"ERROR_NM\":\"TEST_ERROR\"," +
      "\"ERRORMESSAGE\":\"Test error instance\",\"ERRORSTACK\":\"Stack\\nLine2\\nLine3\"}";
  private static final String SAMPLE2_XML =
      "<?xml version=\"1.0\" encoding=\"UTF-8\"?><ERRORSTATUS>" +
          "<STATUS>-1</STATUS><ERROR_NM>TEST_ERROR</ERROR_NM><ERRORMESSAGE>Test error instance</ERRORMESSAGE>"
          +
          "<ERRORSTACK>Stack\n" +
          "Line2\n" +
          "Line3</ERRORSTACK></ERRORSTATUS>";

  @Test
  void toJsonTest() throws JsonProcessingException {
    assertThat(JacksonMappers.getJsonMapper().writeValueAsString(SAMPLE1_VALUE))
        .isEqualTo(SAMPLE1_JSON);
    assertThat(JacksonMappers.getJsonMapper().writeValueAsString(SAMPLE2_VALUE))
        .isEqualTo(SAMPLE2_JSON);
  }

  @Test
  void fromJsonTest() throws IOException {
    assertThat(JacksonMappers.getJsonMapper().readValue(SAMPLE1_JSON, WsError.class))
        .isEqualTo(SAMPLE1_VALUE);
    assertThat(JacksonMappers.getJsonMapper().readValue(SAMPLE2_JSON, WsError.class))
        .isEqualTo(SAMPLE2_VALUE);
  }

  @Test
  void toXmlTest() throws JsonProcessingException {
    assertThat(JacksonMappers.getXmlMapper().writeValueAsString(SAMPLE1_VALUE))
        .isEqualTo(SAMPLE1_XML);
    assertThat(JacksonMappers.getXmlMapper().writeValueAsString(SAMPLE2_VALUE))
        .isEqualTo(SAMPLE2_XML);
  }

  @Test
  void fromXmlTest() throws IOException {
    assertThat(JacksonMappers.getXmlMapper().readValue(SAMPLE1_XML, WsError.class))
        .isEqualTo(SAMPLE1_VALUE);
    assertThat(JacksonMappers.getXmlMapper().readValue(SAMPLE2_XML, WsError.class))
        .isEqualTo(SAMPLE2_VALUE);
  }
}