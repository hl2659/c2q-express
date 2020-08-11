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
- Criteria Fetching
> java -jar criteria_parser.jar -fetch -nctid_path /Users/cy2465/Documents/nctids.txt -result_dir /Users/cy2465/Documents/

- Criteria parsing
> java -jar criteria_parser.jar -parse -criteria_path /Users/cy2465/Documents/criteria.txt -result_dir /Users/cy2465/Documents/ -thread 2

- Entity Nomarlization
> java -jar criteria_parser.jar -normalize -entity -entity_path /Users/cy2465/Documents/testmapping.txt -usagi_path /Users/cy2465/Documents/git/Usagi/ -result_dir /Users/cy2465/Documents/test_mapping_results/ -thread 2


