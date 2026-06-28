package com.vortexbird.orders.infrastructure.adapter.in.rest.controller;

import com.vortexbird.orders.infrastructure.adapter.in.rest.dto.request.LoginRequest;
import com.vortexbird.orders.infrastructure.adapter.in.rest.dto.response.AuthResponse;
import com.vortexbird.orders.infrastructure.adapter.in.rest.dto.response.UserResponse;
import com.vortexbird.orders.infrastructure.security.CustomUserDetails;
import com.vortexbird.orders.infrastructure.security.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/*
 * Controlador REST de autenticación login que emite el JWT y refresh.
 * Es el punto de entrada público, ya que el resto de endpoints exige token.
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login (@Valid @RequestBody LoginRequest loginRequest) {
        log.debug("POST /auth/login");
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password()));
        CustomUserDetails principal = (CustomUserDetails) auth.getPrincipal();
        // el rol ya está en el CustomUserDetails; lo extraemos limpio (sin el prefijo ROLE_)
        String role = principal.getAuthorities().iterator().next()
                .getAuthority().replace("ROLE_", "");
        String token = jwtService.generateToken(loginRequest.email(), role);
        return ResponseEntity.ok(new AuthResponse(token));
    }
    @GetMapping("/me")
    public ResponseEntity<UserResponse> me(@AuthenticationPrincipal CustomUserDetails principal) {
        String name = principal.getFirstName() + " " + principal.getLastName();
        return ResponseEntity.ok(new UserResponse(name, principal.getUsername()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@AuthenticationPrincipal CustomUserDetails principal) {
        log.debug("POST /auth/refresh");
        String role = principal.getAuthorities().iterator().next()
                .getAuthority().replace("ROLE_", "");
        String token = jwtService.generateToken(principal.getUsername(), role);
        return ResponseEntity.ok(new AuthResponse(token));
    }


}
