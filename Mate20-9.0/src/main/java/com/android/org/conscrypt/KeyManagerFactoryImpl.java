package com.android.org.conscrypt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactorySpi;
import javax.net.ssl.ManagerFactoryParameters;

public class KeyManagerFactoryImpl extends KeyManagerFactorySpi {
    private KeyStore keyStore;
    private char[] pwd;

    /* access modifiers changed from: protected */
    public void engineInit(KeyStore ks, char[] password) throws KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException {
        if (ks != null) {
            this.keyStore = ks;
            if (password != null) {
                this.pwd = (char[]) password.clone();
            } else {
                this.pwd = EmptyArray.CHAR;
            }
        } else {
            this.keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            String keyStoreName = System.getProperty("javax.net.ssl.keyStore");
            if (keyStoreName == null || keyStoreName.equalsIgnoreCase("NONE") || keyStoreName.isEmpty()) {
                try {
                    this.keyStore.load(null, null);
                } catch (IOException e) {
                    throw new KeyStoreException(e);
                } catch (CertificateException e2) {
                    throw new KeyStoreException(e2);
                }
            } else {
                String keyStorePwd = System.getProperty("javax.net.ssl.keyStorePassword");
                if (keyStorePwd == null) {
                    this.pwd = EmptyArray.CHAR;
                } else {
                    this.pwd = keyStorePwd.toCharArray();
                }
                try {
                    this.keyStore.load(new FileInputStream(new File(keyStoreName)), this.pwd);
                } catch (FileNotFoundException e3) {
                    throw new KeyStoreException(e3);
                } catch (IOException e4) {
                    throw new KeyStoreException(e4);
                } catch (CertificateException e5) {
                    throw new KeyStoreException(e5);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void engineInit(ManagerFactoryParameters spec) throws InvalidAlgorithmParameterException {
        throw new InvalidAlgorithmParameterException("ManagerFactoryParameters not supported");
    }

    /* access modifiers changed from: protected */
    public KeyManager[] engineGetKeyManagers() {
        if (this.keyStore != null) {
            return new KeyManager[]{new KeyManagerImpl(this.keyStore, this.pwd)};
        }
        throw new IllegalStateException("KeyManagerFactory is not initialized");
    }
}
