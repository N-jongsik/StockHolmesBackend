package com.example.wms.order.application.domain;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Supplier {
    private Long supplierId; // 공급자 고유 ID
    private String supplierName; // 공급자 이름
    private String businessNumber; // 사업자 등록 번호
    private String representativeName; // 대표자 이름
    private String address; // 공급자 주소
    private String supplierPhone; // 공급자 전화번호
    private String managerPhone; // 공급자 관리자 전화번호
}