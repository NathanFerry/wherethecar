-- Agents with French names
-- Password hashes: 'admin' = 8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918
--                  'password' = 5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8
INSERT INTO agent (uuid, firstname, lastname, email, password_hash, is_admin) VALUES
    ('a1b2c3d4-e5f6-7890-abcd-ef1234567890', 'Admin', 'Système', 'admin@wtc.fr', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', true),
    ('b2c3d4e5-f6a7-8901-bcde-f12345678901', 'Sophie', 'Dubois', 'sophie.dubois@wherethecar.fr', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', false),
    ('c3d4e5f6-a7b8-9012-cdef-123456789012', 'Thomas', 'Martin', 'thomas.martin@wherethecar.fr', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', false),
    ('d4e5f6a7-b8c9-0123-def1-234567890123', 'Marie', 'Bernard', 'marie.bernard@wherethecar.fr', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', false),
    ('e5f6a7b8-c9d0-1234-ef12-345678901234', 'Lucas', 'Petit', 'lucas.petit@wherethecar.fr', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', false),
    ('f6a7b8c9-d0e1-2345-f123-456789012345', 'Emma', 'Richard', 'emma.richard@wherethecar.fr', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', true),
    ('11a1b1c1-d1e1-f111-2111-311111111111', 'Pierre', 'Lefebvre', 'pierre.lefebvre@wherethecar.fr', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', false),
    ('22b2c2d2-e2f2-1222-3222-422222222222', 'Julie', 'Moreau', 'julie.moreau@wherethecar.fr', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', false),
    ('33c3d3e3-f3a3-2333-4333-533333333333', 'Antoine', 'Simon', 'antoine.simon@wherethecar.fr', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', false),
    ('44d4e4f4-a4b4-3444-5444-644444444444', 'Camille', 'Laurent', 'camille.laurent@wherethecar.fr', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', false);

-- Vehicles with French license plates (format: AB-123-CD)
INSERT INTO vehicle (uuid, license_plate, manufacturer, model, energy, power, seats, capacity, utility_weight, color, kilometers, acquisition_date, status) VALUES
    ('11111111-1111-1111-1111-111111111111', 'AB-123-CD', 'Peugeot', '208', 'gasoline', 100, 5, 311, 1050, 'blanc', 42000, '2022-03-15 10:00:00', 'available'),
    ('22222222-2222-2222-2222-222222222222', 'EF-456-GH', 'Renault', 'Zoe', 'electric', 135, 5, 338, 1500, 'noir', 28000, '2023-01-20 14:30:00', 'available'),
    ('33333333-3333-3333-3333-333333333333', 'IJ-789-KL', 'Citroën', 'Berlingo', 'diesel', 130, 5, 775, 2200, 'gris', 65000, '2021-06-10 09:00:00', 'available'),
    ('44444444-4444-4444-4444-444444444444', 'MN-012-OP', 'Renault', 'Clio', 'hybrid', 140, 5, 391, 1250, 'bleu', 35000, '2022-09-05 11:15:00', 'available'),
    ('55555555-5555-5555-5555-555555555555', 'QR-345-ST', 'Peugeot', '308', 'gasoline', 130, 5, 420, 1300, 'rouge', 48000, '2021-11-25 15:45:00', 'available'),
    ('66666666-6666-6666-6666-666666666666', 'UV-678-WX', 'Renault', 'Kangoo', 'diesel', 95, 2, 800, 1850, 'blanc', 72000, '2021-08-17 13:20:00', 'maintenance'),
    ('77777777-7777-7777-7777-777777777777', 'YZ-901-AB', 'Citroën', 'C5 Aircross', 'diesel', 130, 5, 580, 1650, 'gris métallisé', 38000, '2023-04-12 10:30:00', 'available'),
    ('88888888-8888-8888-8888-888888888888', 'CD-234-EF', 'Peugeot', 'Expert', 'diesel', 120, 3, 1400, 2800, 'blanc', 55000, '2022-02-28 08:00:00', 'available'),
    ('99999999-9999-9999-9999-999999999999', 'GH-567-IJ', 'Renault', 'Megane E-Tech', 'electric', 160, 5, 440, 1600, 'vert', 18000, '2024-07-19 16:00:00', 'available'),
    ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'KL-890-MN', 'Citroën', 'Jumpy', 'diesel', 120, 3, 1050, 2400, 'argent', 61000, '2021-05-30 12:00:00', 'available'),
    ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'OP-123-QR', 'Peugeot', '3008', 'hybrid', 225, 5, 520, 1700, 'noir', 25000, '2023-10-15 09:00:00', 'available'),
    ('cccccccc-cccc-cccc-cccc-cccccccccccc', 'ST-456-UV', 'Renault', 'Captur', 'gasoline', 90, 5, 404, 1200, 'orange', 32000, '2022-06-20 11:30:00', 'available'),
    ('dddddddd-dddd-dddd-dddd-dddddddddddd', 'WX-789-YZ', 'Citroën', 'C3', 'gasoline', 83, 5, 300, 1100, 'bleu', 45000, '2021-12-08 14:00:00', 'available'),
    ('eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', 'AB-012-CD', 'Peugeot', 'Rifter', 'diesel', 130, 5, 775, 2100, 'gris', 51000, '2022-04-25 10:15:00', 'available'),
    ('ffffffff-ffff-ffff-ffff-ffffffffffff', 'EF-345-GH', 'Renault', 'Twingo', 'electric', 82, 4, 188, 950, 'blanc', 15000, '2024-02-10 13:45:00', 'available'),
    -- Utility vehicles and trucks
    ('10101010-1010-1010-1010-101010101010', 'IJ-678-KL', 'Renault', 'Master', 'diesel', 135, 3, 1800, 3500, 'blanc', 78000, '2021-03-12 09:00:00', 'available'),
    ('20202020-2020-2020-2020-202020202020', 'MN-901-OP', 'Peugeot', 'Boxer', 'diesel', 140, 3, 2000, 3500, 'blanc', 65000, '2021-09-18 10:30:00', 'available'),
    ('30303030-3030-3030-3030-303030303030', 'QR-234-ST', 'Citroën', 'Jumper', 'diesel', 140, 3, 1950, 3500, 'gris', 82000, '2020-11-22 14:00:00', 'available'),
    ('40404040-4040-4040-4040-404040404040', 'UV-567-WX', 'Iveco', 'Daily', 'diesel', 160, 3, 2200, 4500, 'blanc', 95000, '2020-06-15 08:30:00', 'available'),
    ('50505050-5050-5050-5050-505050505050', 'YZ-890-AB', 'Mercedes', 'Vito', 'diesel', 114, 3, 1100, 2500, 'argent', 58000, '2022-08-10 11:00:00', 'available'),
    ('60606060-6060-6060-6060-606060606060', 'CD-123-EF', 'Ford', 'Transit Custom', 'diesel', 130, 3, 1300, 2800, 'bleu', 47000, '2023-02-28 09:30:00', 'available'),
    ('70707070-7070-7070-7070-707070707070', 'GH-456-IJ', 'Fiat', 'Ducato', 'diesel', 140, 3, 1850, 3500, 'blanc', 71000, '2021-07-05 15:00:00', 'available'),
    -- Trucks with flatbeds
    ('80808080-8080-8080-8080-808080808080', 'KL-789-MN', 'Renault', 'Master Plateau', 'diesel', 145, 3, 2500, 4200, 'blanc', 88000, '2020-10-20 10:00:00', 'available'),
    ('90909090-9090-9090-9090-909090909090', 'OP-012-QR', 'Nissan', 'Cabstar', 'diesel', 150, 3, 2800, 4500, 'blanc', 102000, '2019-12-15 13:00:00', 'available'),
    -- Trailers (remorques)
    ('a0a0a0a0-a0a0-a0a0-a0a0-a0a0a0a0a0a0', 'ST-345-UV', 'Anssems', 'GTB 1200', 'none', 0, 0, 1200, 750, 'gris', 8000, '2022-05-10 10:00:00', 'available');

-- Reservations (carefully scheduled to avoid overlaps)
-- Status: completed (past), confirmed (current/future), pending (future), cancelled
INSERT INTO reservation (uuid, agent_uuid, vehicle_uuid, start_date, end_date, status) VALUES
    -- Completed reservations (past)
    ('a9999999-9999-9999-9999-999999999999', 'b2c3d4e5-f6a7-8901-bcde-f12345678901', '11111111-1111-1111-1111-111111111111', '2025-12-05 08:00:00', '2025-12-07 18:00:00', 'completed'),
    ('f8888888-8888-8888-8888-888888888888', 'e5f6a7b8-c9d0-1234-ef12-345678901234', '22222222-2222-2222-2222-222222222222', '2025-12-10 09:00:00', '2025-12-12 17:00:00', 'completed'),
    ('f7777777-7777-7777-7777-777777777777', 'd4e5f6a7-b8c9-0123-def1-234567890123', '88888888-8888-8888-8888-888888888888', '2025-12-15 07:00:00', '2025-12-18 19:00:00', 'completed'),
    ('004c62cc-b78e-4d16-a2e8-873f7662f9fd', 'f6a7b8c9-d0e1-2345-f123-456789012345', '55555555-5555-5555-5555-555555555555', '2026-01-02 08:00:00', '2026-01-05 18:00:00', 'completed'),
    ('f16740b0-6a86-474d-9c7c-6a3b507d5a31', 'b2c3d4e5-f6a7-8901-bcde-f12345678901', '99999999-9999-9999-9999-999999999999', '2026-01-08 10:00:00', '2026-01-10 16:00:00', 'completed'),
    ('aa111111-1111-1111-1111-111111111111', '11a1b1c1-d1e1-f111-2111-311111111111', 'cccccccc-cccc-cccc-cccc-cccccccccccc', '2026-01-12 09:00:00', '2026-01-14 18:00:00', 'completed'),

    -- Confirmed reservations (ongoing and upcoming)
    ('f1111111-1111-1111-1111-111111111111', 'c3d4e5f6-a7b8-9012-cdef-123456789012', '11111111-1111-1111-1111-111111111111', '2026-01-20 08:00:00', '2026-01-22 18:00:00', 'confirmed'),
    ('f2222222-2222-2222-2222-222222222222', 'e5f6a7b8-c9d0-1234-ef12-345678901234', '22222222-2222-2222-2222-222222222222', '2026-01-21 09:00:00', '2026-01-23 17:00:00', 'confirmed'),
    ('5cef33e3-3a57-45b8-b81b-f298d7f4ba16', 'a1b2c3d4-e5f6-7890-abcd-ef1234567890', '88888888-8888-8888-8888-888888888888', '2026-01-22 07:00:00', '2026-01-25 19:00:00', 'confirmed'),
    ('f6666666-6666-6666-6666-666666666666', 'd4e5f6a7-b8c9-0123-def1-234567890123', '77777777-7777-7777-7777-777777777777', '2026-01-23 09:00:00', '2026-01-25 17:00:00', 'confirmed'),
    ('f4444444-4444-4444-4444-444444444444', '22b2c2d2-e2f2-1222-3222-422222222222', '44444444-4444-4444-4444-444444444444', '2026-01-24 10:00:00', '2026-01-26 16:00:00', 'confirmed'),
    ('bb111111-1111-1111-1111-111111111111', 'f6a7b8c9-d0e1-2345-f123-456789012345', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', '2026-01-25 08:00:00', '2026-01-27 18:00:00', 'confirmed'),
    ('cc111111-1111-1111-1111-111111111111', '33c3d3e3-f3a3-2333-4333-533333333333', 'eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', '2026-01-26 09:00:00', '2026-01-28 17:00:00', 'confirmed'),
    ('dd111111-1111-1111-1111-111111111111', '44d4e4f4-a4b4-3444-5444-644444444444', 'ffffffff-ffff-ffff-ffff-ffffffffffff', '2026-01-27 08:30:00', '2026-01-29 18:30:00', 'confirmed'),

    -- Pending reservations (future)
    ('fbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'b2c3d4e5-f6a7-8901-bcde-f12345678901', '55555555-5555-5555-5555-555555555555', '2026-02-01 08:00:00', '2026-02-03 18:00:00', 'pending'),
    ('f3333333-3333-3333-3333-333333333333', '11a1b1c1-d1e1-f111-2111-311111111111', '33333333-3333-3333-3333-333333333333', '2026-02-05 07:00:00', '2026-02-07 19:00:00', 'pending'),
    ('ee80d41c-afe0-40c4-aede-b7fab015ad58', 'c3d4e5f6-a7b8-9012-cdef-123456789012', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', '2026-02-08 09:00:00', '2026-02-10 17:00:00', 'pending'),
    ('ee111111-1111-1111-1111-111111111111', 'e5f6a7b8-c9d0-1234-ef12-345678901234', 'dddddddd-dddd-dddd-dddd-dddddddddddd', '2026-02-10 08:00:00', '2026-02-12 18:00:00', 'pending'),
    ('ff111111-1111-1111-1111-111111111111', 'd4e5f6a7-b8c9-0123-def1-234567890123', '99999999-9999-9999-9999-999999999999', '2026-02-15 10:00:00', '2026-02-17 16:00:00', 'pending'),
    ('11211111-1111-1111-1111-111111111111', '22b2c2d2-e2f2-1222-3222-422222222222', 'cccccccc-cccc-cccc-cccc-cccccccccccc', '2026-02-20 09:00:00', '2026-02-22 17:00:00', 'pending'),

    -- Cancelled reservations
    ('12211111-1111-1111-1111-111111111111', '33c3d3e3-f3a3-2333-4333-533333333333', '11111111-1111-1111-1111-111111111111', '2026-01-28 08:00:00', '2026-01-30 18:00:00', 'cancelled'),
    ('13211111-1111-1111-1111-111111111111', '44d4e4f4-a4b4-3444-5444-644444444444', '77777777-7777-7777-7777-777777777777', '2026-02-12 09:00:00', '2026-02-14 17:00:00', 'cancelled'),

    -- Utility vehicles and trucks reservations
    ('14211111-1111-1111-1111-111111111111', '11a1b1c1-d1e1-f111-2111-311111111111', '10101010-1010-1010-1010-101010101010', '2025-12-08 07:00:00', '2025-12-10 19:00:00', 'completed'),
    ('15211111-1111-1111-1111-111111111111', 'b2c3d4e5-f6a7-8901-bcde-f12345678901', '20202020-2020-2020-2020-202020202020', '2026-01-19 08:00:00', '2026-01-21 18:00:00', 'confirmed'),
    ('16211111-1111-1111-1111-111111111111', '22b2c2d2-e2f2-1222-3222-422222222222', '30303030-3030-3030-3030-303030303030', '2026-01-22 07:00:00', '2026-01-24 19:00:00', 'confirmed'),
    ('17211111-1111-1111-1111-111111111111', 'c3d4e5f6-a7b8-9012-cdef-123456789012', '40404040-4040-4040-4040-404040404040', '2026-02-03 08:00:00', '2026-02-05 18:00:00', 'pending'),
    ('18211111-1111-1111-1111-111111111111', 'e5f6a7b8-c9d0-1234-ef12-345678901234', '50505050-5050-5050-5050-505050505050', '2026-01-25 09:00:00', '2026-01-27 17:00:00', 'confirmed'),
    ('19211111-1111-1111-1111-111111111111', '33c3d3e3-f3a3-2333-4333-533333333333', '60606060-6060-6060-6060-606060606060', '2025-12-18 08:00:00', '2025-12-20 18:00:00', 'completed'),
    ('20211111-1111-1111-1111-111111111111', 'd4e5f6a7-b8c9-0123-def1-234567890123', '70707070-7070-7070-7070-707070707070', '2026-02-06 07:00:00', '2026-02-08 19:00:00', 'pending'),
    ('21211111-1111-1111-1111-111111111111', '44d4e4f4-a4b4-3444-5444-644444444444', '80808080-8080-8080-8080-808080808080', '2026-01-28 08:00:00', '2026-01-30 18:00:00', 'confirmed'),
    ('22211111-1111-1111-1111-111111111111', 'a1b2c3d4-e5f6-7890-abcd-ef1234567890', '90909090-9090-9090-9090-909090909090', '2026-02-11 07:00:00', '2026-02-13 19:00:00', 'pending'),
    ('23211111-1111-1111-1111-111111111111', 'f6a7b8c9-d0e1-2345-f123-456789012345', 'a0a0a0a0-a0a0-a0a0-a0a0-a0a0a0a0a0a0', '2026-01-29 09:00:00', '2026-01-31 17:00:00', 'confirmed');

-- Maintenance operations
INSERT INTO maintenance_operation (uuid, vehicle_uuid, name, description, operation_date, cost) VALUES
    ('11111111-1111-1111-1111-111111111111', '11111111-1111-1111-1111-111111111111', 'Vidange', 'Vidange moteur complète avec remplacement du filtre à huile', '2025-11-05 10:00:00', 85.50),
    ('22222222-2222-2222-2222-222222222222', '22222222-2222-2222-2222-222222222222', 'Remplacement batterie', 'Remplacement batterie 12V auxiliaire', '2025-10-15 14:30:00', 245.00),
    ('33333333-3333-3333-3333-333333333333', '33333333-3333-3333-3333-333333333333', 'Révision complète', 'Révision des 60000 km incluant freins, filtres et liquides', '2025-09-20 09:00:00', 450.75),
    ('44444444-4444-4444-4444-444444444444', '44444444-4444-4444-4444-444444444444', 'Changement pneus', 'Remplacement des 4 pneus avec équilibrage', '2025-12-10 11:00:00', 380.00),
    ('55555555-5555-5555-5555-555555555555', '55555555-5555-5555-5555-555555555555', 'Plaquettes de frein', 'Remplacement plaquettes avant et disques', '2025-11-22 13:00:00', 295.50),
    ('66666666-6666-6666-6666-666666666666', '66666666-6666-6666-6666-666666666666', 'Réparation embrayage', 'Remplacement kit embrayage complet', '2026-01-15 08:30:00', 680.00),
    ('77777777-7777-7777-7777-777777777777', '77777777-7777-7777-7777-777777777777', 'Vidange', 'Vidange moteur diesel avec filtre à gasoil', '2025-12-05 10:30:00', 95.00),
    ('88888888-8888-8888-8888-888888888888', '88888888-8888-8888-8888-888888888888', 'Contrôle technique', 'Passage au contrôle technique et réparations mineures', '2025-10-30 15:00:00', 125.50),
    ('99999999-9999-9999-9999-999999999999', '99999999-9999-9999-9999-999999999999', 'Mise à jour logiciel', 'Mise à jour du logiciel de gestion batterie', '2025-11-18 16:00:00', 0.00),
    ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'Vidange', 'Vidange avec filtre à huile et à gasoil', '2025-12-20 09:30:00', 105.00),
    ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'Entretien système hybride', 'Vérification et entretien du système hybride', '2025-11-28 14:00:00', 175.00),
    ('cccccccc-cccc-cccc-cccc-cccccccccccc', 'cccccccc-cccc-cccc-cccc-cccccccccccc', 'Climatisation', 'Recharge climatisation et désinfection', '2025-10-12 11:30:00', 85.00),
    ('dddddddd-dddd-dddd-dddd-dddddddddddd', '66666666-6666-6666-6666-666666666666', 'Diagnostic électronique', 'Diagnostic complet suite à témoin moteur', '2026-01-10 10:00:00', 75.00),
    -- Maintenance for utility vehicles and trucks
    ('eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', '10101010-1010-1010-1010-101010101010', 'Révision complète', 'Révision des 75000 km avec freins et suspensions', '2025-11-15 08:00:00', 520.00),
    ('ffffffff-ffff-ffff-ffff-ffffffffffff', '20202020-2020-2020-2020-202020202020', 'Vidange utilitaire', 'Vidange moteur diesel avec filtre à gasoil et à air', '2025-12-12 09:00:00', 135.00),
    ('10101010-1010-1010-1010-101010101010', '30303030-3030-3030-3030-303030303030', 'Plaquettes et disques', 'Remplacement plaquettes et disques avant/arrière', '2025-10-25 10:30:00', 485.00),
    ('20202020-2020-2020-2020-202020202020', '40404040-4040-4040-4040-404040404040', 'Contrôle technique VUL', 'Passage contrôle technique véhicule utilitaire léger', '2025-11-08 14:00:00', 95.00),
    ('30303030-3030-3030-3030-303030303030', '50505050-5050-5050-5050-505050505050', 'Changement courroie', 'Remplacement courroie de distribution et pompe à eau', '2025-09-30 08:30:00', 720.00),
    ('40404040-4040-4040-4040-404040404040', '60606060-6060-6060-6060-606060606060', 'Vidange', 'Vidange moteur avec filtre à huile et gasoil', '2025-12-15 11:00:00', 115.00),
    ('50505050-5050-5050-5050-505050505050', '70707070-7070-7070-7070-707070707070', 'Réparation suspension', 'Remplacement amortisseurs arrière', '2025-11-20 13:00:00', 340.00),
    ('60606060-6060-6060-6060-606060606060', '80808080-8080-8080-8080-808080808080', 'Révision plateau', 'Révision complète avec vérification hayon et système hydraulique', '2025-10-18 09:00:00', 395.00),
    ('70707070-7070-7070-7070-707070707070', '90909090-9090-9090-9090-909090909090', 'Changement pneus utilitaires', 'Remplacement 6 pneus avec équilibrage', '2025-12-02 10:00:00', 680.00),
    ('80808080-8080-8080-8080-808080808080', 'a0a0a0a0-a0a0-a0a0-a0a0-a0a0a0a0a0a0', 'Entretien remorque', 'Graissage essieu et vérification freinage', '2025-11-25 15:00:00', 65.00);
