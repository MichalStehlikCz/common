package com.provys.common.datatype;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.math.BigInteger;

/**
 * JAXB adapter for serialization / deserialization of DtUid via BigInteger
 */
@SuppressWarnings("WeakerAccess")
public class XmlDtUidAdapter extends XmlAdapter<BigInteger, DtUid> {
    @Override
    public DtUid unmarshal(BigInteger value) {
        return DtUid.of(value);
    }

    @Override
    public BigInteger marshal(DtUid value) {
        return value.getValue();
    }
}
