package io.github.PercivalGebashe.portfolio_project_showcase.model;

import io.github.PercivalGebashe.portfolio_project_showcase.model.enums.EmailValidationStatusEnum;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "email_validation_status", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class EmailValidationStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "email_validation_status_id")
    private Integer emailValidationStatusId;

    @Column(name = "status_description", nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private EmailValidationStatusEnum statusDescription;

    public EmailValidationStatusEnum getStatusEnum() {
        return EmailValidationStatusEnum.fromId(this.emailValidationStatusId);
    }
}
