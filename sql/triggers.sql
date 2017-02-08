CONNECT 'jdbc:derby://localhost:1527/ST4DB;create=true;user=admin;password=pass';

DROP TRIGGER inserted_order;
DROP TRIGGER close_order;

CREATE TRIGGER inserted_order
AFTER INSERT ON orders
REFERENCING NEW AS ins_order
FOR EACH ROW
UPDATE hotel SET state_id = 1 WHERE number=ins_order.room_number;


CREATE TRIGGER close_order
AFTER UPDATE OF order_state ON orders
REFERENCING NEW AS upd_order
FOR EACH ROW
UPDATE hotel SET state_id = 0 WHERE upd_order.order_state = 3
AND number = upd_order.ROOM_NUMBER;
