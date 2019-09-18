package jcifs.smb;

class SmbComWriteResponse extends ServerMessageBlock {
    long count;

    SmbComWriteResponse() {
    }

    /* access modifiers changed from: package-private */
    public int writeParameterWordsWireFormat(byte[] dst, int dstIndex) {
        return 0;
    }

    /* access modifiers changed from: package-private */
    public int writeBytesWireFormat(byte[] dst, int dstIndex) {
        return 0;
    }

    /* access modifiers changed from: package-private */
    public int readParameterWordsWireFormat(byte[] buffer, int bufferIndex) {
        this.count = ((long) readInt2(buffer, bufferIndex)) & 65535;
        return 8;
    }

    /* access modifiers changed from: package-private */
    public int readBytesWireFormat(byte[] buffer, int bufferIndex) {
        return 0;
    }

    public String toString() {
        return new String("SmbComWriteResponse[" + super.toString() + ",count=" + this.count + "]");
    }
}
