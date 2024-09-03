package coopa.project.domain.items.controller.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

public class ItemDto {
    @Getter
    public static class Scan {
        private int id;
        private String name;
        @Getter
        private static class Location {
            private int x;
            private int y;
        }
        private Location location;
    }

    @Getter
    public static class ScanRequest {
        private List<Scan> scanList;
    }

    @Builder
    @Getter
    public static class Result {
        private int itemId;
        private String itemName;
        private int itemPrice;
        private int Qty;

        public void addQty() {
            this.Qty += 1;
        }
    }

    @Builder
    @Getter
    public static class ScanResponse {
        private Map<Integer, Result> resultList;
    }
}
