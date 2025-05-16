package uk.jinhy.server.service.user.presentation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.jinhy.server.service.domain.UserEntity;

//VaccinationServiceTest 인한 임시 UserRepository
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {


}
