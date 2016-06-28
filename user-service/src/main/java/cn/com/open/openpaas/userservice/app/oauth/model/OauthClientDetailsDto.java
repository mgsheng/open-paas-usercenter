package cn.com.open.openpaas.userservice.app.oauth.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.com.open.openpaas.userservice.app.infrastructure.model.DateUtils;

/**
 * 
 */
public class OauthClientDetailsDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
    private String createTime;
    
    private boolean archived;

    private String clientId;
    
    private String resourceIds;

    private String clientSecret;

    private String scope;

    private String authorizedGrantTypes;

    private String webServerRedirectUri;

    private String authorities;

    private Integer accessTokenValidity;

    private Integer refreshTokenValidity;

    // optional
    private String additionalInformation;

    private boolean trusted;
    
    private String icon;

    public OauthClientDetailsDto() {
    }

    public OauthClientDetailsDto(OauthClientDetails clientDetails) {
        this.clientId = clientDetails.clientId();
        this.clientSecret = clientDetails.clientSecret();
        this.scope = clientDetails.scope();

        this.createTime = DateUtils.toDateTime(new Date(clientDetails.createTime()));
        this.archived = clientDetails.archived();
        this.resourceIds = clientDetails.resourceIds();

        this.webServerRedirectUri = clientDetails.webServerRedirectUri();
        this.authorities = clientDetails.authorities();
        this.accessTokenValidity = clientDetails.accessTokenValidity();

        this.refreshTokenValidity = clientDetails.refreshTokenValidity();
        this.additionalInformation = clientDetails.additionalInformation();
        this.trusted = clientDetails.trusted();

        this.authorizedGrantTypes = clientDetails.authorizedGrantTypes();
        
        this.icon=clientDetails.icon();
    }


    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getResourceIds() {
        return resourceIds;
    }

    public void setResourceIds(String resourceIds) {
        this.resourceIds = resourceIds;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getAuthorizedGrantTypes() {
        return authorizedGrantTypes;
    }

    public void setAuthorizedGrantTypes(String authorizedGrantTypes) {
        this.authorizedGrantTypes = authorizedGrantTypes;
    }

    public String getWebServerRedirectUri() {
        return webServerRedirectUri;
    }

    public void setWebServerRedirectUri(String webServerRedirectUri) {
        this.webServerRedirectUri = webServerRedirectUri;
    }

    public String getAuthorities() {
        return authorities;
    }

    public void setAuthorities(String authorities) {
        this.authorities = authorities;
    }

    public Integer getAccessTokenValidity() {
        return accessTokenValidity;
    }

    public void setAccessTokenValidity(Integer accessTokenValidity) {
        this.accessTokenValidity = accessTokenValidity;
    }

    public Integer getRefreshTokenValidity() {
        return refreshTokenValidity;
    }

    public void setRefreshTokenValidity(Integer refreshTokenValidity) {
        this.refreshTokenValidity = refreshTokenValidity;
    }

    public String getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    public boolean isTrusted() {
        return trusted;
    }

    public void setTrusted(boolean trusted) {
        this.trusted = trusted;
    }

    public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public static List<OauthClientDetailsDto> toDtos(List<OauthClientDetails> clientDetailses) {
        List<OauthClientDetailsDto> dtos = new ArrayList<OauthClientDetailsDto>(clientDetailses.size());
        for (OauthClientDetails clientDetailse : clientDetailses) {
            dtos.add(new OauthClientDetailsDto(clientDetailse));
        }
        return dtos;
    }
}