package uk.jinhy.server.service.hospital.domain;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


@Repository
@RequiredArgsConstructor
public class HospitalRepositoryCustomImpl implements HospitalRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QHospitalEntity hospitalEntity = QHospitalEntity.hospitalEntity;

    /**
     * 수술 가능 여부와 위치 기반으로 병원을 검색합니다.
     * QueryDSL을 사용하여 가독성과 유지보수성을 향상시켰습니다.
     */
    @Override
    public Page<HospitalEntity> findByFilters(
        Double lat,
        Double lon,
        Double radius,
        Boolean surgeryAvailable,
        Pageable pageable
        ) {

        List<HospitalEntity> content = queryFactory
            .selectFrom(hospitalEntity)
            .where(
                surgeryAvailableCondition(surgeryAvailable),
                locationWithinRadiusCondition(lat, lon, radius)
            )
            .offset(pageable.getOffset())  // 페이징 offset
            .limit(pageable.getPageSize()) // 페이징 limit
            .fetch();

        // 2. 전체 개수 조회 (페이징 없이)
        Long totalCount = queryFactory
            .select(hospitalEntity.count())
            .from(hospitalEntity)
            .where(
                surgeryAvailableCondition(surgeryAvailable),
                locationWithinRadiusCondition(lat, lon, radius)
            )
            .fetchOne();

        // 3. Page 객체 생성 및 반환
        return new PageImpl<>(content, pageable, totalCount != null ? totalCount : 0L);
    }

    /**
     * 수술 가능 여부 조건을 생성합니다.
     */
    private BooleanExpression surgeryAvailableCondition(Boolean surgeryAvailable) {
        return surgeryAvailable != null ? hospitalEntity.surgeryAvailable.eq(surgeryAvailable) : null;
    }

    /**
     * 하버사인 공식을 사용한 거리 조건을 생성합니다.
     * 지구를 구체로 가정하여 두 지점 간의 최단 거리를 계산합니다.
     */
    private BooleanExpression locationWithinRadiusCondition(Double lat, Double lon, Double radius) {
        if (lat == null || lon == null || radius == null) {
            return null;
        }

        // 하버사인 공식 (Haversine Formula) 적용
        // distance = 6371 * acos(cos(radians(lat1)) * cos(radians(lat2)) *
        //           cos(radians(lon2) - radians(lon1)) + sin(radians(lat1)) * sin(radians(lat2)))
        NumberExpression<Double> haversineDistance = Expressions.numberTemplate(Double.class,
            "6371 * acos(cos(radians({0})) * cos(radians({1})) * " +
                "cos(radians({2}) - radians({3})) + sin(radians({0})) * sin(radians({1})))",
            lat, hospitalEntity.latitude, hospitalEntity.longitude, lon
        );

        return haversineDistance.loe(radius);
    }
}

