package com.challnege.delivery.domain.review.entity;

import com.challnege.delivery.domain.member.entity.Member;
import com.challnege.delivery.domain.restaurant.entity.Restaurant;
import com.challnege.delivery.domain.review.dto.ReviewRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor

@Getter
@Setter
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @Column(nullable = false)
    private String nickname;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private int rating;

    // member 엔티티에 추가
//    @OneToMany(mappedBy = "member")
//    private List<Review> reviews;

    public Review(Restaurant restaurant, Member member, ReviewRequestDto requestDto) {
        if (restaurant == null || member == null) {
            throw new IllegalArgumentException("Restaurant and Member must not be null.");
        }
        this.restaurant = restaurant;
        this.member = member;
        this.nickname = requestDto.getNickname();
        this.content = requestDto.getContent();
        this.rating = requestDto.getRating();
    }
}
