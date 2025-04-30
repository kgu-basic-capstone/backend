package uk.jinhy.server.api.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uk.jinhy.server.api.pet.domain.Pet;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PetTest {

    @DisplayName("반려동물 건강관리 기록을 저장할 수 있다.")
    @Test
    void 건강관리_기록_저장() {
        // given
        User owner = User.builder()
            .username("testUser")
            .build();
        Pet sut = Pet.builder()
            .owner(owner)
            .name("멍멍이")
            .birthDate(LocalDate.now())
            .build();

        HealthRecord healthRecord = HealthRecord.builder()
            .pet(sut)
            .checkDate(LocalDateTime.now())
            .weight(1.0)
            .notes("첫 건강 검진")
            .build();

        // when
        HealthRecord result = sut.saveHealthRecord(healthRecord);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getPet()).isEqualTo(sut);
        assertThat(sut.getHealthRecords()).contains(result);
    }

    @DisplayName("반려동물 건강관리 기록을 삭제할 수 있다.")
    @Test
    void 건강관리_기록_삭제() {
        // given
        User owner = User.builder()
            .username("testUser")
            .build();
        Pet sut = Pet.builder()
            .owner(owner)
            .name("멍멍이")
            .birthDate(LocalDate.now())
            .build();

        HealthRecord healthRecord1 = HealthRecord.builder()
            .pet(sut)
            .checkDate(LocalDateTime.now())
            .weight(1.0)
            .notes("첫 건강 검진")
            .build();

        HealthRecord healthRecord2 = HealthRecord.builder()
            .pet(sut)
            .checkDate(LocalDateTime.now().plusDays(1))
            .weight(1.2)
            .notes("두 번째 건강 검진")
            .build();

        sut.saveHealthRecord(healthRecord1);
        sut.saveHealthRecord(healthRecord2);

        // when
        HealthRecord deletedRecord = sut.deleteHealthRecord(healthRecord1);

        // then
        assertThat(deletedRecord).isEqualTo(healthRecord1);
        assertThat(sut.getHealthRecords()).doesNotContain(healthRecord1);
        assertThat(sut.getHealthRecords()).contains(healthRecord2);
    }

    @DisplayName("반려동물 건강관리 기록을 수정할 수 있다.")
    @Test
    void 건강관리_기록_수정() {
        // given
        User owner = User.builder()
            .username("testUser")
            .build();
        Pet sut = Pet.builder()
            .owner(owner)
            .name("멍멍이")
            .birthDate(LocalDate.now())
            .build();

        HealthRecord originalRecord = HealthRecord.builder()
            .pet(sut)
            .checkDate(LocalDateTime.now())
            .weight(1.0)
            .notes("첫 건강 검진")
            .build();

        sut.saveHealthRecord(originalRecord);

        // when
        HealthRecord updatedRecord = sut.updateHealthRecord(originalRecord,
            HealthRecord.builder()
                .pet(sut)
                .checkDate(originalRecord.getCheckDate())
                .weight(1.5)
                .notes("체중 증가 확인")
                .build()
        );

        // then
        assertThat(updatedRecord).isNotNull();
        assertThat(updatedRecord.getWeight()).isEqualTo(1.5);
        assertThat(updatedRecord.getNotes()).isEqualTo("체중 증가 확인");
        assertThat(sut.getHealthRecords()).contains(updatedRecord);
        assertThat(sut.getHealthRecords()).doesNotContain(originalRecord);
    }

    @DisplayName("특정 날짜 이후의 건강관리 기록을 조회할 수 있다.")
    @Test
    void 특정_날짜_이후_건강관리_기록_조회() {
        // given
        User owner = User.builder()
            .username("testUser")
            .build();
        Pet sut = Pet.builder()
            .owner(owner)
            .name("멍멍이")
            .birthDate(LocalDate.now())
            .build();

        HealthRecord record1 = HealthRecord.builder()
            .pet(sut)
            .checkDate(LocalDateTime.now().minusDays(10))
            .weight(1.0)
            .notes("첫 건강 검진")
            .build();

        HealthRecord record2 = HealthRecord.builder()
            .pet(sut)
            .checkDate(LocalDateTime.now().minusDays(5))
            .weight(1.2)
            .notes("두 번째 건강 검진")
            .build();

        HealthRecord record3 = HealthRecord.builder()
            .pet(sut)
            .checkDate(LocalDateTime.now())
            .weight(1.5)
            .notes("세 번째 건강 검진")
            .build();

        sut.saveHealthRecord(record1);
        sut.saveHealthRecord(record2);
        sut.saveHealthRecord(record3);

        // when
        List<HealthRecord> recentRecords = sut.getHealthRecordsAfter(LocalDateTime.now().minusDays(7));

        // then
        assertThat(recentRecords).hasSize(2);
        assertThat(recentRecords).contains(record2, record3);
        assertThat(recentRecords).doesNotContain(record1);
    }
}
