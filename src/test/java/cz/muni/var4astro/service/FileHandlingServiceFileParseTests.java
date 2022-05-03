package cz.muni.var4astro.service;


import cz.muni.var4astro.dao.FluxDaoImpl;
import cz.muni.var4astro.dao.PhotoPropertiesDaoImpl;
import cz.muni.var4astro.dao.SpaceObjectDaoImpl;
import cz.muni.var4astro.dto.User;
import cz.muni.var4astro.exceptions.CsvContentException;
import cz.muni.var4astro.services.FileHandlingService;
import cz.muni.var4astro.services.UserService;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;


import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql({"/sql/schema.sql", "/sql_test_data/test-data-user-only.sql"})
@AutoConfigureEmbeddedDatabase(provider = ZONKY,
        refresh = AutoConfigureEmbeddedDatabase.RefreshMode.AFTER_EACH_TEST_METHOD)
public class FileHandlingServiceFileParseTests {

    @Autowired
    SpaceObjectDaoImpl spaceObjectDao;

    @Autowired
    FluxDaoImpl fluxDao;

    @Autowired
    PhotoPropertiesDaoImpl photoPropertiesDao;

    @Autowired
    FileHandlingService fileHandlingService;

    @MockBean
    UserService userService;

    private final User sampleUser = new User("1");

    @Before
    public void mockUserService() {
        Mockito.when(userService.getCurrentUser()).thenReturn(sampleUser);
    }

    @Test
    public void correctFileNoExceptionThrown() throws IOException {
        Path filePath = Paths.get("src/test/resources/correct_files/correct_file.csv");
        fileHandlingService.parseAndPersist(filePath, sampleUser);
    }

    @Test
    public void correctFileMissingNotEssentialHeaderPartNoExceptionThrown() throws IOException {
        Path filePath = Paths.get("src/test/resources/correct_files/correct_file_shorter_header.csv");
        fileHandlingService.parseAndPersist(filePath, sampleUser);
    }

    @Test
    public void correctFileMissingNonEssentialSchemeNoExceptionThrown() throws IOException {
        Path filePath = Paths.get("src/test/resources/correct_files/correct_file_missing_scheme.csv");
        fileHandlingService.parseAndPersist(filePath, sampleUser);
    }

    @Test
    public void fewLinesIncorrectOtherCorrectNoExceptionThrownStackTracePrinted() throws IOException {
        Path filePath = Paths.get("src/test/resources/correct_files/few_lines_incorrect.csv");
        fileHandlingService.parseAndPersist(filePath, sampleUser);
    }

    @Test
    public void incorrectFileMissingExposureTime() {
        Path filePath = Paths.get("src/test/resources/incorrect_files/" +
                "incorrect_missing_initial_info.csv");
        assertThrows(CsvContentException.class, () ->
                fileHandlingService.parseAndPersist(filePath, sampleUser));
    }

    @Test
    public void incorrectFileSchemeMissing() {
        Path filePath = Paths.get("src/test/resources/incorrect_files/" +
                "incorrect_missing_initial_info.csv");
        assertThrows(CsvContentException.class, () ->
                fileHandlingService.parseAndPersist(filePath, sampleUser));
    }


}