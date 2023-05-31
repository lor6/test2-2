package com.baeldung.commons.untar;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

import com.baeldung.commons.untar.impl.TarExtractorAnt;

public class TarExtractorAntUnitTest {

    @Test
    public void givenTarFile_whenUntar_thenExtractedToDestination() throws IOException {
        Path destination = Paths.get("/tmp/ant");

        new TarExtractorAnt(Resources.TAR_FILE, false, destination).untar();

        assertTrue(Files.list(destination)
            .findFirst()
            .isPresent());
    }

    @Test
    public void givenTarGzFile_whenUntar_thenExtractedToDestination() throws IOException {
        Path destination = Paths.get("/tmp/ant-gz");

        new TarExtractorAnt(Resources.TAR_GZ_FILE, true, destination).untar();

        assertTrue(Files.list(destination)
            .findFirst()
            .isPresent());
    }
}
