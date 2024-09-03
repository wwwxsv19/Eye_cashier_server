package coopa.project.domain.items.controller.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class ItemDto {
    @Getter
    public static class Request {
        private String image;
    }

    @Getter
    public static class Scan {

        private String name;
        private int price;
        @Getter
        private static class Location {
            private int x;
            private int y;
        }
        private Location location;
        @Getter
        private static class Size {
            private int width;
            private int height;
        }
        private Size size;

    }

    @Getter
    public static class ScanRequest {
        private LocalDate time;
        private int count;
        private List<Scan> objects;
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
        private Map<String, Result> resultList;
    }
}
