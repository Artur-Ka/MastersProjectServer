CREATE TABLE IF NOT EXISTS `Users` (
  `id` INT NOT NULL DEFAULT 0,
  `login` varchar(255) NOT NULL,
  `password` varchar(25) NOT NULL,
  `name` varchar(25) NOT NULL,
  `access_level` INT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


/*-- Рівень доступу: 0 - заблокований, 1 - користувач, 2 - адміністратор */
INSERT INTO Users VALUES
(1, 'admin', 'admin', 'Адміністратор', '2');