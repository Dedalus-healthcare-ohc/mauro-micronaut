package uk.ac.ox.softeng.mauro.security.authentication

import jakarta.inject.Qualifier
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy


@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@interface Session {
}

