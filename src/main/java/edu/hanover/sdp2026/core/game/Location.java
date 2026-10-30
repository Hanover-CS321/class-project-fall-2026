package edu.hanover.sdp2026.core.game;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class Location {
    private final int col;
    private final int row;


    private Location(int col, int row) {
        this.row = row;
        this.col = col;
    }

    public static Location fromCoordinates(int col, int row) {
        throwIfNotInInputRange(col, "input outside of 1-8 range");
        throwIfNotInInputRange(row, "input outside of 1-8 range");
        return new Location(col, row);
    }

    public static Location fromString(String string) {
        if (string.length() != 2)
            throw new InvalidLocationException("only String of length = 2 is accepted");

        char colLetter = string.charAt(0);
        int row = string.charAt(1) - '0';

        return fromCoordinates(convertCharToCoordinate(colLetter), row);
    }

    public String getStringEquivalent() {
        return convertCoordinateToString(col) + row;
    }

    private static String convertCoordinateToString(int num) {
        throwIfNotInInputRange(num, "number outside of 1-8 range");
        return String.valueOf((char) ('a' + num - 1));
    }

    private static int convertCharToCoordinate(char letter) {
        int num = letter - 'a' + 1;
        throwIfNotInInputRange(num, "letter corresponds to number outside of 1-8 range");
        return num;
    }

    private static void throwIfNotInInputRange(int num, String message) {
        if (!inRange(num)) throw new InvalidLocationException(message);
    }

    private static boolean inRange(int num) {
        return num >= 1 && num <= 8;
    }

    private static boolean inRange(int num1, int num2) {
        return (num1 >= 1 && num1 <= 8) && (num2 >= 1 && num2 <= 8);
    }

    public int col() {
        return col;
    }

    public int row() {
        return row;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Location) obj;
        return this.col == that.col &&
                this.row == that.row;
    }

    @Override
    public int hashCode() {
        return Objects.hash(col, row);
    }

    @Override
    public String toString() {
        return "Location[" +
                "col=" + col + ", " +
                "row=" + row + ']';
    }


    public static class InvalidLocationException extends RuntimeException {
        public InvalidLocationException(String message) {
            super(message);
        }
    }

    @Deprecated
    public Location add(Shift shift) {
        return new Location(
                col() + shift.x,
                row() + shift.y
        );
    }

    public Optional<Location> shift(Shift shift) {
        int newX = col() + shift.x;
        int newY = row() + shift.y;
        if(inRange(newX, newY)) return Optional.of(new Location(newX, newY));
        return Optional.empty();
    }

    public record Shift(int x, int y) {}

    public List<Location> getValidDestinations (List<Location.Shift> shifts) {
        return shifts.stream().map(this::shift)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }
}
