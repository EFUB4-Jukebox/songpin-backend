package sws.songpin.domain.alarm.dto;

import sws.songpin.domain.alarm.entity.Alarm;

import java.util.List;
import java.util.stream.Collectors;

public record AlarmListResponseDto(
        List<AlarmUnitDto> alarmList
) {
    public static AlarmListResponseDto from(List<Alarm> alarmList) {
        List<AlarmUnitDto> alarmUnitDtos = alarmList.stream().map(alarm -> AlarmUnitDto.from(alarm))
                .collect(Collectors.toList());
        return new AlarmListResponseDto(alarmUnitDtos);
    }
}
