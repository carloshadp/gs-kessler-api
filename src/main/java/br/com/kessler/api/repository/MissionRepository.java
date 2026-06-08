package br.com.kessler.api.repository;

import br.com.kessler.api.entity.Mission;
import br.com.kessler.api.enums.MissionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MissionRepository extends JpaRepository<Mission, Long> {

    List<Mission> findByStatus(MissionStatus status);

    List<Mission> findByTargetDebrisId(Long debrisId);
}
