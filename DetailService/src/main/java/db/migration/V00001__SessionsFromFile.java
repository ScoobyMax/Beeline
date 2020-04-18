package db.migration;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.flywaydb.core.internal.jdbc.JdbcTemplate;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;

public class V00001__SessionsFromFile extends BaseJavaMigration {

    @Override
    public void migrate(Context context) {
        JdbcTemplate jdbcTemplate =
                new JdbcTemplate(context.getConnection());

        try {
            Path path = new ClassPathResource("sessions.csv").getFile().toPath();
            Files.lines(path).forEach(str -> {
                String[] strArr = str.split(",");
                if (!strArr[0].equals("cell_id")) {
                    try {
                        int count = jdbcTemplate.queryForInt("SELECT count(1) FROM abonentid WHERE ctn = ? ", strArr[1]);
                        if (count == 0) {
                            jdbcTemplate.update("INSERT INTO abonentid (ctn) VALUES (?)", strArr[1]);
                        }
                        jdbcTemplate.update("INSERT INTO sessions (cell_id, ctn) VALUES (?, ?)", strArr[0], strArr[1]);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
