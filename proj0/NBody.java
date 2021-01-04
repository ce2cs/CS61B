import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class NBody
{
    public static double readRadius(String fileName)
    {
        try
        {
            File file = new File(fileName);
            Scanner reader = new Scanner(file);
            reader.nextLine();
            String radiusString = reader.nextLine();
            double radius = Double.parseDouble(radiusString);
            return radius;
        }
        catch (FileNotFoundException e)
        {
            System.out.println("Data file does not exist.");
            e.printStackTrace();
            return 0;
        }
    }
}
