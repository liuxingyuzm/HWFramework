package jcifs.smb;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Vector;
import jcifs.Config;
import jcifs.UniAddress;
import jcifs.netbios.NbtAddress;
import jcifs.util.LogStream;

public final class SmbSession {
    private static final int CACHE_POLICY = (Config.getInt("jcifs.netbios.cachePolicy", 600) * 60);
    private static final String DOMAIN = Config.getProperty("jcifs.smb.client.domain", null);
    private static final String LOGON_SHARE = Config.getProperty("jcifs.smb.client.logonShare", null);
    private static final int LOOKUP_RESP_LIMIT = Config.getInt("jcifs.netbios.lookupRespLimit", 3);
    private static final String USERNAME = Config.getProperty("jcifs.smb.client.username", null);
    static NbtAddress[] dc_list = null;
    static int dc_list_counter;
    static long dc_list_expiration;
    private UniAddress address;
    NtlmPasswordAuthentication auth;
    int connectionState;
    long expiration;
    private InetAddress localAddr;
    private int localPort;
    String netbiosName = null;
    private int port;
    SmbTransport transport = null;
    Vector trees;
    int uid;

    private static NtlmChallenge interrogate(NbtAddress addr) throws SmbException {
        UniAddress dc = new UniAddress(addr);
        SmbTransport trans = SmbTransport.getSmbTransport(dc, 0);
        if (USERNAME == null) {
            trans.connect();
            LogStream logStream = SmbTransport.log;
            if (LogStream.level >= 3) {
                SmbTransport.log.println("Default credentials (jcifs.smb.client.username/password) not specified. SMB signing may not work propertly.  Skipping DC interrogation.");
            }
        } else {
            trans.getSmbSession(NtlmPasswordAuthentication.DEFAULT).getSmbTree(LOGON_SHARE, null).treeConnect(null, null);
        }
        return new NtlmChallenge(trans.server.encryptionKey, dc);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0058, code lost:
        return r9;
     */
    public static NtlmChallenge getChallengeForDomain() throws SmbException, UnknownHostException {
        if (DOMAIN == null) {
            throw new SmbException("A domain was not specified");
        }
        synchronized (DOMAIN) {
            long now = System.currentTimeMillis();
            int retry = 1;
            loop0:
            while (true) {
                int retry2 = retry;
                if (dc_list_expiration < now) {
                    NbtAddress[] list = NbtAddress.getAllByName(DOMAIN, 28, null, null);
                    dc_list_expiration = (((long) CACHE_POLICY) * 1000) + now;
                    if (list == null || list.length <= 0) {
                        dc_list_expiration = 900000 + now;
                        LogStream logStream = SmbTransport.log;
                        if (LogStream.level >= 2) {
                            SmbTransport.log.println("Failed to retrieve DC list from WINS");
                        }
                    } else {
                        dc_list = list;
                    }
                }
                int max = Math.min(dc_list.length, LOOKUP_RESP_LIMIT);
                int j = 0;
                while (j < max) {
                    int i = dc_list_counter;
                    dc_list_counter = i + 1;
                    int i2 = i % max;
                    if (dc_list[i2] != null) {
                        try {
                            NtlmChallenge interrogate = interrogate(dc_list[i2]);
                            break loop0;
                        } catch (SmbException se) {
                            LogStream logStream2 = SmbTransport.log;
                            if (LogStream.level >= 2) {
                                SmbTransport.log.println("Failed validate DC: " + dc_list[i2]);
                                LogStream logStream3 = SmbTransport.log;
                                if (LogStream.level > 2) {
                                    se.printStackTrace(SmbTransport.log);
                                }
                            }
                            dc_list[i2] = null;
                        }
                    } else {
                        j++;
                    }
                }
                dc_list_expiration = 0;
                retry = retry2 - 1;
                if (retry2 <= 0) {
                    dc_list_expiration = 900000 + now;
                    throw new UnknownHostException("Failed to negotiate with a suitable domain controller for " + DOMAIN);
                }
            }
        }
    }

    public static byte[] getChallenge(UniAddress dc) throws SmbException, UnknownHostException {
        return getChallenge(dc, 0);
    }

    public static byte[] getChallenge(UniAddress dc, int port2) throws SmbException, UnknownHostException {
        SmbTransport trans = SmbTransport.getSmbTransport(dc, port2);
        trans.connect();
        return trans.server.encryptionKey;
    }

    public static void logon(UniAddress dc, NtlmPasswordAuthentication auth2) throws SmbException {
        logon(dc, 0, auth2);
    }

    public static void logon(UniAddress dc, int port2, NtlmPasswordAuthentication auth2) throws SmbException {
        SmbTree tree = SmbTransport.getSmbTransport(dc, port2).getSmbSession(auth2).getSmbTree(LOGON_SHARE, null);
        if (LOGON_SHARE == null) {
            tree.treeConnect(null, null);
        } else {
            tree.send(new Trans2FindFirst2("\\", "*", 16), new Trans2FindFirst2Response());
        }
    }

    SmbSession(UniAddress address2, int port2, InetAddress localAddr2, int localPort2, NtlmPasswordAuthentication auth2) {
        this.address = address2;
        this.port = port2;
        this.localAddr = localAddr2;
        this.localPort = localPort2;
        this.auth = auth2;
        this.trees = new Vector();
        this.connectionState = 0;
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:13:?, code lost:
        r1 = new jcifs.smb.SmbTree(r4, r5, r6);
        r4.trees.addElement(r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x002a, code lost:
        r2 = r1;
     */
    public synchronized SmbTree getSmbTree(String share, String service) {
        SmbTree smbTree;
        if (share == null) {
            share = "IPC$";
        }
        Enumeration e = this.trees.elements();
        while (true) {
            if (!e.hasMoreElements()) {
                break;
            }
            SmbTree t = (SmbTree) e.nextElement();
            if (t.matches(share, service)) {
                smbTree = t;
                break;
            }
        }
        return smbTree;
    }

    /* access modifiers changed from: package-private */
    public boolean matches(NtlmPasswordAuthentication auth2) {
        return this.auth == auth2 || this.auth.equals(auth2);
    }

    /* access modifiers changed from: package-private */
    public synchronized SmbTransport transport() {
        if (this.transport == null) {
            this.transport = SmbTransport.getSmbTransport(this.address, this.port, this.localAddr, this.localPort, null);
        }
        return this.transport;
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    public void send(ServerMessageBlock request, ServerMessageBlock response) throws SmbException {
        synchronized (transport()) {
            if (response != null) {
                response.received = false;
            }
            this.expiration = System.currentTimeMillis() + ((long) SmbTransport.SO_TIMEOUT);
            sessionSetup(request, response);
            if (response == null || !response.received) {
                if (request instanceof SmbComTreeConnectAndX) {
                    SmbComTreeConnectAndX tcax = (SmbComTreeConnectAndX) request;
                    if (this.netbiosName != null && tcax.path.endsWith("\\IPC$")) {
                        tcax.path = "\\\\" + this.netbiosName + "\\IPC$";
                    }
                }
                request.uid = this.uid;
                request.auth = this.auth;
                try {
                    this.transport.send(request, response);
                } catch (SmbException se) {
                    if (request instanceof SmbComTreeConnectAndX) {
                        logoff(true);
                    }
                    request.digest = null;
                    throw se;
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:167:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:82:0x019f, code lost:
        r14 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:83:0x01a0, code lost:
        r5 = r6;
     */
    /* JADX WARNING: Removed duplicated region for block: B:82:0x019f A[Catch:{ SmbAuthException -> 0x01cb, SmbException -> 0x01cd, all -> 0x019f, SmbException -> 0x00c0, all -> 0x019f }, ExcHandler: all (th java.lang.Throwable), Splitter:B:33:0x00a3] */
    public void sessionSetup(ServerMessageBlock andx, ServerMessageBlock andxResponse) throws SmbException {
        SmbComSessionSetupAndX request;
        SmbComSessionSetupAndXResponse response;
        synchronized (transport()) {
            NtlmContext nctx = null;
            SmbException ex = null;
            byte[] token = new byte[0];
            int state = 10;
            while (this.connectionState != 0) {
                if (this.connectionState != 2 && this.connectionState != 3) {
                    try {
                        this.transport.wait();
                    } catch (InterruptedException ie) {
                        throw new SmbException(ie.getMessage(), (Throwable) ie);
                    }
                }
            }
            this.connectionState = 1;
            this.transport.connect();
            SmbTransport smbTransport = this.transport;
            LogStream logStream = SmbTransport.log;
            if (LogStream.level >= 4) {
                SmbTransport smbTransport2 = this.transport;
                SmbTransport.log.println("sessionSetup: accountName=" + this.auth.username + ",primaryDomain=" + this.auth.domain);
            }
            this.uid = 0;
            do {
                NtlmContext nctx2 = nctx;
                switch (state) {
                    case SmbConstants.DEFAULT_MAX_MPX_COUNT:
                        if (this.auth == NtlmPasswordAuthentication.ANONYMOUS || !this.transport.hasCapability(Integer.MIN_VALUE)) {
                            try {
                                request = new SmbComSessionSetupAndX(this, andx, this.auth);
                                response = new SmbComSessionSetupAndXResponse(andxResponse);
                                if (this.transport.isSignatureSetupRequired(this.auth)) {
                                    if (!this.auth.hashesExternal || NtlmPasswordAuthentication.DEFAULT_PASSWORD == "") {
                                        request.digest = new SigningDigest(this.auth.getSigningKey(this.transport.server.encryptionKey), false);
                                    } else {
                                        this.transport.getSmbSession(NtlmPasswordAuthentication.DEFAULT).getSmbTree(LOGON_SHARE, null).treeConnect(null, null);
                                    }
                                }
                                request.auth = this.auth;
                                this.transport.send(request, response);
                            } catch (SmbAuthException sae) {
                                throw sae;
                            } catch (SmbException se) {
                                ex = se;
                            } catch (SmbException e) {
                                se = e;
                                NtlmContext ntlmContext = nctx2;
                                break;
                            } catch (Throwable th) {
                            }
                            if (response.isLoggedInAsGuest && !"GUEST".equalsIgnoreCase(this.auth.username) && this.transport.server.security != 0 && this.auth != NtlmPasswordAuthentication.ANONYMOUS) {
                                throw new SmbAuthException(NtStatus.NT_STATUS_LOGON_FAILURE);
                            } else if (ex != null) {
                                throw ex;
                            } else {
                                this.uid = response.uid;
                                if (request.digest != null) {
                                    this.transport.digest = request.digest;
                                }
                                this.connectionState = 2;
                                state = 0;
                                nctx = nctx2;
                                continue;
                            }
                        } else {
                            state = 20;
                            nctx = nctx2;
                            continue;
                        }
                    case 20:
                        if (nctx2 == null) {
                            nctx = new NtlmContext(this.auth, (this.transport.flags2 & 4) != 0);
                        } else {
                            nctx = nctx2;
                        }
                        try {
                            LogStream logStream2 = SmbTransport.log;
                            if (LogStream.level >= 4) {
                                SmbTransport.log.println(nctx);
                            }
                            if (nctx.isEstablished()) {
                                this.netbiosName = nctx.getNetbiosName();
                                this.connectionState = 2;
                                state = 0;
                                continue;
                            } else {
                                token = nctx.initSecContext(token, 0, token.length);
                                if (token != null) {
                                    SmbComSessionSetupAndX request2 = new SmbComSessionSetupAndX(this, null, token);
                                    SmbComSessionSetupAndXResponse response2 = new SmbComSessionSetupAndXResponse(null);
                                    if (this.transport.isSignatureSetupRequired(this.auth)) {
                                        byte[] signingKey = nctx.getSigningKey();
                                        if (signingKey != null) {
                                            request2.digest = new SigningDigest(signingKey, true);
                                        }
                                    }
                                    request2.uid = this.uid;
                                    this.uid = 0;
                                    this.transport.send(request2, response2);
                                    if (response2.isLoggedInAsGuest && !"GUEST".equalsIgnoreCase(this.auth.username)) {
                                        throw new SmbAuthException(NtStatus.NT_STATUS_LOGON_FAILURE);
                                    } else if (ex != null) {
                                        throw ex;
                                    } else {
                                        this.uid = response2.uid;
                                        if (request2.digest != null) {
                                            this.transport.digest = request2.digest;
                                        }
                                        token = response2.blob;
                                        continue;
                                    }
                                } else {
                                    continue;
                                }
                            }
                        } catch (SmbAuthException sae2) {
                            throw sae2;
                        } catch (SmbException se2) {
                            ex = se2;
                            try {
                                this.transport.disconnect(true);
                            } catch (Exception e2) {
                            }
                        } catch (SmbException se3) {
                            try {
                                this.transport.disconnect(true);
                            } catch (IOException e3) {
                            }
                            this.uid = 0;
                            throw se3;
                        } catch (SmbException e4) {
                            se = e4;
                            break;
                        }
                        break;
                    default:
                        throw new SmbException("Unexpected session setup state: " + state);
                }
                try {
                    logoff(true);
                    this.connectionState = 0;
                    throw se;
                } catch (Throwable th2) {
                    th = th2;
                    this.transport.notifyAll();
                    throw th;
                }
            } while (state != 0);
            this.transport.notifyAll();
        }
    }

    /* access modifiers changed from: package-private */
    public void logoff(boolean inError) {
        synchronized (transport()) {
            if (this.connectionState == 2) {
                this.connectionState = 3;
                this.netbiosName = null;
                Enumeration e = this.trees.elements();
                while (e.hasMoreElements()) {
                    ((SmbTree) e.nextElement()).treeDisconnect(inError);
                }
                if (!inError) {
                    if (this.transport.server.security != 0) {
                        SmbComLogoffAndX request = new SmbComLogoffAndX(null);
                        request.uid = this.uid;
                        try {
                            this.transport.send(request, null);
                        } catch (SmbException e2) {
                        }
                        this.uid = 0;
                    }
                }
                this.connectionState = 0;
                this.transport.notifyAll();
            }
        }
    }

    public String toString() {
        return "SmbSession[accountName=" + this.auth.username + ",primaryDomain=" + this.auth.domain + ",uid=" + this.uid + ",connectionState=" + this.connectionState + "]";
    }
}
