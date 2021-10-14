package graph.junitTests;
import graph.*;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;

import java.util.HashSet;
import java.util.Set;
import java.lang.String;
import static org.junit.Assert.*;

public class GraphTest {

    // Graph Constructors!!
    //________________________________________
    public Graph<String, String> emptyGraph() {
        return new Graph<String, String>();
    }

    public Graph<String, String> oneNodeGraph() {
        // A
        Graph<String, String> g = new Graph<>();
        g.addNode("A");
        return g;
    }

    public Graph<String, String> twoNodesGraph() {
        // A     B
        Graph<String, String> g = new Graph<>();
        g.addNode("A");
        g.addNode("B");
        return g;
    }

    public Graph<String, String> twoNodesOneEdgeGraph() {
        // A -> B
        Graph<String, String> g = new Graph<>();
        g.addNode("A");
        g.addNode("B");
        g.addEdge("A", "B", "A -> B");
        return g;
    }

    public Graph<String, String> twoNodesTwoEdgesGraph() {
        // A <--> B
        Graph<String, String> g = new Graph<>();
        g.addNode("A");
        g.addNode("B");
        g.addEdge("A", "B", "A -> B");
        g.addEdge("B", "A", "B -> A");
        return g;
    }

    public Graph<String, String> threeNodesThreeEdgesGraph() {
        // A --> B --> C --> A (same A as at first)
        Graph<String, String> g = new Graph<>();
        g.addNode("A");
        g.addNode("B");
        g.addNode("C");
        g.addEdge("A", "B", "A -> B");
        g.addEdge("B", "C", "B -> C");
        g.addEdge("C", "A", "C -> A");
        return g;
    }

    public Graph<String, String> threeNodeTwoWayCycleGraph() {
        // A <--> B <--> C <--> A (same A as at first)
        Graph<String, String> g = threeNodesThreeEdgesGraph();
        g.addEdge("B", "A", "B -> A");
        g.addEdge("C", "B", "C -> B");
        g.addEdge("A", "C", "A -> B");
        return g;
    }


    // _________________________________________
    //          GRAPH TESTS
    // _________________________________________

    @Rule
    public Timeout globalTimeout = Timeout.seconds(10); // 10 seconds max per method tested

    @Test
    public void constructorsCheck() {
        Graph<String, String> a = emptyGraph();
        Graph<String, String> b = oneNodeGraph();
        Graph<String, String> c = twoNodesGraph();
        Graph<String, String> d = twoNodesOneEdgeGraph();
        Graph<String, String> e = twoNodesTwoEdgesGraph();
        Graph<String, String> f = threeNodeTwoWayCycleGraph();
        Graph<String, String> g = threeNodesThreeEdgesGraph();
    }


    @Test
    public void isEmptyCheck0N() {
        assertTrue(emptyGraph().isEmpty());
    }

    @Test
    public void isEmptyCheck1N0E() {
        assertFalse(oneNodeGraph().isEmpty());
    }

    @Test
    public void isEmptyCheck2N1E() {
        assertFalse(twoNodesGraph().isEmpty());
    }

    @Test
    public void containsEdgeCheck2N1E() {
        Graph<String, String> g = twoNodesOneEdgeGraph();
        assertTrue(g.containsEdge("A", "B", "A -> B"));
        assertFalse(g.containsEdge("A", "B", "C"));
        assertFalse(g.containsEdge("B", "A", "B -> A"));
    }

    @Test
    public void containsEdgeCheck2N0E() {
        Graph<String, String> g = twoNodesGraph();
        assertFalse(g.containsEdge("A", "B", "A -> B"));
        assertFalse(g.containsEdge("A", "B", "C"));
        assertFalse(g.containsEdge("B", "A", "B -> A"));
    }


    @Test
    public void containsEdgeCheck2N2E() {
        Graph<String, String> g = twoNodesTwoEdgesGraph();
        assertTrue(g.containsEdge("A", "B", "A -> B"));
        assertFalse(g.containsEdge("A", "B", "C"));
        assertTrue(g.containsEdge("B", "A", "B -> A"));
    }

    @Test
    public void containsEdgeNullCheck() {
        Graph<String, String> g = twoNodesTwoEdgesGraph();

        boolean errorThrown = false;
        try {
            g.containsEdge(null,  null, null);
        } catch (Exception IllegalArgumentException) {
            errorThrown = true;
        }
        assertTrue(errorThrown);
    }


    @Test
    public void containsNodeCheck2N1E() {
        Graph<String, String> g = twoNodesOneEdgeGraph();
        assertTrue(g.containsNode("A"));
        assertFalse(g.containsNode("C"));
        assertTrue(g.containsNode("B"));
    }

    @Test
    public void containsNodeCheck2N0E() {
        Graph<String, String> g = twoNodesGraph();
        assertTrue(g.containsNode("A"));
        assertFalse(g.containsNode("C"));
        assertTrue(g.containsNode("B"));
    }


    @Test
    public void containsNodeCheck2N2E() {
        Graph<String, String> g = twoNodesTwoEdgesGraph();
        assertTrue(g.containsNode("A"));
        assertFalse(g.containsNode("C"));
        assertTrue(g.containsNode("B"));
    }

    @Test
    public void containsNodeCheck3N3E() {
        Graph<String, String> g = threeNodesThreeEdgesGraph();
        assertTrue(g.containsNode("A"));
        assertTrue(g.containsNode("B"));
        assertTrue(g.containsNode("C"));
    }


    @Test
    public void allNodesCheck0N0E() {
        Graph<String, String> g = emptyGraph();
        assertTrue(g.allNodes().isEmpty());
    }


    @Test
    public void allNodesCheck2N1E() {
        Graph<String, String> g = twoNodesOneEdgeGraph();
        Set<String> nodes = new HashSet<>();
        nodes.add("A");
        nodes.add("B");

        assertEquals(2, g.allNodes().size());
        for (String node : g.allNodes()) {
            nodes.remove(node);
        }
        assertTrue(nodes.isEmpty());
    }

    @Test
    public void allNodesCheck2N0E() {
        Graph<String, String> g = twoNodesGraph();
        Set<String> nodes = new HashSet<>();
        nodes.add("A");
        nodes.add("B");

        assertEquals(2, g.allNodes().size());
        for (String node : g.allNodes()) {
            nodes.remove(node);
        }
        assertTrue(nodes.isEmpty());
    }


    @Test
    public void allNodesCheck2N2E() {
        Graph<String, String> g = twoNodesTwoEdgesGraph();
        Set<String> nodes = new HashSet<>();
        nodes.add("A");
        nodes.add("B");

        assertEquals(2, g.allNodes().size());
        for (String node : g.allNodes()) {
            nodes.remove(node);
        }
        assertTrue(nodes.isEmpty());
    }

    @Test
    public void allNodesCheck3N3E() {
        Graph<String, String> g = threeNodesThreeEdgesGraph();
        Set<String> nodes = new HashSet<>();
        nodes.add("A");
        nodes.add("B");
        nodes.add("C");

        assertEquals(3, g.allNodes().size());
        for (String node : g.allNodes()) {
            nodes.remove(node);
        }
        assertTrue(nodes.isEmpty());
    }


    @Test
    public void allEdgesCheck0N0E() {
        Graph<String, String> g = emptyGraph();
        assertTrue(g.allEdges().isEmpty());
    }


    @Test
    public void allEdgesCheck2N1E() {
        Graph<String, String> g = twoNodesOneEdgeGraph();
        Set<String> edges = new HashSet<>();
        edges.add("A -> B");

        assertEquals(1, g.allEdges().size());
        for (Graph<String, String>.Edge edge : g.allEdges()) {
            assertTrue(edges.contains(edge.getEdgeData()));
            edges.remove(edge.getEdgeData());
        }
        assertTrue(edges.isEmpty());
    }

    @Test
    public void allEdgesCheck2N0E() {
        Graph<String, String> g = twoNodesGraph();
        assertTrue(g.allEdges().isEmpty());

    }

    @Test
    public void allEdgesCheck2N2E() {
        Graph<String, String> g = twoNodesTwoEdgesGraph();
        Set<String> edges = new HashSet<>();
        edges.add("A -> B");
        edges.add("B -> A");

        assertEquals(2, g.allEdges().size());
        for (Graph<String, String>.Edge edge : g.allEdges()) {
            assertTrue(edges.contains(edge.getEdgeData()));
            edges.remove(edge.getEdgeData());
        }
        assertTrue(edges.isEmpty());
    }

    @Test
    public void allEdgesCheck3N3E() {
        Graph<String, String> g = threeNodesThreeEdgesGraph();
        Set<String> edges = new HashSet<>();
        edges.add("A -> B");
        edges.add("B -> C");
        edges.add("C -> A");

        assertEquals(3, g.allEdges().size());
        for (Graph<String, String>.Edge edge : g.allEdges()) {
            assertTrue(edges.contains(edge.getEdgeData()));
            edges.remove(edge.getEdgeData());
        }
        assertTrue(edges.isEmpty());
    }


    @Test
    public void containsNodeNullCheck() {
        Graph<String, String> g = twoNodesTwoEdgesGraph();
        boolean errorThrown = false;

        try {
            g.containsNode(null);
        } catch (Exception IllegalArgumentException) {
            errorThrown = true;
        }
        assertTrue(errorThrown);
    }


    @Test
    public void incomingEdgesNullCheck() {
        Graph<String, String> g = twoNodesTwoEdgesGraph();
        boolean errorThrown = false;
        try {
            g.incomingEdgesTo(null);
        } catch (Exception IllegalArgumentException) {
            errorThrown = true;
        }
        assertTrue(errorThrown);
    }


    @Test
    public void incomingEdgesCheck2N1E() {
        Graph<String, String> g = twoNodesOneEdgeGraph();
        assertTrue(g.incomingEdgesTo("A").isEmpty());
        assertEquals("A -> B", g.incomingEdgesTo("B").iterator().next().getEdgeData());
    }

    @Test
    public void incomingEdgesCheck2N2E() {
        Graph<String, String> g = twoNodesTwoEdgesGraph();
        assertEquals("B -> A", g.incomingEdgesTo("A").iterator().next().getEdgeData());
        assertEquals("A -> B", g.incomingEdgesTo("B").iterator().next().getEdgeData());
    }


    @Test
    public void incomingEdgesCheck3N3E() {
        Graph<String, String> g = threeNodesThreeEdgesGraph();
        assertEquals("C -> A", g.incomingEdgesTo("A").iterator().next().getEdgeData());
        assertEquals("A -> B", g.incomingEdgesTo("B").iterator().next().getEdgeData());
        assertEquals("B -> C", g.incomingEdgesTo("C").iterator().next().getEdgeData());
    }

    @Test
    public void incomingEdgesCheck2N5E() {
        Graph<String, String> g = twoNodesTwoEdgesGraph();
        g.addEdge("A", "B", "2nd A -> B");
        g.addEdge("A", "B", "3rd A -> B");
        g.addEdge("B", "A", "2nd B -> A");

        Set<String> correctB = new HashSet<>();
        correctB.add("A -> B");
        correctB.add("2nd A -> B");
        correctB.add("3rd A -> B");

        Set<String> correctA = new HashSet<>();
        correctA.add("B -> A");
        correctA.add("2nd B -> A");

        for (Graph<String, String>.Edge edge : g.incomingEdgesTo("A")) {
            assertTrue(correctA.contains(edge.getEdgeData()));
            correctA.remove(edge.getEdgeData());
        }
        assertTrue(correctA.isEmpty());

        for (Graph<String, String>.Edge edge : g.incomingEdgesTo("B")) {
            assertTrue(correctB.contains(edge.getEdgeData()));
            correctB.remove(edge.getEdgeData());
        }
        assertTrue(correctB.isEmpty());
    }

    @Test
    public void outgoingEdgesCheck2N1E() {
        Graph<String, String> g = twoNodesOneEdgeGraph();
        assertTrue(g.outgoingEdgesFrom("B").isEmpty());
        assertEquals("A -> B", g.outgoingEdgesFrom("A").iterator().next().getEdgeData());
    }

    @Test
    public void outgoingEdgesNullCheck() {
        Graph<String, String> g = twoNodesTwoEdgesGraph();
        boolean errorThrown = false;
        try {
            g.outgoingEdgesFrom(null);
        } catch (Exception IllegalArgumentException) {
            errorThrown = true;
        }
        assertTrue(errorThrown);
    }

    @Test
    public void outgoingEdgesCheck2N2E() {
        Graph<String, String> g = twoNodesTwoEdgesGraph();
        assertEquals("A -> B", g.outgoingEdgesFrom("A").iterator().next().getEdgeData());
        assertEquals("B -> A", g.outgoingEdgesFrom("B").iterator().next().getEdgeData());
    }

    @Test
    public void outgoingEdgesCheck3N3E() {
        Graph<String, String> g = threeNodesThreeEdgesGraph();
        assertEquals("A -> B", g.outgoingEdgesFrom("A").iterator().next().getEdgeData());
        assertEquals("B -> C", g.outgoingEdgesFrom("B").iterator().next().getEdgeData());
        assertEquals("C -> A", g.outgoingEdgesFrom("C").iterator().next().getEdgeData());
    }

    @Test
    public void outgoingEdgesCheck2N5E() {
        Graph<String, String> g = twoNodesTwoEdgesGraph();
        g.addEdge("A", "B", "2nd A -> B");
        g.addEdge("A", "B", "3rd A -> B");
        g.addEdge("B", "A", "2nd B -> A");

        Set<String> correctA = new HashSet<>();
        correctA.add("A -> B");
        correctA.add("3rd A -> B");
        correctA.add("2nd A -> B");

        Set<String> correctB = new HashSet<>();
        correctB.add("B -> A");
        correctB.add("2nd B -> A");

        assertEquals(3, g.outgoingEdgesFrom("A").size());
        for (Graph<String, String>.Edge edge : g.outgoingEdgesFrom("A")) {
            assertTrue(correctA.contains(edge.getEdgeData()));
            correctA.remove(edge.getEdgeData());
        }
        assertTrue(correctA.isEmpty());

        assertEquals(2, g.outgoingEdgesFrom("B").size());
        for (Graph<String, String>.Edge edge : g.outgoingEdgesFrom("B")) {
            assertTrue(correctB.contains(edge.getEdgeData()));
            correctB.remove(edge.getEdgeData());
        }
        assertTrue(correctB.isEmpty());
    }

    @Test
    public void getParentNullCheck() {
        Graph<String, String> g = twoNodesTwoEdgesGraph();
        boolean errorThrown = false;
        try {
            g.getParents(null);
        } catch (Exception IllegalArgumentException) {
            errorThrown = true;
        }
        assertTrue(errorThrown);
    }

    @Test
    public void getParents2N0E() {
        Graph<String, String> g = twoNodesGraph();
        assertTrue(g.getParents("A").isEmpty());
        assertTrue(g.getParents("B").isEmpty());
    }

    @Test
    public void getParents2N1E() {
        Graph<String, String> g = twoNodesOneEdgeGraph();
        assertTrue(g.getParents("A").isEmpty());
        assertTrue(g.getParents("B").iterator().next().equals("A")
                && g.getParents("B").size() == 1);
    }

    @Test
    public void getParents2N2E() {
        Graph<String, String> g = twoNodesTwoEdgesGraph();
        assertTrue(g.getParents("A").iterator().next().equals("B") &&
                g.getParents("A").size() == 1);
        assertTrue(g.getParents("B").iterator().next().equals("A") &&
                g.getParents("B").size() == 1);
    }

    @Test
    public void getParents3N3E() {
        Graph<String, String> g = threeNodesThreeEdgesGraph();
        assertTrue(g.getParents("A").iterator().next().equals("C") &&
                g.getParents("A").size() == 1);
        assertTrue(g.getParents("B").iterator().next().equals("A") &&
                g.getParents("B").size() == 1);
        assertTrue(g.getParents("C").iterator().next().equals("B") &&
                g.getParents("C").size() == 1);
    }

    @Test
    public void getParents3N6E() {
        Graph<String, String> g = threeNodeTwoWayCycleGraph();
        Set<String> A = new HashSet<>();
        Set<String> B = new HashSet<>();
        Set<String> C = new HashSet<>();

        A.add("C"); A.add("B");
        B.add("A"); B.add("C");
        C.add("A"); C.add("B");


        for (String node :  g.getParents("A")) {
            A.remove(node);
        }
        assertTrue(A.isEmpty());

        for (String node :  g.getParents("B")) {
            B.remove(node);
        }
        assertTrue(B.isEmpty());

        for (String node :  g.getParents("C")) {
            C.remove(node);
        }
        assertTrue(C.isEmpty());
    }


    @Test
    public void addEdgesCheck2N0E() {
        Graph<String, String> g = twoNodesGraph();
        assertTrue(g.allEdges().isEmpty());

        g.addEdge("A", "B", "A -> B");
        assertTrue(g.containsEdge("A", "B", "A -> B"));
        assertEquals(1, g.allEdges().size());

        g.addEdge("B", "A", "B -> A");
        assertTrue(g.containsEdge("B", "A", "B -> A"));
        assertEquals(2, g.allEdges().size());
    }


    @Test
    public void addEdgesCheck2N1E() {
        Graph<String, String> g = twoNodesOneEdgeGraph();
        assertEquals(1, g.allEdges().size());

        g.addEdge("A", "B", "2nd A -> B");
        assertTrue(g.containsEdge("A", "B", "A -> B"));
        assertTrue(g.containsEdge("A", "B", "2nd A -> B"));

        g.addEdge("B", "A", "1st B -> A");
        assertTrue(g.containsEdge("B", "A", "1st B -> A"));

        g.addEdge("B", "A", "2nd B -> A");
        assertTrue(g.containsEdge("B", "A", "2nd B -> A"));

        assertEquals(4, g.allEdges().size());
    }

    @Test
    public void addEdgesCheck3N3E() {
        Graph<String, String> g = threeNodesThreeEdgesGraph();
        assertEquals(3, g.allEdges().size());

        Set<String> edges = new HashSet<>();
        edges.add("A -> B");
        edges.add("2nd A -> B");
        edges.add("B -> C");
        edges.add("2nd B -> C");
        edges.add("C -> A");
        edges.add("2nd C -> A");

        g.addEdge("A", "B", "2nd A -> B");
        g.addEdge("B", "C", "2nd B -> C");
        g.addEdge("C", "A", "2nd C -> A");

        for (Graph<String, String>.Edge edge : g.allEdges()) {
            assertTrue(edges.contains(edge.getEdgeData()));
            edges.remove(edge.getEdgeData());
        }
    }


}
