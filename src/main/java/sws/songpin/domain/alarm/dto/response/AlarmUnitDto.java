package sws.songpin.domain.alarm.dto.response;

import sws.songpin.domain.alarm.entity.Alarm;

import java.time.LocalDateTime;

public record AlarmUnitDto(
        Boolean isRead,
        String message,
        LocalDateTime createdTime,
        String handle
) {
    public static AlarmUnitDto from(Alarm alarm, String message) {
        return new AlarmUnitDto(
                alarm.getIsRead(),
                message,
                alarm.getCreatedTime(),
                alarm.getSender().getHandle()
        );
    }
}