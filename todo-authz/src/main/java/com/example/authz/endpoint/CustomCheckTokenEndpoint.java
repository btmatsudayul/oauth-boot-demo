package com.example.authz.endpoint;

import java.util.Collection;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.approval.Approval;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.endpoint.CheckTokenEndpoint;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Component
public class CustomCheckTokenEndpoint extends CheckTokenEndpoint {

  @Autowired
  private ApprovalStore approvalStore;

  private ResourceServerTokenServices resourceServerTokenServices;

  private AccessTokenConverter accessTokenConverter = new DefaultAccessTokenConverter();

  public CustomCheckTokenEndpoint(ResourceServerTokenServices resourceServerTokenServices) {
    super(resourceServerTokenServices);
    this.resourceServerTokenServices = resourceServerTokenServices;
  }

  public void setExceptionTranslator(WebResponseExceptionTranslator exceptionTranslator) {
    super.setExceptionTranslator(exceptionTranslator);
  }

  @RequestMapping(value = "/oauth/check_token_custom")
  @ResponseBody
  public Map<String, ?> checkToken(@RequestParam("token") String value) {

    OAuth2AccessToken token = resourceServerTokenServices.readAccessToken(value);
    if (token == null) {
      throw new InvalidTokenException("Token was not recognised");
    }

    if (token.isExpired()) {
      throw new InvalidTokenException("Token has expired");
    }

    OAuth2Authentication authentication =
        resourceServerTokenServices.loadAuthentication(token.getValue());

    // ユーザ名とクライアントIDをキーに認可状態を全取得
    Collection<Approval> approvals = approvalStore.getApprovals(authentication.getName(),
        authentication.getOAuth2Request().getClientId());

    // 認可状態が登録されていなかったらエラー
    if (approvals.isEmpty()) {
      throw new InvalidTokenException("Token was not recognised");
    }

    @SuppressWarnings("unchecked")
    Map<String, Object> response =
        (Map<String, Object>) accessTokenConverter.convertAccessToken(token, authentication);

    response.put("active", true); // Always true if token exists and not expired

    return response;
  }
}
