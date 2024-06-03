package wind57.hazelcast;

import com.hazelcast.internal.serialization.impl.compact.Schema;
import com.hazelcast.internal.serialization.impl.compact.SchemaService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemorySchemaService implements SchemaService {

    private final Map<Long, Schema> schemas = new ConcurrentHashMap<>();

    @Override
    public Schema get(long schemaId) {
        return schemas.get(schemaId);
    }

    @Override
    public void put(Schema schema) {
        long schemaId = schema.getSchemaId();
        Schema existingSchema = schemas.putIfAbsent(schemaId, schema);
        if (existingSchema != null && !schema.equals(existingSchema)) {
            throw new IllegalStateException("Schema with schemaId " + schemaId + " already exists. "
                    + "existing schema " + existingSchema
                    + "new schema " + schema);
        }
    }

    @Override
    public void putLocal(Schema schema) {
        put(schema);
    }
}
