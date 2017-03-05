package com.wafel.beacondatastorageapi;

import java.io.Serializable;

class SimpleDataToken<DATA extends Serializable> implements DataTokens.DataToken<DATA> {
    private final Class<? extends DATA> typeToken;
    private final String name;

    SimpleDataToken(Class<? extends DATA> typeToken, String name) {
        this.typeToken = typeToken;
        this.name = name;
    }

    @Override
    public String getName() {
        throw new UnsupportedOperationException("Not implemented in API demo version");
    }

    @Override
    public DATA getType() {
        throw new UnsupportedOperationException("Not implemented in API demo version");
    }

    @Override
    public int hashCode() {
        int result = typeToken.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleDataToken<?> simpleDataToken = (SimpleDataToken<?>) o;
        return typeToken.equals(simpleDataToken.typeToken) && name.equals(simpleDataToken.name);

    }
}
