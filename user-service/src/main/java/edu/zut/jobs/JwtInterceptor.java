package edu.zut.jobs;

import com.auth0.jwt.exceptions.JWTDecodeException;
import edu.zut.common.ErrorCode;
import edu.zut.entity.vo.UserVo;
import edu.zut.exception.BusinessException;
import edu.zut.util.JWTBlackList;
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

    private final JWTBlackList jwtBlackList;

    public JwtInterceptor(JWTBlackList jwtBlackList) {
        this.jwtBlackList = jwtBlackList;
    }

    @Override
    public boolean preHandle(HttpServletRequest req,
                             HttpServletResponse res,
                             Object handler) throws Exception {

        String authHeader = req.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new BusinessException(ErrorCode.NOT_LOGIN, "没有权限，请先登录！");
        }

        String token = authHeader.substring(7);

        // 1. 检查黑名单
        if (jwtBlackList.contains(token)) {
            res.setCharacterEncoding("UTF-8");
            res.setContentType("application/json");
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            try (PrintWriter out = res.getWriter()) {
                out.write("{\"error\":\"Token 已被注销，请重新登录！\"}");
            }
            return false;
        }

        // 2. 解析并校验 JWT
        UserVo userVo;
        try {
            userVo = JWTUtils.parseToken(token);
        } catch (JWTDecodeException e) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "非法 Token 格式");
        }

        if (userVo == null) {
            res.setCharacterEncoding("UTF-8");
            res.setContentType("application/json");
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            try (PrintWriter out = res.getWriter()) {
                out.write("{\"error\":\"鉴权失败，请重新登录！\"}");
            }
            return false;
        }

        // 3. 放行，并把用户信息存入请求属性
        req.setAttribute("userVo", userVo);
        return true;
    }
}
