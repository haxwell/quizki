-- MySQL dump 10.13  Distrib 5.1.67, for redhat-linux-gnu (x86_64)
--
-- Host: 127.4.86.1    Database: quizki_db
-- ------------------------------------------------------
-- Server version	5.1.67

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `choice`
--

DROP TABLE IF EXISTS `choice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `choice` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `text` varchar(1000) NOT NULL,
  `isCorrect` int(11) NOT NULL,
  `sequence` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=60 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `choice`
--

LOCK TABLES `choice` WRITE;
/*!40000 ALTER TABLE `choice` DISABLE KEYS */;
INSERT INTO `choice` VALUES (1,'florida box turtle',1,0),(2,'turtle',1,0),(3,'otter',1,1),(4,'dog',0,0),(5,'giraffe',0,0),(6,'cat',0,0),(7,'They are 3 to 5 feet tall when standing on all fours.',1,0),(8,'They live 15-35 years in the wild.',1,1),(9,'They do not eat meat.',0,0),(10,'They live in arctic conditions.',0,0),(11,'Elephant',1,4),(12,'cow',1,2),(13,'goat',1,1),(14,'hippo',1,3),(15,'True',1,0),(16,'False',0,0),(17,'False',0,0),(18,'True',1,0),(19,'False',0,0),(20,'True',1,0),(21,'to make efficient the government procedures to the citizens',1,0),(22,'to connect people with government',0,0),(23,'to minimize corruption',0,0),(24,'a bid',1,0),(25,'bid',1,0),(26,'offer',1,0),(27,'False',0,0),(28,'True',1,0),(29,'True',1,0),(30,'False',0,0),(31,'short',0,0),(32,'long',0,0),(33,'put',1,1),(34,'call',0,0),(35,'False',1,0),(36,'True',0,0),(37,'False',0,0),(38,'True',1,0),(39,'in the money',1,0),(40,'False',0,0),(41,'True',1,0),(42,'True',1,0),(43,'False',0,0),(44,'at the money',1,1),(45,'out of the money',0,0),(46,'in the money',0,0),(47,'False',1,0),(48,'True',0,0),(49,'prices will fall',0,0),(50,'prices will rise',1,0),(51,'prices will remain the same',0,0),(52,'True',0,0),(53,'False',1,0),(54,'long',1,0),(55,'short',0,0),(56,'long',0,0),(57,'short',1,0),(58,'volatility',1,0),(59,'volatility',1,0);
/*!40000 ALTER TABLE `choice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `difficulty`
--

DROP TABLE IF EXISTS `difficulty`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `difficulty` (
  `id` bigint(20) NOT NULL,
  `text` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `difficulty`
--

LOCK TABLES `difficulty` WRITE;
/*!40000 ALTER TABLE `difficulty` DISABLE KEYS */;
INSERT INTO `difficulty` VALUES (1,'Junior'),(2,'Intermediate'),(3,'Well-versed'),(4,'Guru');
/*!40000 ALTER TABLE `difficulty` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `exam`
--

DROP TABLE IF EXISTS `exam`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `exam` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(128) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `exam`
--

LOCK TABLES `exam` WRITE;
/*!40000 ALTER TABLE `exam` DISABLE KEYS */;
/*!40000 ALTER TABLE `exam` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `exam_question`
--

DROP TABLE IF EXISTS `exam_question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `exam_question` (
  `exam_id` bigint(20) NOT NULL,
  `question_id` bigint(20) NOT NULL,
  KEY `exam_id` (`exam_id`),
  KEY `question_id` (`question_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `exam_question`
--

LOCK TABLES `exam_question` WRITE;
/*!40000 ALTER TABLE `exam_question` DISABLE KEYS */;
/*!40000 ALTER TABLE `exam_question` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `question`
--

DROP TABLE IF EXISTS `question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `question` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `text` varchar(10000) NOT NULL,
  `difficulty_id` bigint(20) NOT NULL,
  `type_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `description` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `difficulty_id` (`difficulty_id`),
  KEY `type_id` (`type_id`),
  KEY `user_id` (`user_id`)
) ENGINE=MyISAM AUTO_INCREMENT=27 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `question`
--

LOCK TABLES `question` WRITE;
/*!40000 ALTER TABLE `question` DISABLE KEYS */;
INSERT INTO `question` VALUES (1,'<p><img src=\"http://upload.wikimedia.org/wikipedia/commons/f/f4/Florida_Box_Turtle_Digon3_re-edited.jpg\" alt=\"\" width=\"276\" height=\"221\" /></p>',1,3,1,'What animal is this?'),(2,'<p><img src=\"http://onemoregeneration.org/wp-content/uploads/2011/11/OMG-Trivia-11-15-11.jpg\" alt=\"\" width=\"288\" height=\"216\" /></p>',1,1,1,'What animal is this?'),(3,'<p><img src=\"http://bearsoftheworld.net/images/bears/brown_bear_ru.jpg\" alt=\"\" width=\"287\" height=\"215\" /></p>',1,2,1,'Which are characteristics of brown bears'),(4,'<p><img src=\"http://2.bp.blogspot.com/-xpgIOttwTTg/TWRni-cC-tI/AAAAAAAAA-o/9GMqFyMnQCY/s1600/african-elephant2.jpg\" alt=\"\" width=\"268\" height=\"185\" />&nbsp;<img src=\"http://kassowal.com/wallpaper/Animal-wallpaper/Cow/2.jpg\" alt=\"\" width=\"239\" height=\"180\" />&nbsp;<img src=\"http://sites.psu.edu/reshmajblog/files/2012/11/Beautiful-animals-of-Kenya-Masai-Mara-National-Park-Hippopotamus-Adult-with-Baby-Masai-Mara-Kenya-Posters-beautiful-endangered-anmimals-of-kenya-hippopotamus-species-hippopotamus-dangerous-habitat-picture.jpg\" alt=\"\" width=\"237\" height=\"178\" />&nbsp;<img src=\"http://www.kayfabenews.com/wp-content/uploads/2013/01/goat.jpg\" alt=\"\" width=\"244\" height=\"183\" /></p>',2,4,1,'Put these animals in order by weight, ascending.'),(5,'<p>A&nbsp;<span style=\"text-decoration: underline;\">heterotroph</span> is an organism that must ingest other organisms in order to grow.</p>',2,1,1,''),(6,'<p>A <span style=\"text-decoration: underline;\">photoautotroph</span> is an organism that can use energy from sunlight to grow.</p>',2,1,1,''),(7,'<p>Mammals are warm blooded.</p>',1,1,1,''),(8,'<p>What is the main objective of e-government?</p>',1,1,3,''),(9,'<p>The highest price a prospective buyer is willing to pay for a unit of a given security</p>',1,3,1,''),(10,'<p>The price at which someone who owns a security is willing to sell it...</p>',1,3,1,''),(11,'<p>A&nbsp;<strong>listed option</strong> is a put or call that an exchange has authorized for trading.</p>',1,1,1,''),(12,'<p>The&nbsp;<strong>strike price</strong> is the price that you, as the owner of the option, are entitled to buy or sell the underlying commodity.</p>',1,1,1,''),(13,'<p>An option to&nbsp;<strong>sell</strong> a security at a given price on or before a certain date.</p>',1,1,1,''),(14,'<p>If you are hoping the underlying instrument goes down in value, you should be using a&nbsp;<strong>call</strong> option.</p>',1,1,1,''),(15,'<p>If you are hoping the underlying instrument goes up in value, you should be using a&nbsp;<strong>call</strong>&nbsp;option.</p>',1,1,1,''),(16,'<p>If the <strong>strike price</strong> is less than the <strong>market price</strong> of the underlying security, a call option is said to be</p>',1,3,1,''),(17,'<p>A&nbsp;<strong>put option</strong> is \"in the money\" if the strike price is <em>above</em> the market price.</p>',1,1,1,''),(18,'<p>To be \"in the money\" means you can exercise your option at a profit, given the current market conditions.</p>',1,1,1,''),(19,'<p>When the&nbsp;<strong>strike price</strong>&nbsp;is equal to the market price, an option is said to be...</p>',1,1,1,''),(20,'<p>The&nbsp;<strong>market price</strong> is the price you are entitled to exercise your option at</p>',1,1,1,''),(21,'<p>To be&nbsp;<strong>bullish&nbsp;</strong>means you think</p>',1,1,1,''),(22,'<p>To be long is to have the <strong>right of assignment</strong>.</p>',1,1,1,''),(23,'<p>If you have&nbsp;the&nbsp;<strong>right of exercise</strong>, you are _______ a commodity.</p>',1,1,1,''),(24,'<p>If you have the&nbsp;<strong>obligation of assignment</strong>, you are _________ a commodity.</p>',1,1,1,''),(25,'<p>A measure of the fluctuation in the market price of the underlying security....</p>',1,3,1,''),(26,'<p>The annualized standard deviation of returns is defined as ....</p>',3,3,1,'');
/*!40000 ALTER TABLE `question` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `question_choice`
--

DROP TABLE IF EXISTS `question_choice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `question_choice` (
  `question_id` bigint(20) NOT NULL,
  `choice_id` bigint(20) NOT NULL,
  KEY `question_id` (`question_id`),
  KEY `choice_id` (`choice_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `question_choice`
--

LOCK TABLES `question_choice` WRITE;
/*!40000 ALTER TABLE `question_choice` DISABLE KEYS */;
INSERT INTO `question_choice` VALUES (1,1),(1,2),(2,3),(2,6),(2,5),(2,4),(3,9),(3,10),(3,8),(3,7),(4,11),(4,13),(4,14),(4,12),(5,15),(5,16),(6,18),(6,17),(7,20),(7,19),(8,23),(8,22),(8,21),(9,25),(9,24),(10,26),(11,28),(11,27),(12,30),(12,29),(13,33),(13,32),(13,31),(13,34),(14,35),(14,36),(15,37),(15,38),(16,39),(17,41),(17,40),(18,43),(18,42),(19,46),(19,44),(19,45),(20,48),(20,47),(21,51),(21,49),(21,50),(22,52),(22,53),(23,55),(23,54),(24,57),(24,56),(25,58),(26,59);
/*!40000 ALTER TABLE `question_choice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `question_reference`
--

DROP TABLE IF EXISTS `question_reference`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `question_reference` (
  `question_id` bigint(20) NOT NULL,
  `reference_id` bigint(20) NOT NULL,
  KEY `question_id` (`question_id`),
  KEY `reference_id` (`reference_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `question_reference`
--

LOCK TABLES `question_reference` WRITE;
/*!40000 ALTER TABLE `question_reference` DISABLE KEYS */;
INSERT INTO `question_reference` VALUES (1,1),(3,2),(4,3),(4,5),(4,6),(4,4),(5,7),(6,8),(8,9),(12,10);
/*!40000 ALTER TABLE `question_reference` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `question_topic`
--

DROP TABLE IF EXISTS `question_topic`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `question_topic` (
  `question_id` bigint(20) NOT NULL,
  `topic_id` bigint(20) NOT NULL,
  KEY `question_id` (`question_id`),
  KEY `topic_id` (`topic_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `question_topic`
--

LOCK TABLES `question_topic` WRITE;
/*!40000 ALTER TABLE `question_topic` DISABLE KEYS */;
INSERT INTO `question_topic` VALUES (1,1),(2,4),(2,1),(3,3),(3,1),(4,1),(5,5),(5,1),(6,6),(6,7),(6,5),(7,8),(7,1),(8,9),(9,10),(9,11),(9,12),(10,10),(10,11),(10,12),(11,10),(11,11),(11,12),(12,10),(12,11),(12,12),(13,10),(13,11),(13,12),(14,10),(14,11),(14,12),(15,10),(15,11),(15,12),(16,10),(16,11),(16,12),(17,10),(17,11),(17,12),(18,10),(18,11),(18,12),(19,10),(19,11),(19,12),(20,10),(20,11),(20,12),(21,10),(21,11),(21,12),(22,10),(22,11),(22,12),(23,10),(23,11),(23,12),(24,10),(24,11),(24,12),(25,10),(25,11),(25,12),(26,10),(26,11),(26,12);
/*!40000 ALTER TABLE `question_topic` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `question_type`
--

DROP TABLE IF EXISTS `question_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `question_type` (
  `id` bigint(20) NOT NULL,
  `text` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `question_type`
--

LOCK TABLES `question_type` WRITE;
/*!40000 ALTER TABLE `question_type` DISABLE KEYS */;
INSERT INTO `question_type` VALUES (1,'Single'),(2,'Multiple'),(3,'String'),(4,'Sequence');
/*!40000 ALTER TABLE `question_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reference`
--

DROP TABLE IF EXISTS `reference`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `reference` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `text` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reference`
--

LOCK TABLES `reference` WRITE;
/*!40000 ALTER TABLE `reference` DISABLE KEYS */;
INSERT INTO `reference` VALUES (1,'http://en.wikipedia.org/wiki/Turtle'),(2,'http://bearsoftheworld.net/brown_bears.asp'),(3,'http://en.wikipedia.org/wiki/hippopotamus'),(4,'http://en.wikipedia.org/wiki/goat'),(5,'http://en.wikipedia.org/wiki/elephant'),(6,'http://en.wikipedia.org/wiki/cow'),(7,'http://en.wikipedia.org/wiki/Heterotroph'),(8,'http://en.wikipedia.org/wiki/Photoautotroph'),(9,'a book...'),(10,'http://en.wikipedia.org/wiki/Strike_price');
/*!40000 ALTER TABLE `reference` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `topic`
--

DROP TABLE IF EXISTS `topic`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `topic` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `text` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=13 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `topic`
--

LOCK TABLES `topic` WRITE;
/*!40000 ALTER TABLE `topic` DISABLE KEYS */;
INSERT INTO `topic` VALUES (1,'animals'),(2,'aquatic'),(3,'bears'),(4,'aquatic animals'),(5,'biology'),(6,'sunlight'),(7,'plants'),(8,'mammals'),(9,'e-government'),(10,'trading'),(11,'commodities'),(12,'options');
/*!40000 ALTER TABLE `topic` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_roles`
--

DROP TABLE IF EXISTS `user_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_roles` (
  `id` bigint(20) NOT NULL,
  `text` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_roles`
--

LOCK TABLES `user_roles` WRITE;
/*!40000 ALTER TABLE `user_roles` DISABLE KEYS */;
INSERT INTO `user_roles` VALUES (1,'Administrator'),(2,'Beta Tester'),(3,'General User');
/*!40000 ALTER TABLE `user_roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(64) NOT NULL,
  `password` varchar(64) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'johnathan','password'),(2,'billy','password'),(3,'ale','bomboclat');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users_roles_map`
--

DROP TABLE IF EXISTS `users_roles_map`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users_roles_map` (
  `user_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  KEY `user_id` (`user_id`),
  KEY `role_id` (`role_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users_roles_map`
--

LOCK TABLES `users_roles_map` WRITE;
/*!40000 ALTER TABLE `users_roles_map` DISABLE KEYS */;
INSERT INTO `users_roles_map` VALUES (1,1),(2,2),(3,2);
/*!40000 ALTER TABLE `users_roles_map` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-02-13  0:00:54

