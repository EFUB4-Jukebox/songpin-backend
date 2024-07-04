package sws.songpin.entity.alarm.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
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

    @Column(name = "message", length = 100, nullable = false)
    private String message;

    @Column(name = "target_mem_id")
    private Long targetMemId; // NULL일 수 있음

    @Column(name = "is_checked", nullable = false)
    @ColumnDefault("false")
    private Boolean isChecked;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, updatable = false)
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
