# christmas-grabbag

# Overview
Application that will generate a random pairing of people and send each of them an email with who they have in the grabbag.

# Technologies
Requires Java 9+ and Maven to run this application

# Customize Participants
To customize participants, update the participants.csv file found under resources folder

# Customize Exemptions
To customize exemptions, update the participant-pair-exemptions.csv file under resources folder

# Setup
To run the application, run the following command below:

```
mvn clean install
java -jar ./target/christmas-grabbag-1.0-SNAPSHOT.jar
```


