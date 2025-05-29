CREATE TABLE `User` (
  `user_id` varchar(30),
  `user_type` varchar(10),
  `user_pwd` varchar(20),
  `user_profile` text,
  `user_email` varchar(30),
  `user_score` int DEFAULT 0,
  `user_grade` varchar(10) DEFAULT '일반',
  `user_school` varchar(30),
  `registered_at` datetime NOT NULL,
  `user_wd` varchar(1) DEFAULT 'N',
  `user_wd_reason` text DEFAULT null,
  `user_wd_date` datetime DEFAULT null,
  PRIMARY KEY (`user_id`, `user_type`)
);

CREATE TABLE `Question` (
  `q_id` int PRIMARY KEY AUTO_INCREMENT,
  `q_title` varchar(50) NOT NULL,
  `q_content` text NOT NULL,
  `q_difficulty` varchar(10),
  `q_crt_cnt` int DEFAULT 0,
  `q_submits` int DEFAULT 0,
  `q_crt_per` double DEFAULT 0,
  `q_recommend` int DEFAULT 0,
  `user_id` varchar(30),
  `user_type` varchar(10),
  `created_at` datetime NOT NULL
);

CREATE TABLE `Q_Language` (
  `q_id` int,
  `q_language` varchar(20) NOT NULL,
  `q_answer` text NOT NULL
);

CREATE TABLE `Submissions` (
  `user_id` varchar(30) NOT NULL,
  `user_type` varchar(10) NOT NULL,
  `q_id` int NOT NULL,
  `s_language` varchar(20) NOT NULL,
  `s_code` text NOT NULL,
  `s_isCorrect` varchar(1),
  `s_runTime` int,
  `submitted_at` datetime NOT NULL
);

CREATE TABLE `Board` (
  `b_id` int PRIMARY KEY AUTO_INCREMENT,
  `b_type` varchar(20) NOT NULL,
  `b_title` varchar(50) NOT NULL,
  `b_content` text NOT NULL,
  `user_id` varchar(30) NOT NULL,
  `user_type` varchar(10) NOT NULL,
  `created_at` datetime NOT NULL,
  `b_comments` int DEFAULT 0,
  `b_views` int DEFAULT 0,
  `b_likes` int DEFAULT 0,
  `b_isPinned` varchar(5) DEFAULT 'N'
);

CREATE TABLE `BoardFiles` (
  `bf_id` int PRIMARY KEY AUTO_INCREMENT,
  `b_id` int,
  `bf_files` text NOT NULL
);

CREATE TABLE `BoardCmt` (
  `bc_id` int PRIMARY KEY AUTO_INCREMENT,
  `b_id` int NOT NULL,
  `bc_content` text NOT NULL,
  `answered_at` datetime NOT NULL,
  `user_id` varchar(30) NOT NULL,
  `user_type` varchar(10) NOT NULL,
  `bc_likes` int DEFAULT 0,
  `bc_ref` int,
  `bc_comment` int DEFAULT 0
);

CREATE TABLE `Notification` (
  `n_id` int PRIMARY KEY AUTO_INCREMENT,
  `n_type` varchar(10),
  `n_title` varchar(50) NOT NULL,
  `n_content` text NOT NULL,
  `n_url` text,
  `user_id` varchar(30) NOT NULL,
  `user_type` varchar(10) NOT NULL,
  `n_isRead` varchar(5) DEFAULT 'N',
  `created_at` datetime NOT NULL
);

CREATE TABLE `Report` (
  `r_id` int PRIMARY KEY AUTO_INCREMENT,
  `b_id` int NOT NULL,
  `bc_id` int,
  `r_content` varchar(30) NOT NULL,
  `r_detail` text,
  `user_id` varchar(30) NOT NULL,
  `user_type` varchar(10) NOT NULL,
  `reported_at` datetime NOT NULL,
  `r_isProc` varchar(5) DEFAULT 'N',
  `processed_at` datetime DEFAULT null
);

ALTER TABLE `Question` ADD FOREIGN KEY (`user_id`, `user_type`) REFERENCES `User` (`user_id`, `user_type`) ON UPDATE CASCADE;

ALTER TABLE `Q_Language` ADD FOREIGN KEY (`q_id`) REFERENCES `Question` (`q_id`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `Submissions` ADD FOREIGN KEY (`user_id`, `user_type`) REFERENCES `User` (`user_id`, `user_type`) ON UPDATE CASCADE;

ALTER TABLE `Submissions` ADD FOREIGN KEY (`q_id`) REFERENCES `Question` (`q_id`) ON UPDATE CASCADE;

ALTER TABLE `Board` ADD FOREIGN KEY (`user_id`, `user_type`) REFERENCES `User` (`user_id`, `user_type`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `BoardFiles` ADD FOREIGN KEY (`b_id`) REFERENCES `Board` (`b_id`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `BoardCmt` ADD FOREIGN KEY (`b_id`) REFERENCES `Board` (`b_id`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `BoardCmt` ADD FOREIGN KEY (`user_id`, `user_type`) REFERENCES `User` (`user_id`, `user_type`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `Notification` ADD FOREIGN KEY (`user_id`, `user_type`) REFERENCES `User` (`user_id`, `user_type`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `Report` ADD FOREIGN KEY (`b_id`) REFERENCES `Board` (`b_id`) ON UPDATE CASCADE;

ALTER TABLE `Report` ADD FOREIGN KEY (`bc_id`) REFERENCES `BoardCmt` (`bc_id`) ON UPDATE CASCADE;

ALTER TABLE `Report` ADD FOREIGN KEY (`user_id`, `user_type`) REFERENCES `User` (`user_id`, `user_type`) ON UPDATE CASCADE;
