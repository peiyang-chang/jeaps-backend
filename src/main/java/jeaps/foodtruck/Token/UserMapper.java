package jeaps.foodtruck.Token;


import jeaps.foodtruck.common.user.user.UserDTO;
import org.springframework.data.repository.query.Param;


public interface UserMapper {
    UserDTO findByUsername(@Param("username") String username);
}
