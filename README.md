# Criteria Parser
### Instruction 

### Get Started
- -fetch	fetch free-text eligibility criteria from clinicaltrials.gov
- -parse	parse free-text eligibility criteria
- -normalize	numberic and temporal information normalization
	- 	-entity	entity normlization(Concept Mapping)
	- 	-numeric	numberic information normalization
	- 	-temporal	temporal information normalization
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


In the pojo folder, update GlobalSettings.java to change the location of re.model (relex model) and rule_based_model_offline.ser.gz (rule_base_model) to your local directory.

1. nctids.txt contains the list of nctids which we use to fetch eligibility criteria (it is the input to criteria fetch command)
2. output_files contains 3 files in the format "nctids".txt which is the output of criteria fetch step
3. criteria.txt is the input file to the criteria parse step


