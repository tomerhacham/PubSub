package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.Agent;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.Squad;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SquadTest {
    @BeforeEach
    public void setUp(){
        Agent[] agents = new Agent[]{new Agent("007","James Bond") , new Agent("006","Alec Trevelyan")};
        Squad squad =  Squad.getInstance();
        squad.load(agents);
    }

    @Test
    public void test(){
        Squad squadInstance =  Squad.getInstance();
        assertNotNull(squadInstance,"Squad is Null");
        assertSame(squadInstance,Inventory.getInstance(),"Squads are not the same Object");
    }

    @Test
    public void getAgentTest(){
        Squad squad = Squad.getInstance();
        Agent sagi = new Agent("203333554","Mjr. Sagi Dahan");
        Agent tomer = new Agent("313332728","Mjr. Tomer Hacham");
        LinkedList<String> serials = new LinkedList<String>();
        serials.add("203333554");
        assertTrue(squad.getAgents(serials),"Agent is missing");
        assertFalse(sagi.isAvailable());
        assertTrue(tomer.isAvailable());

    }

    @Test
    public void getAgentsNamesTest(){
        Squad squad = Squad.getInstance();
        LinkedList<String> serials = new LinkedList<String>();
        serials.add("007");
        serials.add("006");
        List<String> names = squad.getAgentsNames(serials);
        assertEquals(names.get(0),"James Bond","James Bond is not the name");
        assertEquals(names.get(1),"Alec Trevelyan","Alec Trevelyan is not the name");
    }

    @Test
    public void releaseAgents(){
        Agent agent = new Agent("203333554","Mjr. Sagi Dahan");
        agent.acquire();
        Agent[] agents = new Agent[]{agent};
        Squad squad = Squad.getInstance();
        squad.load(agents);
        LinkedList<String> serials = new LinkedList<>();
        serials.add("203333554");
        squad.releaseAgents(serials);
        assertTrue(agent.isAvailable(),"Sagi is not released");

    }

}
