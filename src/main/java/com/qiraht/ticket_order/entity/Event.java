package com.qiraht.ticket_order.entity;

import com.qiraht.ticket_order.constant.EventStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "events")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private String location;

    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer capacity;

    @Column(name = "available_slot",nullable = false)
    private Integer availableSlot;


    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private EventStatus status = EventStatus.ACTIVE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
