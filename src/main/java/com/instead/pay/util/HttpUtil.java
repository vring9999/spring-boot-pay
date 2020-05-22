package com.instead.pay.util;

import com.instead.pay.superpay.service.AsyncCallBack;
import okhttp3.*;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.Map;

import okhttp3.FormBody.Builder;

import org.apache.commons.lang.StringUtils;

import java.util.Set;
import java.util.concurrent.TimeUnit;
/**
 * @author vring
 * @ClassName HttpUtil.java
 * @Description
 * @createTime 2019/12/5 11:23
 */
public class HttpUtil {

    private static final String CONTENT_TYPE = "application/json; charset=utf-8";
    private static final String CONTENT_FORM_TYPE = "application/x-www-form-urlencoded;charset=utf-8";
    private static final String FILE_CONTENT_TYPE = "application/octet-stream";
    private static final String ACCEPT = "application/json";
    private static final String ACCEPT_FORM = "application/x-www-form-urlencoded";
    private static final MediaType MEDIA_TYPE_JSON = MediaType.parse(CONTENT_TYPE);
    private static final MediaType MEDIA_TYPE_FILE = MediaType.parse(FILE_CONTENT_TYPE);
    private static OkHttpClient client = null;

    protected HttpServletRequest request;

    protected HttpServletResponse response;

    private HttpSession session;

    public HttpSession getSession() {
        return session;
    }

    public void setSession(HttpServletRequest request) {
        this.session = request.getSession(true);
    }

    @ModelAttribute
    public void setReqAndRes(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
        this.session = request.getSession();
    }

    //获取当前登录操作人的账号
    public  String getOperaterUseName() {
        return (String)this.getSession().getAttribute("user");
    }


    static {
        int cacheSize = 10 * 1024 * 1024; // 10MB
        File cacheDirectory = null;
        try {
            cacheDirectory = Files.createTempDirectory("cache").toFile();
            Cache cache = new Cache(cacheDirectory, cacheSize);
            OkHttpClient.Builder httpBuilder = new OkHttpClient().newBuilder();
            client = httpBuilder.connectTimeout(10, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS)
                    .cache(cache) // 设置超时时间
                    .build();
        } catch (IOException e) {
            System.out.println("IO异常");
        }
    }

    /**
     * 同步get请求
     * @param url
     * @return
     * @throws IOException
     * @
     */
    public static String syncGet(String url) throws IOException  {
        System.out.println("同步get请求："+url);
        Request request = new Request.Builder().url(url).get().build();
        String result = execute(request);
        return result;
    }
    /**
     * 异步get请求
     * @param url
     * @param callBack
     * @
     */
    public static void asyncGet(String url, Callback callBack)  {
        System.out.println("异步get请求："+url);
        Request request = new Request.Builder().url(url).get().build();
        enqueue(request, callBack);
    }
    public static String syncPostForm(String url, Map<String, String> params) throws Exception  {
        System.out.println("同步post请求："+url);
        Request.Builder requestBuilder = new Request.Builder().url(url);
        if (params != null && !params.isEmpty()) {
            Builder formBuilder = new FormBody.Builder();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                System.out.println("请求参数："+entry.getKey()+"="+entry.getValue());
                formBuilder.add(entry.getKey(), entry.getValue());
            }
            RequestBody formBody = formBuilder.build();
            requestBuilder.post(formBody);
        }
        Request request = requestBuilder.build();
        String result = execute(request);
        return result;
    }


    /**
     * 发送 http post 请求，参数以原生字符串进行提交
     * @param url
     * @param encode
     * @return
     */
    public static String httpPostRaw(String url,String stringJson,Map<String,String> headers, String encode)throws IOException {
        if(encode == null){
            encode = "utf-8";
        }
        System.out.println("请求地址："+url);
        Request.Builder requestBuilder = new Request.Builder().url(url);
        if (headers != null && !headers.isEmpty()) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                requestBuilder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        if (!StringUtil.isEmpty(stringJson)) {
            requestBuilder.post(RequestBody.create(MEDIA_TYPE_JSON, stringJson));
        }else {
            requestBuilder.post(RequestBody.create(null, ""));
        }

        Request request = requestBuilder.build();
        String result = execute(request);
        return result;

    }



    /**
     * post对象
     * @param url
     * @param postBody
     * @return
     * @throws IOException
     * @
     */
    public static String syncPostJson(String url, String postBody) throws IOException  {
        System.out.println("同步post请求："+url);
        System.out.println("请求参数："+postBody);
        Request request = new Request.Builder().url(url).addHeader("Content-type", CONTENT_TYPE).addHeader("Accept", ACCEPT)
                .post(RequestBody.create(MEDIA_TYPE_JSON, postBody)).build();
        String result = execute(request);
        return result;
    }


    /**
     * post对象
     * @param url
     * @param data
     * @return
     * @throws IOException
     * @
     */
    public static String syncPostForm(String url,NameValuePair[] data ) throws Exception  {
        System.out.println("同步post请求："+url);
        System.out.println("请求参数："+ GsonUtil.boToString(data));
            PostMethod postMethod = new PostMethod(url) ;
            postMethod.setRequestHeader("Content-Type", CONTENT_FORM_TYPE) ;
            //参数设置，需要注意的就是里边不能传NULL，要传空字符串
//            NameValuePair[] data = {
//                    new NameValuePair("startTime",""),
//                    new NameValuePair("endTime","")
//            };
        postMethod.setRequestBody(data);
        org.apache.commons.httpclient.HttpClient httpClient = new org.apache.commons.httpclient.HttpClient();
        int response = httpClient.executeMethod(postMethod); // 执行POST方法
        String result = postMethod.getResponseBodyAsString() ;
        return result;
    }




    /**
     * put对象
     * @param url
     * @param json
     * @return
     * @throws IOException
     * @
     */
    public static String syncPutJson(String url, String json) throws IOException  {
        System.out.println("同步put请求："+url);
        System.out.println("请求参数："+json);
        Request request = new Request.Builder().url(url).addHeader("Content-type", CONTENT_TYPE).addHeader("Accept", ACCEPT)
                .put(RequestBody.create(MEDIA_TYPE_JSON, json)).build();
        String result = execute(request);
        return result;
    }


    public static String syncPostFormNew(String url, Map<String, String> params) throws IOException  {
        System.out.println("同步post请求："+url);
        Request.Builder requestBuilder = new Request.Builder().url(url);
        if (params != null && !params.isEmpty()) {
            Builder formBuilder = new FormBody.Builder();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                System.out.println("请求参数："+entry.getKey()+"="+entry.getValue());
                formBuilder.add(entry.getKey(), entry.getValue());
            }
            RequestBody formBody = formBuilder.build();
            requestBuilder.post(formBody);
        }
        Request request = requestBuilder.build();
        String result = execute(request);
        return result;
    }


    public static String syncPutJsonWithHeader(String url,Map<String,String> headers, String json) throws IOException  {
        System.out.println("同步put请求："+url);
        System.out.println("请求参数："+json);

        Request.Builder requestBuilder = new Request.Builder().url(url);
        if (headers != null && !headers.isEmpty()) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                requestBuilder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        requestBuilder.put(RequestBody.create(MEDIA_TYPE_JSON, json));
        Request request = requestBuilder.build();
        String result = execute(request);
        return result;
    }


    /**
     * delete对象
     * @param url
     * @return
     * @throws IOException
     * @
     */
    public static String syncDelete(String url) throws IOException  {
        Request request = new Request.Builder().url(url).delete().build();
        String result = execute(request);
        return result;
    }


    public static String syncDeleteWithHeaders(String url,Map<String,String>header) throws IOException  {
        Request.Builder requestBuilder = new Request.Builder().url(url);
        if (header != null && !header.isEmpty()) {
            for (Map.Entry<String, String> entry : header.entrySet()) {
                requestBuilder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        Request request = requestBuilder.delete().build();
        String result = execute(request);
        return result;
    }


    /**
     * 文件上传
     * @param url 服务端路径
     * @param files 文件列表
     * @return
     * @throws IOException
     * @
     */
    public static String syncUploadFile(String url, File[] files) throws IOException  {
        MultipartBody.Builder formBuilder = null;
        String result = "";
        for (File file : files) {
            RequestBody fileBody = RequestBody.create(MEDIA_TYPE_FILE, file);
            formBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            String fileName = file.getName();
            formBuilder.addFormDataPart("file", fileName, fileBody);
        }
        if (formBuilder != null) {
            Request request = new Request.Builder().url(url).post(formBuilder.build()).build();
            result = execute(request);
        }
        return result;
    }
    /**
     * 文件下载
     * @param url 服务端路径
     * @param path 本地保存位置
     * @return File 保存后的文件对象
     * @throws IOException
     * @
     */
    public static File syncDownloadFile(String url, String path) throws IOException  {
        Request request = new Request.Builder().url(url).build();
        Response File = execute4File(request);
        String disposition = File.header("Content-disposition");
        String fileName = StringUtils.substringAfter(disposition, "attachment;filename=");
        File file = new File(path + fileName);
        // FileUtils.copyInputStreamToFile(File.body().byteStream(), file);
        FileOutputStream fos = null;
        try {
            InputStream is = File.body().byteStream();
            fos = new FileOutputStream(file);
            if(fos!=null){
                byte[] b = new byte[1024];
                while ((is.read(b)) != -1) {
                    fos.write(b);
                }
            }
        } catch (IOException e) {
            throw e;
        }finally{
            if(fos!=null){
                fos.close();
            }
        }
        return file;
    }

    public static String syncPostFormWithHeaders(String url, Map<String, String> params, Map<String, String> headerParams)
            throws IOException {
        System.out.println("请求地址："+url);
        Request.Builder requestBuilder = new Request.Builder().url(url);
        if (headerParams != null && !headerParams.isEmpty()) {
            for (Map.Entry<String, String> entry : headerParams.entrySet()) {
                requestBuilder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        if (params != null && !params.isEmpty()) {
            Builder formBuilder = new FormBody.Builder();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                System.out.println("请求参数："+entry.getKey()+"="+entry.getValue());
                formBuilder.add(entry.getKey(), entry.getValue());
            }
            RequestBody formBody = formBuilder.build();
            requestBuilder.post(formBody);
        }else {
            Builder formBuilder = new FormBody.Builder();
            RequestBody formBody = formBuilder.build();
            requestBuilder.post(formBody);
        }
        Request request = requestBuilder.build();
        String result = execute(request);
        return result;
    }


    /**
     * 创建异步post方法
     * 创建表单FormBody
     * 把我们取出的key和value对应的放进去
     * 最后，build表单，生成RequestBody
     *
     * @param url 请求地址
     * @param params
     * @param callBack
     * @throws Exception
     */
    public static void asynPost(String url, Map<String, String> params, Map<String, String> headers, AsyncCallBack callBack) throws Exception {
        System.out.println("请求地址："+url);
        FormBody.Builder builder = new FormBody.Builder();

        for (Map.Entry<String, String> entry : params.entrySet()) {
            System.out.println("请求参数："+entry.getKey()+"="+entry.getValue());
            builder.add(entry.getKey(), entry.getValue());
        }

        Request.Builder requestBuilder = new Request.Builder();

        if (headers != null) {
            Set<String> keys = headers.keySet();
            for (Iterator<String> i = keys.iterator(); i.hasNext();) {
                String key = (String) i.next();
                requestBuilder.addHeader(key, headers.get(key));
            }
        }
        RequestBody body = builder.build();
        requestBuilder.post(body).url(url);
        Request request = requestBuilder.build();
        enqueue(request, callBack);
    }

    /**
     * 把流转成字符串 666版
     */
    private static String byte2String(byte[] bytes) {
        return new String(bytes);
    }

    // 同步调用
    private static String execute(Request request) throws IOException  {
        Response response = client.newCall(request).execute();
        return byte2String(response.body().bytes());
    }

    private static Response execute4File(Request request) throws IOException  {
        Response response = client.newCall(request).execute();
        return response;
    }

    // 异步调用
    private static void enqueue(Request request, Callback call)  {
        client.newCall(request).enqueue(call);
    }

    private static void enqueue(Request request,AsyncCallBack asyncCallBack) throws Exception{
        Callback callBack = new Callback()
        {


            @Override
            public void onFailure(Call call, IOException e) {
                asyncCallBack.onFailure(e.getMessage());

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                asyncCallBack.onResponse(response.body().string());

            };

        };


        client.newCall(request).enqueue(callBack);
    }

}
