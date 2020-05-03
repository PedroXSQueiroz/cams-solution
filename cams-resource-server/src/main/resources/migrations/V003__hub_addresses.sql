ALTER TABLE server
	ADD hub_input_port INT NOT NULL ;

ALTER TABLE server
	ADD hub_output_port INT NOT NULL ;

ALTER TABLE client
	ADD hub_input_port INT NOT NULL ;
		
ALTER TABLE client
	ADD hub_output_port INT NOT NULL ;