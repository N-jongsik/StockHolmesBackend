package com.example.wms.user.adapter.in;


import com.example.wms.user.adapter.in.dto.response.UserInfoResDto;
import com.example.wms.user.application.port.in.UserUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "user", description = "user domain apis")
@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class  UserController {
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
    @Operation(summary = "로그인한 회원 정보 조회", description = "로그인한 회원 정보를 조회합니다.")
    @GetMapping("")
    public ResponseEntity<UserInfoResDto> findUser() {
        return ResponseEntity.ok().body(userUseCase.findUser());
    }

    /**
     * 사번으로 회원 정보 조회
     */
    @Operation(summary = "사번으로 회원 정보 조회", description = "사번으로 회원 정보를 조회합니다.")
    @GetMapping("/staff-number")
    public ResponseEntity<UserInfoResDto> findUserByStaffNumber(@RequestParam String staffNumber) {
        return ResponseEntity.ok().body(userUseCase.findUserByStaffNumber(staffNumber));
    }

    /**
     * 모든 회원 정보 조회_
     */
    /**
     * 모든 회원 정보 조회
     */
    @Operation(summary = "모든 회원 정보 조회", description = "페이지네이션한 회원 정보를 조회합니다.")
    @GetMapping("/list")
    public ResponseEntity<Page<UserInfoResDto>> findAllUsers(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(userUseCase.findAllUsers(pageable));
    }
}
