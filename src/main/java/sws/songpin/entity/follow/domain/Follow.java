package sws.songpin.entity.follow.domain;

import jakarta.persistence.*;
import lombok.*;
import sws.songpin.entity.member.domain.Member;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "follow_id", updatable = false)
    private Long followId;

    @ManyToOne
    @JoinColumn(name = "follower_id", nullable = false)
    private Member follower;

    @ManyToOne
    @JoinColumn(name = "following_id", nullable = false)
    private Member following;

    @Builder
    public Follow(Long followId, Member follower, Member following) {
        this.followId = followId;
        this.follower = follower;
        this.following = following;
    }
}
