# Beacon Data Storage API
_Beacon data storage api_ lets you store and retrieve data on the beacon. You can use it to easily deal with any data you'd
like  - text, pictures, audio... everything what can be modeled by Java serializable object.

_Beacon data storage api_ is designed to work much like map or dictionary where all data to be stored/retrieved are "addressed" 
by the unique key. 

To send data to the beacon you have to use one of the available `BeaconDataStorage::put` methods and pass 
the appropriate _key_ and  _your serializable data_ as a parameters. 
Assuming the `FancyData` is a serializable type of date we'd like to store i the beacon:
```Java
Observable<FancyData> observable = beaconDataStorage.put(DataTokens.forType(FancyData.class, "fancy data"), data);
```
Retrieving data from the beacon is even simpler - just choose one of the `BeaconDataStorage::get` methods with delivery mechanism fitting your needs 
and pass appropriate key as a parameter. Here is an example with Rx binding:
```Java
Observable<FancyData> fancyDataObservable = beaconDataStorage.get(DataTokens.forType(FancyData.class, "fancy data"));
```
# 2 min step-by-step guide
Assuming the `FancyData` is a serializable type of date we'd like to store/retrieve:

1. At the very beginning (e.g. in Application's onCreate()) initialize your Estimote SDK.

 ```Java
 // App ID & App Token can be taken from App section of Estimote Cloud.
 EstimoteSDK.initialize(applicationContext, appId, appToken);
 ```
2. Get instance of BeaconDataStorage associated with your beacon

 ```Java
 Beacon beacon = ...; // Here gose code to get the beacon
 BeaconDataStorage beaconDataStorage = new BeaconDataStorageFactory().getBeaconDataStorage(beacon);
 ```
3. To store your data on the beacon:

  ```Java
  FancyData data = ...;
  beaconDataStorage.put(DataTokens.forType(FancyData.class, "fancy data"), data)
                  .subscribeOn(Schedulers.io())
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribe(new Action1<FancyData>() {
                      @Override
                      public void call(FancyData fancyData) {
                          //  Data successfully stored.
                      }
                  }, new Action1<Throwable>() {
                      @Override
                      public void call(Throwable throwable) {
                          // Error occurred while storing data
                      }
                  });
  ```
4. To get you data from the beacon:

  ```Java
  beaconDataStorage.get(DataTokens.forType(FancyData.class, "fancy data"))
                  .subscribeOn(Schedulers.io())
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribe(new Action1<FancyData>() {
                      @Override
                      public void call(FancyData fancyData) {
                          // Data successfully loaded
                      }
                  }, new Action1<Throwable>() {
                      @Override
                      public void call(Throwable throwable) {
                          // Error occurred while loading data
                      }
                  });
  ```
  
# In more detalis...
## Typesafety - What the heck are DataTokens?
_Beacon Data Storage API_ is designed to be used with strongly typed languages (namely Java/Kotlin). 
Thus it takes advantage of languages' strong type system and **allows you to store and retrieve your data in fully type-safe manner :D**
You are allowed to store Strings, Longs, Pictures... and any other serializable data type on a single Beacon device. 
Then, you can access previously stored data without single cast to be placed in your code.

To achieve this nice behaviour we have to somehow "inform" the _Beacon Data Storage API_ about the type of data we are going to store/retrieve.
This is the place where DataTokens come to the party - they are playing two roles in _Beacon Data Storage API_:
- Act as a unique key to store/retrieve data with (just lik key in a map collection).
- Hold information about the type of data to be stored/retrieved

_If you are java programmer, then you probably know the wonderful Joshua Bloch's book named "Effective Java". 
There's **"Item 29: Consider typesafe heterogeneous containers"** chapter in this book.
`BeaconDataStorage` together with `DataToken` is aim to crystallize pattern described in this chapter._

Creation of the DataTokens is fairly easy - there's factory named `DataTokens` for this purposes. It provides handy methods for creating
DataToken for common types like String, Long etc. :
```Java
DataToken<String> stringDataToken = DataTokens.forString("some String data to be stored under this token");
DataToken<Long> longDataToken = DataTokens.forLong("some Long data to be stored under this token");
```
What if you have your own data type (e.g. POJO encapsulating your application's specific data)? Use `DataTokens::forType` method:
```Java
DataToken<FancyData> fancyDataDataToken = DataTokens.forType(FancyData.class, "fancy data");
```
Note that two DataTokens are considered equal when they are reffering to the same data type and are created with the same `name` parameter.
```Java
DataToken<Integer> someIntDataToken = DataTokens.forInt("Some data token");
DataToken<Long> someLongDataToken = DataTokens.forLong("Some data token");
DataToken<Integer> anotherIntDataToken = DataTokens.forInt("Some data token");
DataToken<Integer> yetAnotherIntDataToken = DataTokens.forInt("Yet another int data token");
        
someIntDataToken.equals(yetAnotherIntDataToken); // false - the same type but different name used
someIntDataToken.equals(someLongDataToken); // false - the same name but different type used
someIntDataToken.equals(anotherIntDataToken); // true - the same type and name used
```

## Get Beacon Data Storage 
All beacon's data put/get operations are encapsulated in _BeaconDataStorage_ instance. There is one-to-one mapping between beacon and _BeaconDataStorage_.
In other words - you have to have separate _BeaconDataStorage's_ for separate beacons.
To get the _BeaconDataStorage_ instance - utilize _BeaconDataStorageFactory_ as follows:
```Java
Beacon beacon = ...; // Here gose code to get the beacon 
BeaconDataStorage beaconDataStorage = new BeaconDataStorageFactory().getBeaconDataStorage(beacon);
```
 
## Store data on the device
Different problems requires different solution. `BeaconDataStorage` provides set of alternative methods handling store data operation.
You can choose api that best fit your needs and play well with your application architecture. 

### First things first - constraints
#### Authorization
To be able to write data the beacon you have to authorize your beacons' access. However - to keep things simple 
there's no special authorization options in Beacon Data Storage API. All you need to do is to ensure the Estimote SDK is properly initialized 
with your _application id_ and _application token_ :
```Java
//  App ID & App Token can be taken from App section of Estimote Cloud.
EstimoteSDK.initialize(applicationContext, appId, appToken);
```

#### Storage capacity
Beacon is a small device. Thus there is no much room for you data on it. As for now you can store up to **128KB** in total for you data.
If data you are attempting to write exceeds this limit - you'll get `BeaconStorageFull` exception.

Don't like exceptions? There's recepy for you :). You can use `beaconDataStorage::willItFit` method to check if data you are about to store
will fit in to beacon storage and chandle different cases without playing with  exceptions. 
Assuming the `FancyData` is a serializable type of date we'd like to store in the beacon:
```Java
FancyData data = ...;
if( beaconDataStorage.willItFit(data) ) {
    beaconDataStorage.put(DataTokens.forType(FancyData.class, "fancy data"), data);
} else {
    // Handle the case when there's no available space for you data
}
```
### listener-based `put` api
callback-based `BeaconDataStorage::put` method variant is an asynchronous, non-blocking method using callback `PutDataListener` to inform caller about the store operation results:
```Java
FancyData data = ...;
beaconDataStorage.put(DataTokens.forType(FancyData.class, "fancy data"), data, new BeaconDataStorage.PutDataListener() {
    @Override
    public void onDataStored() {
        // Data successfully stored 
    }

    @Override
    public void onDataStoreError(BeaconDataStoreException error) {
        // Error occurred while storing data
    }
});
```

### Rx-based `put` api
You can seamlessly incorporate `BeaconDataStorage::put` api in to Rx reactive flow. 
To do so - use Rx-based `put` api. It returns observable to be used to observe store results.
The returned observable will emit single item when data store succeeded. 
If case of errors during store operation - `observable::error` will be fired-up.
```Java
 beaconDataStorage.put(DataTokens.forType(FancyData.class, "fancy data"), data)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<FancyData>() {
                    @Override
                    public void call(FancyData fancyData) {
                        //  Data successfully stored.
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        // Error occurred while storing data
                    }
                });
```

### Synchronous `put` api
Sometimes you need to have things synchronous. Simply call a method and block until results are ready. 
You can achieve this with `BeaconDataStorage::syncPut` method:
```Java
beaconDataStorage.syncPut(DataTokens.forType(FancyData.class, "fancy data"), data);
```
Be aware that when you're using synchronous api call, you have to handle exceptions that may be thrown when thing go wrong during saving data on the beacon:
- `BeaconDataStorageAuthorizationFailed` - thrown when you attempt to perform an unauthorized write to the beacon (check Authorization section for details)
- `BeaconDataStorageCapacityExceeded` - thrown when there is no enough space on the beacon to store your data (check Storage capacity section for details)
- `BeaconDataStorageException` - thrown when any other error occurred

## Get data from the device
Once you have some data stored on the beacon, you can utilize `BeaconDataStorage::get` methods to retrieve them. 
Just like in case of `put` methods - there is a set of alternative methods handling get data operation.
You can choose api that best fit your needs and play well with your application architecture. 

There is no authorization constraint during data read access - Reading of the data does not require authorization, 
and can be performed fully offline by simply using a Bluetooth LE connection.

### listener-based `get` api
callback-based `BeaconDataStorage::get` method variant is an asynchronous, non-blocking method using your `GetDataListener` callback implementation. 
- `GetDataListener::onDataLoaded` - method is called as soon as data gets retrieved from the beacon
- `GetDataListener::onDataNotFound` - called to handle error case when there are no data available under requested key
- `GetDataListener::onDaLoadFailure` - called when any other error occurred (beacon connection failure etc.)

Here is the examle:
```Java
beaconDataStorage.get(DataTokens.forType(FancyData.class, "fancy data"), new BeaconDataStorage.GetDataListener<FancyData>() {
            @Override
            public void onDataLoaded(FancyData fancyData) {
                // Data successfully retrieved
            }

            @Override
            public void onDataNotFound(BeaconDataNotFoundException error) {
                // There is no data associated with specified DataToken
            }

            @Override
            public void onDaLoadFailure(BeaconDataLoadFailedException error) {
                // Unable to load data from the beacon
            }
        });
```

### Rx-based `get` api
You can seamlessly incorporate `BeaconDataStorage::get` api in to Rx reactive flow. 
To do so - use Rx-based `get` api. It returns observable that emits single item - the data you requested for - and completes.
If case of errors during the `get` operation - `observable::error` will be fired-up.
```Java
 beaconDataStorage.get(DataTokens.forType(FancyData.class, "fancy data"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<FancyData>() {
                    @Override
                    public void call(FancyData fancyData) {
                        // Data successfully loaded 
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        // Error occurred while loading data
                    }
                });
```

### Synchronous `get` api
To get data from the beacon in synchronous manner (call get and block until results gets ready) - use `BeaconDataStorage::syncGet` method as follows:
```Java
try {
  beaconDataStorage.syncGet(DataTokens.forType(FancyData.class, "fancy data"))
} catch (BeaconDataNotFoundException e) {
  // Handle the case when there's no data associated with specified DataToken
} catch (BeaconDataLoadFailedException e) {
  // Unable to load data from the beacon
}
```
Be aware that when you're using synchronous api call, you have to handle exceptions that may be thrown when thing go wrong during obtaining data from the beacon:
- `BeaconDataNotFoundException` - thrown when there is no data associated with DataToken you provided
- `BeaconDataStorageException` - thrown when any other error occurred while obtaining data from the beacon

