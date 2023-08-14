package server.api.kiwes.global.jwt.entity;

import org.springframework.data.redis.core.RedisHash;

import org.springframework.data.annotation.Id;

@RedisHash(value = "refreshToken", timeToLive = 60000)
public class RefreshToken {

    @Id
    private Long Id;
    private String refreshToken;


    public RefreshToken( String refreshToken, Long memberId) {
        this.refreshToken = refreshToken;
        this.Id = memberId;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public Long getMemberId() {
        return Id;
    }
}