package gamara.server.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "store")
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    private double latitude;

    private double longitude;

    private int recommendCount = 0;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public void incrementRecommendCount() {
        this.recommendCount++;
    }
    public void decrementRecommendCount() {
        this.recommendCount = Math.max(0, this.recommendCount - 1);
    }
}

