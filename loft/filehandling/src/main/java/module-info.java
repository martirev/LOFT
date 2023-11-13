module filehandling {
    requires transitive core;
    requires com.google.gson;
    requires java.net.http;

    exports filehandling;
    opens filehandling to com.google.gson;
}
