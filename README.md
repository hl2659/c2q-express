
### Instruction 

### Get Started
Firstly, fetch the nctids from your required start date to required end date using new_id_extraction.py file using the necessary arguments. 
Example to fetch nctids shown in examples.

Secondly, comes the Criteria2Query process where we have 3 steps (fetch,parse and normalization)  

- -fetch	fetch free-text eligibility criteria from clinicaltrials.gov
- -parse	parse free-text eligibility criteria
- -normalize	numberic and temporal information normalization
	- 	-entity	entity normlization(Concept Mapping)
	- 	-numeric	numberic information normalization
	- 	-temporal	temporal information normalization
	- -all	run the entire pipeline for normalization (numeric, temporal and entity)

- -complete	run the entire pipeline for criteria inforamtion extraction (fetch, parse and normalize)

Thirdly, we need to insert new trials as per ec_all_criteria_table with the use of mapping text file. See instructions for further help
- -all	run the entire pipeline for criteria inforamtion extraction (Under testing)

### Examples
- Nct-id Fetching:
> python new_id_extraction.py --username_aact ____ --password_aact ____ --from_date ___ --to_date ____ --path ____
from_date and to_date to be given in YYYY-MM-DD format. path is where you want to save the text file output of nctids.

- Criteria Fetching
> java -jar criteria_parser.jar -fetch -nctid_path /Users/cy2465/Documents/nctids.txt -result_dir /Users/cy2465/Documents/
- Criteria parsing
> java -jar criteria_parser.jar -parse -criteria_path /Users/cy2465/Documents/criteria.txt -result_dir /Users/cy2465/Documents/ -thread 2
	
- Entity Nomarlization
> java -jar criteria_parser.jar -normalize -entity -entity_path /Users/cy2465/Documents/testmapping.txt -usagi_path /Users/cy2465/Documents/git/Usagi/ -result_dir /Users/cy2465/Documents/test_mapping_results/ -thread 2
- Normalization (all:numeric,temporal and entity)
> java -jar criteria_parser.jar -normalize -numeric -temporal -entity -value_range_path C:\Users\jaysh\OneDrive\Documents\RA\parsed_files\NCT04462783.txt -result_path C:\Users\jaysh\OneDrive\Documents\RA\test_mapping_results\combinedtest1.txt -temporal_path C:\Users\jaysh\OneDrive\Documents\RA\test_mapping_results\combinedtest1.txt -usagi_path C:\Users\jaysh\OneDrive\Documents\Usagi-1.1.5\ -result_dir C:\Users\jaysh\OneDrive\Documents\RA\combined_results\ -thread 2

To Run ALL the above steps Thorugh 1 command:
- Run The Complete pipeline of criteria fetching, parsing and normalization:
> java -jar criteria_parser.jar -complete -nctid_path
C:\Users\jaysh\OneDrive\Documents\RA\test_nctids.txt
-criteria_path
C:\Users\jaysh\OneDrive\Documents\RA\complete_steps\criteria.txt
-usagi_path
C:\Users\jaysh\OneDrive\Documents\Usagi-1.1.5\
-result_dir
C:\Users\jaysh\OneDrive\Documents\RA\complete_steps\
-result_dir2
C:\Users\jaysh\OneDrive\Documents\RA\complete_steps_parsed\
-thread
2
## Instructions
For the Complete Pipeline:
You need to make 2 folders (2 directories):
Directory 1: Eligibilty criterias will be fetched and stored in files in this directory (result_dir)
Directory 2: This directory will contain the parsed eligibility criterias and a final mapping file. (result_dir2)
The final mapping file then needs to be stored in the ec_all criteria table.

You can use nctids and test_nctids for testing the code. to fetch new trials. you need to run new_id_extraction.py file with the required arguments and
then run -complete and the other required args c2q code so as to obtain entity mappings and normalizations (final mapping file is to be used to update ec_all criteria table)
Also need to put umlsabbr.jar and Usagi1.1.5.jar in the lib folder of project

In the pojo folder, update GlobalSettings.java to change the location of re.model (relex model) and rule_based_model_offline.ser.gz (rule_base_model) to your local directory.

1. nctids.txt contains the list of nctids which we use to fetch eligibility criteria (it is the input to criteria fetch command)
2. output_files contains 3 files in the format "nctids".txt which is the output of criteria fetch step
3. criteria.txt is the input file to the criteria parse step

- Inserting New trials as per ec_all_criteria_table with the use of mapping text file
Use insertion_all_criteria.py to accomplish this task with the help of the following arguments
--hostname (give local or server hostname) If it is you local pc, this can be localhost
--database_name (name of database where the table is stored) e.g. ctkb
--username_local (the username you use) e.g. root
--password_local 
--table_name (in our case ec_all_criteria)
--path (the path where the mapping text file is stored)

Example command: python insertion_all_criteria.py --hostname localhost --database_name ctkb --username_local root -password_local #### --table_name ec_all_criteria --path C:\Users\jaysh\OneDrive\Documents\RA\complete_steps_parsed\mapping.txt

## Inserting into ec_* tables (procedure,observation,drug,measurement,condition)
Use ec_tables.py file with the following arguments:
1. -hn or  --hostname, help = "give local or server hostname"
2. -d or  --database_name , help = "give local database name"
 3. -us or --username_local , help = "give local database username"
4. -ps or --password_local , help = "give local database password"
5. -p or --path , help = "path of mapping text file"