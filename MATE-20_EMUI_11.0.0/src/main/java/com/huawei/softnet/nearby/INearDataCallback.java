package com.huawei.softnet.nearby;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface INearDataCallback extends IInterface {
    void onDataReceive(String str, int i, byte[] bArr, int i2, String str2) throws RemoteException;

    void onDataUpdate() throws RemoteException;

    public static abstract class Stub extends Binder implements INearDataCallback {
        private static final String DESCRIPTOR = "com.huawei.softnet.nearby.INearDataCallback";
        static final int TRANSACTION_onDataReceive = 1;
        static final int TRANSACTION_onDataUpdate = 2;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static INearDataCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof INearDataCallback)) {
                return new Proxy(obj);
            }
            return (INearDataCallback) iin;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            if (code != 1598968902) {
                switch (code) {
                    case 1:
                        data.enforceInterface(DESCRIPTOR);
                        onDataReceive(data.readString(), data.readInt(), data.createByteArray(), data.readInt(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 2:
                        data.enforceInterface(DESCRIPTOR);
                        onDataUpdate();
                        reply.writeNoException();
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            } else {
                reply.writeString(DESCRIPTOR);
                return true;
            }
        }

        private static class Proxy implements INearDataCallback {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            @Override // com.huawei.softnet.nearby.INearDataCallback
            public void onDataReceive(String deviceId, int type, byte[] data, int len, String extInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(deviceId);
                    _data.writeInt(type);
                    _data.writeByteArray(data);
                    _data.writeInt(len);
                    _data.writeString(extInfo);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.huawei.softnet.nearby.INearDataCallback
            public void onDataUpdate() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
