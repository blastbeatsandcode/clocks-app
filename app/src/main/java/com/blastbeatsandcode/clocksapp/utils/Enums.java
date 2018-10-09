package com.blastbeatsandcode.clocksapp.utils;

public class Enums {
    public enum Hour {
        ONE(1), TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8), NINE(9), TEN(10), ELEVEN(11), TWELVE(12);

        private int value;

        public int getValue()
        {
            return this.value;
        }

        private Hour(int value)
        {
            this.value = value;
        }
    }
}
