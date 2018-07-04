package org.sweetycode.workpanel.util;

import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class HttpClientUtil {
    public static String doPost(String url, String body, Header[] headers, CloseableHttpClient client)
            throws IOException {
        HttpEntity httpEntity = new StringEntity(body);
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(httpEntity);
        if (headers != null) {
            httpPost.setHeaders(headers);
        }
        CloseableHttpResponse response = client.execute(httpPost);
        try {
            validateStatus(response);
            HttpEntity responseEntity = response.getEntity();
            return getContent(responseEntity);
        } finally {
            response.close();
        }
    }

    public static String doGet(String url, Map<String, String> param) throws IOException {
        CloseableHttpClient client = getHttpClient();
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = client.execute(httpGet);
        try {
            validateStatus(response);
            HttpEntity entity = response.getEntity();
            return getContent(entity);
        } finally {
            response.close();
            close(client);
        }
    }

    private static void validateStatus(HttpResponse response) throws IOException {
        int status = response.getStatusLine().getStatusCode();
        if (status < 200 || status >= 300) {
            throw new IOException("Unexpected response status: " + status);
        }
    }

    public static void close(CloseableHttpClient client) {
        if (client != null) {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static CloseableHttpClient getHttpClient() {
        return HttpClients.createDefault();
    }

    private static String getContent(HttpEntity entity) throws IOException {
        InputStream is = entity.getContent();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int i = -1;
        while ((i = is.read()) != -1) {
            baos.write(i);
        }
        is.close();
        baos.flush();
        baos.close();
        return baos.toString();
    }
}