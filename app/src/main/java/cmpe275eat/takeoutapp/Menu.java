package cmpe275eat.takeoutapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

/**
 * Created by yichinhsiao on 5/8/18.
 */

public class Menu {

    private String category, name;
    private Double price;
    private int id, calories, prep_time, popularity;
    private Bitmap picture;
    private Boolean enabled;

    public Menu(){

    }

    public Menu(int id, String category, String name, Double price, int calories, int prep_time,
                Bitmap picture, Boolean enabled, int popularity){
        this.id = id;
        this.category = category;
        this.name = name;
        this.price = price;
        this.calories = calories;
        this.prep_time = prep_time;
        this.picture = picture;
        this.enabled = enabled;
        this.popularity = popularity;
    }

    public void setID(int id){
        this.id = id;
    }

    public void setCategory(String category){
        this.category = category;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public void setPreparationTime(int prep_time) {
        this.prep_time = prep_time;
    }

    public void setPicture(Bitmap picture) {
        this.picture = picture;
    }

    public void setPicture(String picture) {
        byte[] decodedBytes = Base64.decode(picture, 0);
        Bitmap result = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        this.picture = result;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public int getId() {
        return this.id;
    }

    public String getCategory() {
        return this.category;
    }

    public String getName() {
        return this.name;
    }

    public Double getPrice() {
        return this.price;
    }

    public int getCalories() {
        return this.calories;
    }

    public int getPreparationTime() {
        return this.prep_time;
    }

    public Bitmap getPicture() {
        return this.picture;
    }

    public Boolean getEnabled() {
        return this.enabled;
    }

    public int getPopularity() {
        return this.popularity;
    }
}
