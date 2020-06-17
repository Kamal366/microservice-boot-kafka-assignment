DROP TABLE IF EXISTS users;

create table users (
    id INT AUTO_INCREMENT  PRIMARY KEY,
    username varchar(50) not null,
    password varchar(120) not null
);

INSERT INTO users (username,password) VALUES (
   'admin','$2a$08$fL7u5xcvsZl78su29x1ti.dxI.9rYO8t0q5wk2ROJ.1cdR53bmaVG');
INSERT INTO users (username,password) VALUES (
   'john','$2a$08$fL7u5xcvsZl78su29x1ti.dxI.9rYO8t0q5wk2ROJ.1cdR53bmaVG');

  
 