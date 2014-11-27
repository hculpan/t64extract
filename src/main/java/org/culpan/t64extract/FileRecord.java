package org.culpan.t64extract;

import java.io.StringWriter;

/**
 * Created by harryculpan on 11/27/14.
 */
public class FileRecord {
    public enum EntryType {
        free_entry,
        normal,
        with_header,
        memory_snapshot_v0_9,
        tape_block,
        digitized_stream
    };

    protected byte entryType;

    protected byte c64FileType;

    protected short startAddress;

    protected short endAddress;

    protected int offset;

    protected String c64Filename;

    @Override
    public String toString() {
        StringWriter writer = new StringWriter();

        writer.append("entryType: ");
        writer.append(EntryType.values()[entryType].toString());
        writer.append(" [" + T64Helper.hexify(entryType) + "]");
        writer.append("; c64FileType: ");
        writer.append(T64Helper.hexify(c64FileType));
        writer.append("; startAddress: ");
        writer.append(T64Helper.hexify(startAddress));
        writer.append("; endAddress: ");
        writer.append(T64Helper.hexify(endAddress));
        writer.append("; offset: ");
        writer.append(T64Helper.hexify(offset));
        writer.append("; c64Filename: ");
        writer.append(T64Helper.cleanString(c64Filename));

        return writer.toString();
    }

    public byte getEntryType() {
        return entryType;
    }

    public byte getC64FileType() {
        return c64FileType;
    }

    public short getStartAddress() {
        return startAddress;
    }

    public short getEndAddress() {
        return endAddress;
    }

    public int getOffset() {
        return offset;
    }

    public String getC64Filename() {
        return c64Filename;
    }

    public int size() {
        return getEndAddress() - getStartAddress();
    }
}
