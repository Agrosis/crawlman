
CREATE TABLE `pages` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `link` text NOT NULL,
  `title` text NOT NULL,
  `content_digest` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 AUTO_INCREMENT=1;
