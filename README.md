# ESF JIRA Automations
Jira Automation codes to make ticket creation in development flow easy

---
### Features Available
- Retailer On-Boarding Request related Sub-Tasks Creation
---
### Steps to execute the JAR
- Download the latest release file
- Move the jar to your desired location
- Create a folder name resources and inside that create a file name config.properties
- Now open the config.properties file and paste the below values.
  ``base64AuthKey=PASTE_YOUR_VALID_BASE64_ENCODED_KEY``
- Now move to the location where you have placed the jar.
- Open a terminal at that location and use the below command to execute the jar.
  ``java -jar jira-esf-automations.jar``
- This will provide you with the list of all options available.
---
### How to create base64AuthKey for JIRA
- Use this [link](https://support.atlassian.com/atlassian-account/docs/manage-api-tokens-for-your-atlassian-account/) to create your API Token for Jira
- Now head to [base64encode.org](https://www.base64encode.org/) and paste the email ID and API token generated in the below format
  ``EMAIL_ID:API_TOKEN``
- Now click encode to get the encoded base64 token and paste the value in config.properties file
---
### Need a feature?
Feel free to raise a issue in the project
---
### Found a bug???
Do raise a PR
