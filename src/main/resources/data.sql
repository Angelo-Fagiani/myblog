INSERT IGNORE INTO authority VALUE (1, 'ROLE_ADMIN');
INSERT IGNORE INTO authority VALUE (2, 'ROLE_EDITOR');
INSERT IGNORE INTO authority VALUE (3, 'ROLE_READER');
INSERT IGNORE INTO authority VALUE (4, 'ROLE_MODERATOR');
INSERT IGNORE INTO authority VALUE (5, 'ROLE_GUEST');

CREATE OR REPLACE VIEW visible_posts AS SELECT * FROM post WHERE published=true;