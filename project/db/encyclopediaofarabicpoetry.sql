-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 10, 2023 at 09:46 AM
-- Server version: 10.4.24-MariaDB
-- PHP Version: 8.1.6

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `encyclopediaofarabicpoetry`
--

-- --------------------------------------------------------

--
-- Table structure for table `book`
--

CREATE TABLE `book` (
  `id` int(11) NOT NULL,
  `book_Name` varchar(50) CHARACTER SET utf8 NOT NULL,
  `authorName` varchar(8192) CHARACTER SET utf8 DEFAULT NULL,
  `publishDate` date DEFAULT NULL,
  `authorDeathDate` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `poem`
--

CREATE TABLE `poem` (
  `id` int(11) NOT NULL,
  `book_id` int(11) NOT NULL,
  `poem_Title` varchar(500) CHARACTER SET utf8 NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `root`
--

CREATE TABLE `root` (
  `id` int(11) NOT NULL,
  `root_Word` varchar(50) CHARACTER SET utf8 NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `rootsoftokens`
--

CREATE TABLE `rootsoftokens` (
  `id` int(11) NOT NULL,
  `root_id` int(11) NOT NULL,
  `token_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `rootsofverses`
--

CREATE TABLE `rootsofverses` (
  `id` int(11) NOT NULL,
  `root_id` int(11) NOT NULL,
  `verseId` int(11) NOT NULL,
  `status` varchar(15) DEFAULT 'Not verified'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `tokenize`
--

CREATE TABLE `tokenize` (
  `id` int(11) NOT NULL,
  `token` varchar(50) CHARACTER SET utf8 NOT NULL,
  `partsOfSpeech` varchar(500) CHARACTER SET utf8 DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `tokensofverses`
--

CREATE TABLE `tokensofverses` (
  `verse_id` int(11) NOT NULL,
  `token_id` int(11) NOT NULL,
  `id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `verses`
--

CREATE TABLE `verses` (
  `id` int(11) NOT NULL,
  `poem_id` int(11) NOT NULL,
  `firstVerse` varchar(500) CHARACTER SET utf8 DEFAULT NULL,
  `secondVerse` varchar(500) CHARACTER SET utf8 DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `book`
--
ALTER TABLE `book`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `poem`
--
ALTER TABLE `poem`
  ADD PRIMARY KEY (`id`),
  ADD KEY `book_id foreign key` (`book_id`);

--
-- Indexes for table `root`
--
ALTER TABLE `root`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `unique_root_verse` (`root_Word`);

--
-- Indexes for table `rootsoftokens`
--
ALTER TABLE `rootsoftokens`
  ADD PRIMARY KEY (`id`),
  ADD KEY `root key` (`root_id`),
  ADD KEY `token key` (`token_id`);

--
-- Indexes for table `rootsofverses`
--
ALTER TABLE `rootsofverses`
  ADD PRIMARY KEY (`id`),
  ADD KEY `root_id foreign key` (`root_id`),
  ADD KEY `verse_id foreign key` (`verseId`);

--
-- Indexes for table `tokenize`
--
ALTER TABLE `tokenize`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `tokensofverses`
--
ALTER TABLE `tokensofverses`
  ADD PRIMARY KEY (`id`),
  ADD KEY `token id foreign key` (`token_id`),
  ADD KEY `verse id foreign key` (`verse_id`);

--
-- Indexes for table `verses`
--
ALTER TABLE `verses`
  ADD PRIMARY KEY (`id`),
  ADD KEY `poem_id foreign key` (`poem_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `book`
--
ALTER TABLE `book`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `poem`
--
ALTER TABLE `poem`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10751;

--
-- AUTO_INCREMENT for table `root`
--
ALTER TABLE `root`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15699;

--
-- AUTO_INCREMENT for table `rootsoftokens`
--
ALTER TABLE `rootsoftokens`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5692;

--
-- AUTO_INCREMENT for table `rootsofverses`
--
ALTER TABLE `rootsofverses`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5680;

--
-- AUTO_INCREMENT for table `tokenize`
--
ALTER TABLE `tokenize`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17488;

--
-- AUTO_INCREMENT for table `tokensofverses`
--
ALTER TABLE `tokensofverses`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19752;

--
-- AUTO_INCREMENT for table `verses`
--
ALTER TABLE `verses`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=91954;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `poem`
--
ALTER TABLE `poem`
  ADD CONSTRAINT `book_id foreign key` FOREIGN KEY (`book_id`) REFERENCES `book` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `rootsoftokens`
--
ALTER TABLE `rootsoftokens`
  ADD CONSTRAINT `root key` FOREIGN KEY (`root_id`) REFERENCES `root` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `token key` FOREIGN KEY (`token_id`) REFERENCES `tokenize` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `rootsofverses`
--
ALTER TABLE `rootsofverses`
  ADD CONSTRAINT `root_id foreign key` FOREIGN KEY (`root_id`) REFERENCES `root` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `verse_id foreign key` FOREIGN KEY (`verseId`) REFERENCES `verses` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `tokensofverses`
--
ALTER TABLE `tokensofverses`
  ADD CONSTRAINT `token id foreign key` FOREIGN KEY (`token_id`) REFERENCES `tokenize` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `verse id foreign key` FOREIGN KEY (`verse_id`) REFERENCES `verses` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `verses`
--
ALTER TABLE `verses`
  ADD CONSTRAINT `poem_id foreign key` FOREIGN KEY (`poem_id`) REFERENCES `poem` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
