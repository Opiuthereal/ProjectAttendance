abstract class SQLManipulation {
    abstract void createAccess(String name, String password, String[] accessRight);
    abstract void AccessTable (String[] queryElem);
}
