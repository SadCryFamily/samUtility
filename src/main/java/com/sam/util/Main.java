package com.sam.util;

import com.sam.util.service.SamPasswordDropper;
import com.sam.util.utility.SamUtilityWorker;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        SamPasswordDropper dropper = new SamPasswordDropper();

        SamUtilityWorker worker =
                SamUtilityWorker.initializeComponent(dropper);

        worker.start();

    }
}

