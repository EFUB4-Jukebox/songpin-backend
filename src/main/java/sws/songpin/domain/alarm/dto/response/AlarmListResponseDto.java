package sws.songpin.domain.alarm.dto.response;

import java.util.List;

public record AlarmListResponseDto(
        List<AlarmUnitDto> alarmList
) {
    public static AlarmListResponseDto fromAlarmUnitDto(List<AlarmUnitDto> alarmUnitDtos) {
        return new AlarmListResponseDto(alarmUnitDtos);
    }
}
