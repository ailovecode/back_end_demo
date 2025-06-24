package edu.zut.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import edu.zut.common.ErrorCode;
import edu.zut.entity.vo.UserVo;
import edu.zut.exception.BusinessException;

import java.util.Date;

/**
 * Token 生成 及 解析
 */
public class JWTUtils {
    private static final String SECRET = "MyJwtSecretKeyZhy";
    private static final long EXPIRATION = 1000 * 60 * 60 * 24; // 1天
    private static final Algorithm ALGORITHM = Algorithm.HMAC256(SECRET);

    // 生成 Token，携带角色信息
    public static String generateToken(Long userId, String role) {
        long now = System.currentTimeMillis();
        return JWT.create()
                .withSubject(String.valueOf(userId))
                .withClaim("role", role)
                .withIssuedAt(new Date(now))
                .withExpiresAt(new Date(now + EXPIRATION))
                .sign(ALGORITHM);
    }

    // 解析 Token，返回用户ID和角色
    public static UserVo parseToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(ALGORITHM).build();
            DecodedJWT jwt = verifier.verify(token);

            Long userId = Long.parseLong(jwt.getSubject());
            String role = jwt.getClaim("role").asString();

            return new UserVo(userId, role);
        } catch (JWTVerificationException | NumberFormatException e) {
            // token 无效或过期
            throw new BusinessException(ErrorCode.NO_AUTH, "Token 无效或过期！");
        }
    }
}
