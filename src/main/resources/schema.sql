CREATE TABLE Users (
	username varchar(20),
	password varchar(200),
	enabled boolean
	
);

CREATE TABLE Authorities(
	username varchar(20),
	authority varchar(20)
);
