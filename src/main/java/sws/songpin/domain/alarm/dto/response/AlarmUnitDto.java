package sws.songpin.domain.alarm.dto.response;

import sws.songpin.domain.alarm.dto.ssedata.AlarmFollowDataDto;
import sws.songpin.domain.alarm.entity.Alarm;
import sws.songpin.domain.member.entity.Member;

import java.text.MessageFormat;
import java.time.LocalDateTime;

public record AlarmUnitDto(
        String message,
        LocalDateTime createdTime,
        Object data
) {
    public static AlarmUnitDto fromFollowAlarm(Alarm alarm) {
        Member sender = alarm.getSender();
        return new AlarmUnitDto(
                MessageFormat.format(alarm.getAlarmType().getMessagePattern(), sender.getNickname(), sender.getHandle()),
                alarm.getCreatedTime(),
                AlarmFollowDataDto.from(sender)
        );
    }
}