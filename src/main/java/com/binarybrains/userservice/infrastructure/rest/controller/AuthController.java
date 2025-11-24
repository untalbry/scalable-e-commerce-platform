package com.binarybrains.userservice.infrastructure.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.binarybrains.userservice.core.ports.input.AuthService;
import com.binarybrains.userservice.infrastructure.rest.dto.RegisterDto;
import com.binarybrains.userservice.infrastructure.rest.dto.TokenDto;
import com.binarybrains.userservice.infrastructure.rest.dto.UserDto;
import com.binarybrains.userservice.utils.error.UserException;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping(value = "/api/auth", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class AuthController {
    @Autowired
    private final AuthService authService; 
    @ApiResponses({
        @ApiResponse(responseCode = "200",description = "User register successfully", content = {@Content(schema = @Schema(implementation = UserDto.class))}),
        @ApiResponse(responseCode = "400", description = "User all ready registered", content= @Content( schema= @Schema(example = "{\"error\": \"Ya existe este elemento.\"}"))),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = {@Content()})
    })
    @PostMapping("/register")
    public ResponseEntity<TokenDto> register(@RequestBody RegisterDto registerDto){
        return authService.register(registerDto.toEntity())
        .map(token -> ResponseEntity.ok(TokenDto.fromEntity(token)))
        .getOrElseGet(errorInfo -> {
            throw new UserException(errorInfo);
        });
    }
}
