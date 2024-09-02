package coopa.project.domain.account.repository;

import coopa.project.domain.account.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByUserCode(String userCode);

    User findUserByUserCode(String userCode);
}
