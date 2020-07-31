import mysql.connector
from mysql.connector import Error
import argparse
parser = argparse.ArgumentParser()
parser.add_argument("-hn","--hostname", help = "give local or server hostname")
parser.add_argument("-d","--database_name", help = "givelocal database name")
parser.add_argument("-us","--username_local", help = "give local database username")
parser.add_argument("-ps","--password_local", help = "give local database password")
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

cursor.execute("select id from ec_condition where id = (select MAX(id) from ec_condition)") 
last_id_condition = cursor.fetchall()[0][0]

cursor.execute("select id from ec_drug where id = (select MAX(id) from ec_drug)") 
last_id_drug = cursor.fetchall()[0][0]

cursor.execute("select id from ec_measurement where id = (select MAX(id) from ec_measurement)") 
last_id_measurement = cursor.fetchall()[0][0]

cursor.execute("select id from ec_observation where id = (select MAX(id) from ec_observation)") 
last_id_observation = cursor.fetchall()[0][0]

cursor.execute("select id from ec_procedure where id = (select MAX(id) from ec_procedure)") 
last_id_procedure = cursor.fetchall()[0][0]



cursor.execute("select nctid from ec_condition") 
rows = cursor.fetchall()
condition_nctids = []
for r in rows:
    condition_nctids.append(r[0])

condition_new_nctids = []
for i in range(0,len(mappings)):
	nct=mappings[i][:-1].split("\t")
	condition_new_nctids.append(nct[0])


condition_final= list(set(condition_new_nctids)-set(condition_nctids))


cursor.execute("select nctid from ec_drug") 
rows = cursor.fetchall()
drug_nctids = []
for r in rows:
    drug_nctids.append(r[0])

drug_new_nctids = []
for i in range(0,len(mappings)):
	nct=mappings[i][:-1].split("\t")
	drug_new_nctids.append(nct[0])


drug_final= list(set(drug_new_nctids)-set(drug_nctids))

cursor.execute("select nctid from ec_observation") 
rows = cursor.fetchall()
observation_nctids = []
for r in rows:
    observation_nctids.append(r[0])

observation_new_nctids = []
for i in range(0,len(mappings)):
	nct=mappings[i][:-1].split("\t")
	observation_new_nctids.append(nct[0])


observation_final= list(set(observation_new_nctids)-set(observation_nctids))

cursor.execute("select nctid from ec_procedure") 
rows = cursor.fetchall()
procedure_nctids = []
for r in rows:
    procedure_nctids.append(r[0])

procedure_new_nctids = []
for i in range(0,len(mappings)):
	nct=mappings[i][:-1].split("\t")
	procedure_new_nctids.append(nct[0])


procedure_final= list(set(procedure_new_nctids)-set(procedure_nctids))

cursor.execute("select nctid from ec_measurement") 
rows = cursor.fetchall()
measurement_nctids = []
for r in rows:
    measurement_nctids.append(r[0])

measurement_new_nctids = []
for i in range(0,len(mappings)):
	nct=mappings[i][:-1].split("\t")
	measurement_new_nctids.append(nct[0])


measurement_final= list(set(measurement_new_nctids)-set(measurement_nctids))

for j in range(0,len(mappings)):
	x=mappings[j][:-1].split("\t")
	if x[5]=="Condition":
		x=x[:14]
		last_id_condition+=1
		out=[last_id_condition]
		if x[0] not in condition_final:
			last_id_condition-=1
			continue
		if x[11]=="unmapped":
			x[11]='0.0'

		out+=x
		data=tuple(out)
		print(data)
		cursor.execute("INSERT INTO ec_condition VALUES " + str(data))

	elif x[5]=="Drug":
		x=x[:14]
		last_id_drug+=1
		out=[last_id_drug]
		if x[0] not in drug_final:
			last_id_drug-=1
			continue
		if x[11]=="unmapped":
			x[11]='0.0'

		out+=x
		data=tuple(out)
		print(data)
		cursor.execute("INSERT INTO ec_drug VALUES " + str(data))

	elif x[5]=="Observation":
		x=x[:14]
		last_id_observation+=1
		out=[last_id_observation]
		if x[0] not in observation_final:
			last_id_observation-=1
			continue
		if x[11]=="unmapped":
			x[11]='0.0'

		out+=x
		data=tuple(out)
		print(data)
		cursor.execute("INSERT INTO ec_observation VALUES " + str(data))

	elif x[5]=="Procedure":
		x=x[:14]
		last_id_procedure+=1
		out=[last_id_procedure]
		if x[0] not in procedure_final:
			last_id_procedure-=1
			continue
		if x[11]=="unmapped":
			x[11]='0.0'

		out+=x
		data=tuple(out)
		print(data)
		cursor.execute("INSERT INTO ec_procedure VALUES " + str(data))

	elif x[5]=="Measurement":
		last_id_measurement+=1
		out=[last_id_measurement]
		if x[0] not in measurement_final:
			last_id_measurement-=1
			continue
		if x[11]=="unmapped":
			x[11]='0.0'
		if x[-1] == "-Infinity":
			x[-1] = "-99999"
		if x[-2] == "Infinity":
			x[-2] = "99999"
		x.extend([0,0,"default"])
		out+=x
		data=tuple(out)
		print(data)
		cursor.execute("INSERT INTO ec_measurement VALUES " + str(data))

	# connection.commit()

cursor.close()
connection.close()