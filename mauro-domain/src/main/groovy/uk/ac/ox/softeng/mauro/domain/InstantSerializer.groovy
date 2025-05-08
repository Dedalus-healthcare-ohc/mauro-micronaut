package uk.ac.ox.softeng.mauro.domain

import com.fasterxml.jackson.databind.util.StdConverter
import groovy.transform.CompileStatic

import java.time.Instant

@CompileStatic
class InstantSerializer extends StdConverter<Instant,String> {

    @Override
    String convert(Instant instant) {
        String value = instant.toString()
        println(" value of date conversion: $value")
        value
    }
}
