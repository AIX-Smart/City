package com.aix.city.comm;


import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;

import java.io.IOException;
import java.util.Observable;

/**
 * Singleton wrapper class which configures the Jackson JSON parser.
 */
public final class Mapper
{
    private static ObjectMapper MAPPER;

    public static ObjectMapper get()
    {
        if (MAPPER == null)
        {
            MAPPER = new ObjectMapper();

            MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            // ignore observers in serialization
            MAPPER.setAnnotationIntrospector(new JacksonAnnotationIntrospector(){
                @Override
                public boolean hasIgnoreMarker(final AnnotatedMember m) {
                    return m.getDeclaringClass() == Observable.class || super.hasIgnoreMarker(m);
                }
            });
        }

        return MAPPER;
    }

    public static String string(Object data)
    {
        try
        {
            if (data instanceof String){
                return (String) data;
            }
            else{
                return get().writeValueAsString(data);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T objectOrThrow(String data, Class<T> type) throws JsonParseException, JsonMappingException, IOException
    {
        return get().readValue(data, type);
    }

    public static <T> T object(String data, Class<T> type)
    {
        try
        {
            if (type.equals(String.class)){
                return (T) data;
            }
            else{
                return objectOrThrow(data, type);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
