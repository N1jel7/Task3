package com.innowise.multithreading.state;

import com.innowise.multithreading.entity.Car;

public interface CarState {

    CarState handle(Car car) throws InterruptedException;

    boolean isFinal();

}
