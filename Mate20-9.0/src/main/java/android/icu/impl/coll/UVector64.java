package android.icu.impl.coll;

public final class UVector64 {
    private long[] buffer = new long[32];
    private int length = 0;

    public boolean isEmpty() {
        return this.length == 0;
    }

    public int size() {
        return this.length;
    }

    public long elementAti(int i) {
        return this.buffer[i];
    }

    public long[] getBuffer() {
        return this.buffer;
    }

    public void addElement(long e) {
        ensureAppendCapacity();
        long[] jArr = this.buffer;
        int i = this.length;
        this.length = i + 1;
        jArr[i] = e;
    }

    public void setElementAt(long elem, int index) {
        this.buffer[index] = elem;
    }

    public void insertElementAt(long elem, int index) {
        ensureAppendCapacity();
        System.arraycopy(this.buffer, index, this.buffer, index + 1, this.length - index);
        this.buffer[index] = elem;
        this.length++;
    }

    public void removeAllElements() {
        this.length = 0;
    }

    private void ensureAppendCapacity() {
        int i;
        int length2;
        if (this.length >= this.buffer.length) {
            if (this.buffer.length <= 65535) {
                i = 4;
                length2 = this.buffer.length;
            } else {
                i = 2;
                length2 = this.buffer.length;
            }
            long[] newBuffer = new long[(i * length2)];
            System.arraycopy(this.buffer, 0, newBuffer, 0, this.length);
            this.buffer = newBuffer;
        }
    }
}
