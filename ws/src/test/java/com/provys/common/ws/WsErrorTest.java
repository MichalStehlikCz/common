package com.provys.common.ws;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.provys.common.jackson.JacksonMappers;
import org.assertj.core.api.Fail;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class WsErrorTest {
    private static final WsError SAMPLE1_VALUE;
    static {
        SAMPLE1_VALUE = new WsError(-1, null, null, null);
    }
    private static final String SAMPLE1_JSON = "{\"STATUS\":-1}";
    private static final String SAMPLE1_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><ERRORSTATUS>" +
            "<STATUS>-1</STATUS><ERROR_NM/><ERRORMESSAGE/><ERRORSTACK/></ERRORSTATUS>";
    private static final String SAMPLE1_JAXB = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
            "<ERRORSTATUS><STATUS>-1</STATUS></ERRORSTATUS>";
    private static final WsError SAMPLE2_VALUE;
    static {
        SAMPLE2_VALUE = new WsError(-1, "TEST_ERROR", "Test error instance",
                "Stack\nLine2\nLine3");
    }
    private static final String SAMPLE2_JSON = "{\"STATUS\":-1,\"ERROR_NM\":\"TEST_ERROR\"," +
            "\"ERRORMESSAGE\":\"Test error instance\",\"ERRORSTACK\":\"Stack\\nLine2\\nLine3\"}";
    private static final String SAMPLE2_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><ERRORSTATUS>" +
            "<STATUS>-1</STATUS><ERROR_NM>TEST_ERROR</ERROR_NM><ERRORMESSAGE>Test error instance</ERRORMESSAGE>" +
            "<ERRORSTACK>Stack\n" +
            "Line2\n" +
            "Line3</ERRORSTACK></ERRORSTATUS>";
    private static final String SAMPLE2_JAXB = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
            "<ERRORSTATUS><STATUS>-1</STATUS><ERROR_NM>TEST_ERROR</ERROR_NM>" +
            "<ERRORMESSAGE>Test error instance</ERRORMESSAGE><ERRORSTACK>Stack\n" +
            "Line2\n" +
            "Line3</ERRORSTACK></ERRORSTATUS>";

    @Test
    void toJsonTest() throws JsonProcessingException {
        assertThat(JacksonMappers.getJsonMapper().writeValueAsString(SAMPLE1_VALUE)).isEqualTo(SAMPLE1_JSON);
        assertThat(JacksonMappers.getJsonMapper().writeValueAsString(SAMPLE2_VALUE)).isEqualTo(SAMPLE2_JSON);
    }

    @Test
    void fromJsonTest() throws JsonProcessingException {
        assertThat(JacksonMappers.getJsonMapper().readValue(SAMPLE1_JSON, WsError.class)).isEqualTo(SAMPLE1_VALUE);
        assertThat(JacksonMappers.getJsonMapper().readValue(SAMPLE2_JSON, WsError.class)).isEqualTo(SAMPLE2_VALUE);
    }

    @Test
    void toXmlTest() throws JsonProcessingException {
        assertThat(JacksonMappers.getXmlMapper().writeValueAsString(SAMPLE1_VALUE)).isEqualTo(SAMPLE1_XML);
        assertThat(JacksonMappers.getXmlMapper().writeValueAsString(SAMPLE2_VALUE)).isEqualTo(SAMPLE2_XML);
    }

    @Test
    void fromXmlTest() throws JsonProcessingException {
        assertThat(JacksonMappers.getXmlMapper().readValue(SAMPLE1_XML, WsError.class)).isEqualTo(SAMPLE1_VALUE);
        assertThat(JacksonMappers.getXmlMapper().readValue(SAMPLE2_XML, WsError.class)).isEqualTo(SAMPLE2_VALUE);
    }

    @SuppressWarnings("squid:S00112") // we do not care that generic Exception is thrown in test
    @Test
    void toJsonbTest() throws Exception {
        try (Jsonb jsonb = JsonbBuilder.create()) {
            assertThat(jsonb.toJson(SAMPLE1_VALUE)).isEqualTo(SAMPLE1_JSON);
            assertThat(jsonb.toJson(SAMPLE2_VALUE)).isEqualTo(SAMPLE2_JSON);
        }
    }

    @SuppressWarnings("squid:S00112") // we do not care that generic Exception is thrown in test
    @Test
    void fromJsonbTest() throws Exception {
        try (Jsonb jsonb = JsonbBuilder.create()) {
//            assertThat(jsonb.fromJson(SAMPLE1_JSON, WsError.class)).isEqualTo(SAMPLE1_VALUE); does not work as
//            parameters cannot be missing... we will wait for JSON-B 1.1 as it seems it will be somehow solved there
            assertThat(jsonb.fromJson(SAMPLE2_JSON, WsError.class)).isEqualTo(SAMPLE2_VALUE);
        }
    }

    @Test
    void jaxbMarshalTest() throws JAXBException {
        var context = JAXBContext.newInstance(WsError.class);
        Marshaller m = context.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);
        var resultWriter = new StringWriter();
        m.marshal(SAMPLE1_VALUE, resultWriter);
        assertThat(resultWriter.toString()).isEqualTo(SAMPLE1_JAXB);
        resultWriter = new StringWriter();
        m.marshal(SAMPLE2_VALUE, resultWriter);
        assertThat(resultWriter.toString()).isEqualTo(SAMPLE2_JAXB);
    }

    @Test
    void jaxbUnmarshalTest() throws JAXBException {
        var context = JAXBContext.newInstance(WsError.class);
        Unmarshaller u = context.createUnmarshaller();
        var reader = new StringReader(SAMPLE1_JAXB);
        assertThat((WsError) u.unmarshal(reader)).isEqualTo(SAMPLE1_VALUE);
        reader = new StringReader(SAMPLE2_JAXB);
        assertThat((WsError) u.unmarshal(reader)).isEqualTo(SAMPLE2_VALUE);
    }
}