package com.challnege.delivery.domain.member.service;

import com.challnege.delivery.domain.member.dto.MemberRequestDto;
import com.challnege.delivery.domain.member.dto.MemberResponseDto;
import com.challnege.delivery.domain.member.entity.Member;
import com.challnege.delivery.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    public MemberResponseDto createMember(MemberRequestDto requestDto) {
        String encryptedPassword = passwordEncoder.encode(requestDto.getPassword());
        Member member = requestDto.toEntity(encryptedPassword);
        memberRepository.save(member);
        return MemberResponseDto.fromEntity(member);
    }

    @Transactional(readOnly = true)
    public MemberResponseDto readMember(long memberId) {
        Member member = findMemberById(memberId);
        return MemberResponseDto.fromEntity(member);
    }

    public void deleteMember(long memberId) {
        isMemberExist(memberId);
        memberRepository.deleteById(memberId);
    }

    public Member findMemberById(long memberId) {
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        Member member = optionalMember.orElseThrow(() -> new NullPointerException("존재하지 않는 계정입니다."));
        return member;
    }

    public void isMemberExist(long memberId) {
        boolean isExist = memberRepository.existsById(memberId);
        if (!isExist) {
            throw new NullPointerException("존재하지 않는 계정입니다.");
        }
    }

    public Member findMemberByEmail(String email) {
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        Member member = optionalMember.orElseThrow(() -> new UsernameNotFoundException("회원을 찾을 수 없습니다."));
        return member;
    }
}