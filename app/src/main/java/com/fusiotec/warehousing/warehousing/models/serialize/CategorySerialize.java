package com.fusiotec.warehousing.warehousing.models.serialize;

import com.fusiotec.warehousing.warehousing.models.db_classes.Category;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * Created by Owner on 8/12/2017.
 */

public class CategorySerialize implements JsonSerializer<Category> {
    @Override
    public JsonElement serialize(Category src, Type typeOfSrc, JsonSerializationContext context) {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", src.getId());
        jsonObject.addProperty("name", src.getName());
        jsonObject.addProperty("desc", src.getDesc());
        jsonObject.add("branch_categories", context.serialize(src.getBranch_categories()));
        return jsonObject;
    }
}
