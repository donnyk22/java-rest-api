-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Mar 16, 2026 at 04:13 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `school`
--

-- --------------------------------------------------------

--
-- Table structure for table `log_audit_trails`
--

CREATE TABLE `log_audit_trails` (
  `id` int(10) NOT NULL,
  `user_id` int(10) NOT NULL,
  `method` varchar(255) DEFAULT NULL,
  `table` varchar(255) DEFAULT NULL,
  `details` varchar(255) DEFAULT NULL,
  `data_id` int(10) DEFAULT NULL,
  `properties` text DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `mst_attendances`
--

CREATE TABLE `mst_attendances` (
  `id` int(10) NOT NULL,
  `student_id` int(10) NOT NULL,
  `date` date NOT NULL DEFAULT current_timestamp(),
  `status` varchar(255) DEFAULT NULL,
  `note` varchar(255) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `mst_attendances`
--

INSERT INTO `mst_attendances` (`id`, `student_id`, `date`, `status`, `note`, `created_at`) VALUES
(1, 1, '2026-02-18', 'PRESENT', '', '2026-02-19 04:29:33'),
(2, 1, '2026-02-19', 'PRESENT', '', '2026-02-19 04:29:59'),
(3, 2, '2026-02-18', 'PRESENT', '', '2026-02-19 04:29:33'),
(4, 2, '2026-02-19', 'PRESENT', '', '2026-02-19 04:29:59'),
(5, 3, '2026-02-18', 'PRESENT', '', '2026-02-19 04:29:33'),
(6, 3, '2026-02-19', 'PRESENT', '', '2026-02-19 04:29:59'),
(7, 4, '2026-02-18', 'PRESENT', '', '2026-02-19 04:29:33'),
(8, 4, '2026-02-19', 'PRESENT', '', '2026-02-19 04:29:59'),
(9, 5, '2026-02-18', 'PRESENT', '', '2026-02-19 04:29:33'),
(10, 5, '2026-02-19', 'ABSENT', '', '2026-02-19 04:29:59'),
(11, 6, '2026-02-18', 'PRESENT', '', '2026-02-19 04:29:33'),
(12, 6, '2026-02-19', 'PRESENT', '', '2026-02-19 04:29:59'),
(13, 7, '2026-02-18', 'PRESENT', '', '2026-02-19 04:29:33'),
(14, 7, '2026-02-19', 'LATE', '', '2026-02-19 04:29:59'),
(15, 8, '2026-02-18', 'PRESENT', '', '2026-02-19 04:29:33'),
(16, 8, '2026-02-19', 'PRESENT', '', '2026-02-19 04:29:59'),
(17, 9, '2026-02-18', 'PRESENT', '', '2026-02-19 04:29:33'),
(18, 9, '2026-02-19', 'PRESENT', '', '2026-02-19 04:29:59'),
(19, 10, '2026-02-18', 'PRESENT', '', '2026-02-19 04:29:33'),
(20, 10, '2026-02-19', 'ABSENT', '', '2026-02-19 04:29:59'),
(21, 11, '2026-02-18', 'PRESENT', '', '2026-02-19 04:29:33'),
(22, 11, '2026-02-19', 'PRESENT', '', '2026-02-19 04:29:59'),
(23, 12, '2026-02-18', 'PRESENT', '', '2026-02-19 04:29:33'),
(24, 12, '2026-02-19', 'PRESENT', '', '2026-02-19 04:29:59'),
(25, 13, '2026-02-18', 'PRESENT', '', '2026-02-19 04:29:33'),
(26, 13, '2026-02-19', 'PRESENT', '', '2026-02-19 04:29:59'),
(27, 14, '2026-02-18', 'PRESENT', '', '2026-02-19 04:29:33'),
(28, 14, '2026-02-19', 'LATE', '', '2026-02-19 04:29:59'),
(29, 15, '2026-02-18', 'PRESENT', '', '2026-02-19 04:29:33'),
(30, 15, '2026-02-19', 'ABSENT', '', '2026-02-19 04:29:59'),
(31, 16, '2026-02-18', 'PRESENT', '', '2026-02-19 04:29:33'),
(32, 16, '2026-02-19', 'PRESENT', '', '2026-02-19 04:29:59'),
(33, 17, '2026-02-18', 'PRESENT', '', '2026-02-19 04:29:33'),
(34, 17, '2026-02-19', 'PRESENT', '', '2026-02-19 04:29:59'),
(35, 18, '2026-02-18', 'PRESENT', '', '2026-02-19 04:29:33'),
(36, 18, '2026-02-19', 'PRESENT', '', '2026-02-19 04:29:59'),
(37, 19, '2026-02-18', 'PRESENT', '', '2026-02-19 04:29:33'),
(38, 19, '2026-02-19', 'PRESENT', '', '2026-02-19 04:29:59'),
(39, 20, '2026-02-18', 'PRESENT', '', '2026-02-19 04:29:33'),
(40, 20, '2026-02-19', 'ABSENT', '', '2026-02-19 04:29:59'),
(41, 21, '2026-02-18', 'PRESENT', '', '2026-02-19 04:29:33'),
(42, 21, '2026-02-19', 'LATE', '', '2026-02-19 04:29:59'),
(43, 22, '2026-02-18', 'PRESENT', '', '2026-02-19 04:29:33'),
(44, 22, '2026-02-19', 'PRESENT', '', '2026-02-19 04:29:59'),
(45, 23, '2026-02-18', 'PRESENT', '', '2026-02-19 04:29:33'),
(46, 23, '2026-02-19', 'PRESENT', '', '2026-02-19 04:29:59'),
(47, 24, '2026-02-18', 'PRESENT', '', '2026-02-19 04:29:33'),
(48, 24, '2026-02-19', 'PRESENT', '', '2026-02-19 04:29:59'),
(49, 25, '2026-02-18', 'PRESENT', '', '2026-02-19 04:29:33'),
(50, 25, '2026-02-19', 'ABSENT', '', '2026-02-19 04:29:59'),
(51, 26, '2026-02-18', 'PRESENT', '', '2026-02-19 04:29:33'),
(52, 26, '2026-02-19', 'PRESENT', '', '2026-02-19 04:29:59'),
(53, 27, '2026-02-18', 'PRESENT', '', '2026-02-19 04:29:33'),
(54, 27, '2026-02-19', 'PRESENT', '', '2026-02-19 04:29:59'),
(55, 28, '2026-02-18', 'PRESENT', '', '2026-02-19 04:29:33'),
(56, 28, '2026-02-19', 'LATE', '', '2026-02-19 04:29:59'),
(57, 29, '2026-02-18', 'PRESENT', '', '2026-02-19 04:29:33'),
(58, 29, '2026-02-19', 'PRESENT', '', '2026-02-19 04:29:59'),
(59, 30, '2026-02-18', 'PRESENT', '', '2026-02-19 04:29:33'),
(60, 30, '2026-02-19', 'ABSENT', '', '2026-02-19 04:29:59'),
(61, 31, '2026-02-18', 'PRESENT', '', '2026-02-19 04:29:33'),
(62, 31, '2026-02-19', 'PRESENT', '', '2026-02-19 04:29:59'),
(63, 32, '2026-02-18', 'PRESENT', '', '2026-02-19 04:29:33'),
(64, 32, '2026-02-19', 'PRESENT', '', '2026-02-19 04:29:59'),
(65, 33, '2026-02-18', 'PRESENT', '', '2026-02-19 04:29:33'),
(66, 33, '2026-02-19', 'PRESENT', '', '2026-02-19 04:29:59'),
(67, 34, '2026-02-18', 'PRESENT', '', '2026-02-19 04:29:33'),
(68, 34, '2026-02-19', 'PRESENT', '', '2026-02-19 04:29:59'),
(69, 35, '2026-02-18', 'PRESENT', '', '2026-02-19 04:29:33'),
(70, 35, '2026-02-19', 'ABSENT', '', '2026-02-19 04:29:59'),
(71, 36, '2026-02-18', 'PRESENT', '', '2026-02-19 04:29:33'),
(72, 36, '2026-02-19', 'PRESENT', '', '2026-02-19 04:29:59'),
(73, 37, '2026-02-18', 'PRESENT', '', '2026-02-19 04:29:33'),
(74, 37, '2026-02-19', 'PRESENT', '', '2026-02-19 04:29:59'),
(75, 38, '2026-02-18', 'PRESENT', '', '2026-02-19 04:29:33'),
(76, 38, '2026-02-19', 'PRESENT', '', '2026-02-19 04:29:59'),
(77, 39, '2026-02-18', 'PRESENT', '', '2026-02-19 04:29:33'),
(78, 39, '2026-02-19', 'PRESENT', '', '2026-02-19 04:29:59'),
(79, 40, '2026-02-18', 'PRESENT', '', '2026-02-19 04:29:33'),
(80, 40, '2026-02-19', 'ABSENT', '', '2026-02-19 04:29:59'),
(81, 41, '2026-02-18', 'PRESENT', '', '2026-02-19 04:29:33'),
(82, 41, '2026-02-19', 'PRESENT', '', '2026-02-19 04:29:59'),
(83, 42, '2026-02-18', 'PRESENT', '', '2026-02-19 04:29:33'),
(84, 42, '2026-02-19', 'LATE', '', '2026-02-19 04:29:59'),
(85, 43, '2026-02-18', 'PRESENT', '', '2026-02-19 04:29:33'),
(86, 43, '2026-02-19', 'PRESENT', '', '2026-02-19 04:29:59'),
(87, 44, '2026-02-18', 'PRESENT', '', '2026-02-19 04:29:33'),
(88, 44, '2026-02-19', 'PRESENT', '', '2026-02-19 04:29:59'),
(89, 45, '2026-02-18', 'PRESENT', '', '2026-02-19 04:29:33'),
(90, 45, '2026-02-19', 'ABSENT', '', '2026-02-19 04:29:59'),
(91, 46, '2026-02-18', 'PRESENT', '', '2026-02-19 04:29:33'),
(92, 46, '2026-02-19', 'PRESENT', '', '2026-02-19 04:29:59'),
(93, 47, '2026-02-18', 'PRESENT', '', '2026-02-19 04:29:33'),
(94, 47, '2026-02-19', 'PRESENT', '', '2026-02-19 04:29:59'),
(95, 48, '2026-02-18', 'PRESENT', '', '2026-02-19 04:29:33'),
(96, 48, '2026-02-19', 'PRESENT', '', '2026-02-19 04:29:59'),
(97, 49, '2026-02-18', 'PRESENT', '', '2026-02-19 04:29:33'),
(98, 49, '2026-02-19', 'LATE', '', '2026-02-19 04:29:59'),
(99, 50, '2026-02-18', 'PRESENT', '', '2026-02-19 04:29:33'),
(100, 50, '2026-02-19', 'ABSENT', '', '2026-02-19 04:29:59'),
(101, 51, '2026-02-18', 'PRESENT', '', '2026-02-19 04:29:33'),
(102, 51, '2026-02-19', 'PRESENT', '', '2026-02-19 04:29:59'),
(103, 52, '2026-02-18', 'PRESENT', '', '2026-02-19 04:29:33'),
(104, 52, '2026-02-19', 'PRESENT', '', '2026-02-19 04:29:59'),
(105, 53, '2026-02-18', 'PRESENT', '', '2026-02-19 04:29:33'),
(106, 53, '2026-02-19', 'PRESENT', '', '2026-02-19 04:29:59'),
(107, 54, '2026-02-18', 'PRESENT', '', '2026-02-19 04:29:33'),
(108, 54, '2026-02-19', 'PRESENT', '', '2026-02-19 04:29:59');

-- --------------------------------------------------------

--
-- Table structure for table `mst_classes`
--

CREATE TABLE `mst_classes` (
  `id` int(10) NOT NULL,
  `class_name` varchar(255) DEFAULT NULL,
  `grade_level` int(3) NOT NULL,
  `academic_year` varchar(255) DEFAULT NULL,
  `deleted` tinyint(1) NOT NULL DEFAULT 0,
  `version` int(10) NOT NULL DEFAULT 0,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `mst_classes`
--

INSERT INTO `mst_classes` (`id`, `class_name`, `grade_level`, `academic_year`, `deleted`, `version`, `created_at`, `updated_at`) VALUES
(1, '10-IPA-1', 10, '2025/2026', 0, 0, '2026-02-19 04:14:30', '2026-02-19 04:14:30'),
(2, '10-IPA-2', 10, '2025/2026', 0, 0, '2026-02-19 04:14:30', '2026-02-19 04:14:30'),
(3, '10-IPA-3', 10, '2025/2026', 0, 0, '2026-02-19 04:14:30', '2026-02-19 04:14:30'),
(4, '10-IPS-1', 10, '2025/2026', 0, 0, '2026-02-19 04:14:30', '2026-02-19 04:14:30'),
(5, '10-IPS-2', 10, '2025/2026', 0, 0, '2026-02-19 04:14:30', '2026-02-19 04:14:30'),
(6, '10-IPS-3', 10, '2025/2026', 0, 0, '2026-02-19 04:14:30', '2026-02-19 04:14:30'),
(7, '11-IPA-1', 11, '2025/2026', 0, 0, '2026-02-19 04:14:30', '2026-02-19 04:14:30'),
(8, '11-IPA-2', 11, '2025/2026', 0, 0, '2026-02-19 04:14:30', '2026-02-19 04:14:30'),
(9, '11-IPA-3', 11, '2025/2026', 0, 0, '2026-02-19 04:14:30', '2026-02-19 04:14:30'),
(10, '11-IPS-1', 11, '2025/2026', 0, 0, '2026-02-19 04:14:30', '2026-02-19 04:14:30'),
(11, '11-IPS-2', 11, '2025/2026', 0, 0, '2026-02-19 04:14:30', '2026-02-19 04:14:30'),
(12, '11-IPS-3', 11, '2025/2026', 0, 0, '2026-02-19 04:14:30', '2026-02-19 04:14:30'),
(13, '12-IPA-1', 12, '2025/2026', 0, 0, '2026-02-19 04:14:30', '2026-02-19 04:14:30'),
(14, '12-IPA-2', 12, '2025/2026', 0, 0, '2026-02-19 04:14:30', '2026-02-19 04:14:30'),
(15, '12-IPA-3', 12, '2025/2026', 0, 0, '2026-02-19 04:14:30', '2026-02-19 04:14:30'),
(16, '12-IPS-1', 12, '2025/2026', 0, 0, '2026-02-19 04:14:30', '2026-02-19 04:14:30'),
(17, '12-IPS-2', 12, '2025/2026', 0, 0, '2026-02-19 04:14:30', '2026-02-19 04:14:30'),
(18, '12-IPS-3', 12, '2025/2026', 0, 0, '2026-02-19 04:14:30', '2026-02-19 04:14:30'),
(19, '10-IPA-1', 10, '2025/2026', 1, 12, '2026-03-02 23:29:49', '2026-03-03 02:22:38');

-- --------------------------------------------------------

--
-- Table structure for table `mst_homeroom_teachers`
--

CREATE TABLE `mst_homeroom_teachers` (
  `id` int(10) NOT NULL,
  `class_id` int(10) NOT NULL,
  `teacher_id` int(10) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `mst_homeroom_teachers`
--

INSERT INTO `mst_homeroom_teachers` (`id`, `class_id`, `teacher_id`, `created_at`) VALUES
(1, 1, 1, '2026-02-19 04:21:25'),
(2, 2, 1, '2026-02-19 04:21:25'),
(3, 3, 2, '2026-02-19 04:21:25'),
(4, 4, 2, '2026-02-19 04:21:25'),
(5, 5, 3, '2026-02-19 04:21:25'),
(6, 6, 3, '2026-02-19 04:21:25'),
(7, 7, 4, '2026-02-19 04:21:25'),
(8, 8, 4, '2026-02-19 04:21:25'),
(9, 9, 5, '2026-02-19 04:21:25'),
(10, 10, 5, '2026-02-19 04:21:25'),
(11, 11, 6, '2026-02-19 04:21:25'),
(12, 12, 6, '2026-02-19 04:21:25'),
(13, 13, 7, '2026-02-19 04:21:25'),
(14, 14, 8, '2026-02-19 04:21:25'),
(15, 15, 9, '2026-02-19 04:21:25'),
(16, 16, 10, '2026-02-19 04:21:25'),
(17, 17, 11, '2026-02-19 04:21:25'),
(18, 18, 12, '2026-02-19 04:21:25');

-- --------------------------------------------------------

--
-- Table structure for table `mst_students`
--

CREATE TABLE `mst_students` (
  `id` int(10) NOT NULL,
  `user_id` int(10) NOT NULL,
  `class_id` int(10) NOT NULL,
  `full_name` varchar(255) DEFAULT NULL,
  `gender` enum('M','F') NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `photo` varchar(255) DEFAULT NULL,
  `deleted` tinyint(1) NOT NULL DEFAULT 0,
  `version` int(10) NOT NULL DEFAULT 0,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `mst_students`
--

INSERT INTO `mst_students` (`id`, `user_id`, `class_id`, `full_name`, `gender`, `address`, `phone`, `photo`, `deleted`, `version`, `created_at`, `updated_at`) VALUES
(1, 100, 1, 'Arya Pratama', 'M', 'Jl. Kenanga No. 1', '081300000001', NULL, 0, 0, '2026-02-19 04:26:11', '2026-02-19 04:26:11'),
(2, 101, 1, 'Naya Azzahra', 'F', 'Jl. Kenanga No. 2', '081300000002', NULL, 0, 0, '2026-02-19 04:26:11', '2026-02-19 04:26:11'),
(3, 102, 1, 'Dimas Mahendra', 'M', 'Jl. Kenanga No. 3', '081300000003', NULL, 0, 0, '2026-02-19 04:26:11', '2026-02-19 04:26:11'),
(4, 103, 2, 'Karen Putri', 'F', 'Jl. Melati No. 1', '081300000004', NULL, 0, 0, '2026-02-19 04:26:11', '2026-02-19 04:26:11'),
(5, 104, 2, 'Raka Saputra', 'M', 'Jl. Melati No. 2', '081300000005', NULL, 0, 0, '2026-02-19 04:26:11', '2026-02-19 04:26:11'),
(6, 105, 2, 'Zara Amelia', 'F', 'Jl. Melati No. 3', '081300000006', NULL, 0, 0, '2026-02-19 04:26:11', '2026-02-19 04:26:11'),
(7, 106, 3, 'Alvin Wijaya', 'M', 'Jl. Mawar No. 1', '081300000007', NULL, 0, 0, '2026-02-19 04:26:11', '2026-02-19 04:26:11'),
(8, 107, 3, 'Citra Maharani', 'F', 'Jl. Mawar No. 2', '081300000008', NULL, 0, 0, '2026-02-19 04:26:11', '2026-02-19 04:26:11'),
(9, 108, 3, 'Kevin Andrean', 'M', 'Jl. Mawar No. 3', '081300000009', NULL, 0, 0, '2026-02-19 04:26:11', '2026-02-19 04:26:11'),
(10, 109, 4, 'Melia Kartika', 'F', 'Jl. Anggrek No. 1', '081300000010', NULL, 0, 0, '2026-02-19 04:26:11', '2026-02-19 04:26:11'),
(11, 110, 4, 'Reno Kurniawan', 'M', 'Jl. Anggrek No. 2', '081300000011', NULL, 0, 0, '2026-02-19 04:26:11', '2026-02-19 04:26:11'),
(12, 111, 4, 'Salsa Aulia', 'F', 'Jl. Anggrek No. 3', '081300000012', NULL, 0, 0, '2026-02-19 04:26:11', '2026-02-19 04:26:11'),
(13, 112, 5, 'Jovan Setiawan', 'M', 'Jl. Dahlia No. 1', '081300000013', NULL, 0, 0, '2026-02-19 04:26:11', '2026-02-19 04:26:11'),
(14, 113, 5, 'Nayla Zahira', 'F', 'Jl. Dahlia No. 2', '081300000014', NULL, 0, 0, '2026-02-19 04:26:11', '2026-02-19 04:26:11'),
(15, 114, 5, 'Fauzan Akbar', 'M', 'Jl. Dahlia No. 3', '081300000015', NULL, 0, 0, '2026-02-19 04:26:11', '2026-02-19 04:26:11'),
(16, 115, 6, 'Tania Salsabila', 'F', 'Jl. Flamboyan No. 1', '081300000016', NULL, 0, 0, '2026-02-19 04:26:11', '2026-02-19 04:26:11'),
(17, 116, 6, 'Ilham Maulana', 'M', 'Jl. Flamboyan No. 2', '081300000017', NULL, 0, 0, '2026-02-19 04:26:11', '2026-02-19 04:26:11'),
(18, 117, 6, 'Dinda Lutfia', 'F', 'Jl. Flamboyan No. 3', '081300000018', NULL, 0, 0, '2026-02-19 04:26:11', '2026-02-19 04:26:11'),
(19, 118, 7, 'Rehan Ramadhan', 'M', 'Jl. Cempaka No. 1', '081300000019', NULL, 0, 0, '2026-02-19 04:26:11', '2026-02-19 04:26:11'),
(20, 119, 7, 'Selin Octavia', 'F', 'Jl. Cempaka No. 2', '081300000020', NULL, 0, 0, '2026-02-19 04:26:11', '2026-02-19 04:26:11'),
(21, 120, 7, 'Naufal Hidayat', 'M', 'Jl. Cempaka No. 3', '081300000021', NULL, 0, 0, '2026-02-19 04:26:11', '2026-02-19 04:26:11'),
(22, 121, 8, 'Kayla Anindya', 'F', 'Jl. Cemara No. 1', '081300000022', NULL, 0, 0, '2026-02-19 04:26:11', '2026-02-19 04:26:11'),
(23, 122, 8, 'Rizky Aditya', 'M', 'Jl. Cemara No. 2', '081300000023', NULL, 0, 0, '2026-02-19 04:26:11', '2026-02-19 04:26:11'),
(24, 123, 8, 'Putri Amandari', 'F', 'Jl. Cemara No. 3', '081300000024', NULL, 0, 0, '2026-02-19 04:26:11', '2026-02-19 04:26:11'),
(25, 124, 9, 'Galih Pradana', 'M', 'Jl. Pahlawan No. 1', '081300000025', NULL, 0, 0, '2026-02-19 04:26:11', '2026-02-19 04:26:11'),
(26, 125, 9, 'Siska Larasati', 'F', 'Jl. Pahlawan No. 2', '081300000026', NULL, 0, 0, '2026-02-19 04:26:11', '2026-02-19 04:26:11'),
(27, 126, 9, 'Rama Dwi Putra', 'M', 'Jl. Pahlawan No. 3', '081300000027', NULL, 0, 0, '2026-02-19 04:26:11', '2026-02-19 04:26:11'),
(28, 127, 10, 'Viona Kinasih', 'F', 'Jl. Sudirman No. 1', '081300000028', NULL, 0, 0, '2026-02-19 04:26:11', '2026-02-19 04:26:11'),
(29, 128, 10, 'Bintang Prakoso', 'M', 'Jl. Sudirman No. 2', '081300000029', NULL, 0, 0, '2026-02-19 04:26:11', '2026-02-19 04:26:11'),
(30, 129, 10, 'Aurel Salsabila', 'F', 'Jl. Sudirman No. 3', '081300000030', NULL, 0, 0, '2026-02-19 04:26:11', '2026-02-19 04:26:11'),
(31, 130, 11, 'Haikal Firmansyah', 'M', 'Jl. Gatot Subroto No. 1', '081300000031', NULL, 0, 0, '2026-02-19 04:26:11', '2026-02-19 04:26:11'),
(32, 131, 11, 'Michelle Angelina', 'F', 'Jl. Gatot Subroto No. 2', '081300000032', NULL, 0, 0, '2026-02-19 04:26:11', '2026-02-19 04:26:11'),
(33, 132, 11, 'Andre Kurnia', 'M', 'Jl. Gatot Subroto No. 3', '081300000033', NULL, 0, 0, '2026-02-19 04:26:11', '2026-02-19 04:26:11'),
(34, 133, 12, 'Farhan Alamsyah', 'M', 'Jl. Diponegoro No. 1', '081300000034', NULL, 0, 0, '2026-02-19 04:26:11', '2026-02-19 04:26:11'),
(35, 134, 12, 'Alika Safira', 'F', 'Jl. Diponegoro No. 2', '081300000035', NULL, 0, 0, '2026-02-19 04:26:11', '2026-02-19 04:26:11'),
(36, 135, 12, 'Hansen Pratama', 'M', 'Jl. Diponegoro No. 3', '081300000036', NULL, 0, 0, '2026-02-19 04:26:11', '2026-02-19 04:26:11'),
(37, 136, 13, 'Tiara Dwijayanti', 'F', 'Jl. Ahmad Yani No. 1', '081300000037', NULL, 0, 0, '2026-02-19 04:26:11', '2026-02-19 04:26:11'),
(38, 137, 13, 'Fikri Ramadhan', 'M', 'Jl. Ahmad Yani No. 2', '081300000038', NULL, 0, 0, '2026-02-19 04:26:11', '2026-02-19 04:26:11'),
(39, 138, 13, 'Gisella Paramitha', 'F', 'Jl. Ahmad Yani No. 3', '081300000039', NULL, 0, 0, '2026-02-19 04:26:11', '2026-02-19 04:26:11'),
(40, 139, 14, 'Rian Nugraha', 'M', 'Jl. Veteran No. 1', '081300000040', NULL, 0, 0, '2026-02-19 04:26:11', '2026-02-19 04:26:11'),
(41, 140, 14, 'Amel Karina', 'F', 'Jl. Veteran No. 2', '081300000041', NULL, 0, 0, '2026-02-19 04:26:11', '2026-02-19 04:26:11'),
(42, 141, 14, 'Joel Pradipta', 'M', 'Jl. Veteran No. 3', '081300000042', NULL, 0, 0, '2026-02-19 04:26:11', '2026-02-19 04:26:11'),
(43, 142, 15, 'Nabila Khairunnisa', 'F', 'Jl. Sisingamangaraja No. 1', '081300000043', NULL, 0, 0, '2026-02-19 04:26:11', '2026-02-19 04:26:11'),
(44, 143, 15, 'Rafli Maulana', 'M', 'Jl. Sisingamangaraja No. 2', '081300000044', NULL, 0, 0, '2026-02-19 04:26:11', '2026-02-19 04:26:11'),
(45, 144, 15, 'Marsha Putri', 'F', 'Jl. Sisingamangaraja No. 3', '081300000045', NULL, 0, 0, '2026-02-19 04:26:11', '2026-02-19 04:26:11'),
(46, 145, 16, 'Darren Saputra', 'M', 'Jl. Hayam Wuruk No. 1', '081300000046', NULL, 0, 0, '2026-02-19 04:26:11', '2026-02-19 04:26:11'),
(47, 146, 16, 'Shafa Azzahra', 'F', 'Jl. Hayam Wuruk No. 2', '081300000047', NULL, 0, 0, '2026-02-19 04:26:11', '2026-02-19 04:26:11'),
(48, 147, 16, 'Aditya Putra', 'M', 'Jl. Hayam Wuruk No. 3', '081300000048', NULL, 0, 0, '2026-02-19 04:26:11', '2026-02-19 04:26:11'),
(49, 148, 17, 'Vania Kusuma', 'F', 'Jl. Gajah Mada No. 1', '081300000049', NULL, 0, 0, '2026-02-19 04:26:11', '2026-02-19 04:26:11'),
(50, 149, 17, 'Azka Ramadhan', 'M', 'Jl. Gajah Mada No. 2', '081300000050', NULL, 0, 0, '2026-02-19 04:26:11', '2026-02-19 04:26:11'),
(51, 150, 17, 'Nadine Puspita', 'F', 'Jl. Gajah Mada No. 3', '081300000051', NULL, 0, 0, '2026-02-19 04:26:11', '2026-02-19 04:26:11'),
(52, 151, 18, 'Rio Pratama', 'M', 'Jl. Kartini No. 1', '081300000052', NULL, 0, 0, '2026-02-19 04:26:11', '2026-02-19 04:26:11'),
(53, 152, 18, 'Cindy Maharani', 'F', 'Jl. Kartini No. 2', '081300000053', NULL, 0, 0, '2026-02-19 04:26:11', '2026-02-19 04:26:11'),
(54, 153, 18, 'Hendra Wicaksono', 'M', 'Jl. Kartini No. 3', '081300000054', NULL, 0, 0, '2026-02-19 04:26:11', '2026-02-19 04:26:11');

-- --------------------------------------------------------

--
-- Table structure for table `mst_teachers`
--

CREATE TABLE `mst_teachers` (
  `id` int(10) NOT NULL,
  `user_id` int(10) NOT NULL,
  `full_name` varchar(255) DEFAULT NULL,
  `gender` enum('M','F') NOT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `photo` varchar(255) DEFAULT NULL,
  `deleted` tinyint(1) NOT NULL DEFAULT 0,
  `version` int(10) NOT NULL DEFAULT 0,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `mst_teachers`
--

INSERT INTO `mst_teachers` (`id`, `user_id`, `full_name`, `gender`, `phone`, `address`, `photo`, `deleted`, `version`, `created_at`, `updated_at`) VALUES
(1, 2, 'Budi Santoso', 'M', '081200000001', 'Jl. Merdeka No. 1', NULL, 0, 0, '2026-02-19 04:20:42', '2026-02-19 04:20:42'),
(2, 3, 'Rina Wulandari', 'F', '081200000002', 'Jl. Merdeka No. 2', NULL, 0, 0, '2026-02-19 04:20:42', '2026-02-19 04:20:42'),
(3, 4, 'Agus Prasetyo', 'M', '081200000003', 'Jl. Merdeka No. 3', NULL, 0, 0, '2026-02-19 04:20:42', '2026-02-19 04:20:42'),
(4, 5, 'Dian Lestari', 'F', '081200000004', 'Jl. Merdeka No. 4', NULL, 0, 0, '2026-02-19 04:20:42', '2026-02-19 04:20:42'),
(5, 6, 'Eko Saputra', 'M', '081200000005', 'Jl. Merdeka No. 5', NULL, 0, 0, '2026-02-19 04:20:42', '2026-02-19 04:20:42'),
(6, 7, 'Sari Handayani', 'F', '081200000006', 'Jl. Merdeka No. 6', NULL, 0, 0, '2026-02-19 04:20:42', '2026-02-19 04:20:42'),
(7, 8, 'Yoga Firmansyah', 'M', '081200000007', 'Jl. Merdeka No. 7', NULL, 0, 0, '2026-02-19 04:20:42', '2026-02-19 04:20:42'),
(8, 9, 'Tanti Kusuma', 'F', '081200000008', 'Jl. Merdeka No. 8', NULL, 0, 0, '2026-02-19 04:20:42', '2026-02-19 04:20:42'),
(9, 10, 'Fajar Nugroho', 'M', '081200000009', 'Jl. Merdeka No. 9', NULL, 0, 0, '2026-02-19 04:20:42', '2026-02-19 04:20:42'),
(10, 11, 'Lia Puspitasari', 'F', '081200000010', 'Jl. Merdeka No. 10', NULL, 0, 0, '2026-02-19 04:20:42', '2026-02-19 04:20:42'),
(11, 12, 'Hendro Wijaya', 'M', '081200000011', 'Jl. Merdeka No. 11', NULL, 0, 0, '2026-02-19 04:20:42', '2026-02-19 04:20:42'),
(12, 13, 'Mira Kartika', 'F', '081200000012', 'Jl. Merdeka No. 12', NULL, 0, 0, '2026-02-19 04:20:42', '2026-02-19 04:20:42');

-- --------------------------------------------------------

--
-- Table structure for table `mst_users`
--

CREATE TABLE `mst_users` (
  `id` int(10) NOT NULL,
  `username` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `photo` text DEFAULT NULL COMMENT 'base64 image',
  `role` varchar(255) DEFAULT NULL,
  `mfa_enabled` tinyint(1) NOT NULL DEFAULT 0,
  `mfa_secret` varchar(255) DEFAULT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT 1,
  `deleted` tinyint(1) NOT NULL DEFAULT 0,
  `version` int(10) NOT NULL DEFAULT 0,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `mst_users`
--

INSERT INTO `mst_users` (`id`, `username`, `email`, `password`, `photo`, `role`, `mfa_enabled`, `mfa_secret`, `is_active`, `deleted`, `version`, `created_at`, `updated_at`) VALUES
(1, 'admin', 'admin@school.com', '$2a$10$7hQL36le0cv1gbhYhEP.8uxAu72GHqc01GsIFHCvfb6GAeqPxONWy', NULL, 'ADMIN', 1, '4BJSITCR53A6GRREI55FUTRQFJOUVLNP', 1, 0, 1, '2026-02-19 04:17:12', '2026-03-15 09:36:46'),
(2, 'budi.teacher', 'budi.teacher@school.com', '$2a$10$CKI8fqzBgGlzfLPO8jc4IeDgPNHJSQb4nILlt5yxgFq9S2V8GPNBK', NULL, 'TEACHER', 0, NULL, 1, 0, 0, '2026-02-19 04:18:21', '2026-02-19 04:18:21'),
(3, 'rina.teacher', 'rina.teacher@school.com', 'hashed_pw', NULL, 'TEACHER', 0, NULL, 1, 0, 0, '2026-02-19 04:18:21', '2026-02-19 04:18:21'),
(4, 'agus.teacher', 'agus.teacher@school.com', 'hashed_pw', NULL, 'TEACHER', 0, NULL, 1, 0, 0, '2026-02-19 04:18:21', '2026-02-19 04:18:21'),
(5, 'dian.teacher', 'dian.teacher@school.com', 'hashed_pw', NULL, 'TEACHER', 0, NULL, 1, 0, 0, '2026-02-19 04:18:21', '2026-02-19 04:18:21'),
(6, 'eko.teacher', 'eko.teacher@school.com', 'hashed_pw', NULL, 'TEACHER', 0, NULL, 1, 0, 0, '2026-02-19 04:18:21', '2026-02-19 04:18:21'),
(7, 'sari.teacher', 'sari.teacher@school.com', 'hashed_pw', NULL, 'TEACHER', 0, NULL, 1, 0, 0, '2026-02-19 04:18:21', '2026-02-19 04:18:21'),
(8, 'yoga.teacher', 'yoga.teacher@school.com', 'hashed_pw', NULL, 'TEACHER', 0, NULL, 1, 0, 0, '2026-02-19 04:18:21', '2026-02-19 04:18:21'),
(9, 'tanti.teacher', 'tanti.teacher@school.com', 'hashed_pw', NULL, 'TEACHER', 0, NULL, 1, 0, 0, '2026-02-19 04:18:21', '2026-02-19 04:18:21'),
(10, 'fajar.teacher', 'fajar.teacher@school.com', 'hashed_pw', NULL, 'TEACHER', 0, NULL, 1, 0, 0, '2026-02-19 04:18:21', '2026-02-19 04:18:21'),
(11, 'lia.teacher', 'lia.teacher@school.com', 'hashed_pw', NULL, 'TEACHER', 0, NULL, 1, 0, 0, '2026-02-19 04:18:21', '2026-02-19 04:18:21'),
(12, 'hendro.teacher', 'hendro.teacher@school.com', 'hashed_pw', NULL, 'TEACHER', 0, NULL, 1, 0, 0, '2026-02-19 04:18:21', '2026-02-19 04:18:21'),
(13, 'mira.teacher', 'mira.teacher@school.com', 'hashed_pw', NULL, 'TEACHER', 0, NULL, 1, 0, 0, '2026-02-19 04:18:21', '2026-02-19 04:18:21'),
(100, 'arya.student', 'arya@student.com', '$2a$10$kz9vmaxCnjwpaC5/UMfXPudJZ1JHJA9ejbvrXnPLun5p4b9MufuRW', NULL, 'STUDENT', 0, NULL, 1, 0, 0, '2026-02-19 04:25:43', '2026-02-19 04:25:43'),
(101, 'naya.student', 'naya@student.com', 'hashed_pw', NULL, 'STUDENT', 0, NULL, 1, 0, 0, '2026-02-19 04:25:43', '2026-02-19 04:25:43'),
(102, 'dimas.student', 'dimas@student.com', 'hashed_pw', NULL, 'STUDENT', 0, NULL, 1, 0, 0, '2026-02-19 04:25:43', '2026-02-19 04:25:43'),
(103, 'karen.student', 'karen@student.com', 'hashed_pw', NULL, 'STUDENT', 0, NULL, 1, 0, 0, '2026-02-19 04:25:43', '2026-02-19 04:25:43'),
(104, 'raka.student', 'raka@student.com', 'hashed_pw', NULL, 'STUDENT', 0, NULL, 1, 0, 0, '2026-02-19 04:25:43', '2026-02-19 04:25:43'),
(105, 'zara.student', 'zara@student.com', 'hashed_pw', NULL, 'STUDENT', 0, NULL, 1, 0, 0, '2026-02-19 04:25:43', '2026-02-19 04:25:43'),
(106, 'alvin.student', 'alvin@student.com', 'hashed_pw', NULL, 'STUDENT', 0, NULL, 1, 0, 0, '2026-02-19 04:25:43', '2026-02-19 04:25:43'),
(107, 'citra.student', 'citra@student.com', 'hashed_pw', NULL, 'STUDENT', 0, NULL, 1, 0, 0, '2026-02-19 04:25:43', '2026-02-19 04:25:43'),
(108, 'kevin.student', 'kevin@student.com', 'hashed_pw', NULL, 'STUDENT', 0, NULL, 1, 0, 0, '2026-02-19 04:25:43', '2026-02-19 04:25:43'),
(109, 'melia.student', 'melia@student.com', 'hashed_pw', NULL, 'STUDENT', 0, NULL, 1, 0, 0, '2026-02-19 04:25:43', '2026-02-19 04:25:43'),
(110, 'reno.student', 'reno@student.com', 'hashed_pw', NULL, 'STUDENT', 0, NULL, 1, 0, 0, '2026-02-19 04:25:43', '2026-02-19 04:25:43'),
(111, 'salsa.student', 'salsa@student.com', 'hashed_pw', NULL, 'STUDENT', 0, NULL, 1, 0, 0, '2026-02-19 04:25:43', '2026-02-19 04:25:43'),
(112, 'jovan.student', 'jovan@student.com', 'hashed_pw', NULL, 'STUDENT', 0, NULL, 1, 0, 0, '2026-02-19 04:25:43', '2026-02-19 04:25:43'),
(113, 'nayla.student', 'nayla@student.com', 'hashed_pw', NULL, 'STUDENT', 0, NULL, 1, 0, 0, '2026-02-19 04:25:43', '2026-02-19 04:25:43'),
(114, 'fauzan.student', 'fauzan@student.com', 'hashed_pw', NULL, 'STUDENT', 0, NULL, 1, 0, 0, '2026-02-19 04:25:43', '2026-02-19 04:25:43'),
(115, 'tania.student', 'tania@student.com', 'hashed_pw', NULL, 'STUDENT', 0, NULL, 1, 0, 0, '2026-02-19 04:25:43', '2026-02-19 04:25:43'),
(116, 'ilham.student', 'ilham@student.com', 'hashed_pw', NULL, 'STUDENT', 0, NULL, 1, 0, 0, '2026-02-19 04:25:43', '2026-02-19 04:25:43'),
(117, 'dinda.student', 'dinda@student.com', 'hashed_pw', NULL, 'STUDENT', 0, NULL, 1, 0, 0, '2026-02-19 04:25:43', '2026-02-19 04:25:43'),
(118, 'rehan.student', 'rehan@student.com', 'hashed_pw', NULL, 'STUDENT', 0, NULL, 1, 0, 0, '2026-02-19 04:25:43', '2026-02-19 04:25:43'),
(119, 'selin.student', 'selin@student.com', 'hashed_pw', NULL, 'STUDENT', 0, NULL, 1, 0, 0, '2026-02-19 04:25:43', '2026-02-19 04:25:43'),
(120, 'naufal.student', 'naufal@student.com', 'hashed_pw', NULL, 'STUDENT', 0, NULL, 1, 0, 0, '2026-02-19 04:25:43', '2026-02-19 04:25:43'),
(121, 'kayla.student', 'kayla@student.com', 'hashed_pw', NULL, 'STUDENT', 0, NULL, 1, 0, 0, '2026-02-19 04:25:43', '2026-02-19 04:25:43'),
(122, 'rizky.student', 'rizky@student.com', 'hashed_pw', NULL, 'STUDENT', 0, NULL, 1, 0, 0, '2026-02-19 04:25:43', '2026-02-19 04:25:43'),
(123, 'putri.student', 'putri@student.com', 'hashed_pw', NULL, 'STUDENT', 0, NULL, 1, 0, 0, '2026-02-19 04:25:43', '2026-02-19 04:25:43'),
(124, 'galih.student', 'galih@student.com', 'hashed_pw', NULL, 'STUDENT', 0, NULL, 1, 0, 0, '2026-02-19 04:25:43', '2026-02-19 04:25:43'),
(125, 'siska.student', 'siska@student.com', 'hashed_pw', NULL, 'STUDENT', 0, NULL, 1, 0, 0, '2026-02-19 04:25:43', '2026-02-19 04:25:43'),
(126, 'rama.student', 'rama@student.com', 'hashed_pw', NULL, 'STUDENT', 0, NULL, 1, 0, 0, '2026-02-19 04:25:43', '2026-02-19 04:25:43'),
(127, 'viona.student', 'viona@student.com', 'hashed_pw', NULL, 'STUDENT', 0, NULL, 1, 0, 0, '2026-02-19 04:25:43', '2026-02-19 04:25:43'),
(128, 'bintang.student', 'bintang@student.com', 'hashed_pw', NULL, 'STUDENT', 0, NULL, 1, 0, 0, '2026-02-19 04:25:43', '2026-02-19 04:25:43'),
(129, 'aurel.student', 'aurel@student.com', 'hashed_pw', NULL, 'STUDENT', 0, NULL, 1, 0, 0, '2026-02-19 04:25:43', '2026-02-19 04:25:43'),
(130, 'haikal.student', 'haikal@student.com', 'hashed_pw', NULL, 'STUDENT', 0, NULL, 1, 0, 0, '2026-02-19 04:25:43', '2026-02-19 04:25:43'),
(131, 'michelle.student', 'michelle@student.com', 'hashed_pw', NULL, 'STUDENT', 0, NULL, 1, 0, 0, '2026-02-19 04:25:43', '2026-02-19 04:25:43'),
(132, 'andre.student', 'andre@student.com', 'hashed_pw', NULL, 'STUDENT', 0, NULL, 1, 0, 0, '2026-02-19 04:25:43', '2026-02-19 04:25:43'),
(133, 'farhan.student', 'farhan@student.com', 'hashed_pw', NULL, 'STUDENT', 0, NULL, 1, 0, 0, '2026-02-19 04:25:43', '2026-02-19 04:25:43'),
(134, 'alika.student', 'alika@student.com', 'hashed_pw', NULL, 'STUDENT', 0, NULL, 1, 0, 0, '2026-02-19 04:25:43', '2026-02-19 04:25:43'),
(135, 'hansen.student', 'hansen@student.com', 'hashed_pw', NULL, 'STUDENT', 0, NULL, 1, 0, 0, '2026-02-19 04:25:43', '2026-02-19 04:25:43'),
(136, 'tiara.student', 'tiara@student.com', 'hashed_pw', NULL, 'STUDENT', 0, NULL, 1, 0, 0, '2026-02-19 04:25:43', '2026-02-19 04:25:43'),
(137, 'fikri.student', 'fikri@student.com', 'hashed_pw', NULL, 'STUDENT', 0, NULL, 1, 0, 0, '2026-02-19 04:25:43', '2026-02-19 04:25:43'),
(138, 'gisella.student', 'gisella@student.com', 'hashed_pw', NULL, 'STUDENT', 0, NULL, 1, 0, 0, '2026-02-19 04:25:43', '2026-02-19 04:25:43'),
(139, 'rian.student', 'rian@student.com', 'hashed_pw', NULL, 'STUDENT', 0, NULL, 1, 0, 0, '2026-02-19 04:25:43', '2026-02-19 04:25:43'),
(140, 'amel.student', 'amel@student.com', 'hashed_pw', NULL, 'STUDENT', 0, NULL, 1, 0, 0, '2026-02-19 04:25:43', '2026-02-19 04:25:43'),
(141, 'joel.student', 'joel@student.com', 'hashed_pw', NULL, 'STUDENT', 0, NULL, 1, 0, 0, '2026-02-19 04:25:43', '2026-02-19 04:25:43'),
(142, 'nabila.student', 'nabila@student.com', 'hashed_pw', NULL, 'STUDENT', 0, NULL, 1, 0, 0, '2026-02-19 04:25:43', '2026-02-19 04:25:43'),
(143, 'rafli.student', 'rafli@student.com', 'hashed_pw', NULL, 'STUDENT', 0, NULL, 1, 0, 0, '2026-02-19 04:25:43', '2026-02-19 04:25:43'),
(144, 'marsha.student', 'marsha@student.com', 'hashed_pw', NULL, 'STUDENT', 0, NULL, 1, 0, 0, '2026-02-19 04:25:43', '2026-02-19 04:25:43'),
(145, 'darren.student', 'darren@student.com', 'hashed_pw', NULL, 'STUDENT', 0, NULL, 1, 0, 0, '2026-02-19 04:25:43', '2026-02-19 04:25:43'),
(146, 'shafa.student', 'shafa@student.com', 'hashed_pw', NULL, 'STUDENT', 0, NULL, 1, 0, 0, '2026-02-19 04:25:43', '2026-02-19 04:25:43'),
(147, 'aditya.student', 'aditya@student.com', 'hashed_pw', NULL, 'STUDENT', 0, NULL, 1, 0, 0, '2026-02-19 04:25:43', '2026-02-19 04:25:43'),
(148, 'vania.student', 'vania@student.com', 'hashed_pw', NULL, 'STUDENT', 0, NULL, 1, 0, 0, '2026-02-19 04:25:43', '2026-02-19 04:25:43'),
(149, 'azka.student', 'azka@student.com', 'hashed_pw', NULL, 'STUDENT', 0, NULL, 1, 0, 0, '2026-02-19 04:25:43', '2026-02-19 04:25:43'),
(150, 'nadine.student', 'nadine@student.com', 'hashed_pw', NULL, 'STUDENT', 0, NULL, 1, 0, 0, '2026-02-19 04:25:43', '2026-02-19 04:25:43'),
(151, 'rio.student', 'rio@student.com', 'hashed_pw', NULL, 'STUDENT', 0, NULL, 1, 0, 0, '2026-02-19 04:25:43', '2026-02-19 04:25:43'),
(152, 'cindy.student', 'cindy@student.com', 'hashed_pw', NULL, 'STUDENT', 0, NULL, 1, 0, 0, '2026-02-19 04:25:43', '2026-02-19 04:25:43'),
(153, 'hendra.student', 'hendra@student.com', 'hashed_pw', NULL, 'STUDENT', 0, NULL, 1, 0, 0, '2026-02-19 04:25:43', '2026-02-19 04:25:43'),
(154, 'wulan.student', 'wulan@student.com', 'hashed_pw', NULL, 'STUDENT', 0, NULL, 1, 0, 0, '2026-02-19 04:25:43', '2026-02-19 04:25:43'),
(155, 'yusuf.student', 'yusuf@student.com', 'hashed_pw', NULL, 'STUDENT', 0, NULL, 1, 0, 0, '2026-02-19 04:25:43', '2026-02-19 04:25:43'),
(156, 'angel.student', 'angel@student.com', 'hashed_pw', NULL, 'STUDENT', 0, NULL, 1, 0, 0, '2026-02-19 04:25:43', '2026-02-19 04:25:43'),
(157, 'iqbal.student', 'iqbal@student.com', 'hashed_pw', NULL, 'STUDENT', 0, NULL, 1, 0, 0, '2026-02-19 04:25:43', '2026-02-19 04:25:43'),
(158, 'salma.student', 'salma@student.com', 'hashed_pw', NULL, 'STUDENT', 0, NULL, 1, 0, 0, '2026-02-19 04:25:43', '2026-02-19 04:25:43'),
(159, 'reza.student', 'reza@student.com', 'hashed_pw', NULL, 'STUDENT', 0, NULL, 1, 0, 0, '2026-02-19 04:25:43', '2026-02-19 04:25:43'),
(165, '110054526871038299434', '4dnnyk@gmail.com', NULL, NULL, 'STUDENT', 0, NULL, 1, 0, 0, '2026-03-09 21:55:21', '2026-03-09 21:55:21');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `log_audit_trails`
--
ALTER TABLE `log_audit_trails`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `mst_attendances`
--
ALTER TABLE `mst_attendances`
  ADD PRIMARY KEY (`id`),
  ADD KEY `student_id` (`student_id`);

--
-- Indexes for table `mst_classes`
--
ALTER TABLE `mst_classes`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `mst_homeroom_teachers`
--
ALTER TABLE `mst_homeroom_teachers`
  ADD PRIMARY KEY (`id`),
  ADD KEY `class_id` (`class_id`),
  ADD KEY `teacher_id` (`teacher_id`);

--
-- Indexes for table `mst_students`
--
ALTER TABLE `mst_students`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `class_id` (`class_id`);

--
-- Indexes for table `mst_teachers`
--
ALTER TABLE `mst_teachers`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `mst_users`
--
ALTER TABLE `mst_users`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `log_audit_trails`
--
ALTER TABLE `log_audit_trails`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `mst_attendances`
--
ALTER TABLE `mst_attendances`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=110;

--
-- AUTO_INCREMENT for table `mst_classes`
--
ALTER TABLE `mst_classes`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=20;

--
-- AUTO_INCREMENT for table `mst_homeroom_teachers`
--
ALTER TABLE `mst_homeroom_teachers`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- AUTO_INCREMENT for table `mst_students`
--
ALTER TABLE `mst_students`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=57;

--
-- AUTO_INCREMENT for table `mst_teachers`
--
ALTER TABLE `mst_teachers`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT for table `mst_users`
--
ALTER TABLE `mst_users`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=166;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `log_audit_trails`
--
ALTER TABLE `log_audit_trails`
  ADD CONSTRAINT `log_audit_trails_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `mst_users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `mst_attendances`
--
ALTER TABLE `mst_attendances`
  ADD CONSTRAINT `mst_attendances_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `mst_students` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `mst_homeroom_teachers`
--
ALTER TABLE `mst_homeroom_teachers`
  ADD CONSTRAINT `mst_homeroom_teachers_ibfk_1` FOREIGN KEY (`class_id`) REFERENCES `mst_classes` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `mst_homeroom_teachers_ibfk_2` FOREIGN KEY (`teacher_id`) REFERENCES `mst_teachers` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `mst_students`
--
ALTER TABLE `mst_students`
  ADD CONSTRAINT `mst_students_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `mst_users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `mst_students_ibfk_2` FOREIGN KEY (`class_id`) REFERENCES `mst_classes` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `mst_teachers`
--
ALTER TABLE `mst_teachers`
  ADD CONSTRAINT `mst_teachers_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `mst_users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
