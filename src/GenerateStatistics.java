import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GenerateStatistics {

    public static void main(String args[]){

        generateStatistics();

    }

    public static void generateStatistics(){

        Map<String,Double> resultsTemperature = new HashMap<>();
        resultsTemperature.put("maxTemperature",0.0);
        resultsTemperature.put("minTemperature",0.0);
        resultsTemperature.put("meanTemperature",0.0);

        Map<String,Integer> observationsEachObservatory = new HashMap<>();
        observationsEachObservatory.put("AU",0);
        observationsEachObservatory.put("US",0);
        observationsEachObservatory.put("FR",0);
        observationsEachObservatory.put("All Others",0);

        Map<String,Object> distanceCalc = new HashMap<>();
        distanceCalc.put("totalDistance","0");

        List<String> fileLines = new ArrayList<>();

        try{

            if(!HelperService.checkExistFile("data.txt")){

                System.out.println("Data file not found");
                return;

            }

            List<Path> dataFile = Files.walk(Paths.get(""), new FileVisitOption[]{}).
                    filter(file -> Files.isRegularFile(file)).filter(file -> file.toString().contains("data.txt"))
                    .collect(Collectors.toList());

            fileLines = Files.lines(dataFile.get(0)).collect(Collectors.toList());

            fileLines.forEach(line ->{

                String[] content = line.split("\\|");

                if(content[0]!=null&&!content[0].equals("id")) {

                    observationsEachObservatory.put(content[4],observationsEachObservatory.get(content[4])+1);

                    if(distanceCalc.get("lastPoints")!=null){

                        String[] lastPoints = distanceCalc.get("lastPoints").toString().split("\\,");
                        Double x1 = new Double(lastPoints[0]);
                        Double x2 = new Double(lastPoints[1]);

                        String[] points = content[2].split("\\,");
                        Double y1 = new Double(points[0]);
                        Double y2 = new Double(points[1]);

                        double disc = Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));

                        double currentDistance = new Double(distanceCalc.get("totalDistance").toString());

                        distanceCalc.put("totalDistance",currentDistance+disc);

                    }

                    distanceCalc.put("lastPoints",content[2]);

                    Double temperature = new Double(content[3]);
                    if (content[4].equals("US")) {

                        temperature = ((temperature - 32) * 5) / 9;

                    } else if (content[4].equals("FR") || content[4].equals("All Others")) {

                        temperature = temperature - 273.15F;

                    }

                    if(temperature<resultsTemperature.get("minTemperature")){
                        resultsTemperature.put("minTemperature",temperature);
                    }

                    if(temperature>resultsTemperature.get("maxTemperature")){
                        resultsTemperature.put("maxTemperature",temperature);
                    }

                    Double temperaturaMedia = resultsTemperature.get("meanTemperature");
                    temperaturaMedia += temperature;
                    resultsTemperature.put("meanTemperature",temperaturaMedia);

                }

            });

        }catch(Exception e){

            e.printStackTrace();

        }

        NumberFormat formatter = new DecimalFormat("#0.00");

        System.out.println("The minimum temperature: "+formatter.format(resultsTemperature.get("minTemperature")));
        System.out.println("The maximum temperature: "+formatter.format(resultsTemperature.get("maxTemperature")));
        System.out.println("The mean temperature: "+formatter.format((resultsTemperature.get("meanTemperature") / fileLines.size()-1)));
        System.out.println("The number of observations from each observatory: "+observationsEachObservatory.toString());
        System.out.println("The total distance travelled: "+formatter.format(distanceCalc.get("totalDistance")));

    }

}
