package com.provys.common.datatype;

import java.math.BigInteger;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * JAXB adapter for serialization / deserialization of DtUid via BigInteger.
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
