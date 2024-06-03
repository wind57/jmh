package wind57.hazelcast;

import com.hazelcast.nio.serialization.compact.CompactReader;
import com.hazelcast.nio.serialization.compact.CompactSerializer;
import com.hazelcast.nio.serialization.compact.CompactWriter;

public class PersonCompactSerializer implements CompactSerializer<Person> {

    @Override
    public Person read(CompactReader compactReader) {
        return new Person(
                compactReader.readString("name"),
                compactReader.readString("surname"),
                compactReader.readInt32("age"));
    }

    @Override
    public void write(CompactWriter compactWriter, Person personCompact) {
        compactWriter.writeString("name", personCompact.getName());
        compactWriter.writeString("surname", personCompact.getSurname());
        compactWriter.writeInt32("age", personCompact.getAge());
    }

    @Override
    public String getTypeName() {
        return "wind57.PersonCompact";
    }

    @Override
    public Class<Person> getCompactClass() {
        return Person.class;
    }
}
