package org.culpan.t64extract;

import org.apache.commons.cli.*;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

/**
 * Created by harryculpan on 11/27/14.
 */
public class T64ExtractMain {
    static public Options getOptions() {
        Options result = new Options();

        result.addOption(OptionBuilder.withDescription("displays help").withLongOpt("help").create('h'));
        result.addOption(OptionBuilder.withDescription("extract all").withLongOpt("all").create('a'));
        result.addOption(OptionBuilder.withDescription("display tape info").withLongOpt("display").create('d'));

        return result;
    }

    static public void main(String [] args) {
        CommandLine cmd = null;
        try {
            CommandLineParser parser = new PosixParser();
            cmd = parser.parse(getOptions(), args);
        } catch (ParseException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        if (cmd.hasOption('h')) {
            displayUsage();
            System.exit(0);
        } else if (cmd.getArgs() == null || cmd.getArgs().length == 0) {
            displayUsage("No T64 file specified");
            System.exit(-1);
        }

        System.out.println("load tape '" + cmd.getArgs()[0]);
        try {
            T64Extractor t64Extractor = new T64Extractor(cmd.getArgs()[0]);
            if (cmd.hasOption('d')) {
                System.out.println("  *** tape info:");
                System.out.println("    " + t64Extractor.getTapeRecord().toString());
                for (FileRecord fileRecord : t64Extractor.getFileRecords()) {
                    System.out.println("  *** file record:");
                    System.out.println("    " + fileRecord.toString());
                }
            }
            if (cmd.getArgs().length < 2 && !cmd.hasOption('a')) {
                System.out.println("no files being extracted");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        System.out.println("process complete");
    }

    private static void displayUsage(String errMessage) {
        if (StringUtils.isNotBlank(errMessage)) {
            System.out.println("ERROR: " + errMessage);
        }
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp( "t64extract [OPTIONS]... <T64 file> [program to extract]...", getOptions() );
    }

    private static void displayUsage() {
        displayUsage(null);
    }
}
