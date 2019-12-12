package com.naman14.hacktoberfest.network.api;

/**
 * Created by naman on 17/6/17.
 */

public class BaseURL {

    public static final String PROTOCOL_HTTPS = "https://";

    public static final String API_ENDPOINT = "api.github.com";

    public static final String URL_GITHUB = "https://github.com/";


    public String getUrl() {
        return PROTOCOL_HTTPS + API_ENDPOINT;

    }

}
