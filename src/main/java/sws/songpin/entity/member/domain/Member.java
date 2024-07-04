package sws.songpin.entity.member.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @Column(name = "email", length = 50, unique = true)
    @NotNull
    @Email
    @NotBlank
    private String email;

    @Column(name = "password", length = 20)
    @NotNull
    @NotBlank
    private String password;

    @Column(name = "nickname", length = 10)
    @NotNull
    @NotBlank
    private String nickname;

    @Column(name = "handle", length = 12, unique = true)
    @NotNull
    @NotBlank
    private String handle;

    @Column(name = "profile_img", length = 30)
    @NotNull
    @Enumerated(EnumType.STRING)
    private ProfileImg profileImg;

    @Column(name = "status", length = 10)
    @NotNull
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "is_new_alarm")
    @NotNull
    private Boolean isNewAlarm;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pin> pins;

    @Builder
    public Member(Long memberId, String email, String password, String nickname, String handle) {
        this.memberId = memberId;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.handle= handle;
        this.profileImg = ProfileImg.POP;
        this.status = Status.ACTIVE;
        this.isNewAlarm = false;
        this.pins = new ArrayList<>();
    }
}
