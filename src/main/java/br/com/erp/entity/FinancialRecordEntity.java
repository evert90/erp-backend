package br.com.erp.entity;

import br.com.erp.api.financialrecord.FinancialRecordType;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static javax.persistence.GenerationType.TABLE;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "financial_record")
@Entity
public class FinancialRecordEntity {

    @Id
    @GeneratedValue(strategy = TABLE, generator = "financialRecordGenerator")
    @TableGenerator(name = "financialRecordGenerator", table = "hibernate_sequences")
    private Long id;

    @Column
    private String name;

    @Column
    private String details;

    @Column
    private BigDecimal value;

    @Column
    @Enumerated(EnumType.STRING)
    private FinancialRecordType type;

    @Column
    private LocalDate date;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "financial_record_tags",
            joinColumns = {@JoinColumn(name = "id_financial_record")},
            inverseJoinColumns = {@JoinColumn(name = "id_tag")})
    @Column
    private List<TagEntity> tags;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

}
