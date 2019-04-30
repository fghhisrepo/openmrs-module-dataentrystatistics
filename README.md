[![Build Status](https://travis-ci.org/openmrs/openmrs-module-dataentrystatistics.svg?branch=master)](https://travis-ci.org/openmrs/openmrs-module-dataentrystatistics)
# openmrs-module-dataentrystatistics-v2
Provides data entry statictics

## Download Excel file feature.
The module gives an option to download an excel file which is password protected. The default password is **`segredo`**. One can set a custom password
by specifying `dataentrystatistics.spreadsheet.password` property in Openmrs runtime properties file as shown below.

```
dataentrystatistics.spreadsheet.password=mysecretpassword
```
