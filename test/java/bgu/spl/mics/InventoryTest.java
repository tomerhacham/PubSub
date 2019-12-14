package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.Inventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InventoryTest {
    @BeforeEach
    public void setUp(){
        String[] gadgets = new String[]{"Sky Hook", "Geiger counter", "X-ray glasses", "Dagger shoe"};
        Inventory inventory =  Inventory.getInstance();
        inventory.load(gadgets);
    }

    @Test
    public void test(){
        Inventory InventoryInstance = Inventory.getInstance();
        assertNotNull(InventoryInstance,"Inventory is Null");
        assertSame(InventoryInstance,Inventory.getInstance(),"Inventories are not the same Object");
    }

    @Test
    public void getItemTest(){
        Inventory inventory = Inventory.getInstance();
        assertTrue(inventory.getItem("Sky Hook"), "Sky Hook is not in the inventory");
        assertFalse(inventory.getItem("Sky Hook"), "Sky Hook is in the inventory");
        assertTrue(inventory.getItem("Geiger counter"), "Geiger counter is not in the inventory");
        assertTrue(inventory.getItem("X-ray glasses"), "X-ray glasses is not in the inventory");
        assertTrue(inventory.getItem("Dagger shoe"), "Dagger shoe is not in the inventory");
        assertFalse(inventory.getItem("Bionic Coffee Maker"), "Bionic Coffee Maker is in the inventory");
    }

}
