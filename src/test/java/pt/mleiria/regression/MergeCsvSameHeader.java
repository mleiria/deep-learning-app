package pt.mleiria.regression;

import java.io.*;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;

public class MergeCsvSameHeader {
    private static final String workDir = "/home/manuel/Downloads/Databases/ThreeBodyProblem/X_test/processed_data/";

    public static void main(String[] args) throws Exception {

        args = new String[]{"train_data.csv", "validation_data.csv", "train_validation_merged.csv"};
        Path train = Paths.get(workDir + args[0]);
        Path valid = Paths.get(workDir + args[1]);
        Path out   = Paths.get(workDir + args[2]);

        try (BufferedReader brTrain = Files.newBufferedReader(train, StandardCharsets.UTF_8);
             BufferedReader brValid = Files.newBufferedReader(valid, StandardCharsets.UTF_8);
             BufferedWriter bwOut   = Files.newBufferedWriter(out, StandardCharsets.UTF_8)) {

            // write header from train
            String header = brTrain.readLine();
            if (header == null) throw new IllegalArgumentException("Empty train CSV");
            bwOut.write(header);
            bwOut.newLine();

            // copy train body
            for (String line; (line = brTrain.readLine()) != null; ) {
                if (!line.isBlank()) { bwOut.write(line); bwOut.newLine(); }
            }

            // skip header of valid, then copy body
            String validHeader = brValid.readLine();
            if (validHeader == null) throw new IllegalArgumentException("Empty validation CSV");
            // (optionally check equals)

            for (String line; (line = brValid.readLine()) != null; ) {
                if (!line.isBlank()) { bwOut.write(line); bwOut.newLine(); }
            }
        }
        System.out.println("Wrote merged file to " + out);
    }
}

