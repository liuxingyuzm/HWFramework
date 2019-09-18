package com.android.systemui.shared.recents;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.view.MotionEvent;
import com.android.systemui.shared.recents.ISystemUiProxy;

public interface IOverviewProxy extends IInterface {

    public static abstract class Stub extends Binder implements IOverviewProxy {
        private static final String DESCRIPTOR = "com.android.systemui.shared.recents.IOverviewProxy";
        static final int TRANSACTION_onBind = 1;
        static final int TRANSACTION_onMotionEvent = 3;
        static final int TRANSACTION_onOverviewHidden = 9;
        static final int TRANSACTION_onOverviewShown = 8;
        static final int TRANSACTION_onOverviewToggle = 7;
        static final int TRANSACTION_onPreMotionEvent = 2;
        static final int TRANSACTION_onQuickScrubEnd = 5;
        static final int TRANSACTION_onQuickScrubProgress = 6;
        static final int TRANSACTION_onQuickScrubStart = 4;
        static final int TRANSACTION_onQuickStep = 10;
        static final int TRANSACTION_onSplitTriggleBySystemUI = 12;
        static final int TRANSACTION_onTip = 11;

        private static class Proxy implements IOverviewProxy {
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

            public void onBind(ISystemUiProxy sysUiProxy) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(sysUiProxy != null ? sysUiProxy.asBinder() : null);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onPreMotionEvent(int downHitTarget) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(downHitTarget);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onMotionEvent(MotionEvent event) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (event != null) {
                        _data.writeInt(1);
                        event.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onQuickScrubStart() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onQuickScrubEnd() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(5, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onQuickScrubProgress(float progress) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeFloat(progress);
                    this.mRemote.transact(6, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onOverviewToggle() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(7, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onOverviewShown(boolean triggeredFromAltTab) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(triggeredFromAltTab);
                    this.mRemote.transact(8, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onOverviewHidden(boolean triggeredFromAltTab, boolean triggeredFromHomeKey) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(triggeredFromAltTab);
                    _data.writeInt(triggeredFromHomeKey);
                    this.mRemote.transact(9, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onQuickStep(MotionEvent event) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (event != null) {
                        _data.writeInt(1);
                        event.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(10, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onTip(int actionType, int viewType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(actionType);
                    _data.writeInt(viewType);
                    this.mRemote.transact(11, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onSplitTriggleBySystemUI(boolean triggeredBySystemUI) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(triggeredBySystemUI);
                    this.mRemote.transact(12, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IOverviewProxy asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IOverviewProxy)) {
                return new Proxy(obj);
            }
            return (IOverviewProxy) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            if (code != 1598968902) {
                MotionEvent _arg0 = null;
                boolean _arg1 = false;
                switch (code) {
                    case 1:
                        data.enforceInterface(DESCRIPTOR);
                        onBind(ISystemUiProxy.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 2:
                        data.enforceInterface(DESCRIPTOR);
                        onPreMotionEvent(data.readInt());
                        return true;
                    case 3:
                        data.enforceInterface(DESCRIPTOR);
                        if (data.readInt() != 0) {
                            _arg0 = (MotionEvent) MotionEvent.CREATOR.createFromParcel(data);
                        }
                        onMotionEvent(_arg0);
                        return true;
                    case 4:
                        data.enforceInterface(DESCRIPTOR);
                        onQuickScrubStart();
                        return true;
                    case 5:
                        data.enforceInterface(DESCRIPTOR);
                        onQuickScrubEnd();
                        return true;
                    case 6:
                        data.enforceInterface(DESCRIPTOR);
                        onQuickScrubProgress(data.readFloat());
                        return true;
                    case 7:
                        data.enforceInterface(DESCRIPTOR);
                        onOverviewToggle();
                        return true;
                    case 8:
                        data.enforceInterface(DESCRIPTOR);
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        onOverviewShown(_arg1);
                        return true;
                    case 9:
                        data.enforceInterface(DESCRIPTOR);
                        boolean _arg02 = data.readInt() != 0;
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        onOverviewHidden(_arg02, _arg1);
                        return true;
                    case 10:
                        data.enforceInterface(DESCRIPTOR);
                        if (data.readInt() != 0) {
                            _arg0 = (MotionEvent) MotionEvent.CREATOR.createFromParcel(data);
                        }
                        onQuickStep(_arg0);
                        return true;
                    case 11:
                        data.enforceInterface(DESCRIPTOR);
                        onTip(data.readInt(), data.readInt());
                        return true;
                    case 12:
                        data.enforceInterface(DESCRIPTOR);
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        onSplitTriggleBySystemUI(_arg1);
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

    void onBind(ISystemUiProxy iSystemUiProxy) throws RemoteException;

    void onMotionEvent(MotionEvent motionEvent) throws RemoteException;

    void onOverviewHidden(boolean z, boolean z2) throws RemoteException;

    void onOverviewShown(boolean z) throws RemoteException;

    void onOverviewToggle() throws RemoteException;

    void onPreMotionEvent(int i) throws RemoteException;

    void onQuickScrubEnd() throws RemoteException;

    void onQuickScrubProgress(float f) throws RemoteException;

    void onQuickScrubStart() throws RemoteException;

    void onQuickStep(MotionEvent motionEvent) throws RemoteException;

    void onSplitTriggleBySystemUI(boolean z) throws RemoteException;

    void onTip(int i, int i2) throws RemoteException;
}
