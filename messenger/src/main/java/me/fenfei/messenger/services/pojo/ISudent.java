/*
 * This file is auto-generated.  DO NOT MODIFY.
 */
package me.fenfei.messenger.services.pojo;
// Declare any non-default types here with import statements
//https://blog.csdn.net/nei504293736/article/details/94834528

import me.fenfei.messenger.services.aidl.Student;

public interface ISudent extends android.os.IInterface {
    /**
     * Local-side IPC implementation stub class.
     */
    public static abstract class Stub extends android.os.Binder implements ISudent {
        private static final String DESCRIPTOR = "me.fenfei.messenger.services.aidl.ISudent";

        /**
         * Construct the stub at attach it to the interface.
         */
        public Stub() {
            this.attachInterface(this, DESCRIPTOR);
        }

        /**
         * Cast an IBinder object into an me.fenfei.messenger.services.aidl.ISudent interface,
         * generating a proxy if needed.
         */
        public static ISudent asInterface(android.os.IBinder obj) {
            if ((obj == null)) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (((iin != null) && (iin instanceof ISudent))) {
                return ((ISudent) iin);
            }
            return new Stub.Proxy(obj);
        }

        @Override
        public android.os.IBinder asBinder() {
            return this;
        }

        @Override
        public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException {
            String descriptor = DESCRIPTOR;
            switch (code) {
                case INTERFACE_TRANSACTION: {
                    reply.writeString(descriptor);
                    return true;
                }
                case TRANSACTION_print: {
                    data.enforceInterface(descriptor);
                    Student _arg0;
                    if ((0 != data.readInt())) {
                        _arg0 = Student.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    this.print(_arg0);
                    reply.writeNoException();
                    return true;
                }
                default: {
                    return super.onTransact(code, data, reply, flags);
                }
            }
        }

        private static class Proxy implements ISudent {
            private android.os.IBinder mRemote;

            Proxy(android.os.IBinder remote) {
                mRemote = remote;
            }

            @Override
            public android.os.IBinder asBinder() {
                return mRemote;
            }

            public String getInterfaceDescriptor() {
                return DESCRIPTOR;
            }

            @Override
            public void print(Student student) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    if ((student != null)) {
                        _data.writeInt(1);
                        student.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    mRemote.transact(Stub.TRANSACTION_print, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        static final int TRANSACTION_print = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    }

    public void print(Student student) throws android.os.RemoteException;
}
