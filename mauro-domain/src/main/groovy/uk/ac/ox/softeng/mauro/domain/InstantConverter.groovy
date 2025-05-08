package uk.ac.ox.softeng.mauro.domain

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import groovy.transform.AnnotationCollector

import java.lang.annotation.ElementType
import java.lang.annotation.Target

@AnnotationCollector()
@Target([ElementType.TYPE])
@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
@JsonDeserialize(converter = InstantDeserializer)
@JsonSerialize(converter = InstantSerializer)
@interface InstantConverter {}