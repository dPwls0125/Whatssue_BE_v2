package GDG.whatssue.repository;

import GDG.whatssue.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUserNick(String userNick);

//    User findByUserNickAndUserPw(String userNick, String userPw);

}
