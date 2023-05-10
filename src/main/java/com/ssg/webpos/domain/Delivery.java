package com.ssg.webpos.domain;

import com.ssg.webpos.domain.enums.DeliveryStatus;
import com.ssg.webpos.domain.enums.DeliveryType;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "delivery")
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Delivery extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_id")
    private Long id;

    @NotNull
    private LocalDateTime startedDate;

    private LocalDateTime finishedDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    @NotNull
    private String address;
    @NotNull
    @Enumerated(EnumType.STRING)
    private DeliveryType deliveryType;

    @NotNull
    private String phoneNumber;

    @NotNull
    private String deliveryName;
    @NotNull
    private String userName;
    private String requestInfo;

    @OneToOne(mappedBy = "delivery")
    private Order order;
}
