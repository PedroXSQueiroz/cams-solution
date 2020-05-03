ALTER TABLE server
	ADD messaging_broker_address NVARCHAR(256) NOT NULL;

ALTER TABLE server
	ADD messaging_broker_port INT NOT NULL ;

ALTER TABLE client
	ADD messaging_broker_address NVARCHAR(256) NOT NULL;
		
ALTER TABLE client
	ADD messaging_broker_port INT NOT NULL ;