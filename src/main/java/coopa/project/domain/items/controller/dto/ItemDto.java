package coopa.project.domain.items.controller.dto;

import lombok.Getter;

import java.util.List;

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
}
