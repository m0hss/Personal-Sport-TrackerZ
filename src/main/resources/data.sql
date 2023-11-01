INSERT INTO public.user(
	fname, lname, email, age, role)
	VALUES
	('mika', 'Doe', 'klayton@mailbox.com', '18', 'USER'),
	('Jane', 'Smith', 'jane@cash.org', '30', 'COACH'),
	('walid', 'Johnson', 'alice.johnson@cash.time', '33', 'USER'),
	('sami', 'Williams', 'bob.williams@csc.com', '25', 'USER'),
	('Ella', 'Davis', 'davis@log.com', '27', 'COACH'),
	('mimoun', 'korza', 'mi.brown@me.io', '50', 'USER');

INSERT INTO public.course(
	name, localisation, date, type, places)
	VALUES 
	('FitX12', 'Bois de la Cambre', '2012-02-24', 'OUT', 36),
	('NPNGSeries10', 'Gims rue du tuyau 112 ', '2041-12-20', 'IN', 13),
	('FKillerX1000', 'BasicFit Avenue claire fontaine 22', '2023-02-22', 'IN', 22),
	('MDLSeriesX100', 'Place dugari', '2024-01-06', 'OUT', 41),
	('XXX3', 'BasicFit Boulevard lacastagne 118', '2023-11-09', 'IN', 12),
	('OrbitFitMAX100', 'Gims Avenue lamisère 92', '2023-11-12', 'IN', 10),
	('ChallengerX7', 'Parc Cinquentenaire', '2023-07-03', 'OUT', 30);