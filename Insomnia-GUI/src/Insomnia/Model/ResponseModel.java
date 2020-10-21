package Insomnia.Model;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * response model class
 */
public class ResponseModel implements Serializable {
    private String url;
    private Map<String, List<String>> headers;
    private byte[] body;
    private String status;
    private String dataTransferred;
    private String time;

    public ResponseModel(String url) {
        this.url = url;
        status = "null";
        dataTransferred = "       ";
        time = "            ";
        body = new byte[]{2, 2};
        headers = new HashMap<>();
        headers.put(" ", new ArrayList<String>(Arrays.asList(new String[]{" "})));
    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    public String getUrl() {
        return url;
    }

    public void setHeaders(Map<String, List<String>> headers) {
        this.headers = headers;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDataTransferred() {
        return dataTransferred;
    }

    public void setDataTransferred(String dataTransferred) {
        this.dataTransferred = dataTransferred;
    }

    public String getBodyAsString() {
        return new String(body, StandardCharsets.UTF_8);
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    /**
     * get content type method
     * it returns response content type headers value
     * @return response content type headers value
     */
    public String getContentType() {
        if (headers.containsKey("content-type"))
            return headers.get("content-type").get(0);
        else
            return "";
    }

    @Override
    public String toString() {
        StringBuilder toReturn = new StringBuilder("");
        toReturn.append("expired time: ").append(time).append("\n")
                .append("status: ").append(status).append("\n")
                .append("transferred data Size: ").append(dataTransferred).append("\n");
        if (headers != null) {
            toReturn.append("headers: ").append("\n");
            headers.forEach((k, v) -> toReturn.append(k).append(" : ").append(v).append("\n"));
        }
        toReturn.append("response body: ").append("\n")
                .append(getBodyAsString());
        return toReturn.toString();
    }
}
