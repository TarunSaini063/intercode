-- phpMyAdmin SQL Dump
-- version 4.6.6deb5
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Nov 24, 2020 at 03:16 PM
-- Server version: 5.7.32-0ubuntu0.18.04.1
-- PHP Version: 7.2.24-0ubuntu0.18.04.7

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `InterCode`
--

-- --------------------------------------------------------

--
-- Table structure for table `Users`
--

CREATE TABLE `Users` (
  `name` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL DEFAULT 'unknown',
  `password` varchar(100) NOT NULL DEFAULT 'unknown',
  `mobile` varchar(12) NOT NULL DEFAULT '0',
  `github` varchar(100) NOT NULL DEFAULT 'unknown',
  `linkedin` varchar(100) NOT NULL DEFAULT 'unknown',
  `skills` varchar(1000) NOT NULL DEFAULT 'unknown',
  `qualification` varchar(1000) NOT NULL DEFAULT 'unknown',
  `address` varchar(1000) NOT NULL DEFAULT 'unknown'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `Users`
--

INSERT INTO `Users` (`name`, `email`, `password`, `mobile`, `github`, `linkedin`, `skills`, `qualification`, `address`) VALUES
('tarun', '123@gmail.com', '12345', '283619', 'unknown', 'unknown', 'unknown', 'unknown', 'uy12bj dwm'),
('tarun', 'email1@gmail.com', '12345', '0', 'unknown', 'unknown', 'unknown', 'unknown', 'unknown'),
('satyam', 'email2@gmail.com', '12345', '0', 'unknown', 'unknown', 'unknown', 'unknown', 'oidqwnk'),
('Tarun', 'mail.com', 'unknown', '0', 'unknown', 'unknown', 'unknown', 'unknown', 'unknown');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `Users`
--
ALTER TABLE `Users`
  ADD PRIMARY KEY (`email`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
