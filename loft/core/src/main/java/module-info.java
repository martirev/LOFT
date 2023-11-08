module core {
    exports core;
    requires com.google.gson;

    opens core to com.google.gson;
}
