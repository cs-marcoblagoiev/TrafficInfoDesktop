import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelReader;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.scene.image.Image;


public class Main extends Application {
    public static WebView myWebView = new WebView();
    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        WebEngine engine = myWebView.getEngine();

        loadMap(engine);

        Button btn = new Button("Load Maps");
        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                loadMap(engine);

            }
        });

        Button btn2 = new Button("Take Snapshot");
        btn2.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                new java.util.Timer().schedule(
                        new java.util.TimerTask() {
                            @Override
                            public void run() {

                                Platform.runLater(new Runnable() {
                                    @Override public void run() {
                                            analyzeMap(myWebView);
                                    }
                                });

                            }
                        },
                        0, 5000
                );
            }
        });


        engine.setUserAgent("Mozilla/5.0 (Linux; U; Android 2.2.1; en-us; Nexus One Build/FRG83) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1");


        VBox root = new VBox();
        root.getChildren().addAll(myWebView, btn, btn2);

        Scene scene = new Scene(root, 500, 550);
        stage.setScene(scene);

        stage.show();
    }

    private void analyzeMap(WebView myWebView ){
        String green = "0x63d668ff";
        String orange = "0xff974dff";
        String red = "0xf23c32ff";
        String darkRed = "0x811f1fff";

        int greenCount = 0, orangeCount = 0, redCount = 0, darkRedCount = 0, totalTrafficPixels = 0;

        Image img = myWebView.snapshot(null, null);

        System.out.printf("img.width: %s    height: %s%n", img.getWidth(), img.getHeight());

        PixelReader pr = img.getPixelReader();
        long count = 0;


        for (int i = 0; i < img.getHeight(); i++) {
            for (int j = 0; j < img.getWidth(); j++) {
                // System.out.println(pr.getColor(j, i).toString());
                count++;
                if (pr.getColor(j, i).toString().equals(green)) {
                    greenCount++;
                    totalTrafficPixels++;
                } else if (pr.getColor(j, i).toString().equals(orange)) {
                    orangeCount++;
                    totalTrafficPixels++;
                } else if (pr.getColor(j, i).toString().equals(red)) {
                    redCount++;
                    totalTrafficPixels++;
                } else if (pr.getColor(j, i).toString().equals(darkRed)) {
                    darkRedCount++;
                    totalTrafficPixels++;
                }
            }
        }

        System.out.println(greenCount + " " + orangeCount + " " + redCount + " " + darkRedCount + " " + count + " " + totalTrafficPixels);

        double percentageGreen = 0, percentageOrange = 0, percentageRed = 0, percentageDarkRed = 0;
        percentageGreen = (double)(greenCount * 100) / totalTrafficPixels;
        percentageOrange = (double)(orangeCount * 100) / totalTrafficPixels;
        percentageRed = (double)(redCount * 100) / totalTrafficPixels;
        percentageDarkRed = (double)(darkRedCount * 100) / totalTrafficPixels;

        System.out.println(percentageGreen + " " + percentageOrange + " " + percentageRed + " " + percentageDarkRed);

        double index = (percentageOrange + (2 * percentageRed) + (3 * percentageDarkRed));

        System.out.printf("%.2f", index);
        System.out.println();
    }



    private void loadMap(WebEngine engine){
        engine.loadContent("<!DOCTYPE html>\n" +
                "<html>\n" +
                "  <head>\n" +
                "    <meta name=\"viewport\" content=\"initial-scale=1.0, user-scalable=no\">\n" +
                "    <meta charset=\"utf-8\">\n" +
                "    <title>Traffic layer</title>\n" +
                "    <style>\n" +
                "      /* Always set the map height explicitly to define the size of the div\n" +
                "       * element that contains the map. */\n" +
                "      #map {\n" +
                "        height: 100%;\n" +
                "      }\n" +
                "      /* Optional: Makes the sample page fill the window. */\n" +
                "      html, body {\n" +
                "        height: 100%;\n" +
                "        margin: 0;\n" +
                "        padding: 0;\n" +
                "      }\n" +
                "    </style>\n" +
                "  </head>\n" +
                "  <body>\n" +
                "    <div id=\"map\"></div>\n" +
                "    <script>\n" +
                "      function initMap() {\n" +
                "        var map = new google.maps.Map(document.getElementById('map'), {\n" +
                "          zoom: 19,\n" +
                "          center: {lat: 45.753837, lng: 21.224253}\n" +
                "        });\n" +
                "\n" +
                "        var trafficLayer = new google.maps.TrafficLayer();\n" +
                "        trafficLayer.setMap(map);\n" +
                "      }\n" +
                "    </script>\n" +
                "    <script async defer\n" +
                "    src=\"https://maps.googleapis.com/maps/api/js?key=AIzaSyDr9JbOAvUE_Ab7wKW_PWDIPQtp1U-Jfmg&callback=initMap\">\n" +
                "    </script>\n" +
                "  </body>\n" +
                "</html>");
    }


}
