package com.provys.common.datatype;

import org.junit.jupiter.api.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.StringReader;
import java.io.StringWriter;

import static org.assertj.core.api.Assertions.assertThat;

class XmlDtTimeSAdapterTest {

    @XmlRootElement(name = "DtTimeSElement")
    static class DtTimeSElement {
        private DtTimeS value;

        /**
         * @return value of field value
         */
        @XmlElement
        DtTimeS getValue() {
            return value;
        }

        /**
         * Set value of field value
         *
         * @param value is new value to be set
         */
        DtTimeSElement setValue(DtTimeS value) {
            this.value = value;
            return this;
        }
    }

    @Test
    void marshalTest() {
        try {
            var context = JAXBContext.newInstance(DtTimeSElement.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);
            var value = new DtTimeSElement().setValue(DtTimeS.ofHourToSecond(15, 5, 48));
            var resultWriter = new StringWriter();
            m.marshal(value, resultWriter);
            assertThat(resultWriter.toString())
                    .isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
                            "<DtTimeSElement><value>15:05:48</value></DtTimeSElement>");
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    @Test
    void unmarshalTest() {
        try {
            var context = JAXBContext.newInstance(DtTimeSElement.class);
            Unmarshaller u = context.createUnmarshaller();
            var reader = new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
                    "<DtDateElement><value>05:13:55</value></DtDateElement>");
            DtTimeSElement result = (DtTimeSElement) u.unmarshal(reader);
            assertThat(result.getValue()).isEqualTo(DtTimeS.ofHourToSecond(5, 13, 55));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
}
