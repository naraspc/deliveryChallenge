package com.challnege.delivery.domain.member.dto;

import com.challnege.delivery.domain.member.entity.Member;
import com.challnege.delivery.domain.member.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class MemberRequestDto {

    @Email
    @NonNull
    private String email;
    @Pattern(
            regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,15}$",
            message = "비밀번호는 알파벳 대소문자, 숫자, 특수문자를 모두 포함해야 합니다."
    )
    @NonNull
    private String password;
    @NonNull
    private String nickName;
    @NonNull
    private String phoneNumber;
    @NonNull
    private String address;
    @NonNull
    private Role role;

    public Member toEntity(String encryptedPassword) {
        return Member.builder()
                .email(this.email)
                .password(encryptedPassword)
                .nickName(this.nickName)
                .phoneNumber(this.phoneNumber)
                .address(this.address)
                .role(this.role)
                .build();
    }
}
