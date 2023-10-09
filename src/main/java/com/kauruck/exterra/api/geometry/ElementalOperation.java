package com.kauruck.exterra.api.geometry;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kauruck.exterra.geometry.GeometryParts;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public abstract class ElementalOperation implements IGeometricTest {

    protected List<IGeometricTest> parts = new ArrayList<>();
    private Character[] expectedBlockPosNames;



    @Override
    public void fromJSON(JsonObject json) {
        if(json.has("predicts") && json.get("predicts").isJsonArray()){
            JsonArray partsArray = json.getAsJsonArray("predicts");
            for (JsonElement current : partsArray) {
                if(current.isJsonObject()){
                    JsonObject currentObject = current.getAsJsonObject();
                    if(!currentObject.has(("name")))
                        continue;

                    ResourceLocation name = new ResourceLocation(currentObject.get("name").getAsString());
                    if(!GeometryParts.GEOMETRIC_TESTS.containsKey(name))
                        continue;

                    IGeometricTest test = GeometryParts.GEOMETRIC_TESTS.get(name).get();
                    if(currentObject.has("parameters") && currentObject.get("parameters").isJsonObject()){
                        test.fromJSON(currentObject.get("parameters").getAsJsonObject());
                    }
                }
            }
        }

        this.expectedBlockPosNames = generateExpectedBlockPoses();
    }

    @Override
    public JsonObject toJSON() {
        JsonObject out = new JsonObject();
        out.addProperty("name", this.getName().toString());
        JsonArray predictsArray = new JsonArray();
        parts.forEach((part) -> predictsArray.add(part.toJSON()));
        out.add("predicts", predictsArray);
        return out;
    }

    private Character[] generateExpectedBlockPoses(){
        List<Character> characters = new ArrayList<>();
        for(IGeometricTest current : parts){
            for(Character currentChar : current.expectedBlockPosNames()){
                if(!characters.contains(currentChar))
                    characters.add(currentChar);
            }
        }
        return characters.toArray(new Character[0]);
    }

    @Override
    public Character[] expectedBlockPosNames() {
        return expectedBlockPosNames;
    }

    public List<IGeometricTest> getParts() {
        return parts;
    }

    public void setParts(List<IGeometricTest> parts) {
        this.parts = parts;
    }
}
