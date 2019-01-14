# Dublin-Bus-Data-Visualizer

My first year programming project that was made along with 3 others

#### No database is included so it won't work straight away 
A static database generated from 3gb of csv files found on https://data.gov.ie/dataset/dublin-bus-gps-sample-data-from-dublin-city-council-insight-project. Use the associated makeDb.sql script found in data/Database on these csv's to generate the database (may take ten years to index the tables...)
#### Update:
Now includes a bash script to download and unzip the files, use you favourite cmd line SQL interface (we used Sqlite3) to then run the makeDb.sql script
```
sqlite3 testDatabase.db < makeDb.sql
```
