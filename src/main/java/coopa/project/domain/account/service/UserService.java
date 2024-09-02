package coopa.project.domain.account.service;

import coopa.project.domain.account.User;
import coopa.project.domain.account.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public boolean checkUser(String userCode) {
        return userRepository.existsByUserCode(userCode);
    }

    @Transactional
    public void payUser(String userCode, int payPoint) {
        User user = userRepository.findUserByUserCode(userCode);

        int beforePoint = user.getUserPoint();
        user.payPoint(payPoint);
        int afterPoint = user.getUserPoint();

        log.info("before : {}, payed : {}, after : {}", beforePoint, payPoint, afterPoint);
    }
}
