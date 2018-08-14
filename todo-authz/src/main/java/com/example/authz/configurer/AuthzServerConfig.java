package com.example.authz.configurer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.approval.Approval;
import org.springframework.security.oauth2.provider.approval.Approval.ApprovalStatus;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.ApprovalStoreUserApprovalHandler;
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;
import org.springframework.security.oauth2.provider.approval.UserApprovalHandler;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

@Configuration
@EnableAuthorizationServer
public class AuthzServerConfig extends AuthorizationServerConfigurerAdapter {

  @Autowired
  private DataSource dataSource;

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private ClientDetailsService clientDetailsService;
 
  @Override
  public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
    security.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
  }

  @Override
  public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    clients.jdbc(dataSource).passwordEncoder(new BCryptPasswordEncoder());
  }

  @Override
  public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

    TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
    tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer(), accessTokenConverter()));

    endpoints.tokenStore(tokenStore())
      .accessTokenConverter(accessTokenConverter())
      .tokenEnhancer(tokenEnhancerChain)
      .authorizationCodeServices(authorizationCodeServices())
      .userApprovalHandler(userApprovalHandler(approvalStore()))
      .authenticationManager(authenticationManager);
  }
  
  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public JwtAccessTokenConverter accessTokenConverter() {

    final JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
    KeyStoreKeyFactory keyStoreKeyFactory =
        new KeyStoreKeyFactory(new ClassPathResource("jwt.jks"), "password".toCharArray());
    converter.setKeyPair(keyStoreKeyFactory.getKeyPair("jwt"));
    return converter;
  }

  // JwtTokenStoreに独自のTokenStoreを指定する場合に必要
  @Bean
  TokenStore tokenStore() {
    CustomJwtTokenStore tokenStore = new CustomJwtTokenStore(accessTokenConverter());
    tokenStore.setApprovalStore(approvalStore());
    return tokenStore;
  }

  @Bean
  public TokenEnhancer tokenEnhancer() {
    return new CustomTokenEnhancer();
  }

  // JwtTokenStoreに独自のTokenStoreを指定する場合に必要
  @Bean
  @Primary
  public DefaultTokenServices tokenServices() {
      DefaultTokenServices tokenServices = new DefaultTokenServices();
      tokenServices.setClientDetailsService(clientDetailsService);
      tokenServices.setTokenStore(tokenStore());
      tokenServices.setSupportRefreshToken(true);
      return tokenServices;
  }

  @Bean
  public ApprovalStore approvalStore() {
    return new JdbcApprovalStore(dataSource);
  }
  
  @Bean
  public AuthorizationCodeServices authorizationCodeServices() {
    return new JdbcAuthorizationCodeServices(dataSource);
  }

  @Bean
  public UserApprovalHandler userApprovalHandler(ApprovalStore approvalStore) {
    ApprovalStoreUserApprovalHandler handler = new ApprovalStoreUserApprovalHandler();
    handler.setApprovalStore(approvalStore);
    handler.setRequestFactory(new DefaultOAuth2RequestFactory(clientDetailsService));
    handler.setClientDetailsService(clientDetailsService);
    return handler;
  }

  public class CustomTokenEnhancer implements TokenEnhancer {

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken,
        OAuth2Authentication authentication) {

      DefaultOAuth2AccessToken result = new DefaultOAuth2AccessToken(accessToken);

      // JWTに独自パラメータを追加
      // JwtAccessTokenConverterでリフレッシュトークンにも同様のパラメータが設定される
      Map<String, Object> info =
          new LinkedHashMap<String, Object>(accessToken.getAdditionalInformation());
      info.put("customKey", "customValue");
      result.setAdditionalInformation(info);
      return result;
    }
  }
  
  public class CustomJwtTokenStore extends JwtTokenStore {

    private ApprovalStore approvalStore;
    
    public CustomJwtTokenStore(JwtAccessTokenConverter jwtTokenEnhancer) {
      super(jwtTokenEnhancer);
    }

    @Override
    public void setApprovalStore(ApprovalStore approvalStore) {
      super.setApprovalStore(approvalStore);
      this.approvalStore = approvalStore;
    }

    @Override
    public void removeAccessToken(OAuth2AccessToken token) {
      remove(token.getValue());
    }

    public void remove(String token) {
      if (approvalStore != null) {
        OAuth2Authentication auth = readAuthentication(token);
        String clientId = auth.getOAuth2Request().getClientId();
        Authentication user = auth.getUserAuthentication();
        if (user != null) {
          Collection<Approval> approvals = new ArrayList<>();
          for (String scope : auth.getOAuth2Request().getScope()) {
            approvals.add(
                new Approval(user.getName(), clientId, scope, new Date(), ApprovalStatus.APPROVED));
          }
          approvalStore.revokeApprovals(approvals);
        }
      }
    }
  }
}
