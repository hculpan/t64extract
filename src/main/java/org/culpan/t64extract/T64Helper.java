package org.culpan.t64extract;

/**
 * Created by harryculpan on 11/27/14.
 */
public class T64Helper {
    static public String cleanString(String str) {
        if (str == null) return null;

        String result = "";

        for (int i = 0; i < str.toCharArray().length; i++) {
            char c = str.charAt(i);
            if (c == '\r' || c == '\n') {
                result += " ";
                // We want to put only one space we have a \r\n combo
                if (i - 1 < str.toCharArray().length &&
                        (str.charAt(i + 1) == '\r' || str.charAt(i + 1) == '\n')) {
                    i++;
                }
            } else {
                result += c;
            }
        }

        return result;
    }

    static public String hexify(int value) {
        return String.format("$%8s", Integer.toHexString(value)).replace(' ', '0');
    }

    static public String hexify(byte value) {
        return String.format("$%2s", Integer.toHexString(value)).replace(' ', '0');
    }

    static public String hexify(short value) {
        return String.format("$%4s", Integer.toHexString(value)).replace(' ', '0');
    }

    static public short asShort(byte b1, byte b2) {
        return (short)(b1 + (b2 * 256));
    }

    static public short asShort(byte [] bytes, int offset) {
        return (short)(bytes[offset] + (bytes[offset + 1] * 256));
    }

    static public int asInt(byte [] bytes, int offset) {
        return (short)(bytes[offset] +
                (bytes[offset + 1] * 256) +
                (bytes[offset + 2] * 65536) +
                (bytes[offset + 3] * 16777216)
        );
    }

}
