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
    title VARCHAR(255),
    release_date VARCHAR(20),
    description TEXT,
    image_url VARCHAR(255),
    backdrop_image_url VARCHAR(255),
    tagline VARCHAR(255),
    score DOUBLE,
    budget BIGINT,
    runtime BIGINT,
    genres VARCHAR(255),
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES login_credentials(id)
);

CREATE TABLE user_tv_list (
    id INT NOT NULL AUTO_INCREMENT,
    user_id INT NOT NULL,
    tv_id INT NOT NULL,
    title VARCHAR(255),
    release_date VARCHAR(20),
    description TEXT,
    image_url VARCHAR(255),
    backdrop_image_url VARCHAR(255),
    tagline VARCHAR(255),
    score DOUBLE,
    budget BIGINT,
    runtime BIGINT,
    genres VARCHAR(255),
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES login_credentials(id)
);