package com.wafel.beacondatastorageapi;


import java.io.Serializable;

import rx.Observable;

class ReadOnlyBeaconDataStorage implements BeaconDataStorage {
    private final FullAccessBeaconDataStorage dataStorage;

    ReadOnlyBeaconDataStorage(FullAccessBeaconDataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    @Override
    public <DATA extends Serializable> boolean willItFit(DATA data) {
        return dataStorage.willItFit(data);
    }

    @Override
    public <DATA extends Serializable, KEY extends DataTokens.DataToken<? extends DATA>> void put(KEY key, DATA data, PutDataListener putDataListener) {
        putDataListener.onDataStoreError(new BeaconDataStorageAuthorizationFailedException("Cannot store beacon data in non-authorized state"));
    }

    @Override
    public <DATA extends Serializable, KEY extends DataTokens.DataToken<? extends DATA>> Observable<DATA> put(KEY key, DATA data) {
        return Observable.error(new BeaconDataStorageAuthorizationFailedException("Cannot store beacon data in non-authorized state"));
    }

    @Override
    public <DATA extends Serializable, KEY extends DataTokens.DataToken<? extends DATA>> void syncPut(KEY key, DATA data) throws BeaconDataStorageAuthorizationFailedException {
        throw new BeaconDataStorageAuthorizationFailedException("Cannot store beacon data in non-authorized state");
    }

    @Override
    public <DATA extends Serializable, KEY extends DataTokens.DataToken<DATA>> void get(KEY key, GetDataListener<DATA> getDataListener) {
        dataStorage.get(key, getDataListener);
    }

    @Override
    public <DATA extends Serializable, KEY extends DataTokens.DataToken<DATA>> Observable<DATA> get(final KEY key) {
        return dataStorage.get(key);
    }

    @Override
    public <DATA extends Serializable, KEY extends DataTokens.DataToken<DATA>> DATA syncGet(KEY key) throws BeaconDataNotFoundException, BeaconDataStorageException {
        return dataStorage.syncGet(key);
    }
}
