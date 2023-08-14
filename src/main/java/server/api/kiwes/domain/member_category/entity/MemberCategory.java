package server.api.kiwes.domain.member_category.entity;

import lombok.Getter;
import lombok.Setter;
import server.api.kiwes.domain.category.entity.Category;
import server.api.kiwes.domain.member.entity.Member;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class MemberCategory {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_CATEGORY_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "CATEGORY_ID")
    private Category category;

}
