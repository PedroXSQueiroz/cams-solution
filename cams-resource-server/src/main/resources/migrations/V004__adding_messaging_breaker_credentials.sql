ALTER TABLE server
	ADD messaging_broker_username NVARCHAR(256);
	
ALTER TABLE server
	ADD messaging_broker_password NVARCHAR(256);
	
ALTER TABLE client
	ADD messaging_broker_username NVARCHAR(256);
				
ALTER TABLE client
	ADD messaging_broker_password NVARCHAR(256);