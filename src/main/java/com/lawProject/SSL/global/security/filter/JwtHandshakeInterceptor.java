package com.lawProject.SSL.global.security.filter;

import com.lawProject.SSL.global.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    private final JwtUtil jwtUtil;

    public JwtHandshakeInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
//        String token = request.getHeaders().getFirst("Authorization");
        String token = jwtUtil.extractAccessToken((HttpServletRequest) request).get();
        if (token != null && jwtUtil.isTokenValid(token)) {
            String userId = jwtUtil.extractUserId(token);
            attributes.put("userId", userId); // WebSocketSession의 속성에 사용자 ID 저장
            return true;
        }
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
        // 추가 설정이 필요 없으면 비워두기
    }
}