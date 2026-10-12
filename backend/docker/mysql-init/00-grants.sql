-- Give the lodwar user access to any lodwar_* schema (dev, test, etc.).
GRANT ALL PRIVILEGES ON `lodwar_%`.* TO 'lodwar'@'%';
GRANT ALL PRIVILEGES ON `lodwar`.* TO 'lodwar'@'%';
FLUSH PRIVILEGES;
