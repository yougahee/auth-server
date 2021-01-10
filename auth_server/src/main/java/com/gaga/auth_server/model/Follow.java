package com.gaga.auth_server.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "follow")
public class Follow extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "follow_idx")
    private Long followIdx;

    @Column(name = "user_follower_idx")
    private Long userFollowerIdx;

    @Column(name = "user_streamer_idx")
    private Long userStreamerIdx;
}
