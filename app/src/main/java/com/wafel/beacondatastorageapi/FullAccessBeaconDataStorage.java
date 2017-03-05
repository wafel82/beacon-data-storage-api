package com.wafel.beacondatastorageapi;

import com.estimote.sdk.Beacon;

import java.io.Serializable;

import rx.Observable;

class FullAccessBeaconDataStorage implements BeaconDataStorage {
    private final Beacon beacon;

    FullAccessBeaconDataStorage(Beacon beacon) {
        this.beacon = beacon;
    }

    @Override
    public <DATA extends Serializable> boolean willItFit(DATA data) {
        // Here is the place where internal magic should happened - check if there is enough
        // space available on the beacon to store data
        throw new UnsupportedOperationException("Not implemented in API demo version");
    }

    @Override
    public <DATA extends Serializable, KEY extends DataTokens.DataToken<? extends DATA>> void put(KEY key, DATA data, PutDataListener putDataListener) {
        // Here is the place where internal magic should happened - store data in to beacon
        throw new UnsupportedOperationException("Not implemented in API demo version");

    }

    @Override
    public <DATA extends Serializable, KEY extends DataTokens.DataToken<? extends DATA>> Observable<DATA> put(final KEY key, final DATA data) {
        // Here is the place where internal magic should happened - store data in to beacon
        throw new UnsupportedOperationException("Not implemented in API demo version");
    }

    @Override
    public <DATA extends Serializable, KEY extends DataTokens.DataToken<? extends DATA>> void syncPut(KEY key, DATA data) throws BeaconDataStorageAuthorizationFailedException {
        // Here is the place where internal magic should happened - store data in to beacon
        throw new UnsupportedOperationException("Not implemented in API demo version");
    }

    @Override
    public <DATA extends Serializable, KEY extends DataTokens.DataToken<DATA>> void get(KEY key, GetDataListener<DATA> getDataListener) {
        // Here is the place where internal magic should happened - obtain data from beacon
        throw new UnsupportedOperationException("Not implemented in API demo version");
    }

    @Override
    public <DATA extends Serializable, KEY extends DataTokens.DataToken<DATA>> Observable<DATA> get(KEY key) {
        // Here is the place where internal magic should happened - obtain data from beacon
        throw new UnsupportedOperationException("Not implemented in API demo version");
    }

    @Override
    public <DATA extends Serializable, KEY extends DataTokens.DataToken<DATA>> DATA syncGet(KEY key) throws BeaconDataNotFoundException, BeaconDataStorageException {
        // Here is the place where internal magic should happened - obtain data from beacon
        throw new UnsupportedOperationException("Not implemented in API demo version");
    }
}
