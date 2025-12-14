package com.binarybrains.userservice.infrastructure.rest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.binarybrains.userservice.core.ports.input.AuthService;
import com.binarybrains.userservice.infrastructure.rest.dto.EmailVerificationConfirmDto;
import com.binarybrains.userservice.infrastructure.rest.dto.LoginDto;
import com.binarybrains.userservice.infrastructure.rest.dto.RegisterDto;
import com.binarybrains.userservice.infrastructure.rest.dto.TokenDto;
import com.binarybrains.userservice.infrastructure.rest.dto.UserDto;
import com.binarybrains.userservice.infrastructure.rest.dto.EmailVerificationRequestDto;
import com.binarybrains.userservice.utils.error.ErrorInfo;
import com.binarybrains.userservice.utils.error.UserException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/api/auth", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    @Operation(
        summary = "User register",
        description = "Register a new user and returns an access token."
    )
    @ApiResponse(responseCode = "200",description = "User register successfully", content = {@Content(schema = @Schema(implementation = UserDto.class))})
    @ApiResponse(responseCode = "400", description = "Business validation error", content= @Content( schema= @Schema(example = "{\"error\": \"Ya existe este elemento.\"}")))
    @ApiResponse(responseCode = "500", description = "Internal server error", content = {@Content()})
    @PostMapping("/register")
    public ResponseEntity<TokenDto> register(@RequestBody @Valid RegisterDto registerDto){
        return authService.register(registerDto.toEntity())
        .map(token -> ResponseEntity.ok(TokenDto.fromEntity(token)))
        .getOrElseGet(errorInfo -> {
            throw new UserException(errorInfo);
        });
    }
    @Operation(
        summary = "User login",
        description = "Authenticates a user using their credentials and returns an access token."
    )
    @ApiResponse(responseCode = "200",description = "User login successfully", content = {@Content(schema = @Schema(implementation = UserDto.class))})
    @ApiResponse(responseCode = "400", description = "Business validation error", content= @Content( schema= @Schema(example = "{\"error\": \"Credenciales invalidas.\"}")))
    @ApiResponse(responseCode = "500", description = "Internal server error", content = {@Content()})
    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody @Valid LoginDto loginDto){
        return authService.login(loginDto.toEntity())
        .map(token -> ResponseEntity.ok(TokenDto.fromEntity(token)))
        .getOrElseGet(error -> {
            throw new UserException(error);
        });
    }
    @Operation(
        summary = "Request email verification",
        description = "Sends a verification code to the provided email address."
    )
    @ApiResponse(responseCode = "200", description = "Verification email sent successfully", content = @Content(mediaType = "application/json",schema = @Schema(example = "true")))
    @ApiResponse(responseCode = "400",description = "Business validation error",content = @Content(mediaType = "application/json",schema = @Schema(example = "{\"error\": \"Invalid email format\"}")))
    @ApiResponse(responseCode = "404",description = "Email not found", content = @Content(mediaType = "application/json",schema = @Schema(example = "{\"error\": \"User not found\"}")))
    @ApiResponse(responseCode = "500",description = "Internal server error",content = @Content)
    @PostMapping("/email-verification")
    public ResponseEntity<Boolean> requestEmailVerification(@RequestBody @Valid EmailVerificationRequestDto email) {
        return authService.sendValidationCode(email.getEmail())
        .map(ResponseEntity::ok)
        .getOrElseGet(
            error -> {
                throw new UserException(error);
            }
        );
    } 
    @Operation(
        summary = "Confirm email verification",
        description = "Validates the verification token sent to the user's email and marks the email as verified."
    )
    @ApiResponse(responseCode = "200",description = "Email verified successfully",content = @Content(mediaType = "application/json",schema = @Schema(example = "true")))
    @ApiResponse(responseCode = "400",description = "Business validation error",content = @Content(mediaType = "application/json",schema = @Schema(implementation=ErrorInfo.class), examples= {@ExampleObject(name="Código de verificación no valido", value = " {code: RN007, message: Código de verificación no valido.}"), @ExampleObject(name="Código de verificación no valido", value = " {code: RN007, message: Código de verificación no valido.}")}))
    @ApiResponse(responseCode = "500",description = "Internal server error",content = @Content)
    @PostMapping("/email-validation/confirm")
    public ResponseEntity<Boolean> confirmEmailVenrification(@RequestBody  @Valid EmailVerificationConfirmDto email){
        return authService.confirmValidationCode(email.toEntity())
        .map(ResponseEntity::ok)
        .getOrElseGet(
            error -> {
                throw new UserException(error);
            }
        );
    }
}
