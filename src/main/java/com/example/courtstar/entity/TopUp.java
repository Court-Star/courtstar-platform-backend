package com.example.courtstar.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "top_up")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TopUp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name="transaction_code")
    String transactionCode;

    @Column(name="zp_trans_id")
    String zpTransId;

    @Column(name = "date")
    LocalDate date;

    @Column(name = "status")
    boolean status;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "manager_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    CentreManager manager;
}
