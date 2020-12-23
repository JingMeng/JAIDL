package me.fenfei.app.test2;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import me.fenfei.app.aidl.Add;

public class DoService extends Service {
    public DoService() {
    }

    private StubBinder mStubBinder;

    @Override
    public IBinder onBind(Intent intent) {
        if (null == mStubBinder) {
            mStubBinder = new StubBinder();
        }
        return mStubBinder.asBinder();
    }

    class StubBinder extends Add.Stub {
        @Override
        public int add(int a, int b) throws RemoteException {
            return a + b;
        }
    }

}
