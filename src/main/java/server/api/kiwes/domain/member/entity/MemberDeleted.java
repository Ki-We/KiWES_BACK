package server.api.kiwes.domain.member.entity;

import io.swagger.annotations.Api;
import lombok.*;
import server.api.kiwes.domain.BaseTimeEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Setter
public class MemberDeleted extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "MEMBER_DELETED_ID")
    private Long id;
    private String email;
}
