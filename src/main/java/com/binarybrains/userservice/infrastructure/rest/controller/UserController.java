package com.binarybrains.userservice.infrastructure.rest.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.binarybrains.userservice.core.ports.input.UserService;
import com.binarybrains.userservice.infrastructure.rest.dto.UserDto;
import com.binarybrains.userservice.infrastructure.rest.dto.UserEmailDto;
import com.binarybrains.userservice.infrastructure.rest.dto.UserNameDto;
import com.binarybrains.userservice.utils.error.UserException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/user", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @Operation(
        summary = "Get user by id",
        description = "Gets user information by id"
    )
    @ApiResponse(responseCode = "200",description = "User found successfully", content = {@Content(schema = @Schema(implementation = UserDto.class))})
    @ApiResponse(responseCode = "404", description = "User not found", content= @Content( schema= @Schema(example = "{\"error\": \"User not found\"}")))
    @ApiResponse(responseCode = "500", description = "Internal server error", content = {@Content()})
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUsersById(@PathVariable @NotNull(message = "User id is required") Integer id) {
        return userService.getById(id)
        .map(user -> ResponseEntity.ok(UserDto.fromEntity(user)))
        .getOrElseGet(errorInfo -> {
            throw new UserException(errorInfo);
        });
    }
    @Operation(
        summary = "Patch user email",
        description = "Updates user email, and set validation email flag to false"
    )
    @ApiResponse(responseCode = "200", description = "User email update successfully", content = {@Content(schema = @Schema (example = "true"))})
    @ApiResponse(responseCode = "404", description = "User not found", content= @Content( schema= @Schema(example = "{\"error\": \"User not found\"}")))
    @ApiResponse(responseCode = "500", description = "Internal server error", content = {@Content()})
    @PatchMapping("/email")
    public ResponseEntity<Boolean> updateUserEmail(@Valid @RequestBody UserEmailDto userEmailDto){
        return userService.updateEmail(userEmailDto.toEntity())
                .map(ResponseEntity::ok)
                .getOrElseGet(errorInfo -> {
                    throw new UserException(errorInfo);
                });
    }
    @Operation(
        summary="Patch user name", 
        description="Updates user name"
    )
    @ApiResponse(responseCode="200", description="User name update successfully", content={@Content(schema = @Schema (example = "true"))})
    @ApiResponse(responseCode = "404", description = "User not found", content= @Content( schema= @Schema(example = "{\"error\": \"User not found\"}")))
    @ApiResponse(responseCode = "500", description = "Internal server error", content = {@Content()})
    @PatchMapping("/name")
    public ResponseEntity<Boolean> updateUserName( @Valid @RequestBody UserNameDto userNameDto){
        System.out.println(userNameDto.getName());
        return userService.updateName(userNameDto.toEntity())
            .map(ResponseEntity::ok)
            .getOrElseGet(errorInfo -> {
                throw new UserException(errorInfo);
            });
    }
} 
