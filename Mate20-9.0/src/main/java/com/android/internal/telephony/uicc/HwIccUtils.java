package com.android.internal.telephony.uicc;

import android.content.res.Resources;
import android.telephony.PhoneNumberUtils;
import android.telephony.Rlog;
import android.text.TextUtils;
import com.android.internal.telephony.HwGsmAlphabet;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HwIccUtils {
    static final int ADN_BCD_NUMBER_LENGTH = 0;
    static final int ADN_CAPABILITY_ID = 12;
    static final int ADN_DIALING_NUMBER_END = 11;
    static final int ADN_DIALING_NUMBER_START = 2;
    static final int ADN_EXTENSION_ID = 13;
    static final int ADN_TON_AND_NPI = 1;
    static final int EXT_RECORD_LENGTH_BYTES = 13;
    static final int EXT_RECORD_TYPE_ADDITIONAL_DATA = 2;
    static final int EXT_RECORD_TYPE_MASK = 3;
    static final int FOOTER_SIZE_BYTES = 14;
    private static final String LOG_TAG = "HwIccUtils";
    static final int MAX_EXT_CALLED_PARTY_LENGTH = 10;
    static final int MAX_NUMBER_SIZE_BYTES = 11;
    static final int MDN_BYTE_LENGTH = 11;
    private static final int MDN_NUMBER_TYPE_INDEX = 9;

    public static String adnStringFieldToStringForSTK(byte[] data, int offset, int length) {
        if (length == 0) {
            return "";
        }
        if (length >= 1 && data[offset] == Byte.MIN_VALUE) {
            String ret = null;
            try {
                ret = new String(data, offset + 1, ((length - 1) / 2) * 2, "utf-16be");
            } catch (UnsupportedEncodingException ex) {
                Rlog.e(LOG_TAG, "implausible UnsupportedEncodingException", ex);
            }
            if (ret != null) {
                int ucslen = ret.length();
                while (ucslen > 0 && ret.charAt(ucslen - 1) == 65535) {
                    ucslen--;
                }
                return ret.substring(0, ucslen);
            }
        }
        boolean isucs2 = false;
        char base = 0;
        int len = 0;
        if (length >= 3 && data[offset] == -127) {
            len = data[offset + 1] & 255;
            if (len > length - 3) {
                len = length - 3;
            }
            base = (char) ((data[offset + 2] & 255) << 7);
            offset += 3;
            isucs2 = true;
        } else if (length >= 4 && data[offset] == -126) {
            int len2 = data[offset + 1] & 255;
            if (len2 > length - 4) {
                len2 = length - 4;
            }
            base = (char) (((data[offset + 2] & 255) << 8) | (data[offset + 3] & 255));
            offset += 4;
            isucs2 = true;
        }
        if (isucs2) {
            StringBuilder ret2 = new StringBuilder();
            while (len > 0) {
                if (data[offset] < 0) {
                    ret2.append((char) ((data[offset] & 127) + base));
                    offset++;
                    len--;
                }
                int count = 0;
                while (count < len && data[offset + count] >= 0) {
                    count++;
                }
                ret2.append(HwGsmAlphabet.gsm8BitUnpackedToString(data, offset, count));
                offset += count;
                len -= count;
            }
            return ret2.toString();
        }
        String defaultCharset = "";
        try {
            defaultCharset = Resources.getSystem().getString(17040159);
        } catch (Resources.NotFoundException e) {
        }
        return HwGsmAlphabet.gsm8BitUnpackedToString(data, offset, length, defaultCharset.trim(), true);
    }

    public static int hexCharToInt(char c) {
        if (c >= '0' && c <= '9') {
            return c - '0';
        }
        if (c >= 'A' && c <= 'F') {
            return (c - 'A') + 10;
        }
        if (c >= 'a' && c <= 'f') {
            return (c - 'a') + 10;
        }
        throw new RuntimeException("invalid hex char '" + c + "'");
    }

    public static byte[] hexStringToBcd(String s) {
        if (s == null) {
            return new byte[0];
        }
        int sz = s.length();
        byte[] ret = new byte[(sz / 2)];
        for (int i = 0; i < sz; i += 2) {
            ret[i / 2] = (byte) ((hexCharToInt(s.charAt(i + 1)) << 4) | hexCharToInt(s.charAt(i)));
        }
        return ret;
    }

    public static String bcdIccidToString(byte[] data, int offset, int length) {
        StringBuilder ret = new StringBuilder(length * 2);
        char[] cnum = {'A', 'B', 'C', 'D', 'E', 'F'};
        for (int i = offset; i < offset + length; i++) {
            int v = data[i] & 15;
            if (v > 9) {
                ret.append(cnum[v - 10]);
            } else {
                ret.append((char) (48 + v));
            }
            int v2 = (data[i] >> 4) & 15;
            if (v2 > 9) {
                ret.append(cnum[v2 - 10]);
            } else {
                ret.append((char) (48 + v2));
            }
        }
        return ret.toString();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:68:0x0104, code lost:
        r1 = r24;
     */
    public static byte[] buildAdnStringHw(int recordSize, String mAlphaTag, String mNumber) {
        String mNumber2;
        int gsm7Len;
        String mAlphaTag2;
        int i;
        byte[] byteTag;
        int index;
        int i2 = recordSize;
        String str = mAlphaTag;
        String mNumber3 = mNumber;
        int footerOffset = i2 - 14;
        int ucs80Len = 1;
        int ucs80Converts = 0;
        int ucs81Len = 3;
        int ucs81Converts = 0;
        int ucs82Len = 4;
        int ucs82Converts = 0;
        char baser81 = ' ';
        char baser82Low = ' ';
        char baser82High = ' ';
        boolean useGsm7 = true;
        boolean usePattern81 = true;
        boolean setPattern81 = false;
        boolean usePattern82 = true;
        boolean setPattern82 = false;
        int gsm7Len2 = 0;
        if (mNumber3 != null) {
            if (str != null) {
                int gsm7Converts = 0;
                if (mNumber.length() > 20) {
                    Rlog.w(LOG_TAG, "[buildAdnString] Max length of dialing number is 20");
                    mNumber3 = mNumber3.substring(0, 20);
                }
                int lenTag = mAlphaTag.length();
                int index2 = 0;
                while (true) {
                    if (index2 < lenTag) {
                        if (!useGsm7 && !usePattern81 && !usePattern82 && ucs80Len > footerOffset) {
                            mNumber2 = mNumber3;
                            int i3 = lenTag;
                            int i4 = index2;
                            break;
                        }
                        int lenTag2 = lenTag;
                        char c = str.charAt(index2);
                        mNumber2 = mNumber3;
                        int currGsm7Length = HwGsmAlphabet.UCStoGsm7(c);
                        if (currGsm7Length == -1) {
                            useGsm7 = false;
                        } else if (useGsm7) {
                            gsm7Len2 += currGsm7Length;
                            gsm7Converts++;
                        }
                        gsm7Len = gsm7Len2;
                        if (!usePattern81 || ucs81Len >= footerOffset) {
                            index = index2;
                        } else {
                            if (-1 == currGsm7Length) {
                                index = index2;
                                if ((c & 32768) == 32768) {
                                    usePattern81 = false;
                                } else if (!setPattern81) {
                                    setPattern81 = true;
                                    baser81 = (char) (c & 32640);
                                } else if (baser81 != ((char) (c & 32640))) {
                                    usePattern81 = false;
                                }
                            } else {
                                index = index2;
                            }
                            if (usePattern81) {
                                ucs81Converts++;
                                if (-1 == currGsm7Length) {
                                    ucs81Len++;
                                } else {
                                    ucs81Len += currGsm7Length;
                                }
                            }
                        }
                        if (usePattern82 && ucs82Len < footerOffset) {
                            if (-1 == currGsm7Length) {
                                if (!setPattern82) {
                                    setPattern82 = true;
                                    baser82Low = c;
                                    baser82High = c;
                                } else {
                                    if (baser82Low > c) {
                                        baser82Low = c;
                                    } else if (baser82High < c) {
                                        baser82High = c;
                                    }
                                    if (baser82High - baser82Low > 127) {
                                        usePattern82 = false;
                                    }
                                }
                            }
                            if (usePattern82) {
                                ucs82Converts++;
                                if (-1 == currGsm7Length) {
                                    ucs82Len++;
                                } else {
                                    ucs82Len += currGsm7Length;
                                }
                            }
                        }
                        if (ucs80Len < footerOffset) {
                            ucs80Len += 2;
                            ucs80Converts++;
                        }
                        if (useGsm7) {
                            if (gsm7Len >= footerOffset) {
                                break;
                            }
                        } else if (usePattern81) {
                            if (ucs81Len >= footerOffset) {
                                break;
                            }
                        } else if (usePattern82) {
                            if (ucs82Len >= footerOffset) {
                                break;
                            }
                        } else if (ucs80Len >= footerOffset) {
                            break;
                        }
                        index2 = index + 1;
                        gsm7Len2 = gsm7Len;
                        lenTag = lenTag2;
                        mNumber3 = mNumber2;
                        str = mAlphaTag;
                        int gsm7Len3 = recordSize;
                    } else {
                        mNumber2 = mNumber3;
                        int i5 = lenTag;
                        int i6 = index2;
                        break;
                    }
                }
                int bestConverts = gsm7Converts;
                int bestMode = 0;
                int bestLen = gsm7Len;
                if (bestConverts < ucs81Converts) {
                    bestConverts = ucs81Converts;
                    bestMode = 129;
                    bestLen = ucs81Len;
                }
                if (bestConverts < ucs82Converts) {
                    bestConverts = ucs82Converts;
                    bestMode = 130;
                    bestLen = ucs82Len;
                }
                if (bestConverts < ucs80Converts) {
                    bestConverts = ucs80Converts;
                    bestMode = 128;
                    bestLen = ucs80Len;
                }
                int bestMode2 = bestMode;
                int bestConverts2 = bestConverts;
                int i7 = gsm7Len;
                String mAlphaTag3 = mAlphaTag.substring(0, bestConverts2);
                if (bestLen > footerOffset) {
                    int i8 = ucs80Len;
                    bestLen -= 2;
                    mAlphaTag2 = mAlphaTag3.substring(0, bestConverts2 - 1);
                } else {
                    mAlphaTag2 = mAlphaTag3;
                }
                int i9 = bestConverts2;
                int i10 = recordSize;
                byte[] adnString = new byte[i10];
                for (int i11 = 0; i11 < i10; i11++) {
                    adnString[i11] = -1;
                }
                String mNumber4 = mNumber2;
                if (mNumber4.length() > 0) {
                    byte[] bcdNumber = PhoneNumberUtils.numberToCalledPartyBCD(mNumber4);
                    String str2 = mNumber4;
                    int i12 = ucs80Converts;
                    StringBuilder sb = new StringBuilder();
                    int i13 = ucs81Len;
                    sb.append("buildAdnString bcdNumber.length = ");
                    sb.append(bcdNumber.length);
                    Rlog.e("AdnRecord", sb.toString());
                    System.arraycopy(bcdNumber, 0, adnString, footerOffset + 1, bcdNumber.length);
                    adnString[footerOffset + 0] = (byte) bcdNumber.length;
                } else {
                    int i14 = ucs80Converts;
                    int i15 = ucs81Len;
                }
                adnString[footerOffset + 12] = -1;
                adnString[footerOffset + 13] = -1;
                if (bestMode2 != 0) {
                    switch (bestMode2) {
                        case 128:
                            try {
                                byteTag = mAlphaTag2.getBytes("UTF-16BE");
                                i = 0;
                            } catch (UnsupportedEncodingException e) {
                                i = 0;
                                byteTag = new byte[0];
                            }
                            adnString[i] = Byte.MIN_VALUE;
                            System.arraycopy(byteTag, i, adnString, 1, byteTag.length);
                            break;
                        case 129:
                            byte[] byteTag2 = HwGsmAlphabet.stringToUCS81Packed(mAlphaTag2, baser81, bestLen - 2);
                            adnString[0] = -127;
                            adnString[1] = (byte) (bestLen - 3);
                            System.arraycopy(byteTag2, 0, adnString, 2, byteTag2.length);
                            break;
                        case 130:
                            byte[] byteTag3 = HwGsmAlphabet.stringToUCS82Packed(mAlphaTag2, baser82Low, bestLen - 2);
                            adnString[0] = -126;
                            adnString[1] = (byte) (bestLen - 4);
                            System.arraycopy(byteTag3, 0, adnString, 2, byteTag3.length);
                            break;
                    }
                } else {
                    byte[] byteTag4 = HwGsmAlphabet.stringToGsm8BitPacked(mAlphaTag2);
                    System.arraycopy(byteTag4, 0, adnString, 0, byteTag4.length);
                }
                return adnString;
            }
        }
        Rlog.w(LOG_TAG, "[buildAdnString] Empty alpha tag or number");
        byte[] adnString2 = new byte[i2];
        int i16 = 0;
        while (true) {
            int i17 = i16;
            if (i17 >= i2) {
                return adnString2;
            }
            adnString2[i17] = -1;
            i16 = i17 + 1;
        }
    }

    public static int getAlphaTagEncodingLength(String alphaTag) {
        int index;
        boolean useGsm7;
        String str = alphaTag;
        int ucs80Len = 1;
        int ucs80Converts = 0;
        int ucs81Len = 3;
        int ucs81Converts = 0;
        int ucs82Len = 4;
        int ucs82Converts = 0;
        char baser81 = ' ';
        char baser82Low = ' ';
        char baser82High = ' ';
        boolean useGsm72 = true;
        boolean usePattern81 = true;
        boolean setPattern81 = false;
        boolean usePattern82 = true;
        boolean setPattern82 = false;
        int index2 = 0;
        if (str == null) {
            Rlog.w(LOG_TAG, "[getAlphaTagEncodingLength] Empty alpha tag");
            return 0;
        }
        int gsm7Len = 0;
        int gsm7Converts = 0;
        int lenTag = alphaTag.length();
        while (true) {
            index = index2;
            if (index >= lenTag) {
                break;
            }
            int lenTag2 = lenTag;
            char c = str.charAt(index);
            int currGsm7Length = HwGsmAlphabet.UCStoGsm7(c);
            int index3 = index;
            if (currGsm7Length == -1) {
                useGsm72 = false;
            } else if (useGsm72) {
                gsm7Len += currGsm7Length;
                gsm7Converts++;
            }
            if (usePattern81) {
                if (-1 == currGsm7Length) {
                    useGsm7 = useGsm72;
                    if (c & true) {
                        usePattern81 = false;
                    } else if (!setPattern81) {
                        setPattern81 = true;
                        baser81 = (char) (c & 32640);
                    } else if (baser81 != ((char) (c & 32640))) {
                        usePattern81 = false;
                    }
                } else {
                    useGsm7 = useGsm72;
                }
                if (usePattern81) {
                    ucs81Converts++;
                    if (-1 == currGsm7Length) {
                        ucs81Len++;
                    } else {
                        ucs81Len += currGsm7Length;
                    }
                }
            } else {
                useGsm7 = useGsm72;
            }
            if (usePattern82) {
                if (-1 == currGsm7Length) {
                    if (!setPattern82) {
                        setPattern82 = true;
                        baser82Low = c;
                        baser82High = c;
                    } else {
                        if (baser82Low > c) {
                            baser82Low = c;
                        } else if (baser82High < c) {
                            baser82High = c;
                        }
                        if (baser82High - baser82Low > 127) {
                            usePattern82 = false;
                        }
                    }
                }
                if (usePattern82) {
                    ucs82Converts++;
                    if (-1 == currGsm7Length) {
                        ucs82Len++;
                    } else {
                        ucs82Len += currGsm7Length;
                    }
                }
            }
            ucs80Len += 2;
            ucs80Converts++;
            index2 = index3 + 1;
            lenTag = lenTag2;
            useGsm72 = useGsm7;
            str = alphaTag;
        }
        int i = index;
        int bestConverts = gsm7Converts;
        int bestLen = gsm7Len;
        if (bestConverts < ucs81Converts) {
            bestConverts = ucs81Converts;
            bestLen = ucs81Len;
        }
        if (bestConverts < ucs82Converts) {
            bestConverts = ucs82Converts;
            bestLen = ucs82Len;
        }
        if (bestConverts < ucs80Converts) {
            bestLen = ucs80Len;
        }
        return bestLen;
    }

    public static boolean equalAdn(AdnRecord first, AdnRecord second) {
        return first.mEfid == second.mEfid && first.mRecordNumber == second.mRecordNumber;
    }

    public static boolean isContainZeros(byte[] data, int length, int totalLength, int curIndex) {
        int startIndex = totalLength + curIndex;
        int endIndex = data.length;
        int tempTotalLength = totalLength;
        if (totalLength >= length || startIndex > endIndex) {
            return false;
        }
        for (int valueIndex = startIndex; valueIndex < endIndex; valueIndex++) {
            if (data[valueIndex] == 0) {
                tempTotalLength++;
            }
        }
        if (tempTotalLength == length) {
            return true;
        }
        return false;
    }

    public static boolean arrayCompareNullEqualsEmpty(String[] s1, String[] s2) {
        if (s1 == s2) {
            return true;
        }
        if (s1 == null) {
            s1 = new String[]{""};
        }
        if (s2 == null) {
            s2 = new String[]{""};
        }
        for (String str : s1) {
            if (!TextUtils.isEmpty(str) && !Arrays.asList(s2).contains(str)) {
                return false;
            }
        }
        for (String str2 : s2) {
            if (!TextUtils.isEmpty(str2) && !Arrays.asList(s1).contains(str2)) {
                return false;
            }
        }
        return true;
    }

    public static String[] updateAnrEmailArrayHelper(String[] dest, String[] src, int fileCount) {
        if (fileCount == 0) {
            return null;
        }
        if (dest == null || src == null) {
            return dest;
        }
        String[] ref = new String[fileCount];
        for (int i = 0; i < fileCount; i++) {
            ref[i] = "";
        }
        for (int i2 = 0; i2 < src.length; i2++) {
            if (!TextUtils.isEmpty(src[i2])) {
                int j = 0;
                while (true) {
                    if (j >= dest.length) {
                        break;
                    } else if (src[i2].equals(dest[j])) {
                        ref[i2] = src[i2];
                        break;
                    } else {
                        j++;
                    }
                }
            }
        }
        for (int i3 = 0; i3 < dest.length; i3++) {
            if (!Arrays.asList(ref).contains(dest[i3])) {
                int j2 = 0;
                while (true) {
                    if (j2 >= ref.length) {
                        break;
                    } else if (TextUtils.isEmpty(ref[j2])) {
                        ref[j2] = dest[i3];
                        break;
                    } else {
                        j2++;
                    }
                }
            }
        }
        return ref;
    }

    public static String cdmaDTMFToString(byte[] data, int offset, int length) {
        if (data == null) {
            return null;
        }
        if (data.length < (length + 1) / 2) {
            Rlog.w(LOG_TAG, "cdmaDTMFToString data.length < length");
            length = data.length * 2;
        }
        StringBuilder ret = new StringBuilder();
        if (11 == data.length && 1 == (data[9] & 1)) {
            ret.append('+');
        }
        int count = 0;
        int i = offset;
        while (count < length) {
            char c = intToCdmaDTMFChar(data[i] & 15);
            if ('-' != c) {
                ret.append(c);
            }
            int count2 = count + 1;
            if (count2 == length) {
                break;
            }
            char c2 = intToCdmaDTMFChar((data[i] >> 4) & 15);
            if ('-' != c2) {
                ret.append(c2);
            }
            count = count2 + 1;
            i++;
        }
        return ret.toString();
    }

    public static char intToCdmaDTMFChar(int c) {
        if (c >= 0 && c <= 9) {
            return (char) (c + 48);
        }
        if (c == 10) {
            return '0';
        }
        if (c == 11) {
            return '*';
        }
        if (c == 12) {
            return '#';
        }
        Rlog.w(LOG_TAG, "intToCdmaDTMFChar invalid char " + ((char) (c + 48)));
        return '-';
    }

    public static int cdmaDTMFCharToint(char c) {
        if (c > '0' && c <= '9') {
            return c - '0';
        }
        if (c == '0') {
            return 10;
        }
        if (c == '*') {
            return 11;
        }
        if (c == '#') {
            return 12;
        }
        throw new RuntimeException("invalid char for BCD " + c);
    }

    public static byte[] stringToCdmaDTMF(String number) {
        int numberLenReal = number.length();
        int numberLenEffective = numberLenReal;
        if (numberLenEffective == 0) {
            return new byte[0];
        }
        byte[] result = new byte[((numberLenEffective + 1) / 2)];
        int digitCount = 0;
        for (int i = 0; i < numberLenReal; i++) {
            int i2 = digitCount >> 1;
            result[i2] = (byte) (result[i2] | ((byte) ((cdmaDTMFCharToint(number.charAt(i)) & 15) << ((digitCount & 1) == 1 ? 4 : 0))));
            digitCount++;
        }
        if ((digitCount & 1) == 1) {
            int i3 = digitCount >> 1;
            result[i3] = (byte) (result[i3] | 240);
        }
        return result;
    }

    public static String prependPlusInLongAdnNumber(String Number) {
        StringBuilder ret;
        if (Number == null || Number.length() == 0) {
            return Number;
        }
        if (!(Number.indexOf(43) != -1)) {
            return Number;
        }
        String[] str = Number.split("\\+");
        StringBuilder ret2 = new StringBuilder();
        for (String append : str) {
            ret2.append(append);
        }
        String retString = ret2.toString();
        Matcher m = Pattern.compile("(^[#*])(.*)([#*])(.*)([*]{2})(.*)(#)$").matcher(retString);
        if (!m.matches()) {
            Matcher m2 = Pattern.compile("(^[#*])(.*)([#*])(.*)(#)$").matcher(retString);
            if (!m2.matches()) {
                Matcher m3 = Pattern.compile("(^[#*])(.*)([#*])(.*)").matcher(retString);
                if (m3.matches()) {
                    ret = new StringBuilder();
                    ret.append(m3.group(1));
                    ret.append(m3.group(2));
                    ret.append(m3.group(3));
                    ret.append("+");
                    ret.append(m3.group(4));
                } else {
                    StringBuilder ret3 = new StringBuilder();
                    ret3.append('+');
                    ret3.append(retString);
                    ret = ret3;
                }
            } else if ("".equals(m2.group(2))) {
                ret = new StringBuilder();
                ret.append(m2.group(1));
                ret.append(m2.group(3));
                ret.append(m2.group(4));
                ret.append(m2.group(5));
                ret.append("+");
            } else {
                ret = new StringBuilder();
                ret.append(m2.group(1));
                ret.append(m2.group(2));
                ret.append(m2.group(3));
                ret.append("+");
                ret.append(m2.group(4));
                ret.append(m2.group(5));
            }
        } else if ("".equals(m.group(2))) {
            ret = new StringBuilder();
            ret.append(m.group(1));
            ret.append(m.group(3));
            ret.append(m.group(4));
            ret.append(m.group(5));
            ret.append(m.group(6));
            ret.append(m.group(7));
            ret.append("+");
        } else {
            ret = new StringBuilder();
            ret.append(m.group(1));
            ret.append(m.group(2));
            ret.append(m.group(3));
            ret.append("+");
            ret.append(m.group(4));
            ret.append(m.group(5));
            ret.append(m.group(6));
            ret.append(m.group(7));
        }
        return ret.toString();
    }

    public static String cdmaBcdToStringHw(byte[] data, int offset, int length) {
        StringBuilder ret = new StringBuilder();
        boolean prependPlus = false;
        if (length > 0 && data.length > 9) {
            prependPlus = data[9] == 9;
        }
        if (prependPlus) {
            ret.append('+');
        }
        int count = 0;
        int i = offset;
        while (count < length) {
            int v = data[i] & 15;
            if (v == 11) {
                ret.append('*');
            } else if (v == 12) {
                ret.append('#');
            } else {
                if (v > 9) {
                    v = 0;
                }
                ret.append((char) (48 + v));
            }
            int count2 = count + 1;
            if (count2 == length) {
                break;
            }
            int v2 = (data[i] >> 4) & 15;
            if (v2 == 11) {
                ret.append('*');
            } else if (v2 == 12) {
                ret.append('#');
            } else {
                if (v2 > 9) {
                    v2 = 0;
                }
                ret.append((char) (48 + v2));
            }
            count = count2 + 1;
            i++;
        }
        return ret.toString();
    }
}
