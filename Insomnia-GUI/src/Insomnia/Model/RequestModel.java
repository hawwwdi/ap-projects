package Insomnia.Model;

import Insomnia.Utils.Method;

import java.io.Serializable;
import java.util.regex.Pattern;

/**
 * the requestModel class
 */
public class RequestModel implements Serializable {
    private String url;
    private Method method;
    private String headers;
    private String messageBody;
    private String query;
    private boolean showResponseHeaders;
    private boolean followRedirect;
    private String savedResponseName;
    private boolean sendToServer;
    private String serverIp;
    private int port;

    
    private RequestModel(String url, Method method, String headers
            , String messageBody, String query, boolean showResponseHeaders
            , boolean followRedirect, String savedResponseName, String serverIp, int port) {
        this.url = url;
        this.method = method;
        this.headers = headers;
        this.messageBody = messageBody;
        this.query = query;
        this.showResponseHeaders = showResponseHeaders;
        this.followRedirect = followRedirect;
        this.savedResponseName = savedResponseName;
        this.sendToServer = false;
        this.serverIp = serverIp;
        this.port = port;
    }

    public RequestModel(boolean followRedirect) {
        this.followRedirect = followRedirect;
        this.method = Method.GET;
        this.url = "";
        this.headers = "";
        this.messageBody = "";
        this.savedResponseName = "";
        this.query = "";
        this.serverIp = " ";
        sendToServer = false;
    }

    public RequestModel getCopy() {
        return new RequestModel(url, method, headers
                , messageBody, query, showResponseHeaders
                , followRedirect, savedResponseName, serverIp, port);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public String getHeaders() {
        return headers.replaceAll("\"", "");
    }

    public void setHeaders(String headers) {
        this.headers = headers;
    }

    public String getMessageBody() {
        if (!messageBody.contains("Json"))
            return messageBody.replaceAll("\"", "");
        else
            return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }

    public boolean isShowResponseHeaders() {
        return showResponseHeaders;
    }

    public void setShowResponseHeaders(boolean showResponseHeaders) {
        this.showResponseHeaders = showResponseHeaders;
    }

    public boolean isFollowRedirect() {
        return followRedirect;
    }

    public void setFollowRedirect(boolean followRedirect) {
        this.followRedirect = followRedirect;
    }

    public String getSavedResponseName() {
        return savedResponseName;
    }

    public void setSavedResponseName(String savedResponseName) {
        this.savedResponseName = savedResponseName;
    }

    public String getUrlWithQuery() {
        if (!"".equals(query))
            return url + "?" + query;
        else
            return url;
    }

    public boolean isSendToServer() {
        return sendToServer;
    }

    public void setSendToServer(boolean sendToServer) {
        this.sendToServer = sendToServer;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    /*  public boolean isFile() {
        return Pattern.matches(".+[.]\\w+", url);
    }

    public String getFileName() {
        int slashIndex = url.lastIndexOf('/');
        return url.substring(slashIndex);
    }*/

    @Override
    public String toString() {
        StringBuilder toReturn = new StringBuilder();
        toReturn.append("url= ")
                .append(url).append(" - Method= ")
                .append(method).append(" - headers= ").append(headers)
                .append(" - MessageBody= ").append(messageBody).append(" - follow redirect= ")
                .append(followRedirect).append(" - show response headers= ").append(showResponseHeaders);
        return toReturn.toString();
    }
}
