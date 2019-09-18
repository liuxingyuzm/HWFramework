package com.android.org.bouncycastle.jcajce.provider.asymmetric.x509;

import com.android.org.bouncycastle.asn1.ASN1Sequence;
import com.android.org.bouncycastle.util.encoders.Base64;
import java.io.IOException;
import java.io.InputStream;

class PEMUtil {
    private final String _footer1;
    private final String _footer2;
    private final String _footer3;
    private final String _header1;
    private final String _header2;
    private final String _header3 = "-----BEGIN PKCS7-----";

    PEMUtil(String type) {
        this._header1 = "-----BEGIN " + type + "-----";
        this._header2 = "-----BEGIN X509 " + type + "-----";
        this._footer1 = "-----END " + type + "-----";
        this._footer2 = "-----END X509 " + type + "-----";
        this._footer3 = "-----END PKCS7-----";
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x0023  */
    /* JADX WARNING: Removed duplicated region for block: B:12:0x0025  */
    private String readLine(InputStream in) throws IOException {
        int c;
        StringBuffer l = new StringBuffer();
        while (true) {
            int read = in.read();
            c = read;
            if (read != 13 && c != 10 && c >= 0) {
                l.append((char) c);
            } else if (c < 0 || l.length() != 0) {
                if (c >= 0) {
                    return null;
                }
                if (c == 13) {
                    in.mark(1);
                    int read2 = in.read();
                    int c2 = read2;
                    if (read2 == 10) {
                        in.mark(1);
                    }
                    if (c2 > 0) {
                        in.reset();
                    }
                }
                return l.toString();
            }
        }
        if (c >= 0) {
        }
    }

    /* access modifiers changed from: package-private */
    public ASN1Sequence readPEMObject(InputStream in) throws IOException {
        String line;
        StringBuffer pemBuf = new StringBuffer();
        do {
            String readLine = readLine(in);
            line = readLine;
            if (readLine == null || line.startsWith(this._header1) || line.startsWith(this._header2)) {
            }
        } while (!line.startsWith(this._header3));
        while (true) {
            String readLine2 = readLine(in);
            String line2 = readLine2;
            if (readLine2 != null && !line2.startsWith(this._footer1) && !line2.startsWith(this._footer2) && !line2.startsWith(this._footer3)) {
                pemBuf.append(line2);
            }
        }
        if (pemBuf.length() == 0) {
            return null;
        }
        try {
            return ASN1Sequence.getInstance(Base64.decode(pemBuf.toString()));
        } catch (Exception e) {
            throw new IOException("malformed PEM data encountered");
        }
    }
}
