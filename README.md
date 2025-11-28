# POC for En- and Decryption of sensitive data stored in the Keycloak Database

## Key Features
- Using JPA Attribute Converter for transparent En/Decryption on Attribute Level
- Simple Base64 String Converter for string DB types like VARCHAR etc
- Registering Converter Logic as a Keycloak Extension via _orm.xml_  Descriptor
- Testinfrastructure based on the Keycloak Test Framework for testing own Keycloak Extensions
- Example Configuration for the Keycloak UserEntity encrypting the follwing attributes
  - username
  - firstname
  - lastname
  - email
- Testcases to showcase the behavior of typical User Queries via the Keycloak REST-API for the user ressource

