package br.com.kessler.api.repository;

import br.com.kessler.api.entity.CollisionAlert;
import br.com.kessler.api.enums.AlertSeverity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CollisionAlertRepository extends JpaRepository<CollisionAlert, Long> {

    List<CollisionAlert> findByAcknowledgedFalse();

    List<CollisionAlert> findBySeverity(AlertSeverity severity);

    List<CollisionAlert> findByPrimaryDebrisId(Long debrisId);
}
