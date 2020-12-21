-- 初始化用户表
INSERT INTO sys_user (id,create_by,create_date,del_flag,update_by,update_date,name,salt_password,user_name,dept_code)VALUES(1,1,NOW(),0,1,NOW(),'超级管理员','f0df7414507bcb57e07e18555821228a','admin','11111111');
alter table sys_user AUTO_INCREMENT=100;
-- 初始化角色表
INSERT INTO sys_role (id,create_by,create_date,del_flag,update_by,update_date,code,code_en,code_zh,dtl,name) VALUES (1,1,NOW(),0,1,NOW(),'admin','admin','管理员','超级管理具有所有权限，不能修改', '超级管理员');
INSERT INTO sys_role (id,create_by,create_date,del_flag,update_by,update_date,code,code_en,code_zh,dtl,name) VALUES (2,1,NOW(),0,1,NOW(),'miniprogram','miniprogram','微信小程序用户','微信小程序用户','微信小程序用户');
-- 初始化用户角色表
INSERT INTO sys_user_role(id,create_by,create_date,del_flag,update_by,update_date,roleId,userId) VALUES (1,1,NOW(),0,1,NOW(),1,1);

-- 初始化组织架构表

-- 初始化组织架构表
INSERT INTO sys_dept(create_by, create_date, del_flag, remarks, sort, update_by, update_date, child_ids, code, dtl, email, flag, leader, loc, name, parent_id, phone, path, level, color, ops_server) VALUES ('1', '2020-08-30 03:01:46', '0', NULL, NULL, '1', '2020-08-30 03:01:46', NULL, '10', NULL, NULL, '0', NULL, NULL, '管理系统', '0', NULL, '/管理系统', '1', '#000000', NULL);
INSERT INTO sys_dept(create_by, create_date, del_flag, remarks, sort, update_by, update_date, child_ids, code, dtl, email, flag, leader, loc, name, parent_id, phone, path, level, color, ops_server) VALUES ('1', '2020-08-30 03:01:46', '0', NULL, NULL, '1', '2020-08-30 03:01:46', NULL, '1010', NULL, NULL, '0', NULL, NULL, '微信', '1', NULL, '/管理系统/微信', '2', '#000000', NULL);
INSERT INTO sys_dept(create_by, create_date, del_flag, remarks, sort, update_by, update_date, child_ids, code, dtl, email, flag, leader, loc, name, parent_id, phone, path, level, color, ops_server) VALUES ('1', '2020-08-30 03:01:46', '0', NULL, NULL, '1', '2020-08-30 03:01:46', NULL, '101010', NULL, NULL, '0', NULL, NULL, '小程序', '2', NULL, '/管理系统/微信/小程序', '2', '#000000', NULL);
