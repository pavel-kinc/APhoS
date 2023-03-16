package cz.muni.aphos.helper;

/**
 * JSON hierarchy of fields access
 */
public class ViewField {
    public static class Public{}
    public static class Child extends Public{}
    public static class Private extends Child{}
}
