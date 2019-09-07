package com.provys.common.datatype;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class XmlDtTimeSAdapter extends XmlAdapter<String, DtTimeS> {

    @Override
    public DtTimeS unmarshal(String adapted) throws Exception {
        return DtTimeS.parseIso(adapted);
    }

    @Override
    public String marshal(DtTimeS original) throws Exception {
        return original.toIso();
    }
}
