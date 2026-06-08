-- Dataset inicial: detritos orbitais reais do catálogo NORAD/Space-Track
-- Fonte: https://www.space-track.org / NASA Orbital Debris Program Office

INSERT INTO orbital_debris (norad_id, name, type, altitude_km, inclination_deg, eccentricity, size_cm, mass_kg, risk_level, status, launch_date, discovered_at) VALUES
-- Fengyun-1C: satélite chinês destruído em teste ASAT (2007), gerou ~3000 fragmentos
('25730', 'Fengyun-1C Debris Field', 'DEBRIS', 855.0, 98.8, 0.0013, 10.0, NULL, 'CRITICAL', 'ACTIVE', '1999-05-10', '2007-01-11 22:28:00'),

-- Cosmos 2251: satélite russo que colidiu com Iridium 33 (2009)
('22675', 'Cosmos 2251 Debris', 'DEBRIS', 780.0, 74.0, 0.0015, 15.0, 900.0, 'CRITICAL', 'ACTIVE', '1993-06-16', '2009-02-10 16:56:00'),

-- Iridium 33: satélite comercial destruído na colisão com Cosmos 2251
('24946', 'Iridium 33 Debris', 'DEBRIS', 776.0, 86.4, 0.0012, 12.0, 560.0, 'CRITICAL', 'ACTIVE', '1997-09-14', '2009-02-10 16:56:00'),

-- Breeze-M R/B: estágio superior de foguete russo em LEO
('35817', 'Breeze-M Rocket Body', 'ROCKET_BODY', 1500.0, 63.4, 0.0180, 400.0, 1500.0, 'HIGH', 'ACTIVE', '2009-12-18', '2009-12-18 00:00:00'),

-- SPOT 1 Rocket Body: resíduo de lançamento europeu
('16613', 'SPOT 1 Ariane Rocket Body', 'ROCKET_BODY', 820.0, 98.7, 0.0009, 300.0, 800.0, 'HIGH', 'ACTIVE', '1986-02-22', '1986-02-22 00:00:00'),

-- SL-8 Rocket Body: estágio de foguete soviético
('14820', 'SL-8 Rocket Body', 'ROCKET_BODY', 950.0, 74.0, 0.0022, 350.0, 1400.0, 'HIGH', 'ACTIVE', '1984-01-01', '1984-01-01 00:00:00'),

-- Intelsat 33e: satélite de comunicações com falha estrutural (fragmentado)
('41945', 'Intelsat 33e Debris', 'PAYLOAD', 35786.0, 0.1, 0.0001, 50.0, NULL, 'MEDIUM', 'MONITORING', '2016-08-24', '2024-10-19 00:00:00'),

-- NOAA 16: satélite meteorológico americano inativo, fragmentado em 2015
('26536', 'NOAA 16 Debris', 'PAYLOAD', 849.0, 99.0, 0.0008, 25.0, 1457.0, 'MEDIUM', 'ACTIVE', '2000-09-21', '2015-11-25 00:00:00');

-- Missões de mitigação iniciais
INSERT INTO missions (name, description, target_debris_id, priority, status, scheduled_date, completed_at, created_at) VALUES
('Operação Fênix I', 'Missão de remoção prioritária dos fragmentos Fengyun-1C no cinturão LEO 855km', 1, 'URGENT', 'PLANNED', '2027-06-15', NULL, '2026-06-06 10:00:00'),
('Operação Cosmos Clean', 'Remoção dos detritos primários da colisão Cosmos 2251 x Iridium 33', 2, 'URGENT', 'PLANNED', '2027-09-01', NULL, '2026-06-06 10:00:00'),
('Monitoramento Breeze-M', 'Acompanhamento de trajetória do estágio Breeze-M a 1500km', 4, 'MEDIUM', 'IN_PROGRESS', '2026-12-01', NULL, '2026-06-06 10:00:00');

-- Alertas de colisão iniciais
INSERT INTO collision_alerts (primary_debris_id, secondary_object_id, secondary_object_name, collision_probability, estimated_time_to_event, severity, acknowledged, created_at) VALUES
(1, '22675', 'Cosmos 2251 Debris', 0.00340, '2027-03-15 04:22:00', 'CRITICAL', FALSE, '2026-06-06 10:00:00'),
(2, '24946', 'Iridium 33 Debris', 0.00180, '2027-07-20 11:45:00', 'WARNING', FALSE, '2026-06-06 10:00:00'),
(3, '14820', 'SL-8 Rocket Body', 0.00052, '2028-01-10 08:30:00', 'INFO', TRUE, '2026-06-06 10:00:00');
