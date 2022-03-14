use restaurant;


#insert status

INSERT INTO statuses ( `status_description`,`status_name`,`status_value`) VALUES
('Status của location table, trạng thái tầng, khu đêu ok','LOCATION_TABLE_STATUS','READY'),
('Status của location table, trạng thái tầng này không khả thi','LOCATION_TABLE_STATUS','EXPIRE'),
('Status của location table, trạng thái tầng này bị full','LOCATION_TABLE_STATUS','FULL'),
('Status của bàn, bày này ok, có thể tạo mới order ở bàn này','TABLE_STATUS','READY'),
('Status của bàn, bàn này đã được khách đặt hoặc đã có order chọn vào bàn này','TABLE_STATUS','BUSY'),
('Status của bàn, bàn này khách đã ngồi, order đã xong','TABLE_STATUS','ORDERED'),
('Status của Món ăn, món ăn này có thể chọn','DISH_STATUS','AVAILABLE'),
('Status của Món ăn, món ăn này không khả thi','DISH_STATUS','EXPIRE'),
('Status của Món ăn, món ăn này hết','DISH_STATUS','OVER'),
('Status của order, trạng thái đang order, chưa lưu, icon tay cầm ipad','ORDER_STATUS','ORDERING'),
('Status của order, trạng thái đã duyệt order','ORDER_STATUS','ORDERED'),
('Status của order, trạng thái đã order, bếp đang thực hiện, icon bếp lửa','ORDER_STATUS','PREPARATION'),
('Status của order, trạng thái giao hàng xong, khách đã nhận được đủ món, icon khách ngồi ăn','ORDER_STATUS','COMPLETED'),
('Status của order, trạng thái xác nhận chờ thanh toán, icon tờ bill','ORDER_STATUS','WAITING_FOR_PAYMENT'),
('Status của order, trạng thái xác nhận có thê thanh toán, icon tờ bill','ORDER_STATUS','ACCEPTED_PAYMENT'),
('Status của order, trạng thái thanh toán xong','ORDER_STATUS','DONE'),
('Status của order, trạng thái hủy','ORDER_STATUS','CANCELED'),
('Status của MÓN ĂN TRONG ORDER, món ăn này đã được order','ORDER_DISH_STATUS','ORDERED'),
('Status của MÓN ĂN TRONG ORDER, món ăn này đang được bếp xử lý','ORDER_DISH_STATUS','PREPARATION'),
('Status của MÓN ĂN TRONG ORDER, món ăn này đã giao cho khách','ORDER_DISH_STATUS','COMPLETED'),
('Status của MÓN ĂN TRONG ORDER, món ăn này bị hủy 1 phần','ORDER_DISH_STATUS','OK_CANCEL'),
('Status của MÓN ĂN TRONG ORDER, món ăn này bị hủy trên order','ORDER_DISH_STATUS','CANCELED'),
('Status của Option-topping, option này có thể chọn','OPTION_STATUS','AVAILABLE'),
('Status của Option-topping, option không khả thi','OPTION_STATUS','EXPIRE'),
('Status của Order Dish Option,option này khả thi','ORDER_DISH_OPTION_STATUS','DONE'),
('Status của Option-topping, option này hết','ORDER_DISH_OPTION_STATUS','CANCELED'),
('Status của category, category này có thể chọn','CATEGORY_STATUS','AVAILABLE'),
('Status của category-topping, category không khả thi','CATEGORY_STATUS','EXPIRE'),
('Status của Material, material này khả thi','MATERIAL_STATUS','AVAILABLE'),
('Status của Material, material này không khả thi','MATERIAL_STATUS','EXPIRE'),
('Status của Material, lượng còn lại của nvl này xuống đến mức thông báo','MATERIAL_STATUS','NOTIFICATION');


#Insert role
INSERT INTO role (`description`,`role_code`,`role_name`) VALUES
('Đây là vai trò của quản lý','ROLE_MANAGER','MANAGER'),
('Đây là vai trò của thu ngân','ROLE_CASHIER','CASHIER'),
('Đây là vai trò của đầu bếp','ROLE_CHEF','CHEF'),
('Đây là vai trò của bồi bàn','ROLE_ORDER_TAKER','ORDER_TAKER');


#Insert Staff

INSERT INTO staffs (`address`,`created_by`,`created_date`,`email`,`full_name`,`is_activated`,`password`,`staff_phone`,`staff_code`,`role_id`) VALUES
('Hà Tĩnh','kynhanht','2020-06-02 00:00:00','kynhanht@gmail.com','Nguyễn Thanh Kỳ Nhân',1,'$2a$10$kd7oZomJy.u/G4iD2/FaQeUestqwgBBfKrEqmRe9nna6RpG7IEKBe','0824917021','STAFF001',1),
('Hà Nội','kynhanht','2020-06-02 00:00:00','ducnv@gmail.com','Nguyễn Văn Đức',1,'$2a$10$kd7oZomJy.u/G4iD2/FaQeUestqwgBBfKrEqmRe9nna6RpG7IEKBe','0824917022','STAFF002',2),
('Hưng Yên','kynhanht','2020-06-02 00:00:00','ducbm@gmail.com','Bùi Minh Đức',1,'$2a$10$kd7oZomJy.u/G4iD2/FaQeUestqwgBBfKrEqmRe9nna6RpG7IEKBe','0824917023','STAFF003',3),
('Hà Nội','kynhanht','2020-06-02 00:00:00','ducpa@gmail.com','Phạm Anh Dũng',1,'$2y$12$Wa.zzHHiqt25glz7cNUr6.FsIzpWMCHKeAO9m66EoUuWYia8dJgze','a','STAFF004',4),
('Hà Nội','kynhanht','2020-06-02 00:00:00','dupa@gmail.com','Phạm Anh',1,'$2y$12$F161cN1ayQxB1VU4eqdo5Og.6qJ8cHcHC7Efr0HtbjHy5ItuNfApm','b','STAFF005',3);

#Insert LocationTable

INSERT INTO location_table (`location_code`,`location_name`,`status_id`) VALUES
('Floor', 'Tầng 1', 1),
('Floor', 'Tầng 2', 1),
('Floor', 'Tầng 3', 1),
('Floor', 'Tầng 4', 1),
('Floor', 'Tầng 5', 1),
('LOBBY', 'Hành Lang 1', 1),
('LOBBY', 'Hành lang 2', 1);


#Insert Table

INSERT INTO tables (`min_capacity`,`max_capacity`,`table_code`,`table_name`,`location_id`,`order_id`,`status_id`) VALUES
(2,5,'T1-BAN1','T1-Bàn 1',1,null,4),
(2,5,'T1-BAN2','T1-Bàn 2',1,null,4),
(2,7,'T1-BAN3','T1-Bàn 3',1,null,4),
(2,6,'T1-BAN4','T1-Bàn 4',1,null,4),
(2,5,'T2-BAN1','T2-Bàn 1',2,null,4),
(2,5,'T2-BAN2','T2-Bàn 2',2,null,4),
(2,5,'T2-BAN3','T2-Bàn 3',2,null,4),
(2,5,'T2-BAN4','T2-Bàn 4',2,null,4),
(2,4,'T3-BAN1','T3-Bàn 1',3,null,4),
(2,4,'T3-BAN2','T3-Bàn 2',3,null,4),
(2,4,'T3-BAN3','T3-Bàn 3',3,null,4),
(2,4,'T3-BAN4','T3-Bàn 4',3,null,4),
(2,7,'T4-BAN1','T4-Bàn 1',4,null,4),
(2,7,'T4-BAN2','T4-Bàn 2',4,null,4),
(2,7,'T4-BAN3','T4-Bàn 3',4,null,4),
(2,7,'T4-BAN4','T4-Bàn 4',4,null,4),
(2,3,'T5-BAN1','T5-Bàn 1',5,null,4),
(2,3,'T5-BAN2','T5-Bàn 2',5,null,4),
(2,3,'T5-BAN3','T5-Bàn 3',5,null,4),
(2,3,'T5-BAN4','T5-Bàn 4',5,null,4),
(2,3,'HL1-BAN1','HL1-Bàn 1',6,null,4),
(2,3,'HL1-BAN2','HL1-Bàn 2',6,null,4),
(2,3,'HL2-BAN1','HL2-Bàn 1',7,null,4),
(2,3,'HL2-BAN2','HL2-Bàn 2',7,null,4);

#Insert Category
INSERT INTO categories (`category_name`,`description`,`priority`,`status_id`) VALUES
('Ăn sáng','Đây là thực đơn món ăn sáng',2,27),
('Ăn trưa','Đây là thực đơn món ăn trưa',2,27),
('Ăn tối','Đây là thực đơn món ăn tối',2,27),
('Ăn nhanh','Đây là thực đơn món ăn nhanh',3,27),
('Đồ uống','Đây là nhóm thực đơn đồ uống',1,27),
('Tráng miệng','Đây là nhóm thực đơn món tráng miệng',1,27),
('Đặc biệt','Đây là món nhóm thực đơn đặc biệt',1,27),
('Hải sản','Đây là món nhóm thực đơn hải sản',3,27);


#Insert Dish
INSERT INTO dishes (`dish_code`,`dish_name`,`dish_unit`,`default_price`,`cost`,`dish_cost`,`description`,`time_complete`,`image_url`,`type_return`,`status_id`,`created_by`,`created_date`,`last_modified_by`,`last_modified_date`) VALUES
	# Ăn sáng 1
('PHO-BO','Phở Bò','Bát',40000,15000,30000,'Đây là món Phở Bò',0.15,null,0,7,'kynhanht','2020-07-26 00:00:00','kynhanht','2020-07-26 00:00:00'),
('PHO-GA','Phở Gà','Bát',40000,15000,30000,'Đây là món Phở Gà',0.15,null,0,7,'kynhanht','2020-07-26 00:00:00','kynhanht','2020-07-26 00:00:00'),
('PHO-CUA','Phở Cua','Bát',45000,15000,30000,'Đây là món Phở Cua',0.15,null,0,7,'kynhanht','2020-07-26 00:00:00','kynhanht','2020-07-26 00:00:00'),
('MY-BO','Mỳ Bò','Bát',30000,10000,20000,'Đây là món Mỳ Bò',0.1,null,0,7,'kynhanht','2020-07-26 00:00:00','kynhanht','2020-07-26 00:00:00'),
('MY-GA','Mỳ Gà','Bát',30000,10000,20000,'Đây là món Mỳ Gà',0.1,null,0,7,'kynhanht','2020-07-26 00:00:00','kynhanht','2020-07-26 00:00:00'),
('MY-CUA','Mỳ Bò','Bát',35000,15000,30000,'Đây là món Mỳ Cua',0.15,null,0,7,'kynhanht','2020-07-26 00:00:00','kynhanht','2020-07-26 00:00:00'),
	#Ăn trưa, Ăn Tối 2,3
('COM-BO','Cơm Bò','Bát',35000,15000,30000,'Đây là món Cơm Bò',0.2,null,0,7,'kynhanht','2020-07-26 00:00:00','kynhanht','2020-07-26 00:00:00'),
('COM-GA','Cơm Gà','Bát',35000,15000,30000,'Đây là món Cơm Gà',0.2,null,0,7,'kynhanht','2020-07-26 00:00:00','kynhanht','2020-07-26 00:00:00'),
('COM-THIT-HUN-KHOI','Cơm Thịt Hun Khói','Bát',40000,15000,30000,'Đây là món Cơm Thịt Hun Khói',0.2,null,0,7,'kynhanht','2020-07-26 00:00:00','kynhanht','2020-07-26 00:00:00'),
('COM-TAM','Cơm Tấm','Bát',50000,20000,40000,'Đây là món Cơm Tấm',0.2,null,0,7,'kynhanht','2020-07-26 00:00:00','kynhanht','2020-07-26 00:00:00'),
('COM-RANG-BO','Cơm Rang Bò','Dĩa',40000,12000,24000,'Đây là món Cơm Rang Bò',0.2,null,0,7,'kynhanht','2020-07-26 00:00:00','kynhanht','2020-07-26 00:00:00'),
('COM-RANG-GA','Cơm Rang Gà','Dĩa',40000,12000,24000,'Đây là món Cơm Rang Gà',0.2,null,0,7,'kynhanht','2020-07-26 00:00:00','kynhanht','2020-07-26 00:00:00'),
('COM-RANG-XUC-XICH','Cơm Rang Xúc Xích','Dĩa',35000,10000,20000,'Đây là món Cơm Rang Xúc Xích',0.2,null,0,7,'kynhanht','2020-07-26 00:00:00','kynhanht','2020-07-26 00:00:00'),
	#Ăn Nhanh 4
('KHOAI-LANG-CHIEN','Khoai Lang Chiên','Dĩa',15000,5000,10000,'Đây là món Khoai Lang Chiên',0.1,null,0,7,'kynhanht','2020-07-26 00:00:00','kynhanht','2020-07-26 00:00:00'),
('XUC-XICH-RAN','Xúc Xích Rán','Dĩa',15000,5000,10000,'Đây là món Xúc Xích Rán',0.1,null,0,7,'kynhanht','2020-07-26 00:00:00','kynhanht','2020-07-26 00:00:00'),
('XUC-XICH-LUOC','Xúc Xích Luộc','Dĩa',15000,5000,10000,'Đây là món Xúc Xích Luộc',0.1,null,0,7,'kynhanht','2020-07-26 00:00:00','kynhanht','2020-07-26 00:00:00'),
('GA-RAN','Gà Rán','Dĩa',20000,7500,15000,'Đây là món Gà Rán',0.15,null,0,7,'kynhanht','2020-07-26 00:00:00','kynhanht','2020-07-26 00:00:00'),
('NEM-RAN','Nem Rán','Dĩa',10000,3000,6000,'Đây là món Nem Rán',0.15,null,0,7,'kynhanht','2020-07-26 00:00:00','kynhanht','2020-07-26 00:00:00'),
('NGO-CHIEN','Ngô Chiến','Dĩa',20000,10000,5000,'Đây là món Ngô Chiến',0.15,null,0,7,'kynhanht','2020-07-26 00:00:00','kynhanht','2020-07-26 00:00:00'),
	#Đồ Uống 5
('CO-CA','COCA','Lon',15000,7000,10000,'Đây là đồ uống Coca',0,null,1,7,'kynhanht','2020-07-26 00:00:00','kynhanht','2020-07-26 00:00:00'),
('STING','Sting','Chai',15000,7000,10000,'Đây là đồ uống Sting',0,null,1,7,'kynhanht','2020-07-26 00:00:00','kynhanht','2020-07-26 00:00:00'),
('B0-HUC','Bò Húc','Lon',20000,9000,12000,'Đây là đồ uống Bò Húc',0,null,1,7,'kynhanht','2020-07-26 00:00:00','kynhanht','2020-07-26 00:00:00'),
('LA-VI','La Vi','Chai',7000,3000,5000,'Đây là đồ uống La Vi',0,null,1,7,'kynhanht','2020-07-26 00:00:00','kynhanht','2020-07-26 00:00:00'),
('NUOC-CAM','Nước Cam','Ly',25000,7000,14000,'Đây là đồ uống Nước Cam',0.15,null,0,7,'kynhanht','2020-07-26 00:00:00','kynhanht','2020-07-26 00:00:00'),
	#Tráng Miệng 6
('HOA-QUA-DAM','Hoa Quả Dầm','Cốc',15000,5000,10000,'Đây là Hoa Quả Dầm',0.15,null,1,7,'kynhanht','2020-07-26 00:00:00','kynhanht','2020-07-26 00:00:00'),
('CHE-BUOI','Chè Bưởi','Cốc',15000,5000,10000,'Đây là Chè Bưởi',0.15,null,1,7,'kynhanht','2020-07-26 00:00:00','kynhanht','2020-07-26 00:00:00'),
('CHE-THAP-CAM','Chè Thập Cẩm','Cốc',20000,7000,14000,'Đây là Chè Thập Cẩm',0.15,null,1,7,'kynhanht','2020-07-26 00:00:00','kynhanht','2020-07-26 00:00:00'),
('BANH-SOCOLA','Bánh Socola','dĩa',20000,7000,14000,'Đây là Bánh Socola',0.15,null,1,7,'kynhanht','2020-07-26 00:00:00','kynhanht','2020-07-26 00:00:00'),
('BANH-DAU','Bánh Dâu','dĩa',20000,7000,14000,'Đây là Bánh Dâu',0.15,null,1,7,'kynhanht','2020-07-26 00:00:00','kynhanht','2020-07-26 00:00:00'),
	#Đặc Biệt 7
('GA-QUAY','Gà Quay','con',200000,70000,140000,'Đây là món Gà Quay',0.3,null,1,7,'kynhanht','2020-07-26 00:00:00','kynhanht','2020-07-26 00:00:00'),
('VIT-QUAY','Heo Quay','con',150000,60000,120000,'Đây là món Heo Quay',0.3,null,1,7,'kynhanht','2020-07-26 00:00:00','kynhanht','2020-07-26 00:00:00'),
('BO-LUC-LAC','Bò Lúc Lắc','dĩa',150000,50000,100000,'Đây là món Bò Lúc Lắc',0.3,null,1,7,'kynhanht','2020-07-26 00:00:00','kynhanht','2020-07-26 00:00:00'),
	#Hải Sản 8
('CUA-HAP','Cua Hấp','con',250000,75000,150000,'Đây là món Cua Hấp',0.3,null,1,7,'kynhanht','2020-07-26 00:00:00','kynhanht','2020-07-26 00:00:00'),
('TOM-HUNG-HAP','Tôm Hùm Hấp','con',300000,75000,150000,'Đây là món Tôm Hùm Hấp',0.3,null,1,7,'kynhanht','2020-07-26 00:00:00','kynhanht','2020-07-26 00:00:00'),
('MUC-HAP','Mực Hấp','dĩa',300000,75000,150000,'Đây là món Mực Hấp',0.3,null,1,7,'kynhanht','2020-07-26 00:00:00','kynhanht','2020-07-26 00:00:00');
#insert Dish Category
INSERT INTO dish_category (`category_id`,`dish_id`) VALUES
(1,1),
(1,2),
(1,3),
(1,4),
(1,5),
(1,6),

(2,7),
(2,8),
(2,9),
(2,10),
(2,11),
(2,12),
(2,13),

(3,7),
(3,8),
(3,9),
(3,10),

(4,14),
(4,15),
(4,16),
(4,17),
(4,18),
(4,19),

(5,20),
(5,21),
(5,22),
(5,23),
(5,24),

(6,25),
(6,26),
(6,27),
(6,28),
(6,29),

(7,30),
(7,31),
(7,32),

(8,33),
(8,34),
(8,35);

#Insert Option
INSERT INTO options (`option_name`,`option_type`,`unit`,`price`,`cost`,`option_cost`,`status_id`) VALUES
('Thêm Bò','MONEY','dĩa',15000,5000,10000,23),
('Thêm Phở','MONEY','dĩa',5000,2000,4000,23),
('Thêm Gà','MONEY','dĩa',15000,5000,10000,23),
('Thêm Cua','MONEY','dĩa',15000,5000,10000,23),
('Rau Cải','ADD','dĩa',0,0,0,23),
('Nước Dùng','ADD','bát',0,0,0,23);

INSERT INTO dish_option (`dish_id`,`option_id`) VALUES
(1,1),
(1,2),
(1,6),
(2,2),
(2,3),
(2,6),
(3,2),
(3,4),
(3,6),
(4,1),
(4,5),
(5,3),
(5,5),
(6,4),
(6,5);

#Insert Warehouse
INSERT INTO warehouses (`name`) VALUES
('Kho đông lạnh'),
('Kho đồ khô'),
('Kho đồ uống'),
('Kho hải sản'),
('Kho rau củ quả'),
('Kho đồ tươi');

#Insert Supplier
INSERT INTO suppliers (`name`, `phone`) VALUES
('Nhà hàng Kỳ Nhân', 0337384888),
('Đơn vị Trung Văn', 0962580600),
('Chợ đầu mối', 0123456789);

#Insert Group Material
INSERT INTO group_material (`group_name`) VALUES
('Đồ đông lạnh'),
('Đồ gia vị'),
('Đồ hải sản'),
('Đồ gia súc'),
('Đồ đồ tươi'),
('Rau củ quả');

#Insert Material
INSERT INTO materials (`material_code`, `material_name`, `total_import`, `total_export`, `remain`, `remain_notification`, `unit`, `unit_price`, `total_price`, `group_id`, `status_id`) VALUES
('PHO', 'Phở', 100, 0, 100, 10, 'Kg', 1000, 100000, 1, 29),
('MY', 'Mỳ', 100, 0, 100, 10, 'Kg', 1000, 100000, 1, 29),
('THITBO', 'Thịt bò', 100, 0, 100, 10,'Kg', 150000, 15000000, 2, 29),
('THITLON', 'Thịt lợn', 100, 0, 100, 10, 'Kg', 120000, 12000000, 3, 29),
('RAUMUONG', 'Rau muống', 100, 0, 100, 10, 'Mớ', 3000, 300000, 4, 29),
('TRUNG', 'Trứng', 100, 0, 100, 10, 'Quả', 5000, 500000,5, 29);







