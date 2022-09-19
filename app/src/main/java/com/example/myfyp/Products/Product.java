package com.example.myfyp.Products;

import android.net.Uri;
import android.widget.EditText;

import java.io.Serializable;

public class Product implements Serializable{
    private Categories categories;

    public String typeCosmetic;
    public String image;
    public String prodName;
    public String mDate;
    public String expiry;
    public String prodQuantity;
    public String price;

    public Categories getCategories() {
        return categories;
    }

    public Product(String image, String prodName, String typeCosmetic, String mDate, String expiry, String price, String prodQuantity) {
            this.image = image;
            this.prodName = prodName;
            this.typeCosmetic = typeCosmetic;
            this.mDate = mDate;
            this.expiry = expiry;
            this.prodQuantity = prodQuantity;
            this.price = price;
    }

    public Product(Categories categories) {
        this.categories = categories;
    }

    public Product() {
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getProdName() {
            return prodName;
        }

        public void setProdName(String prodName) {
            this.prodName = prodName;
        }

        public String getTypeCosmetic() {
            return typeCosmetic;
        }

        public void setTypeCosmetic(String typeCosmetic) {
            this.typeCosmetic = typeCosmetic;
        }

        public String getmDate() {
                return mDate;
            }

        public void setmDate(String mDate) {
            this.mDate = mDate;
        }

        public String getExpiry(String pickerDateString) {
            return expiry;
        }

        public void setExpiry(String expiry) {
            this.expiry = expiry;
        }

        public String getProdQuantity() {
            return prodQuantity;
        }

        public void setProdQuantity(String prodQuantity) {
            this.prodQuantity = prodQuantity;
        }

        public Product(Uri imageUri, EditText prodName, EditText dateM, EditText dateE, EditText quantity) {

        }
}
