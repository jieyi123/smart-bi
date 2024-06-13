//package com.pjieyi.smartbi.runner;
//
//import com.sun.net.httpserver.HttpExchange;
//import com.sun.net.httpserver.HttpHandler;
//import com.sun.net.httpserver.HttpServer;
//import okhttp3.*;
//import org.json.JSONObject;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.stereotype.Component;
//import java.io.*;
//import java.net.InetSocketAddress;
//
///**
// * 项目启动的时候启动监听AI问答的服务
// * 百度AI
// */
//@Component
//public class AiHttpServer implements ApplicationRunner {
//
//    public static final String API_KEY = "uX3jOTNS3W88om82q2EGnhmI";
//    public static final String SECRET_KEY = "QccPBv79j0aafW8LQAkFNGxzWfjG2dbh";
//
//    @Override
//    public void run(ApplicationArguments args) throws Exception {
//        int port = 8000; // 定义端口号
//        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
//        server.createContext("/ai/eb_stream", new StreamHandler()); // 设置处理器
//        server.setExecutor(null);
//        server.start();
//    }
//
//    static String getAccessToken() throws IOException {
//        OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();
//        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
//        RequestBody body = RequestBody.create(mediaType, "grant_type=client_credentials&client_id=" + API_KEY + "&client_secret=" + SECRET_KEY);
//        Request request = new Request.Builder()
//                .url("https://aip.baidubce.com/oauth/2.0/token")
//                .method("POST", body)
//                .addHeader("Content-Type", "application/x-www-form-urlencoded")
//                .build();
//        Response response = HTTP_CLIENT.newCall(request).execute();
//        return new JSONObject(response.body().string()).getString("access_token");
//    }
//
//    static class StreamHandler implements HttpHandler {
//        @Override
//        public void handle(HttpExchange exchange) throws IOException {
//            // 读取body中的prompt入参
//            InputStreamReader bodyReader = new InputStreamReader(exchange.getRequestBody());
//            BufferedReader br = new BufferedReader(bodyReader);
//            StringBuilder strBuilder = new StringBuilder();
//            String line;
//            while((line = br.readLine()) != null) {
//                strBuilder.append(line);
//            }
//            br.close();
//            JSONObject body = new JSONObject(strBuilder.toString());
//            String prompt = body.getString("prompt");
//
//            // 发起流式请求
//            OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();
//            MediaType mediaType = MediaType.parse("application/json");
//            RequestBody ebRequestBody = RequestBody.create(mediaType, "{\n" +
//                    "    \"messages\": [\n" +
//                    "        {\n" +
//                    "            \"role\": \"user\",\n" +
//                    "            \"content\": \"" +prompt+"\"  \n" +
//                    "        }\n" +
//                    "    ],\n" +
//                    "      \"stream\":true\n" +
//                    "    \n" +
//                    "}");
//            String source = "&sourceVer=0.0.1&source=app_center&appName=streamDemo";
//            // 大模型接口URL
//            String baseUrl = "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/eb-instant";
//            Request request = new Request.Builder()
//                    .url(baseUrl + "?access_token=" + getAccessToken() + source)
//                    .method("POST", ebRequestBody)
//                    .addHeader("Content-Type", "application/json")
//                    .build();
//
//            OutputStream outputStream = exchange.getResponseBody();
//            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");  // 允许跨域
//            exchange.sendResponseHeaders(200, 0);
//            // 流式返回
//            Response response = HTTP_CLIENT.newCall(request).execute();
//            if (response.isSuccessful()) {
//                ResponseBody responseBody = response.body();
//                if (responseBody != null) {
//                    InputStream inputStream = responseBody.byteStream();
//                    byte[] buffer = new byte[1024];
//                    int bytesRead;
//                    while ((bytesRead = inputStream.read(buffer)) != -1) {
//                        String data = new String(buffer, 0, bytesRead);
//                        if (data.startsWith("data: ")) {
//                            outputStream.write(data.substring(6).getBytes());
//                            System.out.println(data.substring(6));
//                            outputStream.flush();
//                        }
//                    }
//                }
//            } else {
//                System.out.println("流式请求异常: " + response);
//            }
//            outputStream.close();
//            exchange.close();
//        }
//    }
//}
