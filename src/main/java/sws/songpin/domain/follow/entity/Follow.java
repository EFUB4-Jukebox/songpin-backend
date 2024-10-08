package sws.songpin.domain.follow.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import sws.songpin.domain.member.entity.Member;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "follow_id", updatable = false)
    private Long followId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id", updatable = false)
    @NotNull
    private Member follower;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_id", updatable = false)
    @NotNull
    private Member following;

    @Builder
    public Follow(Long followId, Member follower, Member following) {
        this.followId = followId;
        this.follower = follower;
        this.following = following;
    }
}
