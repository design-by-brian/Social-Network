/***********************************************
 * AUTHOR: BRIAN SMITH
 * FILE_NAME: "UnitTestDSAHashTable.java"
 * DATE_CREATED: 20/10/2019
 * DATE_LAST_EDITED: 27/10/2019
 * PURPOSE: Test harness for DSAHashTable
 ***********************************************/
public class UnitTestDSAHashTable {
    public static void main(String[] args) {
        int numTest = 4;
        int numPassed = 0;
        DSAHashTable table = new DSAHashTable(10);

//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=//
        //TEST PUT ... 1//
        try {
            System.out.println("=================================================================");
            System.out.println("Creating new DSAHashTable of size 10 (actual size 11, next prime)");
            System.out.println("=================================================================");
            System.out.println("TABLE SIZE: " + table.getTableSize());
            System.out.println("==============");
            System.out.println("<>--- TESTING: put() ---<>");
            table.put("14491544", "aaa");
            table.put("14224712", "bbb");
            table.put("13984829", "ccc");
            table.put("15984229", "ddd");
            table.put("13324829", "eee");
            System.out.println("Key: 14491544 added, Value: aaa");
            System.out.println("Key: 14224712 added, Value: bbb");
            System.out.println("Key: 13984829 added, Value: ccc");
            System.out.println("Key: 15984229 added, Value: ddd");
            System.out.println("Key: 13324829 added, Value: eee");
            System.out.println("TEST PASSED");
            numPassed++;
        } catch (Exception e) { System.out.println("TEST FAILED"); }

//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=//
        //TEST GET ... 2
        try {
            System.out.println("<>--- TESTING: get() ---<>");

            System.out.print("Getting 14491544: ");
            String getString = (String) table.get("14491544");
            if (!getString.equals("aaa")) {
                throw new Exception();
            }
            System.out.println("PASSED");

            System.out.print("Getting 14224712: ");
            getString = (String) table.get("14224712");
            if (!getString.equals("bbb")) {
                throw new Exception();
            }
            System.out.println("PASSED");

            System.out.print("Getting 13984829: ");
            getString = (String) table.get("13984829");
            if (!getString.equals("ccc")) {
                throw new Exception();
            }
            System.out.println("PASSED");

            System.out.print("Getting 15984229: ");
            getString = (String) table.get("15984229");
            if (!getString.equals("ddd")) {
                throw new Exception();
            }
            System.out.println("PASSED");

            System.out.print("Getting 13324829: ");
            getString = (String) table.get("13324829");
            if (!getString.equals("eee")) {
                throw new Exception();
            }
            System.out.println("PASSED");
            numPassed++;
        } catch (Exception e) { System.out.println("TEST FAILED"); }

//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=//
        //TEST REMOVE ... 3
        try {
            System.out.println("<>--- TESTING: remove() ---<>");
            System.out.print("Removing 14491544: ");
            table.remove("14491544");
            System.out.println("PASSED");
            System.out.print("Removing 14224712: ");
            table.remove("14224712");
            System.out.println("PASSED");
            numPassed++;
        } catch (Exception e) { System.out.println("TEST FAILED"); }

//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=//
        //TEST RESIZE ... 4
        try {
            System.out.println("<>--- TESTING: resize() ---<>");
            System.out.println("=============================");
            System.out.println("LOAD FACTOR: " + table.getLoadFactor());
            System.out.println("=============================");
            table.put("12491514", "fff");
            table.put("13224512", "ggg");
            table.put("13944869", "hhh");
            System.out.println("Key: 12491514 added, Value: fff");
            System.out.println("Key: 13224512 added, Value: ggg");
            System.out.println("Key: 13944869 added, Value: hhh");
            System.out.println("=============================");
            System.out.println("LOAD FACTOR: " + table.getLoadFactor());
            System.out.println("=============================");
            String[] keys = table.exportKeys();

            for (int ii = 0;ii < keys.length;ii++) {
                switch(keys[ii]) {
                    case "12491514":
                        break;
                    case "13224512":
                        break;
                    case "13944869":
                        break;
                    case "13984829":
                        break;
                    case "15984229":
                        break;
                    case "13324829":
                        break;
                    default:
                        throw new Exception();
                }
            }
            System.out.println("TEST PASSED");
            numPassed++;
        } catch (Exception e) { System.out.println("TEST FAILED"); e.printStackTrace(); }

//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=//
        System.out.print("\nTests Passed: " + numPassed + "/" + numTest);
    }
}
