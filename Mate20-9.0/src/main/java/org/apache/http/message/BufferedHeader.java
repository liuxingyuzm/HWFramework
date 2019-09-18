package org.apache.http.message;

import org.apache.http.FormattedHeader;
import org.apache.http.HeaderElement;
import org.apache.http.ParseException;
import org.apache.http.util.CharArrayBuffer;

@Deprecated
public class BufferedHeader implements FormattedHeader, Cloneable {
    private final CharArrayBuffer buffer;
    private final String name;
    private final int valuePos;

    public BufferedHeader(CharArrayBuffer buffer2) throws ParseException {
        if (buffer2 != null) {
            int colon = buffer2.indexOf(58);
            if (colon != -1) {
                String s = buffer2.substringTrimmed(0, colon);
                if (s.length() != 0) {
                    this.buffer = buffer2;
                    this.name = s;
                    this.valuePos = colon + 1;
                    return;
                }
                throw new ParseException("Invalid header: " + buffer2.toString());
            }
            throw new ParseException("Invalid header: " + buffer2.toString());
        }
        throw new IllegalArgumentException("Char array buffer may not be null");
    }

    public String getName() {
        return this.name;
    }

    public String getValue() {
        return this.buffer.substringTrimmed(this.valuePos, this.buffer.length());
    }

    public HeaderElement[] getElements() throws ParseException {
        ParserCursor cursor = new ParserCursor(0, this.buffer.length());
        cursor.updatePos(this.valuePos);
        return BasicHeaderValueParser.DEFAULT.parseElements(this.buffer, cursor);
    }

    public int getValuePos() {
        return this.valuePos;
    }

    public CharArrayBuffer getBuffer() {
        return this.buffer;
    }

    public String toString() {
        return this.buffer.toString();
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
