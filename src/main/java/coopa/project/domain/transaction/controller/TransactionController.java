package coopa.project.domain.transaction.controller;

import coopa.project.domain.items.service.ItemsService;
import coopa.project.domain.transaction.controller.dto.PayDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final ItemsService itemsService;

    // 결제
    @PostMapping("/")
    public ResponseEntity<?> pay(@RequestBody PayDto.PayRequest request) {
        log.info("Payment Started");

        List<PayDto.Pay> payList = request.getPayReqList();
        String userCode = request.getUserCode();

        List<PayDto.Result> resultList = new ArrayList<>();

        for (PayDto.Pay pay : payList) {
            int itemId = pay.getItemId();

            /* User Pay Logic */
            /* Receipt Logic */

            PayDto.Result result = PayDto.Result.builder()
                    .itemId(itemId)
                    .isSuccess(true)
                    .build();

            resultList.add(result);
        }

        return ResponseEntity.ok().body(resultList);
    }
}
