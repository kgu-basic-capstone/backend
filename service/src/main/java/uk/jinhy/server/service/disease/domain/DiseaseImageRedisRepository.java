package uk.jinhy.server.service.disease.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiseaseImageRedisRepository extends CrudRepository<DiseaseImageTaskEntity, String> {
}
