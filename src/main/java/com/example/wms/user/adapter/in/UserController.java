package com.example.wms.user.adapter.in;


import com.example.wms.user.adapter.in.dto.response.UserInfoResDto;
import com.example.wms.user.application.port.in.UserUseCase;
import com.example.wms.user.application.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "user", description = "user domain apis")
@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserUseCase userUseCase;

    @Value("${jwt.cookieName}")
    private String COOKIE_NAME;

    @Value("${jwt.refresh-expired-in}")
    private long REFRESH_TOKEN_EXPIRED_IN;

    /**
     * 회원 탈퇴
     */
    @Operation(summary = "회원 탈퇴", description = "회원(본인) 탈퇴 합니다.")
    @DeleteMapping
    public ResponseEntity<Void> deleteUser() {
        userUseCase.deleteUser();
        return ResponseEntity.ok().build();
    }

    /**
     * 회원 정보 조회
     */
    @Operation(summary = "회원 정보 조회", description = "회원 정보를 조회합니다.")
    @GetMapping
    public ResponseEntity<UserInfoResDto> findUser() {
        return ResponseEntity.ok().body(userUseCase.findUser());
    }


    /**
     * 모든 회원 정보 조회_
     */
    @Operation(summary = "모든 회원 정보 조회", description = "20명씩 페이지네이션한 회원 정보를 조회합니다.")
    @GetMapping("/list")
    public List<UserInfoResDto> findAllUsers(@RequestParam int page) {
        return userUseCase.findAllUsers(20, page);
    }
}
