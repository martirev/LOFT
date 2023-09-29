module core {
    requires com.google.gson;

    exports core;

    opens core to com.google.gson;
}
