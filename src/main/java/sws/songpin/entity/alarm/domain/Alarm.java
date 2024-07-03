package sws.songpin.entity.alarm.domain;

import jakarta.persistence.*;
import lombok.*;
import sws.songpin.entity.member.domain.Member;
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

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "target_mem_id", nullable = false)
    private Long targetMemId;

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "is_checked", nullable = false)
    private boolean isChecked;

    @Builder
    public Alarm(Long alarmId, Member member, Long targetMemId, String message, boolean isChecked) {
        this.alarmId = alarmId;
        this.member = member;
        this.targetMemId = targetMemId;
        this.message = message;
        this.isChecked = isChecked;
    }
}
