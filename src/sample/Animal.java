package sample;

import java.util.Timer;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class Animal {
    private String animalType;
    private String imagePath;
    private int feedingTime;
    private int currentStatus;
    private Timer foodTime = new Timer();
    private boolean hunger;

    public Animal(String type,int time,int status,boolean hStatus){
        animalType = type;
        feedingTime = time;
        currentStatus = status;
        hunger = hStatus;
    }

    public String getImagePath(){
        return imagePath;
    }

    public void setImagePath(String path){
        imagePath = path;
    }

    public Timer getFoodTimer(){
        return foodTime;
    }

    public String getAnimalType(){
        return animalType;
    }

    public int getFeedingTime(){
        return feedingTime;
    }

    public int getCurrentStatus(){
        return currentStatus;
    }

    public boolean getHungerStatus(){
        return hunger;
    }

    public void setHungerStatus(boolean status){
        hunger = status;
    }

    public void setCurrentStatus(int current){
        currentStatus = current;
    }
}
