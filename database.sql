-- MySQL dump 10.13  Distrib 8.0.46, for Linux (x86_64)
--
-- Host: localhost    Database: promptvault
-- ------------------------------------------------------
-- Server version	8.0.46-0ubuntu0.22.04.3

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `policy_keywords`
--

DROP TABLE IF EXISTS `policy_keywords`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `policy_keywords` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `keyword` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `policy_keywords`
--

LOCK TABLES `policy_keywords` WRITE;
/*!40000 ALTER TABLE `policy_keywords` DISABLE KEYS */;
INSERT INTO `policy_keywords` VALUES (1,'password'),(2,'secret'),(3,'api_key'),(7,'pps');
/*!40000 ALTER TABLE `policy_keywords` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prompt_categories`
--

DROP TABLE IF EXISTS `prompt_categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `prompt_categories` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prompt_categories`
--

LOCK TABLES `prompt_categories` WRITE;
/*!40000 ALTER TABLE `prompt_categories` DISABLE KEYS */;
INSERT INTO `prompt_categories` VALUES (1,'Programming prompts','Coding'),(2,'Research and investigation prompts','Research'),(3,'Legal related prompts','Legal'),(4,'A Test Description','Test');
/*!40000 ALTER TABLE `prompt_categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prompts`
--

DROP TABLE IF EXISTS `prompts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `prompts` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `flagged` bit(1) NOT NULL,
  `prompt_text` varchar(3000) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `visibility` varchar(255) DEFAULT NULL,
  `category_id` bigint DEFAULT NULL,
  `owner_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKcc8wje0n5qsvsb39lcsw5p27h` (`category_id`),
  KEY `FKe5rr5wevl4pbtsourihagnugl` (`owner_id`),
  CONSTRAINT `FKcc8wje0n5qsvsb39lcsw5p27h` FOREIGN KEY (`category_id`) REFERENCES `prompt_categories` (`id`),
  CONSTRAINT `FKe5rr5wevl4pbtsourihagnugl` FOREIGN KEY (`owner_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prompts`
--

LOCK TABLES `prompts` WRITE;
/*!40000 ALTER TABLE `prompts` DISABLE KEYS */;
INSERT INTO `prompts` VALUES (3,_binary '\0','Explain Java Streams with examples','Java Streams Guide','SHARED',1,5),(5,_binary '\0','Explain Java Streams','Java Streams','PRIVATE',1,7),(6,_binary '','My password is abc123!','Credentials','PRIVATE',1,5),(7,_binary '','My password is abc123','Credentials','PRIVATE',1,5),(8,_binary '','My password is abc123','Credentials','PRIVATE',1,5),(9,_binary '\0','test123','test','PRIVATE',1,7),(10,_binary '\0','teeeeeeeee','test','PRIVATE',1,5),(11,_binary '\0','testt','testt','PRIVATE',4,9),(12,_binary '','password','password','PRIVATE',4,9);
/*!40000 ALTER TABLE `prompts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `submission_history`
--

DROP TABLE IF EXISTS `submission_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `submission_history` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `flagged` bit(1) NOT NULL,
  `simulated_response` varchar(3000) DEFAULT NULL,
  `submission_date` datetime(6) DEFAULT NULL,
  `prompt_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  `flagged_keyword` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKiywpkpkn7nll8rmwjyqi62c82` (`prompt_id`),
  KEY `FKksudnjwnccdebta5ugvvu8t37` (`user_id`),
  CONSTRAINT `FKiywpkpkn7nll8rmwjyqi62c82` FOREIGN KEY (`prompt_id`) REFERENCES `prompts` (`id`),
  CONSTRAINT `FKksudnjwnccdebta5ugvvu8t37` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `submission_history`
--

LOCK TABLES `submission_history` WRITE;
/*!40000 ALTER TABLE `submission_history` DISABLE KEYS */;
INSERT INTO `submission_history` VALUES (1,_binary '\0','Simulated AI response generated','2026-06-26 21:39:53.939740',5,7,NULL),(2,_binary '','Prompt flagged for review','2026-06-26 21:42:49.187286',6,5,NULL),(3,_binary '','Prompt flagged for review','2026-06-27 00:02:10.159837',6,5,'password'),(4,_binary '\0','Simulated AI response generated','2026-06-27 12:15:14.092597',3,5,NULL),(5,_binary '','Prompt flagged for review','2026-06-27 12:15:36.855960',6,5,'password'),(6,_binary '','This is a simulated AI response.','2026-06-27 12:23:53.929018',7,5,'password'),(7,_binary '','This is a simulated AI response.','2026-06-27 12:24:03.163464',7,5,'password'),(8,_binary '','This is a simulated AI response.','2026-06-27 12:24:06.498806',8,5,'password'),(9,_binary '','This is a simulated AI response.','2026-06-27 12:24:07.734167',6,5,'password'),(10,_binary '\0','This is a simulated AI response.','2026-06-27 12:26:16.669820',3,5,NULL),(11,_binary '','This is a simulated AI response.','2026-06-27 12:26:19.727213',6,5,'password'),(12,_binary '\0','This is a simulated AI response.','2026-06-27 13:26:27.209838',11,9,NULL),(13,_binary '','This is a simulated AI response.','2026-06-27 13:26:53.290871',12,9,'password'),(14,_binary '\0','This is a simulated AI response.','2026-06-27 13:30:15.131047',11,9,NULL),(15,_binary '','This is a simulated AI response.','2026-06-27 13:30:18.906660',12,9,'password');
/*!40000 ALTER TABLE `submission_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `enabled` bit(1) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  `surname` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`),
  UNIQUE KEY `UKr43af9ap4edm43mmtq01oddj6` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (5,'alice1@test.com',_binary '\0','Alice','$2a$10$hRd5p8Th7S.pC0gzPkrJIulA/u6WFpMTV4Po17VK/avk894qbA.WK','USER','Smith','alice1'),(7,'bob@test.com',_binary '','Bob','$2a$10$sBpkHE.j9UCdtdRmjK5Ovun86iow7vZxai8J1gl/5xt2/ZVQVPv72','USER','Jones','bob'),(8,'admin@promptvault.com',_binary '','System','$2a$10$xHgx3W5pcE6Xp394pyWdnOoY8rGsf.YbT/q8O/p4yji2JZHf7B0SW','ADMIN','Admin','admin'),(9,'niamh.gleeson@cellusys.com',_binary '','Niamh','$2a$10$75iO9FP5ygOAtl7uJGzy2u44sh4BgtdbjEB3221iekxzQ/eM5cBw6','USER','Gleeson','niamh1');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-06-27 14:09:44
