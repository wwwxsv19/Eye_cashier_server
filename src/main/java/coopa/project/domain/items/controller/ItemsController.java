package coopa.project.domain.items.controller;

import coopa.project.domain.items.Items;
import coopa.project.domain.items.controller.dto.ItemDto;
import coopa.project.domain.items.service.ItemsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/item")
@RequiredArgsConstructor
@Slf4j
public class ItemsController {
    private final ItemsService itemsService;
    private final RestTemplateBuilder restTemplate;

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

    /*
    이미지 파일 ( BASE64로 인코딩 ) 받아서 AI 에게 토스
    스캔된 JSON 받아서 아이템 리스트로 반환
    */
    @PostMapping("/scan")
    public ResponseEntity<?> getScanData(@RequestBody ItemDto.Request request) throws JSONException {
        log.info("Get Image to String and Change Json");

        String string = request.getImage();

        log.info("Image String : {}", string);

        String url = "http://10.150.149.6:8000/scan?img="; // 대충 AI url 일 듯

        String imageUrl = string.split(",")[1];
        byte[] imageBytes = Base64.getDecoder().decode(imageUrl);
        log.info("Decoded : {}", imageBytes);

        // 멀티파트 폼 데이터 생성
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new org.springframework.core.io.ByteArrayResource(imageBytes) {
            @Override
            public String getFilename() {
                return "image.jpg"; // 파일 이름 설정
            }
        });

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<ItemDto.ScanRequest> aiResponse = restTemplate.build().postForEntity(url, requestEntity, ItemDto.ScanRequest.class);
        log.info("{}", aiResponse.toString());
        List<ItemDto.Scan> resultList = aiResponse.getBody().getObjects();

        log.info("Get Image and Get Scan data");

        Map<String, ItemDto.Result> resultMap = new HashMap<>();

        log.info("Check Does ScanedItem Exists from itemList");

        for (ItemDto.Scan scan : resultList) {
            String itemName = scan.getName();

            if (resultMap.containsKey(itemName)) {
                ItemDto.Result result = resultMap.get(itemName);
                result.addQty();
                resultMap.replace(itemName, result);
            } else {
                try {
                    Items item = itemsService.getOneName(itemName);

                    ItemDto.Result result = ItemDto.Result.builder()
                            .itemId(item.getItemId())
                            .itemName(item.getItemName())
                            .itemPrice(item.getItemPrice())
                            .Qty(1)
                            .build();

                    resultMap.put(itemName, result);
                } catch (Exception e) {
                    continue;
                }
            }

            log.info("itemName : {}", itemName);
        }

        log.info("Check ScanList Finish");
        ItemDto.ScanResponse response = ItemDto.ScanResponse.builder()
                .resultList(resultMap)
                .build();


        return ResponseEntity.ok().body(response);
    }
}
