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

        StringBuilder cookieHeader = new StringBuilder();
        cookieHeader.append(name).append("=").append(value).append(";");
        cookieHeader.append("Max-Age=").append(maxAge).append(";");
        cookieHeader.append("Expires=").append(new java.util.Date(System.currentTimeMillis() + maxAge * 1000L)).append(";");
        cookieHeader.append("Path=/;");
        cookieHeader.append("HttpOnly;");
        cookieHeader.append("Secure;");
        cookieHeader.append("SameSite=None;");

        response.addHeader("Set-Cookie", cookieHeader.toString());

    }

    public void deleteCookie(HttpServletResponse response, String name){
        Cookie cookie = new Cookie(name,null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);

        response.addCookie(cookie);}
}
