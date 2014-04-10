SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

DROP SCHEMA IF EXISTS `MathBaseball` ;
CREATE SCHEMA IF NOT EXISTS `MathBaseball` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `MathBaseball` ;

-- -----------------------------------------------------
-- Table `MathBaseball`.`User`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `MathBaseball`.`User` ;

CREATE TABLE IF NOT EXISTS `MathBaseball`.`User` (
  `userID` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(45) NULL,
  `password` VARCHAR(45) NULL,
  `firstName` VARCHAR(45) NULL,
  `lastName` VARCHAR(45) NULL,
  `isAStudent` TINYINT(1) NULL,
  PRIMARY KEY (`userID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `MathBaseball`.`Student`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `MathBaseball`.`Student` ;

CREATE TABLE IF NOT EXISTS `MathBaseball`.`Student` (
  `studentID` INT NOT NULL,
  `teacherID` INT NOT NULL,
  INDEX `fk_Student_User1_idx` (`studentID` ASC),
  PRIMARY KEY (`studentID`),
  INDEX `fk_Student_User2_idx` (`teacherID` ASC),
  CONSTRAINT `fk_Student_User1`
    FOREIGN KEY (`studentID`)
    REFERENCES `MathBaseball`.`User` (`userID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Student_User2`
    FOREIGN KEY (`teacherID`)
    REFERENCES `MathBaseball`.`User` (`userID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `MathBaseball`.`Problems`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `MathBaseball`.`Problems` ;

CREATE TABLE IF NOT EXISTS `MathBaseball`.`Problems` (
  `questionID` INT NOT NULL AUTO_INCREMENT,
  `teacherID` INT NOT NULL,
  `firstNumber` INT NULL,
  `secondNumber` INT NULL,
  `answer` INT NULL,
  INDEX `fk_Problems_User1_idx` (`teacherID` ASC),
  PRIMARY KEY (`questionID`),
  CONSTRAINT `fk_Problems_User1`
    FOREIGN KEY (`teacherID`)
    REFERENCES `MathBaseball`.`User` (`userID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `MathBaseball`.`GameScore`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `MathBaseball`.`GameScore` ;

CREATE TABLE IF NOT EXISTS `MathBaseball`.`GameScore` (
  `gameID` INT NOT NULL AUTO_INCREMENT,
  `numberOfQuestions` INT NULL,
  `numberOfCorrectAnswers` INT NULL,
  PRIMARY KEY (`gameID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `MathBaseball`.`GameResult`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `MathBaseball`.`GameResult` ;

CREATE TABLE IF NOT EXISTS `MathBaseball`.`GameResult` (
  `gameID` INT NOT NULL,
  `studentID` INT NOT NULL,
  PRIMARY KEY (`gameID`),
  INDEX `fk_GameScore_has_Student_Student1_idx` (`studentID` ASC),
  INDEX `fk_GameScore_has_Student_GameScore1_idx` (`gameID` ASC),
  CONSTRAINT `fk_GameScore_has_Student_GameScore1`
    FOREIGN KEY (`gameID`)
    REFERENCES `MathBaseball`.`GameScore` (`gameID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_GameScore_has_Student_Student1`
    FOREIGN KEY (`studentID`)
    REFERENCES `MathBaseball`.`Student` (`studentID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
