package com.disksfreespace;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Disk {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;
    private String diskName;
    private long freeSpace;
    public Disk(String diskName, long freeSpace) {
        this.diskName = diskName;
        this.freeSpace = freeSpace;
    }
    public Disk() {
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getDiskName() {
        return diskName;
    }
    public void setDiskName(String diskName) {
        this.diskName = diskName;
    }
    public long getFreeSpace() {
        return freeSpace;
    }
    public void setFreeSpace(long freeSpace) {
        this.freeSpace = freeSpace;
    }
    @Override
    public String toString() {
        return "Disk [id=" + id + ", diskName=" + diskName + ", freeSpace=" + freeSpace + "]";
    }
    

    
}
