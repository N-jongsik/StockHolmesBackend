package com.example.wms.infrastructure.api;

import com.example.wms.infrastructure.service.ProductCrawler;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/crawling")
@RestController
public class CrawlerController {
    private final ProductCrawler productCrawler;
    @GetMapping()
    public String startCrawling() {
        productCrawler.init(); // 드라이버 초기화
        productCrawler.crawlProducts(); // 크롤링 시작
        productCrawler.close(); // 드라이버 종료
        return "크롤링 성공";
    }
}
