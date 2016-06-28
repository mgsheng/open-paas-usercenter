package cn.com.open.openpaas.userservice.app.oauth.model;

import java.io.Serializable;

import cn.com.open.openpaas.userservice.app.infrastructure.model.DateUtils;

/**
 * 
 */
public class OauthClientDetails implements Serializable {

    private long createTime = DateUtils.now().getTime();
    private boolean archived = false;

    private int id;
    private String clientId;
    private String resourceIds;

    private String clientSecret;
    /**
     * Available values: read,write
     */
    private String scope;
    
    private String icon;

    /**
     * grant types include
     * "authorization_code", "password", "assertion", and "refresh_token".
     * Default value is "authorization_code,refresh_token".
     */
    private String authorizedGrantTypes = "authorization_code,refresh_token";

    /**
     * The re-direct URI(s) established during registration (optional, comma separated).
     */
    private String webServerRedirectUri;

    /**
     * Authorities that are granted to the client (comma-separated). Distinct from the authorities
     * granted to the user on behalf of whom the client is acting.
     * <p/>
     * For example: ROLE_USER
     */
    private String authorities;

    /**
     * The access token validity period in seconds (optional).
     * If unspecified a global default will be applied by the token services.
     */
    private Integer accessTokenValidity;

    /**
     * The refresh token validity period in seconds (optional).
     * If unspecified a global default will  be applied by the token services.
     */
    private Integer refreshTokenValidity;

    // optional
    private String additionalInformation;

    /**
     * The client is trusted or not. If it is trust, will skip approve step
     * default false.
     */
    private boolean trusted = false;

    public OauthClientDetails() {
    }

    public int id(){
    	return id;
    }

    public boolean trusted() {
        return trusted;
    }

    public long createTime() {
        return createTime;
    }

    public boolean archived() {
        return archived;
    }

    public String clientId() {
        return clientId;
    }

    public String resourceIds() {
        return resourceIds;
    }

    public String clientSecret() {
        return clientSecret;
    }

    public String scope() {
        return scope;
    }

    public String authorizedGrantTypes() {
        return authorizedGrantTypes;
    }

    public String webServerRedirectUri() {
        return webServerRedirectUri;
    }

    public String authorities() {
        return authorities;
    }

    public Integer accessTokenValidity() {
        return accessTokenValidity;
    }

    public Integer refreshTokenValidity() {
        return refreshTokenValidity;
    }

    public String additionalInformation() {
        return additionalInformation;
    }
    
    public String icon(){
    	return icon;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("OauthClientDetails");
        sb.append("{createTime=").append(createTime);
        sb.append(", archived=").append(archived);
        sb.append(", clientId='").append(clientId).append('\'');
        sb.append(", resourceIds='").append(resourceIds).append('\'');
        sb.append(", clientSecret='").append(clientSecret).append('\'');
        sb.append(", scope='").append(scope).append('\'');
        sb.append(", authorizedGrantTypes='").append(authorizedGrantTypes).append('\'');
        sb.append(", webServerRedirectUri='").append(webServerRedirectUri).append('\'');
        sb.append(", authorities='").append(authorities).append('\'');
        sb.append(", accessTokenValidity=").append(accessTokenValidity);
        sb.append(", refreshTokenValidity=").append(refreshTokenValidity);
        sb.append(", additionalInformation='").append(additionalInformation).append('\'');
        sb.append(", icon='").append(icon).append('\'');
        sb.append(", trusted=").append(trusted);
        sb.append('}');
        return sb.toString();
    }
}