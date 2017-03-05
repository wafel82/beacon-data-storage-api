package com.wafel.beacondatastorageapi;

import java.io.Serializable;

import rx.Observable;

@SuppressWarnings({"WeakerAccess", "unused"})
public interface BeaconDataStorage {
    interface GetDataListener<DATA extends Serializable> {
        void onDataLoaded(DATA data);
        void onDataNotFound(BeaconDataNotFoundException error);
        void onDaLoadFailure(BeaconDataStorageException error);
    }

    interface PutDataListener {
        void onDataStored();
        void onDataStoreError(BeaconDataStorageAuthorizationFailedException error);
    }

    <DATA extends Serializable> boolean willItFit(DATA data);
    <DATA extends Serializable, KEY extends DataTokens.DataToken<? extends DATA>> void put(KEY key, DATA data, PutDataListener putDataListener);
    <DATA extends Serializable, KEY extends DataTokens.DataToken<? extends DATA>> Observable<DATA> put(KEY key, DATA data);
    <DATA extends Serializable, KEY extends DataTokens.DataToken<? extends DATA>> void syncPut(KEY key, DATA data) throws BeaconDataStorageAuthorizationFailedException, BeaconDataStorageCapacityExceeded, BeaconDataStorageException;
    <DATA extends Serializable, KEY extends DataTokens.DataToken<DATA>> void get(KEY key, GetDataListener<DATA> getDataListener);
    <DATA extends Serializable, KEY extends DataTokens.DataToken<DATA>> Observable<DATA> get(KEY key);
    <DATA extends Serializable, KEY extends DataTokens.DataToken<DATA>> DATA syncGet(KEY key) throws BeaconDataNotFoundException, BeaconDataStorageException;
}
