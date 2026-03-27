package com.qiraht.ticket_order.repository;

import com.qiraht.ticket_order.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, UUID> {
    List<Ticket> findAllByUserId(UUID userId);

    @Query("SELECT SUM(t.priceAtSales * t.quantity) FROM Ticket t WHERE t.event.id = :eventId AND t.status = 'BOOKED'")
    BigDecimal calculateRevenueByEventId(@Param("eventId") UUID eventId);
}

