package cn.com.open.openpaas.userservice.app.oauth.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.open.openpaas.userservice.app.infrastructure.repository.OauthRepository;
import cn.com.open.openpaas.userservice.app.oauth.model.OauthClientDetails;
import cn.com.open.openpaas.userservice.app.oauth.model.OauthClientDetailsDto;

/**
 * 
 */
@Service("oauthService")
public class OauthServiceImpl implements OauthService {

    @Autowired
    private OauthRepository oauthRepository;

    @Override
    public OauthClientDetails loadOauthClientDetails(String clientId) {
        return oauthRepository.findOauthClientDetails(clientId);
    }

    @Override
    public List<OauthClientDetailsDto> loadAllOauthClientDetailsDtos() {
        List<OauthClientDetails> clientDetailses = oauthRepository.findAllOauthClientDetails();
        return OauthClientDetailsDto.toDtos(clientDetailses);
    }

}