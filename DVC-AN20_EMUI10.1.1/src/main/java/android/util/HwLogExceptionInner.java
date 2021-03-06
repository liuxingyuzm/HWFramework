package android.util;

import android.os.Environment;
import android.os.SystemProperties;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HwLogExceptionInner implements LogException {
    public static final int CONFIG_FILE_DATA = 2;
    public static final int CONFIG_FILE_NONE = 0;
    public static final int CONFIG_FILE_ROM = 1;
    public static final int LEVEL_A = 65;
    public static final int LEVEL_B = 66;
    public static final int LEVEL_C = 67;
    public static final int LEVEL_D = 68;
    private static final int LOG_ID_EXCEPTION = 5;
    public static final String TAG = "HwLogExceptionInner";
    private static int mBlackListFile = 0;
    private static long mLastModifiedTime = 0;
    private static Set<String> mLogBlackList = new HashSet();
    private static LogException mLogExceptionInner = null;
    private static List<String> mPackageNameList = new ArrayList();
    private Runnable mCallback = new UpdateBlackListTask(this);
    private boolean mLogDisable = false;
    private boolean mPropLogSwitch = false;

    public static native int println_exception_native(String str, int i, String str2, String str3);

    public static native int setliblogparam_native(int i, String str);

    static {
        System.loadLibrary("hwlog_jni");
    }

    static class UpdateBlackListTask implements Runnable {
        private HwLogExceptionInner mLogException;

        UpdateBlackListTask(HwLogExceptionInner logException) {
            this.mLogException = logException;
        }

        public void run() {
            if (this.mLogException.updateAllSwitch()) {
                Log.i(HwLogExceptionInner.TAG, "updateLogSwitch");
                this.mLogException.updateLogSwitch();
            }
        }
    }

    public static synchronized LogException getInstance() {
        LogException logException;
        synchronized (HwLogExceptionInner.class) {
            if (mLogExceptionInner == null) {
                mLogExceptionInner = new HwLogExceptionInner();
            }
            logException = mLogExceptionInner;
        }
        return logException;
    }

    public int cmd(String tag, String contain) {
        return println_exception_native(tag, 0, "command", contain);
    }

    public int msg(String category, int level, String header, String body) {
        return println_exception_native(category, level, "message", header + '\n' + body);
    }

    public int msg(String category, int level, int mask, String header, String body) {
        return println_exception_native(category, level, "message", "mask=" + mask + ";" + header + '\n' + body);
    }

    public int setliblogparam(int paramid, String val) {
        Log.i(TAG, "Log blacklist " + val);
        return setliblogparam_native(paramid, val);
    }

    public void initLogBlackList() {
        this.mPropLogSwitch = getPropLogSwitch();
        updateBlackList_static(getConfigFileUpdated());
        if (SystemProperties.getInt("ro.logsystem.usertype", 1) == 3) {
            addChangeCallback();
        }
    }

    private static boolean getPropLogSwitch() {
        if (SystemProperties.getInt("persist.hiview.logblacklist", 1) == 0) {
            return true;
        }
        return false;
    }

    private void addChangeCallback() {
        SystemProperties.addChangeCallback(this.mCallback);
    }

    public boolean isInLogBlackList(String packageName) {
        setPackageName(packageName);
        updateAllSwitch();
        if (this.mPropLogSwitch || !isInLogBlackList_static()) {
            return false;
        }
        return true;
    }

    private static void setPackageName(String packageName) {
        mPackageNameList.add(packageName);
    }

    /* access modifiers changed from: package-private */
    public void updateLogSwitch() {
        boolean disable = true;
        if (!mPackageNameList.isEmpty()) {
            if (this.mPropLogSwitch || !isInLogBlackList_static()) {
                disable = false;
            }
            if (this.mLogDisable != disable) {
                setliblogparam(2, disable ? "on" : "off");
                this.mLogDisable = disable;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public boolean updateAllSwitch() {
        int nFile;
        boolean bRet = false;
        boolean propswitch = getPropLogSwitch();
        if (this.mPropLogSwitch != propswitch) {
            bRet = true;
            this.mPropLogSwitch = propswitch;
        }
        if (!propswitch && (nFile = getConfigFileUpdated()) != 0) {
            return updateBlackList_static(nFile);
        }
        return bRet;
    }

    public static boolean updateBlackList_static(int file) {
        File blackListFile;
        if (file == 1) {
            blackListFile = new File(Environment.getRootDirectory().getPath() + "/etc/hiview/log_blacklist.cfg");
        } else if (file != 2) {
            return false;
        } else {
            blackListFile = new File(Environment.getDataDirectory().getPath() + "/system/hiview/log_blacklist.cfg");
        }
        if (!blackListFile.isFile() || !blackListFile.canRead()) {
            return false;
        }
        boolean bRet = true;
        BufferedReader in = null;
        InputStreamReader is = null;
        FileInputStream fi = null;
        mLogBlackList.clear();
        try {
            FileInputStream fi2 = new FileInputStream(blackListFile);
            InputStreamReader is2 = new InputStreamReader(fi2, "UTF-8");
            BufferedReader in2 = new BufferedReader(is2);
            while (true) {
                String blackPackageName = in2.readLine();
                if (blackPackageName != null) {
                    mLogBlackList.add(blackPackageName);
                } else {
                    try {
                        break;
                    } catch (IOException e) {
                        Log.e(TAG, "close fi IOException");
                    }
                }
            }
            fi2.close();
            try {
                is2.close();
            } catch (IOException e2) {
                Log.e(TAG, "close is IOException");
            }
            try {
                in2.close();
            } catch (IOException e3) {
                Log.e(TAG, "close in IOException");
            }
        } catch (IOException e4) {
            Log.e(TAG, "updateBlackList_static IOException");
            bRet = false;
            if (0 != 0) {
                try {
                    fi.close();
                } catch (IOException e5) {
                    Log.e(TAG, "close fi IOException");
                }
            }
            if (0 != 0) {
                try {
                    is.close();
                } catch (IOException e6) {
                    Log.e(TAG, "close is IOException");
                }
            }
            if (0 != 0) {
                in.close();
            }
        } catch (Throwable th) {
            if (0 != 0) {
                try {
                    fi.close();
                } catch (IOException e7) {
                    Log.e(TAG, "close fi IOException");
                }
            }
            if (0 != 0) {
                try {
                    is.close();
                } catch (IOException e8) {
                    Log.e(TAG, "close is IOException");
                }
            }
            if (0 != 0) {
                try {
                    in.close();
                } catch (IOException e9) {
                    Log.e(TAG, "close in IOException");
                }
            }
            throw th;
        }
        if (bRet) {
            mBlackListFile = file;
            mLastModifiedTime = blackListFile.lastModified();
        }
        return bRet;
    }

    public static boolean isInLogBlackList_static() {
        for (String packageName : mPackageNameList) {
            if (mLogBlackList.contains(packageName)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isInLogBlackList_static(String packageName) {
        return isInLogBlackList_static();
    }

    public static void initLogBlackList_static() {
        updateBlackList_static(getConfigFileUpdated());
    }

    private static int getConfigFileUpdated() {
        File blackListFile = new File(Environment.getDataDirectory().getPath() + "/system/hiview/log_blacklist.cfg");
        if (!blackListFile.isFile() || !blackListFile.canRead()) {
            if (mBlackListFile != 1) {
                return 1;
            }
            return 0;
        } else if (mBlackListFile == 2 && blackListFile.lastModified() == mLastModifiedTime) {
            return 0;
        } else {
            return 2;
        }
    }
}
