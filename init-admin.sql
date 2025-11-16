-- Insert admin user and group
-- Password: admin123 (BCrypt hash)

USE salesmanager;

-- Create ADMIN group if not exists
INSERT IGNORE INTO sm_group (GROUP_ID, GROUP_NAME, GROUP_TYPE) 
VALUES (1, 'ADMIN', 'ADMIN');

-- Create admin user
-- BCrypt hash for 'admin123': $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy
INSERT IGNORE INTO user (admin_email, admin_password, admin_first_name, admin_last_name, active, date_created, date_modified)
VALUES ('admin@spaza.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Admin', 'User', 1, NOW(), NOW());

SET @user_id = LAST_INSERT_ID();

-- Link user to group
INSERT IGNORE INTO user_group (user_id, group_id)
VALUES (@user_id, 1);
