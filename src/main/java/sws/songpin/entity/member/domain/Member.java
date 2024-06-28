package sws.songpin.entity.member.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import sws.songpin.entity.pin.domain.Pin;
import sws.songpin.global.BaseTimeEntity;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", updatable = false)
    private Long memberId;

    @Column(name = "email", nullable = false, unique = true)
    @Email
    @NotBlank
    private String email;

    @Column(name = "password", nullable = false)
    @NotBlank
    @Size(min = 8, max = 20)
    private String password;

    @Column(name = "nickname", nullable = false)
    @NotBlank
    private String nickname;

    @Column(name = "handle", nullable = false, unique = true)
    @NotBlank
    private String handle;

    @Column(name = "profile_image")
    private String profileImage;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "is_new_alarm", nullable = false)
    private boolean isNewAlarm;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pin> pins;

    @Builder
    public Member(Long memberId, String email, String password, String nickname, String handle, String profileImage, Status status, boolean isNewAlarm) {
        this.memberId = memberId;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.handle= handle;
        this.profileImage = profileImage;
        this.status = status;
        this.isNewAlarm = isNewAlarm;
    }

    public enum Status {
        ACTIVE,
        DELETED
    }
}
