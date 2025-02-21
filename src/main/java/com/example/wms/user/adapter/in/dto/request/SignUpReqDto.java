package com.example.wms.user.adapter.in.dto.request;

import com.example.wms.user.application.domain.User;
import com.example.wms.user.application.domain.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpReqDto {

    private String staffNumber; // role에 따라 생성
    private String password; //기본 비밀번호 설정
    private String userRole;
    private String name;
    private String phone;
    private String address;
    private String gender;
    private Boolean isActive;
    private String birthDate;
    private Long supplierId; //공급업체 여부


    public User dtoToEntity() {
        return User.builder()
                .staffNumber(staffNumber)
                .password(password)
                .name(name)
                .phone(phone)
                .address(address)
                .userRole(UserRole.getUserRole(userRole))
                .isActive(true)
                .gender(gender)
                .birthDate(birthDate)
                .supplierId(supplierId)
                .build();
    }
}
