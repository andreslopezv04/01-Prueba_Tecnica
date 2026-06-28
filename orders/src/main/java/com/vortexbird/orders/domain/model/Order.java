package com.vortexbird.orders.domain.model;

import com.vortexbird.orders.domain.exception.InvalidOrderStateException;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/*
 * Entidad de dominio que representa una Orden de Pago.
 * Su fin es contener los datos de la orden y proteger sus reglas de negocio,
 * en especial el ciclo de estados
 *
 * Relación: la operan los casos de uso el adaptador de persistencia
 * la reconstruye desde la base con reconstitute().
 *
 * Es es importante porque es
 * 1. El núcleo del sistema
 * 2. centraliza las invariantes para que ninguna orden pueda quedar en un estado ilegal.
*/

@Getter
public class Order {
    private Long id;
    private String code;
    private BigDecimal amount;
    private String description;
    private OrderStatus status;
    private String invoiceUrl;
    private final Long createdBy;
    private final LocalDateTime createdAt;
    private Long approvedBy;
    private LocalDateTime updatedAt;
    private Long lastModifiedBy;


    private Order(Long id, String code, BigDecimal amount, String description,
                  OrderStatus status, String invoiceUrl, Long createdBy,
                  LocalDateTime createdAt, Long approvedBy, LocalDateTime updatedAt, Long lastModifiedBy) {
        this.id = id;
        this.code = code;
        this.amount = amount;
        this.description = description;
        this.status = status;
        this.invoiceUrl = invoiceUrl;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.approvedBy = approvedBy;
        this.updatedAt = updatedAt;
        this.lastModifiedBy = lastModifiedBy;
    }

    public static Order create(String code, BigDecimal amount, String description,
                               Long createdBy) {
        LocalDateTime now = LocalDateTime.now();
        return new Order (null, code, amount, description, OrderStatus.PENDING,
                null, createdBy,now,null, now, createdBy);
    }

    public static Order reconstitute(Long id, String code, BigDecimal amount, String description,
                                     OrderStatus status, String invoiceUrl, Long createdBy,
                                     LocalDateTime createdAt, Long approvedBy, LocalDateTime updatedAt, Long lastModifiedBy){
        return new Order (id, code, amount, description, status,
                invoiceUrl, createdBy, createdAt, approvedBy, updatedAt, lastModifiedBy);
    }


    public void approve(Long approverId) {
        ensurePending();
        ensureHasInvoice();
        this.status = OrderStatus.APPROVED;
        this.approvedBy = approverId;
        this.updatedAt = LocalDateTime.now();
    }

    public void reject (Long rejectedBy) {
        ensurePending();
        this.status = OrderStatus.REJECTED;
        this.lastModifiedBy = rejectedBy;
        this.updatedAt = LocalDateTime.now();
    }

    public void attachInvoiceUrl(String url) {
        this.invoiceUrl = url;
        this.updatedAt = LocalDateTime.now();
    }

    private void ensurePending(){
        if (this.status != OrderStatus.PENDING) {
            throw new InvalidOrderStateException ("La orden no se puede aprobar ni rechazar porque está en estado " + status);
        }
    }

    private void ensureHasInvoice(){
        if (invoiceUrl == null || invoiceUrl.isBlank()){
            throw new InvalidOrderStateException ("No se puede aprobar una orden sin factura");
        }
    }
}
