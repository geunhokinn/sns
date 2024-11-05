package com.example.sns.entity.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AlarmArgs {

    // 알람을 발생시킨 사람의 id
    private Long fromUserId;
    // 알람이 발생된 주체의 id // ex) post, comment
    private Long targetId;

    @Builder
    private AlarmArgs(Long fromUserId, Long targetId) {
        this.fromUserId = fromUserId;
        this.targetId = targetId;
    }

    public static AlarmArgs of(Long fromUserId, Long targetId) {
        return AlarmArgs.builder()
                .fromUserId(fromUserId)
                .targetId(targetId)
                .build();
    }
}
