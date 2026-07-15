
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
-- GTID state at the beginning of the backup 
--

--
-- Table structure for table `gift`


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
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gift`
--

LOCK TABLES `gift` WRITE;
/*!40000 ALTER TABLE `gift` DISABLE KEYS */;
INSERT INTO `gift` VALUES (1,'âfwa',NULL,NULL,'/files/audio/554a97f7-2105-4652-b338-60cd5f4093b7_voice.webm'),(2,'',NULL,NULL,NULL),(3,'',NULL,NULL,NULL),(4,'',NULL,NULL,NULL),(5,'',NULL,NULL,NULL),(6,'',NULL,NULL,NULL),(7,'',NULL,NULL,NULL),(8,'',NULL,NULL,NULL),(9,'',NULL,NULL,NULL),(10,'',NULL,NULL,NULL),(11,'',NULL,NULL,NULL),(12,'khoale ',NULL,NULL,'262b1d50-4332-4c50-93b5-cca8a771a53e_voice.webm'),(13,'khoale12',NULL,NULL,'947f9580-0e23-4de9-b4ca-339bee89f5b5_record.webm'),(14,'khoale ',NULL,NULL,'9ebb102c-00bc-489e-b2ea-6796a0800c1d_voice.webm');
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
  KEY `FKm7rx0d4spiiufrhte3swvqhhe` (`gift_id`),
  CONSTRAINT `FKm7rx0d4spiiufrhte3swvqhhe` FOREIGN KEY (`gift_id`) REFERENCES `gift` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gift_image`
--

LOCK TABLES `gift_image` WRITE;
/*!40000 ALTER TABLE `gift_image` DISABLE KEYS */;
INSERT INTO `gift_image` VALUES (1,1,'/files/images/ea57015a-4e3f-4adc-aa3f-9ca50caf3c1f_pexels-catiamatos-1072179.jpg'),(1,2,'/files/images/84b87cca-54a6-4c2e-a2ed-97a03860e267_pexels-eberhard-grossgasteiger-1428277.jpg'),(12,3,'087924bb-cfa9-45f2-8423-fad2eb4700eb_pexels-catiamatos-1072179.jpg'),(12,4,'bda6dac9-bc49-4961-bb77-61d925078edd_pexels-eberhard-grossgasteiger-1428277.jpg'),(12,5,'e3a54214-a70f-49c2-8ca7-aa20f1f98ab4_pexels-maxime-francis-2246476.jpg'),(12,6,'e0b23065-0264-4984-b6a2-9ea736a6d84f_Screenshot 2024-09-24 214830.png'),(13,7,'01035778-9371-4822-adbc-0e89557e5911_pexels-maxime-francis-2246476.jpg'),(13,8,'7a014267-f400-42af-9727-48c3abf47cf6_Screenshot 2024-09-24 214830.png'),(14,9,'fdef2c27-d6bb-4f01-9801-06477f14de19_pexels-eberhard-grossgasteiger-1428277.jpg'),(14,10,'cf681943-29c5-46cd-8ff1-59177bbdc961_pexels-maxime-francis-2246476.jpg'),(13,11,'6837d666-e06a-43bf-b73c-868927579295_pexels-maxime-francis-2246476.jpg');
/*!40000 ALTER TABLE `gift_image` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nfc_tag`
--

DROP TABLE IF EXISTS `nfc_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `nfc_tag` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `uid` varchar(255) NOT NULL,
  `gift_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uid` (`uid`),
  KEY `fk_nfc_gift` (`gift_id`),
  CONSTRAINT `fk_nfc_gift` FOREIGN KEY (`gift_id`) REFERENCES `gift` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nfc_tag`
--

LOCK TABLES `nfc_tag` WRITE;
/*!40000 ALTER TABLE `nfc_tag` DISABLE KEYS */;
INSERT INTO `nfc_tag` VALUES (1,'test123',9),(2,'null',11),(3,'NFC001',13),(4,'NFC002',14);
/*!40000 ALTER TABLE `nfc_tag` ENABLE KEYS */;
UNLOCK TABLES;
	