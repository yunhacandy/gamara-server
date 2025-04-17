package gamara.server.domain.entity;

import gamara.server.enums.SpicyLevel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "store_statistics")
public class StoreStatistics {
    @Id
    private Long storeId;

    private int avgPeanutLevel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SpicyLevel avgSpicyLevel;

    private int avgTingleLevel;

    private boolean avgHasSagol;

    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
