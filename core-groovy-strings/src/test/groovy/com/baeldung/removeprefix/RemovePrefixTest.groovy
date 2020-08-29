package com.baeldung.removeprefix

import org.junit.Assert
import org.junit.Test

class RemovePrefixTest {


    @Test
    public void givenWhenCasePrefixIsRemoved_thenReturnTrue() {
        def trimPrefix = {
            it.startsWith('Groovy') ? it.minus('Groovy') : it
        }

        Assert.assertEquals(trimPrefix("GroovyTutorials at Baeldung"), "Tutorials at Baeldung")
    }

    @Test
    public void givenWhenPrefixIsRemoved_thenReturnTrue() {

        String prefix = "groovy-"
        String trimPrefix = "Groovy-Tutorials at Baeldung"
        def result;
        if(trimPrefix.startsWithIgnoreCase(prefix)) {
            result = trimPrefix.substring(prefix.length())
        }

        Assert.assertEquals("Tutorials at Baeldung", result)
    }

    @Test
    public void givenWhenPrefixIsRemovedUsingRegex_thenReturnTrue() {

        def regex = ~"^([Gg])roovy-"
        String trimPrefix = "Groovy-Tutorials at Baeldung"
        String result = trimPrefix - regex

        Assert.assertEquals("Tutorials at Baeldung", result)
    }

    @Test public void givenWhenPrefixIsRemovedUsingReplaceFirst_thenReturnTrue() {
        def regex = ~"^groovy" 
        String trimPrefix = "groovyTutorials at Baeldung's groovy page" 
        String result = trimPrefix.replaceFirst(regex, "") 
        
        Assert.assertEquals("Tutorials at Baeldung's groovy page", result)
    }

    @Test
    public void givenWhenPrefixIsRemovedUsingReplaceAll_thenReturnTrue() {

        String trimPrefix = "groovyTutorials at Baeldung groovy"
        String result = trimPrefix.replaceAll(/^groovy/, "")

        Assert.assertEquals("Tutorials at Baeldung groovy", result)
    }

}
