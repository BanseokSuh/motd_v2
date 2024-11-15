package com.banny.motd.domain.event.infrastructure.entity;

import com.banny.motd.domain.event.Event;
import com.banny.motd.domain.event.EventType;
import com.banny.motd.domain.user.infrastructure.entity.UserEntity;
import com.banny.motd.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@NoArgsConstructor
@SQLRestriction("deleted_at IS NULL")
@SQLDelete(sql = "UPDATE \"post\" SET deleted_at = NOW() where id = ?")
@Table(name = "\"event\"")
@Entity
public class EventEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT")
    private Long id;

    @Column(name = "title", nullable = false, columnDefinition = "VARCHAR(50)")
    private String title;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "max_participants", nullable = false, columnDefinition = "INT")
    private int maxParticipants;

    @Column(name = "event_type", nullable = false, columnDefinition = "VARCHAR(10)")
    @Enumerated(EnumType.STRING)
    private EventType eventType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "register_start_at", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime registerStartAt;

    @Column(name = "register_end_at", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime registerEndAt;

    @Column(name = "event_start_at", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime eventStartAt;

    @Column(name = "event_end_at", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime eventEndAt;

    @Builder
    private EventEntity(Long id, String title, String description, int maxParticipants, EventType eventType, UserEntity user, LocalDateTime registerStartAt, LocalDateTime registerEndAt, LocalDateTime eventStartAt, LocalDateTime eventEndAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.maxParticipants = maxParticipants;
        this.eventType = eventType;
        this.user = user;
        this.registerStartAt = registerStartAt;
        this.registerEndAt = registerEndAt;
        this.eventStartAt = eventStartAt;
        this.eventEndAt = eventEndAt;
    }

    public static EventEntity from(Event event) {
        return EventEntity.builder()
                .id(event.getId())
                .title(event.getTitle())
                .description(event.getDescription())
                .maxParticipants(event.getMaxParticipants())
                .eventType(event.getEventType())
                .user(UserEntity.from(event.getManager()))
                .registerStartAt(event.getRegisterStartAt())
                .registerEndAt(event.getRegisterEndAt())
                .eventStartAt(event.getEventStartAt())
                .eventEndAt(event.getEventEndAt())
                .build();
    }

    public static EventEntity of(String title, String description, int maxParticipants, EventType eventType, LocalDateTime registerStartAt, LocalDateTime registerEndAt, LocalDateTime eventStartAt, LocalDateTime eventEndAt, UserEntity userEntity) {
        return EventEntity.builder()
                .title(title)
                .description(description)
                .maxParticipants(maxParticipants)
                .eventType(eventType)
                .registerStartAt(registerStartAt)
                .registerEndAt(registerEndAt)
                .eventStartAt(eventStartAt)
                .eventEndAt(eventEndAt)
                .user(userEntity)
                .build();
    }

    public Event toDomain() {
        return Event.builder()
                .id(id)
                .title(title)
                .description(description)
                .maxParticipants(maxParticipants)
                .eventType(eventType)
                .manager(user.toDomain())
                .registerStartAt(registerStartAt)
                .registerEndAt(registerEndAt)
                .eventStartAt(eventStartAt)
                .eventEndAt(eventEndAt)
                .createdAt(getCreatedAt())
                .updatedAt(getUpdatedAt())
                .deletedAt(getDeletedAt())
                .build();
    }

}
