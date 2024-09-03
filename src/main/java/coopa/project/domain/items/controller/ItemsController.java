package coopa.project.domain.items.controller;

import coopa.project.domain.items.Items;
import coopa.project.domain.items.controller.dto.ItemDto;
import coopa.project.domain.items.service.ItemsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/item")
@RequiredArgsConstructor
@Slf4j
public class ItemsController {
    private final ItemsService itemsService;

    // 상품 직접 입력 > 전체 목록
    @GetMapping("/")
    public ResponseEntity<?> getItems() {
        log.info("Get Item List");

        List<Items> itemList = itemsService.getAll();

        return ResponseEntity.ok().body(itemList);
    }

    // 상품 직접 입력 > 선택
    @GetMapping("/{itemId}")
    public ResponseEntity<?> getItemInfo(@PathVariable int itemId) {
        log.info("Get Item info by id");

        try {
            Items item = itemsService.getOne(itemId);
            return ResponseEntity.ok().body(item);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // 스캔된 JSON 받아서 아이템 리스트로 반환
    @PostMapping("/scan")
    public ResponseEntity<?> getScanData(@RequestBody ItemDto.ScanRequest request) {
        log.info("Get Scan Item");

        List<ItemDto.Scan> requestList = request.getScanList();

        Map<Integer, ItemDto.Result> resultMap = new HashMap<>();

        log.info("Check Does ScanedItem Exists from itemList");

        for (ItemDto.Scan scan : requestList) {
            int itemId = scan.getId();

            if (resultMap.containsKey(itemId)) {
                ItemDto.Result result = resultMap.get(itemId);
                result.addQty();
                resultMap.replace(itemId, result);
            } else {
                try {
                    Items item = itemsService.getOne(itemId);

                    ItemDto.Result result = ItemDto.Result.builder()
                            .itemId(itemId)
                            .itemName(item.getItemName())
                            .itemPrice(item.getItemPrice())
                            .Qty(1)
                            .build();

                    resultMap.put(itemId, result);
                } catch (Exception e) {
                    continue;
                }
            }
        }

        log.info("Check ScanList Finish");
        ItemDto.ScanResponse response = ItemDto.ScanResponse.builder()
                .resultList(resultMap)
                .build();


        return ResponseEntity.ok().body(response);
    }
}
