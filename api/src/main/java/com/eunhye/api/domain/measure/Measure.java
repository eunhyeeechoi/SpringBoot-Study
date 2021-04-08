package com.eunhye.api.domain.measure;

import lombok.*;

import javax.persistence.*;

@Builder
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "measure1")
public class Measure {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long miPk;

    @Column(nullable = false, length = 30)
    private String oopTime;// 측정 시기

    @Column(nullable = false, length = 200)
    private String pauseTime; // 측정 일시

    @Column(nullable = false, length = 100)
    private int bloodMeasurement; //혈당 측정 값

    @Column(nullable = true, length = 200)
    private String measureMemo; // 메모

    @Column(nullable = false, length = 100)
    private String msrl; // 사용자 name 참조값
}
