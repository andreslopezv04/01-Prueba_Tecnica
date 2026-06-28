package com.vortexbird.orders.infrastructure.adapter.out.persistence.repository;

import com.vortexbird.orders.domain.model.OrderStatus;
import com.vortexbird.orders.infrastructure.adapter.out.persistence.entity.OrderJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/*
 * Repositorio Spring Data de órdenes.
 */
public interface OrderJpaRepository extends JpaRepository<OrderJpaEntity, Long> {
    @Query( "SELECT o " + "" +
            "FROM OrderJpaEntity o " +
            "WHERE " +
            "(:status IS NULL OR o.status = :status) AND " +
            "(:codePattern IS NULL OR LOWER(o.code) LIKE :codePattern) AND " +     // 👈 LIKE directo, sin CONCAT
            "(:hasInvoice IS NULL " +
            "   OR (:hasInvoice = true  AND o.invoiceUrl IS NOT NULL AND o.invoiceUrl <> '') " +
            "   OR (:hasInvoice = false AND (o.invoiceUrl IS NULL OR o.invoiceUrl = ''))) AND " +
            "(:createdBy IS NULL OR o.createdBy = :createdBy) " +
            "ORDER BY o.createdAt DESC")
    List<OrderJpaEntity> search (@Param("status") OrderStatus status,
                                 @Param("codePattern")String codePattern,
                                 @Param("hasInvoice") Boolean hasInvoice,
                                 @Param("createdBy") Long createdBy);

}
