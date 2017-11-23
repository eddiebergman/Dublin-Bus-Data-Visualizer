CREATE TABLE temp(time long ,lineId integer,direction boolean,busId varchar(8),timeFrame string,journeyId int,operator string, congestion boolean , longitude float ,latitude float ,delay integer,blockId integer,vehicleId integer,stopId integer ,atStop boolean);
.print temp made

CREATE TABLE temp2(busId varchar(3),direction boolean,journeyId int,stopId int);

CREATE TABLE mainTable(time long , busId varchar(3),direction boolean ,journeyId int, congestion boolean , longitude float ,latitude float ,delay int, stopId int ,atStop boolean);
.print mainTable created

CREATE TABLE stopPosTable(stopId int PRIMARY KEY,longitude float,latitude float);
.print stopPosTable created

CREATE TABLE stopsBusesTable(stopId int,busId varchar(3),direction boolean,time long);
.print stopsBusesTable created

CREATE TABLE stopInfoTable(stopId int,time long,busId varchar(3),direction boolean,delay int,journeyId int);
.print stopInfoTable created

CREATE TABLE busJourneyTable(busId varchar(3),journeyId int);
.print busJourneyTable created

CREATE TABLE busModelJourneys(busId varchar(3),direction boolean,journeyId int);
.print busModelJourneys created


.print importing csv files...

.mode csv
.import 20130101.csv temp
.print 20130101.csv imported

.import 20130102.csv temp
.print 20130102.csv imported

.import 20130103.csv temp
.print 20130103.csv imported

.import 20130104.csv temp
.print 20130104.csv imported

.import 20130105.csv temp
.print 20130105.csv imported

.import 20130106.csv temp
.print 20130106.csv imported

.import 20130107.csv temp
.print 20130107.csv imported

.import 20130108.csv temp
.print 20130108.csv imported

.import 20130109.csv temp
.print 20130109.csv imported

.import 20130110.csv temp
.print 20130110.csv imported

.import 20130111.csv temp
.print 20130111.csv imported

.import 20130112.csv temp
.print 20130112.csv imported

.import 20130113.csv temp
.print 20130113.csv imported

.import 20130114.csv temp
.print 20130114.csv imported

.import 20130115.csv temp
.print 20130115.csv imported

.import 20130116.csv temp
.print 20130116.csv imported

.import 20130117.csv temp
.print 20130117.csv imported

.import 20130118.csv temp
.print 20130118.csv imported

.import 20130119.csv temp
.print 20130119.csv imported

.import 20130120.csv temp
.print 20130120.csv imported

.import 20130121.csv temp
.print 20130121.csv imported

.import 20130122.csv temp
.print 20130122.csv imported

.import 20130123.csv temp
.print 20130123.csv imported

.import 20130124.csv temp
.print 20130124.csv imported

.import 20130125.csv temp
.print 20130125.csv imported

.import 20130126.csv temp
.print 20130126.csv imported

.import 20130127.csv temp
.print 20130127.csv imported

.import 20130128.csv temp
.print 20130128.csv imported

.import 20130129.csv temp
.print 20130129.csv imported

.import 20130130.csv temp
.print 20130130.csv imported

.import 20130131.csv temp
.print 20130131.csv imported


.print all imports done

.print creating maintable...
INSERT INTO mainTable(time,busId,direction,journeyId,congestion,longitude,latitude,delay,stopId,atStop)
SELECT time/1000,
(CASE WHEN busId LIKE '000%' THEN substr(busId,4,1) ELSE (CASE WHEN busId LIKE '00%' THEN substr(busId,3,2) ELSE substr(busId,2,3) END)END),
substr(busId,5,1),
journeyId,congestion,longitude,latitude,delay,stopId,atStop
FROM temp
ORDER BY time;
.print mainTable populated

.print getting rid of the "ulls"...
UPDATE mainTable
SET busId = '-'
WHERE busId = 'ull' OR busId = '';
.print update to maintable done

.print dropping temp table
DROP TABLE temp;
.print temp dropped

.print inserting into busModelJourneys...
INSERT INTO busModelJourneys(busId,direction,journeyId) SELECT busId,direction,journeyId FROM (SELECT busId,direction,journeyId,max(count) FROM (SELECT busId,journeyId,direction,count(stopId) AS count FROM (SELECT busId,direction,journeyId,stopId FROM mainTable WHERE time <= 1357084800000 GROUP BY busId,direction,journeyId,stopId) GROUP BY journeyId,busId,direction) GROUP BY busId,direction); 
.print busModelJourneys populated

.print dropping table2...
DROP TABLE temp2;
.print temp2 dropped

.print inserting into stopPosTable...
INSERT INTO stopPosTable(stopId,longitude,latitude)
SELECT stopId,longitude,latitude 
FROM mainTable
GROUP BY stopId
ORDER BY stopId;
.print stopPosTable populated

.print inserting into stopsBusesTable v2...
INSERT INTO stopsBusesTable(stopId,busId,direction,time)
SELECT mainTable.stopId,mainTable.busId,mainTable.direction,mainTable.time
FROM mainTable
INNER JOIN busModelJourneys
WHERE mainTable.journeyId = busModelJourneys.journeyId AND mainTable.busId = busModelJourneys.busId AND mainTable.direction = busModelJourneys.direction AND time <= 1357084800000
GROUP BY mainTable.stopId,mainTable.busId,mainTable.direction
ORDER BY time;
.print stopsBusesTable populated

.print inserting into busJourneyTable...
INSERT INTO busJourneyTable(busId,journeyId)
SELECT busId,journeyId
FROM mainTable
GROUP BY busId,journeyId
ORDER BY busId;
.print busJourneyTable populated

.print IjourneyId_mainTable...
CREATE INDEX IjourneyId_mainTable ON mainTable(journeyId);
.print IjourneyId done on mainTable

.print IbusId_stopsBusesTable...
CREATE INDEX IbusId_stopsBusesTable ON stopsBusesTable(busId);
.print IbusId done on stopsBusesTable

.print Itime_mainTable...
CREATE INDEX Itime_mainTable ON mainTable(time);
.print Itime_mainTable done on mainTable

.print IbusId_mainTable...
CREATE INDEX IbusId_mainTable ON mainTable(busId);
.print IbusId_mainTable done on mainTable

.print IbusId_time_mainTable...
CREATE INDEX IbusId_time_mainTable ON mainTable(busId , time);
.print IbusId_time_mainTable on mainTable

.print Istop_time_mainTable...
CREATE INDEX Istop_time_mainTable ON mainTable(stopId , time);
.print Istop_time_mainTable done on mainTable 

.print IjourneyId_time_mainTable...
CREATE INDEX IjourneyId_time_mainTable ON 
.print IjourneyId_time_mainTable...

.print vacuuming...
/*VACUUM;*/
.print done vacuuming

.print done