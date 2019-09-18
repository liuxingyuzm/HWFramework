package android.location;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface ILocationListener extends IInterface {

    public static abstract class Stub extends Binder implements ILocationListener {
        private static final String DESCRIPTOR = "android.location.ILocationListener";
        static final int TRANSACTION_onLocationChanged = 1;
        static final int TRANSACTION_onProviderDisabled = 4;
        static final int TRANSACTION_onProviderEnabled = 3;
        static final int TRANSACTION_onStatusChanged = 2;

        private static class Proxy implements ILocationListener {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            public void onLocationChanged(Location location) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (location != null) {
                        _data.writeInt(1);
                        location.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onStatusChanged(String provider, int status, Bundle extras) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(provider);
                    _data.writeInt(status);
                    if (extras != null) {
                        _data.writeInt(1);
                        extras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onProviderEnabled(String provider) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(provider);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onProviderDisabled(String provider) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(provider);
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ILocationListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof ILocationListener)) {
                return new Proxy(obj);
            }
            return (ILocationListener) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v2, resolved type: android.location.Location} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v6, resolved type: android.os.Bundle} */
        /* JADX WARNING: type inference failed for: r1v1 */
        /* JADX WARNING: type inference failed for: r1v13 */
        /* JADX WARNING: type inference failed for: r1v14 */
        /* JADX WARNING: Multi-variable type inference failed */
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            if (code != 1598968902) {
                ? _arg2 = 0;
                switch (code) {
                    case 1:
                        data.enforceInterface(DESCRIPTOR);
                        if (data.readInt() != 0) {
                            _arg2 = Location.CREATOR.createFromParcel(data);
                        }
                        onLocationChanged(_arg2);
                        return true;
                    case 2:
                        data.enforceInterface(DESCRIPTOR);
                        String _arg0 = data.readString();
                        int _arg1 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg2 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                        }
                        onStatusChanged(_arg0, _arg1, _arg2);
                        return true;
                    case 3:
                        data.enforceInterface(DESCRIPTOR);
                        onProviderEnabled(data.readString());
                        return true;
                    case 4:
                        data.enforceInterface(DESCRIPTOR);
                        onProviderDisabled(data.readString());
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            } else {
                reply.writeString(DESCRIPTOR);
                return true;
            }
        }
    }

    void onLocationChanged(Location location) throws RemoteException;

    void onProviderDisabled(String str) throws RemoteException;

    void onProviderEnabled(String str) throws RemoteException;

    void onStatusChanged(String str, int i, Bundle bundle) throws RemoteException;
}
