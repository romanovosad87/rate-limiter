
package com.example.shorturontime.controller;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/short")
public class ShortUrlController {

    private final Map<String, String> urlMap = new ConcurrentHashMap<>();


    @PostMapping
    public ResponseEntity<String> getShortUrl(
            @RequestBody UrlRequestDto dto) {
        String key = generateKey();
        var url = generateShortUrl(key);
        urlMap.put(key, dto.url());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(url);
    }

    @GetMapping("/{key}")
    public ResponseEntity<?> redirectToOriginalUrl(@PathVariable String key) {
        String originalUrl = urlMap.get(key);
        if (originalUrl != null) {
            return ResponseEntity
                    .status(HttpStatus.MOVED_PERMANENTLY)
                    .location(URI.create(originalUrl))
                    .build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    private String generateShortUrl(String key) {
        return ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{key}")
                .buildAndExpand(key)
                .toString();
    }

    private String generateKey() {
        return RandomStringUtils.randomAlphanumeric(5);
    }
}

record UrlRequestDto(String url) {

}
