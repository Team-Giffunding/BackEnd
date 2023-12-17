package com.example.giffunding.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "gift")
public class Gift {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gift_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "name")
    private String name;
    @Column(name = "total_price")
    private Integer totalPrice;
    @Column(name = "price")
    private Integer price;
    @Column(name="photo_url")
    private String photoUrl;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @JsonIgnore
    @OneToMany(mappedBy = "gift", cascade = CascadeType.REMOVE)
    private List<Fund> funds = new ArrayList<>();

    @Builder
    public Gift(User user, String name, Integer totalPrice, String photoUrl) {
        this.user = user;
        this.name = name;
        this.totalPrice = totalPrice;
        this.price = 0;
        this.photoUrl = photoUrl;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void updateGiftPrice(Integer price) {
        this.price = price;
    }


}
