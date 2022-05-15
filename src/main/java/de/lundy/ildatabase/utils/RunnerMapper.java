package de.lundy.ildatabase.utils;

import de.lundy.ildatabase.dto.RunnerDTO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RunnerMapper {

    private RunnerMapper() {

    }

    private static final MySQLUtils database = new MySQLUtils();

    public static List<RunnerDTO> mapRunners() throws SQLException {

        var runners = new ArrayList<RunnerDTO>();

        for (String r : database.getAllRunners()) {
            System.out.println(database.getRunnerNoRanks(r));
            runners.add(database.getRunnerNoRanks(r));
        }

        return runners;

    }

}
