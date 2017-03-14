
package com.hhkj.talkdata.network.netlayer.base.http;


import android.util.Log;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class HttpsBuilder {

    /**
     * 请求地址
     *
     * @param reqUrl
     * @return
     */
    public static HttpsURLConnection getHttpsURLConnection(String reqUrl) {
        HttpsURLConnection httpsConn = null;
        try {
            URL url = new URL(reqUrl);
            // 信任所有链接
            SSLContext sslctxt = SSLContext.getInstance("TLS");
            sslctxt.init(null, new TrustManager[]{new X509_Trust_Manager()}, new SecureRandom());
            httpsConn = (HttpsURLConnection) url.openConnection();
            httpsConn.setSSLSocketFactory(sslctxt.getSocketFactory());
            httpsConn.setHostnameVerifier(new Hostname_Verifier());
            // 加载证书
//            CertificateFactory cf = CertificateFactory.getInstance("X.509");
//            InputStream in = context.getAssets().open("rt.fund.crt");
//            Certificate ca = cf.generateCertificate(in);
//
//            KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
//            keystore.load(null, null);
//            keystore.setCertificateEntry("ca", ca);
//
//            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
//            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
//            tmf.init(keystore);
//
//            SSLContext sslctxt = SSLContext.getInstance("TLS");
//            sslctxt.init(null, tmf.getTrustManagers(), null);
//
//            httpsConn = (HttpsURLConnection) url.openConnection();
//            httpsConn.setSSLSocketFactory(sslctxt.getSocketFactory());
        } catch (MalformedURLException e) {
            Log.d(HttpsBuilder.class.getName(),e.getMessage());
        } catch (IOException e) {
            Log.d(HttpsBuilder.class.getName(),e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            Log.d(HttpsBuilder.class.getName(),e.getMessage());
        } catch (KeyManagementException e) {
            Log.d(HttpsBuilder.class.getName(),e.getMessage());
        }
        return httpsConn;
    }


    /**
     * 实现x509证书认证
     */
    private static class X509_Trust_Manager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            // 是否信任服务器
//            Principal principal = null;
//            boolean isOK = false;
//            for (X509Certificate x509Certificate : chain) {
//                principal = x509Certificate.getSubjectDN();
//                if (principal != null && (principal.getName().indexOf("www.redfund365.com") != -1)) {
//                    isOK = true;
//                    break;
//                }
//            }
//            if (isOK == false) {
//                return;
//            }
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }

    /**
     * 要实现主机名验证
     */
    private static class Hostname_Verifier implements HostnameVerifier {

        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }

    }
}