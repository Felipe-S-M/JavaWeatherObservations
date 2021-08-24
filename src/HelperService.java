import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class HelperService {

    public static boolean checkExistFile(String fileName) throws Exception{

        List<Path> dataFile = Files.walk(Paths.get(""), new FileVisitOption[]{}).
                filter(file -> Files.isRegularFile(file)).filter(file -> file.toString().contains(fileName))
                .collect(Collectors.toList());

        if(dataFile.isEmpty()){
            return false;
        }

        return true;

    }

}
