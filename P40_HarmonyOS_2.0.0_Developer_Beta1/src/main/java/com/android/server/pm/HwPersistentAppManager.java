package com.android.server.pm;

import android.content.pm.PackageParserEx;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import com.huawei.android.os.SystemPropertiesEx;
import com.huawei.android.util.SlogEx;
import com.huawei.hwpartbasicplatformservices.BuildConfig;
import huawei.cust.HwCfgFilePolicy;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public final class HwPersistentAppManager {
    private static final boolean IS_PERSISTENT_CONFIG_DISABLED = "1".equals(SystemPropertiesEx.get("ro.hw_persistent.disable", "0"));
    private static final String PERSISTENT_CONFIG_FILE_PATH = "xml/hw_persistent_config.xml";
    private static final String TAG = "HwPersistentAppManager";
    private static Map<String, PersistentInfo> sPersistentConfigMap = null;

    private HwPersistentAppManager() {
    }

    /* access modifiers changed from: private */
    public static final class PersistentInfo {
        private String mOriginal;
        private String mPackageName;
        private String mPersistent;
        private String mUpdatable;

        private PersistentInfo() {
            this.mPackageName = BuildConfig.FLAVOR;
            this.mOriginal = BuildConfig.FLAVOR;
            this.mPersistent = BuildConfig.FLAVOR;
            this.mUpdatable = BuildConfig.FLAVOR;
        }

        public String toString() {
            return "packageName:" + this.mPackageName + " original:" + this.mOriginal + " persistent:" + this.mPersistent + " updatable:" + this.mUpdatable;
        }
    }

    private static ArrayList<File> getPersistentConfigFileList(String filePath) {
        ArrayList<File> fileList = new ArrayList<>();
        if (TextUtils.isEmpty(filePath)) {
            Log.e(TAG, "Error: file = [" + filePath + "]");
            return fileList;
        }
        String[] policyDirs = null;
        try {
            policyDirs = HwCfgFilePolicy.getCfgPolicyDir(0);
        } catch (NoClassDefFoundError e) {
            SlogEx.e(TAG, "HwCfgFilePolicy NoClassDefFoundError");
        }
        if (policyDirs == null) {
            return fileList;
        }
        for (int i = 0; i < policyDirs.length; i++) {
            File file = new File(policyDirs[i], filePath);
            if (file.exists()) {
                fileList.add(file);
                SlogEx.d(TAG, "getPersistentConfigFileList from i=" + i + "| " + file.getAbsolutePath());
            }
        }
        if (fileList.size() == 0) {
            Log.w(TAG, "No persistent config file found for:" + filePath);
        }
        return fileList;
    }

    private static HashMap<String, PersistentInfo> loadPersistentConfigInfo() {
        List<File> fileList = null;
        try {
            fileList = getPersistentConfigFileList(PERSISTENT_CONFIG_FILE_PATH);
        } catch (NoClassDefFoundError er) {
            SlogEx.e(TAG, er.getMessage());
        }
        HashMap<String, PersistentInfo> persistentConfigMap = new HashMap<>();
        if (fileList == null || fileList.size() == 0) {
            return persistentConfigMap;
        }
        int fileSize = fileList.size();
        for (int i = 0; i < fileSize; i++) {
            File file = fileList.get(i);
            if (file != null && file.exists()) {
                persistentConfigMap.putAll(readPersistentConfigFile(file));
            }
        }
        return persistentConfigMap;
    }

    private static HashMap<String, PersistentInfo> readPersistentConfigFile(File file) {
        HashMap<String, PersistentInfo> result = new HashMap<>();
        if (file == null || !file.exists()) {
            return result;
        }
        FileInputStream stream = null;
        try {
            FileInputStream stream2 = new FileInputStream(file);
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(stream2, null);
            int type = parser.next();
            while (type != 1 && type != 2) {
                type = parser.next();
            }
            String tag = parser.getName();
            if ("persistent-config".equals(tag)) {
                parser.next();
                int outerDepth = parser.getDepth();
                while (true) {
                    int type2 = parser.next();
                    if (type2 == 1 || (type2 == 3 && parser.getDepth() <= outerDepth)) {
                        try {
                            stream2.close();
                            break;
                        } catch (IOException e) {
                            SlogEx.e(TAG, "close stream Exception");
                        }
                    } else if (type2 != 3) {
                        if (type2 != 4) {
                            getPersistentItem(result, parser);
                        }
                    }
                }
                return result;
            }
            throw new XmlPullParserException("Settings do not start with policies tag: found " + tag);
        } catch (FileNotFoundException e2) {
            SlogEx.e(TAG, "can not open config file: " + file);
            if (0 != 0) {
                stream.close();
            }
        } catch (XmlPullParserException e3) {
            SlogEx.e(TAG, "failed parsing: " + file);
            if (0 != 0) {
                stream.close();
            }
        } catch (Exception e4) {
            SlogEx.e(TAG, "readPersistentConfigFile, failed parsing " + file + " catch Exception");
            if (0 != 0) {
                stream.close();
            }
        } catch (Throwable th) {
            if (0 != 0) {
                try {
                    stream.close();
                } catch (IOException e5) {
                    SlogEx.e(TAG, "close stream Exception");
                }
            }
            throw th;
        }
    }

    private static void getPersistentItem(HashMap<String, PersistentInfo> result, XmlPullParser parser) {
        if ("item".equals(parser.getName())) {
            PersistentInfo info = new PersistentInfo();
            info.mPackageName = parser.getAttributeValue(null, "package");
            if (!TextUtils.isEmpty(info.mPackageName)) {
                info.mPackageName = info.mPackageName.intern();
            }
            info.mOriginal = parser.getAttributeValue(null, "original");
            info.mPersistent = parser.getAttributeValue(null, "persistent");
            info.mUpdatable = parser.getAttributeValue(null, "updatable");
            result.put(info.mPackageName, info);
            SlogEx.d(TAG, info.toString());
        }
    }

    private static boolean fixPkgPersistentFlag(PackageParserEx.PackageEx pkg) {
        Map<String, PersistentInfo> map = sPersistentConfigMap;
        if (map == null) {
            pkg.getApplicationInfo().flags &= -9;
            return true;
        }
        PersistentInfo info = map.get(pkg.getPackageName());
        if (info != null && ("true".equals(info.mOriginal) || "true".equals(info.mPersistent))) {
            return false;
        }
        pkg.getApplicationInfo().flags &= -9;
        return true;
    }

    public static void readPersistentConfig() {
        if (!IS_PERSISTENT_CONFIG_DISABLED) {
            sPersistentConfigMap = loadPersistentConfigInfo();
        }
    }

    public static void resolvePersistentFlagForPackage(int oldFlags, PackageParserEx.PackageEx pkg) {
        if (!IS_PERSISTENT_CONFIG_DISABLED && oldFlags != Integer.MIN_VALUE && pkg != null && pkg.getApplicationInfo() != null) {
            boolean isOldNonPersistent = true;
            boolean newPersistent = (pkg.getApplicationInfo().flags & 8) != 0;
            if (newPersistent) {
                if ((oldFlags & 8) != 0) {
                    isOldNonPersistent = false;
                }
                if (isOldNonPersistent && newPersistent && fixPkgPersistentFlag(pkg)) {
                    SlogEx.i(TAG, pkg.getPackageName() + " does not allow to become a persistent app since old app is not a persistent app!");
                }
            }
        }
    }

    public static boolean isPersistentUpdatable(PackageParserEx.PackageEx pkg) {
        Map<String, PersistentInfo> map;
        PersistentInfo info;
        if (IS_PERSISTENT_CONFIG_DISABLED || pkg == null || (map = sPersistentConfigMap) == null || (info = map.get(pkg.getPackageName())) == null || !"true".equals(info.mUpdatable)) {
            return false;
        }
        SlogEx.i(TAG, pkg.getPackageName() + " is marked as a updatable persistent app!");
        return true;
    }
}
