package org.mongodb.morphia.converters;

import java.util.List;

public class ConvertersUnderTest {
    private List<TypeConverter> untypedTypeEncoders;

    public ConvertersUnderTest(List<TypeConverter> untypedTypeEncoders) {
        this.untypedTypeEncoders = untypedTypeEncoders;
    }

    public org.mongodb.morphia.converters.TypeConverter original(final Class c) {
        for (final TypeConverter tc : untypedTypeEncoders) {
            if (tc.canHandle(c)) {
                return tc;
            }
        }
        return null;
    }

    public org.mongodb.morphia.converters.TypeConverter refactored(final Class c) {
        return untypedTypeEncoders.stream()
                                  .filter(tc -> tc.canHandle(c))
                                  .findFirst()
                                  .orElse(null);
    }

    public TypeConverter parallel(final Class c) {
        return untypedTypeEncoders.parallelStream()
                                  .filter(tc -> tc.canHandle(c))
                                  .findFirst()
                                  .orElse(null);
    }

}
