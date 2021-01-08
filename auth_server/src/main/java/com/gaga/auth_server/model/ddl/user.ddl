CREATE TABLE `user` (
  `user_idx` bigint(20) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `nickname` varchar(10) DEFAULT NULL,
  `pw` varchar(100) DEFAULT NULL,
  `salt` varchar(100) DEFAULT NULL,
  `grade` tinyint(4) NOT NULL DEFAULT '0',
  `point` int(11) NOT NULL DEFAULT '0',
  `update_dt` datetime DEFAULT NULL,
  `login_dt` datetime DEFAULT NULL,
  `created_dt` datetime DEFAULT NULL,
  PRIMARY KEY (`user_idx`),
  UNIQUE KEY `user_idx_UNIQUE` (`user_idx`),
  UNIQUE KEY `email_UNIQUE` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci