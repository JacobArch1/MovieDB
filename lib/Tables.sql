CREATE TABLE login_credentials (
    id INT NOT NULL AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(50) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE user_movie_list (
    id INT NOT NULL AUTO_INCREMENT,
    user_id INT NOT NULL,
    movie_id INT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES login_credentials(id)
);

CREATE TABLE user_tv_list (
    id INT NOT NULL AUTO_INCREMENT,
    user_id INT NOT NULL,
    tv_id INT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES login_credentials(id)
);
