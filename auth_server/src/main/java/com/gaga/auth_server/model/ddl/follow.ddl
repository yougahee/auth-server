CREATE TABLE `follow` (
  `follow_idx` bigint(20) NOT NULL,
  `user_follower_idx` bigint(20) NOT NULL,
  `user_streamer_idx` bigint(20) NOT NULL,
  `created_dt` datetime DEFAULT NULL,
  PRIMARY KEY (`follow_idx`),
  KEY `follewer_idx_idx` (`user_follower_idx`),
  KEY `following_idx_idx` (`user_streamer_idx`),
  CONSTRAINT `follewer_idx` FOREIGN KEY (`user_follower_idx`) REFERENCES `user` (`user_idx`),
  CONSTRAINT `following_idx` FOREIGN KEY (`user_streamer_idx`) REFERENCES `user` (`user_idx`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci