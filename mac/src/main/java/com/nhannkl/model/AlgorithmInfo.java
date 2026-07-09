package com.nhannkl.model;

public class AlgorithmInfo {

    private String name;
    private String displayName;
    private int bitLength;

    public AlgorithmInfo() {
    }

    public AlgorithmInfo(String name, String displayName, int bitLength) {
        this.name = name;
        this.displayName = displayName;
        this.bitLength = bitLength;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getBitLength() {
        return bitLength;
    }

    public void setBitLength(int bitLength) {
        this.bitLength = bitLength;
    }

}
