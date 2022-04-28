package com.kauruck.exterra.data;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.kauruck.exterra.api.geometry.GeometricRule;
import com.kauruck.exterra.api.geometry.IGeometricTest;
import com.kauruck.exterra.geometry.BlockPosHolder;
import com.kauruck.exterra.geometry.GeometryParts;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ShapeData {

    private final String name;
    private final int numberOfPoints;
    private List<IGeometricTest> parts = new ArrayList<>();

    public ShapeData(String name, int numberOfPoints){
        this.name = name;
        this.numberOfPoints = numberOfPoints;
    }

    public String getName() {
        return name;
    }

    public int getNumberOfPoints() {
        return numberOfPoints;
    }

    public List<IGeometricTest> getParts() {
        return parts;
    }

    public boolean test(BlockPosHolder positions){
        if(positions.size() != numberOfPoints)
            return false;
        for(IGeometricTest currentTest : this.parts){
            BlockPosHolder subHolder;
            if(currentTest instanceof GeometricRule){
                subHolder = positions.subHolder(currentTest.expectedBlockPosNames());
            }else{
                subHolder = positions;
            }

            if(!currentTest.test(subHolder))
                return false;
        }

        return true;
    }

    public void setPredictsFromJSON(JsonArray predictsArray){
        for (JsonElement current : predictsArray) {
            if (current.isJsonObject()) {
                JsonObject currentObject = current.getAsJsonObject();
                if (!currentObject.has(("name")))
                    continue;

                String name = currentObject.get("name").getAsString();
                if (!GeometryParts.GEOMETRIC_TESTS.containsKey(name))
                    continue;

                IGeometricTest test = GeometryParts.GEOMETRIC_TESTS.get(name).get();
                if (currentObject.has("parameters") && currentObject.get("parameters").isJsonObject()) {
                    test.fromJSON(currentObject.get("parameters").getAsJsonObject());
                }
                parts.add(test);
            }
        }
    }

    public void setParts(List<IGeometricTest> parts) {
        this.parts = parts;
    }

    public static class ShapeDataSerializer implements JsonDeserializer<ShapeData>, JsonSerializer<ShapeData> {


        @Override
        public ShapeData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if(!json.isJsonObject())
                throw new JsonParseException("ShapeData needs to be a JsonObject");
            JsonObject obj = json.getAsJsonObject();
            if(!obj.has("name"))
                throw new JsonParseException("ShapeData needs to have a name");
            String name = obj.get("name").getAsString();
            if(!obj.has("points"))
                throw new JsonParseException("ShapeData needs to have a number of points");
            int numberOfPoints = obj.get("points").getAsInt();
            ShapeData data = new ShapeData(name, numberOfPoints);
            if(obj.has("predicts") && obj.get("predicts").isJsonArray()){
                JsonArray predictsArray = obj.get("predicts").getAsJsonArray();
                for(JsonElement currentElement : predictsArray){
                    if(!currentElement.isJsonArray())
                        continue;

                    JsonObject current = currentElement.getAsJsonObject();
                    if(!current.has("name"))
                        continue;

                    String cName = current.get("name").getAsString();
                    if(!GeometryParts.GEOMETRIC_TESTS.containsKey(cName))
                        continue;

                    IGeometricTest test = GeometryParts.GEOMETRIC_TESTS.get(cName).get();
                    if(current.has("parameters") && current.get("parameters").isJsonObject()){
                        test.fromJSON(current.get("parameters").getAsJsonObject());
                    }
                    data.parts.add(test);
                }
            }
            return data;
        }

        @Override
        public JsonElement serialize(ShapeData src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject root = new JsonObject();
            root.addProperty("name", src.name);
            root.addProperty("points", src.numberOfPoints);
            JsonArray predictsArray = new JsonArray();
            for(IGeometricTest current : src.parts){
                predictsArray.add(current.toJSON());
            }
            root.add("predicts", predictsArray);
            return root;
        }
    }
}
