package de.lundy.ildatabase;

import com.google.gson.JsonSyntaxException;
import de.lundy.ildatabase.dto.RunDTO;
import de.lundy.ildatabase.dto.RunnerDTO;
import de.lundy.ildatabase.utils.MySQLUtils;
import de.lundy.ildatabase.utils.RunMapper;
import de.lundy.ildatabase.utils.RunnerMapper;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class Main {

    private static final MySQLUtils database = new MySQLUtils();
    private static final boolean fetch = true;

    public static void main(String[] args) throws SQLException {

        database.createTables();
        var now = System.currentTimeMillis();

        if (fetch) {

            List<RunDTO> runs = null;

            try {
                runs = RunMapper.mapRuns();
            } catch (IOException | JsonSyntaxException e) {
                e.printStackTrace();
            }

            List<RunDTO> finalRuns = runs;

            try {
                database.clearRuns();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            assert finalRuns != null;
            finalRuns.forEach(r -> {
                try {
                    database.putRunIntoDatabase(r);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });

        }

        database.backupDiscordId();

        List<RunnerDTO> runners = null;
        try {
            runners = RunnerMapper.mapRunners();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            database.clearRunners();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        assert runners != null;
        runners.forEach(ru -> {

            try {
                database.putRunnerIntoDatabase(ru);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        });

        database.putDiscordIdFromBackup();

        try {
            database.addRanks();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("Update took " + (System.currentTimeMillis() - now) + " ms");

    }

}
