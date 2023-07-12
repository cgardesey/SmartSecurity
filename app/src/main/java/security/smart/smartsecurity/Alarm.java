package security.smart.smartsecurity;

public enum Alarm {
    ALARM_1("1.", 0, R.id.alarm1),
    ALARM_2("2.", 1, R.id.alarm2),
    ALARM_3("3.", 2, R.id.alarm3),
    ALARM_4("4.", 3, R.id.alarm4);

    private final String label;
    private final int index;

    private final int viewId;

    private Alarm(String label, int index, int viewId) {
        this.label = label;
        this.index = index;
        this.viewId = viewId;
    }

    public String getLabel() {
        return label;
    }

    public int getIndex() {
        return index;
    }

    public int getViewId() {
        return viewId;
    }
}
