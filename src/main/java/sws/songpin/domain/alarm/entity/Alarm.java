package sws.songpin.domain.alarm.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import sws.songpin.domain.member.entity.Member;
import sws.songpin.global.BaseTimeEntity;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Alarm extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_id", updatable = false)
    private Long alarmId;

    @Column(name = "message", length = 100)
    @NotNull
    private String message;

    @Column(name = "target_mem_id")
    private Long targetMemId;

    @Column(name = "is_checked")
    @NotNull
    @ColumnDefault("false")
    private Boolean isChecked;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", updatable = false)
    @NotNull
    private Member member;

    @Builder
    public Alarm(Long alarmId, String message, Long targetMemId, Boolean isChecked, Member member) {
        this.alarmId = alarmId;
        this.message = message;
        this.targetMemId = targetMemId;
        this.isChecked = isChecked;
        this.member = member;
    }
}
