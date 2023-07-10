package security.smart.smartsecurity;

import java.util.ArrayList;
import java.util.List;

public class RemoteSystemStateMessage {
    private RemoteSystemStateInnerMessage S;

    public RemoteSystemStateInnerMessage getS() {
        return S;
    }

    public void setS(RemoteSystemStateInnerMessage s) {
        S = s;
    }

    public static class RemoteSystemStateInnerMessage {
        private List<Integer> A = new ArrayList<>();
        private int P;
        private int I;
        private double B;
        private String U;
        private int V;

        public List<Integer> getA() {
            return A;
        }

        public void setA(List<Integer> a) {
            A = a;
        }

        public int getP() {
            return P;
        }

        public void setP(int p) {
            P = p;
        }

        public int getI() {
            return I;
        }

        public void setI(int i) {
            I = i;
        }

        public double getB() {
            return B;
        }

        public void setB(double b) {
            B = b;
        }

        public String getU() {
            return U;
        }

        public void setU(String u) {
            U = u;
        }

        public int getV() {
            return V;
        }

        public void setV(int v) {
            V = v;
        }

        public RemoteSystemState toRemoteSystemResponse() {
            RemoteSystemState response = new RemoteSystemState();
            response.setBalance(getB());
            response.setPower((getP() == 1));
            response.setIntruder(getI() == 1);
            response.setVolume(getV());
            response.setUser(getU());

            List<Boolean> alarmStatus = new ArrayList<>();
            for (int status: getA()) {
                alarmStatus.add(status ==1);
            }
            response.setAlarmStatus(alarmStatus);
            return response;
        }
    }
}
