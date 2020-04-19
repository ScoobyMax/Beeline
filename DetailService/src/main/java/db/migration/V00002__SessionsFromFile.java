package db.migration;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.flywaydb.core.internal.jdbc.JdbcTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;

public class V00002__SessionsFromFile extends BaseJavaMigration {

    private static final Logger log = LoggerFactory.getLogger(V00002__SessionsFromFile.class);

    @Override
    public void migrate(Context context) {
        JdbcTemplate jdbcTemplate =
                new JdbcTemplate(context.getConnection());

        ClassPathResource resource = new ClassPathResource("sessions.csv");

        try (InputStream inputStream = resource.getInputStream();
             InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
             BufferedReader reader = new BufferedReader(inputStreamReader)) {
            reader.lines().forEach(str -> {
                String[] strArr = str.split(",");
                if (!strArr[0].equals("cell_id")) {
                    try {
                        int count = jdbcTemplate.queryForInt("SELECT count(1) FROM abonentid WHERE ctn = ? ", strArr[1]);
                        if (count == 0) {
                            jdbcTemplate.update("INSERT INTO abonentid (ctn) VALUES (?)", strArr[1]);
                        }
                        jdbcTemplate.update("INSERT INTO sessions (cell_id, ctn) VALUES (?, ?)", strArr[0], strArr[1]);
                    } catch (SQLException e) {
                        log.error("SQLException: ", e);
                    }
                }
            });
        } catch (IOException ex) {
            log.error("IOException: ", ex);
        }
    }
}
