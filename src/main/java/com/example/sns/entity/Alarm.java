package com.example.sns.entity;

import com.example.sns.entity.model.AlarmArgs;
import com.example.sns.enumerate.AlarmType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.type.SqlTypes;

import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE alarm SET deleted_at = NOW() where id = ?")
@Where(clause = "deleted_at is NULL")
public class Alarm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 알람을 받는 사람
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private AlarmType alarmType;

    @JdbcTypeCode(SqlTypes.JSON)
    private AlarmArgs args;

    private Timestamp registeredAt;

    private Timestamp updatedAt;

    private Timestamp deletedAt;

    @PrePersist
    void registeredAt() {
        this.registeredAt = Timestamp.from(Instant.now());
    }

    @PreUpdate
    void updatedAt() {
        this.updatedAt = Timestamp.from(Instant.now());
    }

    @Builder
    private Alarm(User user, AlarmType alarmType, AlarmArgs args) {
        this.assignUser(user);
        this.alarmType = alarmType;
        this.args = args;
    }

    public static Alarm of(User user, AlarmType alarmType, AlarmArgs args) {
        return Alarm.builder()
                .user(user)
                .alarmType(alarmType)
                .args(args)
                .build();
    }

    // Alarm N <-> 1 User
    // 양방향 연관관계 편의 메서드
    public void assignUser(User user) {
        if(this.getUser() != null) {
            this.user.getAlarms().remove(this);
        }

        this.user = user;

        if(!user.getAlarms().contains(this)) {
            user.addAlarm(this);
        }
    }
}
