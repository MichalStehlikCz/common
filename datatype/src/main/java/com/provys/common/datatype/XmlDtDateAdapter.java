package com.provys.common.datatype;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class XmlDtDateAdapter extends XmlAdapter<String, DtDate> {

    @Override
    public DtDate unmarshal(String adapted) throws Exception {
        return DtDate.parseIso(adapted);
    }

    @Override
    public String marshal(DtDate original) throws Exception {
        return original.toIso();
    }
}
