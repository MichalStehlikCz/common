package com.provys.common.datatype;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class XmlDtDateAdapter extends XmlAdapter<String, DtDate> {

    @Override
    public DtDate unmarshal(String adapted) {
        return DtDate.parseIso(adapted);
    }

    @Override
    public String marshal(DtDate original) {
        return original.toIso();
    }
}
