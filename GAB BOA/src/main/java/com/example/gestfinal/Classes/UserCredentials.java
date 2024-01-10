package com.example.gestfinal.Classes;

public class UserCredentials {
    private static String ref;
    private  static  String pin;
    private static String transferType;

    public static String getRef() {
        return ref;
    }

    public static String getPin() {
        return pin;
    }

    public static String getTransferType() {
        return transferType;
    }

    public static void setTransferType(String transferType) {
        UserCredentials.transferType = transferType;
    }

    public static void setRef(String ref) {
        UserCredentials.ref = ref;
    }

    public static void setPin(String pin) {
        UserCredentials.pin = pin;
    }

    public static String toJsonString() {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{");
        jsonBuilder.append("\"transferType\":\"").append(transferType).append("\",");
        jsonBuilder.append("\"ref\":\"").append(ref).append("\",");
        jsonBuilder.append("\"pin\":\"").append(pin).append("\"");
        jsonBuilder.append("}");
        return jsonBuilder.toString();
    }


    /*public UserCredentials() {
    }

    public UserCredentials(String ref, String pin) {
        this.ref=ref;
        this.pin=pin;
    }*/
}
