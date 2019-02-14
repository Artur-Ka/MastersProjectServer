CREATE TABLE IF NOT EXISTS `Patients` (
  `id` INT NOT NULL DEFAULT 0,
  `name` varchar(255) NOT NULL,
  `dob` varchar(255) NOT NULL,
  `sex` varchar(255) NOT NULL,
  `phone` varchar(25) NOT NULL,
  `add_phone` varchar(25),
  `mail` varchar(25),
  `country` varchar(25) NOT NULL,
  `city` varchar(25) NOT NULL,
  `address` varchar(255) NOT NULL,
  `note` varchar(255),
  PRIMARY KEY (`id`),
) ENGINE=InnoDB DEFAULT CHARSET=utf8;