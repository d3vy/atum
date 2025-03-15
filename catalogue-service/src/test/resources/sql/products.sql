INSERT INTO catalogue.products (id, title, description)
VALUES (1, 'Iphone 16 pro max', 'New iphone with titanium body'),
       (3, 'Cool keyboard with RGB lights', 'Redragon keyboard'),
       (2, 'trying to create a product from swagger', 'swagger-ui product (edited)')
ON CONFLICT (id) DO NOTHING;
