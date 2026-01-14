INSERT INTO agent (uuid, firstname, lastname, email, password_hash, is_admin) VALUES
    ('a1b2c3d4-e5f6-7890-abcd-ef1234567890', 'John', 'Smith', 'john.smith@wherethecar.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', true),
    ('b2c3d4e5-f6a7-8901-bcde-f12345678901', 'Sarah', 'Johnson', 'sarah.johnson@wherethecar.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', false),
    ('c3d4e5f6-a7b8-9012-cdef-123456789012', 'Michael', 'Brown', 'michael.brown@wherethecar.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', false),
    ('d4e5f6a7-b8c9-0123-def1-234567890123', 'Emily', 'Davis', 'emily.davis@wherethecar.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', false),
    ('e5f6a7b8-c9d0-1234-ef12-345678901234', 'David', 'Martinez', 'david.martinez@wherethecar.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', false),
    ('f6a7b8c9-d0e1-2345-f123-456789012345', 'Jessica', 'Garcia', 'jessica.garcia@wherethecar.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', true);

INSERT INTO vehicle (uuid, license_plate, manufacturer, model, energy, power, seats, capacity, utility_weight, color, kilometers, acquisition_date, status) VALUES
    ('11111111-1111-1111-1111-111111111111', 'ABC-123', 'Toyota', 'Corolla', 'gasoline', 132, 5, 470, 1200, 'white', 45000, '2022-03-15 10:00:00', 'available'),
    ('22222222-2222-2222-2222-222222222222', 'DEF-456', 'Tesla', 'Model 3', 'electric', 283, 5, 425, 1600, 'black', 28000, '2023-01-20 14:30:00', 'available'),
    ('33333333-3333-3333-3333-333333333333', 'GHI-789', 'Ford', 'Transit', 'diesel', 170, 3, 1200, 2500, 'silver', 72000, '2021-06-10 09:00:00', 'available'),
    ('44444444-4444-4444-4444-444444444444', 'JKL-012', 'Honda', 'Civic', 'hybrid', 143, 5, 420, 1300, 'blue', 35000, '2022-09-05 11:15:00', 'reserved'),
    ('55555555-5555-5555-5555-555555555555', 'MNO-345', 'Volkswagen', 'Golf', 'gasoline', 110, 5, 380, 1200, 'red', 52000, '2021-11-25 15:45:00', 'available'),
    ('66666666-6666-6666-6666-666666666666', 'PQR-678', 'Renault', 'Kangoo', 'diesel', 90, 2, 800, 1800, 'white', 64000, '2021-08-17 13:20:00', 'in_maintenance'),
    ('77777777-7777-7777-7777-777777777777', 'STU-901', 'BMW', '3 Series', 'diesel', 190, 5, 480, 1500, 'gray', 41000, '2023-04-12 10:30:00', 'available'),
    ('88888888-8888-8888-8888-888888888888', 'VWX-234', 'Mercedes', 'Sprinter', 'diesel', 163, 3, 1400, 3000, 'white', 58000, '2022-02-28 08:00:00', 'available'),
    ('99999999-9999-9999-9999-999999999999', 'YZA-567', 'Nissan', 'Leaf', 'electric', 150, 5, 435, 1500, 'green', 22000, '2023-07-19 16:00:00', 'available'),
    ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'BCD-890', 'Peugeot', 'Partner', 'diesel', 100, 2, 750, 1700, 'silver', 69000, '2021-05-30 12:00:00', 'available');

INSERT INTO reservation (uuid, agent_uuid, vehicle_uuid, start_date, end_date, status) VALUES
    ('a9999999-9999-9999-9999-999999999999', 'b2c3d4e5-f6a7-8901-bcde-f12345678901', '11111111-1111-1111-1111-111111111111', '2026-01-05 08:00:00', '2026-01-07 18:00:00', 'cancelled'),
    ('f1111111-1111-1111-1111-111111111111', 'b2c3d4e5-f6a7-8901-bcde-f12345678901', '11111111-1111-1111-1111-111111111111', '2026-01-20 08:00:00', '2026-01-22 18:00:00', 'confirmed'),
    ('fbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'd4e5f6a7-b8c9-0123-def1-234567890123', '11111111-1111-1111-1111-111111111111', '2026-02-10 08:00:00', '2026-02-12 18:00:00', 'pending'),
    ('f8888888-8888-8888-8888-888888888888', 'e5f6a7b8-c9d0-1234-ef12-345678901234', '22222222-2222-2222-2222-222222222222', '2025-12-28 09:00:00', '2025-12-30 17:00:00', 'completed'),
    ('f2222222-2222-2222-2222-222222222222', 'c3d4e5f6-a7b8-9012-cdef-123456789012', '22222222-2222-2222-2222-222222222222', '2026-01-18 09:00:00', '2026-01-19 17:00:00', 'confirmed'),
    ('f3333333-3333-3333-3333-333333333333', 'd4e5f6a7-b8c9-0123-def1-234567890123', '33333333-3333-3333-3333-333333333333', '2026-01-25 07:00:00', '2026-01-27 19:00:00', 'pending'),
    ('f4444444-4444-4444-4444-444444444444', 'e5f6a7b8-c9d0-1234-ef12-345678901234', '44444444-4444-4444-4444-444444444444', '2026-01-15 10:00:00', '2026-01-17 16:00:00', 'confirmed'),
    ('004c62cc-b78e-4d16-a2e8-873f7662f9fd', 'f6a7b8c9-d0e1-2345-f123-456789012345', '55555555-5555-5555-5555-555555555555', '2025-12-20 08:00:00', '2025-12-22 18:00:00', 'completed'),
    ('f5555555-5555-5555-5555-555555555555', 'b2c3d4e5-f6a7-8901-bcde-f12345678901', '55555555-5555-5555-5555-555555555555', '2026-02-01 08:00:00', '2026-02-03 18:00:00', 'pending'),
    ('f6666666-6666-6666-6666-666666666666', 'c3d4e5f6-a7b8-9012-cdef-123456789012', '77777777-7777-7777-7777-777777777777', '2026-01-16 09:00:00', '2026-01-18 17:00:00', 'confirmed'),
    ('f7777777-7777-7777-7777-777777777777', 'd4e5f6a7-b8c9-0123-def1-234567890123', '88888888-8888-8888-8888-888888888888', '2025-12-15 07:00:00', '2025-12-20 19:00:00', 'completed'),
    ('5cef33e3-3a57-45b8-b81b-f298d7f4ba16', 'a1b2c3d4-e5f6-7890-abcd-ef1234567890', '88888888-8888-8888-8888-888888888888', '2026-01-22 07:00:00', '2026-01-25 19:00:00', 'confirmed'),
    ('f16740b0-6a86-474d-9c7c-6a3b507d5a31', 'b2c3d4e5-f6a7-8901-bcde-f12345678901', '99999999-9999-9999-9999-999999999999', '2026-01-10 10:00:00', '2026-01-12 16:00:00', 'completed'),
    ('ee80d41c-afe0-40c4-aede-b7fab015ad58', 'c3d4e5f6-a7b8-9012-cdef-123456789012', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', '2026-01-30 09:00:00', '2026-02-02 17:00:00', 'pending');
