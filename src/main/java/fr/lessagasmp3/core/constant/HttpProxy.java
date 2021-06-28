package fr.lessagasmp3.core.constant;

import com.gargoylesoftware.htmlunit.ProxyConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;

public class HttpProxy {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpProxy.class);

    public static final String HTTP_PROXY = System.getenv("HTTP_PROXY");
    public static final String HTTPS_PROXY = System.getenv("HTTPS_PROXY");

    public static String HTTP_HOST = Strings.EMPTY;
    public static int HTTP_PORT = 0;
    public static String HTTPS_HOST = Strings.EMPTY;
    public static int HTTPS_PORT = 0;
    public static ProxyConfig proxyConfig = null;

    static {

        if(HTTP_PROXY != null) {
            try {
                URI uri = new URI(HTTP_PROXY);
                HTTP_HOST = uri.getHost();
                HTTP_PORT = uri.getPort();
            } catch (URISyntaxException e) {
                LOGGER.error("Cannot parse HTTP_PROXY");
            }
        }

        if(HTTPS_PROXY != null) {
            try {
                URI uri = new URI(HTTPS_PROXY);
                HTTPS_HOST = uri.getHost();
                HTTPS_PORT = uri.getPort();
            } catch (URISyntaxException e) {
                LOGGER.error("Cannot parse HTTPS_PROXY");
            }
        }

        if(!HTTPS_HOST.isEmpty() && HTTPS_PORT != 0) {
            proxyConfig = new ProxyConfig(HTTPS_HOST, HTTPS_PORT, "http");
        } else if(!HTTP_HOST.isEmpty() && HTTP_PORT != 0) {
            proxyConfig = new ProxyConfig(HTTP_HOST, HTTP_PORT, "http");
        }

    }

}
