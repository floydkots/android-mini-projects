package com.parse.starter;

public final class UserType {
    final static String RIDER = "rider";
    final static String DRIVER = "driver";
    final static String RIDER_OR_DRIVER = "riderOrDriver";
    final static String LOCATION = "location";
    final static String USERNAME = "username";
    final static String DRIVER_USERNAME = "driverUsername";

    public static final class Classes {
        final static String USER = "User";
        final static String REQUEST = "Request";
    }

    public static final class Extras {
        final static String REQUEST_LATITUDE = "requestLatitude";
        final static String REQUEST_LONGITUDE = "requestLongitude";
        final static String DRIVER_LATITUDE = "driverLatitude";
        final static String DRIVER_LONGITUDE = "driverLongitude";
        final static String USERNAME = UserType.USERNAME;
    }
}
