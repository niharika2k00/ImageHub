CREATE DATABASE  IF NOT EXISTS `imagehub`;
USE `imagehub`;

DROP TABLE IF EXISTS <table_name>;
SHOW COLUMNS FROM <table_name>;
DESCRIBE <table_name>;

-- create new table
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name`varchar(45) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `age` varchar(45) DEFAULT NULL,
  `location` varchar(45) DEFAULT NULL,
  `bio` varchar(45) DEFAULT NULL,
  `phone_number` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;


CREATE TABLE `role` (
  `id` int NOT NULL AUTO_INCREMENT,
  `role_name`varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;


-- delete cascade: to delete the rows from the child table automatically, when the rows from the parent table are deleted
CREATE TABLE `user_role` (
    `user_id` int NOT NULL,
    `role_id` int NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES role(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- LONGBLOB: for binary large object storage eg byte array
CREATE TABLE `image` (
  `id` int NOT NULL PRIMARY KEY AUTO_INCREMENT,
  `title`varchar(100) DEFAULT NULL,
  `description` text DEFAULT NULL,
  `category` varchar(100) DEFAULT NULL,
  `author_id` int DEFAULT NULL,
  `uploaded_at` varchar(100) DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;


CREATE TABLE `image_variant` (
  `image_id` int NOT NULL,
  `width` int NOT NULL,
  `height` int NOT NULL,
  `file_path` varchar(255) NOT NULL,
  PRIMARY KEY (image_id, width, height),
  FOREIGN KEY (image_id) REFERENCES image(id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;
