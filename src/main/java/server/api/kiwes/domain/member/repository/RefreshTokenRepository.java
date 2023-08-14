package server.api.kiwes.domain.member.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.stereotype.Repository;
import server.api.kiwes.global.jwt.entity.RefreshToken;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
public class RefreshTokenRepository {

    private RedisTemplate redisTemplate;

    @Value("${jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenValidityTime;

    public RefreshTokenRepository(final RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void save(String refreshToken, Long userId) {
        System.out.println("save");
        //동일한 key 값으로 저장하면 value값 updat됨
        redisTemplate.opsForValue().set(String.valueOf(userId), refreshToken, refreshTokenValidityTime, TimeUnit.SECONDS);
    }

    public void deleteById(Long userId){
        redisTemplate.delete(String.valueOf(userId));
    }

    public Optional<String> findById(final Long userId) {
        String refreshToken=(String)redisTemplate.opsForValue().get(String.valueOf(userId));
        return Optional.ofNullable(refreshToken);
    }
}
