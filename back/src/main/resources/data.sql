SET GLOBAL max_allowed_packet = 1024*1024*50;
-- #############################################################################
INSERT IGNORE INTO model_genre VALUES (1, 'action');
INSERT IGNORE INTO model_genre VALUES (2, 'drama');
INSERT IGNORE INTO model_genre VALUES (3, 'comedy');
INSERT IGNORE INTO model_genre VALUES (4, 'thriller');
INSERT IGNORE INTO model_genre VALUES (5, 'mystery');
INSERT IGNORE INTO model_genre VALUES (6, 'sci-fi');
INSERT IGNORE INTO model_genre VALUES (7, 'horror');
INSERT IGNORE INTO model_genre VALUES (8, 'romance');
INSERT IGNORE INTO model_genre VALUES (9, 'cookbook');
INSERT IGNORE INTO model_genre VALUES (10, 'history');
INSERT IGNORE INTO model_genre VALUES (11, 'memoir');
INSERT IGNORE INTO model_genre VALUES (12, 'poetry');
-- ##############################################################################
INSERT IGNORE INTO model_roles VALUES(1, 'ROLE_READER');
INSERT IGNORE INTO model_roles VALUES(2, 'ROLE_BETA');
INSERT IGNORE INTO model_roles VALUES(3, 'ROLE_WRITER');
INSERT IGNORE INTO model_roles VALUES(4, 'ROLE_EDITOR');
INSERT IGNORE INTO model_roles VALUES(5, 'ROLE_MODERATOR');
INSERT IGNORE INTO model_roles VALUES(6, 'ROLE_LECTOR');
INSERT IGNORE INTO model_roles VALUES(7, 'ROLE_WRITERUNCOMPLETE');



INSERT IGNORE `model_user`
(`id`,`active`,`beta`,`city`,`country`,`email`,`first_name`,`last_name`,`password`,`username`)
VALUES
(1,True,False,'Novi Sad','Srbija','moderator@mailinator.com','moderator','moderator','$2a$10$l0Hd.Pyjxy8AuvQRAfsd9u/2tJRF/uR1PIJSRd5VDx..ONoluLJxG','moderator');

INSERT IGNORE INTO `model_user_roles`
(`user_id`,`role_id`)
VALUES
(1,5);


INSERT IGNORE `model_user`
(`id`,`active`,`beta`,`city`,`country`,`email`,`first_name`,`last_name`,`password`,`username`)
VALUES
(2,True,False,'Novi Sad','Srbija','editor1@mailinator.com','editor1','editor1','$2a$10$l0Hd.Pyjxy8AuvQRAfsd9u/2tJRF/uR1PIJSRd5VDx..ONoluLJxG','editor1');

INSERT IGNORE INTO `model_user_roles`
(`user_id`,`role_id`)
VALUES
(2,4);

INSERT IGNORE `model_user`
(`id`,`active`,`beta`,`city`,`country`,`email`,`first_name`,`last_name`,`password`,`username`)
VALUES
(3,True,False,'Novi Sad','Srbija','editor2@mailinator.com','editor2','editor2','$2a$10$l0Hd.Pyjxy8AuvQRAfsd9u/2tJRF/uR1PIJSRd5VDx..ONoluLJxG','editor2');

INSERT IGNORE INTO `model_user_roles`
(`user_id`,`role_id`)
VALUES
(3,4);


-- INSERT IGNORE `model_user`
-- (`id`,`active`,`beta`,`city`,`country`,`email`,`first_name`,`last_name`,`password`,`username`)
-- VALUES
-- (4,True,False,'Novi Sad','Srbija','editor3@mailinator.com','editor3','editor3','$2a$10$l0Hd.Pyjxy8AuvQRAfsd9u/2tJRF/uR1PIJSRd5VDx..ONoluLJxG','editor3');

-- INSERT IGNORE INTO `model_user_roles`
-- (`user_id`,`role_id`)
-- VALUES
-- (4,4);


-- INSERT IGNORE `model_user`
-- (`id`,`active`,`beta`,`city`,`country`,`email`,`first_name`,`last_name`,`password`,`username`)
-- VALUES
-- (5,True,False,'Novi Sad','Srbija','editor4@mailinator.com','editor4','editor4','$2a$10$l0Hd.Pyjxy8AuvQRAfsd9u/2tJRF/uR1PIJSRd5VDx..ONoluLJxG','editor4');

-- INSERT IGNORE INTO `model_user_roles`
-- (`user_id`,`role_id`)
-- VALUES
-- (5,4);


INSERT IGNORE `model_user`
(`id`,`active`,`beta`,`city`,`country`,`email`,`first_name`,`last_name`,`password`,`username`)
VALUES
(6,True,False,'Novi Sad','Srbija','lektor@mailinator.com','lektor','lektor','$2a$10$l0Hd.Pyjxy8AuvQRAfsd9u/2tJRF/uR1PIJSRd5VDx..ONoluLJxG','lektor');

INSERT IGNORE INTO `model_user_roles`
(`user_id`,`role_id`)
VALUES
(6,6);



-- INSERT IGNORE `model_user`
-- (`id`,`active`,`beta`,`city`,`country`,`email`,`first_name`,`last_name`,`password`,`username`)
-- VALUES
-- (7,True,False,'Novi Sad','Srbija','writer1@mailinator.com','writer1','writer1','$2a$10$l0Hd.Pyjxy8AuvQRAfsd9u/2tJRF/uR1PIJSRd5VDx..ONoluLJxG','writer1');

-- INSERT IGNORE INTO `model_user_roles`
-- (`user_id`,`role_id`)
-- VALUES
-- (7,3);

INSERT IGNORE `model_user`
(`id`,`active`,`beta`,`city`,`country`,`email`,`first_name`,`last_name`,`password`,`username`, `points`)
VALUES
(8,True,False,'Novi Sad','Srbija','reader3@mailinator.com','reader3','reader3','$2a$10$l0Hd.Pyjxy8AuvQRAfsd9u/2tJRF/uR1PIJSRd5VDx..ONoluLJxG','reader3', 0);

INSERT IGNORE INTO `model_user_roles`
(`user_id`,`role_id`)
VALUES
(8,1);


INSERT IGNORE INTO `model_user_roles`
(`user_id`,`role_id`)
VALUES
(8,2);

INSERT IGNORE `model_user`
(`id`,`active`,`beta`,`city`,`country`,`email`,`first_name`,`last_name`,`password`,`username`, `points`)
VALUES
(9,True,False,'Novi Sad','Srbija','reader2@mailinator.com','reader2','reader2','$2a$10$l0Hd.Pyjxy8AuvQRAfsd9u/2tJRF/uR1PIJSRd5VDx..ONoluLJxG','reader2', 0);

INSERT IGNORE INTO `model_user_roles`
(`user_id`,`role_id`)
VALUES
(9,1);

INSERT IGNORE INTO `model_user_roles`
(`user_id`,`role_id`)
VALUES
(9,2);
