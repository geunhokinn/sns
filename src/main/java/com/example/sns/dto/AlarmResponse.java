package com.example.sns.dto;

import com.example.sns.entity.Alarm;
import com.example.sns.entity.model.AlarmArgs;
import com.example.sns.enumerate.AlarmType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

public class AlarmResponse {

    @Getter
    @NoArgsConstructor
    public static class ReadDTO {

        private Long id;
        private AlarmType alarmType;
        private AlarmArgs alarmArgs;
        private String text;
        private Timestamp registeredAt;
        private Timestamp updatedAt;
        private Timestamp deletedAt;

        @Builder
        private ReadDTO(Long id, AlarmType alarmType, AlarmArgs alarmArgs, String text, Timestamp registeredAt, Timestamp updatedAt, Timestamp deletedAt) {
            this.id = id;
            this.alarmType = alarmType;
            this.alarmArgs = alarmArgs;
            this.text = text;
            this.registeredAt = registeredAt;
            this.updatedAt = updatedAt;
            this.deletedAt = deletedAt;
        }

        public static AlarmResponse.ReadDTO from(Alarm alarm) {
            return ReadDTO.builder()
                    .id(alarm.getId())
                    .alarmType(alarm.getAlarmType())
                    .alarmArgs(alarm.getArgs())
                    .text(alarm.getAlarmType().getAlarmText())
                    .registeredAt(alarm.getRegisteredAt())
                    .updatedAt(alarm.getUpdatedAt())
                    .deletedAt(alarm.getDeletedAt())
                    .build();
        }
    }
}
