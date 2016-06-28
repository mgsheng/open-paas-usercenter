package com.andaily.springoauth.service.impl;

import com.andaily.springoauth.infrastructure.httpclient.MkkHttpResponse;
import com.andaily.springoauth.service.dto.AccessTokenDto;

/**
 * 15-5-18
 *
 * 
 */
public class AccessTokenResponseHandler extends AbstractResponseHandler<AccessTokenDto> {


    private AccessTokenDto accessTokenDto;

    public AccessTokenResponseHandler() {
    }

    public void handleResponse(MkkHttpResponse response) {
        if (response.isResponse200()) {
            this.accessTokenDto = responseToDto(response, new AccessTokenDto());
        } else {
            this.accessTokenDto = responseToErrorDto(response, new AccessTokenDto());
        }
    }


    public AccessTokenDto getAccessTokenDto() {
        return accessTokenDto;
    }
}
