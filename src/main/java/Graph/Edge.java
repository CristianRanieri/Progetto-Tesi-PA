package Graph;

public class Edge {
    public int node1;
    public int node2;
    public int weight;
    public int getNode1() {
        return node1;
    }

    public Edge(int node1, int node2, int weight) {
        this.node1 = node1;
        this.node2 = node2;
        this.weight = weight;
    }

    public void setNode1(int node1) {
        this.node1 = node1;
    }

    public int getNode2() {
        return node2;
    }

    public void setNode2(int node2) {
        this.node2 = node2;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "(" +
                "node1=" + node1 +
                ", node2=" + node2 +
                ", weight=" + weight +
                "), ";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return (node1 == edge.node1 && node2 == edge.node2) || (node1 == edge.node2 && node2 == edge.node1);
    }

}
