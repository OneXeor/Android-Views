package io.singulart.bottomnavigationbar;

public enum ButtonState {
    PRESSED(0),
    NORMAL(1);

    private int state;

    ButtonState(int i) {
        this.state = i;
    }

    public int getState() {
        return state;
    }
}
