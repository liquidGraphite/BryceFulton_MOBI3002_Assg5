package com.codelab.basics;

// Object DB ... see Room for Android Studio
// https://developer.android.com/training/data-storage/room
public class DataModel {

    private long id;
    private String str_col;
    private Integer num_col;

    // Change to reflect Pokemon
//    private long id;
//    private String Pokemon_Name;
//    private String Pokemon_Type;
//    private Integer Pokemon_Number;
    // ...


    public DataModel() {
        this.setId(0);
        this.setModelName("default modelName");
        this.setModelNumber(0);
    }

    public DataModel(long id, String modelName, Integer modelNumber) {
        this.setId(id);
        this.setModelName(modelName);
        this.setModelNumber(modelNumber);
    }

    @Override
    public String toString() {
        return "DataModel{" +
                "id=" + getId() +
                ", modelName='" + getModelName() + '\'' +
                ", modelNumber=" + getModelNumber();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getModelName() {
        return str_col;
    }

    public void setModelName(String modelName) {
        this.str_col = modelName;
    }

    public Integer getModelNumber() {
        return num_col;
    }

    public void setModelNumber(Integer modelNumber) {
        this.num_col = modelNumber;
    }

}
