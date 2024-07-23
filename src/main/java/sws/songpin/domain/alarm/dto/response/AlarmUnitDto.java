package sws.songpin.domain.alarm.dto.response;

import sws.songpin.domain.alarm.entity.Alarm;

import java.time.LocalDateTime;

public record AlarmUnitDto(
        Boolean isRead,
        String message,
        LocalDateTime createdTime,
        Long senderId
) {
    public static AlarmUnitDto from(Alarm alarm) {
        return new AlarmUnitDto(
                alarm.getIsRead(),
                alarm.getMessage(),
                alarm.getCreatedTime(),
                alarm.getSender().getMemberId()
        );
    }
}