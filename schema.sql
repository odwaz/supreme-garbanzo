-- MySQL Database Setup Script for Online Spaza
-- Run this script in MySQL to create the required database

SET FOREIGN_KEY_CHECKS = 0;

DROP DATABASE IF EXISTS salesmanager;
CREATE DATABASE salesmanager CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

SET FOREIGN_KEY_CHECKS = 1;

-- Create a user for the application (optional, you can use root)
-- CREATE USER IF NOT EXISTS 'spaza_user'@'localhost' IDENTIFIED BY 'spaza_password';
-- GRANT ALL PRIVILEGES ON salesmanager.* TO 'spaza_user'@'localhost';
-- FLUSH PRIVILEGES;

USE salesmanager;

-- The application will create tables automatically using Hibernate DDL
-- This script just ensures the database exists