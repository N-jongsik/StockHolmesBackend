package com.example.wms.inbound.application.port.out;

public interface AssignInboundNumberPort {
    String findMaxISNumber();
    String findMaxICNumber();
    String findMaxPANumber();

}
