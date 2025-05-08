package uk.ac.ox.softeng.mauro.domain

import com.fasterxml.jackson.databind.util.StdConverter
import groovy.transform.CompileStatic

import java.time.Instant
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@CompileStatic
class InstantDeserializer extends StdConverter<String, Instant> {
    static DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

    @Override
    Instant convert(String value) {
        try {
            Instant converted =  OffsetDateTime.parse(value, formatter).toInstant()
            println(" converted value: $converted".toString())
            return converted
        } catch (DateTimeParseException ignored) {
            // if timezone is missing, assume UTC (used for deserialising JSON from Postgres)
            return OffsetDateTime.parse(value + 'Z', formatter).toInstant()
        }
    }
}
