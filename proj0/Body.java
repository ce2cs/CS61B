public class Body {

    public static double G = 6.67e-11;
    public double xxPos;
    public double yyPos;
    public double xxVel;
    public double yyVel;
    public double mass;
    public String imgFileName;
    
    public Body(double xP, double yP, double xV, double yV, double mass, String imgFileName)
    {
        this.xxPos = xP;
        this.yyPos = yP;
        this.xxVel = xV;
        this.yyVel = yV;
        this.mass = mass;
        this.imgFileName = imgFileName;
    }
    
    public Body(Body b)
    {
        this.xxPos = b.xxPos;
        this.yyPos = b.yyPos;
        this.xxVel = b.xxVel;
        this.yyVel = b.yyVel;
        this.mass = b.mass;
        this.imgFileName = b.imgFileName;
    }

    public double calcDistance(Body b)
    {
        double distance;

        distance = Math.hypot((b.xxPos - this.xxPos), (b.yyPos - this.yyPos));
        return distance;
    }
    
    public double calcForceExertedBy(Body b)
    {
        double force;

        if (this.equals(b))
        {
            return 0;
        }
        force = G * this.mass * b.mass / Math.pow(this.calcDistance(b), 2);
        return force;
    }
    
    public double calcForceExertedByX(Body b)
    {
        double forceX;
        double dx;
        double forceNet;

        if (this.equals(b))
        {
            return 0;
        }
        dx = b.xxPos - this.xxPos;
        forceX = this.calcForceExertedBy(b) * dx / this.calcDistance(b);
        return forceX;
    }

    public double calcForceExertedByY(Body b)
    {
        double forceY;
        double dy;

        if (this.equals(b))
        {
            return 0;
        }
        dy = b.yyPos - this.yyPos;
        forceY = this.calcForceExertedBy(b) * dy / this.calcDistance(b);
        return forceY;
    }
    
    public double calcNetForceExertedByX(Body allBodys[])
    {
        double forceX = 0;
        
        for (Body b: allBodys)
        {
            forceX = forceX + this.calcForceExertedByX(b);
        }
        return forceX;
    }

    public double calcNetForceExertedByY(Body allBodys[])
    {
        double forceY = 0;

        for (Body b: allBodys)
        {
            forceY = forceY + this.calcForceExertedByY(b);
        }
        return forceY;
    }
    
    public void update(double dt, double fX, double fY)
    {
        double aX = 0;
        double aY = 0;
        
        aX = fX / this.mass;
        aY = fY / this.mass;
        this.xxVel = aX * dt + this.xxVel;
        this.yyVel = aY * dt + this.yyVel;
        this.xxPos = this.xxVel * dt + this.xxPos;
        this.yyPos = this.yyVel * dt + this.yyPos;
    }

    public static boolean checkIfEqual(Body a, Body b)
    {
        if (a.xxPos == b.xxPos & a.yyPos == b.yyPos)
        {
            return true;
        }
        return false;
    }
    
    public boolean equals(Body b)
    {
        return Body.checkIfEqual(this, b);
    }
    
}





