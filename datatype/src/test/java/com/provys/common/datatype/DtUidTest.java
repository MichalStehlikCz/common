package com.provys.common.datatype;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.provys.common.jackson.JacksonMappers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Objects;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class DtUidTest {

    static Stream<Object[]> isRegularTest() {
        return Stream.of(
                new Object[]{DtUid.valueOf("25"), true}
                , new Object[]{DtUid.valueOf("-125"), false}
                , new Object[]{DtUid.PRIV, false}
                , new Object[]{DtUid.valueOf("-2"), false}
                , new Object[]{DtUid.ME, false}
                , new Object[]{DtUid.valueOf("-1"), false}
        );
    }

    @ParameterizedTest
    @MethodSource
    void isRegularTest(DtUid value, boolean result) {
        assertThat(value.isRegular()).isEqualTo(result);
    }

    static Stream<Object[]> isMultilineTest() {
        return Stream.of(
                new Object[]{DtUid.valueOf("25"), false}
                , new Object[]{DtUid.valueOf("-125"), true}
                , new Object[]{DtUid.PRIV, false}
                , new Object[]{DtUid.valueOf("-2"), false}
                , new Object[]{DtUid.ME, false}
                , new Object[]{DtUid.valueOf("-1"), false}
        );
    }

    @ParameterizedTest
    @MethodSource
    void isMultilineTest(DtUid value, boolean result) {
        assertThat(value.isMultiline()).isEqualTo(result);
    }

    static Stream<Object[]> isPrivTest() {
        return Stream.of(
                new Object[]{DtUid.valueOf("25"), false}
                , new Object[]{DtUid.valueOf("-125"), false}
                , new Object[]{DtUid.PRIV, true}
                , new Object[]{DtUid.valueOf("-2"), true}
                , new Object[]{DtUid.ME, false}
                , new Object[]{DtUid.valueOf("-1"), false}
        );
    }

    @ParameterizedTest
    @MethodSource
    void isPrivTest(DtUid value, boolean result) {
        assertThat(value.isPriv()).isEqualTo(result);
    }

    static Stream<Object[]> isMETest() {
        return Stream.of(
                new Object[]{DtUid.valueOf("25"), false}
                , new Object[]{DtUid.valueOf("-125"), false}
                , new Object[]{DtUid.PRIV, false}
                , new Object[]{DtUid.valueOf("-2"), false}
                , new Object[]{DtUid.ME, true}
                , new Object[]{DtUid.valueOf("-1"), true}
        );
    }

    @ParameterizedTest
    @MethodSource
    void isMETest(DtUid value, boolean result) {
        assertThat(value.isME()).isEqualTo(result);
    }

    static Stream<Object[]> toStringTest() {
        return Stream.of(
                new Object[]{DtUid.valueOf("25"), "ID25"}
                , new Object[]{DtUid.valueOf("-125"), "ID-125"}
                , new Object[]{DtUid.PRIV, "ID##########"}
                , new Object[]{DtUid.ME, "ID**********"}
        );
    }

    @ParameterizedTest
    @MethodSource
    void toStringTest(DtUid value, String result) {
        assertThat(value.toString()).isEqualTo(result);
    }

    @XmlRootElement(name = "DtUidElement")
    public static class DtUidElement {
        private DtUid value1;
        private DtUid value2;
        private DtUid value3;

        /**
         * @return value of field value1
         */
        @XmlElement
        public DtUid getValue1() {
            return value1;
        }

        /**
         * Set value of field value1
         *
         * @param value1 is new value to be set
         */
        public DtUidElement setValue1(DtUid value1) {
            this.value1 = value1;
            return this;
        }

        /**
         * @return value of field value2
         */
        @XmlElement
        public DtUid getValue2() {
            return value2;
        }

        /**
         * Set value of field value2
         *
         * @param value2 is new value to be set
         */
        public DtUidElement setValue2(DtUid value2) {
            this.value2 = value2;
            return this;
        }

        /**
         * @return value of field value3
         */
        @XmlElement
        public DtUid getValue3() {
            return value3;
        }

        /**
         * Set value of field value3
         *
         * @param value3 is new value to be set
         */
        public DtUidElement setValue3(DtUid value3) {
            this.value3 = value3;
            return this;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DtUidElement that = (DtUidElement) o;
            return Objects.equals(value1, that.value1) &&
                    Objects.equals(value2, that.value2) &&
                    Objects.equals(value3, that.value3);
        }

        @Override
        public int hashCode() {
            return Objects.hash(value1, value2, value3);
        }

        @Override
        public String toString() {
            return "DtUidElement{" +
                    "value1=" + value1 +
                    ", value2=" + value2 +
                    ", value3=" + value3 +
                    '}';
        }
    }

    private static final DtUidElement SAMPLE_VALUE = new DtUidElement()
            .setValue1(DtUid.ME)
            .setValue3(DtUid.valueOf("12345678901234567890123456789"));
    private static final String SAMPLE_JSON = "{\"value1\":-1,\"value3\":\"12345678901234567890123456789\"}";
    private static final String SAMPLE_JACKSON = "{\"value1\":-1,\"value3\":12345678901234567890123456789}";
    private static final String SAMPLE_JSONB = SAMPLE_JACKSON;
    private static final String SAMPLE_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><DtUidElement><value1>-1</value1>" +
            "<value2/><value3>12345678901234567890123456789</value3></DtUidElement>";
    private static final String SAMPLE_JAXB = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><DtUidElement>" +
            "<value1>-1</value1><value3>12345678901234567890123456789</value3></DtUidElement>";

    @Test
    void serializeToJsonTest() throws JsonProcessingException {
        assertThat(JacksonMappers.getJsonMapper().writeValueAsString(SAMPLE_VALUE))
                .isEqualTo(SAMPLE_JACKSON);
    }

    @Test
    void deserializeFromJsonTest() throws IOException {
        assertThat(JacksonMappers.getJsonMapper().readValue(SAMPLE_JSON, DtUidElement.class))
                .isEqualTo(SAMPLE_VALUE);
    }

    @Test
    void serializeToXmlTest() throws JsonProcessingException {
        assertThat(JacksonMappers.getXmlMapper().writeValueAsString(SAMPLE_VALUE))
                .isEqualTo(SAMPLE_XML);
    }

    @Test
    void deserializeFromXmlTest() throws IOException {
        assertThat(JacksonMappers.getXmlMapper().readValue(SAMPLE_XML, DtUidElement.class))
                .isEqualTo(SAMPLE_VALUE);
        assertThat(JacksonMappers.getXmlMapper().readValue(SAMPLE_JAXB, DtUidElement.class))
                .isEqualTo(SAMPLE_VALUE);
    }

    @Test
    void serializeJsonbTest() {
        Jsonb jsonb = JsonbBuilder.create();
        assertThat(jsonb.toJson(SAMPLE_VALUE)).isEqualTo(SAMPLE_JSONB);
    }

    @Test
    void deserializeJsonbTest() {
        Jsonb jsonb = JsonbBuilder.create();
        assertThat(jsonb.fromJson(SAMPLE_JSON, DtUidElement.class)).isEqualTo(SAMPLE_VALUE);
    }

    @Test
    void serializeJaxbTest() throws JAXBException {
        var context = JAXBContext.newInstance(DtUidElement.class);
        Marshaller m = context.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);
        var resultWriter = new StringWriter();
        m.marshal(SAMPLE_VALUE, resultWriter);
        assertThat(resultWriter.toString())
                .isEqualTo(SAMPLE_JAXB);
    }

    @Test
    void deserializeJaxbTest() throws JAXBException {
        var context = JAXBContext.newInstance(DtUidElement.class);
        Unmarshaller u = context.createUnmarshaller();
        var reader = new StringReader(SAMPLE_XML);
        assertThat((DtUidElement) u.unmarshal(reader)).isEqualTo(SAMPLE_VALUE);
        reader = new StringReader(SAMPLE_JAXB);
        assertThat((DtUidElement) u.unmarshal(reader)).isEqualTo(SAMPLE_VALUE);
    }
}