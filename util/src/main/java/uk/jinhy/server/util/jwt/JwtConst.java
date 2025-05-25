package uk.jinhy.server.util.jwt;

public final class JwtConst {

    public static final long JWT_ACCESS_TOKEN_EXPIRATION_TIME = 10 * 60 * 1_000L;
    public static final long JWT_REFRESH_TOKEN_EXPIRATION_TIME = 7 * 24 * 60 * 60 * 1_000L;

    public static final String JWT_ISSUER = "MediPet";

    public static final String JWT_REFRESH_TOKEN_COOKIE_NAME = "refreshToken";

    public static final String BEARER = "Bearer ";

    public static final String JWT_REFRESH_TOKEN_COOKIE_PATH = "/api/auth";
}
