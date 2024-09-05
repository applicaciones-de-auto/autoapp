/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package org.guanzon.autoapp;

import javafx.application.Application;
import org.guanzon.appdriver.base.GRider;

/**
 *
 * @author User
 */
public class LetMeInn {

    public static void main(String[] args) {
        String path;
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            path = "D:/GGC_Maven_Systems";
        } else {
            path = "/srv/GGC_Maven_Systems";
        }
        System.setProperty("sys.default.path.config", path);
        System.setProperty("sys.default.path.metadata", "D:/GGC_Maven_Systems/config/metadata/");

        GRider instance = new GRider("gRider");
        // Use the instance

        if (!instance.logUser("gRider", "M001000001")) {
            System.err.println(instance.getErrMsg());
            System.exit(1);
        }

        System.out.println("Connected");

        Autoapp instance_ui = new Autoapp();
        instance_ui.setGRider(instance);
        Application.launch(instance_ui.getClass());
    }

}
