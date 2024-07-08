package sws.songpin.global.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import sws.songpin.global.exception.ErrorCode;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        Object exception = request.getAttribute("exception");
        if(exception instanceof  ErrorCode){
            ErrorCode errorCode = (ErrorCode) exception;
            setResponse(response,errorCode);
            return;
        }
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }

    private void setResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException{
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("timestamp", LocalDateTime.now().toString());
        jsonObject.put("status",errorCode.getStatus());
        jsonObject.put("errorCode", errorCode.name());
        jsonObject.put("message", errorCode.getMessage());
        response.getWriter().println(jsonObject);

    }
}