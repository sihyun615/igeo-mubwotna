package com.sparta.igeomubwotna.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class RecipeRequestDto {

	@NotBlank
	private String title;

	@NotBlank
	private String content;
}
