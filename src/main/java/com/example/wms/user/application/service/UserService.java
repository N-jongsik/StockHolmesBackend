package com.example.wms.user.application.service;

import com.example.wms.infrastructure.jwt.enums.JwtHeaderUtil;
import com.example.wms.user.adapter.in.dto.response.UserInfoResDto;
import com.example.wms.user.application.domain.User;
import com.example.wms.user.application.domain.enums.UserRole;
import com.example.wms.user.application.port.in.UserUseCase;
import com.example.wms.user.application.port.out.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.wms.infrastructure.security.util.SecurityUtils.getLoginUserStaffNumber;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserUseCase {

    private final AuthPort authPort;
    private final UserQueryPort userQueryPort;
    private final JwtTokenPort jwtTokenPort;
    private final RefreshTokenPort refreshTokenPort;
    private final UserPort userPort;

    public String generateStaffNumber(UserRole userRole) {
        String prefix = getRolePrefix(userRole);

        String lastStaffNumber = userQueryPort.findLastStaffNumberByRole(prefix);

        // lastStaffNumber가 없으면 처음 생성되는 사번으로 처리
        int nextNumber = 1;

        if (lastStaffNumber != null && lastStaffNumber.length() > prefix.length()) {
            String lastNumber = lastStaffNumber.substring(prefix.length());
            nextNumber = Integer.parseInt(lastNumber) + 1;
        }

        // 사번 포맷 (접두어 + 6자리 번호)
        String generatedStaffNumber = String.format("%s%06d", prefix, nextNumber);
        log.info("[사번 생성] 생성된 사번: {}", generatedStaffNumber);

        return generatedStaffNumber;
    }


    private String getRolePrefix(UserRole userRole) {
        log.debug("[사번 접두어] 역할: {}, 접두어: {}", userRole, userRole.name());
        switch (userRole) {
            case ROLE_WORKER:
                return "WO";
            case ROLE_SUPPLIER:
                return "SU";
            case ROLE_ADMIN:
                return "AD";
            default:
                return "US";
        }
    }

    @Override
    public void deleteUser() {
        String staffNumber = getLoginUserStaffNumber();
        User user = userPort.findByStaffNumber(staffNumber);

        log.debug("[회원 탈퇴] 탈퇴 요청. 로그인 유저 : {}", user.getStaffNumber());

        refreshTokenPort.deleteByStaffNumber(staffNumber);
        userPort.deleteUser(staffNumber);
    }

    @Override
    public UserInfoResDto findUser() {
        String staffNumber = getLoginUserStaffNumber();

        return UserInfoResDto.entityToResDto(userPort.findByStaffNumber(staffNumber));
    }

    @Override
    public List<UserInfoResDto> findAllUsers(int pageSize, int page) {

        if (page <= 0) {
            page = 1;
        }

        int offset = (page - 1) * pageSize;

        List<User> users = userPort.findAllUsers(pageSize, offset);
        return users.stream()
                .map(UserInfoResDto::entityToResDto)
                .collect(Collectors.toList());
    }

    /**
     * Access Token에서 Bearer 타입을 제거한 순수 Token 값 반환
     */
    private String parseAccessToken(String accessToken) {
        return accessToken.substring(JwtHeaderUtil.GRANT_TYPE.getValue().length());
    }
}
