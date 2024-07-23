package sws.songpin.global.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;
import sws.songpin.global.exception.CustomException;
import sws.songpin.global.exception.ErrorCode;

@Component
@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {

    private final RedisService redisService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        if(authentication!=null && authentication.getName() != null){
            //Redis 에서 Refresh Token 삭제
            redisService.deleteValues(authentication.getName());

            // 기본 로그아웃 핸들러 기능 수행
            SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
            securityContextLogoutHandler.logout(request, response, authentication);
        } else{
            throw new CustomException(ErrorCode.NOT_AUTHENTICATED);
        }
    }
}
