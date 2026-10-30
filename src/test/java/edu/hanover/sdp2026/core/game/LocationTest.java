package edu.hanover.sdp2026.core.game;

import org.junit.jupiter.api.Test;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class LocationTest {

    @Test
    void fromCoordinatesAssertsPropperRowAndColValues() {
        assertEquals(1, Location.fromCoordinates(1,1).col());
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

    @Test 
    void shiftMethodWithValidDestination() {
        Optional<Location> location = Location.fromCoordinates(1,1).shift(new Location.Shift(1,0));
        assertTrue(location.isPresent());
        assertEquals(2, location.get().col());
        assertEquals(1, location.get().row());
    }

    @Test
    void shiftMethodWithInvalidDestination() {
        Optional<Location> location = Location.fromCoordinates(1,1).shift(new Location.Shift(-1,0));
        assertTrue(location.isEmpty());
    }
}