package com.provys.common.datatype;

import org.assertj.core.api.Fail;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class XmlDtDateAdapterTest {

    @XmlRootElement(name = "DtDateElement")
    static class DtDateElement {
        private DtDate value;

        /**
         * @return value of field value
         */
        @XmlElement
        DtDate getValue() {
            return value;
        }

        /**
         * Set value of field value
         *
         * @param value is new value to be set
         */
        DtDateElement setValue(DtDate value) {
            this.value = value;
            return this;
        }
    }

    @Test
    void marshalTest() {
        try {
            var context = JAXBContext.newInstance(DtDateElement.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);
            var value = new DtDateElement().setValue(DtDate.of(2010, 12, 1));
            var resultWriter = new StringWriter();
            m.marshal(value, resultWriter);
            assertThat(resultWriter.toString())
                    .isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
                            "<DtDateElement><value>2010-12-01</value></DtDateElement>");
        } catch (JAXBException e) {
            //noinspection ResultOfMethodCallIgnored
            Fail.fail("JAXBException thrown during test", e);
        }
    }

    @Test
    void unmarshalTest() {
        try {
            var context = JAXBContext.newInstance(DtDateElement.class);
            Unmarshaller u = context.createUnmarshaller();
            var reader = new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
                    "<DtDateElement><value>2018-05-12</value></DtDateElement>");
            DtDateElement result = (DtDateElement) u.unmarshal(reader);
            assertThat(result.getValue()).isEqualTo(DtDate.of(2018, 5, 12));
        } catch (JAXBException e) {
            //noinspection ResultOfMethodCallIgnored
            Fail.fail("JAXBException thrown during test", e);
        }
    }
}