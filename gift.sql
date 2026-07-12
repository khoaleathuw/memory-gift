-- MySQL dump 10.13  Distrib 8.0.44, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: memory_gift
-- ------------------------------------------------------
-- Server version	9.5.0

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
--

DROP TABLE IF EXISTS `gift`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `gift` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(255) DEFAULT NULL,
  `message` text,
  `voice_url` varchar(255) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gift`
--

LOCK TABLES `gift` WRITE;
/*!40000 ALTER TABLE `gift` DISABLE KEYS */;
INSERT INTO `gift` VALUES (1,NULL,'',NULL,'2026-06-26 14:51:54','2026-06-26 14:51:54'),(2,NULL,'dăffawf','1782485527214_18883e66-8677-43a3-89c6-25c2fa84ba6c_noinaycoanh_khoa.m4a','2026-06-26 14:52:07','2026-06-26 14:52:07'),(3,NULL,'dăffawf','1782485535386_e238851e-89f7-435f-b0da-22afbad1aa74_noinaycoanh_khoa.m4a','2026-06-26 14:52:15','2026-06-26 14:52:15'),(4,NULL,'dăffawf','1782485577103_71de4bb8-c674-4a69-859a-b380f37739c6_noinaycoanh_khoa.m4a','2026-06-26 14:52:57','2026-06-26 14:52:57'),(5,NULL,'đă','1782486210045_d6a89306-a842-4354-87da-23705cf91f38_noinaycoanh_khoa.m4a','2026-06-26 15:03:30','2026-06-26 15:03:30'),(6,NULL,'khoale','1782487282838_e5597be7-ad2d-4c4d-a304-b64e3ae4c90a_noinaycoanh_khoa.m4a','2026-06-26 15:21:22','2026-06-26 15:21:22'),(7,NULL,'KL ','1782488451307_6fa92fde-a8b5-4c9b-971f-aa133e6be9d7_noinaycoanh_khoa.m4a','2026-06-26 15:40:51','2026-06-26 15:40:51'),(8,NULL,'gsgsgsgs','1782488882465_8118551a-7454-49ae-b0f1-cc5e8c91a9f5_noinaycoanh_khoa.m4a','2026-06-26 15:48:02','2026-06-26 15:48:02'),(9,NULL,'Text-9','1782490180670_523bc3c7-4c6d-499e-86fd-3f4012ba90f2_noinaycoanh_khoa.m4a','2026-06-26 16:09:40','2026-06-26 16:09:40'),(10,NULL,'Noi Nay co anh','1782560619946_518c9ee3-3c47-46cd-84b3-f1ad963cd692_noinaycoanh_khoa.m4a','2026-06-27 11:43:40','2026-06-27 11:43:40'),(11,NULL,'jkjkl','1782561332604_6a4776c0-aecc-4875-b233-3b73f5f3de8d_noinaycoanh_khoa.m4a','2026-06-27 11:55:32','2026-06-27 11:55:32'),(12,NULL,'jkjkl','1782562189833_8494f516-da4e-4c88-9ff6-f6fae7b9521e_noinaycoanh_khoa.m4a','2026-06-27 12:09:49','2026-06-27 12:09:49'),(13,NULL,'kl','1782562376170_8cffda92-5fce-484f-8cbf-f4ef3ce10c14_noinaycoanh_khoa.m4a','2026-06-27 12:12:56','2026-06-27 12:12:56'),(14,NULL,'',NULL,'2026-06-27 12:14:55','2026-06-27 12:14:55'),(15,NULL,'Kl',NULL,'2026-06-27 12:15:12','2026-06-27 12:15:12');
/*!40000 ALTER TABLE `gift` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `gift_image`
--

DROP TABLE IF EXISTS `gift_image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `gift_image` (
  `id` int NOT NULL AUTO_INCREMENT,
  `gift_id` int NOT NULL,
  `image_url` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `gift_id` (`gift_id`),
  CONSTRAINT `gift_image_ibfk_1` FOREIGN KEY (`gift_id`) REFERENCES `gift` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gift_image`
--

LOCK TABLES `gift_image` WRITE;
/*!40000 ALTER TABLE `gift_image` DISABLE KEYS */;
INSERT INTO `gift_image` VALUES (1,2,'1782485527220_d865c26b-e334-4c6d-b459-a0fc654e9aa6_images (6).jpeg'),(2,3,'1782485535394_02b1434c-241e-4993-bcd6-0a2ec2fe0ccc_images (6).jpeg'),(3,4,'1782485577105_21110f2b-0e7d-44b8-9dae-d2c36e2f5ef8_images.png'),(4,5,'1782486210048_fe4a9ff6-dfbe-45a5-b5f3-acea46b8b906_images (6).jpeg'),(5,6,'1782487282847_8637f382-e16d-4ba4-b864-f715e9598a24_images (6).jpeg'),(6,7,'1782488451314_b01e0d75-aec0-41f1-afc4-b9215f51812c_images (4).jpeg'),(7,7,'1782488451317_0bb09abc-36af-4e71-91cf-0c146f2c1456_images (3).jpeg'),(8,7,'1782488451319_f09932a2-161d-461a-8d5d-8aa39bb1751c_images (5).jpeg'),(9,7,'1782488451321_3945f5b2-28b6-4ac3-b9ad-a3dc91aae7b3_images (1).jpeg'),(10,7,'1782488451322_9d6ef26c-c9c7-4f20-9ab1-7f4099866916_images.jpeg'),(11,7,'1782488451325_63b95ecf-d4d2-4b52-bbb6-9db173b9bfab_images (3).jpeg'),(12,8,'1782488882471_16ccd61f-da53-4732-8b45-99f6a1518f56_Screenshot 2024-09-24 214830.png'),(13,9,'1782490180677_a1a09efd-0ed4-4f86-a0a3-55077ec2e6ae_Screenshot 2024-09-24 214830.png'),(14,9,'1782490180681_41d11ece-81ae-4a58-ace7-d2417ce249fe_pexels-eberhard-grossgasteiger-1428277.jpg'),(15,9,'1782490180684_15dabb96-25b5-48dc-96a1-d285f2d6af89_pexels-maxime-francis-2246476.jpg'),(16,9,'1782490180688_97345305-435c-4cc4-a4c2-59fdb36cd9f3_pexels-catiamatos-1072179.jpg'),(17,10,'1782560619953_a88461b9-8cde-4385-8972-c797de33d280_maxresdefault.jpg'),(18,10,'1782560619955_ad065a7c-de86-45d9-9b34-14afc620b3db_haytraochoanh.jpg'),(19,10,'1782560619956_21cc893a-34af-4364-bd44-b5c611bf1b99_maxresdefault (1).jpg'),(20,11,'1782561332614_3db4c4b8-cada-4e8f-a3bc-5d718169442a_Screenshot 2024-09-24 214830.png'),(21,12,'1782562189835_13514b31-b949-4705-990a-914e009c33ce_thanh-pho-da-lat.webp'),(22,13,'1782562376214_b92b8045-d66a-4d9f-b3a4-5b588f173191_sontung05-1490876144086.webp'),(23,15,'1782562512179_9586589a-fb52-4d79-a958-a8611b230d30_att.CtKHg3MKVdXYyiv1CJO9tbv7SofpEx2QNtpY9bzFFag.jpeg');
/*!40000 ALTER TABLE `gift_image` ENABLE KEYS */;
UNLOCK TABLES;

/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-06-27 20:08:20
