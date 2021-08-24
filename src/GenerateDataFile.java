import java.io.File;
import java.io.FileWriter;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class GenerateDataFile {

    static Random random = new Random();

    static public void main(String... args){

        generateFile();

    }

    public static void generateFile(){

        try {

            Path pathToFile = Paths.get("");
            File file;
            if(HelperService.checkExistFile("data.txt")){
                file = new File(pathToFile.toAbsolutePath()+"/data.txt");
                file.delete();
            }

            file = new File(pathToFile.toAbsolutePath()+"/data.txt");
            file.createNewFile();

            FileWriter fw = new FileWriter(file);
            StringBuilder content = new StringBuilder();
            content.append("id|timestamp|location|temperature|observatory\n");
            fw.write(content.toString());

            for (int i = 0; i < 50_000; i++) {

                content.setLength(0);

                Date d1 = new SimpleDateFormat("dd/MM/yyyy").parse("30/12/1999");
                Date d2 = new SimpleDateFormat("dd/MM/yyyy").parse("30/12/2020");

                Date randomDate = new Date(ThreadLocalRandom.current()
                        .nextLong(d1.getTime(), d2.getTime()));

                SimpleDateFormat isoFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

                String timestamp = isoFormatter.format(randomDate);
                String observatory;
                StringBuilder temperature = new StringBuilder();
                StringBuilder location = new StringBuilder();

                int observatoryChance = random.nextInt(100 );
                if(observatoryChance<=25){

                    observatory = "AU";
                    generateObservatoryData(temperature,-88,58,location,-38.168786,145.366598,false);

                }else if(observatoryChance>25&&observatoryChance<=50){
                    observatory = "US";
                    generateObservatoryData(temperature,-460,212,location,26.007706,-80.388898,false);

                }else if(observatoryChance>50&&observatoryChance<=75){
                    observatory = "FR";
                    generateObservatoryData(temperature,0,373,location,45.599727,2.609266,false);
                }else{
                    observatory = "All Others";
                    generateObservatoryData(temperature,0,373,location,0.0,0.0,true);
                }

                content.append(i+"|"+timestamp+"|"+location+"|"+temperature+"|"+observatory+"\n");
                fw.write(content.toString());

            }

            fw.close();

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public static void generateObservatoryData(StringBuilder temperature, Integer minTemperature,Integer maxTemperature,StringBuilder location,Double pointX,Double pointY,Boolean generateRandomLocation){

        temperature.append(random.nextInt(maxTemperature - minTemperature) + minTemperature);
        if(generateRandomLocation){
            location.append(( (-100) + (100 - (-100)) * random.nextDouble())+","+((-100) + (100 - (-100)) * random.nextDouble()));
        }else{
            location.append(pointX+","+pointY);
        }

    }

}