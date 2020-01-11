package com.provys.common.jakarta;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.provys.common.jackson.JacksonMappers;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

@Provider
public class XmlMapperContextResolver implements ContextResolver<XmlMapper> {

    @Override
    public XmlMapper getContext(Class<?> type) {
        return JacksonMappers.getXmlMapper();
    }

}
