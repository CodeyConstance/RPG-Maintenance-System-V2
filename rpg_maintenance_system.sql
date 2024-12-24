-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 24, 2024 at 07:03 AM
-- Server version: 10.4.27-MariaDB
-- PHP Version: 8.1.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `rpg_maintenance_system`
--

-- --------------------------------------------------------

--
-- Table structure for table `characters`
--

CREATE TABLE `characters` (
  `character_id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `char_class` varchar(50) NOT NULL,
  `level` int(11) NOT NULL,
  `hp` int(11) NOT NULL,
  `xp` int(11) NOT NULL,
  `gold` int(11) DEFAULT 100
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `characters`
--

INSERT INTO `characters` (`character_id`, `name`, `char_class`, `level`, `hp`, `xp`, `gold`) VALUES
(19, 'Frodo Baggins', 'Rogue', 12, 120, 2500, 0),
(20, 'Aragorn', 'Warrior', 20, 350, 5000, 1500),
(21, 'Gandalf', 'Mage', 30, 250, 10000, 1000),
(22, 'Legolas', 'Archer', 18, 200, 4000, 300),
(23, 'Gimli', 'Warrior', 15, 300, 3200, 2700),
(24, 'Samwise Gamgee', 'Rogue', 10, 100, 2000, 0),
(25, 'Boromir', 'Warrior', 16, 320, 3500, 1800),
(26, 'Sauron', 'Mage', 50, 1000, 20000, 0),
(27, 'Saruman', 'Mage', 40, 800, 15000, 2000),
(28, 'Eowyn', 'Warrior', 14, 280, 3000, 400),
(29, 'Bilbo Baggins', 'Rogue', 15, 130, 2800, 400),
(30, 'Thorin Oakenshield', 'Warrior', 18, 320, 4200, 750),
(31, 'Galadriel', 'Mage', 35, 300, 11000, 2000),
(32, 'Elrond', 'Mage', 32, 280, 10500, 300),
(33, 'Faramir', 'Warrior', 14, 260, 3100, 450),
(34, 'Haldir', 'Archer', 20, 210, 4600, 650),
(35, 'Pippin Took', 'Rogue', 8, 90, 1500, 150),
(36, 'Merry Brandybuck', 'Rogue', 9, 95, 1700, 200),
(37, 'Gollum', 'Rogue', 5, 70, 1200, 100),
(38, 'The Witch-King', 'Mage', 45, 900, 16000, 4000);

-- --------------------------------------------------------

--
-- Table structure for table `character_items`
--

CREATE TABLE `character_items` (
  `character_id` int(11) NOT NULL,
  `item_id` int(11) NOT NULL,
  `quantity` int(11) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `character_items`
--

INSERT INTO `character_items` (`character_id`, `item_id`, `quantity`) VALUES
(19, 19, 1),
(21, 16, 1),
(22, 19, 1),
(32, 15, 1);

-- --------------------------------------------------------

--
-- Table structure for table `items`
--

CREATE TABLE `items` (
  `item_id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `type` varchar(50) NOT NULL,
  `quantity` int(11) NOT NULL,
  `effect` text NOT NULL,
  `price` int(11) NOT NULL DEFAULT 0,
  `class_relevance` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `items`
--

INSERT INTO `items` (`item_id`, `name`, `type`, `quantity`, `effect`, `price`, `class_relevance`) VALUES
(14, 'Sting', 'Weapon', 15, 'Increases attack by 15', 500, 'Rogue'),
(15, 'Anduril', 'Weapon', 14, 'Increases attack by 30', 1500, 'Warrior'),
(16, 'Staff of Gandalf', 'Magic', 17, 'Boosts magic attack by 50', 2000, 'Mage'),
(17, 'Bow of the Galadhrim', 'Weapon', 6, 'Increases ranged attack by 20', 1000, 'Archer'),
(18, 'Mithril Armor', 'Defense', 16, 'Boosts defense by 25', 2000, 'Warrior,Rogue'),
(19, 'Healing Potion', 'Consumable', 16, 'Restores 50 HP', 100, 'Mage,Warrior,Rogue,Archer'),
(20, 'Elven Cloak', 'Defense', 9, 'Increases evasion by 10', 300, 'Rogue,Archer'),
(21, 'Anti-Magic Shield', 'Defense', 7, 'Reduces magic damage by 30', 1500, 'Warrior'),
(22, 'Palantir', 'Magic', 9, 'Boosts magic by 40', 2500, 'Mage'),
(23, 'Horn of Gondor', 'Consumable', 2, 'Summons allies in battle', 800, 'Warrior'),
(24, 'Orcrist', 'Weapon', 3, 'Increases attack by 25', 1200, 'Warrior'),
(25, 'Nenya', 'Magic', 9, 'Boosts magic by 60', 3000, 'Mage'),
(26, 'Lothlorien Bow', 'Weapon', 16, 'Increases ranged attack by 25', 1500, 'Archer'),
(27, 'The One Ring', 'Magic', 10, 'Boosts magic attack by 80, but corrupts the user', 5000, 'Mage,Rogue'),
(28, 'Aeglos', 'Weapon', 2, 'Increases attack by 30', 2000, 'Warrior,Mage'),
(29, 'Mirror of Galadriel', 'Magic', 1, 'Reveals hidden enemies', 2500, 'Mage'),
(30, 'Phial of Light', 'Consumable', 18, 'Illuminates dark areas', 200, 'Mage,Rogue'),
(31, 'Dagger of Westernesse', 'Weapon', 5, 'Increases attack by 10', 400, 'Rogue'),
(32, 'Shield of Rohan', 'Defense', 12, 'Boosts defense by 20', 1000, 'Warrior'),
(33, 'Golden Harp', 'Magic', 5, 'Calms enemies during battle', 1500, 'Mage,Archer');

-- --------------------------------------------------------

--
-- Table structure for table `transactions`
--

CREATE TABLE `transactions` (
  `transaction_id` int(11) NOT NULL,
  `character_id` int(11) DEFAULT NULL,
  `item_id` int(11) DEFAULT NULL,
  `transaction_type` enum('BUY','SELL') DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `total_cost` int(11) DEFAULT NULL,
  `transaction_date` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `transactions`
--

INSERT INTO `transactions` (`transaction_id`, `character_id`, `item_id`, `transaction_type`, `quantity`, `total_cost`, `transaction_date`) VALUES
(1, 19, 19, 'BUY', 1, 100, '2024-12-23 08:10:53'),
(2, 32, 15, 'BUY', 1, 1500, '2024-12-23 09:32:40'),
(3, 22, 19, 'BUY', 1, 100, '2024-12-23 09:33:06'),
(4, 21, 16, 'BUY', 1, 2000, '2024-12-23 09:52:17'),
(5, 22, 19, 'BUY', 1, 100, '2024-12-24 05:48:34'),
(6, 22, 19, 'SELL', 1, 100, '2024-12-24 05:48:37'),
(7, 20, 14, 'BUY', 1, 500, '2024-12-24 05:48:48'),
(8, 20, 14, 'SELL', 1, 500, '2024-12-24 05:48:52'),
(9, 20, 14, 'BUY', 1, 500, '2024-12-24 05:49:00'),
(10, 20, 14, 'BUY', 1, 500, '2024-12-24 05:49:02'),
(11, 20, 14, 'SELL', 1, 500, '2024-12-24 05:49:05'),
(12, 20, 14, 'SELL', 1, 500, '2024-12-24 05:49:07');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `characters`
--
ALTER TABLE `characters`
  ADD PRIMARY KEY (`character_id`);

--
-- Indexes for table `character_items`
--
ALTER TABLE `character_items`
  ADD PRIMARY KEY (`character_id`,`item_id`),
  ADD KEY `item_id` (`item_id`);

--
-- Indexes for table `items`
--
ALTER TABLE `items`
  ADD PRIMARY KEY (`item_id`);

--
-- Indexes for table `transactions`
--
ALTER TABLE `transactions`
  ADD PRIMARY KEY (`transaction_id`),
  ADD KEY `character_id` (`character_id`),
  ADD KEY `item_id` (`item_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `characters`
--
ALTER TABLE `characters`
  MODIFY `character_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=40;

--
-- AUTO_INCREMENT for table `items`
--
ALTER TABLE `items`
  MODIFY `item_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=34;

--
-- AUTO_INCREMENT for table `transactions`
--
ALTER TABLE `transactions`
  MODIFY `transaction_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `character_items`
--
ALTER TABLE `character_items`
  ADD CONSTRAINT `character_items_ibfk_1` FOREIGN KEY (`character_id`) REFERENCES `characters` (`character_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `character_items_ibfk_2` FOREIGN KEY (`item_id`) REFERENCES `items` (`item_id`) ON DELETE CASCADE;

--
-- Constraints for table `transactions`
--
ALTER TABLE `transactions`
  ADD CONSTRAINT `transactions_ibfk_1` FOREIGN KEY (`character_id`) REFERENCES `characters` (`character_id`),
  ADD CONSTRAINT `transactions_ibfk_2` FOREIGN KEY (`item_id`) REFERENCES `items` (`item_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
