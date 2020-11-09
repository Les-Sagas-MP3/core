package fr.lessagasmp3.core.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class ImgurService extends HttpClientService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImgurService.class);

    @Value("${imgur.token}")
    private String token;

    @Autowired
    private Gson gson;

    public String createAlbum(String title) {
        String url = "https://api.imgur.com/3/album";

        HttpEntity entity = MultipartEntityBuilder.create()
                .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
                .addPart("title", new StringBody(title, ContentType.MULTIPART_FORM_DATA))
                .build();

        String json = executeRequest(new HttpPost(url), entity);
        if(json != null) {
            JsonObject response = gson.fromJson(json, JsonObject.class);
            if(response.has("data")) {
                if(response.getAsJsonObject("data").has("id")) {
                    return response.getAsJsonObject("data").get("id").getAsString();
                }
            }
        }
        return null;
    }

    public String getAlbum(String albumHash) {
        String url = "https://api.imgur.com/3/album/" + albumHash;
        String json = executeRequest(new HttpGet(url));
        return json;
    }

    public String upload(File file, String albumHash, String title) {
        String url = "https://api.imgur.com/3/upload";

        HttpEntity entity = null;
        try {
            entity = MultipartEntityBuilder.create()
                    .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
                    .addBinaryBody("image", file, ContentType.parse(Files.probeContentType(Path.of(file.getPath()))), file.getName())
                    .addPart("album", new StringBody(albumHash, ContentType.MULTIPART_FORM_DATA))
                    .addPart("title", new StringBody(title, ContentType.MULTIPART_FORM_DATA))
                    .build();
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }

        String json = executeRequest(new HttpPost(url), entity);
        if(json != null) {
            JsonObject response = gson.fromJson(json, JsonObject.class);
            if(response.has("data")) {
                if(response.getAsJsonObject("data").has("link")) {
                    return response.getAsJsonObject("data").get("link").getAsString();
                }
            }
        }
        return null;
    }

    private String executeRequest(HttpRequestBase request) {
        request.addHeader("Authorization", "Bearer " + token);
        LOGGER.debug(request.getMethod() + " " + request.getURI());
        LOGGER.debug("Authorization : " + request.getHeaders("Authorization")[0].getValue());
        CloseableHttpResponse response;
        try (CloseableHttpClient httpClient = getHttpClient()) {
            response = httpClient.execute(request);
            String responseString = getStringResponse(response);
            if(response.getStatusLine().getStatusCode() == 200) {
                LOGGER.debug("Response : " + responseString);
                return responseString;
            } else {
                LOGGER.error("response : " + responseString);
                throw new IllegalStateException("Error while requesting Imgur");
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    private String executeRequest(HttpEntityEnclosingRequestBase request, HttpEntity entity) {
        request.addHeader("Authorization", "Bearer " + token);
        LOGGER.debug(request.getMethod() + " " + request.getURI());
        LOGGER.debug("Authorization : " + request.getHeaders("Authorization")[0].getValue());
        CloseableHttpResponse response;
        try (CloseableHttpClient httpClient = getHttpClient()) {
            if (entity != null) {
                request.setEntity(entity);
                LOGGER.debug("Content-Type : " + entity.getContentType());
            }
            response = httpClient.execute(request);
            String responseString = getStringResponse(response);
            if(response.getStatusLine().getStatusCode() == 200) {
                LOGGER.debug("Response : " + responseString);
                return responseString;
            } else {
                LOGGER.error("response : " + responseString);
                throw new IllegalStateException("Error while requesting Imgur");
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

}
