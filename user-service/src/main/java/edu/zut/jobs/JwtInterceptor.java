package edu.zut.jobs;

import edu.zut.common.ErrorCode;
import edu.zut.entity.vo.UserVo;
import edu.zut.exception.BusinessException;
import edu.zut.util.JWTUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.PrintWriter;

/**
 * 拦截请求解析Token
 * AI
 *
 * @version 1.0
 * @Author zhy
 * @Date 2025/6/18 17:21
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest req,
                             HttpServletResponse res,
                             Object handler) throws Exception {

        String token = req.getHeader("Authorization");
        if(token == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN, "没有权限，请先登录！");
        }
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        UserVo userVo = JWTUtils.parseToken(token);
        if (userVo == null) {
            res.setCharacterEncoding("UTF-8");
            res.setContentType("application/json");
            res.setStatus(401);
            try (PrintWriter out = res.getWriter()) {
                out.write("{\"error\":\"鉴权失败请重新登录！\"}");
            }
            return false;
        }
        // 可将用户名存入请求属性，供后续代码使用
        req.setAttribute("userVo", userVo);
        return true;
    }
}
