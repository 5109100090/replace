/*
SQLyog Enterprise - MySQL GUI v8.1 
MySQL - 5.5.27 : Database - replace
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

CREATE DATABASE /*!32312 IF NOT EXISTS*/`replace` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `replace`;

/*Table structure for table `place` */

DROP TABLE IF EXISTS `place`;

CREATE TABLE `place` (
  `placeId` int(11) NOT NULL AUTO_INCREMENT,
  `placeName` varchar(200) DEFAULT NULL,
  `placeDesc` text,
  `placeLat` float(15,11) DEFAULT NULL,
  `placeLng` float(15,11) DEFAULT NULL,
  `placeType` int(11) DEFAULT NULL,
  PRIMARY KEY (`placeId`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=latin1;

/*Data for the table `place` */

insert  into `place`(placeId,placeName,placeDesc,placeLat,placeLng,placeType) values (1,'Warung Tegal Keputih','Warung Tegal di Keputih',-7.29005908966,112.79872131348,1),(2,'Warung J-One','Warung J-One Perumdos',-7.28747797012,112.79456329346,1),(3,'Depot Muslim','Depot Muslim Keputih',-7.27988195419,112.78988647461,1),(4,'Sederhana','Rumah Makan Padang Sederhana',-7.28088998795,112.78117370605,1),(5,'Warung U7','Warung U7 Perumdos',-7.28379583359,112.79960632324,1),(6,'McDonald Mulyosari',NULL,-7.26670980453,112.79646301270,1),(7,'KFC Mulyosari',NULL,-7.26632022858,112.79634857178,1),(8,'Kantin Pusat ITS',NULL,-7.28388977051,112.79400634766,1),(9,'SAS Cafe & Resto',NULL,-7.27027988434,112.79856109619,1),(10,'Soto Cak Har',NULL,-7.28952598572,112.78244018555,1),(11,'Bintang Jaya Steak','Steak di Giant',-7.28999710083,112.78691101074,1),(12,'Warkit','Warung Kita',-7.28026485443,112.78950500488,1),(13,'Es Teler 77','Es Teler 77 di East Coast',-7.27659988403,112.80554962158,1),(14,'Wok','Chinese Food di East Coast',-7.27643013000,112.80555725098,1),(15,'AW','AW di East Coast',-7.27662992477,112.80561065674,1),(16,'Solaria','Solaria di East Coast',-7.27673006058,112.80561828613,1),(17,'Top Noodle','Top Noodle di East Coast',-7.27685022354,112.80561065674,1),(18,'Bunaken','Ikan Bakar',-7.26625394821,112.79576110840,1),(19,'Depot Pak Bas 2',NULL,-7.27445983887,112.80037689209,1),(20,'Kampoeng Steak','Kampoeng Steak Mulyosari',-7.26967287064,112.79673004150,1),(21,'Primarasa','Ayam Bakar',-7.28037691116,112.76850128174,1),(22,'Malioboro',NULL,-7.28002309799,112.77016448975,1),(23,'Pizza Hut Kertajaya','PizzaHut Kertajaya',-7.27962589264,112.76622772217,1),(24,'Pizza Hut Mulyosari','Pizza Hut Mulyosari',-7.26598787308,112.79617309570,1);

/*Table structure for table `review` */

DROP TABLE IF EXISTS `review`;

CREATE TABLE `review` (
  `reviewId` int(11) NOT NULL AUTO_INCREMENT,
  `reviewUser` int(11) DEFAULT NULL,
  `reviewPlace` int(11) DEFAULT NULL,
  `reviewPoint` decimal(10,0) DEFAULT NULL,
  PRIMARY KEY (`reviewId`)
) ENGINE=InnoDB AUTO_INCREMENT=175 DEFAULT CHARSET=latin1;

/*Data for the table `review` */

insert  into `review`(reviewId,reviewUser,reviewPlace,reviewPoint) values (1,6,1,'4'),(2,24,1,'3'),(3,21,1,'5'),(4,10,1,'2'),(5,3,1,'5'),(6,13,1,'2'),(7,8,1,'3'),(8,16,2,'5'),(9,9,2,'4'),(10,2,2,'4'),(11,6,2,'3'),(12,8,2,'5'),(13,11,2,'3'),(14,23,2,'2'),(15,20,2,'3'),(16,10,3,'3'),(17,11,3,'2'),(18,2,3,'2'),(19,3,3,'4'),(20,13,3,'2'),(21,9,3,'4'),(22,22,3,'1'),(23,14,3,'4'),(24,4,3,'3'),(25,17,3,'2'),(26,12,4,'1'),(27,1,4,'5'),(28,18,4,'3'),(29,8,4,'1'),(30,4,4,'5'),(31,23,4,'2'),(32,5,5,'2'),(33,9,5,'1'),(34,22,5,'3'),(35,1,5,'4'),(36,11,5,'5'),(37,15,5,'5'),(38,3,5,'4'),(39,24,6,'5'),(40,1,6,'3'),(41,2,6,'1'),(42,11,6,'2'),(43,10,6,'1'),(44,20,6,'5'),(45,8,6,'4'),(46,15,7,'5'),(47,5,7,'5'),(48,16,7,'5'),(49,24,7,'3'),(50,6,7,'2'),(51,1,7,'5'),(52,7,7,'3'),(53,25,7,'5'),(54,2,7,'1'),(55,9,8,'5'),(56,5,8,'3'),(57,24,8,'3'),(58,22,8,'5'),(59,3,8,'5'),(60,10,9,'1'),(61,6,9,'3'),(62,13,9,'2'),(63,8,9,'4'),(64,18,9,'2'),(65,6,10,'4'),(66,17,10,'2'),(67,5,10,'5'),(68,9,10,'3'),(69,10,10,'2'),(70,22,10,'2'),(71,18,10,'1'),(72,6,11,'4'),(73,18,11,'2'),(74,12,11,'4'),(75,15,11,'1'),(76,21,11,'3'),(77,23,11,'5'),(78,13,11,'5'),(79,1,11,'5'),(80,4,11,'3'),(81,14,12,'3'),(82,7,12,'5'),(83,11,12,'3'),(84,9,12,'5'),(85,25,12,'5'),(86,6,13,'4'),(87,10,13,'5'),(88,15,13,'1'),(89,8,13,'4'),(90,11,13,'1'),(91,25,13,'5'),(92,3,13,'5'),(93,7,14,'3'),(94,20,14,'4'),(95,18,14,'5'),(96,1,14,'4'),(97,22,14,'1'),(98,14,14,'1'),(99,4,14,'5'),(100,3,14,'3'),(101,19,14,'4'),(102,16,15,'1'),(103,14,15,'5'),(104,20,15,'2'),(105,24,15,'2'),(106,15,15,'1'),(107,1,15,'1'),(108,9,15,'2'),(109,12,15,'3'),(110,4,16,'5'),(111,6,16,'4'),(112,7,16,'3'),(113,9,16,'4'),(114,23,16,'5'),(115,18,16,'5'),(116,13,16,'3'),(117,16,16,'3'),(118,1,16,'5'),(119,19,17,'4'),(120,21,17,'5'),(121,20,17,'2'),(122,4,17,'2'),(123,9,17,'1'),(124,7,18,'2'),(125,9,18,'2'),(126,13,18,'4'),(127,15,18,'5'),(128,2,18,'3'),(129,18,18,'1'),(130,14,18,'1'),(131,4,19,'5'),(132,12,19,'2'),(133,5,19,'4'),(134,14,19,'5'),(135,9,19,'4'),(136,18,19,'5'),(137,11,19,'1'),(138,23,19,'4'),(139,16,20,'1'),(140,19,20,'3'),(141,18,20,'5'),(142,2,20,'3'),(143,14,20,'3'),(144,22,20,'1'),(145,11,20,'5'),(146,22,21,'1'),(147,6,21,'1'),(148,10,21,'2'),(149,21,21,'3'),(150,2,21,'2'),(151,12,21,'5'),(152,24,22,'5'),(153,8,22,'5'),(154,17,22,'4'),(155,20,22,'4'),(156,6,22,'3'),(157,25,22,'3'),(158,15,22,'4'),(159,4,22,'2'),(160,20,23,'5'),(161,25,23,'3'),(162,6,23,'2'),(163,11,23,'3'),(164,15,23,'1'),(165,3,24,'3'),(166,10,24,'5'),(167,22,24,'1'),(168,4,24,'3'),(169,14,24,'4'),(170,18,24,'3'),(171,12,24,'4'),(172,11,24,'5'),(173,20,24,'4'),(174,19,24,'4');

/*Table structure for table `type` */

DROP TABLE IF EXISTS `type`;

CREATE TABLE `type` (
  `typeId` int(11) NOT NULL AUTO_INCREMENT,
  `typeName` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`typeId`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

/*Data for the table `type` */

insert  into `type`(typeId,typeName) values (1,'Restaurant'),(2,'Hotel');

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `userId` int(11) NOT NULL AUTO_INCREMENT,
  `userName` varchar(200) DEFAULT NULL,
  `userAlias` varchar(200) DEFAULT NULL,
  `userPassword` varchar(32) DEFAULT NULL,
  `userFoods` text,
  `userDrinks` text,
  `userBooks` text,
  `userMovies` text,
  `userMusics` text,
  `userGender` char(1) DEFAULT NULL,
  `userOccupation` varchar(50) DEFAULT NULL,
  `userDOB` date DEFAULT NULL,
  PRIMARY KEY (`userId`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=latin1;

/*Data for the table `user` */

insert  into `user`(userId,userName,userAlias,userPassword,userFoods,userDrinks,userBooks,userMovies,userMusics,userGender,userOccupation,userDOB) values (1,'rizky','Rizky Noor Ichwan','46f94c8de14fb36680850768ff1b7f2a','Ayam Bakar,Rendang,Mie Aceh,Seafood,Tahu Telor,Nasi Padang','Es Jeruk,Teh Tawar,Es Campur','Fiksi,Fashion,Kesehatan,Musik,Gaya Hidup,Novel','Film-Noir,News,Western,Horror','Pop,Ambient,Country,Indie','L','Pegawai Kantor','1991-10-22'),(2,'aldo','Aldo Aditya Alase','46f94c8de14fb36680850768ff1b7f2a','Soto,Sate,Bubur Ayam,Tahu Telor','Es Jeruk,Jus Apel,Es Leci,Jus Jeruk','Kisah Nyata,Novel,Bisnis,Peternakan','History,Western,War,Talk-Show,Crime,Sport','Punk,60s,Blues,Folk','L','Pelukis','1986-10-20'),(3,'yoyok','Satro Gita Nugraha','46f94c8de14fb36680850768ff1b7f2a','Tahu Telor,Ayam Goreng,Seafood,Mie Ayam,Soto,Sate','Jus Alpukat,Es Teh,Es Leci,Jus Jeruk,Es Sirup','Novel,Manajemen,Fotografi,Desain,Komputer,Kisah Nyata','War,News,Romance,Biography,Sci-Fi,Comedy','World,Jazz,Country,Indie','L','Penyanyi','1985-03-18'),(4,'mumu','Muhammad','46f94c8de14fb36680850768ff1b7f2a','Nasi Pecel,Bubur Ayam,Mie Aceh,Bebek Goreng','Jus Alpukat,Es Leci,Es Sirup','Majalah,Fiksi,Gaya Hidup,Musik','Drama,Film-Noir,Sport','Blues,Country,Indie,World,Ambient','L','Pelukis','1988-07-08'),(5,'bayu','IGN Bayu Dita Prawira','46f94c8de14fb36680850768ff1b7f2a','Bakso,Steak,Sate,Nasi Pecel,Ayam Bakar','Es Leci,Teh Hangat Tawar,Es Jeruk,Jus Apel','Kesehatan,Jurnalisme,Non Profit,Majalah','Western,Sport,War,Mystery','80s,90s,70s','L','Penyanyi','1993-11-03'),(6,'erico','Emerson Ericdiansyah','46f94c8de14fb36680850768ff1b7f2a','Nasi Pecel,Steak,Rendang,Bebek Goreng','Jus Semangka,Es Sirup,Teh Hangat Tawar','Biografi,Fiksi,Komputer','Biography,Music,Talk-Show,Sci-Fi','Blues,Classical,70s,80s','L','Dokter','1995-06-17'),(7,'maja','AANB Trihatmaja','46f94c8de14fb36680850768ff1b7f2a','Bebek Goreng,Mie Ayam,Nasi Goreng,Rendang','Jus Alpukat,Es Teh,Jus Apel','Komputer,Non Profit,Kesehatan,Filsafat,Bisnis','Action,Film-Noir,Adventure,Mystery','Classical,Acoustic,Blues,Indie,Metal,70s,Hardcore','L','Dokter','1986-10-26'),(8,'anyeng','Rahajeng Ellysa','46f94c8de14fb36680850768ff1b7f2a','Rendang,Mie Aceh,Mie Ayam,Seafood','Teh Hangat Tawar,Jus Semangka,Jus Melon,Air Putih,Jeruk Hangat','Komputer,Manajemen,Non Fiksi,Non Profit,Peternakan','War,Drama,Horror,History','Blues,Hip Hop,Indie,Metal','P','Penyanyi','1990-09-09'),(9,'kevin','Kevin Discotanzo Pythagoras','46f94c8de14fb36680850768ff1b7f2a','Sate,Rendang,Nasi Padang','Air Putih,Jus Alpukat,Teh Tawar','Jurnalisme,Gaya Hidup,Fiksi,Peternakan,Novel','Sci-Fi,Adventure,Comedy','Pop,RNB,Acoustic,Soul','L','Pegawai Negri Sipil','1986-06-05'),(10,'wira','I Made Wirantara','46f94c8de14fb36680850768ff1b7f2a','Rendang,Soto,Bakso','Teh Hijau,Jeruk Hangat,Jus Jeruk,Air Putih','Filsafat,Manajemen,Peternakan','Documentary,Western,Music,News','70s,Metal,Pop Punk,Hip Hop,RNB','L','Penyanyi','1990-11-09'),(11,'nurul','Nurul Kusumaningsih','46f94c8de14fb36680850768ff1b7f2a','Mie Aceh,Tahu Telor,Soto,Bebek Goreng','Es Teh,Es Jeruk,Teh Hijau','Musik,Fotografi,Fashion,Jurnalisme','Western,Drama,Sport','Pop Punk,Emo,Ambient,Blues,Latin','P','Pelukis','1991-09-13'),(12,'nandes','Nandes Daras','46f94c8de14fb36680850768ff1b7f2a','Ayam Goreng,Ayam Bakar,Bakso,Nasi Pecel','Jus Jeruk,Teh Hangat Tawar,Teh Tawar,Es Sirup','Peternakan,Perkebunan,Kisah Nyata,Filsafat','Comedy,War,Mystery,Musical,News','Punk,60s,70s,Country,Latin','L','Pegawai Kantor','1989-07-11'),(13,'idham','Idham Mardy','46f94c8de14fb36680850768ff1b7f2a','Mie Aceh,Ayam Bakar,Seafood,Soto','Es Sirup,Teh Hangat,Es Leci,Jus Melon','Kesehatan,Novel,Biografi,Perkebunan,Komputer,Keluarga','Drama,Animation,Action','Jazz,Blues,90s,Pop,Punk','L','Pelajar','1985-09-10'),(14,'bowo','Dimas Prabowo','46f94c8de14fb36680850768ff1b7f2a','Mie Ayam,Rendang,Sate,Steak','Jus Melon,Teh Tawar,Es Sirup','Bisnis,Jurnalisme,Desain,Komputer,Manajemen,Ensiklopedi','Talk-Show,Documentary,Romance','Classical,Acoustic,90s','L','Pelajar','1986-05-03'),(15,'baihaqi','Baihaqi','46f94c8de14fb36680850768ff1b7f2a','Rendang,Ayam Bakar,Mie Aceh,Nasi Goreng','Es Teh,Jus Alpukat,Teh Hijau,Teh Tawar','Fiksi,Filsafat,Gaya Hidup,Kisah Nyata','War,History,News,Talk-Show','60s,World,Electronic,Punk,Jazz,Country,Classical','L','Wiraswasta','1992-07-15'),(16,'rama','Ramdhan Satyaputra','46f94c8de14fb36680850768ff1b7f2a','Ayam Bakar,Mie Ayam,Bebek Goreng,Bubur Ayam,Bakso,Nasi Goreng','Teh Hijau,Air Putih,Jus Apel,Es Campur,Teh Hangat Tawar,Teh Tawar','Perkebunan,Komputer,Fotografi,Jurnalisme,Keluarga','Western,Musical,Biography','Hip Hop,Soul,Acoustic,Blues','L','Dokter','1992-08-23'),(17,'ajeng','Ajeng','46f94c8de14fb36680850768ff1b7f2a','Ayam Bakar,Bebek Goreng,Mie Ayam,Seafood,Rendang,Tahu Telor','Es Campur,Es Teh,Teh Hangat Tawar,Jus Apel','Biografi,Perkebunan,Filsafat','Sport,Animation,Action,Musical,News','Jazz,Punk,World,Hip Hop,Pop Punk','P','Penyanyi','1990-10-19'),(18,'puspa','Puspa','46f94c8de14fb36680850768ff1b7f2a','Bebek Goreng,Rendang,Mie Ayam,Nasi Pecel,Bakso,Seafood','Es Jeruk,Air Putih,Es Sirup,Teh Hangat Tawar,Jus Jeruk','Kisah Nyata,Musik,Fotografi,Desain,Komputer','Horror,Talk-Show,FamilyFantasy,Drama,Comedy,Game-Show','Ambient,Reggae,Pop Punk,RNB,Country','P','Dokter','1995-02-13'),(19,'hepi','Happy','46f94c8de14fb36680850768ff1b7f2a','Nasi Padang,Seafood,Bakso','Jus Jeruk,Es Jeruk,Air Putih,Es Teh','Peternakan,Kisah Nyata,Biografi,Gaya Hidup,Jurnalisme','History,Animation,Western,Reality-TV','Rock,Reggae,Ambient,Indie','P','Pengajar','1993-07-09'),(20,'uyung','Bahrul Halimi','46f94c8de14fb36680850768ff1b7f2a','Mie Ayam,Bubur Ayam,Sate,Soto,Nasi Goreng','Es Teh,Jus Alpukat,Es Leci,Jus Melon,Jeruk Hangat,Jus Jeruk','Non Profit,Keluarga,Peternakan,Manajemen,Perkebunan,Kesehatan','Mystery,Action,Crime,History,FamilyFantasy,Game-Show','Ambient,70s,Blues,Classical,Jazz','L','Dokter','1985-02-10'),(21,'sam','Samid','46f94c8de14fb36680850768ff1b7f2a','Nasi Pecel,Nasi Goreng,Bebek Goreng,Rendang,Ayam Bakar','Jeruk Hangat,Jus Melon,Es Teh','Non Profit,Perkebunan,Ensiklopedi,Keluarga,Fiksi','Game-Show,Crime,Animation,Sci-Fi,Action','Pop,Ambient,Metal,Classical','L','Pelukis','1995-08-13'),(22,'romen','Rohman','46f94c8de14fb36680850768ff1b7f2a','Nasi Goreng,Sate,Steak,Bakso,Bubur Ayam,Mie Ayam','Es Jeruk,Teh Hangat,Jus Jeruk,Jus Semangka,Teh Tawar','Jurnalisme,Kisah Nyata,Desain,Fiksi','History,Game-Show,Sci-Fi,Documentary,Romance','Emo,RNB,Metal,Hardcore,Punk','L','Pegawai Kantor','1994-06-20'),(23,'harum','Harum','46f94c8de14fb36680850768ff1b7f2a','Soto,Ayam Goreng,Mie Aceh,Nasi Goreng,Nasi Padang','Teh Hijau,Teh Hangat,Teh Tawar,Es Leci','Komik,Fashion,Majalah,Kesehatan','Romance,Game-Show,Sci-Fi,Thriller,Comedy,War','Indie,Latin,Pop','L','Pegawai Negri Sipil','1987-07-06'),(24,'rangga','Dimas Rangga','46f94c8de14fb36680850768ff1b7f2a','Nasi Goreng,Ayam Bakar,Bakso,Mie Ayam,Steak','Teh Hangat,Es Jeruk,Teh Tawar,Jus Alpukat,Es Campur','Manajemen,Kisah Nyata,Fashion,Jurnalisme,Gaya Hidup','Sport,Drama,Reality-TV,War,Talk-Show','Pop,Hardcore,Metal,Blues','L','Dokter','1987-12-29'),(25,'vivi','Novita Nata','46f94c8de14fb36680850768ff1b7f2a','Rendang,Ayam Goreng,Tahu Telor','Es Teh,Teh Hangat Tawar,Es Campur,Es Leci,Jus Jeruk','Fiksi,Komik,Filsafat,Peternakan,Jurnalisme,Majalah','Comedy,Sport,Western,Mystery','Country,80s,Classical,60s,Latin','P','Dokter','1993-05-13');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
