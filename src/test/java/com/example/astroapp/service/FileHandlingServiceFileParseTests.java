package com.example.astroapp;


import com.example.astroapp.dao.FluxDao;
import com.example.astroapp.dao.PhotoPropertiesDao;
import com.example.astroapp.dao.SpaceObjectDao;
import com.example.astroapp.entities.PhotoProperties;
import com.example.astroapp.entities.User;
import com.example.astroapp.exceptions.CsvRowDataParseException;
import com.example.astroapp.services.FileHandlingService;
import com.example.astroapp.services.UserService;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql({"/schema.sql", "/sql_test_data/test-data-user-only.sql"})
@AutoConfigureEmbeddedDatabase(provider = ZONKY,
        refresh = AutoConfigureEmbeddedDatabase.RefreshMode.AFTER_EACH_TEST_METHOD)
public class FileHandlingServiceFileParseTests {

    @Autowired
    SpaceObjectDao spaceObjectDao;

    @Autowired
    FluxDao fluxDao;

    @Autowired
    PhotoPropertiesDao photoPropertiesDao;

    @Autowired
    FileHandlingService fileHandlingService;

    @MockBean
    UserService userService;

    private User sampleUser = new User("1");


}