# POC for En- and Decryption of sensitive data stored in the Keycloak Database

This Proof Of Concept has the goal to evaluate a transparent data encryption on keycloak level for the persisted data.
We are evaluating the usage of JPA Attribute Converter integrated as a Keycloak Extension for de- and encrypting data on
a attribute level.

## Key Features
- Using JPA Attribute Converter for transparent En/Decryption on Attribute Level
- Simple AES256 String Converter for string DB types like VARCHAR etc
- Registering Converter Logic as a Keycloak Extension via _orm.xml_  Descriptor
- Testinfrastructure based on the Keycloak Test Framework for testing own Keycloak Extensions
- Example Configuration for the Keycloak UserEntity encrypting the follwing attributes
  - username
  - firstname
  - lastname
  - email
- Testcases to showcase the behavior of typical User Queries via the Keycloak REST-API for the user ressource
- Added and integrated a copy of the original keycloak base testsuite (new quarkus based testsuite) to have a better measurement of the impact of our extension 

## Building and Running the tests

Building keycloak extension and running the integration tests:

    $ ./mvn clean install

Running the keycloak testsuite against our extension:

    $ ./mvn clean install -Pkeycloak-testsuite

## Layout

```
.
├── LICENSE
├── README.md
├── keycloak_encrypt_poc_attribute_converter
├── keycloak_encrypt_poc_attribute_converter_test
├── pom.xml
├── run_postgres.sh
└── tests

```

This Maven Multimodule project consists of 3 different modules:
### keycloak_encrypt_poc_attribute_converter

- Simple AES256 Converter Logic Implementation
- orm.xml Deploymentdescriptor for Converter Registration (Keycloaks UserEntity for now)
- normal JAR packaging to be installed as a keycloak extension

### keycloak_encrypt_poc_attribute_converter_test

- Testmodule for integration tests of our extension and a real keycloak
- Based on the new Keycloak Testframework (Quarkus based tests instead of the old Arquillian bases Testsuite)
- Starts a keycloak with our extension installed
- Tests for triggering Keycloak Client ADMIN Rest API for creating and manipulating Keycloak Users

### tests

- A Copy of the current (Keycloak V26.3.4) Testsuite (New Testsuite/Quarkus Based Tests)
- Added Prefix 'de.spree.' to the original groupId to avoid conflicts
- Configuration to use our own Keycloak Extension
- Adapting some Tests
    - Failed Test which do not work with Data Encryption (Queries with lower() and like operators, Sorting via order by) are DISABLED
    - Changed some Test preparations to use exact queries instead of "fuzzy" ones to get them working for our usecase
- clustering Tests are excluded for now (hard dependencies to modules in the keycloak source tree are not easily to be fixed)
- Unfortunately this test suite seems to be flaky from time to time (independent from our extension). For this reason the testsuite will not run automatically but need to be triggerd via a maven profile
