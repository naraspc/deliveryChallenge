package com.challnege.delivery.domain.member.controller;

import com.challnege.delivery.domain.member.dto.MemberRequestDto;
import com.challnege.delivery.domain.member.dto.MemberResponseDto;
import com.challnege.delivery.domain.member.service.MemberService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    /**
     * 테스트 위해 모든 메서드 당, html 추가 및 그에 따른 getMapping 추가
     * html 논의 후 확립 되면
     * show Controller 삭제 후 정립 예정
     */
    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        model.addAttribute("memberRequestDto", new MemberRequestDto());
        return "signup";
    }

    @PostMapping("/signup")
    public String createMember(@Valid @ModelAttribute MemberRequestDto memberRequestDto) {
        MemberResponseDto memberResponseDto = memberService.createMember(memberRequestDto);
        return "redirect:/restaurants";
    }

    @GetMapping("/{memberId}")
    public String readMember(@PathVariable @Positive long memberId, Model model) {
        MemberResponseDto memberResponseDto = memberService.readMember(memberId);
        model.addAttribute("memberResponseDto", memberResponseDto);
        return "member";
    }

    @DeleteMapping("/delete/{memberId}")
    public String deleteMember(@PathVariable @Positive long memberId) {
        memberService.deleteMember(memberId);
        // 여기서 적절한 리다이렉션을 수행하거나 다른 동작을 수행할 수 있습니다.
        return "redirect:/index";
    }
}