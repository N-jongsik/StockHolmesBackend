package com.example.wms.user.application.port.out;

import com.example.wms.user.application.domain.User;

public interface AuthPort {
    User save(User user);
    User findByStaffNumber(String staffNumber);
    boolean isStaffNumberDuplicated(String staffNumber);
}
