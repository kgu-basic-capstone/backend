package uk.jinhy.server.api.vaccination.presentation;

public enum VaccinationStatusType {
    SCHEDULED,  // 예정
    RECOMMENDED, // 권장
    MANDATORY,  // 필수
    OPTIONAL,   // 선택
    FIRST_DOSE, // 1차
    SECOND_DOSE, // 2차
    BOOSTER,    // 부스터
    ETC         // 기타
}
