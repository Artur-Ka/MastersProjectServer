CREATE TABLE IF NOT EXISTS `Appointment_history` (
  `doctor_id` INT NOT NULL DEFAULT 0,
  `patient_id` INT NOT NULL DEFAULT 0,
  `date` varchar(255) NOT NULL,
  `complaint` varchar(255) NOT NULL,
  `doctor_advice` varchar(255) NOT NULL,
  PRIMARY KEY (`doctor_id`,`patient_id`),
) ENGINE=InnoDB DEFAULT CHARSET=utf8;