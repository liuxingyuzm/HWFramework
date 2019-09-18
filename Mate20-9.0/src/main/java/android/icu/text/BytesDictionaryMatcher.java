package android.icu.text;

import android.icu.impl.Assert;
import android.icu.util.BytesTrie;
import java.text.CharacterIterator;

class BytesDictionaryMatcher extends DictionaryMatcher {
    private final byte[] characters;
    private final int transform;

    public BytesDictionaryMatcher(byte[] chars, int transform2) {
        this.characters = chars;
        Assert.assrt((2130706432 & transform2) == 16777216);
        this.transform = transform2;
    }

    private int transform(int c) {
        if (c == 8205) {
            return 255;
        }
        if (c == 8204) {
            return 254;
        }
        int delta = c - (this.transform & DictionaryData.TRANSFORM_OFFSET_MASK);
        if (delta < 0 || 253 < delta) {
            return -1;
        }
        return delta;
    }

    public int matches(CharacterIterator text_, int maxLength, int[] lengths, int[] count_, int limit, int[] values) {
        UCharacterIterator text = UCharacterIterator.getInstance(text_);
        BytesTrie bt = new BytesTrie(this.characters, 0);
        int c = text.nextCodePoint();
        if (c == -1) {
            return 0;
        }
        BytesTrie.Result result = bt.first(transform(c));
        int numChars = 1;
        int i = c;
        int count = 0;
        while (true) {
            if (!result.hasValue()) {
                if (result == BytesTrie.Result.NO_MATCH) {
                    break;
                }
            } else {
                if (count < limit) {
                    if (values != null) {
                        values[count] = bt.getValue();
                    }
                    lengths[count] = numChars;
                    count++;
                }
                if (result == BytesTrie.Result.FINAL_VALUE) {
                    break;
                }
            }
            if (numChars >= maxLength) {
                break;
            }
            int c2 = text.nextCodePoint();
            if (c2 == -1) {
                break;
            }
            numChars++;
            result = bt.next(transform(c2));
        }
        count_[0] = count;
        return numChars;
    }

    public int getType() {
        return 0;
    }
}
