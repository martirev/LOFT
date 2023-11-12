module springboot.restserver {
    requires spring.web;
    requires spring.beans;
    requires spring.boot;
    requires spring.context;
    requires spring.boot.autoconfigure;

    requires org.slf4j;

    requires filehandling;
    requires core;

    opens springboot.restserver to spring.beans, spring.context, spring.web;
}
