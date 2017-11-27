package sample;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class DB {

    private static String jsonFileLocation = "src/sample/animalData.json";

    public DB(){
    }

    public static ArrayList<Animal> getCurrentAnimals() {
        org.json.simple.parser.JSONParser parser = new org.json.simple.parser.JSONParser();
        ArrayList<Animal> animals = new ArrayList<Animal>();
        try (FileReader reader = new FileReader(jsonFileLocation)) {
            Object obj = parser.parse(reader);
            JSONArray currentAnimals = (JSONArray) obj;
            animals = extractAnimals(currentAnimals);
        }
        catch (IOException e) {
            System.out.println("# IOException DB.java " + e);
        }
        catch(org.json.simple.parser.ParseException e){
            System.out.println("# ParserException DB.java " + e);
        }
        return animals;
    }

    public static ArrayList<Animal> extractAnimals(JSONArray animalData){
        ArrayList<Animal> animals = new ArrayList<Animal>();
        for(int i = 0;i <= animalData.size() - 1;i++){
            JSONObject animal = (JSONObject) animalData.get(i);
            String type = (String) animal.get("type");
            int currentStatus = Integer.parseInt((String) animal.get("status"));
            int feedingTime = Integer.parseInt((String) animal.get("feedingtime"));
            String hStatus = (String) animal.get("hstatus");
            String src = (String) animal.get("path");
            if(hStatus.equals("true")){
                Animal newAnimal = new Animal(type,feedingTime,currentStatus,true);
                animals.add(newAnimal);
                newAnimal.setImagePath(src);
            }
            else{
                Animal newAnimal = new Animal(type,feedingTime,currentStatus,false);
                animals.add(newAnimal);
                newAnimal.setImagePath(src);
            }
        }
        return animals;
    }

    public static void setCurrentAnimals(ArrayList<Animal> newAnimals) {
        ArrayList<JSONObject> animalDataInJSON = convertToJSON(newAnimals);
        JSONParser parser = new JSONParser();
        try(FileWriter writer = new FileWriter(jsonFileLocation)){
            JSONArray newJSONAnimals = new JSONArray();
            for(JSONObject animal : animalDataInJSON){
                newJSONAnimals.add(animal);
            }
            writer.write(newJSONAnimals.toJSONString());
            writer.flush();
        }
        catch(IOException e){
            System.out.println("# IOException DB.java " + e);
        }
    }

    public static ArrayList<JSONObject> convertToJSON(ArrayList<Animal> newAnimals) {
        ArrayList<JSONObject> animalDataInJSON = new ArrayList<JSONObject>();
        for (Animal animal : newAnimals) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", animal.getAnimalType());
            jsonObject.put("status", "" + animal.getCurrentStatus());
            jsonObject.put("feedingtime", "" + animal.getFeedingTime());
            jsonObject.put("path",animal.getImagePath());
            if (animal.getHungerStatus()) {
                jsonObject.put("hstatus", "true");
            } else {
                jsonObject.put("hstatus", "false");
            }
            animalDataInJSON.add(jsonObject);
        }
        return animalDataInJSON;
    }

}
