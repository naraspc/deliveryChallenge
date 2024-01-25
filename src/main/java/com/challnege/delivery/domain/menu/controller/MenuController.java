package com.challnege.delivery.domain.menu.controller;


import com.challnege.delivery.domain.menu.dto.MenuImageResponseDto;
import com.challnege.delivery.domain.menu.dto.MenuRequestDto;
import com.challnege.delivery.domain.menu.dto.MenuResponseDto;
import com.challnege.delivery.domain.menu.service.ImageS3Service;
import com.challnege.delivery.domain.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Controller
@RequestMapping("/restaurant")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;
    private final ImageS3Service imageS3Service;

    // 메뉴 화면
    @GetMapping("/{restaurantsId}")
    public String menuHome(
            @PathVariable Long restaurantsId,
            Model model) {
        model.addAttribute("restaurantsId", restaurantsId);
        return "menu";
    }

    // 메뉴 등록

    @PostMapping("/{restaurantsId}/menus")
    public String createMenu(
            @PathVariable Long restaurantsId, // RequestParam 고려해볼것
            @RequestPart MultipartFile image,
            @ModelAttribute MenuRequestDto menuRequestDto,
            @AuthenticationPrincipal UserDetails auth,
            Model model) throws IOException {
        String imageUrl = imageS3Service.saveFile(image);
        MenuResponseDto menuResponseDto = menuService.createMenu(restaurantsId, imageUrl, menuRequestDto, auth);
        model.addAttribute("menuResponse", menuResponseDto);
        return "menu"; // redirect html 만들어지면 싹 정리
    }


    // 메뉴 수정 (메뉴 이름, 가격)
    @PatchMapping("/{restaurantsId}/menus/{menuId}")
    public String updateMenu(
            @PathVariable Long restaurantsId,
            @PathVariable Long menuId,
            @ModelAttribute MenuRequestDto menuRequestDto,
            @AuthenticationPrincipal UserDetails auth,
            Model model) {
        MenuResponseDto menuResponseDto = menuService.updateMenu(restaurantsId, menuId, menuRequestDto, auth);
        model.addAttribute("menuResponse", menuResponseDto);

        return "menu";
    }

    // 메뉴 사진 수정
    @PatchMapping("/{restaurantsId}/menus/{menuId}/images")
    public String updateMenuImage(
            @PathVariable Long restaurantsId,
            @PathVariable Long menuId,
            @RequestPart MultipartFile image,
            @AuthenticationPrincipal UserDetails auth,
            Model model) throws IOException {
        String imageUrl = imageS3Service.saveFile(image);
        MenuImageResponseDto menuImageResponseDto = menuService.updateMenuImage(restaurantsId, menuId, imageUrl, auth);
        model.addAttribute("menuImageResponse", menuImageResponseDto);
        return "menu";
    }

    // 메뉴 삭제 (Delete는 서비스에서 안정성 문제가 있어서 고려해봐야함)
    @DeleteMapping ("/{restaurantsId}/menus/{menuId}")
    public String deleteMenu(@PathVariable Long restaurantsId, @PathVariable Long menuId,
                             @AuthenticationPrincipal UserDetails auth) {
        menuService.deleteMenu(restaurantsId, menuId, auth);
        return "menu";
    }
}
