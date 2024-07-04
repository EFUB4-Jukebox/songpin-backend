package sws.songpin.entity.member.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import sws.songpin.entity.pin.domain.Pin;
import sws.songpin.global.BaseTimeEntity;

import java.util.ArrayList;
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

    @Column(name = "email", length = 50, nullable = false, unique = true)
    @Email
    @NotBlank
    private String email;

    @Column(name = "password", length = 20, nullable = false)
    @NotBlank
    private String password;

    @Column(name = "nickname", length = 10, nullable = false)
    @NotBlank
    private String nickname;

    @Column(name = "handle", length = 12, nullable = false, unique = true)
    @NotBlank
    private String handle;

    @Column(name = "profile_img", length = 30, nullable = false)
    @Enumerated(EnumType.STRING)
    @ColumnDefault("POP")
    private ProfileImg profileImg;

    @Column(name = "status", length = 10, nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "is_new_alarm", nullable = false)
    @ColumnDefault("false")
    private Boolean isNewAlarm;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pin> pins;

    @Builder
    public Member(Long memberId, String email, String password, String nickname, String handle,
                  ProfileImg profileImg, Status status, Boolean isNewAlarm) {
        this.memberId = memberId;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.handle = handle;
        this.profileImg = profileImg;
        this.status = status;
        this.isNewAlarm = isNewAlarm;
        this.pins = new ArrayList<>();
    }

}
