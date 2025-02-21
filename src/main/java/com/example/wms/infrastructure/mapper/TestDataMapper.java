package com.example.wms.infrastructure.mapper;

import java.util.List;

import com.example.wms.order.application.domain.Supplier;
import com.example.wms.outbound.application.domain.Outbound;
import com.example.wms.outbound.application.domain.OutboundPlan;
import com.example.wms.outbound.application.domain.OutboundPlanProduct;
import com.example.wms.product.application.domain.Product;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TestDataMapper {
    int batchInsertSupplier(List<Supplier> suppliers);
    int batchInsertProduct(List<Product> products);
    int batchInsertOutboundPlan(List<OutboundPlan> outboundPlans);
    int batchInsertOutboundPlanProduct(List<OutboundPlanProduct> planProducts);
    int batchInsertOutbound(List<Outbound> outbounds);
    int insertProduct(Product product);
    int insertOutboundPlan(OutboundPlan outboundPlan);
    int insertSupplier(Supplier supplier);
}
