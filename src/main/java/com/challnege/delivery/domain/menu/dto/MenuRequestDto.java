package com.challnege.delivery.domain.menu.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MenuRequestDto {

    @NotNull
    private String foodName;

    @NotNull
    @PositiveOrZero(message = "0이상의 수를 입력해야 합니다.")
    private int price;

}
