import java.util.Objects;

public class Visitor implements Comparable<Visitor> {
    private final String ip;
    private int visits;

    public Visitor(String ip) {
        this.ip = ip;
        this.visits = 1;
    }

    public int getVisits() {
        return visits;
    }

    public void setVisits(int visits) {
        this.visits = visits;
    }

    public String getIp() {
        return ip;
    }

    @Override
    public int compareTo(Visitor other) {
        if (this.visits == other.visits) {
            return 0;
        } else if (this.visits > other.visits) {
            return 1;
        } else {
            return -1;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Visitor visitor = (Visitor) o;
        return Objects.equals(ip, visitor.ip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ip);
    }
}
