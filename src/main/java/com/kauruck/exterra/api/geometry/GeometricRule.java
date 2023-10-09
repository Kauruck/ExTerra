package com.kauruck.exterra.api.geometry;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.StreamSupport;

public abstract class GeometricRule implements IGeometricTest{

    protected Character[] expectedBlockPos;
    private final String jsonName;
    protected float epsilon = 0.5f;

    protected GeometricRule(String jsonName){
        this.jsonName = jsonName;
    }

    @Override
    public void fromJSON(JsonObject json) {
        for(Map.Entry<String, JsonElement> object : json.entrySet()){
            if(object.getKey().equalsIgnoreCase("epsilon")) {
                this.setEpsilon(object.getValue().getAsFloat());
            }
            else if(object.getKey().equalsIgnoreCase("pos")) {
                if(object.getValue().isJsonArray()) {
                    JsonArray posArray = object.getValue().getAsJsonArray();
                    this.expectedBlockPos = StreamSupport.stream(posArray.spliterator(), true)
                            .map(JsonElement::getAsCharacter)
                            .toArray(Character[]::new);
                }
            }
            else {
                this.offerParameter(object.getKey(), object.getValue());
            }
        }
    }



    @Override
    public JsonObject toJSON() {
        JsonObject out = new JsonObject();
        out.addProperty("name", this.getName().toString());
        out.addProperty("epsilon", this.epsilon);
        JsonArray positionsArray = new JsonArray();
        Arrays.stream(expectedBlockPos)
                .forEach(positionsArray::add);
        Map<String, JsonElement> parameters = this.getParameters();
        JsonObject parameterObject = new JsonObject();
        for(String currentParameter : parameters.keySet()){
            parameterObject.add(currentParameter, parameters.get(currentParameter));
        }
        parameterObject.add("pos", positionsArray);

        out.add("parameters", parameterObject);
        return out;
    }

    protected abstract Map<String, JsonElement> getParameters();

    public abstract void offerParameter(String parameterName, JsonElement jsonElement);

    public Character[] expectedBlockPosNames(){
        return expectedBlockPos;
    }

    public String getJSONName(){
        return jsonName;
    }

    public float getEpsilon() {
        return epsilon;
    }

    public void setEpsilon(float epsilon) {
        this.epsilon = epsilon;
    }
}
