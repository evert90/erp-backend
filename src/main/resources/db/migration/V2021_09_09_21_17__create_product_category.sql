CREATE TABLE product_category(
    id BIGINT(20) NOT NULL,
    name VARCHAR(250) NOT NULL,
    user_id BIGINT(20) NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    UNIQUE KEY `product_category_name_user_id` (`name`,`user_id`)
);
