package com.huawei.zxing.oned;

import com.huawei.zxing.BarcodeFormat;
import com.huawei.zxing.EncodeHintType;
import com.huawei.zxing.Writer;
import com.huawei.zxing.WriterException;
import com.huawei.zxing.common.BitMatrix;
import java.util.Map;

public abstract class OneDimensionalCodeWriter implements Writer {
    public abstract boolean[] encode(String str);

    public final BitMatrix encode(String contents, BarcodeFormat format, int width, int height) throws WriterException {
        return encode(contents, format, width, height, null);
    }

    public BitMatrix encode(String contents, BarcodeFormat format, int width, int height, Map<EncodeHintType, ?> hints) throws WriterException {
        if (contents.isEmpty()) {
            throw new IllegalArgumentException("Found empty contents");
        } else if (width < 0 || height < 0) {
            throw new IllegalArgumentException("Negative size is not allowed. Input: " + width + 'x' + height);
        } else {
            int sidesMargin = getDefaultMargin();
            if (hints != null) {
                Integer sidesMarginInt = (Integer) hints.get(EncodeHintType.MARGIN);
                if (sidesMarginInt != null) {
                    sidesMargin = sidesMarginInt.intValue();
                }
            }
            return renderResult(encode(contents), width, height, sidesMargin);
        }
    }

    private static BitMatrix renderResult(boolean[] code, int width, int height, int sidesMargin) {
        int inputWidth = code.length;
        int fullWidth = inputWidth + sidesMargin;
        int outputWidth = Math.max(width, fullWidth);
        int outputHeight = Math.max(1, height);
        int multiple = outputWidth / fullWidth;
        BitMatrix output = new BitMatrix(outputWidth, outputHeight);
        int inputX = 0;
        int outputX = (outputWidth - (inputWidth * multiple)) / 2;
        while (inputX < inputWidth) {
            if (code[inputX]) {
                output.setRegion(outputX, 0, multiple, outputHeight);
            }
            inputX++;
            outputX += multiple;
        }
        return output;
    }

    protected static int appendPattern(boolean[] target, int pos, int[] pattern, boolean startColor) {
        int numAdded = 0;
        int length = pattern.length;
        boolean color = startColor;
        int pos2 = pos;
        int pos3 = 0;
        while (pos3 < length) {
            int len = pattern[pos3];
            int pos4 = pos2;
            int j = 0;
            while (j < len) {
                target[pos4] = color;
                j++;
                pos4++;
            }
            numAdded += len;
            color = !color;
            pos3++;
            pos2 = pos4;
        }
        return numAdded;
    }

    public int getDefaultMargin() {
        return 10;
    }
}
