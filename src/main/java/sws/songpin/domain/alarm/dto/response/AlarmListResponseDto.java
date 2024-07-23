package sws.songpin.domain.alarm.dto.response;

import sws.songpin.domain.alarm.entity.Alarm;

import java.util.List;
import java.util.stream.Collectors;

public record AlarmListResponseDto(
        List<AlarmUnitDto> alarmList
) {
    public static AlarmListResponseDto fromFollowAlarm(List<Alarm> alarmList) {
        List<AlarmUnitDto> alarmUnitDtos = alarmList.stream()
                .map(AlarmUnitDto::fromFollowAlarm)
                .collect(Collectors.toList());
        return new AlarmListResponseDto(alarmUnitDtos);
    }
}
