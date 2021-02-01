# What is it?
This application shows that Azurite 3.9.0-table-alpha.1 does not work well with Azure Storage 8.6.5 library.
`CloudTable.createIfNotExists` throws NullPointerException if table already exists.

# How to run?
1. Start Azurite (tried with NPM)
   
## Reproducing https://github.com/Azure/Azurite/issues/686
2. Make sure code between `// START - UNCOMMENT TO REPRODUCE https://github.com/Azure/Azurite/issues/686` and `// END` is uncommented.
3. Start the application (`./gradlew run`)

Expected behaviour:
* This text is printed to stdout:
```
Trying to create table foo if not exists...
Was table created? yes
Trying again to create table foo if not exists...
Was table created? no
```

Actual behaviour:
* Table is created, then exception is thrown by Azure Storage library due to response JSON parsing issues.

The problem does not appear when running against Azure Table Storage cloud endpoints, only with Azurite. 
```
Trying to create table foo if not exists...
Was table created? yes
Trying again to create table foo if not exists...
Exception in thread "main" com.microsoft.azure.storage.StorageException: OK
        at com.microsoft.azure.storage.StorageException.translateException(StorageException.java:87)
        at com.microsoft.azure.storage.core.ExecutionEngine.executeWithRetry(ExecutionEngine.java:220)
        at com.microsoft.azure.storage.table.QueryTableOperation.performRetrieve(QueryTableOperation.java:178)
        at com.microsoft.azure.storage.table.TableOperation.execute(TableOperation.java:694)
        at com.microsoft.azure.storage.table.CloudTable.exists(CloudTable.java:888)
        at com.microsoft.azure.storage.table.CloudTable.createIfNotExists(CloudTable.java:290)
        at com.microsoft.azure.storage.table.CloudTable.createIfNotExists(CloudTable.java:265)
        at pl.mstrejczek.azurite_table_test.MainApp.main(MainApp.java:28)
Caused by: java.lang.NullPointerException
        at com.microsoft.azure.storage.table.TableDeserializer.parseJsonEntity(TableDeserializer.java:291)
        at com.microsoft.azure.storage.table.TableDeserializer.parseSingleOpResponse(TableDeserializer.java:203)
        at com.microsoft.azure.storage.table.QueryTableOperation.parseResponse(QueryTableOperation.java:143)
        at com.microsoft.azure.storage.table.QueryTableOperation$1.postProcessResponse(QueryTableOperation.java:236)
        at com.microsoft.azure.storage.table.QueryTableOperation$1.postProcessResponse(QueryTableOperation.java:193)
        at com.microsoft.azure.storage.core.ExecutionEngine.executeWithRetry(ExecutionEngine.java:166)
        ... 6 more
```

## Reproducing https://github.com/Azure/Azurite/issues/687
2. Make sure code between `// START - UNCOMMENT TO REPRODUCE https://github.com/Azure/Azurite/issues/686` and `// END` is commented out.
3. Make sure code between `// START - UNCOMMENT TO REPRODUCE https://github.com/Azure/Azurite/issues/687` and `// END` is uncommented.
4. Start the application (`./gradlew run`)

Expected behaviour:
* This text is printed to stdout - note that HTTP Status code should be 404 (Not Found)
```
Trying to create table foo if not exists...
Was table created? yes
TableResult httpStatus=404
```

Actual behaviour:
* This text is printed to stdout - note that HTTP Status code is 409 (Conflict), which does not seem right. Also StorageException stacktrace is printed to stderr.
```
Trying to create table foo if not exists...
Was table created? yes
Trying to retrieve an entity that does not exist...
Storage exception caught, HTTP status code=409
```

