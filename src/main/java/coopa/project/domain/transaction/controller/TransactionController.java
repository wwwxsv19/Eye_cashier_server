package coopa.project.domain.transaction.controller;

import coopa.project.domain.account.User;
import coopa.project.domain.account.service.UserService;
import coopa.project.domain.items.Items;
import coopa.project.domain.items.service.ItemsService;
import coopa.project.domain.transaction.controller.dto.PayDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
@Slf4j
public class TransactionController {
    private final UserService userService;
    private final ItemsService itemsService;

    // 결제
    @PostMapping("/")
    public ResponseEntity<?> pay(@RequestBody PayDto.PayRequest request) {
        log.info("Payment Started");

        String userCode = request.getUserCode();
        List<PayDto.Pay> payList = request.getPayReqList();

        // 사용자 존재 여부 검사
        if (!userService.checkUser(userCode)) {
            return ResponseEntity.status(404).body("User Doesn't Exists");
        }

        // 결과 저장할 리스트
        List<PayDto.Result> resultList = new ArrayList<>();

        try {
            for (PayDto.Pay pay : payList) {
                int itemId = pay.getItemId();
                int itemQty = pay.getItemQty();

                log.info("find item");
                Items item = itemsService.getOne(itemId);

                log.info("pay");
                userService.payUser(userCode, item.getItemPrice() * itemQty);

                PayDto.Result result = PayDto.Result.builder()
                        .itemId(itemId)
                        .isSuccess(true)
                        .build();

                resultList.add(result);
            }
            User user = userService.getUser(userCode);
            int userPoint = user.getUserPoint();

            PayDto.PayResponse response = PayDto.PayResponse.builder()
                    .userPoint(userPoint)
                    .resultList(resultList)
                    .build();

            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while paying");
        }
    }
}
