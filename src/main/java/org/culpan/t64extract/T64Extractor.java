package org.culpan.t64extract;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by harryculpan on 11/27/14.
 */
public class T64Extractor {
    protected String t64Filename;

    protected TapeRecord tapeRecord;

    protected FileRecord [] fileRecords;

    protected boolean displayInfo = false;

    protected String destination;

    public T64Extractor(String t64Filename, String destination, boolean displayInfo) throws IOException {
        this.t64Filename = t64Filename;
        this.displayInfo = displayInfo;
        if (StringUtils.isBlank(destination)) {
            this.destination = System.getProperty("user.dir");
        } else {
            this.destination = destination;
        }
        readTapeRecord(t64Filename);
    }

    public String getT64Filename() {
        return t64Filename;
    }

    public TapeRecord getTapeRecord() {
        return tapeRecord;
    }

    public FileRecord[] getFileRecords() {
        return fileRecords;
    }

    public String getDestination() {
        return destination;
    }

    public FileRecord[] getNormalFileRecords() {
        return getFileRecordsOfType(FileRecord.EntryType.normal);
    }

    public FileRecord[] getFileRecordsOfType(FileRecord.EntryType entryType) {
        List<FileRecord> result = new ArrayList<>();
        for (FileRecord fileRecord : fileRecords) {
            if (FileRecord.EntryType.values()[fileRecord.getEntryType()].equals(entryType)) {
                result.add(fileRecord);
            }
        }
        return result.toArray(new FileRecord[result.size()]);
    }

    protected void readTapeRecord(String t64Filename) throws IOException {
        File t64 = new File(t64Filename);
        if (!t64.exists()) {
            throw new FileNotFoundException("Tape archive '" + t64Filename + "' is not found");
        } else if (!t64.isFile()) {
            throw new IOException("File '" + t64Filename + "' is not a file");
        }

        byte [] t64Bytes = Files.readAllBytes(Paths.get(t64.toURI()));
        tapeRecord = new TapeRecord();
        tapeRecord.tapeDescription = new String(ArrayUtils.subarray(t64Bytes, 0, 32));
        tapeRecord.tapeVersion = T64Helper.asShort(t64Bytes, 32);
        tapeRecord.numberOfEntries = T64Helper.asShort(t64Bytes, 34);
        tapeRecord.numberOfUsedEntries = T64Helper.asShort(t64Bytes, 36);
        tapeRecord.userDescription = new String(ArrayUtils.subarray(t64Bytes, 40, 24));

        fileRecords = new FileRecord[tapeRecord.numberOfEntries];
        for (int i = 0; i < tapeRecord.numberOfEntries; i++) {
            int baseOffset = 64 + (32 * i);
            FileRecord fileRecord = new FileRecord();
            fileRecord.entryType = t64Bytes[baseOffset];
            fileRecord.c64FileType = t64Bytes[baseOffset + 1];
            fileRecord.startAddress = T64Helper.asShort(t64Bytes, baseOffset + 2);
            fileRecord.endAddress = T64Helper.asShort(t64Bytes, baseOffset + 4);
            fileRecord.offset = T64Helper.asInt(t64Bytes, baseOffset + 8);
            fileRecord.c64Filename = new String(ArrayUtils.subarray(t64Bytes, baseOffset + 16, 16));
            fileRecords[i] = fileRecord;
        }
    }

    public void processFile() throws IOException {
        if (displayInfo) {
            System.out.println("  *** tape info:");
            System.out.println("    " + getTapeRecord().toString());
            for (FileRecord fileRecord : getFileRecords()) {
                System.out.println("  *** file record:");
                System.out.println("    " + fileRecord.toString());
            }
            System.out.println("no files being extracted");
        } else {
            System.out.println("  *** tape info:");
            System.out.println("    " + getTapeRecord().toString());
            File outputDir = new File(destination);
            if (!outputDir.exists() && !outputDir.mkdirs()) {
                throw new IOException("Destination '" + destination + "' does not exist and cannot be created");
            }

            String baseFilename = FilenameUtils.getBaseName(getT64Filename());
            FileRecord [] normalRecords = getNormalFileRecords();
            int fileIndex = 0;
            for (FileRecord fileRecord : normalRecords) {
                System.out.println("  *** file record:");
                System.out.println("    " + fileRecord.toString());
                String outputFilename;
                if (StringUtils.isNotBlank(fileRecord.getC64Filename())) {
                    outputFilename = T64Helper.cleanString(fileRecord.getC64Filename().toLowerCase());
                } else {
                    outputFilename = (normalRecords.length > 1 ? baseFilename + StringUtils.leftPad(Integer.toString(fileIndex), 2, '0') + ".prg": baseFilename + ".prg").toLowerCase();
                }

                byte[] bytes = Files.readAllBytes(Paths.get(getT64Filename()));
                Path destPath = Paths.get(destination, outputFilename);
                byte[] outputBytes = new byte[fileRecord.size() + 2];
                outputBytes[0] = (byte)((fileRecord.getStartAddress() << 8) >> 8);
                outputBytes[1] = (byte)(fileRecord.getStartAddress() >> 8);
                for (int i = 0; i < fileRecord.size(); i++) {
                    outputBytes[i + 2] = bytes[fileRecord.getOffset() + i];
                }
                Files.write(destPath, outputBytes);
                System.out.println("    written to: " + destPath);
            }
        }
    }

    public boolean isDisplayInfo() {
        return displayInfo;
    }

    public void setDisplayInfo(boolean displayInfo) {
        this.displayInfo = displayInfo;
    }
}
