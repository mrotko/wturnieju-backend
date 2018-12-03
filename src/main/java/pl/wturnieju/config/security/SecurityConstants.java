package pl.wturnieju.config.security;

public class SecurityConstants {

    public static final String SECRET = "JwtSecretKey";
    public static final long EXPIRATION_TIME = 86_400_000;  // 24h
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/registration";
}
