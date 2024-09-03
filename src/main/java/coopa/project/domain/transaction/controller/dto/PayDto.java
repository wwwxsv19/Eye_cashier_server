package coopa.project.domain.transaction.controller.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class PayDto {
    @Getter
    public static class Pay {
        private int itemId;
        private int itemQty; // 결제 요청 수량
    }

    @Getter
    public static class PayRequest {
        private String userCode;
        private List<Pay> payReqList;
    }

    @Builder
    @Getter
    public static class Result {
        private int itemId;
        private boolean isSuccess;
    }

    @Builder
    @Getter
    public static class PayResponse {
        private int userPoint;
        private List<Result> resultList;
    }
}
