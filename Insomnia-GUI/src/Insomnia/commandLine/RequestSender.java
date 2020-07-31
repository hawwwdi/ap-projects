package Insomnia.commandLine;

import Insomnia.Model.ResponseModel;
import Insomnia.Server.RequestClient;
import Insomnia.Utils.Method;
import Insomnia.Model.RequestModel;
import Insomnia.Utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * the RequestSender class
 * it use to convert a request model to http request
 * and send request to server then receive a response
 */
public class RequestSender {
    private final static String BOUNDARY = "--52415674654153375";
    private HttpClient client;
    private RequestModel model;

    /**
     * constructor of this class
     * it use to create new object of this class
     *
     * @param model model of http request
     */
    public RequestSender(RequestModel model) {
        this.model = model;
        client = HttpClient.newBuilder().followRedirects(
                model.isFollowRedirect() ? HttpClient.Redirect.ALWAYS : HttpClient.Redirect.NEVER)
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    /**
     * it use to create http request with req model details
     *
     * @return HttpRequest of request model
     * @throws IllegalArgumentException
     */
    private HttpRequest getRequest() throws IllegalArgumentException {
        HttpRequest.Builder reqBuilder = HttpRequest.newBuilder()
                .uri(URI.create(model.getUrlWithQuery()));
        try {
            reqBuilder = reqBuilder.headers(model.getHeaders().split("[:;]"));
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
        reqBuilder = addMessageBodyHeader(reqBuilder)
                .method(model.getMethod().name(), getBodyPublisher());
        return reqBuilder.build();
    }

    /**
     * the send method
     * it use to send http request and return its response
     *
     * @return request response
     */
    public ResponseModel send() {
        System.out.println("sending...");
        ResponseModel response;
        if (model.isSendToServer()) {
            System.out.println("\bto server");
            try {
                response = new RequestClient(model).send();
            } catch (Exception e) {
                System.err.println(e.getMessage());
                System.err.println("server notFound!\n try local sender");
                response = sendReq();
            }
        } else
            response = sendReq();
        System.out.println("received");
        return response;
    }

    /**
     * this method send request object to server
     * then give its response and create response model
     *
     * @return response model of request response
     */
    private ResponseModel sendReq() {
        ResponseModel toReturn = new ResponseModel(model.getUrl());
        HttpResponse<byte[]> response = null;
        try {
            long time = System.currentTimeMillis();
            response = client.send(getRequest(), HttpResponse.BodyHandlers.ofByteArray());
            String expiredTime = String.valueOf((System.currentTimeMillis() - time)) + " ms";
            toReturn.setTime(expiredTime);
            toReturn.setStatus(getStatus(response.statusCode()));
            if (model.isShowResponseHeaders()) {
                HttpHeaders headers = response.headers();
                toReturn.setHeaders(headers.map());
            }
            toReturn.setBody(response.body());
            toReturn.setDataTransferred(response.body().length / 1024f + " KB");
        } catch (Exception e) {
            System.out.println("Exception handled :))");
            e.printStackTrace();
        }
        return toReturn;
    }

   /* private String download() {
        StringBuilder toReturn = new StringBuilder("");
        Path localFile = Paths.get("C:\\Users\\hawwwdi\\IdeaProjects\\Insomnia-GUI\\save\\download\\" + model.getFileName());
        System.out.println("downloading");
        try {
            HttpResponse<Path> response = client.send(getRequest(), HttpResponse.BodyHandlers.ofFile(localFile));
            toReturn.append("Response: ").append("\n");
            toReturn.append("--------------------------------------------------------------------------------------").append("\n");
            toReturn.append("status: \n").append(getStatus(response.statusCode())).append("\n");

            toReturn.append("--------------------------------------------------------------------------------------").append("\n");
            if (model.isShowResponseHeaders()) {
                toReturn.append("headers: ").append("\n");
                HttpHeaders headers = response.headers();
                headers.map().forEach((k, v) -> toReturn.append(k).append(":").append(v).append("\n"));
            }
            toReturn.append("message body:").append("\n")
                    .append(response.body().toFile().getAbsolutePath()).append("\n")
                    .append("transferred data size: ").append(response.body().toFile().getUsableSpace()).append(" KB");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return toReturn.toString();
    }*/

    /**
     * this method use to convert a formed string to Map with given delimiters
     *
     * @param data       formed string
     * @param delimiter1 first delimiter
     * @param delimiter2 second delimiter
     * @return Map of form data
     */
    private Map splitFormData(String data, String delimiter1, String delimiter2) {
        Map<String, String> toReturn = new HashMap();
        String[] splited = data.replaceAll("\"", "").split(delimiter1);
        for (String curr : splited) {
            String[] form = curr.split(delimiter2);
            if (form.length > 1)
                toReturn.put(form[0], form[1]);
        }
        return toReturn;
    }

    /**
     * this method convert status code to status message
     *
     * @param status response status code
     * @return status message
     */
    private String getStatus(int status) {
        String code = String.valueOf(status);
        String[] statuses = ("100 Continue\n" +
                "101 Switching Protocols\n" +
                "103 Early Hints\n" +
                "200 OK\n" +
                "201 Created\n" +
                "202 Accepted\n" +
                "203 Non-Authoritative Information\n" +
                "204 No Content\n" +
                "205 Reset Content\n" +
                "206 Partial Content\n" +
                "300 Multiple Choices\n" +
                "301 Moved Permanently\n" +
                "302 Found\n" +
                "303 See Other\n" +
                "304 Not Modified\n" +
                "307 Temporary Redirect\n" +
                "308 Permanent Redirect\n" +
                "400 Bad Request\n" +
                "401 Unauthorized\n" +
                "402 Payment Required\n" +
                "403 Forbidden\n" +
                "404 Not Found\n" +
                "405 Method Not Allowed\n" +
                "406 Not Acceptable\n" +
                "407 Proxy Authentication Required\n" +
                "408 Request Timeout\n" +
                "409 Conflict\n" +
                "410 Gone\n" +
                "411 Length Required\n" +
                "412 Precondition Failed\n" +
                "413 Payload Too Large\n" +
                "414 URI Too Long\n" +
                "415 Unsupported Media Type\n" +
                "416 Range Not Satisfiable\n" +
                "417 Expectation Failed\n" +
                "418 I'm a teapot\n" +
                "422 Unprocessable Entity\n" +
                "425 Too Early\n" +
                "426 Upgrade Required\n" +
                "428 Precondition Required\n" +
                "429 Too Many Requests\n" +
                "431 Request Header Fields Too Large\n" +
                "451 Unavailable For Legal Reasons\n" +
                "500 Internal Server Error\n" +
                "501 Not Implemented\n" +
                "502 Bad Gateway\n" +
                "503 Service Unavailable\n" +
                "504 Gateway Timeout\n" +
                "505 HTTP Version Not Supported\n" +
                "506 Variant Also Negotiates\n" +
                "507 Insufficient Storage\n" +
                "508 Loop Detected\n" +
                "510 Not Extended\n" +
                "511 Network Authentication Required").split("\n");

        for (String curr : statuses)
            if (curr.substring(0, 3).equals(code))
                return curr.trim();
        return "Status code " + code + " Not found!";
    }

    /**
     * this is a local enum for message body types
     */
    private enum Type {
        STRING, FORM_DATA, JSON
    }

     /**
     * this method used to detect a message body type
     *
     * @param messageBody request message body
     * @return message body type
     */
    private Type detectType(String messageBody) {
        if (messageBody.contains("Json"))
            return Type.JSON;
        else if (Pattern.matches("(.*=.*&?)*", messageBody) && !messageBody.equals(""))
            return Type.FORM_DATA;
        else
            return Type.STRING;
    }

    /**
     * this method used to add message body header to request builder according to message body type
     *
     * @param builder
     * @return
     */
    private HttpRequest.Builder addMessageBodyHeader(HttpRequest.Builder builder) {
        Type bodyType = detectType(model.getMessageBody());
        switch (bodyType) {
            case STRING:
                return builder.header("Content-Type", "text/plain");
            case FORM_DATA:
                return builder.header("Content-Type", "multipart/form-data;boundary=" + BOUNDARY);
            case JSON:
                return builder.header("Content-Type", "application/json");
        }
        return builder;
    }

    /**
     * this method used to create body publisher for request builder according to request body type
     *
     * @return Body publisher of requset
     */
    private HttpRequest.BodyPublisher getBodyPublisher() {
        Type bodyType = detectType(model.getMessageBody());
        switch (bodyType) {
            case STRING:
                return HttpRequest.BodyPublishers.ofString(model.getMessageBody());
            case JSON:
                return HttpRequest.BodyPublishers.ofString(model.getMessageBody().substring(5));
            case FORM_DATA:
                try {
                    return ofMultipartData(splitFormData(model.getMessageBody(), "&", "="));
                } catch (IOException e) {
                    System.out.println("multi part exception handled");
                    return HttpRequest.BodyPublishers.noBody();
                }
        }
        return HttpRequest.BodyPublishers.noBody();
    }

    /**
     * this method convert message body to byte array format
     * add boundary between each part
     *
     * @param data Map of form data body
     * @return body publisher of byte arrays form data
     * @throws IOException
     */
    private HttpRequest.BodyPublisher ofMultipartData(Map<String, String> data) throws IOException {
        LinkedList<byte[]> bytes = new LinkedList<byte[]>();
        byte[] separator = ("--" + BOUNDARY + "\r\nContent-Disposition: form-data; name=")
                .getBytes(StandardCharsets.UTF_8);
        for (String key : data.keySet()) {
            bytes.add(separator);
            String value = data.get(key);
            if ("file".equals(key)) {
                Path path = Paths.get(value);
                String Type = Files.probeContentType(path);
                bytes.add(("\"" + key + "\"; filename=\"" + path.getFileName()
                        + "\"\r\nContent-Type: " + Type + "\r\n\r\n").getBytes(StandardCharsets.UTF_8));
                bytes.add(Files.readAllBytes(path));
                bytes.add("\r\n".getBytes(StandardCharsets.UTF_8));
            } else {
                bytes.add(("\"" + key + "\"\r\n\r\n" + value + "\r\n")
                        .getBytes(StandardCharsets.UTF_8));
            }
        }
        bytes.add(("--" + BOUNDARY + "--").getBytes(StandardCharsets.UTF_8));
        return HttpRequest.BodyPublishers.ofByteArrays(bytes);
    }
}
