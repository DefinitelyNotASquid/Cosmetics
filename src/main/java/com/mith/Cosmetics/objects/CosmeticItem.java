package com.mith.Cosmetics.objects;

public class CosmeticItem {

    private final String name;
    private final String genericItem;
    private Integer customModel;

    public CosmeticItem(String name, String genericItem, Integer customModel) {
        this.customModel = customModel;
        this.name = name;
        this.genericItem = genericItem;
    }

    public String getGenericItem(){
        return genericItem;
    }

    public Integer getCustomModel() {
        return customModel;
    }

    public String getName() {
        return name;
    }
}
