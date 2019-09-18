package com.android.server.job;

import android.app.IActivityManager;
import android.content.ClipData;
import android.content.ContentProvider;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.UserHandle;
import android.util.Slog;
import android.util.proto.ProtoOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

public final class GrantedUriPermissions {
    private final int mGrantFlags;
    private final IBinder mPermissionOwner;
    private final int mSourceUserId;
    private final String mTag;
    private final ArrayList<Uri> mUris = new ArrayList<>();

    private GrantedUriPermissions(IActivityManager am, int grantFlags, int uid, String tag) throws RemoteException {
        this.mGrantFlags = grantFlags;
        this.mSourceUserId = UserHandle.getUserId(uid);
        this.mTag = tag;
        this.mPermissionOwner = am.newUriPermissionOwner("job: " + tag);
    }

    public void revoke(IActivityManager am) {
        for (int i = this.mUris.size() - 1; i >= 0; i--) {
            try {
                am.revokeUriPermissionFromOwner(this.mPermissionOwner, this.mUris.get(i), this.mGrantFlags, this.mSourceUserId);
            } catch (RemoteException e) {
            }
        }
        this.mUris.clear();
    }

    public static boolean checkGrantFlags(int grantFlags) {
        return (grantFlags & 3) != 0;
    }

    public static GrantedUriPermissions createFromIntent(IActivityManager am, Intent intent, int sourceUid, String targetPackage, int targetUserId, String tag) {
        int grantFlags = intent.getFlags();
        if (!checkGrantFlags(grantFlags)) {
            return null;
        }
        GrantedUriPermissions perms = null;
        Uri data = intent.getData();
        if (data != null) {
            perms = grantUri(am, data, sourceUid, targetPackage, targetUserId, grantFlags, tag, null);
        }
        ClipData clip = intent.getClipData();
        if (clip != null) {
            perms = grantClip(am, clip, sourceUid, targetPackage, targetUserId, grantFlags, tag, perms);
        }
        return perms;
    }

    public static GrantedUriPermissions createFromClip(IActivityManager am, ClipData clip, int sourceUid, String targetPackage, int targetUserId, int grantFlags, String tag) {
        if (!checkGrantFlags(grantFlags)) {
            return null;
        }
        GrantedUriPermissions perms = null;
        if (clip != null) {
            perms = grantClip(am, clip, sourceUid, targetPackage, targetUserId, grantFlags, tag, null);
        }
        return perms;
    }

    private static GrantedUriPermissions grantClip(IActivityManager am, ClipData clip, int sourceUid, String targetPackage, int targetUserId, int grantFlags, String tag, GrantedUriPermissions curPerms) {
        int N = clip.getItemCount();
        GrantedUriPermissions curPerms2 = curPerms;
        for (int i = 0; i < N; i++) {
            curPerms2 = grantItem(am, clip.getItemAt(i), sourceUid, targetPackage, targetUserId, grantFlags, tag, curPerms2);
        }
        ClipData clipData = clip;
        return curPerms2;
    }

    private static GrantedUriPermissions grantUri(IActivityManager am, Uri uri, int sourceUid, String targetPackage, int targetUserId, int grantFlags, String tag, GrantedUriPermissions curPerms) {
        GrantedUriPermissions curPerms2;
        int i;
        int i2;
        IActivityManager iActivityManager;
        try {
            try {
                int sourceUserId = ContentProvider.getUserIdFromUri(uri, UserHandle.getUserId(sourceUid));
                Uri uri2 = ContentProvider.getUriWithoutUserId(uri);
                if (curPerms == null) {
                    iActivityManager = am;
                    i2 = sourceUid;
                    i = grantFlags;
                    try {
                        curPerms2 = new GrantedUriPermissions(iActivityManager, i, i2, tag);
                    } catch (RemoteException e) {
                        curPerms2 = curPerms;
                        Slog.e(JobSchedulerService.TAG, "AM dead");
                        return curPerms2;
                    }
                } else {
                    iActivityManager = am;
                    i2 = sourceUid;
                    i = grantFlags;
                    String str = tag;
                    curPerms2 = curPerms;
                }
                try {
                    iActivityManager.grantUriPermissionFromOwner(curPerms2.mPermissionOwner, i2, targetPackage, uri2, i, sourceUserId, targetUserId);
                    curPerms2.mUris.add(uri2);
                } catch (RemoteException e2) {
                }
            } catch (RemoteException e3) {
                IActivityManager iActivityManager2 = am;
                int i3 = sourceUid;
                int i4 = grantFlags;
                String str2 = tag;
                curPerms2 = curPerms;
                Slog.e(JobSchedulerService.TAG, "AM dead");
                return curPerms2;
            }
        } catch (RemoteException e4) {
            IActivityManager iActivityManager3 = am;
            Uri uri3 = uri;
            int i32 = sourceUid;
            int i42 = grantFlags;
            String str22 = tag;
            curPerms2 = curPerms;
            Slog.e(JobSchedulerService.TAG, "AM dead");
            return curPerms2;
        }
        return curPerms2;
    }

    private static GrantedUriPermissions grantItem(IActivityManager am, ClipData.Item item, int sourceUid, String targetPackage, int targetUserId, int grantFlags, String tag, GrantedUriPermissions curPerms) {
        GrantedUriPermissions curPerms2;
        if (item.getUri() != null) {
            curPerms2 = grantUri(am, item.getUri(), sourceUid, targetPackage, targetUserId, grantFlags, tag, curPerms);
        } else {
            curPerms2 = curPerms;
        }
        Intent intent = item.getIntent();
        if (intent == null || intent.getData() == null) {
            return curPerms2;
        }
        return grantUri(am, intent.getData(), sourceUid, targetPackage, targetUserId, grantFlags, tag, curPerms2);
    }

    public void dump(PrintWriter pw, String prefix) {
        pw.print(prefix);
        pw.print("mGrantFlags=0x");
        pw.print(Integer.toHexString(this.mGrantFlags));
        pw.print(" mSourceUserId=");
        pw.println(this.mSourceUserId);
        pw.print(prefix);
        pw.print("mTag=");
        pw.println(this.mTag);
        pw.print(prefix);
        pw.print("mPermissionOwner=");
        pw.println(this.mPermissionOwner);
        for (int i = 0; i < this.mUris.size(); i++) {
            pw.print(prefix);
            pw.print("#");
            pw.print(i);
            pw.print(": ");
            pw.println(this.mUris.get(i));
        }
    }

    public void dump(ProtoOutputStream proto, long fieldId) {
        long token = proto.start(fieldId);
        proto.write(1120986464257L, this.mGrantFlags);
        proto.write(1120986464258L, this.mSourceUserId);
        proto.write(1138166333443L, this.mTag);
        proto.write(1138166333444L, this.mPermissionOwner.toString());
        for (int i = 0; i < this.mUris.size(); i++) {
            Uri u = this.mUris.get(i);
            if (u != null) {
                proto.write(2237677961221L, u.toString());
            }
        }
        proto.end(token);
    }
}
