package com.challnege.delivery.domain.menu.service;


import com.challnege.delivery.domain.member.entity.Member;
import com.challnege.delivery.domain.member.service.MemberService;
import com.challnege.delivery.domain.menu.dto.MenuImageResponseDto;
import com.challnege.delivery.domain.menu.dto.MenuRequestDto;
import com.challnege.delivery.domain.menu.dto.MenuResponseDto;
import com.challnege.delivery.domain.menu.entity.Menu;
import com.challnege.delivery.domain.menu.repository.MenuRepository;
import com.challnege.delivery.domain.restaurant.entity.Restaurant;
import com.challnege.delivery.domain.restaurant.repository.RestaurantRepository;
import com.challnege.delivery.domain.restaurant.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final RestaurantService restaurantService;
    private final MemberService memberService;

    // 메뉴 등록
    @Transactional
    public MenuResponseDto createMenu(Long restaurantsId, String imageUrl, MenuRequestDto menuRequestDto, UserDetails auth) {
        Restaurant restaurant = restaurantService.findRestaurantByResId(restaurantsId);

        Member member = memberService.findMemberByEmail(auth.getUsername());

        isValidOwner(restaurant, member.getMemberId());

        Menu menu = new Menu(restaurant, imageUrl, menuRequestDto);
        Menu createMenu = menuRepository.save(menu);
        return new MenuResponseDto(createMenu);
    }

    // 메뉴목록 전체 조회
    @Transactional(readOnly = true)
    public List<MenuResponseDto> readAllMenu(Long restaurantsId) {
        Restaurant restaurant = restaurantService.findRestaurantByResId(restaurantsId);
        return menuRepository.findAllByRestaurant(restaurant).stream().map(MenuResponseDto::new).toList();
    }


    // 메뉴 수정
    @Transactional
    public MenuResponseDto updateMenu(Long restaurantsId, Long menuId, MenuRequestDto menuRequestDto, UserDetails auth) {
        Restaurant restaurant = restaurantService.findRestaurantByResId(restaurantsId);

        Member member = memberService.findMemberByEmail(auth.getUsername());
        isValidOwner(restaurant, member.getMemberId());

        Menu menu = findMenuById(menuId);
        menu.updateMenu(restaurant, menuRequestDto);

        return new MenuResponseDto(menu); // 빌더패턴? -> 등록일말고 수정일만 넘기게
    }

    // 메뉴 이미지 수정
    @Transactional
    public MenuImageResponseDto updateMenuImage(Long restaurantsId, Long menuId, String imageUrl, UserDetails auth) {
        Restaurant restaurant = restaurantService.findRestaurantByResId(restaurantsId);
        Menu menu = findMenuById(menuId);

        Member member = memberService.findMemberByEmail(auth.getUsername());
        isValidOwner(restaurant, member.getMemberId());

        menu.updateMenuImage(restaurant, imageUrl);

        return new MenuImageResponseDto(menu);
    }

    // 메뉴 삭제
    @Transactional
    public void deleteMenu(Long restaurantsId, Long menuId, UserDetails auth) {
        Menu menu = findMenuById(menuId);
        Restaurant restaurant = restaurantService.findRestaurantByResId(restaurantsId);

        Member member = memberService.findMemberByEmail(auth.getUsername());
        isValidOwner(restaurant, member.getMemberId());

        restaurant.getMenu().remove(menu);
        menuRepository.delete(menu);
    }

    @Transactional(readOnly = true)
    public Menu findMenuById(Long menuId) {
        Menu menu = menuRepository.findById(menuId).orElseThrow(
                () -> new NoSuchElementException("메뉴를 찾을 수 없습니다."));
        return menu;
    }

    @Transactional(readOnly = true)
    public void isValidOwner(Restaurant restaurant, long memberId) {
        if (!restaurant.getMember().getMemberId().equals(memberId)) {
            throw new NoSuchElementException("식당 소유주만 메뉴 등록,수정,삭제 가능합니다.");
        }
    }
}
