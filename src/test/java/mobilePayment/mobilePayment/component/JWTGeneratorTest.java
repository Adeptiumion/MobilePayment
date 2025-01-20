package mobilePayment.mobilePayment.component;

import com.auth0.jwt.exceptions.TokenExpiredException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class JWTGeneratorTest {

    private JWTGenerator jwtGenerator;

    @BeforeEach
    public void init() {
        jwtGenerator = new JWTGenerator();
        jwtGenerator.setIssuer("test");
        jwtGenerator.setSubject("test");
        jwtGenerator.setIssuer("test");
        jwtGenerator.setSecret("test");
        jwtGenerator.setTokenLifeTime(1); // 1 минута.
    }

    // Тест на признание токена недействительным по истечению его срока жизни.
    @Test
    public void invalidateWhenTokenDie() throws InterruptedException {
        String token = jwtGenerator.generate("test", "test");
        Thread.sleep(60000); // Ждем минуту.
        assertThrows(TokenExpiredException.class, () -> jwtGenerator.validate(token));
    }

}
