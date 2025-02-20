package com.example.wms.infrastructure.service;

import com.example.wms.infrastructure.mapper.ProductMapper;
import com.example.wms.product.application.domain.Product;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Random;

@Component
public class ProductCrawler {
    private WebDriver driver;
    private WebDriverWait wait;
    private final ProductMapper productMapper;

    public ProductCrawler(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    @PostConstruct
    public void init() {
        System.setProperty("webdriver.chrome.driver", "/Users/leejoohee/Desktop/chromedriver-mac-x64/chromedriver");

        ChromeOptions options = new ChromeOptions();
        // 브라우저 설정
        options.addArguments("--headless");                    // 헤드리스 모드
        options.addArguments("--disable-gpu");                 // GPU 비활성화
        options.addArguments("--no-sandbox");                  // 샌드박스 비활성화
        options.addArguments("--disable-popup-blocking");      // 팝업 차단
        options.addArguments("--disable-dev-shm-usage");       // 공유 메모리 사용 비활성화
        options.addArguments("--blink-settings=imagesEnabled=false");  // 이미지 로딩 비활성화
        options.addArguments("--remote-allow-origins=*");      // 원격 접근 허용

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @PreDestroy
    public void close() {
        if (driver != null) {
            driver.close();
            driver.quit();
        }
    }

    public void crawlProducts() {
        Map<String, String> categories = Map.of(
                "447", "전기/램프",
                "448", "엔진/미션",
                "449", "하체/바디",
                "550", "내장/외장",
                "551", "기타소모품"
        );

        try {
            for (String categoryNo : categories.keySet()) {
                int page = 1;
                boolean hasNextPage = true;

                while (hasNextPage) {
                    String url = String.format("https://m.hellowcar.com/product/list.account_bookhtml?cate_no=%s&page=%d",
                            categoryNo, page);
                    driver.get(url);

                    wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".prdList .item")));
                    List<WebElement> products = driver.findElements(By.cssSelector(".prdList .item"));

                    if (products.isEmpty()) {
                        hasNextPage = false;
                        continue;
                    }

                    for (WebElement productElement : products) {
                        processProduct(productElement, categories.get(categoryNo));
                    }

                    page++;
                }
            }
        } finally {
            driver.quit();
        }
    }

    private void processProduct(WebElement productElement, String category) {
        try {
            WebElement imgElement = productElement.findElement(By.cssSelector("img.thumb"));
            String imageUrl = imgElement.getAttribute("src");
            if (imageUrl.startsWith("//")) {
                imageUrl = "https:" + imageUrl;
            }

            // 제품명 & 가격 추출
            WebElement nameElement = productElement.findElement(By.cssSelector(".description .name a"));
            String productName = nameElement.getText();

            WebElement priceElement = productElement.findElement(By.cssSelector(".xans-product-listitem span"));
            String priceText = priceElement.getText().replaceAll("[^0-9]", "");
            Integer price = Integer.parseInt(priceText);

            // 제품 코드 추출 및 처리
            List<String> productCodes = extractProductCodes(productName);

            Random random = new Random();
            for (String productCode : productCodes) {
                // 공급업체 ID 결정
                Long supplierId = determineSupplierId(productCode);

                String detailCategory = determineDetailCategory(productCode);

                // 10~20% 사이의 랜덤한 마진율 계산
                double margin = 1.1 + (random.nextDouble() * 0.1); // 1.1 ~ 1.2 사이의 랜덤값
                int salePrice = (int)(price * margin);

                Product product = Product.builder()
                        .productCode(productCode)
                        .productName(productName)
                        .category(category)
                        .detailCategory(detailCategory)
                        .purchasePrice(price)
                        .salePrice(salePrice)
                        .stockLotCount(0)  // 초기 재고 0으로 설정
                        .supplierId(supplierId)
                        .build();

                productMapper.save(product);
            }
        } catch (Exception e) {
            System.err.println("상품 처리 중 오류 발생: " + e.getMessage());
        }
    }

    private String determineDetailCategory(String productCode) {
        if (productCode == null || productCode.length() < 5) {
            return "기타";
        }

        String prefix = productCode.substring(0, 3);

        // 엔진 파워트레인
        switch(prefix) {
            case "211": return "크랭크샤프트";
            case "215": return "엔진오일팬";
            case "243": case "244": return "밸브타이밍";
            case "251": return "라디에이터호스";
            case "253": return "라디에이터";
            case "254": return "냉각수탱크";
            case "255": return "써모스탯";
            case "263": return "오일필터";
            case "281": return "에어클리너";
            case "282": case "286": return "머플러";
            case "283": return "엔진매니폴드";
            case "284": return "배기매니폴드";
            case "351": return "스로틀바디";
            case "353": return "고압펌프";

            // 섀시/조향
            case "517": return "휠허브/액슬";
            case "527": return "휠허브";
            case "529": return "휠커버";
            case "546": return "쇼바";
            case "548": return "스태빌라이저";
            case "553": return "코일스프링";
            case "563": return "컬럼하우징";
            case "564": return "볼조인트";

            // 변속기/제동
            case "463": return "미션밸브바디";
            case "465": return "변속기";
            case "581": return "브레이크캘리퍼";
            case "582": return "브레이크패드";
            case "583": return "브레이크라이닝";
            case "584": return "브레이크";
            case "597": return "주차브레이크";

            // 전장품
            case "392": return "센서류";
            case "589": return "ABS모듈";
            case "819": return "폴딩키";
            case "857": return "전장부품";
            case "863": return "오디오";
            case "912": return "램프컨트롤러";
            case "924": return "전조등";
            case "954": return "스마트키";
            case "959": return "전자장치";
            case "961": return "전자제어";
            case "962": return "안테나";
            case "971": return "에어컨제어";

            // 차체/내외장
            case "310": return "주유캡";
            case "769": return "도어체커";
            case "821": return "도어트림";
            case "823": return "도어트림핀";
            case "826": return "도어캐치";
            case "851": return "룸미러";
            case "864": return "후드몰딩";
            case "876": return "사이드미러";
            case "877": return "사이드가니쉬";
            case "884": return "시트";
            case "885": return "시트커버";

            // 소모품
            case "983": case "985": return "와이퍼";
            case "988": return "와이퍼리필";

            default: return "기타";
        }
    }

    private Long determineSupplierId(String productCode) {
        if (productCode == null || productCode.length() < 5) {
            return 1L; // 기본값 현대모비스
        }

        String prefix = productCode.substring(0, 3);

        // 현대위아 (파워트레인, 섀시, 엔진 관련)
        if (prefix.startsWith("211") ||   // 엔진 크랭크샤프트
                prefix.startsWith("215") ||   // 엔진 오일팬
                prefix.startsWith("243") ||   // CVVT, 밸브 타이밍
                prefix.startsWith("244") ||   // 엔진 타이밍
                prefix.startsWith("251") ||   // 라디에이터 호스
                prefix.startsWith("253") ||   // 라디에이터
                prefix.startsWith("254") ||   // 냉각수 탱크
                prefix.startsWith("255") ||   // 써모스탯
                prefix.startsWith("263") ||   // 오일필터
                prefix.startsWith("281") ||   // 에어클리너/엔진부품
                prefix.startsWith("282") ||   // 머플러, 배기
                prefix.startsWith("283") ||   // 엔진 매니폴드
                prefix.startsWith("284") ||   // 배기 매니폴드
                prefix.startsWith("286") ||   // 머플러
                prefix.startsWith("351") ||   // 스로틀바디
                prefix.startsWith("353") ||   // 고압펌프
                prefix.startsWith("517") ||   // 휠볼트, 액슬, 허브
                prefix.startsWith("527") ||   // 휠 허브
                prefix.startsWith("529") ||   // 휠 캡
                prefix.startsWith("563") ||   // 컬럼하우징/스티어링
                prefix.startsWith("564")) {   // 볼조인트/스티어링
            return 2L;
        }

        // 현대트랜시스 (변속기, 제동, 시트 관련)
        if (prefix.startsWith("463") ||   // 미션 밸브바디
                prefix.startsWith("465") ||   // 변속기 관련
                prefix.startsWith("581") ||   // 브레이크 캘리퍼
                prefix.startsWith("582") ||   // 브레이크 패드
                prefix.startsWith("583") ||   // 브레이크 라이닝
                prefix.startsWith("584") ||   // 브레이크 시스템
                prefix.startsWith("597") ||   // 파킹 브레이크
                prefix.startsWith("821") ||   // 도어트림
                prefix.startsWith("823") ||   // 도어트림 핀
                prefix.startsWith("826") ||   // 도어 캐치
                prefix.startsWith("884") ||   // 시트 관련
                prefix.startsWith("885")) {   // 시트 커버
            return 3L;
        }

        // 현대케피코 (전자제어, 전장품)
        if (prefix.startsWith("392") ||   // 센서류
                prefix.startsWith("589") ||   // ABS/ESP 모듈
                prefix.startsWith("819") ||   // 폴딩키
                prefix.startsWith("863") ||   // 오디오
                prefix.startsWith("857") ||   // 전장품
                prefix.startsWith("912") ||   // 램프컨트롤
                prefix.startsWith("924") ||   // 전조등/램프
                prefix.startsWith("954") ||   // 스마트키
                prefix.startsWith("959") ||   // 전자장치
                prefix.startsWith("961") ||   // 전자제어
                prefix.startsWith("962") ||   // 안테나
                prefix.startsWith("971")) {   // 히터컨트롤/에어컨
            return 4L;
        }

        // 현대모비스 (기본 부품 및 소모품)
        // 310XX - 주유캡/연료캡
        // 546XX - 쇼바/서스펜션
        // 548XX - 스태빌라이저
        // 553XX - 코일스프링
        // 769XX - 도어체커
        // 851XX - 룸미러
        // 864XX - 후드몰딩
        // 876XX - 사이드미러
        // 877XX - 사이드 가니쉬
        // 983XX - 와이퍼
        // 985XX - 와이퍼블레이드
        // 988XX - 와이퍼 리필
        return 1L;
    }

    private List<String> extractProductCodes(String text) {
        List<String> codes = new ArrayList<>();

        // 제품 코드 패턴
        Pattern pattern = Pattern.compile("\\b[0-9][A-Z0-9]{4,}\\b");
        Matcher matcher = pattern.matcher(text.toUpperCase());

        while (matcher.find()) {
            codes.add(matcher.group());
        }

        return codes;
    }
}