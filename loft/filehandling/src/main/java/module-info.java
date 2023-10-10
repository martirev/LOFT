module filehandling {
    requires transitive core;
    requires com.google.gson;
    
    exports filehandling;
    opens filehandling to com.google.gson;
}
