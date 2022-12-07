# CS7IS3-Assignment-2
## Group 7

## Members
Dominique Meudec - 18327666

## Description

text


## Usage
To build the project, you can use the following maven command
with the project directory:
```bash
mvn package
```

Upon success of the build, a jar will be built. To run the resulting
program, run the following command:
```bash
java -jar target/Assignment2-1.0.jar
```

## Result (trec_eval)
To return the Mean Average Precision (MAP) score, run the following command:
```bash
trec_eval/trec_eval -m map QRelsPath data/answer.test
```
To return the Recall score, run the following command:
```bash
trec_eval-9.0.7/trec_eval -m recall QRelsPath data/answer.test
```
