package pl.mstrejczek.azurite_table_test;

import java.net.URISyntaxException;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.RetryNoRetry;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.table.CloudTable;
import com.microsoft.azure.storage.table.CloudTableClient;
import com.microsoft.azure.storage.table.TableRequestOptions;

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
        System.out.println("Was table created? "+(created ? "yes" : "no"));

        System.out.println("Trying again to create table foo if not exists...");
        created = cloudTable.createIfNotExists();
        System.out.println("Was table created? "+(created ? "yes" : "no"));
    }
}
