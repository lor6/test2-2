DROP TABLE IF EXISTS foo;

CREATE TABLE foo (
  id   INTEGER      NOT NULL AUTO_INCREMENT,
  title VARCHAR(250) NOT NULL,
  body VARCHAR(250),
  PRIMARY KEY (id)
);