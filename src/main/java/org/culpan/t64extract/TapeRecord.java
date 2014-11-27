package org.culpan.t64extract;

import javax.swing.text.NumberFormatter;
import java.io.StringWriter;

/**
 * Created by harryculpan on 11/27/14.
 */
public class TapeRecord {
    protected String tapeDescription;

    protected short tapeVersion;

    protected short numberOfEntries;

    protected short numberOfUsedEntries;

    protected String userDescription;

    @Override
    public String toString() {
        StringWriter writer = new StringWriter();

        writer.append("tapeDescription: ");
        writer.append(T64Helper.cleanString(tapeDescription));
        writer.append("; tapeVersion: ");
        writer.append(T64Helper.hexify(tapeVersion));
        writer.append("; numberOfDirectoryEntries: ");
        writer.append(T64Helper.hexify(numberOfEntries));
        writer.append("; numberOfUsedEntries: ");
        writer.append(T64Helper.hexify(numberOfUsedEntries));
        writer.append("; userDescription: ");
        writer.append(T64Helper.cleanString(userDescription));

        return writer.toString();
    }

    public String getTapeDescription() {
        return tapeDescription;
    }

    public short getTapeVersion() {
        return tapeVersion;
    }

    public short getNumberOfEntries() {
        return numberOfEntries;
    }

    public short getNumberOfUsedEntries() {
        return numberOfUsedEntries;
    }

    public String getUserDescription() {
        return userDescription;
    }
}
