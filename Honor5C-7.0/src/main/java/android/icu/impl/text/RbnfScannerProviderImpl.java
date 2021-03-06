package android.icu.impl.text;

import android.icu.text.CollationElementIterator;
import android.icu.text.Collator;
import android.icu.text.RbnfLenientScanner;
import android.icu.text.RbnfLenientScannerProvider;
import android.icu.text.RuleBasedCollator;
import android.icu.util.ULocale;
import java.util.HashMap;
import java.util.Map;

@Deprecated
public class RbnfScannerProviderImpl implements RbnfLenientScannerProvider {
    private static final boolean DEBUG = false;
    private Map<String, RbnfLenientScanner> cache;

    private static class RbnfLenientScannerImpl implements RbnfLenientScanner {
        private final RuleBasedCollator collator;

        private RbnfLenientScannerImpl(RuleBasedCollator rbc) {
            this.collator = rbc;
        }

        public boolean allIgnorable(String s) {
            CollationElementIterator iter = this.collator.getCollationElementIterator(s);
            int o = iter.next();
            while (o != -1 && CollationElementIterator.primaryOrder(o) == 0) {
                o = iter.next();
            }
            if (o == -1) {
                return true;
            }
            return false;
        }

        public int[] findText(String str, String key, int startingAt) {
            int keyLen = 0;
            for (int p = startingAt; p < str.length() && keyLen == 0; p++) {
                keyLen = prefixLength(str.substring(p), key);
                if (keyLen != 0) {
                    return new int[]{p, keyLen};
                }
            }
            return new int[]{-1, 0};
        }

        public int[] findText2(String str, String key, int startingAt) {
            CollationElementIterator strIter = this.collator.getCollationElementIterator(str);
            CollationElementIterator keyIter = this.collator.getCollationElementIterator(key);
            int keyStart = -1;
            strIter.setOffset(startingAt);
            int oStr = strIter.next();
            int oKey = keyIter.next();
            while (oKey != -1) {
                while (oStr != -1 && CollationElementIterator.primaryOrder(oStr) == 0) {
                    oStr = strIter.next();
                }
                while (oKey != -1 && CollationElementIterator.primaryOrder(oKey) == 0) {
                    oKey = keyIter.next();
                }
                if (oStr == -1) {
                    return new int[]{-1, 0};
                }
                if (oKey == -1) {
                    break;
                } else if (CollationElementIterator.primaryOrder(oStr) == CollationElementIterator.primaryOrder(oKey)) {
                    keyStart = strIter.getOffset();
                    oStr = strIter.next();
                    oKey = keyIter.next();
                } else if (keyStart != -1) {
                    keyStart = -1;
                    keyIter.reset();
                } else {
                    oStr = strIter.next();
                }
            }
            if (oKey != -1) {
                return new int[]{-1, 0};
            }
            return new int[]{keyStart, strIter.getOffset() - keyStart};
        }

        public int prefixLength(String str, String prefix) {
            CollationElementIterator strIter = this.collator.getCollationElementIterator(str);
            CollationElementIterator prefixIter = this.collator.getCollationElementIterator(prefix);
            int oStr = strIter.next();
            int oPrefix = prefixIter.next();
            while (oPrefix != -1) {
                while (CollationElementIterator.primaryOrder(oStr) == 0 && oStr != -1) {
                    oStr = strIter.next();
                }
                while (CollationElementIterator.primaryOrder(oPrefix) == 0 && oPrefix != -1) {
                    oPrefix = prefixIter.next();
                }
                if (oPrefix == -1) {
                    break;
                } else if (oStr == -1 || CollationElementIterator.primaryOrder(oStr) != CollationElementIterator.primaryOrder(oPrefix)) {
                    return 0;
                } else {
                    oStr = strIter.next();
                    oPrefix = prefixIter.next();
                }
            }
            int result = strIter.getOffset();
            if (oStr != -1) {
                result--;
            }
            return result;
        }
    }

    static {
        /* JADX: method processing error */
/*
        Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: android.icu.impl.text.RbnfScannerProviderImpl.<clinit>():void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:113)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:256)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:281)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:161)
Caused by: jadx.core.utils.exceptions.DecodeException:  in method: android.icu.impl.text.RbnfScannerProviderImpl.<clinit>():void
	at jadx.core.dex.instructions.InsnDecoder.decodeInsns(InsnDecoder.java:46)
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:98)
	... 5 more
Caused by: java.lang.IllegalArgumentException: bogus opcode: 0073
	at com.android.dx.io.OpcodeInfo.get(OpcodeInfo.java:1197)
	at com.android.dx.io.OpcodeInfo.getFormat(OpcodeInfo.java:1212)
	at com.android.dx.io.instructions.DecodedInstruction.decode(DecodedInstruction.java:72)
	at jadx.core.dex.instructions.InsnDecoder.decodeInsns(InsnDecoder.java:43)
	... 6 more
*/
        /*
        // Can't load method instructions.
        */
        throw new UnsupportedOperationException("Method not decompiled: android.icu.impl.text.RbnfScannerProviderImpl.<clinit>():void");
    }

    @Deprecated
    public RbnfScannerProviderImpl() {
        this.cache = new HashMap();
    }

    @Deprecated
    public RbnfLenientScanner get(ULocale locale, String extras) {
        String key = locale.toString() + "/" + extras;
        synchronized (this.cache) {
            RbnfLenientScanner result = (RbnfLenientScanner) this.cache.get(key);
            if (result != null) {
                return result;
            }
            result = createScanner(locale, extras);
            synchronized (this.cache) {
                this.cache.put(key, result);
            }
            return result;
        }
    }

    @Deprecated
    protected RbnfLenientScanner createScanner(ULocale locale, String extras) {
        RuleBasedCollator collator;
        try {
            collator = (RuleBasedCollator) Collator.getInstance(locale.toLocale());
            if (extras != null) {
                collator = new RuleBasedCollator(collator.getRules() + extras);
            }
            collator.setDecomposition(17);
        } catch (Exception e) {
            if (DEBUG) {
                e.printStackTrace();
                System.out.println("++++");
            }
            collator = null;
        }
        return new RbnfLenientScannerImpl(null);
    }
}
