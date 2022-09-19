package com.example.myfyp.Products;

import java.io.Serializable;

public class Categories extends Product implements Serializable {
    public String categoryName;
    private Product product;

    public Categories(Product product) {
        this.product = product;
    }

    public Product getProduct() {
        return product;
    }

    public Categories(String categoryName) {
        this.categoryName = categoryName;
    }

    public Categories() {
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
