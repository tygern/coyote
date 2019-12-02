DROP DATABASE IF EXISTS coyote_dev;
DROP DATABASE IF EXISTS coyote_test;

CREATE DATABASE coyote_dev;
CREATE DATABASE coyote_test;

CREATE USER IF NOT EXISTS 'coyote'@'localhost' IDENTIFIED BY '';
GRANT ALL PRIVILEGES ON coyote_dev.* TO 'coyote' @'localhost';
GRANT ALL PRIVILEGES ON coyote_test.* TO 'coyote' @'localhost';
