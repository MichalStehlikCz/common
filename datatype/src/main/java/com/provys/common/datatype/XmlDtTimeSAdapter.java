package com.provys.common.datatype;

import javax.xml.bind.annotation.adapters.XmlAdapter;

@SuppressWarnings("CyclicClassDependency") // Cyclic dependency of adapters is expected
public class XmlDtTimeSAdapter extends XmlAdapter<String, DtTimeS> {

    @Override
    public DtTimeS unmarshal(String adapted) {
        return DtTimeS.parseIso(adapted);
    }

    @Override
    public String marshal(DtTimeS original) {
        return original.toIso();
    }
}
