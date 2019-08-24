package com.provys.common.datatype;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class XmlDtDateAdapter extends XmlAdapter<DtDate, String> {
    @Override
    public String unmarshal(DtDate original) throws Exception {
        return original.toIso();
    }

    @Override
    public DtDate marshal(String adapted) throws Exception {
        return DtDate.parseIso(adapted);
    }
}
