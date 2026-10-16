package edu.hanover.sdp2026;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LocationTest {

    @Test
    void fromCoordinatesAssertsPropperRowAndColValues() {
        assertEquals(1,Location.fromCoordinates(1,1).col());
        assertEquals(1,Location.fromCoordinates(1,1).row());
    }

    @Test
    void fromStringAssertsPropperRowAndColValues() {
        assertEquals(4,Location.fromString("d1").col());
        assertEquals(1,Location.fromString("d1").row());

    }


    @Test
    void getStringEquivalent(){
        assertEquals("a1", Location.fromString("a1").getStringEquivalent());
    }

    @Test
    void colExistsAsExpected() {
    }

    @Test
    void rowExistsAsExpected() {
    }
}