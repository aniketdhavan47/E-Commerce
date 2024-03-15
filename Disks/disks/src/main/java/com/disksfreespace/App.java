
package com.disksfreespace;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.util.regex.Matcher;

public class App {
    public static ArrayList<Disk> calaculateFreeSpace(int exitCode, Process process)
            throws NumberFormatException, IOException, InterruptedException {

        ArrayList<Disk> disks = new ArrayList<>();

        if (exitCode == 0) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String str;

            while ((str = reader.readLine()) != null) {
                String regex = "\\s*(\\d+)\\s+(\\S+)";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(str);
                while (matcher.find()) {
                    String avail = matcher.group(1);
                    String filesystem = matcher.group(2);
                    disks.add(new Disk(filesystem, Long.parseLong(avail)));
                }
            }

        }
        return disks;
    }

    public static Disk calculateMaximumFreeSpace() throws IOException, NumberFormatException, InterruptedException {
        Process process = Runtime.getRuntime().exec(" df --output=avail,source");

        ArrayList<Disk> disks = calaculateFreeSpace(process.waitFor(), process);

        String diskName = "";
        Long maxFreeSpace = (long) 0;

        for (Disk d1 : disks) {
            if (d1.getFreeSpace() > maxFreeSpace) {
                maxFreeSpace = d1.getFreeSpace();
                diskName = d1.getDiskName();
            }
        }
        return new Disk(diskName, maxFreeSpace);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        EntityManager em = emf.createEntityManager();

        ScheduledExecutorService service = Executors.newScheduledThreadPool(1);

        service.scheduleAtFixedRate(() -> {
            try {
                Disk d1 = calculateMaximumFreeSpace();
                System.out.println(d1);
                em.getTransaction().begin();
                em.persist(d1);
                em.getTransaction().commit();

            } catch (NumberFormatException | IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }, 10, 10, TimeUnit.SECONDS);

    }
}
