package com.challnege.delivery.global.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Map;

public class CustomFormUsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private static final String DEFAULT_LOGIN_REQUEST_URL = "/login";
    private static final String HTTP_METHOD = "POST";
    private static final String CONTENT_TYPE = "application/json";
    private static final String USERNAME_KEY = "email";
    private static final String PASSWORD_KEY = "password";
    private static final AntPathRequestMatcher DEFAULT_LOGIN_PATH_REQUEST_MATCHER =
            new AntPathRequestMatcher(DEFAULT_LOGIN_REQUEST_URL, HTTP_METHOD);
    private final ObjectMapper objectMapper;

    public CustomFormUsernamePasswordAuthenticationFilter(ObjectMapper objectMapper) {
        super(DEFAULT_LOGIN_PATH_REQUEST_MATCHER);
        this.objectMapper = objectMapper;
    }

//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {
//        if(request.getContentType() == null || !request.getContentType().equals(CONTENT_TYPE)  ) {
//            throw new AuthenticationServiceException("Authentication Content-Type not supported: " + request.getContentType());
//        }
//
//        String messageBody = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
//
//        Map<String, String> usernamePasswordMap = objectMapper.readValue(messageBody, Map.class);
//
//        String email = usernamePasswordMap.get(USERNAME_KEY);
//        String password = usernamePasswordMap.get(PASSWORD_KEY);
//
//        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(email, password);//principal 과 credentials 전달
//
//        return this.getAuthenticationManager().authenticate(authRequest);
//    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {
        // form-data로 전송된 데이터를 JSON 형식으로 파싱
        Map<String, String[]> paramMap = request.getParameterMap();

        // email과 password에 대한 null 체크 추가
        String email = paramMap.containsKey("email") && StringUtils.hasText(paramMap.get("email")[0]) ? paramMap.get("email")[0] : "";
        String password = paramMap.containsKey("password") && StringUtils.hasText(paramMap.get("password")[0]) ? paramMap.get("password")[0] : "";

        // UsernamePasswordAuthenticationToken에 변경된 파라미터 전달
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(email, password);

        return this.getAuthenticationManager().authenticate(authRequest);
    }
}
