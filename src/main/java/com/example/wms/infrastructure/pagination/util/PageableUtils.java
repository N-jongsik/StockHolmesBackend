package com.example.wms.infrastructure.pagination.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PageableUtils {

    /**
     * 주어진 Pageable의 정렬 정보를 검증하여, 엔티티 클래스에 해당 필드가 존재하는 경우에만
     * camelCase 필드명을 snake_case로 변환한 새로운 Pageable을 반환합니다.
     * 만약 하나라도 엔티티에 존재하지 않는 필드가 있다면 IllegalArgumentException을 발생시킵니다.
     *
     * @param pageable    원본 Pageable (외부 입력)
     * @param entityClass 엔티티 클래스 (예: Product.class)
     * @return 안전한 정렬 조건이 적용된 Pageable
     * @throws IllegalArgumentException 유효하지 않은 정렬 조건이 포함된 경우
     */
    public static Pageable convertToSafePageableStrict(Pageable pageable, Class<?> entityClass) {
        // 정렬 조건이 없다면 그대로 반환
        if (!pageable.getSort().isSorted()) {
            return pageable;
        }

        List<Sort.Order> safeOrders = new ArrayList<>();

        // 기존 정렬 조건 순회
        for (Sort.Order order : pageable.getSort()) {
            String property = order.getProperty();

            // 엔티티에 해당 필드가 있는지 검사 (화이트 리스트 검증)
            if (!hasField(entityClass, property)) {
                // 존재하지 않는 필드가 있다면 예외 발생
                throw new IllegalArgumentException("Invalid sort property: " + property);
            }

            // camelCase → snake_case 변환
            String snakeCaseProperty = camelToSnake(property);
            safeOrders.add(new Sort.Order(order.getDirection(), snakeCaseProperty));
        }

        Sort safeSort = Sort.by(safeOrders);
        return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), safeSort);
    }

    public static Pageable convertToSafePageableStrict(Pageable pageable, Class<?> entityClass, Map<String, String> fieldMapping) {
        // 정렬 조건이 없다면 그대로 반환
        if (!pageable.getSort().isSorted()) {
            return pageable;
        }

        List<Sort.Order> safeOrders = new ArrayList<>();

        // 기존 정렬 조건 순회: 매핑 사전에 값이 있으면 그 값을 그대로 사용, 없으면 엔티티 필드 존재여부 확인 후 camelCase -> snake_case 변환
        for (Sort.Order order : pageable.getSort()) {
            String apiProperty = order.getProperty();
            String dbProperty;
            if (fieldMapping != null && fieldMapping.containsKey(apiProperty)) {
                // 매핑 사전에 정의된 값 사용 (snake_case 변환 없이)
                dbProperty = fieldMapping.get(apiProperty);
            } else {
                // 엔티티에 해당 필드가 있는지 확인
                if (!hasField(entityClass, apiProperty)) {
                    throw new IllegalArgumentException("Invalid sort property: " + apiProperty);
                }
                dbProperty = camelToSnake(apiProperty);
            }
            safeOrders.add(new Sort.Order(order.getDirection(), dbProperty));
        }

        Sort safeSort = Sort.by(safeOrders);
        return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), safeSort);
    }

    /**
     * 주어진 클래스에 지정한 이름의 필드가 존재하는지 확인합니다.
     *
     * @param clazz     엔티티 클래스
     * @param fieldName 필드 이름 (camelCase)
     * @return 필드가 존재하면 true, 아니면 false
     */
    private static boolean hasField(Class<?> clazz, String fieldName) {
        Class<?> current = clazz;
        while (current != null) {
            try {
                current.getDeclaredField(fieldName);
                return true;
            } catch (NoSuchFieldException e) {
                // 현재 클래스에 없으면 상위 클래스로 이동
                current = current.getSuperclass();
            }
        }
        return false;
    }


    /**
     * camelCase 문자열을 snake_case 문자열로 변환합니다.
     * 예: "createdDate" -> "created_date"
     *
     * @param str 변환할 문자열
     * @return snake_case로 변환된 문자열
     */
    private static String camelToSnake(String str) {
        return str.replaceAll("([a-z])([A-Z]+)", "$1_$2").toLowerCase();
    }
}
