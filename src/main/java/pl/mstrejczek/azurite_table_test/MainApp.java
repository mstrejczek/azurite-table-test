package pl.mstrejczek.azurite_table_test;

import java.net.URISyntaxException;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.RetryNoRetry;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.table.CloudTable;
import com.microsoft.azure.storage.table.CloudTableClient;
import com.microsoft.azure.storage.table.TableOperation;
import com.microsoft.azure.storage.table.TableRequestOptions;
import com.microsoft.azure.storage.table.TableResult;

public class MainApp {

    public static void main(String[] args) throws URISyntaxException, StorageException {
        CloudStorageAccount cloudStorageAccount = CloudStorageAccount.getDevelopmentStorageAccount();
        CloudTableClient cloudTableClient = cloudStorageAccount.createCloudTableClient();

        TableRequestOptions tableRequestOptions = new TableRequestOptions();
        tableRequestOptions.setRetryPolicyFactory(RetryNoRetry.getInstance()); // disable retry to complete faster
        cloudTableClient.setDefaultRequestOptions(tableRequestOptions);

        CloudTable cloudTable = cloudTableClient.getTableReference("foo");

        System.out.println("Trying to create table foo if not exists...");
        boolean created = cloudTable.createIfNotExists();
        System.out.println("Was table created? " + (created ? "yes" : "no"));

        // START - UNCOMMENT TO REPRODUCE https://github.com/Azure/Azurite/issues/686
        // System.out.println("Trying again to create table foo if not exists...");
        // created = cloudTable.createIfNotExists();
        // System.out.println("Was table created? "+(created ? "yes" : "no"));
        // END

        // START - UNCOMMENT TO REPRODUCE https://github.com/Azure/Azurite/issues/687
        // System.out.println("Trying to retrieve an entity that does not exist...");
        // try {
        //     TableResult tableResult = cloudTable.execute(TableOperation.retrieve("partitionKey", "rowKey", FooEntity.class));
        //     System.out.println("TableResult httpStatus=" + tableResult.getHttpStatusCode());
        // } catch (StorageException e) {
        //     System.out.println("Storage exception caught, HTTP status code="+e.getHttpStatusCode());
        //     e.printStackTrace();
        // }
        // END

        // START - UNCOMMENT TO REPRODUCE https://github.com/Azure/Azurite/issues/688
        System.out.println("Trying to insert an entity...");
        try {
            FooEntity fooEntity = new FooEntity("partitionKey", "rowKey");
            TableResult tableResult = cloudTable.execute(TableOperation.insert(fooEntity));
            System.out.println("TableResult httpStatus=" + tableResult.getHttpStatusCode());
        } catch (StorageException e) {
            System.out.println("StorageException caught, cause message=" + e.getCause().getMessage());
            e.printStackTrace();
        }
        // END
    }
}
