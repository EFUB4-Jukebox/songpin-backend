package sws.songpin.global.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import sws.songpin.global.exception.CustomException;
import sws.songpin.global.exception.ErrorCode;

@Component
public class CookieUtil {

    public String getCookieValue(HttpServletRequest request, String name){
        Cookie[] cookies = request.getCookies();
        if(cookies == null){
            throw new CustomException(ErrorCode.NO_COOKIE);
        }
        for(Cookie cookie : cookies){
            if(cookie.getName().equals(name)){
                return cookie.getValue();
            }
        }
        throw new CustomException(ErrorCode.NO_COOKIE);
    }

    public void addCookie(HttpServletResponse response, String name, String value, int maxAge){
        Cookie refreshTokenCookie = new Cookie(name, value);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(maxAge);

        response.addCookie(refreshTokenCookie);
    }

    public void deleteCookie(HttpServletResponse response, String name){
        Cookie refreshTokenCookie = new Cookie(name,null);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(0);

        response.addCookie(refreshTokenCookie);
    }
}
