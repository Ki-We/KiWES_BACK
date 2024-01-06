package server.api.kiwes.domain.club_category.entity;

import lombok.*;
import org.hibernate.mapping.ToOne;
import server.api.kiwes.domain.category.entity.Category;
import server.api.kiwes.domain.club.entity.Club;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClubCategory {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CLUB_CATEGORY_ID")
    private Long id;

    @OneToOne
    @JoinColumn(name = "CLUB_ID")
    private Club club;

    @OneToOne
    @JoinColumn(name = "CATEGORY_ID")
    private Category category;

}
