//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package smartbi.sdk;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;
import java.util.zip.GZIPInputStream;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.pegasus.common.utils.JsonUtil;
import smartbi.SmartbiException;
import smartbi.net.sf.json.JSONArray;
import smartbi.net.sf.json.JSONObject;
import smartbi.util.CommonErrorCode;
import smartbi.util.StringUtil;

public class ClientConnector {
    private static boolean trustAny = true;
    private static SSLSocketFactory socketFactory;
    private static HostnameVerifier hostnameVerifier;
    private static Timer timer = null;
    private static List<WeakReference<ClientConnector>> newConns = new ArrayList();
    private static List<WeakReference<ClientConnector>> removedConns = new ArrayList();
    private static List<WeakReference<ClientConnector>> allConns = new LinkedList();
    protected String freequeryURL;
    protected String servletURL;
    private WeakReference<ClientConnector> ref;
    private boolean needNoopTask;
    protected Locale locale;
    protected String cookie;

    public static boolean getTrustAny() {
        return trustAny;
    }

    public static void setTrustAny(boolean trustAny) {
        ClientConnector.trustAny = trustAny;
    }

    public Locale getLocale() {
        return this.locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public ClientConnector(String freequeryURL) {
        this(freequeryURL, "/vision/RMIServlet", true);
    }

    public ClientConnector(String freequeryURL, String servletRelativePath, boolean needNoopTask) {
        this.locale = Locale.getDefault();
        if (freequeryURL != null) {
            freequeryURL = freequeryURL.trim().replaceAll("[/]+$", "");
            if (!"".equals(freequeryURL) && servletRelativePath != null && !"".equals(servletRelativePath.trim()) && !servletRelativePath.startsWith("/")) {
                servletRelativePath = "/" + servletRelativePath;
            }
        }

        this.freequeryURL = freequeryURL;
        this.servletURL = freequeryURL + servletRelativePath;
        this.needNoopTask = needNoopTask;
        if (needNoopTask && timer == null) {
            Class var4 = ClientConnector.class;
            synchronized (ClientConnector.class) {
                if (timer == null) {
                    timer = new Timer("ClientConnector.Noop", true);
                    timer.schedule(new ClientConnector.NoopTask(), new Date(), 120000L);
                }
            }
        }

    }

    public boolean open(String user, String password) {
        InvokeResult ret = null;
        ret = this.internalInvoke("UserService", "login", this.obj2JsonStr(new String[]{user, password}));
        if (ret != null && ret.isSucceed()) {
            if ((Boolean) ret.getResult()) {
                if (this.ref == null && this.needNoopTask) {
                    this.ref = new WeakReference(this);
                    synchronized (newConns) {
                        newConns.add(this.ref);
                    }
                }

                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean testConnection(String url, boolean needOk) {
        if (url == null) {
            url = this.servletURL;
        }

        if (url == null) {
            return false;
        } else {
            HttpURLConnection conn = null;

            try {
                conn = (HttpURLConnection) (new URL(url)).openConnection();
                this.setSSL(conn);
                boolean var4;
                if (needOk) {
                    var4 = conn.getResponseCode() == 200;
                    return var4;
                }

                var4 = conn.getResponseCode() != -1;
                return var4;
            } catch (IOException var15) {
            } finally {
                try {
                    if (conn != null) {
                        conn.disconnect();
                    }
                } catch (Exception var14) {
                }

            }

            return false;
        }
    }

    protected void setSSL(HttpURLConnection conn) {
        if (conn instanceof HttpsURLConnection && trustAny) {
            if (socketFactory == null) {
                try {
                    SSLContext sc = SSLContext.getInstance("SSL");
                    sc.init((KeyManager[]) null, new TrustManager[]{new ClientConnector.TrustAnyTrustManager()}, new SecureRandom());
                    socketFactory = sc.getSocketFactory();
                    hostnameVerifier = new ClientConnector.TrustAnyHostnameVerifier();
                } catch (Exception var3) {
                    throw new SmartbiException(CommonErrorCode.UNKOWN_ERROR, var3);
                }
            }

            HttpsURLConnection https = (HttpsURLConnection) conn;
            https.setSSLSocketFactory(socketFactory);
            https.setHostnameVerifier(hostnameVerifier);
        }

    }

    public boolean openFromDB(String user, String password) {
        InvokeResult ret = null;
        ret = this.internalInvoke("UserService", "loginFromDB", this.obj2JsonStr(new String[]{user, password}));
        if (ret != null && ret.isSucceed()) {
            if ((Boolean) ret.getResult()) {
                if (this.ref == null && this.needNoopTask) {
                    this.ref = new WeakReference(this);
                    synchronized (newConns) {
                        newConns.add(this.ref);
                    }
                }

                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean switchUser(String user) {
        InvokeResult ret = null;
        ret = this.internalInvoke("UserService", "switchUser", this.obj2JsonStr(new String[]{user}));
        if (ret != null && ret.isSucceed()) {
            return (Boolean) ret.getResult();
        } else {
            return false;
        }
    }

    public void close() {
        this.internalInvoke("UserService", "logout", this.obj2JsonStr(new String[0]));
        if (this.ref != null) {
            synchronized (removedConns) {
                removedConns.add(this.ref);
            }
        }

        this.ref = null;
        this.cookie = null;
    }

    public InvokeResult remoteInvoke(String classname, String method, Object[] params) {
        return this.internalInvoke(classname, method, this.obj2JsonStr(params));
    }

    public InvokeResult remoteMultipartInvoke(String classname, String method, Object[] params) {
        return this.internalMultipartInvoke(classname, method, this.obj2JsonStr(params));
    }

    public boolean download(String url, String postData, OutputStream os) {
        HttpURLConnection conn = null;

        boolean var6;
        try {
            if (url != null && !url.startsWith("/")) {
                url = "/" + url;
            }

            conn = (HttpURLConnection) (new URL(this.freequeryURL + url)).openConnection();
            conn.setInstanceFollowRedirects(false);
            if (this.cookie != null) {
                conn.setRequestProperty("Cookie", this.cookie);
            }

            conn.setDoInput(true);
            if (postData != null) {
                conn.setDoOutput(true);
                OutputStream connOut = conn.getOutputStream();
                connOut.write(postData.getBytes());
                connOut.flush();
                connOut.close();
            }

            if (conn.getResponseCode() == 302) {
                String location = conn.getHeaderField("Location");
                conn.disconnect();
                conn = (HttpURLConnection) (new URL(location)).openConnection();
                if (this.cookie != null) {
                    conn.setRequestProperty("Cookie", this.cookie);
                }
            }

            int BUF_SIZE = 1024;
            byte[] buf = new byte[BUF_SIZE];
            InputStream is = conn.getInputStream();

            while (true) {
                int count = is.read(buf, 0, BUF_SIZE);
                if (-1 == count) {
                    os.flush();
                    return true;
                }

                os.write(buf, 0, count);
            }
        } catch (Exception var17) {
            var17.printStackTrace();
            var6 = false;
        } finally {
            try {
                if (conn != null) {
                    conn.disconnect();
                }
            } catch (Exception var16) {
            }

        }

        return var6;
    }

    public void noop() {
        this.download("/vision/noop.jsp", (String) null, new ByteArrayOutputStream());
    }

    public boolean test() {
        return this.download("/vision/noop.jsp", (String) null, new ByteArrayOutputStream());
    }

    protected InvokeResult internalInvoke(String classname, String method, String params) {
        String postData = new String();
        postData = postData + "className=" + classname + "&methodName=" + method + "&params=";

        try {
            postData = postData + URLEncoder.encode(params, "UTF-8");
        } catch (UnsupportedEncodingException var31) {
            var31.printStackTrace();
        }

        HttpURLConnection conn = null;

        DataOutputStream out;
        try {
            URL u = new URL(this.servletURL);
            conn = (HttpURLConnection) u.openConnection();
            this.setSSL(conn);
            conn.setRequestProperty("Content-Type", "");
            conn.setRequestProperty("user-agent", "Mozilla/5.0");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=UTF-8");
            conn.setRequestMethod("POST");
            if (this.cookie != null) {
                conn.setRequestProperty("Cookie", this.cookie);
            }

            out = new DataOutputStream(conn.getOutputStream());
            out.writeBytes(postData);
            out.flush();
            String key;
            if (conn.getResponseCode() != 200) {
                InputStream is = conn.getErrorStream();
                byte[] bs = new byte[is.available()];
                is.read(bs);
                key = StringUtil.replaceLanguage("${InternalServerError}${Colon}", this.locale);
                throw new RemoteException(key + new String(bs));
            }

            StringBuilder cookieBuff = new StringBuilder();

            try {
                for (int i = 0; i < 100; ++i) {
                    key = conn.getHeaderFieldKey(i);
                    String value = conn.getHeaderField(i);
                    if ("Set-Cookie".equals(key)) {
                        cookieBuff.append(value).append(';');
                    }
                }
            } catch (ArrayIndexOutOfBoundsException var32) {
            }

            if (cookieBuff.length() > 0) {
                this.cookie = cookieBuff.toString();
            }

            String result = null;
            InputStream is = conn.getInputStream();
            int length = conn.getContentLength();
            byte[] buff;
            if ("gzip".equals(conn.getHeaderField("Content-Encoding"))) {
                if (length < 0) {
                    is = new GZIPInputStream((InputStream) is);
                } else {
                    buff = new byte[length];
                    (new DataInputStream((InputStream) is)).readFully(buff);
                    is = new GZIPInputStream(new ByteArrayInputStream(buff));
                    length = -1;
                }
            }

            if (length < 0) {
                Reader reader = new InputStreamReader((InputStream) is, "UTF-8");
                int BUF_SIZE = 1024;
                char[] buffer = new char[1024];
                StringBuffer stringBuffer = new StringBuffer(1024);

                while (true) {
                    int count = reader.read(buffer, 0, 1024);
                    if (-1 == count) {
                        result = stringBuffer.toString();
                        break;
                    }

                    stringBuffer.append(buffer, 0, count);
                }
            } else {
                buff = new byte[length];
                (new DataInputStream((InputStream) is)).readFully(buff);
                result = new String(buff, "UTF-8");
            }

            Map<String, Object> mapOrigin = JsonUtil.toObject(result, Map.class);
            JSONObject origin = new JSONObject();
            mapOrigin.forEach((k, v) -> {
                origin.put(k, v);
            });
            InvokeResult ret = new InvokeResult(origin);
            if (!ret.isSucceed()) {
                String msg;
                if (origin.has("detail") && origin.has("result")) {
                    msg = origin.getString("result") + ":" + origin.getString("detail");
                } else if (origin.has("detail")) {
                    msg = origin.getString("detail");
                } else {
                    msg = origin.getString("result");
                }

                String RemoteInvokeError = StringUtil.replaceLanguage("${RemoteInvokeError}${Colon}", this.locale);
                throw new RemoteException(RemoteInvokeError + msg);
            }

            InvokeResult var45 = ret;
            return var45;
        } catch (MalformedURLException var33) {
            var33.printStackTrace();
            out = null;
            return null;
        } catch (ProtocolException var34) {
            var34.printStackTrace();
            out = null;
            return null;
        } catch (IOException var35) {
            var35.printStackTrace();
            out = null;
        } finally {
            try {
                if (conn != null) {
                    conn.disconnect();
                }
            } catch (Exception var30) {
            }

        }

        return null;
    }

    private InvokeResult internalMultipartInvoke(String classname, String method, String params) {
        HttpURLConnection conn = null;

        String boundary;
        try {
            URL u = new URL(this.servletURL);
            conn = (HttpURLConnection) u.openConnection();
            this.setSSL(conn);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setDefaultUseCaches(false);
            conn.setRequestProperty("Accept", "*/*");
            boundary = "--------------------" + Long.toString(System.currentTimeMillis(), 16);
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Cache-Control", "no-cache");
            if (this.cookie != null) {
                conn.setRequestProperty("Cookie", this.cookie);
            }

            DataOutputStream out = new DataOutputStream(conn.getOutputStream());
            this.writeField(out, boundary, "className", classname);
            this.writeField(out, boundary, "methodName", method);
            this.writeField(out, boundary, "params", params);
            out.writeBytes("--");
            out.writeBytes(boundary);
            out.writeBytes("--");
            out.writeBytes("\r\n");
            out.flush();
            out.close();
            String key;
            if (conn.getResponseCode() == 500) {
                InputStream is = conn.getErrorStream();
                byte[] bs = new byte[is.available()];
                is.read(bs);
                key = StringUtil.replaceLanguage("${InternalServerError}${Colon}", this.locale);
                throw new RemoteException(key + new String(bs));
            }

            StringBuilder cookieBuff = new StringBuilder();

            try {
                for (int i = 0; i < 100; ++i) {
                    key = conn.getHeaderFieldKey(i);
                    String value = conn.getHeaderField(i);
                    if ("Set-Cookie".equals(key)) {
                        cookieBuff.append(value).append(';');
                    }
                }
            } catch (ArrayIndexOutOfBoundsException var30) {
            }

            if (cookieBuff.length() > 0) {
                this.cookie = cookieBuff.toString();
            }

            String result = null;
            InputStream is = conn.getInputStream();
            int length = conn.getContentLength();
            if (length >= 0) {
                byte[] buff = new byte[length];
                (new DataInputStream(is)).readFully(buff);
                result = new String(buff, "UTF-8");
            } else {
                Reader reader = new InputStreamReader(is, "UTF-8");
                int BUF_SIZE = 1024;
                char[] buffer = new char[1024];
                StringBuffer stringBuffer = new StringBuffer(1024);

                while (true) {
                    int count = reader.read(buffer, 0, 1024);
                    if (-1 == count) {
                        result = stringBuffer.toString();
                        break;
                    }

                    stringBuffer.append(buffer, 0, count);
                }
            }

            JSONObject origin = JSONObject.fromString(result);
            InvokeResult ret = new InvokeResult(origin);
            if (!ret.isSucceed()) {
                String msg;
                if (origin.has("detail") && origin.has("result")) {
                    msg = origin.getString("result") + ":" + origin.getString("detail");
                } else if (origin.has("detail")) {
                    msg = origin.getString("detail");
                } else {
                    msg = origin.getString("result");
                }

                String RemoteInvokeError = StringUtil.replaceLanguage("${RemoteInvokeError}${Colon}", this.locale);
                throw new RemoteException(RemoteInvokeError + msg);
            }

            InvokeResult var43 = ret;
            return var43;
        } catch (MalformedURLException var31) {
            var31.printStackTrace();
            boundary = null;
            return null;
        } catch (ProtocolException var32) {
            var32.printStackTrace();
            boundary = null;
            return null;
        } catch (IOException var33) {
            var33.printStackTrace();
            boundary = null;
        } finally {
            try {
                if (conn != null) {
                    conn.disconnect();
                }
            } catch (Exception var29) {
            }

        }

        return null;
    }

    private void writeField(DataOutputStream out, String boundary, String name, String value) throws IOException {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null or empty.");
        } else {
            if (value == null) {
                value = "";
            }

            out.writeBytes("--");
            out.writeBytes(boundary);
            out.writeBytes("\r\n");
            out.writeBytes("Content-Type: application/octet-stream; charset=UTF-8\r\n");
            out.writeBytes("Content-Disposition: form-data; name=\"" + name + "\"");
            out.writeBytes("\r\n");
            out.writeBytes("\r\n");
            out.write(value.getBytes("UTF-8"));
            out.writeBytes("\r\n");
            out.flush();
        }
    }

    protected String obj2JsonStr(Object obj) {
//        String s = null;
//        if (!(obj instanceof Collection) && !obj.getClass().isArray()) {
//            if (obj instanceof Boolean || obj instanceof Number || obj instanceof String) {
//                s = JSONArray.fromObject(new Object[]{obj}).toString();
//            }
//        } else {
//            s = JSONArray.fromObject(obj).toString();
//        }
        return JsonUtil.toString(obj);
//        return s;
    }

    public String getCookie() {
        return this.cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public static void shutdown() {
        if (timer != null) {
            timer.cancel();
        }

        timer = null;
    }

    private static class NoopTask extends TimerTask {
        private NoopTask() {
        }

        public void run() {
            synchronized (ClientConnector.removedConns) {
                ClientConnector.allConns.removeAll(ClientConnector.removedConns);
            }

            Iterator it = ClientConnector.allConns.iterator();

            while (it.hasNext()) {
                WeakReference<ClientConnector> ref = (WeakReference) it.next();
                ClientConnector conn = (ClientConnector) ref.get();
                if (conn == null) {
                    it.remove();
                } else {
                    conn.noop();
                }
            }

            synchronized (ClientConnector.newConns) {
                ClientConnector.allConns.addAll(ClientConnector.newConns);
                ClientConnector.newConns.clear();
            }
        }
    }

    private static class TrustAnyHostnameVerifier implements HostnameVerifier {
        private TrustAnyHostnameVerifier() {
        }

        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    private static class TrustAnyTrustManager implements X509TrustManager {
        private TrustAnyTrustManager() {
        }

        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }
}
