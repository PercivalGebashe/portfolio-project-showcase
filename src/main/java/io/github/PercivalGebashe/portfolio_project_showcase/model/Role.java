package io.github.PercivalGebashe.portfolio_project_showcase.model;

import io.github.PercivalGebashe.portfolio_project_showcase.model.enums.RoleEnum;
import jakarta.persistence.*;

@Entity
@Table(name = "roles", schema = "public")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Integer roleId;

    @Column(name = "role_description")
    private RoleEnum roleDescription;

    public RoleEnum getRoleEnum() {
        return RoleEnum.fromId(roleId);
    }
}
