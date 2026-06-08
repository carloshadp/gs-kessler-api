package br.com.kessler.api.repository;

import br.com.kessler.api.entity.OrbitalDebris;
import br.com.kessler.api.enums.DebrisStatus;
import br.com.kessler.api.enums.RiskLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrbitalDebrisRepository extends JpaRepository<OrbitalDebris, Long> {

    Optional<OrbitalDebris> findByNoradId(String noradId);

    boolean existsByNoradId(String noradId);

    List<OrbitalDebris> findByRiskLevel(RiskLevel riskLevel);

    List<OrbitalDebris> findByStatus(DebrisStatus status);

    @Query("SELECT d FROM OrbitalDebris d WHERE d.altitudeKm BETWEEN :min AND :max")
    List<OrbitalDebris> findByAltitudeRange(@Param("min") Double min, @Param("max") Double max);

    @Query("SELECT d FROM OrbitalDebris d WHERE d.riskLevel = 'CRITICAL' AND d.status = 'ACTIVE'")
    List<OrbitalDebris> findActiveCritical();
}
