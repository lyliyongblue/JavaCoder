CREATE DATABASE IF NOT EXISTS `dianpingdb` DEFAULT CHARSET utf8 COLLATE utf8_general_ci;

use dianpingdb;

create table user (
  id int NOT NULL AUTO_INCREMENT COMMENT '主键',
  telephone varchar(40) NOT NULL DEFAULT '' COMMENT '电话号码',
  password varchar(200) NOT NULL DEFAULT '' COMMENT '密码',
  nick_name varchar(40) NOT NULL DEFAULT '' COMMENT '昵称',
  gender tinyint NOT NULL DEFAULT 0 COMMENT '性别',
  created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at timestamp NOT NULL ON UPDATE CURRENT_TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`),
  UNIQUE `telephone_unique_index` USING BTREE (`telephone`) comment '电话号码唯一索引'
);