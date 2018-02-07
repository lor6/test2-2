package com.baeldung.passwordstorage;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class Rot13PasswordEncoderTest {

    private final Rot13PasswordEncoder encoder = new Rot13PasswordEncoder();

    @Test
    public void givenEncodedPassword_whenEncodeIsCalledWithTheEncodedPassword_thenTheClearTextPassword() {
        String password = "baeldung";
        String encoded = encoder.encode(password);
        String actualResult = encoder.encode(encoded);

        assertThat(actualResult, is(password));
    }

    @Test
    public void givenCorrectPassword_whenMatchesIsCalled_thenReturnTrue() {
        String password = "baeldung";
        String encoded = encoder.encode(password);
        boolean actualResult = encoder.matches(password, encoded);

        assertThat(actualResult, is(true));
    }

    @Test
    public void givenIncorrectPassword_whenMatchesIsCalled_thenReturnFalse() {
        boolean actualResult = encoder.matches("baeldung", "spring");

        assertThat(actualResult, is(false));
    }
}