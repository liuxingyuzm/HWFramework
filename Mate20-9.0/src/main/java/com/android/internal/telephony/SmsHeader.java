package com.android.internal.telephony;

import com.android.internal.util.HexDump;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Iterator;

public class SmsHeader {
    public static final int ELT_ID_APPLICATION_PORT_ADDRESSING_16_BIT = 5;
    public static final int ELT_ID_APPLICATION_PORT_ADDRESSING_8_BIT = 4;
    public static final int ELT_ID_CHARACTER_SIZE_WVG_OBJECT = 25;
    public static final int ELT_ID_COMPRESSION_CONTROL = 22;
    public static final int ELT_ID_CONCATENATED_16_BIT_REFERENCE = 8;
    public static final int ELT_ID_CONCATENATED_8_BIT_REFERENCE = 0;
    public static final int ELT_ID_ENHANCED_VOICE_MAIL_INFORMATION = 35;
    public static final int ELT_ID_EXTENDED_OBJECT = 20;
    public static final int ELT_ID_EXTENDED_OBJECT_DATA_REQUEST_CMD = 26;
    public static final int ELT_ID_HYPERLINK_FORMAT_ELEMENT = 33;
    public static final int ELT_ID_LARGE_ANIMATION = 14;
    public static final int ELT_ID_LARGE_PICTURE = 16;
    public static final int ELT_ID_NATIONAL_LANGUAGE_LOCKING_SHIFT = 37;
    public static final int ELT_ID_NATIONAL_LANGUAGE_SINGLE_SHIFT = 36;
    public static final int ELT_ID_OBJECT_DISTR_INDICATOR = 23;
    public static final int ELT_ID_PREDEFINED_ANIMATION = 13;
    public static final int ELT_ID_PREDEFINED_SOUND = 11;
    public static final int ELT_ID_REPLY_ADDRESS_ELEMENT = 34;
    public static final int ELT_ID_REUSED_EXTENDED_OBJECT = 21;
    public static final int ELT_ID_RFC_822_EMAIL_HEADER = 32;
    public static final int ELT_ID_SMALL_ANIMATION = 15;
    public static final int ELT_ID_SMALL_PICTURE = 17;
    public static final int ELT_ID_SMSC_CONTROL_PARAMS = 6;
    public static final int ELT_ID_SPECIAL_SMS_MESSAGE_INDICATION = 1;
    public static final int ELT_ID_STANDARD_WVG_OBJECT = 24;
    public static final int ELT_ID_TEXT_FORMATTING = 10;
    public static final int ELT_ID_UDH_SOURCE_INDICATION = 7;
    public static final int ELT_ID_USER_DEFINED_SOUND = 12;
    public static final int ELT_ID_USER_PROMPT_INDICATOR = 19;
    public static final int ELT_ID_VARIABLE_PICTURE = 18;
    public static final int ELT_ID_WIRELESS_CTRL_MSG_PROTOCOL = 9;
    public static final int PORT_WAP_PUSH = 2948;
    public static final int PORT_WAP_WSP = 9200;
    public ConcatRef concatRef;
    public int languageShiftTable;
    public int languageTable;
    public ArrayList<MiscElt> miscEltList = new ArrayList<>();
    public PortAddrs portAddrs;
    public ArrayList<SpecialSmsMsg> specialSmsMsgList = new ArrayList<>();

    public static class ConcatRef {
        public boolean isEightBits;
        public int msgCount;
        public int refNumber;
        public int seqNumber;
    }

    public static class MiscElt {
        public byte[] data;
        public int id;
    }

    public static class PortAddrs {
        public boolean areEightBits;
        public int destPort;
        public int origPort;
    }

    public static class SpecialSmsMsg {
        public int msgCount;
        public int msgIndType;
    }

    public static SmsHeader fromByteArray(byte[] data) {
        ByteArrayInputStream inStream = new ByteArrayInputStream(data);
        SmsHeader smsHeader = new SmsHeader();
        while (inStream.available() > 0) {
            int id = inStream.read();
            int length = inStream.read();
            switch (id) {
                case 0:
                    ConcatRef concatRef2 = new ConcatRef();
                    concatRef2.refNumber = inStream.read();
                    concatRef2.msgCount = inStream.read();
                    concatRef2.seqNumber = inStream.read();
                    concatRef2.isEightBits = true;
                    if (!(concatRef2.msgCount == 0 || concatRef2.seqNumber == 0 || concatRef2.seqNumber > concatRef2.msgCount)) {
                        smsHeader.concatRef = concatRef2;
                        break;
                    }
                case 1:
                    SpecialSmsMsg specialSmsMsg = new SpecialSmsMsg();
                    specialSmsMsg.msgIndType = inStream.read();
                    specialSmsMsg.msgCount = inStream.read();
                    smsHeader.specialSmsMsgList.add(specialSmsMsg);
                    break;
                case 4:
                    PortAddrs portAddrs2 = new PortAddrs();
                    portAddrs2.destPort = inStream.read();
                    portAddrs2.origPort = inStream.read();
                    portAddrs2.areEightBits = true;
                    smsHeader.portAddrs = portAddrs2;
                    break;
                case 5:
                    PortAddrs portAddrs3 = new PortAddrs();
                    portAddrs3.destPort = (inStream.read() << 8) | inStream.read();
                    portAddrs3.origPort = (inStream.read() << 8) | inStream.read();
                    portAddrs3.areEightBits = false;
                    smsHeader.portAddrs = portAddrs3;
                    break;
                case 8:
                    ConcatRef concatRef3 = new ConcatRef();
                    concatRef3.refNumber = (inStream.read() << 8) | inStream.read();
                    concatRef3.msgCount = inStream.read();
                    concatRef3.seqNumber = inStream.read();
                    concatRef3.isEightBits = false;
                    if (!(concatRef3.msgCount == 0 || concatRef3.seqNumber == 0 || concatRef3.seqNumber > concatRef3.msgCount)) {
                        smsHeader.concatRef = concatRef3;
                        break;
                    }
                case 36:
                    smsHeader.languageShiftTable = inStream.read();
                    break;
                case 37:
                    smsHeader.languageTable = inStream.read();
                    break;
                default:
                    MiscElt miscElt = new MiscElt();
                    miscElt.id = id;
                    miscElt.data = new byte[length];
                    inStream.read(miscElt.data, 0, length);
                    smsHeader.miscEltList.add(miscElt);
                    break;
            }
        }
        return smsHeader;
    }

    public static byte[] toByteArray(SmsHeader smsHeader) {
        if (smsHeader.portAddrs == null && smsHeader.concatRef == null && smsHeader.specialSmsMsgList.isEmpty() && smsHeader.miscEltList.isEmpty() && smsHeader.languageShiftTable == 0 && smsHeader.languageTable == 0) {
            return null;
        }
        ByteArrayOutputStream outStream = new ByteArrayOutputStream(140);
        ConcatRef concatRef2 = smsHeader.concatRef;
        if (concatRef2 != null) {
            if (concatRef2.isEightBits) {
                outStream.write(0);
                outStream.write(3);
                outStream.write(concatRef2.refNumber);
            } else {
                outStream.write(8);
                outStream.write(4);
                outStream.write(concatRef2.refNumber >>> 8);
                outStream.write(concatRef2.refNumber & 255);
            }
            outStream.write(concatRef2.msgCount);
            outStream.write(concatRef2.seqNumber);
        }
        PortAddrs portAddrs2 = smsHeader.portAddrs;
        if (portAddrs2 != null) {
            if (portAddrs2.areEightBits) {
                outStream.write(4);
                outStream.write(2);
                outStream.write(portAddrs2.destPort);
                outStream.write(portAddrs2.origPort);
            } else {
                outStream.write(5);
                outStream.write(4);
                outStream.write(portAddrs2.destPort >>> 8);
                outStream.write(portAddrs2.destPort & 255);
                outStream.write(portAddrs2.origPort >>> 8);
                outStream.write(portAddrs2.origPort & 255);
            }
        }
        if (smsHeader.languageShiftTable != 0) {
            outStream.write(36);
            outStream.write(1);
            outStream.write(smsHeader.languageShiftTable);
        }
        if (smsHeader.languageTable != 0) {
            outStream.write(37);
            outStream.write(1);
            outStream.write(smsHeader.languageTable);
        }
        Iterator<SpecialSmsMsg> it = smsHeader.specialSmsMsgList.iterator();
        while (it.hasNext()) {
            SpecialSmsMsg specialSmsMsg = it.next();
            outStream.write(1);
            outStream.write(2);
            outStream.write(specialSmsMsg.msgIndType & 255);
            outStream.write(specialSmsMsg.msgCount & 255);
        }
        Iterator<MiscElt> it2 = smsHeader.miscEltList.iterator();
        while (it2.hasNext()) {
            MiscElt miscElt = it2.next();
            outStream.write(miscElt.id);
            outStream.write(miscElt.data.length);
            outStream.write(miscElt.data, 0, miscElt.data.length);
        }
        return outStream.toByteArray();
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("UserDataHeader ");
        builder.append("{ ConcatRef ");
        if (this.concatRef == null) {
            builder.append("unset");
        } else {
            builder.append("{ refNumber=" + this.concatRef.refNumber);
            builder.append(", msgCount=" + this.concatRef.msgCount);
            builder.append(", seqNumber=" + this.concatRef.seqNumber);
            builder.append(", isEightBits=" + this.concatRef.isEightBits);
            builder.append(" }");
        }
        builder.append(", PortAddrs ");
        if (this.portAddrs == null) {
            builder.append("unset");
        } else {
            builder.append("{ destPort=" + this.portAddrs.destPort);
            builder.append(", origPort=" + this.portAddrs.origPort);
            builder.append(", areEightBits=" + this.portAddrs.areEightBits);
            builder.append(" }");
        }
        if (this.languageShiftTable != 0) {
            builder.append(", languageShiftTable=" + this.languageShiftTable);
        }
        if (this.languageTable != 0) {
            builder.append(", languageTable=" + this.languageTable);
        }
        Iterator<SpecialSmsMsg> it = this.specialSmsMsgList.iterator();
        while (it.hasNext()) {
            SpecialSmsMsg specialSmsMsg = it.next();
            builder.append(", SpecialSmsMsg ");
            builder.append("{ msgIndType=" + specialSmsMsg.msgIndType);
            builder.append(", msgCount=" + specialSmsMsg.msgCount);
            builder.append(" }");
        }
        Iterator<MiscElt> it2 = this.miscEltList.iterator();
        while (it2.hasNext()) {
            MiscElt miscElt = it2.next();
            builder.append(", MiscElt ");
            builder.append("{ id=" + miscElt.id);
            builder.append(", length=" + miscElt.data.length);
            builder.append(", data=" + HexDump.toHexString(miscElt.data));
            builder.append(" }");
        }
        builder.append(" }");
        return builder.toString();
    }
}
