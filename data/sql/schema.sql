SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

CREATE SCHEMA IF NOT EXISTS `tappxi` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `tappxi` ;

-- -----------------------------------------------------
-- Table `tappxi`.`user`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `tappxi`.`user` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(145) NOT NULL ,
  `email` VARCHAR(120) NOT NULL ,
  `balance` FLOAT NOT NULL ,
  `status` SMALLINT NOT NULL ,
  `role` SMALLINT NULL ,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `tappxi`.`session`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `tappxi`.`session` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `user_id` INT UNSIGNED NOT NULL ,
  `fb_token` VARCHAR(255) NOT NULL ,
  `token` VARCHAR(255) NOT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_sessions_users` (`user_id` ASC) ,
  CONSTRAINT `fk_sessions_users`
    FOREIGN KEY (`user_id` )
    REFERENCES `tappxi`.`user` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `tappxi`.`address`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `tappxi`.`address` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `street` VARCHAR(90) NOT NULL ,
  `settlement` VARCHAR(90) NOT NULL ,
  `city` VARCHAR(90) NOT NULL ,
  `state` VARCHAR(45) NOT NULL ,
  `zip_code` VARCHAR(5) NOT NULL ,
  `lat` FLOAT NOT NULL ,
  `long` FLOAT NOT NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `tappxi`.`stand`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `tappxi`.`stand` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `address_id` INT UNSIGNED NOT NULL ,
  `name` VARCHAR(60) NOT NULL ,
  `start_fare` FLOAT NOT NULL ,
  `status` SMALLINT NOT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_stand_address1` (`address_id` ASC) ,
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) ,
  CONSTRAINT `fk_stand_address1`
    FOREIGN KEY (`address_id` )
    REFERENCES `tappxi`.`address` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `tappxi`.`taxi`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `tappxi`.`taxi` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `stand_id` INT UNSIGNED NOT NULL ,
  `name` VARCHAR(45) NOT NULL ,
  `tag_number` VARCHAR(16) NOT NULL ,
  `status` SMALLINT NOT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_taxi_stand1` (`stand_id` ASC) ,
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) ,
  UNIQUE INDEX `tag_number_UNIQUE` (`tag_number` ASC) ,
  CONSTRAINT `fk_taxi_stand1`
    FOREIGN KEY (`stand_id` )
    REFERENCES `tappxi`.`stand` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `tappxi`.`request`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `tappxi`.`request` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `user_id` INT UNSIGNED NOT NULL ,
  `address_start_id` INT UNSIGNED NOT NULL ,
  `address_end_id` INT UNSIGNED NOT NULL ,
  `status` SMALLINT NOT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_request_users1` (`user_id` ASC) ,
  INDEX `fk_request_address1` (`address_start_id` ASC) ,
  INDEX `fk_request_address2` (`address_end_id` ASC) ,
  CONSTRAINT `fk_request_users1`
    FOREIGN KEY (`user_id` )
    REFERENCES `tappxi`.`user` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_request_address1`
    FOREIGN KEY (`address_start_id` )
    REFERENCES `tappxi`.`address` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_request_address2`
    FOREIGN KEY (`address_end_id` )
    REFERENCES `tappxi`.`address` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `tappxi`.`offer`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `tappxi`.`offer` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `request_id` INT UNSIGNED NOT NULL ,
  `stand_id` INT UNSIGNED NOT NULL ,
  `eta` INT NOT NULL ,
  `aproximate_fare` FLOAT NOT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_offers_request1` (`request_id` ASC) ,
  INDEX `fk_offers_stand1` (`stand_id` ASC) ,
  CONSTRAINT `fk_offers_request1`
    FOREIGN KEY (`request_id` )
    REFERENCES `tappxi`.`request` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_offers_stand1`
    FOREIGN KEY (`stand_id` )
    REFERENCES `tappxi`.`stand` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `tappxi`.`movement`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `tappxi`.`movement` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `user_id` INT UNSIGNED NOT NULL ,
  `amount` FLOAT NOT NULL ,
  `type` VARCHAR(45) NOT NULL ,
  `created_at` TIMESTAMP NOT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_movement_users1` (`user_id` ASC) ,
  CONSTRAINT `fk_movement_users1`
    FOREIGN KEY (`user_id` )
    REFERENCES `tappxi`.`user` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `tappxi`.`trip`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `tappxi`.`trip` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `request_id` INT UNSIGNED NOT NULL ,
  `offer_id` INT UNSIGNED NOT NULL ,
  `taxi_id` INT NOT NULL ,
  `movement_id` INT UNSIGNED NULL ,
  `fare` FLOAT NOT NULL ,
  `status` SMALLINT NOT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_trip_request1` (`request_id` ASC) ,
  INDEX `fk_trip_offers1` (`offer_id` ASC) ,
  INDEX `fk_trip_taxi1` (`taxi_id` ASC) ,
  INDEX `fk_trip_movement1` (`movement_id` ASC) ,
  CONSTRAINT `fk_trip_request1`
    FOREIGN KEY (`request_id` )
    REFERENCES `tappxi`.`request` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_trip_offers1`
    FOREIGN KEY (`offer_id` )
    REFERENCES `tappxi`.`offer` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_trip_taxi1`
    FOREIGN KEY (`taxi_id` )
    REFERENCES `tappxi`.`taxi` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_trip_movement1`
    FOREIGN KEY (`movement_id` )
    REFERENCES `tappxi`.`movement` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;



SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
