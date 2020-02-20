package com.provys.common.datatype;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.math.BigInteger;

/**
 * JAXB adapter for serialization / deserialization of DtUid via BigInteger
 */
@SuppressWarnings("CyclicClassDependency") // Cyclic dependency of adapters is expected
public class XmlDtUidAdapter extends XmlAdapter<BigInteger, DtUid> {
    @Override
    public DtUid unmarshal(BigInteger value) {
        return DtUid.valueOf(value);
    }

    @Override
    public BigInteger marshal(DtUid value) {
        return value.getValue();
    }
}
