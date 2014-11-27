package org.culpan.t64extract;

import org.apache.commons.lang3.ArrayUtils;

import java.io.*;

/**
 * Created by harryculpan on 11/27/14.
 */
public class T64Extractor {
    protected String t64Filename;

    protected TapeRecord tapeRecord;

    protected FileRecord [] fileRecords;

    public T64Extractor(String t64Filename) throws IOException {
        this.t64Filename = t64Filename;
        readTapeRecord(t64Filename);
    }

    public String getT64Filename() {
        return t64Filename;
    }

    public void setT64Filename(String t64Filename) {
        this.t64Filename = t64Filename;
    }

    public TapeRecord getTapeRecord() {
        return tapeRecord;
    }

    public FileRecord[] getFileRecords() {
        return fileRecords;
    }

    protected void readTapeRecord(String t64Filename) throws IOException {
        File t64 = new File(t64Filename);
        if (!t64.exists()) {
            throw new FileNotFoundException("Tape archive '" + t64Filename + "' is not found");
        } else if (!t64.isFile()) {
            throw new IOException("File '" + t64Filename + "' is not a file");
        }

        byte [] t64Bytes = new byte[(int)t64.length()];
        int bytesRead = new FileInputStream(t64).read(t64Bytes);
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
}
