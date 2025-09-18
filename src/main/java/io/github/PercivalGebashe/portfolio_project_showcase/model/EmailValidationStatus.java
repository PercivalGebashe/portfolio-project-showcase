package io.github.PercivalGebashe.portfolio_project_showcase.model;

import io.github.PercivalGebashe.portfolio_project_showcase.model.enums.EmailValidationStatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "email_validation_status", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EmailValidationStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "email_validation_status_id")
    private Integer emailValidationStatusId;

    @Column(name = "status_description", nullable = false, unique = true)
    private String statusDescription;

    public EmailValidationStatusEnum getStatusEnum() {
        return EmailValidationStatusEnum.fromId(this.emailValidationStatusId);
    }
}
