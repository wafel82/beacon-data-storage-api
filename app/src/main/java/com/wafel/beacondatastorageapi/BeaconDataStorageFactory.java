package com.wafel.beacondatastorageapi;


import com.estimote.sdk.Beacon;

@SuppressWarnings("unused")
public class BeaconDataStorageFactory {

    public BeaconDataStorage getBeaconDataStorage(Beacon beacon) {
        FullAccessBeaconDataStorage fullAccessBeaconDataStorage = new FullAccessBeaconDataStorage(beacon);
        return isAuthorized(beacon)
                ? fullAccessBeaconDataStorage
                : new ReadOnlyBeaconDataStorage(fullAccessBeaconDataStorage);
    }

    private boolean isAuthorized(Beacon beacon) {
        // Here is the place where internal magic should happened - determine if we are authorized
        // to write to the beacon
        throw new UnsupportedOperationException("Not implemented in API demo version");
    }
}
