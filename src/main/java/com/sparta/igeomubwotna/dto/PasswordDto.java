package com.sparta.igeomubwotna.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PasswordDto {
    @NotNull(message = "password는 null이 될 수 없습니다.")
    private String password;
}
