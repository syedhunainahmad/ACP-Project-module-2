class Product {
    private String name;
    private String id;
    private String price;
    private String status;

    public Product(String name, String id, String price, String status) {
        this.name = name;
        this.id = id;
        this.price = price;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getPrice() {
        return price;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return name + "," + id + "," + price + "," + status;
    }
}
