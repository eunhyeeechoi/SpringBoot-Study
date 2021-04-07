package com.eunhye.api.domain.measure;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MeasureRepository extends JpaRepository<Measure, Long> {
    // 기본적 CRUD 생성
}
