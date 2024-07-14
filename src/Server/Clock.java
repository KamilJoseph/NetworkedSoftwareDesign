package Server;

public class Clock
{
    private int timeStamp;

    public Clock(){timeStamp=0;}

    public synchronized int tick() {return ++timeStamp;}
}
