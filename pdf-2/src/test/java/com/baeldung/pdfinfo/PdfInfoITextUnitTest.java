package com.baeldung.pdfinfo;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;

public class PdfInfoITextUnitTest {

    private static final String PDF_FILE = "src/test/resources/input.pdf";

    @Test
    public void givenPdf_whenGetNumberOfPage_thenOK() throws IOException {
        // given
        int expectedNumberOfPage = 4;

        // when
        int actualNumberOfPage = PdfInfoIText.getNumberOfPage(PDF_FILE);

        // then
        Assert.assertEquals(expectedNumberOfPage, actualNumberOfPage);
    }

    @Test
    public void givenPdf_whenIsPasswordRequired_thenOK() throws IOException {
        // given
        boolean expectedPasswordRequired = false;

        // when
        boolean actualPasswordRequired = PdfInfoIText.isPasswordRequired(PDF_FILE);

        // then
        Assert.assertEquals(expectedPasswordRequired, actualPasswordRequired);
    }

    @Test
    public void givenPdf_whenGetInfo_thenOK() throws IOException {
        // given
        String expectedProducer = "LibreOffice 4.2";
        String expectedCreator = "Writer";

        // when
        HashMap<String, String> info = PdfInfoIText.getInfo(PDF_FILE);

        // then
        Assert.assertEquals(expectedProducer, info.get("Producer"));
        Assert.assertEquals(expectedCreator, info.get("Creator"));
    }
}
