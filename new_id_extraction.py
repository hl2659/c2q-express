import pandas as pd
import numpy as np
import datetime

import psycopg2
import argparse

parser = argparse.ArgumentParser()
parser.add_argument("-u","--username_aact", help = "give aact database username")
parser.add_argument("-p","--password_aact", help = "give aact database password")
parser.add_argument("-f","--from_date", help = "give start date for trials extraction")
parser.add_argument("-t","--to_date", help = "give end date for trials extraction")
parser.add_argument("-pa","--path", help = "Enter path where you want to save the txt file")
args = parser.parse_args()

connection1 = psycopg2.connect(
    user=args.username_aact,
    password=args.password_aact,
    database="aact",
    host="aact-db.ctti-clinicaltrials.org")

connection1.set_client_encoding('utf-8')

cursor1 = connection1.cursor()
x = args.from_date.split("-")
y = args.to_date.split("-")

from_date = datetime.date(int(x[0]),int(x[1]), int(x[2]))
till_date = datetime.date(int(y[0]),int(y[1]), int(y[2]))

cursor1.execute("Select nct_id from studies where study_first_submitted_date BETWEEN '" + str(from_date) + "' AND '" + str(till_date) + "'")
result = cursor1.fetchall()
nct_id_aact = []
for r in result:
    nct_id_aact.append(r[0])

with open(args.path, 'w') as f:
    for item in nct_id_aact:
        f.write('%s\n' % item)

cursor1.close()
connection1.close()