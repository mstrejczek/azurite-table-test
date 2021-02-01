package pl.mstrejczek.azurite_table_test;

import com.microsoft.azure.storage.table.TableServiceEntity;

public class FooEntity extends TableServiceEntity {

    public FooEntity() {
        // intentionally empty
    }

    public FooEntity(final String partitionKey, final String rowKey) {
        super(partitionKey, rowKey);
    }
}
