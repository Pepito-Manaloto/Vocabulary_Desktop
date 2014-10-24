CREATE SCHEMA my_vocabulary;

CREATE TABLE `foreign_language`
(
   `id` TINYINT UNSIGNED AUTO_INCREMENT,
   `language` VARCHAR(30) CHARACTER SET utf8mb4 NOT NULL,
   PRIMARY KEY(`id`)
) ENGINE=InnoDB AUTO_INCREMENT = 0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `vocabulary`
(
   `id` INT UNSIGNED AUTO_INCREMENT,
   `foreign_id` TINYINT UNSIGNED NOT NULL,
   `english_word` VARCHAR(70) CHARACTER SET utf8mb4 NOT NULL,
   `foreign_word` VARCHAR(100) CHARACTER SET utf8mb4 NOT NULL,
   `datein` DATETIME NOT NULL,
   `last_updated` DATETIME NOT NULL,
   PRIMARY KEY(`id`),
   KEY `english_word_index`(`english_word`, `id`),
   KEY `foreign_word_index`(`foreign_word`),
   UNIQUE KEY `english_foreign_unique`(`english_word`, `foreign_word`),
   FOREIGN KEY(`foreign_id`) REFERENCES `foreign_language`(`id`) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT = 0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

DELIMITER $$
CREATE PROCEDURE `Add_Vocabulary`(IN `pEnglish_word` VARCHAR(70), IN `pForeign_word` VARCHAR(100) CHARSET utf8mb4, IN `pForeign_language` TINYINT)
BEGIN

  INSERT INTO vocabulary(english_word, foreign_word, foreign_id, datein, last_updated) VALUES(pEnglish_word, pForeign_word, pForeign_language, NOW(), NOW());

END $$
DELIMITER;

DELIMITER $$
CREATE PROCEDURE `Delete_Vocabulary`(IN pEnglish_word VARCHAR(70), IN pForeign_language VARCHAR(30))
BEGIN

  DELETE vocabulary FROM vocabulary INNER JOIN foreign_language ON foreign_language.id = vocabulary.foreign_id WHERE english_word = pEnglish_word AND language = pForeign_language;

END $$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE  `Show_Vocabulary`(IN pForeign_language VARCHAR(30), IN letter CHAR(3), OUT total INT)
BEGIN

    IF (letter LIKE 'All') THEN
      SELECT COUNT(*) FROM vocabulary v, foreign_language f WHERE v.foreign_id = f.id AND f.language = pForeign_language INTO total;
      SELECT english_word, foreign_word FROM vocabulary v, foreign_language f WHERE v.foreign_id = f.id AND f.language = pForeign_language ORDER BY english_word ASC;
    ELSEIF (letter LIKE 'Rec') THEN
      SELECT COUNT(*) FROM vocabulary v, foreign_language f WHERE v.foreign_id = f.id AND f.language = pForeign_language INTO total;
      SELECT english_word, foreign_word FROM vocabulary v, foreign_language f WHERE v.foreign_id = f.id AND f.language = pForeign_language ORDER BY v.last_updated DESC;
    ELSE
      SELECT COUNT(*) FROM vocabulary v, foreign_language f WHERE v.foreign_id = f.id AND f.language = pForeign_language AND english_word LIKE CONCAT(letter, '%') INTO total;
      SELECT english_word, foreign_word FROM vocabulary v, foreign_language f WHERE v.foreign_id = f.id AND f.language = pForeign_language AND english_word LIKE CONCAT(letter, '%');
    END IF;

END $$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE `Update_Vocabulary`(IN pEnglish_word VARCHAR(70), IN pForeign_word VARCHAR(100) CHARSET utf8mb4, IN pForeign_language VARCHAR(30), IN word_language TINYINT)
BEGIN

  CASE word_language
    WHEN 0 THEN
      UPDATE vocabulary INNER JOIN foreign_language ON foreign_language.id = vocabulary.foreign_id SET english_word = pEnglish_word, last_updated = NOW() WHERE foreign_word = pForeign_word AND language = pForeign_language;
    WHEN 1 THEN
      UPDATE vocabulary INNER JOIN foreign_language ON foreign_language.id = vocabulary.foreign_id SET foreign_word = pForeign_word, last_updated = NOW() WHERE english_word = pEnglish_word AND language = pForeign_language;
    ELSE
      /* DO NOTHING */SELECT 1;
    END CASE;

END $$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE `Get_Vocabularies`(IN pLast_updated DATETIME, OUT recently_added_count INT)
BEGIN

    SELECT COUNT(*) FROM vocabulary WHERE last_updated > (pLast_updated  - INTERVAL 30 MINUTE) INTO recently_added_count;
    SELECT english_word, foreign_word FROM vocabulary v, foreign_language f WHERE v.foreign_id = f.id AND f.language = "Hokkien"  ORDER BY english_word ASC;
    SELECT english_word, foreign_word FROM vocabulary v, foreign_language f WHERE v.foreign_id = f.id AND f.language = "Japanese"  ORDER BY english_word ASC;
    SELECT english_word, foreign_word FROM vocabulary v, foreign_language f WHERE v.foreign_id = f.id AND f.language = "Mandarin"  ORDER BY english_word ASC;

END $$
DELIMITER ;