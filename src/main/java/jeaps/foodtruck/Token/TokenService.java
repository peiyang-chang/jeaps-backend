package jeaps.foodtruck.Token;
// get token
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jeaps.foodtruck.common.user.user.User;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service("TokenService")
public class TokenService {

    public String getToken(User user) {
        // the token will expire after 5 mins (we can make it longer).
        // request re-login to refresh token.
        long EXPIRE_TIME = 5 * 60 * 1000;
        Date expireDate = new Date(System.currentTimeMillis() + EXPIRE_TIME);

        String token= JWT.create().withAudience(user.getUsername())// store username into token
                .withExpiresAt(expireDate) // Set up the expire time.
                .sign(Algorithm.HMAC256(user.getPassword()));// make password as token's key.
        return token;
    }
}
