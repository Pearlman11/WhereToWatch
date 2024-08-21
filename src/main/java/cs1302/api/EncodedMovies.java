package cs1302.api;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Stream;


/**
 * Class used to map the id numbers to movieNames for watchMode Api.
 */

public class EncodedMovies {



    static final String FILEPATH = "resources/WatchIDandMovieName.rtf";

    /**
     * Method used to get the map of movies.
     * @throws IOException if the initialization fails.
     * @return The map of the movies and id numbers.
     */

    public static Map<String,String> getMap() throws IOException {
        String delimiter = ",";
        Map<String, String> map = new HashMap<>();

        try (Stream<String> lines = Files.lines(Paths.get(FILEPATH))) {
            lines.filter(line -> line.contains(delimiter)).forEach(
                line -> map.putIfAbsent(line.split(delimiter)[0], line.split(delimiter)[1])
            );
        }

        return map;
    } //getMap






} // EncodedMovies
