package com.provys.common.datatype;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

public class DtDateEnvelope {
    @XmlRootElement(name = "DtDateElement")
    static class DtDateJaxb {
        private DtDate value;

        /**
         * @return value of field value
         */
        @XmlElement
        public DtDate getValue() {
            return value;
        }

        /**
         * Set value of field value
         *
         * @param value is new value to be set
         * @return self to enable chaining
         */
        public DtDateJaxb setValue(DtDate value) {
            this.value = value;
            return this;
        }
    }

}
