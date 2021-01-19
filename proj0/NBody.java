import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.io.LineNumberReader;
import java.util.Arrays;
import java.util.ArrayList;

public class NBody
{
    private static int NUM_OF_DOUBLE= 5;
    private static int INFO_LINEG= 3;
    private static String IMAGE_PATH = "images/";
    public static double readRadius(String fileName)
    {
        File file = null;
        Scanner reader = null;
        try
        {
            file = new File(fileName);
            reader = new Scanner(file);
        }
        catch (FileNotFoundException e)
        {
            System.out.println("Data file does not exist.");
            e.printStackTrace();
        }
        In stream = new In(reader);
        stream.readInt();
        double radius = stream.readDouble();
        return radius;
    }
    
    public static Body[] readBodies(String fileName)
    {
        File file = null;
        Scanner reader = null;
        try
        {
            file = new File(fileName);
            reader = new Scanner(file);
        }
        catch (FileNotFoundException e)
        {
            System.out.println("Data file does not exist.");
            e.printStackTrace();
        }
        In stream = new In(reader);

        int numOfBodies= stream.readInt();
        Body[] bodies = new Body[numOfBodies];
        stream.readDouble();
        
        for (int i = 0; i<numOfBodies; i++)
        {
            double xxPos = stream.readDouble();
            double yyPos = stream.readDouble();
            double xVel = stream.readDouble();
            double yVel = stream.readDouble();
            double mass = stream.readDouble();
            String image = stream.readString();
            bodies[i] = new Body(
                    xxPos,
                    yyPos,
                    xVel,
                    yVel,
                    mass,
                    image
                    );
        }
        return bodies;
    }
    
    public static void main(String args[])
    {
        double T = 0.0;
        double dt = 0.0;
        double currentTime = 0.0;
        String fileName = "";

        T = Double.parseDouble(args[0]);
        dt = Double.parseDouble(args[1]);
        fileName = args[2];
        double radius = readRadius(fileName);
        Body[] bodies = readBodies(fileName);
        double[] xForces = new double[bodies.length];
        double[] yForces = new double[bodies.length];

        StdDraw.enableDoubleBuffering();
        StdDraw.setScale(-radius, radius);
        for (double t = 0.0; t < T; t+=dt)
        {
            StdDraw.clear();
            StdDraw.picture(0, 0, IMAGE_PATH + "starfield.jpg");
            for (int i = 0; i < bodies.length; i++)
            {
                xForces[i] = bodies[i].calcNetForceExertedByX(bodies);
                yForces[i] = bodies[i].calcNetForceExertedByY(bodies);

            }
            for (int i = 0; i < bodies.length; i++)
            {
                bodies[i].update(dt, xForces[i], yForces[i]);
                bodies[i].draw();
            }
            StdDraw.show();
            StdDraw.pause(10);
        }
        StdOut.printf("%d\n", bodies.length);
        StdOut.printf("%.2e\n", radius);
        for (int i = 0; i < bodies.length; i++) 
        {
            StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
                bodies[i].xxPos, bodies[i].yyPos, bodies[i].xxVel,
                bodies[i].yyVel, bodies[i].mass, bodies[i].imgFileName);
        }
    }
}
