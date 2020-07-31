import pandas as pd
import numpy as np
import mysql.connector
from mysql.connector import Error

import argparse

parser = argparse.ArgumentParser()

parser.add_argument("-hn","--hostname", help = "give local or server hostname")
parser.add_argument("-d","--database_name", help = "givelocal database name")
parser.add_argument("-us","--username_local", help = "give local database username")
parser.add_argument("-ps","--password_local", help = "give local database password")
parser.add_argument("-ta","--table_name", help = "give name of your local table")
parser.add_argument("-p","--path",help = "path of mapping text file")
args = parser.parse_args()


connection = mysql.connector.connect(host=args.hostname,
                                      database=args.database_name, # put the name of your local database where you have the tables
                                      user=args.username_local,
                                      charset='utf8',
                                      password=args.password_local)

cursor = connection.cursor()

with open(args.path,'r',encoding='utf8') as f:
    mappings = f.readlines()


cursor.execute("select id from "+ args.table_name+ " where id = (select MAX(id) from "+args.table_name+")") 
last_id = cursor.fetchall()[0][0]

cursor.execute("select nctid from "+args.table_name) # name of your table 
rows = cursor.fetchall()
nct_id_present = []
for r in rows:
    nct_id_present.append(r[0])

new_nctids = []
for i in range(0,len(mappings)):
	nct=mappings[i][:-1].split("\t")
	new_nctids.append(nct[0])


final_nctids= list(set(new_nctids)-set(nct_id_present))

for j in range(0,len(mappings)):
	last_id+=1
	out=[last_id]
	x=mappings[j][:-1].split("\t")
	x=x[:15]
	if x[0] not in final_nctids:
		last_id-=1
		continue
	# print(x)
	if x[11]=="unmapped":
		x[11]='0.0'
	out+=x
	data=tuple(out)
	print(str(data))
	cursor.execute("INSERT INTO " +args.table_name+" (id,nctid,include,line_num,concept_id,concept_name,domain,neg,start_index,\
		end_index,temporal_source_text,entity_source_text,score,beforedays,afterdays,numeric_source_text)\
		VALUES " + str(data))
	connection.commit()


cursor.close()
connection.close()

