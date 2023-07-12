package security.smart.smartsecurity;

public enum Alarm {
    ALARM_1("1.", 0),
    ALARM_2("2.", 1),
    ALARM_3("3.", 2),
    ALARM_4("4.", 3);

    private final String label;
    private final int index;

    private Alarm(String label, int index) {
        this.label = label;
        this.index = index;
    }

    public String getLabel() {
        return label;
    }

    public int getIndex() {
        return index;
    }
}
