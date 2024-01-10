-- MySQL dump 10.13  Distrib 8.2.0, for Win64 (x86_64)
--
-- Host: localhost    Database: boa_db
-- ------------------------------------------------------
-- Server version	8.2.0

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
-- Table structure for table `beneficiary`
--

DROP TABLE IF EXISTS `beneficiary`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `beneficiary` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `cin` varchar(255) DEFAULT NULL,
  `customerid` bigint DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `rib` varchar(255) DEFAULT NULL,
  `transferid` bigint NOT NULL,
  `customer_id` bigint DEFAULT NULL,
  `transfer_entity_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK9k47rotqnxuptgoa8d97as0qo` (`customer_id`),
  KEY `FK5c41ynlglg3nq9ofgvrpp1uw9` (`transfer_entity_id`),
  CONSTRAINT `FK5c41ynlglg3nq9ofgvrpp1uw9` FOREIGN KEY (`transfer_entity_id`) REFERENCES `transfer_entity` (`id`),
  CONSTRAINT `FK9k47rotqnxuptgoa8d97as0qo` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `beneficiary`
--

LOCK TABLES `beneficiary` WRITE;
/*!40000 ALTER TABLE `beneficiary` DISABLE KEYS */;
/*!40000 ALTER TABLE `beneficiary` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customer` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `cin` varchar(255) DEFAULT NULL,
  `ctc` bigint DEFAULT NULL,
  `customer_type` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `otp` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `rib` varchar(255) DEFAULT NULL,
  `kyc_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKcycw2mo060conujojml5y2g5j` (`kyc_id`),
  CONSTRAINT `FKcycw2mo060conujojml5y2g5j` FOREIGN KEY (`kyc_id`) REFERENCES `kyc` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer`
--

LOCK TABLES `customer` WRITE;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
INSERT INTO `customer` VALUES (1,'9876754321',NULL,'EXISTING','haffsa','Hamidi','2023','+19876543210',NULL,1),(2,'EE9392516',1,'PROSPECT','mohamed','AMMAHA',NULL,'+19876543210',NULL,2);
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `kyc`
--

DROP TABLE IF EXISTS `kyc`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `kyc` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `city` varchar(255) DEFAULT NULL,
  `country_of_address` varchar(255) DEFAULT NULL,
  `country_of_issue` varchar(255) DEFAULT NULL,
  `date_of_birth` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `gsm` varchar(255) DEFAULT NULL,
  `id_expiration_date` varchar(255) DEFAULT NULL,
  `id_number` varchar(255) DEFAULT NULL,
  `id_type` varchar(255) DEFAULT NULL,
  `id_validity_date` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `legal_address` varchar(255) DEFAULT NULL,
  `nationality` varchar(255) DEFAULT NULL,
  `profession` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `kyc`
--

LOCK TABLES `kyc` WRITE;
/*!40000 ALTER TABLE `kyc` DISABLE KEYS */;
INSERT INTO `kyc` VALUES (1,'Los Angeles','USA','USA','1985-05-15','jane.smith@example.com','haffsa','+19876543210','2025-06-30','9876754321','Passport','2025-06-30','Hamidi','456 Sunset Blvd','American','Doctor','Dr.'),(2,'paris ','USA','USA','1985-05-15','jane.smith@example.com','mohamed','+19876543210','2025-06-30','EE9392516','CIN','2025-06-30','AMMAHA','456 Sunset Blvd','American','Enginner','Dr.');
/*!40000 ALTER TABLE `kyc` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sirone`
--

DROP TABLE IF EXISTS `sirone`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sirone` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `birth` datetime(6) DEFAULT NULL,
  `cin` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `reason` varchar(255) DEFAULT NULL,
  `rib` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sirone`
--

LOCK TABLES `sirone` WRITE;
/*!40000 ALTER TABLE `sirone` DISABLE KEYS */;
/*!40000 ALTER TABLE `sirone` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transfer_entity`
--

DROP TABLE IF EXISTS `transfer_entity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transfer_entity` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `pincode` varchar(255) DEFAULT NULL,
  `amount` double NOT NULL,
  `benefeicaryid` bigint DEFAULT NULL,
  `initiated_at` varchar(255) DEFAULT NULL,
  `maxbamount_periodc` double NOT NULL,
  `maxpin_attempts` int NOT NULL,
  `max_transfers_per_customer` int NOT NULL,
  `motif` varchar(255) DEFAULT NULL,
  `pdf_content` longblob,
  `receipt_url` varchar(255) DEFAULT NULL,
  `reference` varchar(255) DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL,
  `to_be_served_from` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `validation_duration` int NOT NULL,
  `customer_id` bigint DEFAULT NULL,
  `wallet_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKnqd9nmnq7mptks8twlyeagfgq` (`customer_id`),
  KEY `FK9c8bmgps9ulfi9f5jwtgl55sl` (`wallet_id`),
  CONSTRAINT `FK9c8bmgps9ulfi9f5jwtgl55sl` FOREIGN KEY (`wallet_id`) REFERENCES `wallet` (`id`),
  CONSTRAINT `FKnqd9nmnq7mptks8twlyeagfgq` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transfer_entity`
--

LOCK TABLES `transfer_entity` WRITE;
/*!40000 ALTER TABLE `transfer_entity` DISABLE KEYS */;
INSERT INTO `transfer_entity` VALUES (1,'2023',400,NULL,'2024-01-05 19:16:24',500,0,5,NULL,_binary '%PDF-1.4\n%\â\ã\Ï\Ó\n2 0 obj\n<</Type/XObject/Subtype/Image/Width 448/Height 83/Length 5389/ColorSpace/DeviceGray/BitsPerComponent 8/Filter/FlateDecode>>stream\nxœ\í]gXG\×^@¥Û»Q\ì)*J4vƒ¢\ñµ1_,‰‚I\ì1úY\Ğ$`ìŠŠ]cL¢)\Öh¬‰±F\Å\nÖ¨±¢XÀ€PaŞsfwgwg\ğG|¾‹k\ïú\ì9\Óv\ïİ™3g\Î’dÃ†\r6lØ°a\Ã\ÆÂ½V\ÏIk\öŸKHˆûû\à\Úi½\ëz9»E6\òŒ\n=V\\~FtÈ¾¶\ê£J\În—< h¿])@Y\Æ\å½+L˜¿b\÷¥4<:0¤¤³[g\Ã1Ê„]®\Î\Ì\éR\Í]\ò\ë0\ãXßVÖ™­³\áe&Ş¦,o\àf\Ò\Õqœ\ê\î\Îy\Å	\r³‘t»I:Ù» X] \ë!ª¿\Õ\ï\å¶\ÊF\ŞPee\çÊB\ÖI\Ü>9M\Ólz\õ\åµ\ÊF\Ñ!R³°˜\ãT\Ş\óhª\ä\Ş/§M6\òŒ\ğ\ç´k\É=ag0pÆ›HÎƒ\ÛJ\ÊùªyI\ë\r}¨=¡ÿÿƒBk(%‡+²K\×ÿ\é\ÙÃ„\îj\ÏZdM½Á\ÛIµa„\Ğ¥\ò-\ág5½\ç\ôz¯S\Zk\Ã\à\ëRy\år>’\ÓT\Í\à{\nV;¥±6Œ§d\ÄWS.û<\óG¢\\\Õ,\å\â©`SškCvÀW;\å\ò\í,úùX\Ë\ô”\nú:£½6tx\í!ebrY\ç%}$‘[E©¯9£\Å6x\ì¦D\Äú°«\nW¬\é#d±–\Í\õ¨¡Kµ\á\ô¦4d·gW>1\è#\éoj\ÛÂ‚Ä—Ni´\r% »<Â®\\~uH!]´¬[¨\àIu§4\Û\Ã\\ ¥+^x.É…>BjY€	\óµs\ÚmP)ƒrp’b\Õs¥\Ü-¬e\ŞL™¶	\ãDŒR+—\ós\'0B\Ëü!–:£\İ6\0\Å\îQ’Ô¨¯s¹\ò\÷¬¡š»t2d\ÔpJ\Ó_ÅŠ¹\ç\ÊI(\ä\å\å`\É\Õ!ú%[5APv®\òQSo\0Á8‡U\ô\Z<2lBD\äÜ…‹/˜üº Eû)“e\Ô\Ô}?\î\ÓD\í-‚{zÂ…_ˆü;$H¯\î[ZØ‚zv]}˜»\ó^\ê\Ò9$Ø€R·P•¶oJgƒ´1u@\ì\ñÙ˜qSf\ÎY°h\ñ¢©\Z\ó>ş€>\ô–\Z\è‹\î»\ì\à\Å\ë\×/ûc\ñ° chQú`¦4¶h•¤\Ì\á\ÈXN´2\÷4PMü\\w8\Ô{s\ìjL\àq4+\ô\Ò\Z L\Ó>\öº x^.>€‹}Šn){³D+\"\rW#!Ÿ®­§\Éİ®šn¬¹\Å-|\Ú;>z\é_¦üe©oŠNtuB5\ÃpShé­µi|â¤•ú\'y\0¤‡-Z%£z:\Ûp¢Wsaœ\Öş^‡G\ó¬u\r’”dÌ¿Î°l\Ñ\Åz;¨\ÚÆ«\ï$¶\õ^w\å6¦\n\Å\â*H&x-\Ê\ä\ëÎˆT\ç?ngMw\ÖÄœŸ¢*{\ÈC\ô\âÃ¦ü\Ô\é“`>Pp=W- \ğ\nCŒ-Ù®« (¥ïˆ›%#\ô)exYO3cz¼«¥-’¯,+üfN3\ñ|˜\ñG6*kş\ŞÁ·\ğ‰ £\ñ\Øf¬|­Så¿L}Q?„\æ‘?B”˜~\ÇL)\Ç\ë*\ØÂ¤›\ÅÍ’±\ô§\õ_\íS±:\ãS\Ã: 9hY$\â/C7¶W\ÄÉº\áG\á\ÌfKşÊ³\'\Ö\Ç\\·\çVsûc¶‚€¿¦\ædx«Œt\Ñ\É\ÍüQ\'–€?\Åi¥\ç\Ïï’¹mº\npz-\ã?‹Gë‰¡º~«-1?s\rù´8\ßHt\\*°¬\ÙÃ‡½5y¯ÿE-Y·˜¡\òGz ÀŠ?·]˜\ì\'Aİ‹”2r’’\ÕjXíŒ¿\Ì\'*2\Z	o\àC5\çFù\Ë\â\ò·•ş²\ì\ŞşÛ®\Ë,\ß˜AÏŸ\ö™dŞº|\ã\ô•:\î5ÁT‹G\ëÆ¦ÙƒR¦\é\ào£\n\é;\ç\Â\'ƒ\Â\ö¦4 p\èş\ï9u­µ\è³|ÿ¤ñ—ˆ¯¯Œ£½Û¥!@\n(QªÁx•^”Œ¿\îU«©\ğ0!”\'\Õf\Õ\ã\È_.?5¿LX¨qos\Ò\\Ç¶\éøae>]Ö­†¯»w\Ùz\Îø‘)*yW­\÷¾…a\ÜÕ“\Å\ÚRGr?Ñ¥\é‡\Âpq	\0\ä¯>^Tk\â;N\Í{\ì\Ş\ã\ä\Z\ä\n4ß‚¿\÷É¢.`\ê¾a\áÈ…™™º®o9h: \×Ày\ò\×Êœ\ñ\Ç\æ\Ä\õÁú\ÎÁ‰ÏŸZ\äv\É¡\\½c\ÄI¦¡6Ì²\í\æ\ÙüıL³JR\èø«_\ÛpM\ë\÷˜\n’C¢ƒ\\L\"\Ç\ÙJ¿y1uÀ¯\çÿ+¨ù5œºpûjdBgÀøk\æ \é®{ \Ùüû\ô\ßøø<ä¯­!=\ãO\é	q’$\Ï_GFŸ¿dŸ8ª2º\È8q¬\ß\Ú\Üø›m\â\Ï·\ËøsdÀ µ\ñb4ªŠ¦\Ú_<ÁD\Ş\Ö<d‚$\ä\ï©¨hDU\ğ9¨\Î\Ğ$¬O‚øÿ<\ò\ç}\ğ}¯\Õ\Ü\ÃGä‰¿ı\Ç?\\6\Ğ—&\0ı±B\Ø{w¦9ˆ…L²,$\ĞH\ßYC\Ô.›»µ,Aá¯§¿¿İ \à77Q(„\Ä;\ìMY¢i?6€\åt\ò·»4”[\Øh!\èú\ö\ÉZ\õ4\ô<©5ü\Ï}‡¯@ÏŸl\"\è…\àùÃ®ıQy\É\n°Wˆ’>…ÿ·‰’¸ıw¿È²_c,\ÅC\ÖI_µ,A\á/›ÿÄ™	h\Ûw•ªA\ÇúŸü]G\ÆIz-\É\ß\Ì\ßS\æ\ë8U\\Tq\èıs\Z\ò²F\ğBì€»Gş\öoØˆ\Ø4DTH…ÇŒ\\\Ø\æ&)\È\ß!5ÿh2şXG;\Ò\Ü/\n<M¢,ŸZSxX·}¥\Â\ğäˆ¦¦^8} ›,K‘¶\ë\é»aŒøœ‡\ò\ëŒ\ó¿‡ı9ú\ï\È?\×Á/\ÍRFşnf}\Ä1\ïšfşl•D\ğ¿\\ZE^\æ—Ne\ÑP·qş·JT\ÈDP\Ñ\ï»ü:¡\õ\Æ\Æù\ß eü5*Y²d\Ù\0\öxØ¼ƒ\ã\Ï+·¶ph›.)Î»µ‚4\ÊT\ó”‹@‰ø?}‡\õ¿¡ü®e&ş2N\Õ\Úv©\Ôÿ\Ó\n\ÙU-e\ä\ïNÁ\n\ñ˜\ï\×7²,ù;€pv\Ë¶’T>s\ğ(šø[&(£$šG\Ô:\òÆ¾H\ó>ù[R\öP\'\'\'?b½¿b£\òü¡\Ói\ÕC\ó\ÏZZeùg(\åi-s\"…¿±Û¢†\Îû|\ÙÓ fş3rÇ²\0µÿ|.ƒ\İ\ÏµE\ï\ÄuÄ¸©PE\Åø+\"µ`\Ä\õ­ùK«gªV~F§@×š—µ\Ñú3Oü\r\ÍM\õp¬İ«\ê\ò\Çc\ËÀ\÷Ÿ°‘;°.\ì£\à\÷xs\"\Ï8VA Y\Ç\àrœo\ÈH£º:s\ç\Ş\ö¨#£n{ˆ\Ó\'+™ª&P\ó¼c	¹³)6T—”“\ñWXb#8³e\Äü‘Ë¢}ù°QŒ¤À\ázi?›Kp\ÅÛŸUTn`‰6\à\ÏÊ®«(üÁ[!\àoºÒ½\ñüaœQº\Å~!\æ1HLq\\\ãK(bn\İV\ÃXA_q\r1»\'\Â\ë\ô\ó6 2£\rÎœ‡r_“œ\Äl\ÅÙ®\ñ\'}ÏµA\Ç_V\â\ÃD6\ÍXa¬VR7\ïq\Şù:˜º\'ı\Íø»}5aº¹„ ,<%‰¶01[ÿ> w\Ôü\è\Æ2\ò—£Mxş\ğ7ùBü\ÌF¡6‰Ö›\Ä \ædûY˜·r´¦L0i\×0\Íi\ë\ô\ówi0\\a\Ü}y\Í\'©aK\È\ñ\Ç/\"\è\çÅŠ«\Ç\nùÀ\\sYT\íS­\Ó\è\ñO‚2A‚M\ã;\rLQ¶ÿ#\Ôü\è\Ù0}š³\ç/•\É\Æ\éÀ\ó¢ Ş‹f\ï\Ş*¦z`}*HÁj·K•¥”µ\Â(\ËüFş\ğ~\ß\ã­$„9‘9ş¤r\÷\Äü\Å×‡)[º™ùw”­w4\Â‘½=Œ?«ÛªÿT‰L‹üı_\È_Ö»\ë1}7E\Å\ó\çÆ¼ª)\ïJf|h®”ˆ\Ş\ĞIŠ\ê#\ë{˜§\æ7\ë\ï+*ÁĞ¡ùc‹³Mp>.­â·„\Íd–2ÏŸ\ÔZ]¦¬¡Mb\Ì\ïg\åTT=^\ZÒ¦\í\Ç?0[,—\ğ\ó\à\Ç\"\Üfß³Cÿ\Èú\ákw½S\éü×Yi9\Ë{¼ZÔ·X•&]‡-Z‡~\î\ÇÍ•\Ê8`j_wEm¡¦y`úüØœM\Æhùûu\ö”q_Eş\ÉœaE³°•$«2d\Ó\ñ\';\àÏ“Á7\Ò@Ql‘-wşJ?·\ğ#T\ç\î?\Ã(\ò;S\é×–iw|?\r\ã\ØQt&B<«k¨Š…?P´3ªTTLgI\Ì{\Åj+Œz\İ\0ÁZb<}\\q\ô=Ü¬i3D\ÓNø¼\Ğ«\çO]¦­¿¡ü¬ƒ¹\òsåŠ‹ wş¾„©\Ô6C«ÿ\0¾\íy\ğ²t0\ß…¿’\Ñ\æ¶aW‹«\êqj½­®ƒ\Ä4ÁqW¹Y	\è\')\æ\Ã\ÏÔ•\×d\ë	¤ˆ¿\ğÆ¢C‘\ğ\áLhh\Ş\Îü\ÜmÍŸb®%V6\×>\ÙX\÷(E“+\è¹R_]°|]\óÀ_u\ì\÷“°a†ø‰¢¿›˜À5p\Â\Ë\÷i\è‰Ï¨&°V½­ £JsÁL3)Z©\Õ\î¶\Ì,\Ãdd&¡%Œ­\å]\r°?€\×¢Z´w9\ì%\óü)\á>.,Lb‹ ú\à8¾\î\Øîª‚y­ù\Ã]\ÈÙ¼ã†!vˆG\ÄüÁ¨\Åü\×\ì\Í\Ú	³Zcü’\Û}¬š\ÜÑ©\"\Æ4\è\\\Ûp Ÿ%0J\ÍyJ¸\öL\ñ\ô’IfOù^5s¨ ›\n~H\Æ\ÍY\Í\Ñ/\õÛ—\ğ[¿II«³­$-Z¿!”\Æ@Oı{S\é>–6V\à	,6F5¡\Ï\åB\0\İpé©…U»}0¬\ñ´®\È!DË„V†q\ä\ñA³†]\Ø\ŞşHø¹K\\9\ò&\÷h’\ö”{\æj8*\éƒ\"p¹\"\İH\ç³\Ü\n\ír\ÌX+b¸š5\Óa\0v`—>ùtÔ—\ã§DÌ˜1¾S…¿ @-\õ§UiI…A\ô){¶ ?›p\ëUu¨²Î²J\Ñ\ß-µ¸Åš±e3¡\'×£uøÊ;–‡5\×Å¹4€<f·ƒW+(RV\ì\r„W0 ¥ù\ä×¢1·Pº•Ø°¦.\Êİµ\Ôw‚EÚ_µ/ú\È\Şu³·@GC\éÖCo-–\Çcj\ì>•„lq\ğ•„CH¦\É}Z?]\Í\ê0ü\ÌÆ¿	.\Ø\óºU”Zs¢[XE”¾¦\åt\Ø}\Úø7\á}]£\áXQqšI$İ¸I³\ĞoZ¾\Ç\öy’\Î\Ã0nüü\Ó\âD¬M¦z>5ÀÖ† \ó1l\æÈ›zAÁ\Õ\\¦TûTlgB\çD>jš!R\Ô2\ìĞ¬²›\Ï!\Êb\ãeÁ[w^\È-Q Z}¿\ÚX·\ñ*\Ù:„\Ê\Æ\Ë@G\İ\"I\ö$a,—\ïqú	ù€—\ÓJ–X«\ãƒ\\\é\á(q\'Ã®Å\Ûx(CO	\Ù\ö«\ó\Ë\ß^oHz\ß\Ñ\Î#/\r\Ó¬3ŸW1\'\óû\ì¤1aGœ\r\ç`€‰’ºsd\ÓJªO\Ûı•\ÆC·¤šS}\î\ÌVÿ\Ë(=v\òŒ\Ù\ó\"?Sœ»Æa¾[\÷c\ÕåšŠŸ­\Üû\çÚ™=!°9(<n\è{=l\İ\Ş\İk¦¼‹±u\'Î˜\÷\õ‚0\Îq\î?a\óŞ\ã”ı\ã^#\Ç\â¢\Æ[\áaĞ½Õ‹X¸lù…¡2ˆ	fjd¤]İ¿ú\ëÈ™W\í‰pGŒ;\ó”§ÀV§\Òu×–Rû)@•é•º]”\ß\æ\"¹Og®\âk8,…³Dë™‹\Úu6®?§M\Äu\áR\Ù,\æi4[\èP¢~4 ÀA‹ry¬±&\óü³\É_\ë\×\ízL\î\ñšC\Èa|À»\ÕVº3>)\æÀù\Ô\ğiE’œk—cep\ä$\Ze¿ÿ@l\Z‹\ì	%\ÙG\öD\İ\"d;dr¥Qd\'·\ÓXN\Ü\éV2™,€\ÃØüeııw	\Ùl}\æÿ\Ü§oqşş2tCw›\çl7\İü	\İfÀ3e·>!Ï¾’\ç¿+¼×‘$½¦;·en’M\n)\"†•\ØNŒP’VZ’ŠofA]!„<h\'\Ó\Ø1™\í3\ñWK’\ÊlU£ÀD\ó9À\×ùü\äO™?z\òÇ«Ï5ˆ˜†\àT\Z6¢%„|\Êg‰$i&d$K¿\ÆŠ{j\Ş\Ã\Ş\Ñ\í8\Él	\ò\÷²qc”ˆ?º? ¹£@y¹+·ˆ¶#Cœ¿ \ó\÷ıû\İ\'{\Ñı´“\Şv3p‚J\ÜÎ’‡:¾\"\É\Óydğ‘š•\Ğc†^\ëK\ñ	øFB\É?t\äƒA±\Ó\ÕcˆN“´840-\Äq°ª\ô\æ™\\8\ã\ç(\â,€\î@3Ÿ\ğ’\ğ€º+–’”\ä\"\r\ğ¾Iş\ÖuB‘\ì\é\âdusÈ·jQ\0•„’Œà¶&&“\ó4Ü³n¶zÀ&’N\×Tş†jü\õ®\ßd\Ä=r\Ç\ñ\ß6*>\Ëú\är=¾)ã° |™¿+û¾O.Ã¨\ãz†l\\@\Ò\Ëx%‘…?j\nŠ%\ñºY‘$ûÂ™3g\Î.\àd5ŸR\Ò{\ßJˆOd{_”ƒ	\î4fú\õ,iyLùJ¦0\Ã~;‘\æc–\áùû¹µ¹ù‰¼°w\Ú:Z4A\æn5­püNƒW<¯’\å¯f’\ğş$»\ö\\’¶ÿf\ò\\wdšl¿Tu•ÁG\áx]§gc\ô\õ\ñ\éDr9’sHVKE\Ïv\Ø\ÔL%Q\Ã\Ç\ë&ù‹\És°‹€¿\ì‡\Û,ª\Ğ\à>(\×N\ô\ï\Ñ\ÖÓüf¿H;\ÈM\Z\÷T\ä\Ü\Ï-\'\Évi:IƒE\Ï\÷	¹À›„²ıb\î\áf\Ê\Ö*<±–*\éş\Åû¨»†dıv:û\÷=@N5wTº	\Ä?…\ÄBÄ•\Ì_ÿ\ÚV‡#\Z\à,ˆ\Ö\ğ\× \Ë@\Ã|j¿t~§Ëœgd7ı\Ê<\"3¥:\ò\0“(}AA¤»\ÇAB’…\ô[\õ|r²ı\ò]¤ŒY|U\ö¦\ÌrÄ°Ÿ/«\ã_Zy\Ø[ºeb\å§:¦Wø9y–‡\ÛÓƒ\åGD¯\ğ\Û\Ê~g™¿7^ \á®ÿù\Ù\è\Ñfˆ_\×%_O\Ùu¨§\ÜuB5`›\ä2˜ú\rV,Áâ›•D–ª^¬\Û3Z_]a½¦Ğ—$\ÇOşc•\ó*«±c$\È\ñ\Ê,;—G\ñ\ò\ô\Ïı”|šM2œ#™|dZ\ëÂ¹\ç\Ì?¨~\ì\Ì\Å+qWO/\Ç\rm\Õ\Çt•¤\ÚG¢e“°MtL\0K\Õ\ïÏ«\î\Ç];†\ÆDb\ô‹¢%\'Ÿ¼•˜pjEO\ë³x¢\ÎGH\ñ±Go%\ÆÇ„j\'€\õˆº‘x\ë\Ğ@f\ÜvŠ‰6n›”n\Şw\ÆÊ9¸k\õÌ\öŸ¶aÃ†\r6\â¿;\Ï.\'\nendstream\nendobj\n3 0 obj\n<</Type/XObject/Subtype/Image/Width 448/Height 83/SMask 2 0 R/Length 1610/ColorSpace[/CalRGB<</Matrix[0.34102 0.18997 0.04361 0.42093 0.73446 0.08085 0.18862 0.07562 0.96528]/WhitePoint[0.95057 1 1.09]>>]/BitsPerComponent 8/Filter/FlateDecode>>stream\nxœ\í\İ\íSW\Ç\ñ\ó\Õš>\ÌhmZG;­cmgl\Ç\ÖvJ[ \"Š XQªøT-Š¶*\á1›ı³\ì9Y\Â\î\Í&»K¿g>oŒ‰\ŞW¿9\÷\î½w_¾¤(Š¢(Š¢(Š¢(Š¢(Š¢(Š¢(—’w\Û\àÿ€ü\07\ä\'\0¸!?À\rù	\0n\ÈO\0)“m—mÒœ—¦œl¯Ê´¾\Éÿ\\¿ _\Ó/g\ë2\ò@:húÁ˜m“]\'d·\î“o\äø üø‡ä®¼¢\Ô\õ¯¾ì·¯\íî´Ÿûq\Z\é\ÈO\0) é·«SÚ®É¥Q},“Ó²0/\å¢<+™å’¬”_Y.­}^\ñ\ìkO2\öX†\Æ\ä\èy\É\ä,‡#\Zù	 Ñ´\Û\Ü\Öj\äß“\ò¼b\ñ\è£†giQ¼\r\é\ôkYOVË²\äI\ï°|\ğ›¥qc#?$×¼-`¸!¥j«¹qZnJÿ‘ÕŠŒO\ÈÁ\Ëäš‡G~H¨¦œ\õŠƒ#\ÖvjYcx®\Ó\Ùı\Ì\Ü\Ú\\¾¶’Ÿ\0’H§\ØûN[¯¨\óî¨’sN\ä\Õ/C5v¡\ä\'€\Ä\ÉT\Ãsb\ÊV;#OŸ¿‚zt –%?$‹vG\ÎIa6‚\ÏM#´¸(\Ç.8O\ä\ÉO\0	Òœ—»dz\ÆV)\ë\Z\ëù\Å9\Ğ#My‡Ñ’Ÿ\0’\Â?(tûaƒ\ÂÓ·R–\ã–\Û\áL~H\nGŸ¶-F\rO\ßrYZ.;,„’Ÿ\0A\ã\ë\Ø[\ó\ÜtW|\ä*­\ì=e\ÛMÃŒ™ü?>\ï9i§2—¼F‡§\ïy\ÅZ\ßwÂµ \ä\'€˜e«ËW\ï\Õe«g@\å¢\Ì\ÌÊ¾\îP¡\ä\'€˜5\å\åp\İw+mjµbg\Â\ìe\"?\ÄL[¾=¶¨\İ!M·´¸ Ÿ\õoA\ÉO\01\ÛY½\Ï\óÉ”]”sZ¶û\ñ\ß\ÎD~ˆŸF\è\İ\âŸ\Âû« \ö >\Ğ¡\ä\'€øer2p§§İƒ[)\ÉO—\í¢û\0\Ã&?\ÄO\ó*w5ù©=\ğ±‹§\ğ\ä\'€ø5\å\äP_\ÌÏ¼\ê\Ë\Å\Å\à\ÈO\0\ñ\ó_	79mGb\Ì\ÏrQ¦\n\ò\áÉ€\ïH\"?$‚N™‡\Æb\Â/y\ò`B\Ş\ë°<0f\ò@\"h~~wÑ®\òˆ1?—Kr\óû?¤L¶]\Ş\ï\ñ\'q\îb\Ò\î·\ëÏ€ß…ü\Z\\-—\ã<¯\ó\÷£\ìŸ>\Ù6[{¼Sº¶y¾+\à\æy!?$J&\'\ß\Äs\èrIn…Xü\ò@¢dÛ­½GºZ–\ë¡n¡\'?$\Ë\öœ|ı»mm\ävzı¿ü\É\ÑH5\ĞK£\r\İúÌ“‘G·Í¯#?$6{O\É\ì\\\ãn´\Ó\É{\ïp\ğK>\ò@i”¹Õ¸wqV<ù\ê\\\ğK>\ò@\í¬\îe\Z{ÜˆwÁ?+\Ù\ä=[}zf\ä\'€„\Òn\ğ‹³–o\å:\Ï\âu\òş\ë\Şÿ`K\ÑL;u³¾’t\æ>U°·\'‡|x$\ä\'€$k\Î\ËÁK¹ú\íeZ­Hß°C\ó)\ä\'€$³WÃ·\É\İG\õ\ÚN_ª.\ê³œ\Ãüh™VùyH^\Ô\çAüJY®\İsO!?$œ©\È\Í\ñ\èWAmM`A>?K~Ø²v\ä\í\í\'#\ĞÕŠØ%®#?¤€¶ˆûNË“©\ÈB—<™Ÿ—ıİ¡¼¿ü\Ûs\ò\ÉiyZˆ B+=6ú>\è{Šÿù	 5ü\õ³s5E¨&§ş¼\å’Û¥×‘Ÿ\0\Ò$“³\÷³ON;\ë\Ô\ğ\Ô¶]µ\Çú!Ok¾ü2\Ú7j„fCG¨†§\Î\Ü-<sµ‡§Ÿ\0\ÒH\'\òŸv‡{\"\ï¯yş0h\ñEx\nù	 ¥4B?\ê²\r\ò¾\Î%Ï\Z¬}\Í\óu\ä\'€´j\Ê\Ë\îN¹~_—7º£I{\ÔÂŒ9\öz\äM‘Ÿ\0R¬9o\÷&\õüU\İÿVŠ\êœ]?}d—DB~H;\ÍO\ËkB¿#ssk)Z*Z\Û95#\×\ì¨\ë	Í‘Ÿ\0¶mD·µ\ÚûşÛ²0o\Z§{NVŸ…¾\Ø3 \òÀÖ¡)ª\ó\ôg\äH¿\õœ;\Ü\ÏfA~\Øj4E\ë3aù	\0n\ÈO\0pC~€\ò\0ÜŸ\0\à†ü\07\ä\'\0¸!?À\rù	\0n\ÈO\0pC~€\ò\0ÜŸ\0\à†ü\07‘\ç\'EQEQEQEQEQTº\ê_kx“<\nendstream\nendobj\n7 0 obj\n<</Length 362/Filter/FlateDecode>>stream\nxœµ”\ßNƒ0\Æ\ïû\ç\ÎyÁ\Ö\Æİ–©\ÛF\'/P¡l5ĞR\Ş\×;\Ã[Xœ.L™q:{’¦’\ïü¾¶§%*\Ñ\è\æÀºB%8n\0\ØD\à˜\ÎwmH\n\ñbM`.\áµQ¢YŒ\ìAœ\"	\ÚÁ\èš\0±!\Î\Ğ\0.\ã\'\Ô\Êt\÷ÿ²\êø\Ã\ñ.‡\r\öx—\ãX[±—\ZRZQQeL\é¾g‚\õ> ˆÿ¦e-E&UA5—¢j!\ò©R)Õ¬V\'‚¼‹;û}€[Y@\"‹m\Î\ô\õ`C³¬¢° Où\á¡,g!ˆ:\Ï\Çp\Ä`U+\ÈYh2pÊ•™\Ùf} OŒM(\ä†,…iMÓ³ı\ö~\÷lKÁ5oŒ\çÏ\ÎP\Ù\Øv-L,<2	‰\ÚnŸ\Ú ’\æ\æºï³£q\'•f«ynR<Ã¶3\í\ÕZ5Y£˜Húx\Ç\Ç›8‡±\ó5\ğ³\Ê8Z®§·H\nM…-5\ÍCp1b\óE‡\à*6\Õ+o\Ï\nendstream\nendobj\n9 0 obj\n<</Type/Page/MediaBox[0 0 595 842]/Resources<</Font<</F1 4 0 R/F2 5 0 R/F3 6 0 R>>/XObject<</img0 2 0 R/img1 3 0 R>>/ExtGState<</GS1 1 0 R>>>>/Contents 7 0 R/Parent 8 0 R>>\nendobj\n4 0 obj\n<</Type/Font/Subtype/Type1/BaseFont/Helvetica/Encoding/WinAnsiEncoding>>\nendobj\n5 0 obj\n<</Type/Font/Subtype/Type1/BaseFont/Times-Bold/Encoding/WinAnsiEncoding>>\nendobj\n6 0 obj\n<</Type/Font/Subtype/Type1/BaseFont/Times-Roman/Encoding/WinAnsiEncoding>>\nendobj\n1 0 obj\n<</ca 0.5>>\nendobj\n8 0 obj\n<</Type/Pages/Count 1/Kids[9 0 R]>>\nendobj\n10 0 obj\n<</Type/Catalog/Pages 8 0 R>>\nendobj\n11 0 obj\n<</Producer(iText® 5.5.13.3 ©2000-2022 iText Group NV \\(AGPL-version\\))/CreationDate(D:20240105191625+01\'00\')/ModDate(D:20240105191625+01\'00\')>>\nendobj\nxref\n0 12\n0000000000 65535 f \n0000008331 00000 n \n0000000015 00000 n \n0000005560 00000 n \n0000008064 00000 n \n0000008152 00000 n \n0000008241 00000 n \n0000007447 00000 n \n0000008358 00000 n \n0000007876 00000 n \n0000008409 00000 n \n0000008455 00000 n \ntrailer\n<</Size 12/Root 10 0 R/Info 11 0 R/ID [<598c9dec2bf256939d117c74d7deb7d0><598c9dec2bf256939d117c74d7deb7d0>]>>\n%iText-5.5.13.3\nstartxref\n8616\n%%EOF\n',NULL,'8370921366003','SERVED','dfghjkl','WALLET_TO_WALLET',60,1,NULL);
/*!40000 ALTER TABLE `transfer_entity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wallet`
--

DROP TABLE IF EXISTS `wallet`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `wallet` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `account_number` varchar(255) DEFAULT NULL,
  `balance` double NOT NULL,
  `rib` varchar(255) DEFAULT NULL,
  `beneficiary_id` bigint DEFAULT NULL,
  `customer_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKixira4nkrvwxbh5auslcu672s` (`beneficiary_id`),
  KEY `FKpb5ltxtks766lq2b9hgvnr2bq` (`customer_id`),
  CONSTRAINT `FKixira4nkrvwxbh5auslcu672s` FOREIGN KEY (`beneficiary_id`) REFERENCES `beneficiary` (`id`),
  CONSTRAINT `FKpb5ltxtks766lq2b9hgvnr2bq` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wallet`
--

LOCK TABLES `wallet` WRITE;
/*!40000 ALTER TABLE `wallet` DISABLE KEYS */;
INSERT INTO `wallet` VALUES (1,'4567887654345678',99999600,'123456789012345678901234',NULL,1),(2,'4567997645565678',100000400,'123456789012345768967234',NULL,2);
/*!40000 ALTER TABLE `wallet` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-01-05 19:18:06
