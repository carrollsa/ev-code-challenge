import java.util.*;

public class IpTracker {
    private HashMap<String, Visitor> visitors;
    private PriorityQueue<Visitor> top_visitors;
    private static final int TOP_CAPACITY = 100;

    public IpTracker() {
        visitors = new HashMap<>();
        top_visitors = new PriorityQueue<>(100);
    }

    public void request_handled(String visitor_ip) {
        track_visit(visitor_ip);
        update_top_visitors(visitor_ip);
    }

    public List<String> top100() {
        List<String> top_ips = new ArrayList<>();
        for (Visitor visitor : top_visitors) {
            top_ips.add(visitor.getIp());
        }
        return top_ips;
    }

    public void clear() {
        visitors = new HashMap<>();
        top_visitors = new PriorityQueue<>();
    }

    private void track_visit(String visitor_ip) {
        if (! visitors.containsKey(visitor_ip)) {
            visitors.put(visitor_ip, new Visitor(visitor_ip));
        } else {
            increment_visits(visitor_ip);
        }
    }

    private void increment_visits(String visitor_ip) {
        Visitor visitor = visitors.get(visitor_ip);
        visitor.setVisits(visitor.getVisits() + 1);
        visitors.put(visitor_ip, visitor);
    }

    private void update_top_visitors(String visitor_ip) {
        Visitor visitor = visitors.get(visitor_ip);
        // Visitor is already one of the top100
        if (top_visitors.contains(visitor)) {
            // Feels hacky, but does update the visits value of the Visitor in the list and reorganizes the queue.
            top_visitors.remove(visitor);
            top_visitors.offer(visitor);
        }
        // Visitor is not already top 100, but there are fewer than 100 total visitors
        else if (top_visitors.size() < TOP_CAPACITY) {
            top_visitors.offer(visitor);
        }
        // See if visitor is valid to be a top visitor
        else {
            if (visitor.getVisits() > top_visitors.peek().getVisits()) {
                top_visitors.poll();
                top_visitors.offer(visitor);
            }
        }
    }

    public PriorityQueue<Visitor> getTop_visitors() {
        return top_visitors;
    }
}
