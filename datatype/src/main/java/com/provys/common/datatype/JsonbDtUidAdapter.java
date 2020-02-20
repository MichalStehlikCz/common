package com.provys.common.datatype;

import javax.json.bind.adapter.JsonbAdapter;
import java.math.BigInteger;

/**
 * Adapter for DtUid JSON-B serialization / deserialization via BigInteger
 */
@SuppressWarnings("CyclicClassDependency") // Cyclic dependency of adapters is expected
public class JsonbDtUidAdapter implements JsonbAdapter<DtUid, BigInteger> {

  @Override
  public BigInteger adaptToJson(DtUid value) {
    return value.getValue();
  }

  @Override
  public DtUid adaptFromJson(BigInteger adapted) {
    return DtUid.valueOf(adapted);
  }
}
