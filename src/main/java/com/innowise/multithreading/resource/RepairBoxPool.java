package com.innowise.multithreading.resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RepairBoxPool {

    private static final Logger log = LogManager.getLogger(RepairBoxPool.class);

    private final Semaphore semaphore;
    private final Deque<Integer> freeBoxIds;
    private final Lock idLock = new ReentrantLock();

    public RepairBoxPool(int boxCount) {
        this.semaphore = new Semaphore(boxCount, true);
        this.freeBoxIds = new ArrayDeque<>();
        for (int i = 1; i <= boxCount; i++) {
            freeBoxIds.push(i);
        }
        log.info("RepairBoxPool initialized with {} boxes", boxCount);
    }

    public int acquireBox() throws InterruptedException {
        semaphore.acquire();
        idLock.lock();
        try {
            return freeBoxIds.pop();
        } finally {
            idLock.unlock();
        }
    }

    public void releaseBox(int boxId) {
        idLock.lock();
        try {
            freeBoxIds.push(boxId);
        } finally {
            idLock.unlock();
        }
        semaphore.release();
        log.info("Box #{} released", boxId);
    }
}