package net.smileycorp.magiadaemonica.common.capabilities;

public interface Petrified {

    void setPetrified();

    boolean isPetrified();

    void tick();

    class Impl implements Petrified {

        private int petrifyTicks;

        @Override
        public void setPetrified() {
            petrifyTicks = 40;
        }

        @Override
        public boolean isPetrified() {
            return petrifyTicks > 0;
        }

        @Override
        public void tick() {
            petrifyTicks--;
        }

    }

}
