package com.vortexbird.orders.infrastructure.adapter.in.rest.controller;

import com.vortexbird.orders.application.port.in.InvoiceContent;
import com.vortexbird.orders.application.port.in.OrderUseCase;
import com.vortexbird.orders.application.port.in.OrderView;
import com.vortexbird.orders.domain.exception.ResourceNotFoundException;
import com.vortexbird.orders.domain.model.OrderStatus;
import com.vortexbird.orders.infrastructure.adapter.in.rest.dto.request.CreateOrderRequest;
import com.vortexbird.orders.infrastructure.adapter.in.rest.dto.response.OrderResponse;
import com.vortexbird.orders.infrastructure.adapter.in.rest.mapper.OrderMapper;
import com.vortexbird.orders.infrastructure.security.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


/*
 * Controlador REST de órdenes para crear, listar/filtrar, detalle, aprobar, rechazar y factura.
 * Traduce HTTP a casos de uso.
 * Se encarga de aplicar el RBAC (@PreAuthorize) y la regla de que el operador solo ve sus propias órdenes.
 */
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderUseCase orderUseCase;
    private final OrderMapper orderMapper;

    @PostMapping
    @PreAuthorize("hasRole('OPERATOR')")
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody CreateOrderRequest createOrderRequest,
                                                     @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        log.debug("POST /orders - code: {}", createOrderRequest.code());
        OrderView order = orderUseCase.createOrder(createOrderRequest.code(),
                createOrderRequest.amount(), createOrderRequest.description(), customUserDetails.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(orderMapper.toResponse(order));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','OPERATOR')")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable Long id,
                                                  @AuthenticationPrincipal CustomUserDetails principal) {
        log.debug("GET /orders/{}", id);
        OrderView order = orderUseCase.findById(id).orElseThrow(()-> new ResourceNotFoundException("Order", id));
        if (isOperator(principal) && !order.order().getCreatedBy().equals(principal.getId())) {
            throw new AccessDeniedException("No tiene permiso para ver esta orden");   // → 403
        }
        return ResponseEntity.ok(orderMapper.toResponse(order));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','OPERATOR')")
    public ResponseEntity<List<OrderResponse>> getOrders(@RequestParam(required = false) OrderStatus status,
                                                         @RequestParam(required = false) String code,
                                                         @RequestParam(required = false) Boolean hasInvoice,
                                                         @AuthenticationPrincipal CustomUserDetails principal) {
        Long createdBy = isOperator(principal) ? principal.getId() : null;
        log.debug("GET /orders - status: {}, code: {}, hasInvoice: {}, createdBy: {}", status, code, hasInvoice, createdBy);
        List<OrderResponse> orders = orderUseCase.search(status, code , hasInvoice, createdBy)
                .stream().map(orderMapper::toResponse).toList();
        return ResponseEntity.ok(orders);
    }
    private boolean isOperator(CustomUserDetails principal) {
        return principal.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_OPERATOR"));
    }

    @PostMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderResponse> approveOrder(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        log.debug("POST /orders/{}/approve", id);
        OrderView order = orderUseCase.approveOrder(id, customUserDetails.getId());
        return ResponseEntity.ok(orderMapper.toResponse(order));
    }

    @PostMapping("/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderResponse> rejectOrder(@PathVariable Long id,
                                                     @AuthenticationPrincipal CustomUserDetails principal) {
        log.debug("POST /orders/{}/reject", id);
        OrderView order = orderUseCase.rejectOrder(id, principal.getId());
        return ResponseEntity.ok(orderMapper.toResponse(order));
    }

    @PostMapping("/{id}/invoice")
    @PreAuthorize("hasRole('OPERATOR')")
    public ResponseEntity<OrderResponse> uploadInvoice(@PathVariable Long id,
                                                       @RequestParam("file") MultipartFile file) throws IOException {
        log.debug("POST /orders/{}/invoice", id);
        OrderView order = orderUseCase.attachInvoice(id, file.getBytes(), file.getOriginalFilename());
        return ResponseEntity.ok(orderMapper.toResponse(order));
    }

    @GetMapping("/{id}/invoice")
    @PreAuthorize("hasAnyRole('ADMIN','OPERATOR')")
    public ResponseEntity<byte[]> getInvoice(@PathVariable Long id) {
        InvoiceContent invoice = orderUseCase.getInvoice(id);

        MediaType contentType = MediaTypeFactory.getMediaType(invoice.filename())
                .orElse(MediaType.APPLICATION_OCTET_STREAM);

        return ResponseEntity.ok()
                .contentType(contentType)                       // ej. application/pdf, image/png
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + invoice.filename() + "\"")
                .body(invoice.content());
    }


}
