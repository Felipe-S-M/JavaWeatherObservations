import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Collectors;

public class GenerateOutputfilteredFile {

    public static void main(String[] args){

        try {

            generateFilteredFile();

        }catch (Exception e){

            e.printStackTrace();

        }

    }

    public static void generateFilteredFile() throws Exception{


        if(!HelperService.checkExistFile("data.txt")){

            System.out.println("Data file not found");
            return;

        }

        Path pathToFile = Paths.get("");
        if(HelperService.checkExistFile("dataFiltered.txt")){
            File file = new File(pathToFile.toAbsolutePath()+"/dataFiltered.txt");
            file.delete();
        }

        String temperatureScale;
        String distanceScale;
        Scanner in = new Scanner(System.in);

        while(true){

            System.out.println("Select temperature scale: ");
            System.out.println("1 - Celsius");
            System.out.println("2 - Fahrenheit");
            System.out.println("3 - Kelvin");

            temperatureScale = in.next();
            if(temperatureScale.equals("1")||temperatureScale.equals("2")||temperatureScale.equals("3")){

                break;

            }else{

                System.out.println("invalid scale");

            }

        }

        while(true){

            System.out.println("Select distance scale: ");
            System.out.println("1 - Km");
            System.out.println("2 - Miles");
            System.out.println("3 - Meters");

            distanceScale = in.next();
            if(distanceScale.equals("1")||distanceScale.equals("2")||distanceScale.equals("3")){

                break;

            }else{

                System.out.println("invalid scale");

            }

        }

        File file = new File(pathToFile.toAbsolutePath()+"/dataFiltered.txt");
        file.createNewFile();

        FileWriter fw = new FileWriter(file);
        StringBuilder content = new StringBuilder();

        content.append("id|timestamp|location|temperature|observatory|distanceFromLastObservation\n");
        fw.write(content.toString());

        File dataFile = new File(pathToFile.toAbsolutePath()+"/data.txt");
        List<String> fileLines = Files.lines(dataFile.toPath()).collect(Collectors.toList());

        Map<String,String> map = new HashMap<>();
        map.put("temperatureScale",temperatureScale);
        map.put("distanceScale",distanceScale);

        NumberFormat formatter = new DecimalFormat("#0.00");

        fileLines.forEach(line -> {

            content.setLength(0);

            String[] fileContent = line.split("\\|");

            if(!fileContent[0].equals("id")){

                Integer id = new Integer(fileContent[0]);
                String timestamp = fileContent[1];
                String location = fileContent[2];
                Double temperature = new Double(fileContent[3]);
                String observatory = fileContent[4];

                Double distanciaFromLastPoin = 0.0;

                if(map.get("lastPoints") != null){

                    String[] lastPoints = map.get("lastPoints").split("\\,");
                    Double x1 = new Double(lastPoints[0]);
                    Double x2 = new Double(lastPoints[1]);

                    String[] points = fileContent[2].split("\\,");
                    Double y1 = new Double(points[0]);
                    Double y2 = new Double(points[1]);

                    distanciaFromLastPoin = Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));

                }

                map.put("lastPoints", fileContent[2]);

                if(map.get("temperatureScale").equals("1")){

                    if(observatory.equals("US")){

                        temperature = ((temperature - 32) * 5) / 9;

                    }else if(observatory.equals("FR") || observatory.equals("All Others")){

                        temperature = temperature - 273.15F;

                    }

                }else if(map.get("temperatureScale").equals("2")){

                    if(observatory.equals("AU")){

                        temperature = (9 / 5) * temperature + 32;

                    }else if(observatory.equals("FR") || observatory.equals("All Others")){

                        temperature = temperature * 1.8 - 459.67;

                    }

                }else if(map.get("temperatureScale").equals("3")){

                    if(observatory.equals("US")){

                        temperature = 273.5 + ((temperature - 32.0) * (5.0 / 9.0));

                    }else if(observatory.equals("AU")){

                        temperature = temperature + 273.15;

                    }

                }

                if(map.get("distanceScale").equals("2")){

                    double conversionFactor = 1.609344;
                    distanciaFromLastPoin = distanciaFromLastPoin / conversionFactor;


                }else if(map.get("distanceScale").equals("3")){

                    distanciaFromLastPoin = distanciaFromLastPoin * 1000;

                }

                content.append(id + "|" + timestamp + "|" + location + "|" + formatter.format(temperature) + "|" + observatory + "|" + formatter.format(distanciaFromLastPoin) + "\n");

                try {

                    fw.write(content.toString());

                }catch (Exception e){

                    e.printStackTrace();

                }

            }

        });

        fw.close();

    }

}
