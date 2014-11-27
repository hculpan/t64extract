package org.culpan.t64extract;

import org.apache.commons.cli.*;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

/**
 * Created by harryculpan on 11/27/14.
 */
public class T64ExtractMain {
    static public Options getOptions() {
        Options result = new Options();

        result.addOption(OptionBuilder.withDescription("displays help").withLongOpt("help").create('h'));
        result.addOption(OptionBuilder.withDescription("output location").withLongOpt("directory to output programs").hasArg().withArgName("directory").create('o'));
        result.addOption(OptionBuilder.withDescription("display tape info only; do not extract").withLongOpt("display").create('d'));

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
        } else if (cmd.getArgs().length > 1) {
            displayUsage("Only one T64 file may be processed at a time");
            System.exit(-1);
        }

        System.out.println("load tape '" + cmd.getArgs()[0]);
        try {
            T64Extractor t64Extractor = new T64Extractor(cmd.getArgs()[0], cmd.getOptionValue('o'), cmd.hasOption('d'));
            t64Extractor.processFile();
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
        formatter.printHelp( "t64extract [OPTIONS]... <T64 file>", getOptions() );
    }

    private static void displayUsage() {
        displayUsage(null);
    }
}
