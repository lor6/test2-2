CREATE TABLE PRODUCT(
	ID INT AUTO_INCREMENT,
	NAME VARCHAR(255),
	QUANTITY INTEGER,
	PRICE DOUBLE,
	DESCRIPTION VARCHAR(1000),
);

INSERT INTO PRODUCT(NAME,QUANTITY,PRICE,DESCRIPTION) VALUES ('iPhone 11 Pro',10,300,'First triple camera system');