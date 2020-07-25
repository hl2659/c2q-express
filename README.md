# Criteria Parser
### Instruction 

### Get Started
- -fetch	fetch free-text eligibility criteria from clinicaltrials.gov
- -parse	parse free-text eligibility criteria
- -normalize	numberic and temporal information normalization
	- 	-entity	entity normlization(Concept Mapping)
	- 	-numeric	numberic information normalization
	- 	-temporal	temporal information normalization
	- -all	run the entire pipeline for normalization (numeric, temporal and entity)
	
- -complete	run the entire pipeline for criteria inforamtion extraction (fetch, parse and normalize)



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


