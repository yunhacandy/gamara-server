package gamara.server.domain.entity;

import gamara.server.enums.SpicyLevel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "review")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    private long userId;

    private long storeId;

    private String context;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SpicyLevel spicyLevel;

    private int peanutLevel;

    private boolean hasSagol;

    private int tingleLevel;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}
