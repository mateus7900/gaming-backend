package com.gaming.utils.converters;

import org.neo4j.ogm.typeconversion.AttributeConverter;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Converters {

    public static class TimestampStringConverter implements AttributeConverter<Timestamp, String> {

        private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

        @Override
        public String toGraphProperty(Timestamp value) {
            if (value == null) {
                return null;
            }
            return dateFormat.format(value);
        }

        @Override
        public Timestamp toEntityAttribute(String value) {
            if (value == null) {
                return null;
            }
            try {
                Date parsedDate = dateFormat.parse(value);
                return new Timestamp(parsedDate.getTime());
            } catch (ParseException e) {
                throw new IllegalArgumentException("Failed to parse Timestamp from string: " + value, e);
            }
        }
    }
}
