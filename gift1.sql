/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `gift`
--

DROP TABLE IF EXISTS `gift`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `gift` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `message` text,
  `title` varchar(255) DEFAULT NULL,
  `uid` varchar(255) DEFAULT NULL,
  `voice_url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKgwgqrogsj3w19th09p8e0od34` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gift`
--

LOCK TABLES `gift` WRITE;
/*!40000 ALTER TABLE `gift` DISABLE KEYS */;
INSERT INTO `gift` VALUES (1,'âfwa',NULL,NULL,'/files/audio/554a97f7-2105-4652-b338-60cd5f4093b7_voice.webm');
/*!40000 ALTER TABLE `gift` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `gift_image`
--

DROP TABLE IF EXISTS `gift_image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `gift_image` (
  `gift_id` bigint DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `image_url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKm7rx0d4spiiufrhte3swvqhhe` (`gift_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gift_image`
--

LOCK TABLES `gift_image` WRITE;
/*!40000 ALTER TABLE `gift_image` DISABLE KEYS */;
INSERT INTO `gift_image` VALUES (1,1,'/files/images/ea57015a-4e3f-4adc-aa3f-9ca50caf3c1f_pexels-catiamatos-1072179.jpg'),(1,2,'/files/images/84b87cca-54a6-4c2e-a2ed-97a03860e267_pexels-eberhard-grossgasteiger-1428277.jpg');
/*!40000 ALTER TABLE `gift_image` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'memory_gift'
--

/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

